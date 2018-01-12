package ly15_qz18.mini.model.cmd;

import common.ICRCmd2ModelAdapter;
import common.IReceiver;

/**
 * Customized command to chat room model adapter.
 *
 */
public interface ICRCmd2ModelCustomAdapter extends ICRCmd2ModelAdapter {
	/**
	 * Add receiver to the chat room model.
	 * @param receiver The receiver to add to chat room model.
	 */
	public void addReceiver(IReceiver receiver);

	/**
	 * Remove receiver from chat room model.
	 * @param receiver The receiver to remove from the chat room model.
	 */
	public void removeReceiver(IReceiver receiver);
}
