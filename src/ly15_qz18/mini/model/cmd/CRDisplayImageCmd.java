package ly15_qz18.mini.model.cmd;

import java.awt.Component;
import java.awt.Image;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IComponentFactory;
import ly15_qz18.model.type.IImageIconType;

/**
 * The command to display the ImageIcon in the data packet.
 *
 */
public class CRDisplayImageCmd extends DataPacketCRAlgoCmd<IImageIconType> {
	
	private static final long serialVersionUID = -4390262620406962086L;
	/**
	 * ImageIcon max height;
	 */
	private final int IMAGE_ICON_MAXHEIGHT = 150;
	
	/**
	 * Command to chat room model adapter.
	 */
	private transient ICRCmd2ModelAdapter crCmd2ModelAdapter;
	
	/**
	 * Constructor.
	 * @param crCmd2ModelAdapter is the command to chat room mode adapter.
	 */
	public CRDisplayImageCmd(ICRCmd2ModelAdapter crCmd2ModelAdapter) {
		this.crCmd2ModelAdapter = crCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketCR<IImageIconType> host, String... params) {
		ImageIcon icon = host.getData().getImageIcon();
		try {
			crCmd2ModelAdapter.buildScrollableComponent(new IComponentFactory() {
				@Override
				public Component makeComponent() {
					double aspectRatio = (double) icon.getIconWidth() / icon.getIconHeight();
					int newHeight = Math.min(icon.getIconHeight(), IMAGE_ICON_MAXHEIGHT);
					int newWidth = (int) (newHeight * aspectRatio);
					Image image = icon.getImage();
					ImageIcon resizedIcon = new ImageIcon(image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));
					return new JLabel(resizedIcon);
				}
			}, host.getSender().getUserStub().getName());
		} catch (RemoteException e) {
			System.out.println("failed to get the sender's name, sender: " + host.getSender());
			e.printStackTrace();
		}
		return icon.toString();
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2ModelAdapter) {
		this.crCmd2ModelAdapter = crCmd2ModelAdapter;
	}
}
