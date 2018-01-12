package ly15_qz18.main.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Dimension;

import javax.swing.border.TitledBorder;

import common.IComponentFactory;
import ly15_qz18.mini.view.ChatRoomView;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

/**
 * The main GUI view.
 * @param <UserObj> the object in the user list.
 * @param <ChatRoomObj> the object in the chat room list.
 */
public class MainView<UserObj, ChatRoomObj> extends JFrame {

	private static final long serialVersionUID = 3027338569906051915L;
	
	private IMainView2MainModelAdapter<UserObj, ChatRoomObj> _view2ModelAdapter;
	private Map<ChatRoomObj, Component> chatRoomViewMap = new HashMap<>();
	
	private final JPanel contentPane = new JPanel();;
	private final JTextField tfUserName = new JTextField("Li Yang");;
	private final JTextField tfIP = new JTextField();;
	
	private final JPanel panel_top = new JPanel();
	private final JLabel lblUsername = new JLabel("User name:");
	private final JPanel pnlConnectToIP = new JPanel();
	private final JButton btnLogIn = new JButton("Log In");
	private final JButton btnConnect = new JButton("Connect");
	private final JComboBox<UserObj> cbUsers = new JComboBox<>();
	private final JPanel panel_4 = new JPanel();
	private final JButton btnExit = new JButton("Exit Program");
	private final JTabbedPane panel_tab = new JTabbedPane(JTabbedPane.TOP);
	private final JPanel pnlInfo = new JPanel();
	
	private final JPanel pnlJoinChatRoom = new JPanel();
	private final JComboBox<ChatRoomObj> cbChatRooms = new JComboBox<>();
	private final JButton btnJoin = new JButton("Join Chat Room");
	
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTextArea taInfo = new JTextArea();
	private final JPanel pnlLogIn = new JPanel();
	private final JLabel lblChatRoomList = new JLabel("Chat Room list");
	private final JLabel lblConnectedUsers = new JLabel("Connected Users:");
	private final JLabel lblUser = new JLabel("User: ");

	/**
	 * Constructor.
	 * @param _view2ModelAdapter - main view to main model adapter.
	 */
	public MainView(IMainView2MainModelAdapter<UserObj, ChatRoomObj> _view2ModelAdapter) {
		this._view2ModelAdapter = _view2ModelAdapter;
		initGUI();
	}

	private void initGUI() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 700);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(panel_top, BorderLayout.NORTH);
		pnlLogIn.setBorder(new TitledBorder(null, "Log In", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlLogIn.setToolTipText("log in panel");
		
		panel_top.add(pnlLogIn);
		pnlLogIn.setLayout(new GridLayout(0, 1));
		pnlLogIn.add(lblUsername);
		pnlLogIn.add(tfUserName);
		tfUserName.setToolTipText("input user name");
		tfUserName.setColumns(10);
		pnlLogIn.add(btnLogIn);
		btnLogIn.setToolTipText("log in to start the user instance");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_view2ModelAdapter.startServer(tfUserName.getText());
			}
		});
		
		panel_top.add(pnlConnectToIP);
		pnlConnectToIP.setPreferredSize(new Dimension(150, 140));// hardCoded sizing
		pnlConnectToIP.setMaximumSize(new Dimension(150, 200));  // hardCoded sizing
		pnlConnectToIP.setMinimumSize(new Dimension(150, 160));  // hardCoded sizing
		pnlConnectToIP.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Connect to IP", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlConnectToIP.setLayout(new GridLayout(0, 1, 0, 0));
		pnlConnectToIP.add(tfIP);
		tfIP.setColumns(12);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_view2ModelAdapter.connectToIP(tfIP.getText());
			}
		});
		pnlConnectToIP.add(btnConnect);
		
		pnlConnectToIP.add(lblConnectedUsers);
		pnlConnectToIP.add(cbUsers);
		cbUsers.setToolTipText("connected users will listed here");
		cbUsers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbUsers.getSelectedIndex() == -1) {
					return;
				}
				_view2ModelAdapter.requestChatRoomList(cbUsers.getItemAt(cbUsers.getSelectedIndex()));
				lblUser.setText("Users: " + cbUsers.getItemAt(cbUsers.getSelectedIndex()));
			}
		});
		pnlJoinChatRoom.setToolTipText("join chat room panel");
		pnlJoinChatRoom.setPreferredSize(new Dimension(150, 140));// hardCoded sizing
		pnlJoinChatRoom.setMaximumSize(new Dimension(150, 200));  // hardCoded sizing
		pnlJoinChatRoom.setMinimumSize(new Dimension(150, 160));  // hardCoded sizing
		pnlJoinChatRoom.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Connected User", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		panel_top.add(pnlJoinChatRoom);
		pnlJoinChatRoom.setLayout(new GridLayout(0, 1));
		
		pnlJoinChatRoom.add(lblUser);
		
		pnlJoinChatRoom.add(lblChatRoomList);
		cbChatRooms.setToolTipText("the chat rooms the selected user has joined will listed here after request");
		
		pnlJoinChatRoom.add(cbChatRooms);
		btnJoin.setToolTipText("press to join a chat room");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_view2ModelAdapter.joinChatRoom(cbChatRooms.getItemAt(cbChatRooms.getSelectedIndex()));
			}
		});
		
		pnlJoinChatRoom.add(btnJoin);
		panel_top.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		btnExit.setToolTipText("exit the whole program");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_view2ModelAdapter.exit();
			}
		});
		panel_4.add(btnExit);
		contentPane.add(panel_tab, BorderLayout.CENTER);
		panel_tab.addTab("Info", null, pnlInfo, null);
		pnlInfo.setLayout(new BorderLayout(0, 0));
		pnlInfo.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(taInfo);
	}
	
	/**
	 * append info in the info component.
	 * @param text the text to append in the info component.
	 */
	public void appendInfo(String text) {
		taInfo.append(text);
	}

	/**
	 * list the chat rooms the selected remote host created or joined.
	 * @param chatRoomLIst a list of chat rooms.
	 */
	public void listChatRooms(Iterable<ChatRoomObj> chatRoomLIst) {
		cbChatRooms.removeAllItems();
		for (ChatRoomObj chatRoom : chatRoomLIst) {
			cbChatRooms.addItem(chatRoom);
		}
		if (cbChatRooms.getItemCount() > 0) {
			cbChatRooms.setSelectedIndex(0);
		}
	}
	
	/**
	 * Add a chat room view in the JTabbedPane. 
	 * @param chatRoomView the chat room view to add.
	 */
	public void addChatRoomView(ChatRoomView<UserObj, ChatRoomObj> chatRoomView) {
		panel_tab.addTab(chatRoomView.getName(), null, chatRoomView, null);
		chatRoomViewMap.put(chatRoomView.getChatRoom(), chatRoomView);
		panel_tab.setSelectedComponent(chatRoomView);
	}
	
	/**
	 * Remove a chat room view in the JTabbedPane.
	 * @param chatRoom the chat room to remove.
	 */
	public void removeChatRoomView(ChatRoomObj chatRoom) {
		panel_tab.remove(chatRoomViewMap.remove(chatRoom));
	}
	
	/**
	 * start the GUI.
	 */
	public void start() {
		setVisible(true);
	}

	/**
	 * High light an already join chat room view.
	 * @param chatRoom is the chat room the user already joined.
	 */
	public void seletChatRoom(ChatRoomObj chatRoom) {
		panel_tab.setSelectedComponent(chatRoomViewMap.get(chatRoom));
	}

	/**
	 * Display IP adress in connect to IP panel.
	 * @param localAddress The IP adress.
	 */
	public void displayIP(String localAddress) {
		tfIP.setText(localAddress);
	}
	
	/**
	 * Get the connect user stub from main view.
	 * @return the user stub.
	 */
	public UserObj getConnectedUser() {
		return cbUsers.getItemAt(cbUsers.getSelectedIndex());
	}

	/**
	 * Add a new connected user to user list.
	 * @param userStub - the new connected user.
	 */
	public void addUserToList(UserObj userStub) {
		cbUsers.addItem(userStub);
		cbUsers.setSelectedItem(userStub);
	}
	
	/**
	 * buildScrollableComponent
	 * @param fac - the component factory.
	 * @param label - the component label.
	 */
	public void buildScrollableComponent(IComponentFactory fac, String label) {
		Component component = fac.makeComponent();
		panel_tab.addTab(label, component);
		panel_tab.setSelectedComponent(component);
	}

	/**
	 * buildNonScrollableComponent
	 * @param fac - the component factory.
	 * @param label - the component label.
	 */
	public void buildNonScrollableComponent(IComponentFactory fac, String label) {
		Component component = fac.makeComponent();
		panel_tab.addTab(label, component);
		panel_tab.setSelectedComponent(component);
	}
}
