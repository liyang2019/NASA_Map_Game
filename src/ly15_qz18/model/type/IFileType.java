package ly15_qz18.model.type;

import java.io.File;

import common.ICRMessageType;
import common.IUserMessageType;

/**
 * The file message type.
 *
 */
public interface IFileType extends IUserMessageType, ICRMessageType {
	
	/**
	 * Get the file.
	 * @return - the file.
	 */
	public File getFile();

}
