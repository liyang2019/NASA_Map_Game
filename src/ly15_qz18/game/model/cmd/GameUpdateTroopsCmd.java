package ly15_qz18.game.model.cmd;

import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import ly15_qz18.game.model.type.IGameUpdateTroopsType;

/**
 * Game update troops command. To change the number of troops of a user in a team in a city
 * by the amount from getUpdateTroops();
 *
 */
public class GameUpdateTroopsCmd extends DataPacketCRAlgoCmd<IGameUpdateTroopsType> {
	
	private static final long serialVersionUID = 7527029885403976439L;
	private IGameCmd2ModelAdapter gameCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param gameCmd2ModelAdapter - the game command to model adapter.
	 */
	public GameUpdateTroopsCmd(IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<IGameUpdateTroopsType> host, String... params) {
		IGameUpdateTroopsType data = host.getData();
		UUID cityId = data.getCityId();
		UUID teamId = data.getTeamId();
		UUID userId = data.getUserId();
		int troops = data.getUpdateTroops();
		gameCmd2ModelAdapter.updateTroops(cityId, teamId, userId, troops);
		return "updated troops";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {}

}
