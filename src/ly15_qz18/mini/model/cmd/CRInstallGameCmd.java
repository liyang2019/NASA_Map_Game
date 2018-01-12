package ly15_qz18.mini.model.cmd;

import java.awt.EventQueue;
import java.rmi.RemoteException;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import common.IUser;
import ly15_qz18.game.controller.GameController;
import ly15_qz18.mini.model.type.CRIInstallGameType;

/**
 * The command for installing a game.
 *
 */
public class CRInstallGameCmd extends DataPacketCRAlgoCmd<CRIInstallGameType> {

	private static final long serialVersionUID = 5777970167970583395L;
	/**
	 * Command to mode adapter.
	 */
	private transient ICRCmd2ModelAdapter crCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param crCmd2ModelAdapter - the command to model adapter.
	 */
	public CRInstallGameCmd(ICRCmd2ModelAdapter crCmd2ModelAdapter) {
		this.crCmd2ModelAdapter = crCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketCR<CRIInstallGameType> host, String... params) {
		CRIInstallGameType data = host.getData();
		IReceiver receiver = host.getSender();
		try {
			IUser gameCreator = receiver.getUserStub();
			// Java specs say that the system must be constructed on the GUI event thread.
			EventQueue.invokeLater(new Runnable() { 
				public void run() {
					GameController gameController = data.makeController(crCmd2ModelAdapter, gameCreator);
					gameController.startASync();
				}
			});
		} catch (RemoteException e) {
			System.out.println("Failed to get user stub from receiver: " + receiver);
			e.printStackTrace();
		}
		return "Game Started";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2ModelAdapter) {
		this.crCmd2ModelAdapter = crCmd2ModelAdapter;
	}
}
