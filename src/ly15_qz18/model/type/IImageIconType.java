package ly15_qz18.model.type;

import java.io.Serializable;

import javax.swing.ImageIcon;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 * The image icon message type.
 *
 */
public interface IImageIconType extends IUserMessageType, ICRMessageType, Serializable {
	
	/**
	 * Get the image icon.
	 * @return - the image icon.
	 */
	public ImageIcon getImageIcon();

}
