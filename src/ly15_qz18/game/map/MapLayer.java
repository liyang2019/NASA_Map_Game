package ly15_qz18.game.map;


import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import gov.nasa.worldwind.render.Annotation;
import gov.nasa.worldwind.render.GlobeAnnotation;
import java.util.Vector;

import gov.nasa.worldwind.layers.AnnotationLayer;

/**
 * Map layer.
 *
 */
public class MapLayer extends AnnotationLayer implements Serializable{

	/**
	 * Compiler generated unique ID
	 */
	private static final long serialVersionUID = 8976518656781662362L;
	

	/**
	 * constructor
	 */
	public MapLayer() {
		super();
	}
	
	/**
	 * Custom serialization process to serialize non-serializable superclass
	 * @param out Output stream for serialization
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject(); //MapLayer-defined fields can use default serialization process

		Vector<CityAnnotation> annotations = new Vector<CityAnnotation>();
 		for(Annotation annotation: this.getAnnotations()) {
			if(annotation instanceof CityAnnotation){  // must check this before GlobeAnnotation
				annotations.add((CityAnnotation)annotation);
			}
			else if(annotation instanceof GlobeAnnotation) {
				annotations.add(new CityAnnotation((GlobeAnnotation) annotation));
			}
			// ignore all other types of annotations
		}
 		out.writeObject(annotations);
	}

	/**
	 * Custom deserialization process to deserialize non-serializable superclass.
	 * @param in Input stream for deserialization
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();//MapLayer-defined fields can use default deserialization process
		@SuppressWarnings("unchecked")
		Vector<CityAnnotation> annotations = (Vector<CityAnnotation>) in.readObject();
		for(CityAnnotation ta: annotations) {
			addAnnotation(ta);
		}		
	}
	
	private UUID uuid = UUID.randomUUID();
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof MapLayer)) return false;
		return uuid.equals(((MapLayer)other).uuid);	
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}
