package ly15_qz18.model.type;

import java.io.Serializable;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 * The string message type.
 *
 */
public interface IStringType extends IUserMessageType, ICRMessageType, Serializable {
	
	/**
	 * Get the string of this string message type.
	 * @return - the string of this string message type.
	 */
	public String getText();
}
