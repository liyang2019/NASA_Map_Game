package ly15_qz18.game.model.data;

import java.util.UUID;

import common.IUser;
import ly15_qz18.game.model.type.IGameReadyType;
import ly15_qz18.model.object.Team;

/**
 * Game add team user to city data.
 *
 */
public class GameReadyData implements IGameReadyType {
	
	private static final long serialVersionUID = 860592001579275887L;
	private Team team;
	private IUser user;
	private UUID cityId;
	
	/**
	 * Constructor.
	 * @param team - the team to add.
	 * @param user - the user stub to add.
	 * @param cityId - initial city UUID for the team.
	 */
	public GameReadyData(Team team, IUser user, UUID cityId) {
		this.team = team;
		this.user = user;
		this.cityId = cityId;
	}

	@Override
	public Team getTeam() {
		return team;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	@Override
	public UUID getCityId() {
		return cityId;
	}

}
