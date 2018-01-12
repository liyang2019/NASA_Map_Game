package ly15_qz18.main.model.cmd;

import java.rmi.RemoteException;

import common.DataPacketUser;
import common.DataPacketUserAlgoCmd;
import common.IUser;
import common.IUserCmd2ModelAdapter;
import common.datatype.user.IUserRejectionStatusType;

/**
 * The user reject data packet status command.
 *
 */
public class UserRejectionStatusCmd extends DataPacketUserAlgoCmd<IUserRejectionStatusType> {

	private static final long serialVersionUID = 4666172511333787128L;
	private transient IUserCmd2ModelAdapter userCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param userCmd2ModelAdapter - the user command to model adapter.
	 */
	public UserRejectionStatusCmd(IUserCmd2ModelAdapter userCmd2ModelAdapter) {
		this.userCmd2ModelAdapter = userCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketUser<IUserRejectionStatusType> host, String... params) {
		IUser sender = host.getSender();
		IUserRejectionStatusType data = host.getData();
		String senderName = "failed to get name";
		try {
			senderName = sender.getName();
		} catch (RemoteException e) {
			System.out.println("Failed to get sender name..");
			e.printStackTrace();
		}
		userCmd2ModelAdapter.appendMsg(data.getFailureInfo(), senderName);
		return "data packet: " + data.getOriginalData() + " rejected";
	}

	@Override
	public void setCmd2ModelAdpt(IUserCmd2ModelAdapter userCmd2ModelAdapter) {
		this.userCmd2ModelAdapter = userCmd2ModelAdapter;
	}

}
