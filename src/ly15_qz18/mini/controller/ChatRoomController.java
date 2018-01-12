package ly15_qz18.mini.controller;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.swing.ImageIcon;

import common.ICRMessageType;
import common.IChatRoom;
import common.IComponentFactory;
import common.IReceiver;
import common.IUser;
import ly15_qz18.main.model.MainModel;
import ly15_qz18.main.view.MainView;
import ly15_qz18.mini.model.ChatRoomModel;
import ly15_qz18.mini.model.ICRModel2CRViewAdapter;
import ly15_qz18.mini.model.ICRModel2MainModelAdapter;
import ly15_qz18.mini.model.LobbyModel;
import ly15_qz18.mini.model.cmd.ICRCmd2ModelCustomAdapter;
import ly15_qz18.mini.view.ChatRoomView;
import ly15_qz18.mini.view.ICRView2CRModelAdapter;
import ly15_qz18.mini.view.LobbyView;
import ly15_qz18.model.object.Lobby;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;

/**
 * Chat room mini controller.
 */
public class ChatRoomController {

	private ChatRoomView<IUser, IChatRoom> chatRoomView;
	private ChatRoomModel chatRoomModel;
	
	/**
	 * Constructor.
	 * @param userStub The userStub for this chat room.
	 * @param chatRoom The chat room object of this chat room.
	 * @param dictionary - The mixed data type dictionary.
	 * @param model The main chat room model. 
	 * @param view is the main view;
	 */
	public ChatRoomController(IUser userStub, IChatRoom chatRoom, MixedDataDictionary dictionary, MainModel model, MainView<IUser, IChatRoom> view) {
		
		ICRView2CRModelAdapter<IUser, IChatRoom> crView2CRModelAdapter = new ICRView2CRModelAdapter<IUser, IChatRoom>() {

			@Override
			public void exitChatRoom() {
				chatRoomModel.exitChatRoom();
			}

			@Override
			public void sendText(String text) {
				chatRoomModel.sendText(text);
			}

			@Override
			public void sendFile(File file) {
				chatRoomModel.sendFile(file);
			}

			@Override
			public void sendEmoji(ImageIcon img) {
				chatRoomModel.sendEmoji(img);

			}

			@Override
			public IChatRoom getChatRoom() {
				return chatRoomModel.getChatRoom();
			}

			@Override
			public void requestChatRoomList(IUser user) {
				chatRoomModel.requestChatRoomList(user);
			}

			@Override
			public void startGame(int numOfTeams, int teamCapacity) {
				if (chatRoomModel instanceof LobbyModel) {
					((LobbyModel) chatRoomModel).startGame(numOfTeams, teamCapacity);
				}
			}

		};
		
		ICRModel2CRViewAdapter crModel2CRViewAdapter = new ICRModel2CRViewAdapter() {

			@Override
			public void appendMessage(String text, String name) {
				chatRoomView.appendMessage(text, name);
			}

			@Override
			public void listUsers(Collection<IUser> users) {
				IUser[] array = new IUser[users.size()];
				users.toArray(array);
				chatRoomView.listUsers(array);
			}

			@Override
			public void displayImage(ImageIcon img) {
				chatRoomView.displayImageIcon(img);
			}

		};
		
		ICRModel2MainModelAdapter crModel2MainModelAdapter = new ICRModel2MainModelAdapter() {

			@Override
			public void removeChatRoom(IChatRoom chatRoom) {
				model.removeChatRoom(chatRoom);
			}

			@Override
			public void requestChatRoomList(IUser user) {
				model.requestChatRooms(user);
			}

			@Override
			public void appendInfo(String text) {
				view.appendInfo(text);
			}

			@Override
			public void joinChatRoom(IChatRoom chatRoom) {
				model.joinChatRoom(chatRoom);
			}

			@Override
			public void addChatRoomToUser(IChatRoom chatRoom) {
				try {
					model.addChatRoomToUser(chatRoom);
				} catch (RemoteException e) {
					System.out.println("Falied to add chat room to user: " + userStub);
					e.printStackTrace();
				}
			}

		};
		
		ICRCmd2ModelCustomAdapter crCmd2ModelCustomAdapter = new ICRCmd2ModelCustomAdapter() {

			@Override
			public <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data) {
				chatRoomModel.sendTo(target, id, data);
			}

			@Override
			public <T> T put(MixedDataKey<T> key, T value) {
				return dictionary.put(key, value);
			}

			@Override
			public <T> T get(MixedDataKey<T> key) {
				return dictionary.get(key);
			}

			@Override
			public void buildScrollableComponent(IComponentFactory fac, String label) {
				chatRoomView.buildScrollableComponent(fac, label);
			}

			@Override
			public void buildNonScrollableComponent(IComponentFactory fac, String label) {
				chatRoomView.buildNonScrollableComponent(fac, label);
			}

			@Override
			public void appendMsg(String text, String name) {
				chatRoomView.appendMessage(text, name);
			}

			@Override
			public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {
				chatRoomModel.sendToChatRoom(id, data);
			}

			@Override
			public void removeReceiver(IReceiver receiverStub) {
				chatRoomModel.removeReceiver(receiverStub);
			}

			@Override
			public void addReceiver(IReceiver receiverStub) {
				chatRoomModel.addReceiver(receiverStub);
			}

			@Override
			public String getName() {
				try {
					return userStub.getName();
				} catch (RemoteException e) {
					System.out.println("Failed to get user name of user: " + userStub);
					e.printStackTrace();
					return "can not get name";
				}
			}
		};
		
		if (chatRoom instanceof Lobby) {
			Lobby lobby = (Lobby) chatRoom;
			chatRoomView = new LobbyView<IUser, IChatRoom>(crView2CRModelAdapter, userStub + "|" + lobby.toString());
			chatRoomModel = new LobbyModel(
					userStub, 
					lobby,
					dictionary,
					crModel2CRViewAdapter,
					crModel2MainModelAdapter,
					crCmd2ModelCustomAdapter);
			
		} else {
			chatRoomView = new ChatRoomView<IUser, IChatRoom>(crView2CRModelAdapter, userStub + "|" + chatRoom.toString());
			chatRoomModel = new ChatRoomModel(
					userStub, 
					chatRoom,
					dictionary,
					crModel2CRViewAdapter,
					crModel2MainModelAdapter,
					crCmd2ModelCustomAdapter);
		}
		view.addChatRoomView(chatRoomView);
	}

	/**
	 * start the controller.
	 */
	public void start() {
		chatRoomView.start();
		chatRoomModel.start();
	}

	/**
	 * Exit the chat room, doing:
	 * Call the exit chat room method in chat room model. 
	 */
	public void exitChatRoom() {
		chatRoomModel.exitChatRoom();
	}
}