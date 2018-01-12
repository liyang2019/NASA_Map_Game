package ly15_qz18.mini.model;

import common.IChatRoom;
import common.IUser;

/**
 * Chat room model to main model adapter.
 *
 */
public interface ICRModel2MainModelAdapter {

	/**
	 * Exit a chat room doing:
	 * Tell the main model to remove the chat room in user and to remove the chat room mini MVC.
	 * @param chatRoom is the chat room to remove.
	 */
	void removeChatRoom(IChatRoom chatRoom);

	/**
	 * Request chat rooms list a user created or joined.
	 * @param user The user.
	 */
	void requestChatRoomList(IUser user);

	/**
	 * Append info message to the main model.
	 * @param string - the info message to append.
	 */
	void appendInfo(String string);

	/**
	 * Join chat room.
	 * @param chatRoom - the chat room to join.
	 */
	void joinChatRoom(IChatRoom chatRoom);

	/**
	 * Add a chat room object to user.
	 * @param chatRoom - the chat room to add.
	 */
	void addChatRoomToUser(IChatRoom chatRoom);
}
