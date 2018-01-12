package ly15_qz18.game.model.cmd;

import java.util.UUID;

import common.ICRMessageType;
import common.IComponentFactory;
import common.IReceiver;
import common.IUser;
import ly15_qz18.mini.model.cmd.ICRCmd2ModelCustomAdapter;
import ly15_qz18.model.object.Team;

/**
 * Game command to team adapter.
 */
public interface IGameCmd2ModelAdapter extends ICRCmd2ModelCustomAdapter {

	@Override
	public default <T extends ICRMessageType> void sendToChatRoom(Class<T> id, T data) {}

	@Override
	public default void buildScrollableComponent(IComponentFactory fac, String label) {}

	@Override
	public default void buildNonScrollableComponent(IComponentFactory fac, String label) {}

	@Override
	public default <T extends ICRMessageType> void sendTo(IReceiver target, Class<T> id, T data) {}

	/**
	 * Remove team receiver.
	 * @param receiver - the receiver to remove.
	 */
	public void removeTeamReceiver(IReceiver receiver);

	/**
	 * Remove game receiver.
	 * @param receiver - the receiver to remove.
	 */
	public void removeGameReceiver(IReceiver receiver);

	/**
	 * Add team receiver.
	 * @param receiver - the receiver to add.
	 */
	public void addTeamReceiver(IReceiver receiver);

	/**
	 * Add game receiver.
	 * @param receiver - the receiver to add.
	 */
	public void addGameReceiver(IReceiver receiver);

	/**
	 * Set user game ready, add team and user to game, set initial city for team.
	 * @param team - the team to add.
	 * @param user - the user to add.
	 * @param cityId - initial city UUID for team.
	 */
	public void setGameReady(Team team, IUser user, UUID cityId);

	/**
	 * Set user round ready.
	 * @param userId - user UUID.
	 */
	public void setRoundReady(UUID userId);

	/**
	 * Update city information for a team and a user, Update the cityAnnotation, refresh the map.
	 * @param cityId - the UUID of the city to update..
	 * @param teamId - the UUID of the team.
	 * @param userId - the UUID of the user.
	 * @param troops - the troops for the city to change.
	 */
	public void updateTroops(UUID cityId, UUID teamId, UUID userId, int troops);

}
