package ly15_qz18.main.model;

import common.IChatRoom;
import common.IUser;
import provided.mixedData.MixedDataDictionary;

/**
 * Main model to main view adapter.
 *
 */
public interface IMainModel2MainViewAdapter {

	/**
	 * Append text in the Info area of main view.
	 * @param text the text to append to the Info area of main view.
	 */
	void appendInfo(String text);

	/**
	 * List chat rooms in the main view.
	 * @param iterable the chat rooms to list.
	 */
	void listChatRooms(Iterable<IChatRoom> iterable);

	/**
	 * Create a chat room miniMVC. 
	 * @param userStub - the current user stub used to create the chat room mini MVC.
	 * @param chatRoom - the chat room object used to create the chat room mini MVC.
	 * @param dictionary - the mixed data type dictionary.
	 * @param chatRoomName - the name of the chat room.
	 * @return The main model to chat room mini MVC adapter.
	 */
	IMainModel2CRMVCAdapter createChatRoomMVC(IUser userStub, IChatRoom chatRoom, MixedDataDictionary dictionary);
	
	/**
	 * Remove a chat room miniMVC. 
	 * @param currentUser the current user stub used to remove the chat room mini MVC.
	 * @param chatRoom the chat room object used to remove the chat room mini MVC.
	 * @param chatRoomName the name of the chat room.
	 */
	void removeChatRoomMVC(IChatRoom chatRoom);

	/**
	 * High light an already join chat room in chat room view.
	 * @param chatRoom
	 */
	void selectChatRoom(IChatRoom chatRoom);

	/**
	 * Display IP address in connect to IP panel.
	 * @param localAddress
	 */
	void displayIP(String localAddress);

	/**
	 * Show a pop up window of the join chat room invitation.
	 * @param chatRoom - the chat room of the invitation.
	 * @param sender - the sender of the invitation.
	 */
	void showInvitation(IChatRoom chatRoom, IUser sender);

	/**
	 * Add a connected user to user list.
	 * @param userStub the user stub
	 */
	void addUserToList(IUser userStub);
}
