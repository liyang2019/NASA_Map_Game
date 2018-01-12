package ly15_qz18.game.map;


import java.awt.Color;


import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import common.IUser;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;
import ly15_qz18.game.model.City;
import ly15_qz18.model.object.Team;

/**
 * City annotation.
 *
 */
public class CityAnnotation extends DummyGlobeAnnotation implements Serializable{

	/**
	 * Compiler generated unique ID
	 */
	private static final long serialVersionUID = -2549413179108957561L;
	
	/**
	 * The city the annotation hold
	 */
	private City city;
	
	/**
	 * The localuser hold the annotation
	 */
	private IUser localuser;
	
	/**
	 * The team the local user belongs to
	 */
	private Team localteam;
	
	
	private Color backgroundColor = Color.GRAY;

	
	/**
	 * Constructor
	 * @param city city annotation
	 * @param user - local user.
	 * @param team - local team.
	 */
	public CityAnnotation(City city, IUser user, Team team) {
		super(city.getName(),city.getPosition());
		this.city = city;
		this.localuser = user;
		this.localteam = team;
		update();	
	}
	
	
	/**
	 * constructor
	 * @param ga globe annotation used for create the city annotation
	 */
	public CityAnnotation(GlobeAnnotation ga) {
		super("default",ga.getPosition());
		this.city = new City("default", Position.fromDegrees(0, 0), 0);
	}
	
	/**
	 * return the city annotation's city
	 * @return the city
	 */
	public City getCity() {
		return city;
	}
	
	
	/**
	 * Update the annotation.
	 */
	public void update(){
		AnnotationAttributes newAttri = new DefaultAnnotationAttributes();
		Set<IUser> users = city.getUsers(localteam);
		if (users == null) {
			return;
		}
		String text = this.city.getName() + " - " + city.getResource();
		for (IUser user : users) {
			if (user.equals(localuser)) {
				text += "\n" + "you: " + city.getTroops(user);
			} else {
				text += "\n" + user + ": " + city.getTroops(user);
			}
		}
		setText(text);
		newAttri.setBackgroundColor(this.backgroundColor);
		if (this.city.getOccupy() != null && localteam != null && this.city.getOccupy().equals(localteam)) {
			newAttri.setBackgroundColor(this.localteam.getTeamColor());
		}
		this.setAttributes(newAttri);		
	}

	/**
	 * Custom serialization process to serialize non-serializable superclass
	 * @param out Output stream for serialization
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject(); //ToggleAnnotation-defined fields can use default serialization process
		out.writeObject(new DefaultAnnotationAttributes(this.getAttributes())); // convert the attributes to something serializable first
		out.writeUTF(this.getText());  // send out the superclass's annotation string
		Position p = this.getPosition(); // get the Position which is non-serializable 
		out.writeDouble(p.getLatitude().getDegrees()); // send out latitude
		out.writeDouble(p.getLongitude().getDegrees()); // send out longitude
		out.writeDouble(p.getElevation()); // send out elevation
		out.writeDouble(this.getMinActiveAltitude()); // send out min active altitude
		out.writeDouble(this.getMaxActiveAltitude()); // send out max active altitude
	}

	/**
	 * Custom deserialization process to deserialize non-serializable superclass.
	 * @param in Input stream for deserialization
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();//ToggleAnnotation-defined fields can use default deserialization process
		this.setAttributes((AnnotationAttributes) in.readObject());
		this.setText(in.readUTF()); // read in superclass's annotation string
		double lat = in.readDouble(); // read in latitude
		double lon = in.readDouble(); // read in longitude
		double elev = in.readDouble(); // read in elevation
		this.setPosition(Position.fromDegrees(lat, lon, elev));	// set Position	
		this.setMinActiveAltitude(in.readDouble()); // read in min active altitude
		this.setMaxActiveAltitude(in.readDouble()); // read in max active altitude
	}
	
	/**
	 * UUID
	 */
	private UUID uuid = UUID.randomUUID();
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof CityAnnotation)) return false;
		return uuid.equals(((CityAnnotation)other).uuid);	
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}

/**
 * Dummy non-serializable superclass that provides a no-arg constructor that is needed by the serialization 
 * process of a serializable subclass in order to initialize a non-serializable superclass.
 */
class DummyGlobeAnnotation extends GlobeAnnotation {
	
	/**
	 * Constructor used by ToggleAnnotation
	 * @param unselectedText  - text to display when annotation is not selected
	 * @param pos             - position of annotation
	 * @param attr            - annotation attributes
	 */
	public DummyGlobeAnnotation(String unselectedText, Position pos, AnnotationAttributes attr) {
		super(unselectedText, pos, attr);
	}
	
	/**
	 * No-arg constructor used during serialization.  Assume no useful values are set here.
	 */
	protected DummyGlobeAnnotation(String name,Position pos){
		super(name, pos, new DefaultAnnotationAttributes());
	}	
}