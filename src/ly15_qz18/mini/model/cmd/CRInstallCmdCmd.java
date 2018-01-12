package ly15_qz18.mini.model.cmd;

import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.IReceiver;
import common.datatype.chatroom.ICRInstallCmdType;
import provided.datapacket.DataPacketAlgo;
import provided.mixedData.MixedDataKey;

/**
 * The chat room install command command.
 *
 */
public class CRInstallCmdCmd extends DataPacketCRAlgoCmd<ICRInstallCmdType> {
	
	private static final long serialVersionUID = -4630685934397538223L;
	private transient ICRCmd2ModelAdapter crCmd2CRModelAdapter;
	private transient DataPacketAlgo<String,String> algo;
	private IReceiver receiver;

	/**
	 * Constructor.
	 * @param crCmd2CRModelAdapter - the chat room command to model adapter.
	 * @param receiver - the receiver for the chat room.
	 * @param algo - the algo for the chat room receiver.
	 */
	public CRInstallCmdCmd(ICRCmd2ModelAdapter crCmd2CRModelAdapter, IReceiver receiver, DataPacketAlgo<String,String> algo) {
		this.crCmd2CRModelAdapter = crCmd2CRModelAdapter;
		this.receiver = receiver;
		this.algo = algo;
	}

	@Override
	public String apply(Class<?> index, DataPacketCR<ICRInstallCmdType> dataPacket, String... params) {
		try {
			UUID receiverUUID = receiver.getUUID();
			ICRInstallCmdType data = dataPacket.getData();
			Class<?>  id = data.getCmdId();
			DataPacketCRAlgoCmd<?> cmd = data.getCmd();
			cmd.setCmd2ModelAdpt(crCmd2CRModelAdapter);
			algo.setCmd(id, cmd);
			crCmd2CRModelAdapter.appendMsg("missing command installed.", receiver.toString());
			DataPacketCR<?> cachedDataPacket = crCmd2CRModelAdapter.get(new MixedDataKey<>(
					receiverUUID, 
					id.toString(), 
					DataPacketCR.class));
			cmd.apply(id, cachedDataPacket);
			crCmd2CRModelAdapter.appendMsg("cached data packet executed.", receiver.toString());
			return "install command and execute";
		} catch (RemoteException e) {
			System.out.println("Failed to get UUID of receiver: " + receiver);
			e.printStackTrace();
			return "failed to get sender UUID";
		}
	}


	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2CRModelAdapter) {
		this.crCmd2CRModelAdapter = crCmd2CRModelAdapter;
	}
}
