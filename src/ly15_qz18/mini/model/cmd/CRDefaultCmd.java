package ly15_qz18.mini.model.cmd;

import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketCR;
import common.DataPacketCRAlgoCmd;
import common.ICRCmd2ModelAdapter;
import common.ICRMessageType;
import common.IReceiver;
import common.datatype.IRequestCmdType;
import ly15_qz18.model.data.RequestCmdData;
import provided.mixedData.MixedDataKey;

/**
 * The chat room default command, request missing command functionality is inside.
 *
 */
public class CRDefaultCmd extends DataPacketCRAlgoCmd<ICRMessageType> {

	private static final long serialVersionUID = -5934308402384090411L;
	private transient ICRCmd2ModelAdapter crCmd2CRModelAdapter;
	private IReceiver receiver;
	
	/**
	 * Constructor.
	 * @param receiver - the receiver of the chat room.
	 * @param crCmd2CRModelAdapter - the chat room command to model adapter.
	 */
	public CRDefaultCmd(ICRCmd2ModelAdapter crCmd2CRModelAdapter, IReceiver receiver) {
		this.receiver = receiver;
		this.crCmd2CRModelAdapter = crCmd2CRModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketCR<ICRMessageType> dataPacket, String... params) {
		IReceiver sender = dataPacket.getSender();
		try {
			UUID receiverUUID = receiver.getUUID();
			crCmd2CRModelAdapter.put(
					new MixedDataKey<>(
							receiverUUID, // sender's UUID as UUID in MixedDataDictionary
							index.toString(), // string representation of data packets's type as description in MixedDataDictionary
							DataPacketCR.class), // DataPacketCR.class as type in MixedDataDictionary
					dataPacket);
			crCmd2CRModelAdapter.appendMsg("missing cmd for type: " + index.toString() + ", requesting cmd from sender: " + sender, sender.toString());
		} catch (RemoteException e) {
			System.out.println("Failed to get UUID of sender: " + sender);
			e.printStackTrace();
			return "failed to get sender UUID";
		}
		try {
			sender.receive(new DataPacketCR<IRequestCmdType>(IRequestCmdType.class, new RequestCmdData(index), receiver));
		} catch (RemoteException e) {
			System.out.println("failed to request cmd from sender: + " + sender);
			e.printStackTrace();
		}
		return "missing command..";
	}

	@Override
	public void setCmd2ModelAdpt(ICRCmd2ModelAdapter crCmd2CRModelAdapter) {
		this.crCmd2CRModelAdapter = crCmd2CRModelAdapter;
	}
}
