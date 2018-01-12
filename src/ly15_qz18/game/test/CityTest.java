package ly15_qz18.game.test;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import common.IUser;
import gov.nasa.worldwind.geom.Position;
import ly15_qz18.game.model.City;
import ly15_qz18.model.object.Team;
import ly15_qz18.model.object.User;

/**
 * Unit tests for the city operation.
 * @author Li Yang
 *
 */
public class CityTest {

	/**
	 * city init tests
	 */
	@Test
	public void testInit() {
		City city = new City("Houston", Position.fromDegrees(29.7604, 95.3698), 20);
		assertEquals("city name", "Houston", city.getName());
		assertEquals("init", 20, city.getResource());
		assertEquals("init", null, city.getOccupy());
		assertEquals("lat", 29.7604, city.getPosition().latitude.degrees, 1e-5);
		assertEquals("lon", 95.3698, city.getPosition().longitude.degrees, 1e-5);
	}
	
	/**
	 * city fight tests 1.
	 */
	@Test
	public void testFight1() {
		City city = new City("Houston", Position.fromDegrees(29.7604, 95.3698), 20);
		Team team1 = new Team("team1", null, 5, Color.RED);
		Team team2 = new Team("team2", null, 5, Color.RED);
		IUser user1 = new User("Li1", null);
		IUser user2 = new User("Li2", null);
		IUser user3 = new User("Li3", null);
		IUser user4 = new User("Li4", null);
		city.addTroops(team1, user1, 10);
		assertEquals("after add troops", 20, city.getResource());
		assertEquals("after add troops", 10, city.getTroops(user1));
		assertEquals("after add troops", 0, city.getTroops(user2));
		city.addTroops(team1, user2, 20);
		assertEquals("after add troops", 20, city.getResource());
		assertEquals("after add troops", 10, city.getTroops(user1));
		assertEquals("after add troops", 20, city.getTroops(user2));
		city.addTroops(team2, user3, 15);
		assertEquals("after add troops", 15, city.getTroops(user3));
		assertEquals("after add troops", 0, city.getTroops(user4));
		city.fight();
		double ratio = (double) (20 + 10 - 15 + 20) / (double) 30;
		assertEquals("after fight", (int) (10 * ratio), city.getTroops(user1));
		assertEquals("after fight", (int) (20 * ratio), city.getTroops(user2));
		assertEquals("after fight", 0, city.getTroops(user3));
		assertEquals("after fight", 0, city.getResource());
	}
	
	/**
	 * city fight tests 2.
	 */
	@Test
	public void testFight2() {
		City city = new City("Houston", Position.fromDegrees(29.7604, 95.3698), 20);
		Team team1 = new Team("team1", null, 5, Color.RED);
		Team team2 = new Team("team2", null, 5, Color.RED);
		Team team3 = new Team("team3", null, 5, Color.RED);
		Team team4 = new Team("team4", null, 5, Color.RED);
		Team team5 = new Team("team5", null, 5, Color.RED);
		IUser user1 = new User("Li1", null);
		IUser user2 = new User("Li2", null);
		IUser user3 = new User("Li3", null);
		IUser user4 = new User("Li4", null);
		IUser user5 = new User("Li5", null);
		city.addTroops(team1, user1, 10);
		city.addTroops(team2, user2, 20);
		city.addTroops(team3, user3, 30);
		city.addTroops(team4, user4, 40);
		city.addTroops(team5, user5, 50);
		city.fight();
		double ratio = (double) (50 - 40 + 20) / (double) 50;
		assertEquals("after fight", 0, city.getTroops(user1));
		assertEquals("after fight", 0, city.getTroops(user2));
		assertEquals("after fight", 0, city.getTroops(user3));
		assertEquals("after fight", 0, city.getTroops(user4));
		assertEquals("after fight", (int) (ratio * 50), city.getTroops(user5));
		assertEquals("winner", team5, city.getOccupy());
	}
	
	
	
	/**
	 * city army transfer tests
	 */
	@Test
	public void testTransfer() {
		City city = new City("Houston", Position.fromDegrees(29.7604, 95.3698), 20);
		Team team1 = new Team("team1", null, 5, Color.RED);
		Team team2 = new Team("team2", null, 5, Color.RED);
		IUser user1 = new User("Li1", null);
		IUser user2 = new User("Li2", null);
		city.addTroops(team1, user1, 10);
		city.addTroops(team2, user2, 20);
//		City city1 = new City("Austin", Position.fromDegrees(29.7604, 95.3698), 30);
//		city.transferTroops(city1, team2, user2, 5);
//		assertEquals("after transfer", 10, city.getTroops(user1));
//		assertEquals("after transfer", 15, city.getTroops(user2));
//		assertEquals("after transfer", 0, city1.getTroops(user1));
//		assertEquals("after transfer", 5, city1.getTroops(user2));
	}
}
