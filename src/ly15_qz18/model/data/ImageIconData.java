package ly15_qz18.model.data;

import javax.swing.ImageIcon;

import ly15_qz18.model.type.IImageIconType;

/**
 * The image icon data.
 *
 */
public class ImageIconData implements IImageIconType {

	private static final long serialVersionUID = 2691297722368154072L;
	private ImageIcon imageIcon;
	
	/**
	 * Constructor.
	 * @param imageIcon - the image icon.
	 */
	public ImageIconData(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	@Override
	public ImageIcon getImageIcon() {
		return imageIcon;
	}

}
