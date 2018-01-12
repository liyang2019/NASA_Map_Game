package ly15_qz18.mini.model;

import java.io.File;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import common.DataPacketCR;
import common.ICRMessageType;
import common.IChatRoom;
import common.IReceiver;
import common.IUser;
import common.datatype.IRequestCmdType;
import common.datatype.chatroom.IAddReceiverType;
import common.datatype.chatroom.ICRInstallCmdType;
import common.datatype.chatroom.IRemoveReceiverType;
import ly15_qz18.mini.model.cmd.CRInstallGameCmd;
import ly15_qz18.mini.model.cmd.CRAddReceiverCmd;
import ly15_qz18.mini.model.cmd.CRDefaultCmd;
import ly15_qz18.mini.model.cmd.CRDisplayImageCmd;
import ly15_qz18.mini.model.cmd.CRDisplayTextCmd;
import ly15_qz18.mini.model.cmd.CRInstallCmdCmd;
import ly15_qz18.mini.model.cmd.ICRCmd2ModelCustomAdapter;
import ly15_qz18.mini.model.cmd.CRRemoveReceiverCmd;
import ly15_qz18.mini.model.cmd.CRRequestCmdCmd;
import ly15_qz18.mini.model.cmd.CRSaveFileCmd;
import ly15_qz18.mini.model.data.CRAddReceiverData;
import ly15_qz18.mini.model.data.CRInstallGameData;
import ly15_qz18.mini.model.data.CRRemoveReceiverData;
import ly15_qz18.model.data.FileData;
import ly15_qz18.model.data.ImageIconData;
import ly15_qz18.model.data.StringData;
import ly15_qz18.model.object.ProxyReceiver;
import ly15_qz18.model.object.ProxyUser;
import ly15_qz18.model.object.Receiver;
import provided.datapacket.DataPacketAlgo;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;

/**
 * The chat room model in the chat room mini MVC.
 *
 */
public class ChatRoomModel {
	
	/**
	 * Chat room model to chat room view adapter.
	 */
	private ICRModel2CRViewAdapter crModel2CRViewAdapter;
	/**
	 * Chat room model to main model adapter.
	 */
	protected ICRModel2MainModelAdapter crModel2MainModelAdapter;
	/**
	 * Chat room command to model adapter.
	 */
	protected ICRCmd2ModelCustomAdapter crCmd2ModelCustomAdapter;
	/**
	 * The chat room object for this chat room model.
	 */
	protected IChatRoom chatRoom;
	/**
	 * The receiver for this chat room, receives data packets.
	 */
	private Receiver receiver;
	/**
	 * The receiverStub for this chat room, receives data packets.
	 */
	protected IReceiver receiverStub;
	/**
	 * The user stub for the local user of this chat room.
	 */
	protected IUser userStub;
	/**
	 * The algo to execute data packets the receiver received.
	 */
	private DataPacketAlgo<String,String> algo;
	/**
	 * mixed data dictionary.
	 */
	MixedDataDictionary dictionary;
	
	/**
	 * Constructor.
	 * @param userStub - the user stub for the chat room.
	 * @param chatRoom - the chat room object for this chat room model.
	 * @param dictionary - mixed data dictionary.
	 * @param crModel2MainModelAdapter - the chat room model to main model adapter.
	 * @param crModel2CRViewAdapter - the chat room model to chat room view adapter.
	 * @param crCmd2ModelCustomAdapter - the customized chat room command to model adapter.
	 */
	public ChatRoomModel(
			IUser userStub,
			IChatRoom chatRoom, 
			MixedDataDictionary dictionary,
			ICRModel2CRViewAdapter crModel2CRViewAdapter, 
			ICRModel2MainModelAdapter crModel2MainModelAdapter,
			ICRCmd2ModelCustomAdapter crCmd2ModelCustomAdapter) {
		this.userStub = userStub;
		this.chatRoom = chatRoom;
		this.dictionary = dictionary;
		this.crModel2CRViewAdapter = crModel2CRViewAdapter;
		this.crModel2MainModelAdapter = crModel2MainModelAdapter;
		this.crCmd2ModelCustomAdapter = crCmd2ModelCustomAdapter;
		System.out.println("creating receiver from user: " + userStub);
		receiver = new Receiver(userStub);
		try {
			receiverStub = (IReceiver) UnicastRemoteObject.exportObject(receiver, 0);
			receiverStub = new ProxyReceiver(receiverStub, chatRoom.toString());
			crModel2MainModelAdapter.appendInfo("created receiver: " + receiverStub + "\n");
		} catch (RemoteException e) {
			System.out.println("fail to create a receiver stub for receiver: " + receiver);
			e.printStackTrace();
		}
		algo = new DataPacketAlgo<String,String>(new CRDefaultCmd(crCmd2ModelCustomAdapter, receiverStub));
		algo.setCmd(CRInstallGameData.class, new CRInstallGameCmd(crCmd2ModelCustomAdapter));
		algo.setCmd(IRemoveReceiverType.class, new CRRemoveReceiverCmd(crCmd2ModelCustomAdapter));
		algo.setCmd(IAddReceiverType.class, new CRAddReceiverCmd(crCmd2ModelCustomAdapter));
		algo.setCmd(IRequestCmdType.class, new CRRequestCmdCmd(crCmd2ModelCustomAdapter, receiverStub, algo));
		algo.setCmd(ICRInstallCmdType.class, new CRInstallCmdCmd(crCmd2ModelCustomAdapter, receiverStub, algo));
		algo.setCmd(StringData.class, new CRDisplayTextCmd(crCmd2ModelCustomAdapter));
		algo.setCmd(ImageIconData.class, new CRDisplayImageCmd(crCmd2ModelCustomAdapter));
		algo.setCmd(FileData.class, new CRSaveFileCmd(crCmd2ModelCustomAdapter));
		receiver.setAlgo(algo);
		addIReceiverToAll(receiverStub);
	}
	
	/**
	 * start the chat room model
	 */
	public void start() {
		listUsers();
	}

	/**
	 * User exit this chat room, doing:
	 * Send message to the chat room to tell remote receivers the quit information.
	 * Send remove receiver data packet to all other receivers in the chat room to remove the receiver.
	 * Tell the main model to remove the chat room in user and to remove the chat room mini MVC.
	 */
	public void exitChatRoom() {
		chatRoom.sendPacket(new DataPacketCR<StringData>(StringData.class, new StringData(receiverStub + " quit this chat room. "), receiverStub));
		removeIReceiverToAll(receiverStub);
		crModel2MainModelAdapter.removeChatRoom(chatRoom);
	}
	
	/**
	 * Add a receiver to this chat room. 
	 * The implementation should inform others in the chat room and then add the 
	 * receiver in the local chat room by calling addIReceiverStub .
	 * @param receiverStub the receiver to add
	 * @return False if the receiver is already in the chat room; True if successfully add the receiver
	 */
	public boolean addIReceiverToAll(IReceiver receiverStub) {
		chatRoom.sendPacket(new DataPacketCR<IAddReceiverType>(IAddReceiverType.class, new CRAddReceiverData(receiverStub), this.receiverStub));
		return chatRoom.addIReceiverStub(receiverStub);
	}
	
	/**
	 * Remove a receiver from this chat room. 
	 * The implementation should remove the receiver in the local chat room, unexport from RMI server, 
	 * and inform others in the chat room.
	 * @param receiverStub the receiver to remove
	 * @return False if the receiver is actually not in the chat room; True if successfully remove the receiver
	 */
	public boolean removeIReceiverToAll(IReceiver receiverStub) {
		chatRoom.sendPacket(new DataPacketCR<IRemoveReceiverType>(IRemoveReceiverType.class, new CRRemoveReceiverData(receiverStub), this.receiverStub));
		try {
			UnicastRemoteObject.unexportObject(receiverStub, false);
		} catch (NoSuchObjectException e) {
			System.out.println("failed to unexport the receiver stub to be remove.");
			e.printStackTrace();
		}
		return chatRoom.removeIReceiverStub(receiverStub);
	}
	
	/**
	 * Remove a receiverStub from this chat room locally.
	 * @param receiverStub is the receiverStub to remove from this chat room.
	 */
	public void removeReceiver(IReceiver receiverStub) {
		chatRoom.removeIReceiverStub(receiverStub);
		listUsers();
	}

	/**
	 * Add a receiverStub to this chat room locally.
	 * @param receiverStub is the receiverStub to add to this chat room.
	 */
	public void addReceiver(IReceiver receiverStub) {
		chatRoom.addIReceiverStub(receiverStub);
		listUsers();
	}

	/**
	 * Send text to the chat room in this chat room model.
	 * @param text is sent to the chat room in this chat room model.
	 */
	public void sendText(String text) {
		chatRoom.sendPacket(new DataPacketCR<StringData>(StringData.class, new StringData(text), receiverStub));
	}

	/**
	 * Send file to the chat room in this chat room model.
	 * @param file is sent to the chat room in this chat room model.
	 */
	public void sendFile(File file) {
		chatRoom.sendPacket(new DataPacketCR<FileData>(FileData.class, new FileData(file), receiverStub));
	}

	/**
	 * Send emoji to the chat room in this chat room model.
	 * @param emoji is sent to the chat room in this chat room model.
	 */
	public void sendEmoji(ImageIcon emoji) {
		chatRoom.sendPacket(new DataPacketCR<ImageIconData>(ImageIconData.class, new ImageIconData(emoji), receiverStub));
	}
	
	/**
	 * Refresh the user list to list users.
	 */
	public void listUsers() {
		List<IUser> users = new ArrayList<>();
		for (IReceiver rcvr : chatRoom.getIReceiverStubs()) {
			try {
				users.add(new ProxyUser(rcvr.getUserStub()));
			} catch (RemoteException e) {
				System.out.println("failed to get user stubs from receiver: " + rcvr);
				e.printStackTrace();
			}
		}
		crModel2CRViewAdapter.listUsers(users);
	}

	/**
	 * Get chat room object from chat room model.
	 * @return chat room object in chat room model.
	 */
	public IChatRoom getChatRoom() {
		return chatRoom;
	}

	/**
	 * Request chat rooms list a user created or joined.
	 * @param user The user.
	 */
	public void requestChatRoomList(IUser user) {
		crModel2MainModelAdapter.requestChatRoomList(user);
	}

	/**
	 * Send a data packet to a target recipient. 
	 * @param target the target recipient
	 * @param id the class id, i.e. message type
	 * @param data the data
	 */
	public <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data) {
		try {
			target.receive(new DataPacketCR<T>(id, data, receiverStub));
		} catch (RemoteException e) {
			System.out.println("Failed to send data to target: " + target);
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
	 * Send a data packet to the chat room corresponding with the receiver
	 * @param id the class id, i.e. message type
	 * @param data the data
	 */
	public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {
		DataPacketCR<T> dataPacket = new DataPacketCR<T>(id, data, receiverStub);
		chatRoom.sendPacket(dataPacket);
		try {
			receiverStub.receive(dataPacket);
		} catch (RemoteException e) {
			System.out.println("Falied to send data: " + data + " to self");
			e.printStackTrace();
		}
	}
}
