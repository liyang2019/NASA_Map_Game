package ly15_qz18.main.model.cmd;

import java.rmi.RemoteException;
import java.util.UUID;

import common.DataPacketUser;
import common.DataPacketUserAlgoCmd;
import common.IUserCmd2ModelAdapter;
import common.IUserMessageType;
import common.IUser;
import common.datatype.IRequestCmdType;
import ly15_qz18.model.data.RequestCmdData;
import provided.mixedData.MixedDataKey;

/**
 * User default command.
 *
 */
public class UserDefaultCmd extends DataPacketUserAlgoCmd<IUserMessageType> {

	private static final long serialVersionUID = -5101483481128513177L;
	private transient IUserCmd2ModelAdapter userCmd2ModelAdapter;
	private IUser user;
	
	/**
	 * Constructor.
	 * @param user - the local user
	 * @param userCmd2ModelAdapter - the user command to model adapter.
	 */
	public UserDefaultCmd(IUserCmd2ModelAdapter userCmd2ModelAdapter, IUser user) {
		this.user = user;
		this.userCmd2ModelAdapter = userCmd2ModelAdapter;
	}
	
	@Override
	public String apply(Class<?> index, DataPacketUser<IUserMessageType> dataPacket, String... params) {
		IUser sender = dataPacket.getSender();
		try {
			UUID userUUID = user.getUUID();
			userCmd2ModelAdapter.put(
					new MixedDataKey<>(
							userUUID, // sender's UUID as UUID in MixedDataDictionary
							index.toString(), // string representation of data packets's type as description in MixedDataDictionary
							DataPacketUser.class), // DataPacketUser.class as type in MixedDataDictionary
					dataPacket);
			userCmd2ModelAdapter.appendMsg("missing cmd, requesting cmd from sender: " + sender, sender.toString());
		} catch (RemoteException e1) {
			System.out.println("Failed to get UUID of user: " + user);
			e1.printStackTrace();
		}
		try {
			sender.receive(new DataPacketUser<IRequestCmdType>(IRequestCmdType.class, new RequestCmdData(index), user));
		} catch (RemoteException e) {
			System.out.println("failed to request cmd from sender: + " + sender);
			e.printStackTrace();
		}
		return "missing command..";
	}

	@Override
	public void setCmd2ModelAdpt(IUserCmd2ModelAdapter userCmd2ModelAdapter) {
		this.userCmd2ModelAdapter = userCmd2ModelAdapter;
	}

}
