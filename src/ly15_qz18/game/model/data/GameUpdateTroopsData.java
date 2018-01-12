package ly15_qz18.game.model.data;

import java.util.UUID;

import ly15_qz18.game.model.type.IGameUpdateTroopsType;

/**
 * Game update troops data. To change the number of troops of a user in a team in a city
 * by the amount from getUpdateTroops();
 *
 */
public class GameUpdateTroopsData implements IGameUpdateTroopsType {
	
	private static final long serialVersionUID = -5690289200234654699L;
	private UUID cityId;
	private UUID teamId;
	private UUID userId;
	private int troops;
	
	/**
	 * Constructor.
	 * @param cityId - the UUID of the city to update.
	 * @param teamId - the team UUID.
	 * @param userId - the userUUID.
	 * @param troops - the amount of troops to update.
	 */
	public GameUpdateTroopsData(UUID cityId, UUID teamId, UUID userId, int troops) {
		this.cityId = cityId;
		this.teamId = teamId;
		this.userId = userId;
		this.troops = troops;
	}

	@Override
	public UUID getCityId() {
		return cityId;
	}

	@Override
	public UUID getTeamId() {
		return teamId;
	}

	@Override
	public UUID getUserId() {
		return userId;
	}

	@Override
	public int getUpdateTroops() {
		return troops;
	}

}
