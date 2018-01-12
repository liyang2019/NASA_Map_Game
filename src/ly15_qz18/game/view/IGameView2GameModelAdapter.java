package ly15_qz18.game.view;

import java.util.UUID;

/**
 * Game view to game model adapter.
 *
 * @param <TeamObj> - the generic team object.
 * @param <UserObj> - the generic user object.
 */
public interface IGameView2GameModelAdapter<TeamObj, UserObj> {

	
	
	/**
	 * send click message
	 * @param cityUUID the UUID of the city being clicked
	 * @param n click number
	 */
	public void sendClick(UUID cityUUID, int n);

	/**
	 * Get teams for users to select to join.
	 * @return teams for users to select to join.
	 */
	public Iterable<TeamObj> getTeams();

	/**
	 * Join a selected team.
	 * @param team - the team selected to join.
	 * @return true if successfully joined a team.
	 */
	public boolean joinTeam(TeamObj team);

	/**
	 * Send the text message to game.
	 * @param text - message.
	 */
	public void sendGameMessage(String text);

	/**
	 * Send the text message to team.
	 * @param text - message.
	 */
	public void sendTeamMessage(String text);

	/**
	 * User ready for game, send team and user stubs to all others.
	 */
	public void readyForGame();

	/**
	 * User ready for game, send ready status to all other.
	 */
	public void readyForRound();



}
