package ly15_qz18.model.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

import common.DataPacketUser;
import common.IChatRoom;
import common.IUser;
import common.IUserMessageType;

/**
 * The proxy of IUser.
 *
 */
public class ProxyUser implements IUser, Serializable {

	private static final long serialVersionUID = -4680479802913331855L;
	private IUser user;
	private String userName; // to store the user name, no need get it remotely;
	private UUID uuid; // to store the user's uuid, no need get it remotely;
	
	/**
	 * Constructor.
	 * @param user is the user in proxy user.
	 */
	public ProxyUser(IUser user) {
		this.user = user;
		try {
			userName = user.getName(); // get the user name remotely.
			uuid = user.getUUID(); // get the user's uuid remotely.
		} catch (RemoteException e) {
			System.out.println("failed to get userName or UUID when constructing proxy user from: " + user);
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return userName;
	}

	@Override
	public Collection<IChatRoom> getChatRooms() throws RemoteException {
		return user.getChatRooms();
	}
	
	/**
	 * Overridden equals() method to compare UUID's
	 * @return  Equal if UUID's are equal.  False otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof IUser)) return false;
		IUser that = (IUser) obj;
		try {
			return this.uuid.equals(((IUser)that).getUUID());
		} catch (RemoteException e) {
			System.err.println("Failed to get other user's uuid, the user is: " + obj);
			e.printStackTrace();
		}
		return false;
	}		

	/**
	 * Overridden hashCode() method to create a hashCode from that is hashCode of the UUID since
	 * equality is based on equality of UUID.
	 * @return a hashCode of the UUID.	
	 */
	@Override
	public int hashCode(){
		return uuid.hashCode();
	}
	
	@Override
	public String toString() {
		return userName;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void connect(IUser userStub) throws RemoteException {
		user.connect(userStub);
	}

	@Override
	public <T extends IUserMessageType> void receive(DataPacketUser<T> data) throws RemoteException {
		user.receive(data);
	}

}