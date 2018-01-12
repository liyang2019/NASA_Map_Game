package ly15_qz18.main.model.cmd;

import common.DataPacketUser;
import common.DataPacketUserAlgoCmd;
import common.IUser;
import common.IUserCmd2ModelAdapter;
import common.datatype.user.IInvitationType;

/**
 * User invitation to join chat room command.
 *
 */
public class UserInvitationCmd extends DataPacketUserAlgoCmd<IInvitationType> {
	
	private static final long serialVersionUID = -4180788720735365728L;
	private transient IUserCmd2ModelCustomAdapter userCmd2ModelCustomAdapter;
	
	/**
	 * Constructor.
	 * @param userCmd2ModelCustomAdapter - the customized user command to model adapter.
	 */
	public UserInvitationCmd(IUserCmd2ModelCustomAdapter userCmd2ModelCustomAdapter) {
		this.userCmd2ModelCustomAdapter = userCmd2ModelCustomAdapter;
	}

	@Override
	public String apply(Class<?> index, DataPacketUser<IInvitationType> host, String... params) {
		IUser sender = host.getSender();
		IInvitationType data = host.getData();
		userCmd2ModelCustomAdapter.manageInvitation(data.getChatRoom(), sender);
		return "invited to chat room";
	}

	@Override
	public void setCmd2ModelAdpt(IUserCmd2ModelAdapter userCmd2ModelAdapter) {}
}
