package ly15_qz18.game.model;

import java.io.Serializable;
import java.util.UUID;

import gov.nasa.worldwind.geom.Position;

/**
 * Place class.
 *
 */
public class Place implements Serializable {
	
	private static final long serialVersionUID = 7305405631003299359L;
	private String   _name;
	private Position _pos;
	private UUID _uuid;
	
	/**
	 * @param name - the name of the place.
	 * @param pos - the position of the place.
	 */
	public Place(String name, Position pos) {
		_name = name;
		_pos = pos;
		_uuid = new UUID(name.hashCode(), pos.hashCode());
	}
	
	/**
	 * Get the position of the place.
	 * @return - the position.
	 */
	public Position getPosition() {
		return _pos;
	}
	
	/**
	 * Get the name of the place.
	 * @return name of the place.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Get the UUID of the place.
	 * @return UUID of the place.
	 */
	public UUID getUUID() {
		return _uuid;
	}
	
	@Override
	public String toString() {
		return _name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Place)) return false;
		Place that = (Place) obj;
		return this._uuid == that._uuid;
	}
	
	@Override
	public int hashCode() {
		return _uuid.hashCode();
	}
}
