package ly15_qz18.mini.model.cmd;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import common.datatype.chatroom.IAddReceiverType;

/**
 * Add receiver command.
 *
 */
public class CRAddReceiverCmd extends DataPacketCRAlgoCmd<IAddReceiverType> {

	private static final long serialVersionUID = 3892222599371359703L;
	private transient ICRCmd2ModelCustomAdapter crCmd2ModelCustomAdapter;
	
	/**
	 * Constructor.
	 * @param crCmd2ModelCustomAdapter - the customized command to chat room model adapter.
	 */
	public CRAddReceiverCmd(ICRCmd2ModelCustomAdapter crCmd2ModelCustomAdapter) {
		this.crCmd2ModelCustomAdapter = crCmd2ModelCustomAdapter;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<IAddReceiverType> host, String... params) {
		IReceiver receiver = host.getData().getReceiverStub();
		crCmd2ModelCustomAdapter.addReceiver(receiver);
		return "receiver: " + receiver + " added";
	}
	
	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2ModelAdpt) {}

}
