package ly15_qz18.game.model.cmd;

import java.rmi.RemoteException;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import ly15_qz18.model.type.IStringType;

/**
 * Team display text command.
 */
public class TeamDisplayTextCmd extends DataPacketCRAlgoCmd<IStringType>  {

	private static final long serialVersionUID = -3142925049705023888L;
	/**
	 * Command to chat room mode adapter.
	 */
	private transient IGameCmd2ModelAdapter gameCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param gameCmd2ModelAdapter - the game command to model adapter.
	 */
	public TeamDisplayTextCmd(IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketCR<IStringType> host, String... params) {
		String text = host.getData().getText();
		IReceiver sender = host.getSender();
		try {
			gameCmd2ModelAdapter.appendMsg(text, "[Team] " + sender.getUserStub().getName());
		} catch (RemoteException e) {
			System.out.println("Failed to get sender name with sender.getUserStub().getName()");
			e.printStackTrace();
		}
		return text;
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2ModelAdapter) {}

}
