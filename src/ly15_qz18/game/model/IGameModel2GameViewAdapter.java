package ly15_qz18.game.model;

import java.util.Collection;

import common.IUser;
import ly15_qz18.game.map.MapLayer;
import ly15_qz18.model.object.Team;

/**
 * Game model to game view adapter.
 *
 */
public interface IGameModel2GameViewAdapter {

	/**
	 * show the map layer
	 * @param layer the map layer to be displayed
	 */
	public void show(MapLayer layer);

	/**
	 * refresh the map GUI
	 */
	public void refreshGUI();

	/**
	 * Append game info in the game view.
	 * @param string - the game info message.
	 */
	public void appendInfo(String string);

	/**
	 * List users in the game view.
	 * @param users - users to list in the game view.
	 */
	public void listUsers(Collection<IUser> users);
	
	/**
	 * Get the number of troops that can be distributed
	 * @param troops number of troops
	 */
	public void displayTroops(int troops);
	
	/**
	 * Display round remain time
	 * @param time - the remaining time.
	 */
	public void displayTimer(int time);
	
	/**
	 * Display the round number of the game.
	 * @param round - the current round of the game.
	 * @param maxRound - the maximum number of round of the game.
	 */
	public void displayRound(int round, int maxRound);

	/**
	 * Update users ready for next round.
	 * @param readyNumber - number of user ready for next round.
	 * @param userNumber - number of total user in the game.
	 */
	public void updataRoundReadyUsers(int readyNumber, int userNumber);

	/**
	 * Update users ready for game.
	 * @param gameReadyNumber - number of user ready for game.
	 * @param userNumber - number of total user in the game.
	 */
	public void updataGameReadyUsers(int gameReadyNumber, int userNumber);

	/**
	 * List teams in the game view.
	 * @param teams - the teams to list.
	 */
	public void listTeams(Collection<Team> teams);	
}
