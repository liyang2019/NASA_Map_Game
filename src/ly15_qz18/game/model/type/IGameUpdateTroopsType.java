package ly15_qz18.game.model.type;

import java.io.Serializable;
import java.util.UUID;

import common.ICRMessageType;

/**
 * Game update troops type. To change the number of troops of a user in a team in a city
 * by the amount from getUpdateTroops();
 */
public interface IGameUpdateTroopsType extends ICRMessageType, Serializable {
	
	/**
	 * Get the city UUID.
	 * @return the city UUID
	 */
	public UUID getCityId();
	
	/**
	 * Get the team UUID.
	 * @return the team UUID
	 */
	public UUID getTeamId();
	
	/**
	 * Get the user UUID.
	 * @return the user UUID
	 */
	public UUID getUserId();
	
	/**
	 * Get the amount of troops to update.
	 * @return the amount of troops to update.
	 */
	public int getUpdateTroops();

}
