package ly15_qz18.main.model.cmd;

import common.IChatRoom;
import common.IUser;
import common.IUserCmd2ModelAdapter;

/**
 * Customize user command to model adapter.
 *
 */
public interface IUserCmd2ModelCustomAdapter extends IUserCmd2ModelAdapter {

	/**
	 * Manage the invitation to join a chat room from a user.
	 * @param chatRoom - the chat room to join.
	 * @param sender - the user who sent the invitation.
	 */
	void manageInvitation(IChatRoom chatRoom, IUser sender);

}
