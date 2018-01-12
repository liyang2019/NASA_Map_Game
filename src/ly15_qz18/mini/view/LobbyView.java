package ly15_qz18.mini.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.GridLayout;
import javax.swing.JSpinner;

/**
 * The chat room view.
 * @param <UserObj> the user generic object.
 * @param <ChatRoomObj> chat room generic object.
 */
public class LobbyView<UserObj, ChatRoomObj> extends ChatRoomView<UserObj, ChatRoomObj> {

	private static final long serialVersionUID = 8653973223050019950L;
	private final JButton btnStartGame = new JButton("Start Game");
	private final JLabel lblTeamNumber = new JLabel("Team Number:");
	private final JLabel lblTeamCapacity = new JLabel("Team Capacity:");
	private final JSpinner spinnerTeamNumber = new JSpinner();
	private final JSpinner spinnerTeamCapacity = new JSpinner();

	/**
	 * Constructor.
	 * @param _modelAdapter view to model adapter.
	 * @param chatRoomName the name of the chat room.
	 */
	public LobbyView(ICRView2CRModelAdapter<UserObj, ChatRoomObj> _modelAdapter, String chatRoomName) {
		super(_modelAdapter, chatRoomName);
		initGUI();
	}
	
	private void initGUI() {
		pnlNorth.setLayout(new GridLayout(1, 0));
		pnlNorth.add(lblTeamNumber);
		pnlNorth.add(spinnerTeamNumber);
		spinnerTeamNumber.setValue(2);
		pnlNorth.add(lblTeamCapacity);
		pnlNorth.add(spinnerTeamCapacity);
		spinnerTeamCapacity.setValue(5);
		pnlNorth.add(btnStartGame);
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_modelAdapter.startGame((int) spinnerTeamNumber.getValue(), (int) spinnerTeamCapacity.getValue());
			}
		});
	}
	
	
}
