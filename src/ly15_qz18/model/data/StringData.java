package ly15_qz18.model.data;

import ly15_qz18.model.type.IStringType;

/**
 * The string data
 *
 */
public class StringData implements IStringType {

	private static final long serialVersionUID = -8874155567870363120L;
	private String text;
	
	/**
	 * Constructor.
	 * @param text - the text.
	 */
	public StringData(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

}
