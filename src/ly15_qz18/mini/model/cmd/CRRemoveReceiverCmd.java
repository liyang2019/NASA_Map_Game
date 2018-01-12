package ly15_qz18.mini.model.cmd;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import common.datatype.chatroom.IRemoveReceiverType;

/**
 * Remove receiver command.
 *
 */
public class CRRemoveReceiverCmd extends DataPacketCRAlgoCmd<IRemoveReceiverType> {

	private static final long serialVersionUID = 4660742865033672487L;
	private transient ICRCmd2ModelCustomAdapter crCmd2ModelCustomAdapter;
	
	/**
	 * Constructor.
	 * @param crCmd2ModelCustomAdapter - the customized command to chat room model adapter.
	 */
	public CRRemoveReceiverCmd(ICRCmd2ModelCustomAdapter crCmd2ModelCustomAdapter) {
		this.crCmd2ModelCustomAdapter = crCmd2ModelCustomAdapter;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<IRemoveReceiverType> host, String... params) {
		IReceiver receiver = host.getData().getReceiverStub();
		crCmd2ModelCustomAdapter.removeReceiver(receiver);
		return "receiver: " + receiver + " removed";
	}
	
	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2ModelAdpt) {}

}
