package ly15_qz18.game.model.cmd;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import common.datatype.chatroom.IAddReceiverType;

/**
 * Game add receiver command.
 *
 */
public class GameAddReceiverCmd extends DataPacketCRAlgoCmd<IAddReceiverType> {

	private static final long serialVersionUID = -9208236435950225141L;
	private transient IGameCmd2ModelAdapter gameCmd2ModelAdapter;

	/**
	 * Constructor.
	 * @param gameCmd2ModelAdapter - game command to model adapter.
	 */
	public GameAddReceiverCmd(IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<IAddReceiverType> host, String... params) {
		IReceiver receiver = host.getData().getReceiverStub();
		gameCmd2ModelAdapter.addGameReceiver(receiver);
		return "receiver: " + receiver + " added";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2ModelAdpt) {}

}