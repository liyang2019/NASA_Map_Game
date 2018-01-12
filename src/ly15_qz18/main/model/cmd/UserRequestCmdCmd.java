package ly15_qz18.main.model.cmd;

import java.rmi.RemoteException;

import common.DataPacketUser;
import common.DataPacketUserAlgoCmd;
import common.IUserCmd2ModelAdapter;
import common.IUser;
import common.datatype.IRequestCmdType;
import common.datatype.user.IUserInstallCmdType;
import ly15_qz18.main.model.data.UserInstallCmdData;
import provided.datapacket.DataPacketAlgo;

/**
 * User request command command.
 *
 */
public class UserRequestCmdCmd extends DataPacketUserAlgoCmd<IRequestCmdType> {

	private static final long serialVersionUID = -2499904072080364585L;
	private transient IUserCmd2ModelAdapter userCmd2ModelAdapter;
	private IUser user;
	private transient DataPacketAlgo<String,String> algo;
	
	/**
	 * Constructor.
	 * @param userCmd2ModelAdapter - the command to chat room model adapter.
	 * @param user - the user stub.
	 * @param algo - the algo for the user.
	 */
	public UserRequestCmdCmd(IUserCmd2ModelAdapter userCmd2ModelAdapter, IUser user, DataPacketAlgo<String,String> algo) {
		this.userCmd2ModelAdapter = userCmd2ModelAdapter;
		this.user = user;
		this.algo = algo;
	}

	@Override
	public String apply(Class<?> index, DataPacketUser<IRequestCmdType> host, String... params) {
		IUser sender = host.getSender();
		Class<?> id = host.getData().getCmdId();
		DataPacketUserAlgoCmd<?> cmd = (DataPacketUserAlgoCmd<?>) algo.getCmd(id);
		if (cmd == null) {
			System.out.println("even sender can not find command for id: " + id);
			return "even sender can not find command for id: " + id;
		}
		try {
			sender.receive(new DataPacketUser<IUserInstallCmdType>(
					IUserInstallCmdType.class, 
					new UserInstallCmdData(id, cmd), 
					user));
		} catch (RemoteException e) {
			System.out.println("failed to send cmd back to user: " + user + " for install");
			e.printStackTrace();
		}
		userCmd2ModelAdapter.appendMsg("request send to data packet sender: ", sender.toString());
		return "request command..";
	}

	@Override
	public void setCmd2ModelAdpt(IUserCmd2ModelAdapter userCmd2ModelAdapter) {
		this.userCmd2ModelAdapter = userCmd2ModelAdapter;
	}
}
