package ly15_qz18.game.model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;

import common.IUser;
import gov.nasa.worldwind.geom.Position;
import ly15_qz18.model.object.Team;
import util.SineMaker;

/**
 * The defense AI game.
 *
 */
public class DefenseAIGame implements Serializable {
	
	private static final long serialVersionUID = 5711885126193571399L;
	/**
	 * the minimum resource of the city.
	 */
	private final int CITY_MIN_RESOURCE = 1;
	/**
	 * the maximum resource of the city.
	 */
	private final int CITY_MAX_RESOURCE = 10;
	/**
	 * initial troops for each user.
	 */
	private final int INITIAL_TROOPS_FOR_USER = 10;
	/**
	 * number of neighbors for each city.
	 */
	private final int NUM_OF_NEIGHBORS = 5;
	
	/**
	 * All cities in the game.
	 */
	private List<City> cities;
	
	/**
	 * City UUID to City map.
	 */
	private Map<UUID, City> cityMap;
	/**
	 * Team UUID to Team map.
	 */
	private Map<UUID, Team> teamMap;
	/**
	 * User UUID to user map.
	 */
	private Map<UUID, IUser> userMap;
	/**
	 * User UUID and ready map.
	 */
	private Set<UUID> readyUsers;
	/**
	 * base(initial) cities for teams.
	 */
	private Map<Team, City> baseCities;
		
	/**
	 * Constructor.
	 */
	public DefenseAIGame() {
		cityMap = new HashMap<UUID, City>();
		teamMap = new HashMap<UUID, Team>();
		userMap = new HashMap<UUID, IUser>();
		readyUsers = new HashSet<UUID>();
		baseCities = new HashMap<Team, City>();
		initializeCities();
	}
	
	/**
	 * Initialize cities.
	 */
	private void initializeCities() {
		
		SineMaker sineMaker = new SineMaker(CITY_MIN_RESOURCE, CITY_MAX_RESOURCE, 0.1);

		cities = new ArrayList<City>();
		cities.add(new City("California", Position.fromDegrees(36.116203, -119.681564), sineMaker.getIntVal()));
		cities.add(new City("Texas",	Position.fromDegrees(31.054487, -97.563461), sineMaker.getIntVal()));
		cities.add(new City("Florida", Position.fromDegrees(27.766279, -81.686783), sineMaker.getIntVal()));
		cities.add(new City("New York", Position.fromDegrees(42.165726, -74.948051), sineMaker.getIntVal()));
		cities.add(new City("Illinois", Position.fromDegrees(40.349457, -88.986137), sineMaker.getIntVal()));
		cities.add(new City("Pennsylvania",Position.fromDegrees(40.590752, -77.209755), sineMaker.getIntVal()));
		cities.add(new City("Ohio", Position.fromDegrees(40.388783, -82.764915), sineMaker.getIntVal()));
		cities.add(new City("Georgia", Position.fromDegrees(33.040619, -83.643074), sineMaker.getIntVal()));
		cities.add(new City("Alabama", Position.fromDegrees(32.806671, -86.791130, 3500), sineMaker.getIntVal()));
		cities.add(new City("Arizona", Position.fromDegrees(33.729759, -111.431221, 3500), sineMaker.getIntVal()));
		cities.add(new City("Arkansas", Position.fromDegrees(34.969704, -92.373123, 3500), sineMaker.getIntVal()));
		cities.add(new City("Colorado", Position.fromDegrees(39.059811, -105.311104, 3500), sineMaker.getIntVal()));
		cities.add(new City("Connecticut", Position.fromDegrees(41.597782, -72.755371, 3500), sineMaker.getIntVal()));
		cities.add(new City("Delaware", Position.fromDegrees(39.318523, -75.507141, 3500), sineMaker.getIntVal()));
		cities.add(new City("District of Columbia", Position.fromDegrees(38.897438, -77.026817, 3500), sineMaker.getIntVal()));
		cities.add(new City("Florida", Position.fromDegrees(27.766279, -81.686783, 3500), sineMaker.getIntVal()));
		cities.add(new City("Georgia", Position.fromDegrees(33.040619, -83.643074, 3500), sineMaker.getIntVal()));
		cities.add(new City("Idaho", Position.fromDegrees(44.240459, -114.478828, 3500), sineMaker.getIntVal()));
		cities.add(new City("Illinois", Position.fromDegrees(40.349457, -88.986137, 3500), sineMaker.getIntVal()));
		cities.add(new City("Indiana", Position.fromDegrees(39.849426, -86.258278, 3500), sineMaker.getIntVal()));
		cities.add(new City("Iowa", Position.fromDegrees(42.011539, -93.210526, 3500), sineMaker.getIntVal()));
		cities.add(new City("Kansas", Position.fromDegrees(38.526600, -96.726486, 3500), sineMaker.getIntVal()));
		cities.add(new City("Kentucky", Position.fromDegrees(37.668140, -84.670067, 3500), sineMaker.getIntVal()));
		cities.add(new City("Louisiana", Position.fromDegrees(31.169546, -91.867805, 3500), sineMaker.getIntVal()));
		cities.add(new City("Maine", Position.fromDegrees(44.693947, -69.381927, 3500), sineMaker.getIntVal()));
		cities.add(new City("Maryland", Position.fromDegrees(39.063946, -76.802101, 3500), sineMaker.getIntVal()));
		cities.add(new City("Massachusetts", Position.fromDegrees(42.230171, -71.530106, 3500), sineMaker.getIntVal()));
		cities.add(new City("Michigan", Position.fromDegrees(43.326618, -84.536095, 3500), sineMaker.getIntVal()));
		cities.add(new City("Minnesota", Position.fromDegrees(45.694454, -93.900192, 3500), sineMaker.getIntVal()));
		cities.add(new City("Mississippi", Position.fromDegrees(32.741646, -89.678696, 3500), sineMaker.getIntVal()));
		cities.add(new City("Missouri", Position.fromDegrees(38.456085, -92.288368, 3500), sineMaker.getIntVal()));
		cities.add(new City("Montana", Position.fromDegrees(46.921925, -110.454353, 3500), sineMaker.getIntVal()));
		cities.add(new City("Nebraska", Position.fromDegrees(41.125370, -98.268082, 3500), sineMaker.getIntVal()));
		cities.add(new City("Nevada", Position.fromDegrees(38.313515, -117.055374, 3500), sineMaker.getIntVal()));
		cities.add(new City("New Hampshire", Position.fromDegrees(43.452492, -71.563896, 3500), sineMaker.getIntVal()));
		cities.add(new City("New Jersey", Position.fromDegrees(40.298904, -74.521011, 3500), sineMaker.getIntVal()));
		cities.add(new City("New Mexico", Position.fromDegrees(34.840515, -106.248482, 3500), sineMaker.getIntVal()));
		cities.add(new City("New York", Position.fromDegrees(42.165726, -74.948051, 3500), sineMaker.getIntVal()));
		cities.add(new City("North Carolina", Position.fromDegrees(35.630066, -79.806419, 3500), sineMaker.getIntVal()));
		cities.add(new City("North Dakota", Position.fromDegrees(47.528912, -99.784012, 3500), sineMaker.getIntVal()));
		cities.add(new City("Ohio", Position.fromDegrees(40.388783, -82.764915, 3500), sineMaker.getIntVal()));
		cities.add(new City("Oklahoma", Position.fromDegrees(35.565342, -96.928917, 3500), sineMaker.getIntVal()));
		cities.add(new City("Oregon", Position.fromDegrees(44.572021, -122.070938, 3500), sineMaker.getIntVal()));
		cities.add(new City("Pennsylvania", Position.fromDegrees(40.590752, -77.209755, 3500), sineMaker.getIntVal()));
		cities.add(new City("Rhode Island", Position.fromDegrees(41.680893, -71.511780, 3500), sineMaker.getIntVal()));
		cities.add(new City("South Carolina", Position.fromDegrees(33.856892, -80.945007, 3500), sineMaker.getIntVal()));
		cities.add(new City("South Dakota", Position.fromDegrees(44.299782, -99.438828, 3500), sineMaker.getIntVal()));
		cities.add(new City("Tennessee", Position.fromDegrees(35.747845, -86.692345, 3500), sineMaker.getIntVal()));
		cities.add(new City("Texas", Position.fromDegrees(31.054487, -97.563461, 3500), sineMaker.getIntVal()));
		cities.add(new City("Utah", Position.fromDegrees(40.150032, -111.862434, 3500), sineMaker.getIntVal()));
		cities.add(new City("Vermont", Position.fromDegrees(44.045876, -72.710686, 3500), sineMaker.getIntVal()));
		cities.add(new City("Virginia", Position.fromDegrees(37.769337, -78.169968, 3500), sineMaker.getIntVal()));
		cities.add(new City("Washington", Position.fromDegrees(47.400902, -121.490494, 3500), sineMaker.getIntVal()));
		cities.add(new City("West Virginia", Position.fromDegrees(38.491226, -80.954453, 3500), sineMaker.getIntVal()));
		cities.add(new City("Wisconsin", Position.fromDegrees(44.268543, -89.616508, 3500), sineMaker.getIntVal()));
		cities.add(new City("Wyoming", Position.fromDegrees(42.755966, -107.302490, 3500), sineMaker.getIntVal()));
		// hard code of Hawaii and Alaska.
		City california = new City("California", Position.fromDegrees(36.116203, -119.681564, 3500), sineMaker.getIntVal());
		City hawaii = new City("Hawaii", Position.fromDegrees(21.094318, -157.498337, 3500), 100);
		california.addNeighbor(hawaii);
		hawaii.addNeighbor(california);
		cities.add(hawaii);
		cities.add(california);
		City alaska = new City("Alaska", Position.fromDegrees(61.370716, -152.404419, 3500), sineMaker.getIntVal());
		City washington = new City("Washington", Position.fromDegrees(47.400902, -121.490494, 3500), sineMaker.getIntVal());
		alaska.addNeighbor(washington);
		washington.addNeighbor(alaska);
		cities.add(alaska);
		cities.add(washington);
//		// set up neighbors.
//		for (int i = 0; i < cities.size(); i++) {
//			City city1 = cities.get(i);
//			for (int j = i + 1; j < cities.size(); j++) {
//				City city2 = cities.get(j);
//				if (Position.greatCircleDistance(city1.getPosition(), city2.getPosition()).compareTo(MAX_DISTANCE) < 0) {
//					city1.addNeighbor(city2);
//					city2.addNeighbor(city1);
//				}
//			}
//		}
		
		
		for (City city1 : cities) {
			PriorityQueue<City> pq = new PriorityQueue<>(NUM_OF_NEIGHBORS, new Comparator<City>() {
				@Override
				public int compare(City c1, City c2) {
					return Position.greatCircleDistance(city1.getPosition(), c2.getPosition())
							.compareTo(Position.greatCircleDistance(city1.getPosition(), c1.getPosition()));
				}				
			});
			for (City city2 : cities) {
				if (!city1.equals(city2)) {
					if (pq.size() < NUM_OF_NEIGHBORS) {
						pq.add(city2);
					}
					else if (Position.greatCircleDistance(city1.getPosition(), city2.getPosition())
							.compareTo(Position.greatCircleDistance(city1.getPosition(), pq.peek().getPosition())) < 0) {
						pq.poll();
						pq.add(city2);
					}
				}
			}
			while (!pq.isEmpty()) {
				city1.addNeighbor(pq.poll());
			}
		}
		
//		cities = new ArrayList<City>();
//		cities.add(new City("NYC", Position.fromDegrees(40.748974, -73.990288, 3500), sineMaker.getIntVal()));
//		cities.add(new City("LA", Position.fromDegrees(34.0522342, -118.2436849, 3500), sineMaker.getIntVal()));
//		cities.add(new City("CHI", Position.fromDegrees(41.8781136, -87.6297982, 3500), sineMaker.getIntVal()));
//		cities.add(new City("HOU", Position.fromDegrees(29.7604267, -95.3698028, 3500), sineMaker.getIntVal()));
//		cities.add(new City("IND", Position.fromDegrees(39.768403, -86.158068, 3500), sineMaker.getIntVal()));
//		cities.add(new City("SEA", Position.fromDegrees(47.6147628, -122.475989, 3500), sineMaker.getIntVal()));
//		cities.add(new City("PHI", Position.fromDegrees(39.9525839, -75.1652215, 3500), sineMaker.getIntVal()));
//		cities.add(new City("PHO", Position.fromDegrees(33.4483771, -112.0740373, 3500), sineMaker.getIntVal()));
//		cities.add(new City("SD", Position.fromDegrees(32.715738, -117.1610838, 3500), sineMaker.getIntVal()));
//		cities.add(new City("BOS", Position.fromDegrees(42.3600825, -71.0588801, 3500), sineMaker.getIntVal()));
//		cities.add(new City("MAD", Position.fromDegrees(43.0730517, -89.4012302, 3500), sineMaker.getIntVal()));
//		cities.add(new City("DAL", Position.fromDegrees(32.7766642, -96.796987, 3500), sineMaker.getIntVal()));
//		cities.add(new City("SA", Position.fromDegrees(29.4241219, -98.493628, 3500), sineMaker.getIntVal()));
//		cities.add(new City("CAS", Position.fromDegrees(42.866632, -106.313081, 3500), sineMaker.getIntVal()));
//		cities.add(new City("BIS", Position.fromDegrees(46.0653638, -106.313081, 3500), sineMaker.getIntVal()));
//		cities.add(new City("BIL", Position.fromDegrees(45.2240988, -109.8340373, 3500), sineMaker.getIntVal()));
		
		for (City city : cities) {
			cityMap.put(city.getUUID(), city);
		}
	}
	
	/**
	 * Update troops to city of a given team and user in the team by certain amount.
	 * @param cityId - the UUID of the city.
	 * @param teamId - the UUID of the team.
	 * @param userId - the UUID of the user.
	 * @param troops - the number of troops to modify.
	 * @return true if add troops successfully.
	 */
	public boolean updateTroops(UUID cityId, UUID teamId, UUID userId, int troops) {
		City city = cityMap.get(cityId);
		if (city == null) {
			System.out.println("can not find city from city id");
			return false;
		}
		Team team = teamMap.get(teamId);
		if (team == null) {
			System.out.println("can not find team from team id");
			return false;
		}
		IUser user = userMap.get(userId);
		if (user == null) {
			System.out.println("can not find user from user id");
			return false;
		}
		if (troops > 0) {
			return city.addTroops(team, user, troops);
		} else if (troops < 0) {
			return city.removeTroops(team, user, -troops);
		}
		return false;
	}
	
	/**
	 * Get all the cities in the game.
	 * @return all the cities in the game.
	 */
	public List<City> getCities() {
		return cities;
	}

	/**
	 * Set user to be ready for next round.
	 * @param userId - user's UUID.
	 */
	public void addReadyUser(UUID userId) {
		readyUsers.add(userId);
	}
	
	/**
	 * Get the number of users ready for next round.
	 * @return the number of users ready for next round.
	 */
	public int getReadyNumber() {
		return readyUsers.size();
	}
	
	
	/**
	 * Update the game at the end of each round.
	 */
	public void update() {
		for (City city : cities) {
			city.fight();
		}
		readyUsers = new HashSet<UUID>();
	}

	/**
	 * Get the number of all users ready for game.
	 * @return the number of all users ready for game.
	 */
	public int getGameReadyNumber() {
		return userMap.size();
	}

	
	/**
	 * Set game ready, add team and user to the game, set initial city for team.
	 * @param team - the team to add.
	 * @param user - the user to add.
	 * @param cityId - city UUID for team.
	 */
	public void setGameReady(Team team, IUser user, UUID cityId) {
		teamMap.put(team.getUUID(), team);
		try {
			userMap.put(user.getUUID(), user);
		} catch (RemoteException e) {
			System.out.println("Falied to get UUID of user: " + user);
			e.printStackTrace();
		}
		City city = baseCities.get(team);
		if (city == null) {
			city = cityMap.get(cityId);
			baseCities.put(team, city);
			city.setOccupy(team);
		}
		city.addTroops(team, user, INITIAL_TROOPS_FOR_USER);
	}

	/**
	 * Get the winner team of the game.
	 * @return the winner team of the game.
	 */
	public Team getWinnerTeam() {
		Map<Team, Integer> map = new HashMap<>();
		for (City city : cities) {
			Team ocp = city.getOccupy();
			if (ocp != null) {
				Integer count = map.get(ocp);
				count = count == null ? 1 : count + 1;
				map.put(ocp, count);
			}
		}
		Team winnerTeam = null;
		int cityNum = 0;
		for (Map.Entry<Team, Integer> entry : map.entrySet()) {
			if (cityNum < entry.getValue()) {
				cityNum = entry.getValue();
				winnerTeam = entry.getKey();
			}
		}
		return winnerTeam;
	}
}
