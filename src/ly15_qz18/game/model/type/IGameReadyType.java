package ly15_qz18.game.model.type;

import java.io.Serializable;
import java.util.UUID;

import common.ICRMessageType;
import common.IUser;
import ly15_qz18.model.object.Team;

/**
 * Game add team and user objects type for synchronizing those objects in city.
 *
 */
public interface IGameReadyType extends ICRMessageType, Serializable {

	/**
	 * Get the team to add city.
	 * @return - team
	 */
	public Team getTeam();
	
	/**
	 * Get the user to add city.
	 * @return - user stub.
	 */
	public IUser getUser();
	
	/**
	 * Get the initial city UUID for the team.
	 * @return initial city UUID for the team.
	 */
	public UUID getCityId();
}
