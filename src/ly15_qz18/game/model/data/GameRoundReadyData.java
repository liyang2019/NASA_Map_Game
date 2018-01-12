package ly15_qz18.game.model.data;

import java.util.UUID;

import ly15_qz18.game.model.type.IGameRoundReadyType;

/**
 * Game ready status data.
 *
 */
public class GameRoundReadyData implements IGameRoundReadyType {

	private static final long serialVersionUID = 5411659702287518956L;
	private UUID userId;
	
	/**
	 * Constructor.
	 * @param userId - the user UUID.
	 */
	public GameRoundReadyData(UUID userId) {
		this.userId = userId;
	}
	
	@Override
	public UUID getUserId() {
		return userId;
	}

}
