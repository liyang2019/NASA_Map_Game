package ly15_qz18.main.view;

/**
 * Main view to main model adapter.
 * @param <UserObj> the user generic object.
 * @param <ChatRoomObj> the chat room generic object.
 */
public interface IMainView2MainModelAdapter<UserObj, ChatRoomObj> {

	/**
	 * Start the server.
	 * @param userName The user name.
	 */
	void startServer(String userName);

	/**
	 * connect to an ip address.
	 * @param ipAddress the ip address to connect.
	 */
	void connectToIP(String ipAddress);

	/**
	 * Request chat rooms list a user created or joined.
	 * @param user The user.
	 */
	void requestChatRoomList(UserObj user);

	/**
	 * exit the chat room application.
	 */
	void exit();

	/**
	 * Join a chat room.
	 * @param chatRoom the chat room object to join.
	 */
	void joinChatRoom(ChatRoomObj chatRoom);

}
