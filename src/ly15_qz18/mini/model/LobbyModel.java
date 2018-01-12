package ly15_qz18.mini.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import common.DataPacketCR;
import common.ICRMessageType;
import common.IComponentFactory;
import common.IReceiver;
import common.IUser;
import common.datatype.chatroom.IAddReceiverType;
import common.datatype.chatroom.IRemoveReceiverType;
import ly15_qz18.mini.model.cmd.CRAddReceiverCmd;
import ly15_qz18.mini.model.cmd.CRRemoveReceiverCmd;
import ly15_qz18.mini.model.cmd.ICRCmd2ModelCustomAdapter;
import ly15_qz18.mini.model.data.CRInstallGameData;
import ly15_qz18.model.object.Game;
import ly15_qz18.model.object.Lobby;
import ly15_qz18.model.object.ProxyReceiver;
import ly15_qz18.model.object.ProxyUser;
import ly15_qz18.model.object.Receiver;
import ly15_qz18.model.object.Team;
import provided.datapacket.DataPacketAlgo;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
import util.Randomizer;

/**
 * Lobby model.
 *
 */
public class LobbyModel extends ChatRoomModel {

	/**
	 * The team server name.
	 */
	private static final String TEAM_SERVER_NAME = "Team Server";
	/**
	 * The game server name.
	 */
	private static final String GAME_SERVER_NAME = "Game Server";
	/**
	 * The team server receiver objects for the predefined teams. 
	 * This is used to avoid garbage collection.
	 */
	private List<Receiver> serverReceivers;
	/**
	 * The game server object.
	 * This is used to avoid garbage collection.
	 */
	private Receiver gameReceiver;

	/**
	 * Constructor.
	 * @param userStub - the user stub for the local user of the lobby chat room.
	 * @param lobby - the lobby chat room.
	 * @param dictionary - mixed data dictionary.
	 * @param crModel2CRViewAdapter - chat room model to chat room view adapter.
	 * @param crModel2MainModelAdapter - chat room model to main model adapter.
	 * @param crCmd2ModelCustomAdapter - chat room command to model custom adapter.
	 */
	public LobbyModel(
			IUser userStub, 
			Lobby lobby, 
			MixedDataDictionary dictionary,
			ICRModel2CRViewAdapter crModel2CRViewAdapter, 
			ICRModel2MainModelAdapter crModel2MainModelAdapter,
			ICRCmd2ModelCustomAdapter crCmd2ModelCustomAdapter) {
		super(userStub, lobby, dictionary, crModel2CRViewAdapter, crModel2MainModelAdapter, crCmd2ModelCustomAdapter);
	}

	/**
	 * Start the game, send the game install data packet to every receivers in this chat room, including self.
	 * @param numOfTeams - number of teams.
	 * @param teamCapacity - max team capacity.
	 */
	public void startGame(int numOfTeams, int teamCapacity) {
		// initialize game server receiver object.
		Game game = new Game("game", GAME_SERVER_NAME);
		gameReceiver = new Receiver(userStub);
		// initialize server command to model adapter.
		ICRCmd2ModelCustomAdapter gameCmd2ModelCustomAdapter = new ICRCmd2ModelCustomAdapter() {

			@Override
			public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {}

			@Override
			public String getName() {
				return GAME_SERVER_NAME;
			}

			@Override
			public void appendMsg(String text, String name) {}

			@Override
			public void buildScrollableComponent(IComponentFactory fac, String label) {}

			@Override
			public void buildNonScrollableComponent(IComponentFactory fac, String label) {}

			@Override
			public <T> T put(MixedDataKey<T> key, T value) {
				return null;
			}

			@Override
			public <T> T get(MixedDataKey<T> key) {
				return null;
			}

			@Override
			public <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data) {}

			@Override
			public void addReceiver(IReceiver receiver) {
				game.addIReceiverStub(receiver);
			}

			@Override
			public void removeReceiver(IReceiver receiver) {
				game.removeIReceiverStub(receiver);
			}

		};
		// set up algo for server receiver.
		DataPacketAlgo<String,String> gameAlgo = new DataPacketAlgo<String,String>(null); // don't need default command.
		gameAlgo.setCmd(IAddReceiverType.class, new CRAddReceiverCmd(gameCmd2ModelCustomAdapter));
		gameAlgo.setCmd(IRemoveReceiverType.class, new CRRemoveReceiverCmd(gameCmd2ModelCustomAdapter));
		gameReceiver.setAlgo(gameAlgo);
		// export server receiver to stub, add to team.
		try {
			IReceiver gameReceiverStub = (IReceiver) UnicastRemoteObject.exportObject(gameReceiver, 0);
			gameReceiverStub = new ProxyReceiver(gameReceiverStub, game.toString());
			game.setServerReceiver(gameReceiverStub);
		} catch (RemoteException e) {
			System.out.println("Falied to export receiver: " + gameReceiver);
			e.printStackTrace();
		}
		// add team to user.
		crModel2MainModelAdapter.addChatRoomToUser(game);
		
		// initialize team server receiver objects.
		serverReceivers = new ArrayList<Receiver>(numOfTeams);
		for (int i = 0; i < numOfTeams; i++) {
			// initialize teams and receivers
			Team team = new Team("team " + i, TEAM_SERVER_NAME, teamCapacity, Randomizer.Singleton.randomColor());
			Receiver serverReceiver = new Receiver(userStub);
			serverReceivers.add(serverReceiver);
			// initialize server command to model adapter.
			ICRCmd2ModelCustomAdapter teamCmd2ModelCustomAdapter = new ICRCmd2ModelCustomAdapter() {

				@Override
				public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {}

				@Override
				public String getName() {
					return TEAM_SERVER_NAME;
				}

				@Override
				public void appendMsg(String text, String name) {}

				@Override
				public void buildScrollableComponent(IComponentFactory fac, String label) {}

				@Override
				public void buildNonScrollableComponent(IComponentFactory fac, String label) {}

				@Override
				public <T> T put(MixedDataKey<T> key, T value) {
					return null;
				}

				@Override
				public <T> T get(MixedDataKey<T> key) {
					return null;
				}

				@Override
				public <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data) {}

				@Override
				public void addReceiver(IReceiver receiver) {
					team.addIReceiverStub(receiver);
				}

				@Override
				public void removeReceiver(IReceiver receiver) {
					team.removeIReceiverStub(receiver);
				}

			};
			// set up algo for server receiver.
			DataPacketAlgo<String,String> teamAlgo = new DataPacketAlgo<String,String>(null); // don't need default command.
			teamAlgo.setCmd(IAddReceiverType.class, new CRAddReceiverCmd(teamCmd2ModelCustomAdapter));
			teamAlgo.setCmd(IRemoveReceiverType.class, new CRRemoveReceiverCmd(teamCmd2ModelCustomAdapter));
			serverReceiver.setAlgo(teamAlgo);
			// export server receiver to stub, add to team.
			try {
				IReceiver serverReceiverStub = (IReceiver) UnicastRemoteObject.exportObject(serverReceiver, 0);
				serverReceiverStub = new ProxyReceiver(serverReceiverStub, team.toString());
				team.setServerReceiver(serverReceiverStub);
			} catch (RemoteException e) {
				System.out.println("Falied to export receiver: " + serverReceiver);
				e.printStackTrace();
			}
			// add team to user.
			crModel2MainModelAdapter.addChatRoomToUser(team);
		}
		// send start game data packet to all receivers, including the their user object.
		for (IReceiver rcvr : chatRoom.getIReceiverStubs()) {
			try {
				rcvr.receive(new DataPacketCR<CRInstallGameData>(
						CRInstallGameData.class, 
						new CRInstallGameData(new ProxyUser(rcvr.getUserStub())), 
						receiverStub));
			} catch (RemoteException e) {
				System.out.println("Falied to send install game data packet to receiver: " + rcvr);
				e.printStackTrace();
			}
		}
	}
}
