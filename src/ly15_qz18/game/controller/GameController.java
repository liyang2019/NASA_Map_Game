package ly15_qz18.game.controller;

import java.awt.EventQueue;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import common.ICRCmd2ModelAdapter;
import common.ICRMessageType;
import common.IComponentFactory;
import common.IReceiver;
import common.IUser;
import gov.nasa.worldwind.geom.Position;
import ly15_qz18.game.map.MapLayer;
import ly15_qz18.game.model.IGameModel2GameViewAdapter;
import ly15_qz18.game.model.cmd.IGameCmd2ModelAdapter;
import ly15_qz18.game.model.GameModel;
import ly15_qz18.game.view.GameView;
import ly15_qz18.game.view.IGameView2GameModelAdapter;
import ly15_qz18.model.object.Team;
import map.IRightClickAction;
import provided.mixedData.MixedDataKey;

/**
 * Game controller.
 *
 */
public class GameController {
	private GameView<Team, IUser> view;
	private GameModel model;
	
	/**
	 *  May want larger or different type of blocking queue
	 */
	private BlockingQueue<Runnable> bq = new LinkedBlockingQueue<Runnable>(5); 
	
	/**
	 * Constructor.
	 * @param crCmd2ModelAdapter - chat room command to model adapter.
	 * @param userStub - local user stub who the install game data packet sent to.
	 * @param gameCreator - the creator user of this game.
	 */
	public GameController(ICRCmd2ModelAdapter crCmd2ModelAdapter, IUser userStub, IUser gameCreator) {
		view = new GameView<Team, IUser>(new IGameView2GameModelAdapter<Team, IUser>() {

			@Override
			public void sendClick(UUID cityID, int n) {
				model.sendClick(cityID, n);
			}

			@Override
			public Iterable<Team> getTeams() {
				return model.getTeams();
			}

			@Override
			public boolean joinTeam(Team team) {
				return model.joinTeam(team);
			}

			@Override
			public void sendGameMessage(String text) {
				model.sendGameMessage(text);
			}

			@Override
			public void sendTeamMessage(String text) {
				model.sendTeamMessage(text);
			}

			@Override
			public void readyForGame() {
				model.readyForGame();
			}

			@Override
			public void readyForRound() {
				model.readyForRound();
			}
			
		}, new IRightClickAction() {

			@Override
			public void apply(Position p) {
				// TODO Auto-generated method stub
			}
			
		});

		model = new GameModel(userStub, gameCreator, 
				
				new IGameModel2GameViewAdapter() {

			@Override
			public void show(MapLayer layer) {
				view.addMapLayer(layer);
			}

			@Override
			public void refreshGUI() {
				view.refreshGUI();
			}

			@Override
			public void appendInfo(String text) {
				view.appendMessage(text, "System");
			}

			@Override
			public void listUsers(Collection<IUser> users) {
				IUser[] array = new IUser[users.size()];
				users.toArray(array);
				view.listUsers(array);
			}
			
			@Override
			public void displayTroops(int troops) {
				view.displayTroops(troops);
			}
			
			@Override
			public void displayTimer(int time) {
				view.displayTimer(time);
			}
			
			@Override			
			public void displayRound(int round, int maxRound) {
				view.displayRound(round, maxRound);
			}

			@Override
			public void updataRoundReadyUsers(int readyNumber, int userNumber) {
				view.updataRoundReadyUsers(readyNumber, userNumber);
			}

			@Override
			public void updataGameReadyUsers(int gameReadyNumber, int userNumber) {
				view.updataGameReadyUsers(gameReadyNumber, userNumber);
			}

			@Override
			public void listTeams(Collection<Team> teams) {
				view.listTeams(teams);
			}

		}, 
				new IGameCmd2ModelAdapter() {

			@Override
			public <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data) {
				// TODO Auto-generated method stub
			}

			@Override
			public <T> T put(MixedDataKey<T> key, T value) {
				return crCmd2ModelAdapter.put(key, value);
			}

			@Override
			public String getName() {
				try {
					return userStub.getName();
				} catch (RemoteException e) {
					System.out.println("Failed to get the name of the local user: " + userStub);
					e.printStackTrace();
					return "can not get name";
				}
			}

			@Override
			public <T> T get(MixedDataKey<T> key) {
				return crCmd2ModelAdapter.get(key);
			}

			@Override
			public void buildScrollableComponent(IComponentFactory fac, String label) {
				// TODO Auto-generated method stub
			}

			@Override
			public void buildNonScrollableComponent(IComponentFactory fac, String label) {
				// TODO Auto-generated method stub
			}

			@Override
			public void appendMsg(String text, String name) {
				view.appendMessage(text, name);
			}

			@Override
			public <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {
				// TODO Auto-generated method stub
			}

			@Override
			public void removeReceiver(IReceiver receiver) {}

			@Override
			public void addReceiver(IReceiver receiver) {}

			@Override
			public void removeTeamReceiver(IReceiver receiver) {
				model.removeTeamReceiver(receiver);
			}

			@Override
			public void removeGameReceiver(IReceiver receiver) {
				model.removeGameReceiver(receiver);
			}

			@Override
			public void addTeamReceiver(IReceiver receiver) {
				model.addTeamReceiver(receiver);
			}

			@Override
			public void addGameReceiver(IReceiver receiver) {
				model.addGameReceiver(receiver);
			}

			@Override
			public void setGameReady(Team team, IUser user, UUID cityId) {
				model.setGameReady(team, user, cityId);
			}

			@Override
			public void setRoundReady(UUID userId) {
				model.setRoundReady(userId);
			}

			@Override
			public void updateTroops(UUID cityId, UUID teamId, UUID userId, int troops) {
				model.updateTroops(cityId, teamId, userId, troops);
			}
			
		});
	}

	/**
	 * start game MVC synchronously.
	 */
	public void startSync() {
		model.start();
		view.start();
	}
	
	/**
	 * start game MVC asynchronously.
	 */
	public void startASync() {
		(new Thread() {
			@Override
			public void run() {
				startSync();						
			}
		}).start();
	}
	
	/**
	 * Run the given Runnable job on the main thread.
	 * @param r   The Runnable job to run
	 */
	public void runJob(Runnable r) {
		try {
			bq.put(r);   // Put job into the queue, blocking if out of space
		} catch (InterruptedException e) {
			System.out.println("runJob(): Exception putting job into blocking queue = "+e);
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * get the string used for generate game UUID
	 * @return the string used for generate game UUID
	 */
	public static String getGameIdStr() {
		return "a-b-c-d-e";
	}
	
	/**
	 * get the description used for generate game UUID
	 * @return the description used for generate game UUID
	 */
	public static String getDes() {
		return "Click2Win";
	}
	
	/**
	 * get the map model
	 * @return map model
	 */
	public GameModel getModel() {
		return model;
	}
	
	/**
	 * get the game view
	 * @return the game view
	 */
	public GameView<Team, IUser> getGameView() {
		return view;
	}
	
	/**
	 * Debug use main.
	 * @param args - input arguments.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { // Java specs say that the system must be constructed on the GUI event thread.
			public void run() {
				try {
					GameController controller = new GameController(null, null, null); // instantiate the system
					controller.startASync(); // start the system
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * User local created teams for game creator user.
	 * @param teams - local created teams.
	 */
	public void useLocalTeams(List<Team> teams) {
		view.listTeams(teams);
	}
}