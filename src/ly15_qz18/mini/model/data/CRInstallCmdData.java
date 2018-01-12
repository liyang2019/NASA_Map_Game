package ly15_qz18.mini.model.data;

import common.DataPacketCRAlgoCmd;
import common.datatype.chatroom.ICRInstallCmdType;

/**
 * When send data packet to install a chat room command into a remote receiver, the data is CRInstallCmdData, which
 * contains the command and the data type id for this command.
 */
public class CRInstallCmdData implements ICRInstallCmdType {

	private static final long serialVersionUID = 7254681078667406822L;
	private DataPacketCRAlgoCmd<?> cmd;
	private Class<?> id;
	
	/**
	 * Constructor.
	 * @param id The data type for the missing command.
	 * @param cmd The missing command.
	 */
	public CRInstallCmdData(Class<?> id, DataPacketCRAlgoCmd<?> cmd) {
		this.id = id;
		this.cmd = cmd;
	}
	
	@Override
	public DataPacketCRAlgoCmd<?> getCmd() {
		return cmd;
	}

	@Override
	public Class<?> getCmdId() {
		return id;
	}

}
