package ly15_qz18.game.model.cmd;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import common.datatype.chatroom.IAddReceiverType;

/**
 * Team add receiver command.
 *
 */
public class TeamAddReceiverCmd extends DataPacketCRAlgoCmd<IAddReceiverType> {

	private static final long serialVersionUID = -2147765638279502477L;
	private transient IGameCmd2ModelAdapter gameCmd2ModelAdapter;

	/**
	 * Constructor.
	 * @param gameCmd2ModelAdapter - game command to model adapter.
	 */
	public TeamAddReceiverCmd(IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<IAddReceiverType> host, String... params) {
		IReceiver receiver = host.getData().getReceiverStub();
		gameCmd2ModelAdapter.addTeamReceiver(receiver);
		return "receiver: " + receiver + " added";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2ModelAdpt) {}

}
