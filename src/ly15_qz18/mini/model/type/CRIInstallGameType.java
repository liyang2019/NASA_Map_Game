package ly15_qz18.mini.model.type;

import java.io.Serializable;

import common.ICRCmd2ModelAdapter;
import common.ICRMessageType;
import common.IUser;
import ly15_qz18.game.controller.GameController;

/**
 * Data type for installing a game. This data type is send to all receivers in a lobby,
 * so it use ICRCmd2ModelAdapter to install the game for this specific lobby chat room.
 */
public interface CRIInstallGameType extends ICRMessageType, Serializable {
	
	/**
	 * Make a game controller.
	 * @param crCmd2ModelAdapter - the command to model adapter.
	 * @param gameCreator - the creator user of the game.
	 * @return the game controller.
	 */
	public GameController makeController(ICRCmd2ModelAdapter crCmd2ModelAdapter, IUser gameCreator);

}

