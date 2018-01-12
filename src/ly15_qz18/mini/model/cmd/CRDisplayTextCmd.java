package ly15_qz18.mini.model.cmd;

import java.rmi.RemoteException;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import ly15_qz18.model.type.IStringType;

/**
 * The command to display the text in the data packet.
 *
 */
public class CRDisplayTextCmd extends DataPacketCRAlgoCmd<IStringType> {

	private static final long serialVersionUID = -4146905191178168531L;
	
	/**
	 * Command to chat room mode adapter.
	 */
	private transient ICRCmd2ModelAdapter crCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param crCmd2ModelAdapter is the command to chat room mode adapter.
	 */
	public CRDisplayTextCmd(ICRCmd2ModelAdapter crCmd2ModelAdapter) {
		this.crCmd2ModelAdapter = crCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketCR<IStringType> host, String... params) {
		String text = host.getData().getText();
		IReceiver sender = host.getSender();
		try {
			crCmd2ModelAdapter.appendMsg(text, sender.getUserStub().getName());
		} catch (RemoteException e) {
			System.out.println("Failed to get sender name with sender.getUserStub().getName()");
			e.printStackTrace();
		}
		return text;
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2ModelAdapter) {
		this.crCmd2ModelAdapter = crCmd2ModelAdapter;
	}

}
