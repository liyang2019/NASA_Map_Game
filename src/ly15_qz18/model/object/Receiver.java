package ly15_qz18.model.object;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketCR;
import common.ICRMessageType;
import common.IReceiver;
import common.IUser;
import provided.datapacket.DataPacketAlgo;

/**
 * The receiver for a chat room.
 *
 */
public class Receiver implements IReceiver, Serializable {
		
	private static final long serialVersionUID = -80343119221410606L;
	/**
	 * The userStub for this receiver.
	 */
	private IUser userStub;
	/**
	 * The uuid of this receiver.
	 */
	private UUID uuid = UUID.randomUUID();
	/**
	 * The algo to execute data packets the receiver received.
	 */
	private DataPacketAlgo<String,String> algo;
	
	/**
	 * Constructor.
	 * @param userStub is the userStub of this receiver.
	 */
	public Receiver(IUser userStub) {
		this.userStub = userStub;
	}

	@Override
	public <T extends ICRMessageType> void receive(DataPacketCR<T> data) throws RemoteException {
		data.execute(algo);
	}

	@Override
	public IUser getUserStub() throws RemoteException {
		return userStub;
	}

	@Override
	public UUID getUUID() throws RemoteException {
		return uuid;
	}
	
	/**
	 * Set the algo for the receiver.
	 * @param algo - the algo to execute data packets the receiver received.
	 */
	public void setAlgo(DataPacketAlgo<String,String> algo) {
		this.algo = algo;
	}
}
