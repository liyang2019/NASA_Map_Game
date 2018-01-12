package ly15_qz18.model.data;

import java.io.File;

import ly15_qz18.model.type.IFileType;

/**
 * The file data.
 *
 */
public class FileData implements IFileType {

	private static final long serialVersionUID = 7876006281891873796L;
	private File file;
	
	/**
	 * Constructor.
	 * @param file - the file.
	 */
	public FileData(File file) {
		this.file = file;
	}
	
	@Override
	public File getFile() {
		return file;
	}

}
