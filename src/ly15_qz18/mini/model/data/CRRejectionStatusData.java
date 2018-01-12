package ly15_qz18.mini.model.data;

import common.DataPacketCR;
import common.ICRMessageType;
import common.datatype.chatroom.ICRRejectionStatusType;

/**
 * Chat room data packet rejection data.
 *
 */
public class CRRejectionStatusData implements ICRRejectionStatusType {

	private static final long serialVersionUID = -4729290876696332072L;
	private DataPacketCR<? extends ICRMessageType> dataPacket;
	private String info;
	
	/**
	 * Constructor.
	 * @param dataPacket - the data packet rejected.
	 * @param info - the rejection info.
	 */
	public CRRejectionStatusData(DataPacketCR<? extends ICRMessageType> dataPacket, String info) {
		this.dataPacket = dataPacket;
		this.info = info;
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
