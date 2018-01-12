package ly15_qz18.model.object;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import common.DataPacketCR;
import common.ICRMessageType;
import common.IChatRoom;
import common.IReceiver;

/**
 * Abstract group class.
 *
 */
public abstract class AChatRoom implements IChatRoom {
	
	private static final long serialVersionUID = 8469471662500033355L;

	/**
	 * Group name.
	 */
	private final String groupName;
	
	/**
	 * The group's user(creator) name.
	 */
	private final String userName;
	
	/**
	 * Group's UUID.
	 */
	private final UUID uuid = UUID.randomUUID();
	
	/**
	 * The data packet receivers in this group.
	 */
	protected Set<IReceiver> receivers = new HashSet<IReceiver>();
	
	/**
	 * Constructor.
	 * @param groupName - the name of the group.
	 * @param userName - the group's user(creator) name.
	 */
	public AChatRoom(String groupName, String userName) {
		this.groupName = groupName;
		this.userName = userName;
	}
	
	@Override
	public String getName() {
		return groupName;
	}
	
	@Override
	public Collection<IReceiver> getIReceiverStubs() {
		return receivers;
	}
	
	@Override
	public <T extends ICRMessageType> void sendPacket(DataPacketCR<T> data) {
		IReceiver sender = data.getSender();
		for (IReceiver rcvr : receivers) {
			if (!sender.equals(rcvr)) {
				try {
					System.out.println(data + " sending to receiver: " + rcvr);
					rcvr.receive(data);
				} catch (RemoteException e) {
					System.out.println("Failed to send data packet to receiver: " + rcvr);
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Override
	public String toString() {
		return userName + "'s " + groupName;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public boolean addIReceiverStub(IReceiver receiver) {
		return receivers.add(receiver);
	}

	@Override
	public boolean removeIReceiverStub(IReceiver receiver) {
		return receivers.remove(receiver);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof IChatRoom)) return false;
		IChatRoom that = (IChatRoom) obj;
		return this.getUUID().equals(that.getUUID());
	}
	
	@Override
	public int hashCode() {
		return getUUID().hashCode();
	}

}
