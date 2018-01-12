package ly15_qz18.model.object;

import java.awt.Color;
import java.rmi.RemoteException;

import common.DataPacketCR;
import common.ICRMessageType;
import common.IReceiver;
import common.datatype.chatroom.IAddReceiverType;
import common.datatype.chatroom.IRemoveReceiverType;
import ly15_qz18.mini.model.data.CRAddReceiverData;
import ly15_qz18.mini.model.data.CRRejectionStatusData;

/**
 * Team.
 */
public class Team extends AChatRoom {

	private static final long serialVersionUID = 316379380377777840L;
	
	/**
	 * The server receiver for the team.
	 */
	private IReceiver serverReceiver;
	
	/**
	 * The max team capacity.
	 */
	private int teamCapacity;
	
	/**
	 * Team color.
	 */
	private Color color;
	
	/**
	 * Constructor.
	 * @param teamName is the name of the team in the game.
	 * @param userName is the team's user(creator) name.
	 * @param teamCapacity - the max capacity of this team.
	 */
	public Team(String teamName, String userName, int teamCapacity) {
		this(teamName, userName, teamCapacity, Color.WHITE);
	}

	/**
	 * Constructor.
	 * @param teamName is the name of the team in the game.
	 * @param userName is the team's user(creator) name.
	 * @param teamCapacity - the max capacity of this team.
	 * @param color - the color of the team.
	 */
	public Team(String teamName, String userName, int teamCapacity, Color color) {
		super(teamName, userName);
		this.teamCapacity = teamCapacity;
		this.color = color;
	}

	/**
	 * Set the server receiver stub for the team.
	 * @param receiverStub - the server receiver stub.
	 */
	public void setServerReceiver(IReceiver receiverStub) {
		serverReceiver = receiverStub;
	}
	/**
	 * Get the server receiver stub for the team.
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
		if (!isFull()) {
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
		try {
			receiver.receive(new DataPacketCR<CRRejectionStatusData>(
					CRRejectionStatusData.class, 
					new CRRejectionStatusData(null, "team is full, can not add more receiver"), serverReceiver));
		} catch (RemoteException e) {
			System.out.println("Falied to send add receiver rejection data packet to receiver: " + receiver);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Check if the team is full.
	 * @return true if the team is full.
	 */
	public boolean isFull() {
		return receivers.size() == teamCapacity;
	}
	
	/**
	 * Set the color of the team.
	 * @param newColor - new color of the team.
	 * @return previous color of the team.
	 */
	public Color setTeamColor(Color newColor) {
		Color prevColor = color;
		color = newColor;
		return prevColor;
	}
	
	/**
	 * Get the color of the team.
	 * @return the color of the team.
	 */
	public Color getTeamColor() {
		return color;
	}
}
