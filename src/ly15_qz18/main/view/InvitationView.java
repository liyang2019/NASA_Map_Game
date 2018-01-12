package ly15_qz18.main.view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The join chat room invitation view.
 * @param <ChatRoomObj> - the chat room generic object.
 * @param <UserObj> - the user generic object. 
 *
 */
public class InvitationView<UserObj, ChatRoomObj> extends JFrame {

	private static final long serialVersionUID = 5534784514074007255L;
	private UserObj sender;
	private ChatRoomObj chatRoom;
	private IInvitationView2MainModelAdapter<UserObj, ChatRoomObj> invitationView2MainModelAdapter;
	
	private JPanel contentPane;
	private final JLabel lblSender = new JLabel();
	private final JPanel panel = new JPanel();
	private final JButton btnAccept = new JButton("accept");
	private final JButton btnReject = new JButton("cancel");
	private final JLabel lblChatRoom = new JLabel("New label");

	/**
	 * Create the frame.
	 * @param sender - the sender of the invitation.
	 * @param chatRoom - the chat room of the invitation.
	 * @param invitationView2MainModelAdapter - the invitation view to main model adapter.
	 */
	public InvitationView(UserObj sender, ChatRoomObj chatRoom, IInvitationView2MainModelAdapter<UserObj, ChatRoomObj> invitationView2MainModelAdapter) {
		this.sender = sender;
		this.chatRoom = chatRoom;
		this.invitationView2MainModelAdapter = invitationView2MainModelAdapter;
		initGUI();
	}
	
	private void initGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 260, 150);
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		panel.setPreferredSize(new Dimension(250, 120));
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		lblSender.setHorizontalAlignment(SwingConstants.CENTER);
		lblSender.setText(sender + " invited you to join");
		panel.add(lblSender);
		lblChatRoom.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblChatRoom);
		lblChatRoom.setText(chatRoom.toString());
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				invitationView2MainModelAdapter.acceptInvitation(sender, chatRoom);
				dispose();
			}
		});
		panel.add(btnAccept);		
		btnReject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				invitationView2MainModelAdapter.rejectInvitation(sender, chatRoom);
				dispose();
			}
		});
		panel.add(btnReject);
	}

	/**
	 * Start the invitation view.
	 */
	public void start() {
		setVisible(true);
	}

}
