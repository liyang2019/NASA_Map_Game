package ly15_qz18.main.model.data;

import common.IChatRoom;
import common.datatype.user.IInvitationType;

/**
 * The invite to join a chat room data.
 *
 */
public class UserInvitationData implements IInvitationType {

	private static final long serialVersionUID = 640788695948550490L;
	/**
	 * The chat room to invite a user to join.
	 */
	private IChatRoom chatRoom;
	
	/**
	 * Constructor.
	 * @param chatRoom - the chat room to invite a user to join.
	 */
	public UserInvitationData(IChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}
	
	@Override
	public IChatRoom getChatRoom() {
		return chatRoom;
	}

}
