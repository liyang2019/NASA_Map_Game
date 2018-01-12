package ly15_qz18.game.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import common.IUser;
import ly15_qz18.model.object.Team;

import gov.nasa.worldwind.geom.Position;

/**
 * City.
 */
public class City extends Place {
	
	private static final long serialVersionUID = -917318243565956525L;
	/**
	 * The resource of the city.
	 */
	private int resource;
	/**
	 * The teams in this city and their associated users.
	 */
	private Map<Team, Set<IUser>> teamUsersMap;
	
	/**
	 * The users in this city and their associated troops.
	 */
	private Map<IUser, Integer> userTroopsMap;
	
	/**
	 * The occupying team;
	 */
	private Team occupy;
	
	/**
	 * Neighbor cities of this city.
	 */
	Set<City> neighbors;

	/**
	 * Constructor for City.
	 * @param name - the name of the city.
	 * @param pos - position of the city.
	 * @param resource - the remaining resource of the city.
	 */
	public City(String name, Position pos, int resource) {
		super(name, pos);
		this.resource = resource;
		teamUsersMap = new HashMap<Team, Set<IUser>>();
		userTroopsMap = new HashMap<IUser, Integer>();
		neighbors = new HashSet<City>();
	}
	
	/**
	 * Add troops to a user in the city. If the team is not
	 * already in this city, add the team in the city. 
	 * @param team - the team to add troops.
	 * @param user - the user to add troops.
	 * @param troopsAdd - the troops to add.
	 * @return true if successfully added troops.
	 */
	public boolean addTroops(Team team, IUser user, int troopsAdd) {
		if (troopsAdd < 0) {
			System.out.println("illigal troops amount to add");
			return false;
		}
		Set<IUser> users = teamUsersMap.get(team);
		users = users == null ? new HashSet<IUser>() : users;
		users.add(user);
		teamUsersMap.put(team, users);
		Integer troops = userTroopsMap.get(user);
		troops = troops == null ? troopsAdd : troops + troopsAdd;
		userTroopsMap.put(user, troops);
		return true;
	}
	
	/**
	 * Remove troops of a user in the city. If the troops of the user 
	 * becomes zero, remove the user from the user list of its team. 
	 * If the number of users of one team is zero, remove the team from the city.
	 * @param team - the team to remove troops.
	 * @param user - the user to remove troops.
	 * @param troopsRemove - the troops to remove.
	 * @return true if successfully removed troops.
	 */
	public boolean removeTroops(Team team, IUser user, int troopsRemove) {
		Integer troops = userTroopsMap.get(user);
		if (troops == null) {
			System.out.println("User is not in this city");
			return false;
		}
		if (troops < troopsRemove) {
			System.out.println("illigal troops amount to remove");
			return false;
		} 
		if (troops == troopsRemove) {
			userTroopsMap.remove(user);
			Set<IUser> usrs = teamUsersMap.get(team);
			usrs.remove(user);
			if (usrs.isEmpty()) {
				teamUsersMap.remove(team);
				if (occupy != null && occupy.equals(team)) {
					occupy = null;
				}
			}
		} else {
			userTroopsMap.put(user, troops - troopsRemove);
		}
		return true;
	}
	
	/**
	 * Let all the teams in this city fight with each other.
	 * The team with largest number of troops will win, and 
	 * all other teams will disappear. All the resources will 
	 * be transformed to troops of the remaining team. If teams 
	 * have the same number of troops, then it is not well defined
	 * to decide which team will remain. The remaining troops of 
	 * a team will be distributed to every user in that team according
	 * to their previous troops ratio.
	 */
	public void fight() {
		if (teamUsersMap.size() == 0) {
			occupy = null;
			return;
		}
		Team winnerTeam = null;
		int winnerTroops = 0;
		int secondWinnerTroops = 0;
		for (Map.Entry<Team, Set<IUser>> teamUser : teamUsersMap.entrySet()) {
			int teamTroops = 0;
			for (IUser user : teamUser.getValue()) {
				teamTroops += userTroopsMap.get(user);
			}
			if (winnerTroops < teamTroops) {
				secondWinnerTroops = winnerTroops;
				winnerTroops = teamTroops;
				winnerTeam = teamUser.getKey();
			} else if (secondWinnerTroops < teamTroops) {
				secondWinnerTroops = teamTroops;
			}
		}
		
		Iterator<Map.Entry<Team, Set<IUser>>> itrTeamUser = teamUsersMap.entrySet().iterator();
		while (itrTeamUser.hasNext()) {
			if (!itrTeamUser.next().getKey().equals(winnerTeam)) {
				itrTeamUser.remove();
			}
		}
		
		double ratio = (double) (winnerTroops - secondWinnerTroops + resource) / (double) (winnerTroops);
		Iterator<Map.Entry<IUser, Integer>> itrUserTroop = userTroopsMap.entrySet().iterator();
		Set<IUser> users = teamUsersMap.get(winnerTeam);
		while (itrUserTroop.hasNext()) {
			Map.Entry<IUser, Integer> entry = itrUserTroop.next();
			if (users.contains(entry.getKey())) {
				int newTroop = (int) Math.round(entry.getValue() * ratio);
				newTroop = newTroop == 0 ? 1 : newTroop;
				entry.setValue(newTroop);
			} else {
				itrUserTroop.remove();
			}
		}
		resource = 0;
		occupy = winnerTeam;
	}
	
	/** 
	 * Get the troops of a user in this city.
	 * @param user - a user.
	 * @return the troops of the user in this city.
	 */
	public int getTroops(IUser user) {
		Integer troops = userTroopsMap.get(user);
		return troops == null ? 0 : troops;
	}
	
	/**
	 * Get users of a team in this city.
	 * @param team - a team.
	 * @return a set of users of the team in this city.
	 */
	public Set<IUser> getUsers(Team team) {
		return teamUsersMap.get(team);
	}
	
	/**
	 * Get remaining resource.
	 * @return remaining resource.
	 */
	public int getResource() {
		return resource;
	}

	/**
	 * Get the occupying team.
	 * @return the occupying team.
	 */
	public Team getOccupy() {
		return occupy;
	}
	
	/**
	 * Add a city to neighbor.
	 * @param city - the city to add to neighbor.
	 * @return true if city is not already added as neighbor.
	 */
	public boolean addNeighbor(City city) {
		return neighbors.add(city);
	}
	
	/**
	 * Get the neighbors of the city.
	 * @return the neighbors of the city.
	 */
	public Set<City> getNeighbors() {
		return neighbors;
	}

	/**
	 * Set the occupying team of the city.
	 * @param team - the occupying team of the city.
	 */
	public void setOccupy(Team team) {
		occupy = team;
	}
}
