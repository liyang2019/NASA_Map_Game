package ly15_qz18.model.object;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import common.DataPacketUser;
import common.IChatRoom;
import common.IUser;
import common.IUserMessageType;
import ly15_qz18.main.model.IMainModel2MainViewAdapter;
import provided.datapacket.DataPacketAlgo;

/**
 * User
 */
public class User implements IUser {
	
	/**
	 * User name.
	 */
	private String userName;
	
	/**
	 * User's uuid.
	 */
	private UUID uuid = UUID.randomUUID();
	
	/**
	 * The chat rooms the user has joined.
	 */
	protected Set<IChatRoom> chatRooms = new HashSet<IChatRoom>();
	
	/**
	 * Main model to main view adapter.
	 */
	private transient IMainModel2MainViewAdapter _mainViewAdapter;
	
	/**
	 * The algo to execute data packets the receiver received.
	 */
	private DataPacketAlgo<String,String> algo;
	
	/**
	 * Constructor.
	 * @param userName - the name of the user.
	 * @param _mainViewAdapter - main model to main view adapter.
	 */
	public User(String userName, IMainModel2MainViewAdapter _mainViewAdapter) {
		this.userName = userName;
		this._mainViewAdapter = _mainViewAdapter;
	}
	
	@Override
	public String getName() throws RemoteException {
		return userName;
	}

	@Override
	public UUID getUUID() throws RemoteException {
		return uuid;
	}

	@Override
	public Collection<IChatRoom> getChatRooms() throws RemoteException {
		return chatRooms;
	}

	@Override
	public void connect(IUser userStub) throws RemoteException {
		_mainViewAdapter.addUserToList(userStub);
	}

	@Override
	public <T extends IUserMessageType> void receive(DataPacketUser<T> data) throws RemoteException {
		data.execute(algo);
	}
	
	/**
	 * User joins a chatRoom. This method only add the chatRoom into user's chatRoom set.
	 * @param chatRoom - the chatRoom the user want to join.
	 * @return return false if the chatRoom already in the user's chatRoom list.
	 * @throws RemoteException  if a network error occurs.
	 */
	public boolean joinChatRoom(IChatRoom chatRoom) throws RemoteException {
		return chatRooms.add(chatRoom);
	}

	/**
	 * User exits a chatRoom. This method only remove the chatRoom into user's chatRoom set.
	 * @param chatRoom - the chatRoom the user want to exit.
	 * @return return false if the chatRoom not in the user's chatRoom list.
	 * @throws RemoteException if a network error occurs.
	 */	
	public boolean exitChatRoom(IChatRoom chatRoom) throws RemoteException {
		return chatRooms.remove(chatRoom);
	}
	
	/**
	 * Set the algo for the user.
	 * @param algo - the algo to execute data packets the receiver received.
	 */
	public void setAlgo(DataPacketAlgo<String,String> algo) {
		this.algo = algo;
	}
}
