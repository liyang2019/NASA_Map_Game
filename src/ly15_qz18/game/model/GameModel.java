package ly15_qz18.game.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import common.DataPacketCR;
import common.IChatRoom;
import common.IReceiver;
import common.IUser;
import common.datatype.chatroom.IAddReceiverType;
import common.datatype.chatroom.IRemoveReceiverType;
import gov.nasa.worldwind.render.Annotation;
import ly15_qz18.game.map.CityAnnotation;
import ly15_qz18.game.map.MapLayer;
import ly15_qz18.game.model.cmd.GameAddReceiverCmd;
import ly15_qz18.game.model.cmd.GameReadyCmd;
import ly15_qz18.game.model.cmd.GameDisplayTextCmd;
import ly15_qz18.game.model.cmd.GameRoundReadyCmd;
import ly15_qz18.game.model.cmd.GameUpdateTroopsCmd;
import ly15_qz18.game.model.cmd.GameRejectionStatusCmd;
import ly15_qz18.game.model.cmd.GameRemoveReceiverCmd;
import ly15_qz18.game.model.cmd.IGameCmd2ModelAdapter;
import ly15_qz18.game.model.cmd.TeamAddReceiverCmd;
import ly15_qz18.game.model.cmd.TeamDisplayTextCmd;
import ly15_qz18.game.model.cmd.TeamRemoveReceiverCmd;
import ly15_qz18.game.model.data.GameReadyData;
import ly15_qz18.game.model.data.GameRoundReadyData;
import ly15_qz18.game.model.data.GameUpdateTroopsData;
import ly15_qz18.mini.model.data.CRAddReceiverData;
import ly15_qz18.mini.model.data.CRRejectionStatusData;
import ly15_qz18.model.data.StringData;
import ly15_qz18.model.object.Game;
import ly15_qz18.model.object.ProxyReceiver;
import ly15_qz18.model.object.Receiver;
import ly15_qz18.model.object.Team;
import provided.datapacket.DataPacketAlgo;
import util.Randomizer;

/**
 * Defense AI Game model.
 *
 */
public class GameModel {
	
	IGameModel2GameViewAdapter gameModel2GameViewAdapter;
	
	/**
	 * the max allowed number of game rounds.
	 */
	private final int MAX_ROUND_NUMBER = 10;
	/**
	 * timer count down time for each round.
	 */
	private final int COUNT_DOWN_TIME = 60;
	
	/**
	 * local user stub who the install game data packet sent to.
	 */
	private IUser userStub;
	/**
	 * User UUID.
	 */
	private UUID userId;
	/**
	 * The creator user of this game.
	 */
	private IUser gameCreator;
	
	/**
	 * The team the local user has joined.
	 */
	private Team team;
	/**
	 * Receiver object for the team.
	 */
	private Receiver teamReceiver;
	/**
	 * Receiver remote stub for the team.
	 */
	private IReceiver teamReceiverStub;
	
	/**
	 * The game global chat room.
	 */
	private Game game;
	/**
	 * Receiver object for the game.
	 */
	private Receiver gameReceiver;
	/**
	 * Receiver remote stub for the game.
	 */
	private IReceiver gameReceiverStub;
	
	/**
	 * Command to team adapter.
	 */
	private IGameCmd2ModelAdapter gameCmd2ModelAdapter;	
	
	/**
	 * The defense ai game model.
	 */
	DefenseAIGame defenseAIGame = new DefenseAIGame();
	
	/**
	 * The troops that can distribute.
	 */
	private int troops = 10;
	
	/**
	 * The map layer.
	 */
	private MapLayer _mMapLayer = new MapLayer();
	
	/**
	 * True if is ready for next round.
	 */
	private boolean isReady = false;
	
	/**
	 * True if game has started.
	 */
	private boolean gameStarted = false;
	
	/**
	 * The round of the game.
	 */
	private int round;
	
	/**
	 * constructor 
	 * @param userStub - local user stub who the install game data packet sent to.
	 * @param gameCreator - the creator user of this game.
	 * @param gameModel2GameViewAdapter - game model to game view adapter
	 * @param gameCmd2ModelAdapter - game command to model adapter
	 */
	public GameModel(
			IUser userStub, 
			IUser gameCreator, 
			IGameModel2GameViewAdapter gameModel2GameViewAdapter, 
			IGameCmd2ModelAdapter gameCmd2ModelAdapter) {
		this.userStub = userStub;
		this.gameCreator = gameCreator;
		this.gameModel2GameViewAdapter = gameModel2GameViewAdapter;
		this.gameCmd2ModelAdapter = gameCmd2ModelAdapter;
		try {
			userId = userStub.getUUID();
		} catch (RemoteException e) {
			System.out.println("Falied to get UUID of user: " + userStub);
			e.printStackTrace();
		}
	}
	

	/**
	 * Action while click the city. left click n = 1, right click n = -1
	 * @param cityId the city UUID
	 * @param n index to identify left(1) or right(-1) click
	 */
	public void sendClick(UUID cityId, int n) {
		int troopsOld = troops;
		if (gameStarted && !isReady) {
			if (n == 1 && troops > 0) {
				troops--;
			} else if (n == -1 && findCityAnnotationById(cityId).getCity().getTroops(userStub) > 0) {
				troops++;
			}
			if (troops != troopsOld) {
				gameModel2GameViewAdapter.displayTroops(troops);
				game.sendPacket(new DataPacketCR<GameUpdateTroopsData>(
						GameUpdateTroopsData.class, 
						new GameUpdateTroopsData(cityId, team.getUUID(), userId, n), 
						gameReceiverStub));
				updateTroops(cityId, team.getUUID(), userId, n);
			}
		}
	}
	
	/**
	 * Update city information for a team and a user, Update the cityAnnotation, refresh the map.
	 * @param cityId - the UUID of the city to update..
	 * @param teamId - the UUID of the team.
	 * @param userId - the UUID of the user.
	 * @param troops - the troops for the city to change.
	 */
	public void updateTroops(UUID cityId, UUID teamId, UUID userId, int troops) {
		defenseAIGame.updateTroops(cityId, teamId, userId, troops);
		CityAnnotation cityAnnotation = findCityAnnotationById(cityId);
		if (cityAnnotation != null) {
			cityAnnotation.update();
			gameModel2GameViewAdapter.refreshGUI();
		}
	}

	
	/**
	 * Find city annotation by city UUID
	 * @param cityId the city UUID
	 * @return the city annotation
	 */
	public CityAnnotation findCityAnnotationById(UUID cityId){
		for(Annotation annotation : _mMapLayer.getAnnotations()){
			if (annotation instanceof CityAnnotation) {
				CityAnnotation cityAnnotation = (CityAnnotation) annotation;
				if(cityAnnotation.getCity().getUUID().equals(cityId)) {
					return cityAnnotation;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Show the remain time for the round
	 * @param seconds the maximum time interval in a round
	 */
	public void Timer(int seconds){
		int currentRound = this.round;
		new Thread(() -> {
			int n = seconds;
			while(n >= 0){
				try {
					gameModel2GameViewAdapter.displayTimer(n);
					Thread.sleep(1000);
					n--;
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(currentRound != this.round) {
					break;
				}
			}
			if (currentRound == this.round) {
				readyForRound();
			}
			
		}).start();
	}


	/**
	 * update the game map.
	 */
	private void updataMap() {
		_mMapLayer.removeAllAnnotations();
		Set<City> occupiedCities = new HashSet<City>();
		Set<City> neighborCities = new HashSet<City>();
		for (City city : defenseAIGame.getCities()) {
			if (city.getOccupy() != null && city.getOccupy().equals(team)) {
				occupiedCities.add(city);
				_mMapLayer.addAnnotation(new CityAnnotation(city, this.userStub, this.team));
			}
		}
		for (City city : occupiedCities) {
			for (City nbCity : city.getNeighbors()) {
				if (nbCity.getOccupy() == null || !nbCity.getOccupy().equals(team)) {
					neighborCities.add(nbCity);
				}
			}
		}
		for (City nbCity : neighborCities) {
			_mMapLayer.addAnnotation(new CityAnnotation(nbCity, this.userStub, this.team));
		}
		gameModel2GameViewAdapter.show(_mMapLayer);
	}
	
	/**
	 * start game model
	 */
	public void start() {
		gameModel2GameViewAdapter.listTeams(getTeams());
		try {
			for (IChatRoom chatRoom : gameCreator.getChatRooms()) {
				if (chatRoom instanceof Game) {
					game = (Game) chatRoom;
					joinGame(game);
				}
			}
		} catch (RemoteException e) {
			System.out.println("Falied to get chat room from user: " + gameCreator);
			e.printStackTrace();
		}
		gameModel2GameViewAdapter.displayTroops(troops);
	}



	/**
	 * Get teams from the creator user of this game.
	 * @return teams from the creator user of this game.
	 */
	public Collection<Team> getTeams() {
		List<Team> teams = new ArrayList<Team>();
		try {
			for (IChatRoom chatRoom : gameCreator.getChatRooms()) {
				if (chatRoom instanceof Team) {
					teams.add((Team) chatRoom);
				}
			}
		} catch (RemoteException e) {
			System.out.println("Falied to get chat room from user: " + gameCreator);
			e.printStackTrace();
		}
		return teams;
	}

	/**
	 * Join the game chat room.
	 * @param game - the game chat room.
	 */
	public void joinGame(Game game) {
		try {
			gameReceiver = new Receiver(userStub);
			gameReceiverStub = (IReceiver) UnicastRemoteObject.exportObject(gameReceiver, 0);
			gameReceiverStub = new ProxyReceiver(gameReceiverStub, game.toString());
			gameModel2GameViewAdapter.appendInfo("created receiver: " + gameReceiverStub + "\n");
			DataPacketAlgo<String,String> algo = new DataPacketAlgo<String,String>(null);
			algo.setCmd(CRRejectionStatusData.class, new GameRejectionStatusCmd(gameCmd2ModelAdapter));
			algo.setCmd(IRemoveReceiverType.class, new GameRemoveReceiverCmd(gameCmd2ModelAdapter));
			algo.setCmd(IAddReceiverType.class, new GameAddReceiverCmd(gameCmd2ModelAdapter));
			algo.setCmd(StringData.class, new GameDisplayTextCmd(gameCmd2ModelAdapter));
			algo.setCmd(GameReadyData.class, new GameReadyCmd(gameCmd2ModelAdapter));
			algo.setCmd(GameRoundReadyData.class, new GameRoundReadyCmd(gameCmd2ModelAdapter));
			algo.setCmd(GameUpdateTroopsData.class, new GameUpdateTroopsCmd(gameCmd2ModelAdapter));
			gameReceiver.setAlgo(algo);
			game.sendPacket(new DataPacketCR<IAddReceiverType>(
					IAddReceiverType.class, 
					new CRAddReceiverData(gameReceiverStub), 
					gameReceiverStub));
			addGameReceiver(gameReceiverStub);
		} catch (RemoteException e) {
			System.out.println("fail to create receiver stub for the game: " + game);
			e.printStackTrace();
		}
	}


	/**
	 * Join a selected team.
	 * @param team - the selected team.
	 * @return true if successfully joined a team.
	 */
	public boolean joinTeam(Team team) {
		if (this.team != null) {
			gameModel2GameViewAdapter.appendInfo("You have already joined a team, can not join another team");
			return false;
		}
		
		if (team.isFull()) {
			gameModel2GameViewAdapter.appendInfo("the team you have selected is full");
			return false;
		}
		this.team = team;
		try {
			teamReceiver = new Receiver(userStub);
			teamReceiverStub = (IReceiver) UnicastRemoteObject.exportObject(teamReceiver, 0);
			teamReceiverStub = new ProxyReceiver(teamReceiverStub, team.toString());
			gameModel2GameViewAdapter.appendInfo("created receiver: " + teamReceiverStub + "\n");
			DataPacketAlgo<String,String> algo = new DataPacketAlgo<String,String>(null);
			algo.setCmd(CRRejectionStatusData.class, new GameRejectionStatusCmd(gameCmd2ModelAdapter));
			algo.setCmd(IRemoveReceiverType.class, new TeamRemoveReceiverCmd(gameCmd2ModelAdapter));
			algo.setCmd(IAddReceiverType.class, new TeamAddReceiverCmd(gameCmd2ModelAdapter));
			algo.setCmd(StringData.class, new TeamDisplayTextCmd(gameCmd2ModelAdapter));
			teamReceiver.setAlgo(algo);
			team.sendPacket(new DataPacketCR<IAddReceiverType>(
					IAddReceiverType.class, 
					new CRAddReceiverData(teamReceiverStub), 
					teamReceiverStub));
			addTeamReceiver(teamReceiverStub);
		} catch (RemoteException e) {
			System.out.println("fail to create receiver stub for team: " + team);
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Refresh the user list to list users.
	 */
	public void listUsers() {
		if (team == null) {
			System.out.println("havn't join a team, can not list users");
			return;
		}
		List<IUser> users = new ArrayList<>();
		for (IReceiver rcvr : team.getIReceiverStubs()) {
			try {
				IUser usr = rcvr.getUserStub();
				// filter out the server receiver, which must satisfy the following two condition.
				if (usr.equals(userStub) && !rcvr.equals(teamReceiverStub)) {
					continue;
				}
				users.add(usr);
			} catch (RemoteException e1) {
				System.out.println("failed to get user stubs from receiver: " + rcvr);
				e1.printStackTrace();
			}
		}
		gameModel2GameViewAdapter.listUsers(users);
	}
	
	/**
	 * Remove a receiverStub from the game locally.
	 * @param receiverStub is the receiverStub to remove from the game.
	 */
	public void removeGameReceiver(IReceiver receiverStub) {
		game.removeIReceiverStub(receiverStub);
	}

	/**
	 * Remove a receiverStub from the joined team locally.
	 * @param receiverStub is the receiverStub to remove from the joined team.
	 */
	public void removeTeamReceiver(IReceiver receiverStub) {
		team.removeIReceiverStub(receiverStub);
		listUsers();
	}

	/**
	 * Add a receiverStub to the game locally.
	 * @param receiverStub is the receiverStub to add to the game.
	 */
	public void addGameReceiver(IReceiver receiverStub) {
		game.addIReceiverStub(receiverStub);
	}
	
	/**
	 * Add a receiverStub to the joined team locally.
	 * @param receiverStub is the receiverStub to add to the joined team.
	 */
	public void addTeamReceiver(IReceiver receiverStub) {
		team.addIReceiverStub(receiverStub);
		listUsers();
	}



	/**
	 * Send text message to game.
	 * @param text - message.
	 */
	public void sendGameMessage(String text) {
		game.sendPacket(new DataPacketCR<StringData>(StringData.class, new StringData(text), gameReceiverStub));
		gameCmd2ModelAdapter.appendMsg(text, "[Game] You");
	}


	/**
	 * Send text message to team.
	 * @param text - message.
	 */
	public void sendTeamMessage(String text) {
		team.sendPacket(new DataPacketCR<StringData>(StringData.class, new StringData(text), teamReceiverStub));
		gameCmd2ModelAdapter.appendMsg(text, "[Team] You");
	}


	/**
	 * User ready for game, send team and user stubs to all others.
	 */
	public void readyForGame() {
		List<City> cities = defenseAIGame.getCities();
		UUID cityId = cities.get(Randomizer.Singleton.randomInt(0, cities.size() - 1)).getUUID();
		game.sendPacket(new DataPacketCR<GameReadyData>(
				GameReadyData.class, 
				new GameReadyData(team, userStub, cityId), 
				gameReceiverStub));
		setGameReady(team, userStub, cityId);
	}


	/**
	 * set user game ready, add team and user to game, set initial city for team.
	 * @param team - the team to add.
	 * @param user - the user to add.
	 * @param cityId - city UUID for team.
	 */
	public void setGameReady(Team team, IUser user, UUID cityId) {
		defenseAIGame.setGameReady(team, user, cityId);
		gameModel2GameViewAdapter.updataGameReadyUsers(defenseAIGame.getGameReadyNumber(), game.getIReceiverStubs().size());
		if (defenseAIGame.getGameReadyNumber() == game.getIReceiverStubs().size()) {
			gameStarted = true;
			updataMap();
			this.round = 1;
			this.Timer(COUNT_DOWN_TIME);
			gameModel2GameViewAdapter.displayRound(this.round, this.MAX_ROUND_NUMBER);
		}
	}

	/**
	 * User ready for next round, send ready status to all others.
	 */
	public void readyForRound() {
		if (gameStarted) {
			isReady = true;
			game.sendPacket(new DataPacketCR<GameRoundReadyData>(
					GameRoundReadyData.class, 
					new GameRoundReadyData(userId), 
					gameReceiverStub));
			setRoundReady(userId);
		} else {
			gameModel2GameViewAdapter.appendInfo("others are joining teams, game has not started");
		}
	}
	
	/**
	 * Set user to ready for next round.
	 * @param userId - user's UUID.
	 */
	public void setRoundReady(UUID userId) {
		defenseAIGame.addReadyUser(userId);
		if (defenseAIGame.getReadyNumber() == game.getIReceiverStubs().size()) {
			update();
		}
		gameModel2GameViewAdapter.updataRoundReadyUsers(defenseAIGame.getReadyNumber(), game.getIReceiverStubs().size());
	}
	
	private void update() {
		if (round > MAX_ROUND_NUMBER) {
			return;
		}
		defenseAIGame.update();
		updataMap();
		if (round == MAX_ROUND_NUMBER) {
			endGame();
			return;
		}
		round++;		
		gameModel2GameViewAdapter.displayRound(round, MAX_ROUND_NUMBER);
		isReady = false;
		this.Timer(COUNT_DOWN_TIME);		
	}
	
	private void endGame() {
		gameModel2GameViewAdapter.appendInfo("game ended");
		gameModel2GameViewAdapter.appendInfo(defenseAIGame.getWinnerTeam() + " win!!!");
	}

}