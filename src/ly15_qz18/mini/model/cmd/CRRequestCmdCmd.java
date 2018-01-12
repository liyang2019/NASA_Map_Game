package ly15_qz18.mini.model.cmd;

import java.rmi.RemoteException;

import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.DataPacketCR;
import common.IReceiver;
import common.datatype.IRequestCmdType;
import common.datatype.chatroom.ICRInstallCmdType;
import ly15_qz18.mini.model.data.CRInstallCmdData;
import provided.datapacket.DataPacketAlgo;

/**
 * Request command cmd.
 *
 */
public class CRRequestCmdCmd extends DataPacketCRAlgoCmd<IRequestCmdType> {

	private static final long serialVersionUID = 6403316234950613424L;
	private transient ICRCmd2ModelAdapter crCmd2CRModelAdapter;
	private IReceiver receiver;
	private transient DataPacketAlgo<String,String> algo;
	
	/**
	 * Constructor.
	 * @param crCmd2CRModelAdapter - the command to chat room model adapter.
	 * @param receiver - the receiver stub.
	 * @param algo - the algo for the receiver.
	 */
	public CRRequestCmdCmd(ICRCmd2ModelAdapter crCmd2CRModelAdapter, IReceiver receiver, DataPacketAlgo<String,String> algo) {
		this.crCmd2CRModelAdapter = crCmd2CRModelAdapter;
		this.receiver = receiver;
		this.algo = algo;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<IRequestCmdType> host, String... params) {
		IReceiver sender = host.getSender();
		Class<?> id = host.getData().getCmdId();
		DataPacketCRAlgoCmd<?> cmd = (DataPacketCRAlgoCmd<?>) algo.getCmd(id);
		if (cmd == null) {
			System.out.println("even sender can not find command for id: " + id);
			return "even sender can not find command for id: " + id;
		}
		try {
			sender.receive(new DataPacketCR<ICRInstallCmdType>(
					ICRInstallCmdType.class, 
					new CRInstallCmdData(id, cmd), 
					receiver));
		} catch (RemoteException e) {
			System.out.println("failed to send cmd back to receiver: " + receiver + " for install");
			e.printStackTrace();
		}
		crCmd2CRModelAdapter.appendMsg("request send to data packet sender: ", sender.toString());
		return "request command..";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2CRModelAdapter) {
		this.crCmd2CRModelAdapter = crCmd2CRModelAdapter;
	}
}
