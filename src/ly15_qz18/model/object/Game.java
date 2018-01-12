package ly15_qz18.model.object;

import java.rmi.RemoteException;

import common.DataPacketCR;
import common.ICRMessageType;
import common.IReceiver;
import common.datatype.chatroom.IAddReceiverType;
import common.datatype.chatroom.IRemoveReceiverType;
import ly15_qz18.mini.model.data.CRAddReceiverData;

/**
 * Game.
 *
 */
public class Game extends AChatRoom {
	
	private static final long serialVersionUID = 6161418659057082279L;
	/**
	 * The server receiver for the game.
	 */
	private IReceiver serverReceiver;

	/**
	 * Constructor.
	 * @param gameName - the name of the game.
	 * @param userName - the game's creator's name.
	 */
	public Game(String gameName, String userName) {
		super(gameName, userName);
	}

	/**
	 * Set the server receiver stub for the game.
	 * @param receiverStub - the server receiver stub.
	 */
	public void setServerReceiver(IReceiver receiverStub) {
		serverReceiver = receiverStub;
	}
	/**
	 * Get the server receiver stub for the game.
	 * @return the server receiver stub.
	 */
	public IReceiver getServerReceiver() {
		return serverReceiver;
	}
	
	@Override
	public <T extends ICRMessageType> void sendPacket(DataPacketCR<T> dataPacket) {
		super.sendPacket(dataPacket);
		T data = dataPacket.getData();
		if ((data instanceof IAddReceiverType) || (data instanceof IRemoveReceiverType)) {
			try {
				System.out.println(data + " sending to server receiver: " + serverReceiver);
				serverReceiver.receive(dataPacket);
			} catch (RemoteException e) {
				System.out.println("Failed to send data packet to receiver: " + serverReceiver);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public boolean addIReceiverStub(IReceiver receiver) {
		if (receivers.contains(receiver)) {
			return false;
		}
		synchronized(this) {
			receivers.add(receiver);
			for (IReceiver rcvr : receivers) {
				if (!rcvr.equals(receiver)) {
					try {
						rcvr.receive(new DataPacketCR<IAddReceiverType>(
								IAddReceiverType.class, 
								new CRAddReceiverData(receiver), 
								serverReceiver));
						receiver.receive(new DataPacketCR<IAddReceiverType>(
								IAddReceiverType.class, 
								new CRAddReceiverData(rcvr), 
								serverReceiver));
					} catch (RemoteException e) {
						System.out.println("Falie to synchronize receivers");
						e.printStackTrace();
					}
				}
			}
		}
		return true;
	}
}
