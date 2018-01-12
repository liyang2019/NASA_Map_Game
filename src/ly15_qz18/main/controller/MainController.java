package ly15_qz18.main.controller;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import common.DataPacketUser;
import common.IChatRoom;
import common.IComponentFactory;
import common.IUser;
import common.IUserMessageType;
import common.datatype.user.IUserRejectionStatusType;
import ly15_qz18.main.model.IMainModel2CRMVCAdapter;
import ly15_qz18.main.model.IMainModel2MainViewAdapter;
import ly15_qz18.main.model.MainModel;
import ly15_qz18.main.model.cmd.IUserCmd2ModelCustomAdapter;
import ly15_qz18.main.model.data.UserRejectionStatusData;
import ly15_qz18.main.view.IInvitationView2MainModelAdapter;
import ly15_qz18.main.view.IMainView2MainModelAdapter;
import ly15_qz18.main.view.InvitationView;
import ly15_qz18.main.view.MainView;
import ly15_qz18.mini.controller.ChatRoomController;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;

/**
 * chat room controller.
 *
 */
public class MainController {

	private MainView<IUser, IChatRoom> view;
	private MainModel model;
	/**
	 * A map of chatRoom and chatRoomController.
	 */
	private Map<IChatRoom, ChatRoomController> chatRoomControllerMap = new HashMap<>();

	/**
	 * Constructor.
	 */
	public MainController() {

		view = new MainView<>(new IMainView2MainModelAdapter<IUser, IChatRoom>() {

			@Override
			public void startServer(String userName) {
				model.startServer(userName);
			}

			@Override
			public void connectToIP(String ipAddress) {
				model.connectToIP(ipAddress);
			}

			@Override
			public void requestChatRoomList(IUser user) {
				model.requestChatRooms(user);
			}

			@Override
			public void exit() {
				model.exit();
			}

			@Override
			public void joinChatRoom(IChatRoom chatRoom) {
				model.joinChatRoom(chatRoom);
			}

		});

		model = new MainModel(new IMainModel2MainViewAdapter() {

			@Override
			public void appendInfo(String text) {
				view.appendInfo(text);
			}

			@Override
			public void listChatRooms(Iterable<IChatRoom> chatRoomLIst) {
				view.listChatRooms(chatRoomLIst);
			}

			@Override
			public IMainModel2CRMVCAdapter createChatRoomMVC(IUser userStub, IChatRoom chatRoom, MixedDataDictionary dictionary) {
				ChatRoomController chatRoomController = new ChatRoomController(userStub, chatRoom, dictionary, model, view);
				chatRoomControllerMap.put(chatRoom, chatRoomController);
				chatRoomController.start();
				return new IMainModel2CRMVCAdapter() {
					@Override
					public void exitChatRoom() {
						chatRoomController.exitChatRoom();
					}
				};
			}

			@Override
			public void removeChatRoomMVC(IChatRoom chatRoom) {
				view.removeChatRoomView(chatRoom);
				chatRoomControllerMap.remove(chatRoom);
			}

			@Override
			public void selectChatRoom(IChatRoom chatRoom) {
				view.seletChatRoom(chatRoom);
			}

			@Override
			public void displayIP(String localAddress) {
				view.displayIP(localAddress);
			}

			@Override
			public void showInvitation(IChatRoom chatRoom, IUser sender) {
				(new InvitationView<IUser, IChatRoom>(
						sender, 
						chatRoom, 
						new IInvitationView2MainModelAdapter<IUser, IChatRoom>() {

							@Override
							public void acceptInvitation(IUser sender, IChatRoom chatRoom) {
								model.joinChatRoom(chatRoom);
							}

							@Override
							public void rejectInvitation(IUser sender, IChatRoom chatRoom) {
								try {
									sender.receive(
											new DataPacketUser<IUserRejectionStatusType>(
													IUserRejectionStatusType.class, 
													new UserRejectionStatusData(null, "refused to join the chat room: " + chatRoom), 
													model.getUser()));
								} catch (RemoteException e) {
									System.out.println("Failed to send the reject to join chat room info to: " + sender);
									e.printStackTrace();
								}
							}
						})).start();
			}

			@Override
			public void addUserToList(IUser userStub) {
				view.addUserToList(userStub);
			}
		}, 
				new IUserCmd2ModelCustomAdapter() {

			@Override
			public <T extends IUserMessageType> void sendTo(IUser target, Class<T> id, T data) {
				model.sendTo(target, id, data);
			}

			@Override
			public <T> T put(MixedDataKey<T> key, T value) {
				return model.put(key, value);
			}

			@Override
			public <T> T get(MixedDataKey<T> key) {
				return model.get(key);
			}

			@Override
			public void buildScrollableComponent(IComponentFactory fac, String label) {
				view.buildScrollableComponent(fac, label);
			}

			@Override
			public void buildNonScrollableComponent(IComponentFactory fac, String label) {
				view.buildNonScrollableComponent(fac, label);
			}

			@Override
			public void appendMsg(String text, String name) {
				model.appendMsg(text, name);
			}

			@Override
			public void manageInvitation(IChatRoom chatRoom, IUser sender) {
				model.manageInvitation(chatRoom, sender);
			}

			@Override
			public String getName() {
				IUser userStub = model.getUser();
				try {
					return model.getUser().getName();
				} catch (RemoteException e) {
					System.out.println("Falied to get user name of user: " + userStub);
					e.printStackTrace();
					return "can not get name";
				}
			}
		});
	}

	/**
	 * start the controller.
	 */
	public void start() {
		view.start();
		model.start();
	}

	/**
	 * @param args input arguments.
	 */
	public static void main(String[] args) {
		MainController controller = new MainController();
		controller.start();
	}
}
