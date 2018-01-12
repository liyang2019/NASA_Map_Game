package ly15_qz18.game.model.cmd;

import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IUser;
import ly15_qz18.game.model.type.IGameReadyType;
import ly15_qz18.model.object.Team;

/**
 * Game add team and user to city command.
 *
 */
public class GameReadyCmd extends DataPacketCRAlgoCmd<IGameReadyType> {
	
	private static final long serialVersionUID = -2492333298284272217L;
	private transient IGameCmd2ModelAdapter gameCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param gameCmd2ModelAdapter - game command to model adapter.
	 */
	public GameReadyCmd(IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<IGameReadyType> host, String... params) {
		IGameReadyType data = host.getData();
		Team team = data.getTeam();
		IUser user = data.getUser();
		UUID cityId = data.getCityId();
		gameCmd2ModelAdapter.setGameReady(team, user, cityId);
		return "add team and user to city";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {}

}
