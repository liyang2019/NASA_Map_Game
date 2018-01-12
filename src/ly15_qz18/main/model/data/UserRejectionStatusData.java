package ly15_qz18.main.model.data;

import common.DataPacketUser;
import common.datatype.user.IUserRejectionStatusType;

/**
 * User rejection data packet data.
 */
public class UserRejectionStatusData implements IUserRejectionStatusType {

	private static final long serialVersionUID = 1305510581656347866L;
	private DataPacketUser<?> dataPacket;
	private String failureInfo;
	
	/**
	 * Constructor.
	 * @param dataPacket - the rejected data packet.
	 * @param failureInfo - the failure info.
	 */
	public UserRejectionStatusData(DataPacketUser<?> dataPacket, String failureInfo) {
		this.dataPacket = dataPacket;
		this.failureInfo = failureInfo;
	}
	
	@Override
	public DataPacketUser<?> getOriginalData() {
		return dataPacket;
	}

	@Override
	public String getFailureInfo() {
		return failureInfo;
	}

}
