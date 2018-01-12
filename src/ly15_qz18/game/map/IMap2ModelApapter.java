package ly15_qz18.game.map;

import java.util.UUID;


/**
 * Map
 *
 */
public interface IMap2ModelApapter {

	/**
	 * send left click message
	 * @param cityID the UUID of the city being clicked on
	 */
	public void sendLeftClick(UUID cityID);
	
	/**
	 * send right click message
	 * @param cityID the UUID of the city being clicked on
	 */
	public void sendRightClick(UUID cityID);
}
