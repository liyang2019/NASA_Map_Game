package ly15_qz18.main.model;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import common.DataPacketUser;
import common.IChatRoom;
import common.IUser;
import common.IUserMessageType;
import common.datatype.IRequestCmdType;
import common.datatype.user.IInvitationType;
import common.datatype.user.IUserInstallCmdType;
import common.datatype.user.IUserRejectionStatusType;
import ly15_qz18.main.model.cmd.IUserCmd2ModelCustomAdapter;
import ly15_qz18.main.model.cmd.UserDefaultCmd;
import ly15_qz18.main.model.cmd.UserInstallCmdCmd;
import ly15_qz18.main.model.cmd.UserInvitationCmd;
import ly15_qz18.main.model.cmd.UserRejectionStatusCmd;
import ly15_qz18.main.model.cmd.UserRequestCmdCmd;
import ly15_qz18.main.model.data.UserInvitationData;
import ly15_qz18.model.object.Lobby;
import ly15_qz18.model.object.ProxyUser;
import ly15_qz18.model.object.User;
import provided.datapacket.DataPacketAlgo;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;

/**
 * chat room model.
 *
 */
public class MainModel {
	
	private static final String LOCAL_USER_BIND_NAME = "USER";
	private static final String REMOTE_USER_BIND_NAME = "USER";
	
	private Consumer<String> outputCmd = s -> MainModel.this.mainModel2MainViewAdapter.appendInfo((s) + "\n");
	private IRMIUtils rmiUtils = new RMIUtils(outputCmd);
	private Registry localRegistry;
	/**
	 * Current local user object.
	 */
	private User user; 
	/**
	 * Current local user remote stub proxy object
	 */
	private IUser userStub;
	/**
	 * Main model to main view adapter.
	 */
	private IMainModel2MainViewAdapter mainModel2MainViewAdapter;
	/**
	 * Customized user command to model adapter.
	 */
	IUserCmd2ModelCustomAdapter userCmd2ModelCustomAdapter;
	/**
	 * The lobby for the game.
	 */
	private Lobby lobby;
	private List<IMainModel2CRMVCAdapter> chatRoomMVCAdapters = new ArrayList<IMainModel2CRMVCAdapter>();
	/**
	 * The algo to execute data packets the receiver received.
	 */
	private DataPacketAlgo<String,String> algo;
	/**
	 * Mixed data type dictionary.
	 */
	private MixedDataDictionary dictionary;
	
	/**
	 * Constructor.
	 * @param mainModel2MainViewAdapter - the local model to view adapter.
	 * @param userCmd2ModelCustomAdapter - the customized user command to model adapter. 
	 */
	public MainModel(
			IMainModel2MainViewAdapter mainModel2MainViewAdapter, 
			IUserCmd2ModelCustomAdapter userCmd2ModelCustomAdapter) {
		this.mainModel2MainViewAdapter = mainModel2MainViewAdapter;
		this.userCmd2ModelCustomAdapter = userCmd2ModelCustomAdapter;
	}

	/**
	 * Start the server. Create a user, export to user stub, bind the user stub to the register.
	 * @param userName The user name.
	 */
	public void startServer(String userName) {
		dictionary = new MixedDataDictionary();
		user = new User(userName, mainModel2MainViewAdapter);
		try {
			userStub = (IUser) UnicastRemoteObject.exportObject(user, 0);
			userStub = new ProxyUser(userStub); 
			localRegistry.rebind(LOCAL_USER_BIND_NAME, userStub);
			mainModel2MainViewAdapter.appendInfo("User: " + userStub + " logged in");
		} catch (Exception e) {
			System.err.println("Failed to created a user stub and bind to registry \n");
			e.printStackTrace();
		}
		algo = new DataPacketAlgo<String,String>(new UserDefaultCmd(userCmd2ModelCustomAdapter, userStub));
		algo.setCmd(IRequestCmdType.class, new UserRequestCmdCmd(userCmd2ModelCustomAdapter, userStub, algo));
		algo.setCmd(IUserInstallCmdType.class, new UserInstallCmdCmd(userCmd2ModelCustomAdapter, userStub, algo));
		algo.setCmd(IInvitationType.class, new UserInvitationCmd(userCmd2ModelCustomAdapter));
		algo.setCmd(IUserRejectionStatusType.class, new UserRejectionStatusCmd(userCmd2ModelCustomAdapter));
		user.setAlgo(algo);
		makeLobby("Defense AI game");
	}

	/**
	 * Connect to a IP. List all the users binded to the registry on that IP.
	 * @param ipAddress is the remote IP address.
	 */
	public void connectToIP(String ipAddress) {
		try {
			// get remote registry.
			mainModel2MainViewAdapter.appendInfo("Locating registry at " + ipAddress +"...\n");
			Registry remoteRegistry = rmiUtils.getRemoteRegistry(ipAddress);
			mainModel2MainViewAdapter.appendInfo("Found registry : " + remoteRegistry + "\n");
			IUser remoteUser = (IUser) remoteRegistry.lookup(REMOTE_USER_BIND_NAME);
			mainModel2MainViewAdapter.appendInfo("Found remote user stub: " + remoteUser + " on ip: " + ipAddress + "\n");
			userStub.connect(new ProxyUser(remoteUser));
			remoteUser.connect(userStub);
			remoteUser.receive(new DataPacketUser<IInvitationType>(IInvitationType.class, new UserInvitationData(lobby), userStub));
		} catch (Exception e) {
			mainModel2MainViewAdapter.appendInfo("Error connecting to " + ipAddress + ": " + e + "\n");
			e.printStackTrace();
		}
	}

	/**
	 * Request chat rooms list a user created or joined.
	 * @param user The user.
	 */
	public void requestChatRooms(IUser user) {
		try {
			mainModel2MainViewAdapter.listChatRooms(user.getChatRooms());
			mainModel2MainViewAdapter.appendInfo("requesting joined chat room list to user: " + user.getName() + "\n");
		} catch (Exception e) {
			System.out.println("Error listing chat rooms from user " + user + "\n");
			e.printStackTrace();
		}
	}

	/**
	 * Create a Lobby, then let the current user join this Lobby.
	 * Only one lobby is permitted.
	 * @param lobbyName - The name of the lobby.
	 */
	public void makeLobby(String lobbyName) {
		if (userStub == null) {
			mainModel2MainViewAdapter.appendInfo("Need to log in first.. \n");
			System.out.println("Need to log in first..");
			return;
		}
		if (lobby != null) {
			System.out.println("already created a lobby...");
			return;
		}
		lobby = new Lobby(lobbyName, userStub.toString());
		mainModel2MainViewAdapter.appendInfo("Successfully created alobby: " + lobby + "\n");
		joinChatRoom(lobby);
	}
	
	/**
	 * Given a chat room, let the current join the chat room. 
	 * Create a chat room mini MVC using this chat room.
	 * @param chatRoom is the chat room to join.
	 */
	public void joinChatRoom(IChatRoom chatRoom) {
		if (chatRoom == null) {
			System.out.println("no chat room selected");
			return;
		}
		try {
			// note here use user not userStub, which is crucial..
			if (addChatRoomToUser(chatRoom)) {
				chatRoomMVCAdapters.add(mainModel2MainViewAdapter.createChatRoomMVC(userStub, chatRoom, dictionary));
				mainModel2MainViewAdapter.appendInfo("Successfully joined the chat room: " + chatRoom + "\n");
			} else {
				mainModel2MainViewAdapter.selectChatRoom(chatRoom);
				mainModel2MainViewAdapter.appendInfo("Already has joined the chat room: " + chatRoom + "\n");
			}
		} catch (RemoteException e) {
			System.out.println("failed to join chat room. user: " + userStub + " chat room: " + chatRoom);
			e.printStackTrace();
		}
	}
		
	/**
	 * Exit a chat room, doing:
	 * Remove the chat room in current user. 
	 * Remove the chat room MVC.
	 * @param chatRoom is the chat room to remove.
	 */
	public void removeChatRoom(IChatRoom chatRoom) {
		try {
			user.exitChatRoom(chatRoom);
			mainModel2MainViewAdapter.removeChatRoomMVC(chatRoom);
		} catch (RemoteException e) {
			System.out.println("failed to exit chat room. user: " + userStub + " chat room: " + chatRoom);
			e.printStackTrace();
		}
	}
	
	/**
	 * start the model, start RMI.
	 */
	public void start() {
		mainModel2MainViewAdapter.appendInfo("starting RMI... \n");
		rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER);
		try {
			mainModel2MainViewAdapter.displayIP(rmiUtils.getLocalAddress());
		} catch (SocketException | UnknownHostException e) {
			System.out.println("failed to get local IP address.");
			e.printStackTrace();
		}
		mainModel2MainViewAdapter.appendInfo("Looking for registry... \n");
		localRegistry = rmiUtils.getLocalRegistry();
		mainModel2MainViewAdapter.appendInfo("Found registry: " + localRegistry + "\n");
	}
	
	/**
	 * exit the program.
	 */
	public void exit() {
		System.out.println("exiting all chat rooms");
		for (IMainModel2CRMVCAdapter chatRoomMVCAdapter : chatRoomMVCAdapters) {
			chatRoomMVCAdapter.exitChatRoom();
		}
		System.out.println("rmiUtils.stopRMI(): server is quitting");
		try {
			rmiUtils.stopRMI();
		}catch(Exception e) {
			System.err.println("rmiUtils.stopRMI(): Error when stopping server: "+ e);
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * Send data packet to target.
	 * @param target - the target to send data packet to.
	 * @param id - the data type.
	 * @param data - the data in the data packet.
	 */
	public <T extends IUserMessageType> void sendTo(IUser target, Class<T> id, T data) {
		try {
			target.receive(new DataPacketUser<T>(id, data, userStub));
		} catch (RemoteException e) {
			System.out.println("Failed to send data: " + data);
			e.printStackTrace();
		}
	}

	/**
	 * Put a value into the local data storage(IMixedDataDictionary), associated with the given key,
	 * replacing any value already in the dictionary that is already associated
	 * with the key.
	 * Note that the local data storage(IMixedDataDictionary) is designed to be per-application.
	 * 
	 * @param key   the key to use to find the value
	 * @param value  The value to associate with the key
	 * @return The previous value associated with the key or null if there was none. 
	 * @param <T>  The type of data being put in
	 */
	public <T> T put(MixedDataKey<T> key, T value) {
		return dictionary.put(key, value);
	}

	/**
	 * Get the value associated with the given key from the local data storage(IMixedDataDictionary)
	 * Note that the local data storage(IMixedDataDictionary) is designed to be per-application.
	 * @param key  the key to use
	 * @return The value associated with the key or null if there is no entry for the key.
	 * @param <T> The type of data being retrieved
	 */
	public <T> T get(MixedDataKey<T> key) {
		return dictionary.get(key);
	}

	/**
	 * Append message.
	 * @param text - the message to append.
	 * @param name - the name of the sender.
	 */
	public void appendMsg(String text, String name) {
		mainModel2MainViewAdapter.appendInfo(name + ": " + text);
	}

	/**
	 * Manage join chat room invitation.
	 * @param chatRoom - the chat room of the invitation.
	 * @param sender - the sender of the invitation.
	 */
	public void manageInvitation(IChatRoom chatRoom, IUser sender) {
		mainModel2MainViewAdapter.appendInfo(sender + " invited you to join the chat room: " + chatRoom);
		mainModel2MainViewAdapter.showInvitation(chatRoom, sender);
	}

	/**
	 * Get the user stub of main model.
	 * @return the user stub.
	 */
	public IUser getUser() {
		return userStub;
	}

	/**
	 * Add a chat room object to user.
	 * @param chatRoom - the chat room to add.
	 * @return return false if the chatRoom already in the user's chatRoom list.
	 * @throws RemoteException if a network error occurs.
	 */
	public boolean addChatRoomToUser(IChatRoom chatRoom) throws RemoteException {
		return user.joinChatRoom(chatRoom);
	}
}