package ly15_qz18.main.view;

/**
 * Join chat room invitation view to main model adapter.
 *
 * @param <ChatRoomObj> - the chat room generic object.
 * @param <UserObj> - the user generic object. 
 */
public interface IInvitationView2MainModelAdapter<UserObj, ChatRoomObj> {

	/**
	 * Accept the invitation.
	 * @param sender - the sender of the invitation.
	 * @param chatRoom - the chat room of the invitation.
	 */
	void acceptInvitation(UserObj sender, ChatRoomObj chatRoom);

	/**
	 * Reject the invitation.
	 * @param sender - the sender of the invitation.
	 * @param chatRoom - the chat room of the invitation.
	 */
	void rejectInvitation(UserObj sender, ChatRoomObj chatRoom);

}
