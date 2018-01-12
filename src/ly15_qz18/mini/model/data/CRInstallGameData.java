package ly15_qz18.mini.model.data;

import common.ICRCmd2ModelAdapter;
import common.IUser;
import ly15_qz18.game.controller.GameController;
import ly15_qz18.mini.model.type.CRIInstallGameType;

/**
 * Install game data.
 *
 */
public class CRInstallGameData implements CRIInstallGameType {

	private static final long serialVersionUID = -9171569411645097607L;
	private IUser userStub;
	
	/**
	 * Constructor.
	 * @param userStub - local user stub who the install game data packet sent to.
	 */
	public CRInstallGameData(IUser userStub) {
		this.userStub = userStub;
	}

	@Override
	public GameController makeController(ICRCmd2ModelAdapter crCmd2ModelAdapter, IUser gameCreator) {
		return new GameController(crCmd2ModelAdapter, userStub, gameCreator);
	}
}
