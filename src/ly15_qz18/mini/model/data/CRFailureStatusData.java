package ly15_qz18.mini.model.data;

import common.DataPacketCR;
import common.ICRMessageType;
import common.datatype.chatroom.ICRFailureStatusType;

/**
 * The failure status data.
 * @param <T> - the generic data type.
 */
public class CRFailureStatusData<T extends ICRMessageType> implements ICRFailureStatusType {

	private static final long serialVersionUID = -3067315889040770694L;
	private String info;
	private DataPacketCR<? extends ICRMessageType> dataPacket;
	
	/**
	 * Constructor.
	 * @param info - The failure info.
	 * @param dataPacket - The original sent dataPacket.
	 */
	public CRFailureStatusData(String info, DataPacketCR<? extends ICRMessageType> dataPacket) {
		this.info = info;
		this.dataPacket = dataPacket;
	}
	
	@Override
	public DataPacketCR<? extends ICRMessageType> getOriginalData() {
		return dataPacket;
	}

	@Override
	public String getFailureInfo() {
		return info;
	}

}
