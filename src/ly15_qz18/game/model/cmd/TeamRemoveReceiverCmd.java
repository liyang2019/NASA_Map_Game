package ly15_qz18.game.model.cmd;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import common.datatype.chatroom.IRemoveReceiverType;

/**
 * Team remove receiver command.
 *
 */
public class TeamRemoveReceiverCmd extends DataPacketCRAlgoCmd<IRemoveReceiverType> {

	private static final long serialVersionUID = 2865150713104685541L;
	private transient IGameCmd2ModelAdapter gameCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param gameCmd2ModelAdapter - game command to model adapter.
	 */
	public TeamRemoveReceiverCmd(IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketCR<IRemoveReceiverType> host, String... params) {
		IReceiver receiver = host.getData().getReceiverStub();
		gameCmd2ModelAdapter.removeTeamReceiver(receiver);
		return "receiver: " + receiver + " removed";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {}

	
	
}
