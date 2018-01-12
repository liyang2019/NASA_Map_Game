package ly15_qz18.game.model.cmd;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.datatype.chatroom.ICRRejectionStatusType;

/**
 * Game rejection status command.
 *
 */
public class GameRejectionStatusCmd extends DataPacketCRAlgoCmd<ICRRejectionStatusType>{
	
	private static final long serialVersionUID = 4186715379105856802L;
	private IGameCmd2ModelAdapter gameCmd2ModelCustomAdapter;
	
	/**
	 * Constructor.
	 * @param gameCmd2ModelCustomAdapter - game command to model adapter.
	 */
	public GameRejectionStatusCmd(IGameCmd2ModelAdapter gameCmd2ModelCustomAdapter) {
		this.gameCmd2ModelCustomAdapter = gameCmd2ModelCustomAdapter;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<ICRRejectionStatusType> host, String... params) {
		gameCmd2ModelCustomAdapter.appendMsg("team is full, please select another team", "System");
		return "team is full";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {}

}
