package ly15_qz18.main.model.cmd;

import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketUserAlgoCmd;
import common.DataPacketUser;
import common.IUserCmd2ModelAdapter;
import common.IUser;
import common.datatype.user.IUserInstallCmdType;
import provided.datapacket.DataPacketAlgo;
import provided.mixedData.MixedDataKey;

/**
 * User install command command.
 *
 */
public class UserInstallCmdCmd extends DataPacketUserAlgoCmd<IUserInstallCmdType> {
	
	private static final long serialVersionUID = -5842474321493062913L;
	private transient IUserCmd2ModelAdapter userCmd2ModelAdapter;
	private transient DataPacketAlgo<String,String> algo;
	private IUser user;

	/**
	 * Constructor.
	 * @param userCmd2ModelAdapter - the user command to model adapter.
	 * @param algo - the algo for the local user.
	 * @param user - the local user.
	 */
	public UserInstallCmdCmd(IUserCmd2ModelAdapter userCmd2ModelAdapter, IUser user, DataPacketAlgo<String,String> algo) {
		this.userCmd2ModelAdapter = userCmd2ModelAdapter;
		this.algo = algo;
		this.user = user;
	}

	@Override
	public String apply(Class<?> index, DataPacketUser<IUserInstallCmdType> dataPacket, String... params) {
		try {
			UUID userUUID = user.getUUID();
			IUserInstallCmdType data = dataPacket.getData();
			Class<?>  id = data.getCmdId();
			DataPacketUserAlgoCmd<?> cmd = data.getCmd();
			cmd.setCmd2ModelAdpt(userCmd2ModelAdapter);
			algo.setCmd(id, cmd);
			userCmd2ModelAdapter.appendMsg("missing command installed.", user.toString());
			DataPacketUser<?> cachedDataPacket = userCmd2ModelAdapter.get(new MixedDataKey<>(
					userUUID, 
					id.toString(), 
					DataPacketUser.class));
			cmd.apply(id, cachedDataPacket);
			userCmd2ModelAdapter.appendMsg("cached data packet executed.", user.toString());
			return "install command and execute";
		} catch (RemoteException e) {
			System.out.println("Failed to get UUID of user: " + user);
			e.printStackTrace();
			return "failed to get sender UUID";
		}
	}


	@Override
	public void setCmd2ModelAdpt(IUserCmd2ModelAdapter userCmd2ModelAdapter) {
		this.userCmd2ModelAdapter = userCmd2ModelAdapter;
	}
}
