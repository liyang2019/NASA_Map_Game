package ly15_qz18.game.model.type;

import java.io.Serializable;
import java.util.UUID;

import common.ICRMessageType;

/**
 * Game ready status type.
 *
 */
public interface IGameRoundReadyType extends ICRMessageType, Serializable {

	/**
	 * get the UUID of the user who is ready.
	 * @return the UUID of the user.
	 */
	public UUID getUserId();
	
}
