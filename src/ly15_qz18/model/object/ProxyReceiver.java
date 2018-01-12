package ly15_qz18.model.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketCR;
import common.ICRMessageType;
import common.IReceiver;
import common.IUser;

/**
 * Receiver remote stub proxy object.
 *
 */
public class ProxyReceiver implements IReceiver, Serializable {
	

	private static final long serialVersionUID = 6295382999572277390L;
	private IReceiver receiver;
	private UUID uuid; // // to store the receiver's uuid, no need get it remotely;
	private String userName; // to store the user name of receiver's user, no need get it remotely;
	private String chatRoomName; // to store the chat room name for toSting();
	
	/**
	 * Constructor.
	 * @param receiver a receiver remote stub.
	 * @param chatRoomName is the chat room name of this receiver.
	 */
	public ProxyReceiver(IReceiver receiver, String chatRoomName) {
		this.receiver = receiver;
		try {
			this.userName = receiver.getUserStub().getName();
		} catch (RemoteException e) {
			System.out.println("Failed to get user name of receiver: " + receiver);
			e.printStackTrace();
		}
		try {
			this.uuid = receiver.getUUID();
		} catch (RemoteException e) {
			System.out.println("Failed to get UUID of receiver: " + receiver);
			e.printStackTrace();
		}
		this.chatRoomName = chatRoomName;
	}

	@Override
	public <T extends ICRMessageType> void receive(DataPacketCR<T> data) throws RemoteException {
		receiver.receive(data);
	}

	@Override
	public IUser getUserStub() throws RemoteException {
		return receiver.getUserStub();
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * Overridden equals() method to compare UUID's
	 * @return  Equal if UUID's are equal.  False otherwise.
	 */
	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (!(obj instanceof IReceiver)) return false;
		IReceiver that = (IReceiver) obj;
		try {
			return this.uuid.equals(that.getUUID());
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
		return "receiver of [user: " + userName + ", chat room: " + chatRoomName + "]"; 
	}
}
