package ly15_qz18.main.model.data;

import common.DataPacketUserAlgoCmd;
import common.datatype.user.IUserInstallCmdType;

/**
 * When send data packet to install a user command into a remote receiver, the data is UserInstallCmdData, which
 * contains the command and the data type id for this command.
 */
public class UserInstallCmdData implements IUserInstallCmdType {

	private static final long serialVersionUID = 7254681078667406822L;
	private DataPacketUserAlgoCmd<?> cmd;
	private Class<?> id;
	
	/**
	 * Constructor.
	 * @param id The data type for the missing command.
	 * @param cmd The missing command.
	 */
	public UserInstallCmdData(Class<?> id, DataPacketUserAlgoCmd<?> cmd) {
		this.id = id;
		this.cmd = cmd;
	}
	
	@Override
	public DataPacketUserAlgoCmd<?> getCmd() {
		return cmd;
	}

	@Override
	public Class<?> getCmdId() {
		return id;
	}

}