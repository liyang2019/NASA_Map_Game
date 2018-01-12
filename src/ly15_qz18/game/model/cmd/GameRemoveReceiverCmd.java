package ly15_qz18.game.model.cmd;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import common.datatype.chatroom.IRemoveReceiverType;

/**
 * Game remove receiver command.
 *
 */
public class GameRemoveReceiverCmd extends DataPacketCRAlgoCmd<IRemoveReceiverType> {

	private static final long serialVersionUID = 520995975794080687L;
	private transient IGameCmd2ModelAdapter gameCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param gameCmd2ModelAdapter - game command to model adapter.
	 */
	public GameRemoveReceiverCmd(IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketCR<IRemoveReceiverType> host, String... params) {
		IReceiver receiver = host.getData().getReceiverStub();
		gameCmd2ModelAdapter.removeGameReceiver(receiver);
		return "receiver: " + receiver + " removed";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter cmd2ModelAdpt) {}

	
	
}