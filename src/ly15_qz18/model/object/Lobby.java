package ly15_qz18.model.object;

import common.DataPacketCR;
import common.ICRMessageType;

/**
 * Lobby.
 */
public class Lobby extends AChatRoom {

	private static final long serialVersionUID = 2096572937906726262L;

	/**
	 * Constructor.
	 * @param lobbyName is the name of the lobby of the game.
	 * @param userName is the game lobby's user(creator) name.
	 */
	public Lobby(String lobbyName, String userName) {
		super(lobbyName, userName);
	}

	/**
	 * When users in this lobby in a game, send the data packet to all users
	 * and the data packet is for the game process instead of for chat rooms.
	 * @param dataPacket - game data packet.
	 */
	public <T extends ICRMessageType> void sendGamePacket(DataPacketCR<T> dataPacket) {
		// TODO Auto-generated method stub
	}
}
