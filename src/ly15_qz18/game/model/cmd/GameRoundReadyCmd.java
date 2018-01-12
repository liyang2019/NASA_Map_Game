package ly15_qz18.game.model.cmd;

import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import ly15_qz18.game.model.type.IGameRoundReadyType;

/**
 * Game user ready for next round cmd.
 *
 */
public class GameRoundReadyCmd extends DataPacketCRAlgoCmd<IGameRoundReadyType> {

	private static final long serialVersionUID = 7422482185825191711L;
	private IGameCmd2ModelAdapter gameCmd2ModelAdapter;
	
	/**
	 * @param gameCmd2ModelAdapter - game command to model adapter.
	 */
	public GameRoundReadyCmd(IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketCR<IGameRoundReadyType> host, String... params) {
		IGameRoundReadyType data = host.getData();
		UUID userId = data.getUserId();
		gameCmd2ModelAdapter.setRoundReady(userId);
		return "set user ready";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {}

}
