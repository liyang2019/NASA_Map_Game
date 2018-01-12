package ly15_qz18.game.view;

import java.awt.BorderLayout;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ly15_qz18.game.map.MapLayer;
import ly15_qz18.game.map.MapPanel;
import map.IRightClickAction;
import ly15_qz18.game.map.IMap2ModelApapter;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JSplitPane;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

/**
 * Defense AI Game View.
 * @param <TeamObj> - team object generic type.
 * @param <UserObj> - user object generic type.
 *
 */
public class GameView<TeamObj, UserObj> extends JFrame {

	/**
	 * Compiler generated unique ID
	 */
	private static final long serialVersionUID = -1151460082960794096L;
	private MapPanel mapPanel = new MapPanel(new IMap2ModelApapter() {
		@Override
		public void sendLeftClick(UUID cityUUID) {
			_gameView2GameModelAdapter.sendClick(cityUUID, 1);
		}
		@Override
		public void sendRightClick(UUID cityUUID) {
			_gameView2GameModelAdapter.sendClick(cityUUID, -1);
		}
		
	});
	
	private IGameView2GameModelAdapter<TeamObj, UserObj> _gameView2GameModelAdapter;
	
	private JPanel contentPane;
	private final JPanel pnlControll = new JPanel();
	private final JPanel pnlJoinTeam = new JPanel();
	private final JComboBox<TeamObj> cbTeams = new JComboBox<TeamObj>();
	private final JButton btnJoinTeam = new JButton("Join and Ready");
	private final JPanel pnlReady = new JPanel();
	private final JButton btnReady = new JButton("Ready for Round");
	private final JScrollPane spChatRoom = new JScrollPane();
	private final JScrollPane spMessage = new JScrollPane();
	private final JTextArea taMessage = new JTextArea();
	private final JPanel pnlSendMessage = new JPanel();
	private final JButton btnSendToGame = new JButton("Send to Game");
	private final JButton btnSendToTeam = new JButton("Send to Team");
	private final JSplitPane splitPaneMessage = new JSplitPane();
	private final JTextPane tpChatRoom = new JTextPane();
	private final JSplitPane splitPaneChatRoom = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private final JScrollPane spUsers = new JScrollPane();
	private final JList<UserObj> userList = new JList<UserObj>();
	private final JPanel pnlTroops = new JPanel();
	private final JLabel lblTroopsInfo = new JLabel("troops to be distributed:");
	private final JLabel lblTroopsToDistribute = new JLabel("");
	private final JPanel pnlGameProcess = new JPanel();
	private final JLabel lblTimeRemainingInfo = new JLabel("Time Remaining:");
	private final JLabel lblTimeRemaining = new JLabel("");
	private final JLabel lblRoundInfo = new JLabel("Round:");
	private final JLabel lblRound = new JLabel("");
	private final JLabel lblReady = new JLabel("-/-");
	private final JProgressBar pbGameReady = new JProgressBar();
	private final JPanel panel = new JPanel();;
	
	/**
	 * Create the frame.
	 * @param gameView2GameModelAdapter - game view to game model adapter.
	 * @param rightClick - right click action.
	 */
	public GameView(IGameView2GameModelAdapter<TeamObj, UserObj> gameView2GameModelAdapter, IRightClickAction rightClick) {
		this._gameView2GameModelAdapter = gameView2GameModelAdapter;
		initGUI(rightClick);
	}
	
	/**
	 * initialize the game GUI
	 */
	private void initGUI(IRightClickAction rightClick){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setToolTipText("select a team to join");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		panel.add(mapPanel);
		mapPanel.setPreferredSize(new java.awt.Dimension(600, 400));
		pnlControll.setToolTipText("the game controll panel");
		
		contentPane.add(pnlControll, BorderLayout.NORTH);
		pnlJoinTeam.setToolTipText("the join team panel");
		
		pnlControll.add(pnlJoinTeam);
		pnlJoinTeam.setLayout(new GridLayout(0, 1));
		
		pnlJoinTeam.add(cbTeams);
		btnJoinTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TeamObj selectedTeam = cbTeams.getItemAt(cbTeams.getSelectedIndex());
				for (TeamObj teamObj : _gameView2GameModelAdapter.getTeams()) {
					if (selectedTeam.equals(teamObj)) {
						selectedTeam = teamObj;
					}
				}
				if (_gameView2GameModelAdapter.joinTeam(selectedTeam)) {
					_gameView2GameModelAdapter.readyForGame();
				}
			}
		});
		btnJoinTeam.setToolTipText("Join the selected team and ready for game");
		
		pnlJoinTeam.add(btnJoinTeam);
		pbGameReady.setToolTipText("progress bar for users ready to start game");
		pnlJoinTeam.add(pbGameReady);
		
		pnlTroops.setPreferredSize(new Dimension(200, 60));
		pnlControll.add(pnlTroops);
		pnlTroops.setLayout(new GridLayout(0, 1));
		lblTroopsInfo.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnlTroops.add(lblTroopsInfo);
		lblTroopsToDistribute.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnlTroops.add(lblTroopsToDistribute);
		
		pnlControll.add(pnlGameProcess);
		pnlGameProcess.setPreferredSize(new Dimension(250, 60));
		pnlGameProcess.setLayout(new GridLayout(2, 2));
		lblRoundInfo.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnlGameProcess.add(lblRoundInfo);
		lblRound.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnlGameProcess.add(lblRound);
		lblTimeRemainingInfo.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnlGameProcess.add(lblTimeRemainingInfo);
		lblTimeRemaining.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnlGameProcess.add(lblTimeRemaining);
		
		pnlControll.add(pnlReady);
		pnlReady.setLayout(new GridLayout(0, 1));
		btnReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_gameView2GameModelAdapter.readyForRound();
			}
		});
		btnReady.setToolTipText("press to ready for next round");
		
		pnlReady.add(btnReady);
		lblReady.setHorizontalAlignment(SwingConstants.CENTER);
		
		pnlReady.add(lblReady);
		contentPane.add(splitPaneChatRoom, BorderLayout.WEST);
		
		splitPaneChatRoom.setResizeWeight(1);
		splitPaneChatRoom.setLeftComponent(spUsers);
		splitPaneChatRoom.setMaximumSize(new Dimension(150, 300));
		splitPaneChatRoom.setMinimumSize(new Dimension(150, 300));
		splitPaneChatRoom.setPreferredSize(new Dimension(150, 300));
		userList.setToolTipText("user list");
		
		spUsers.setViewportView(userList);
		splitPaneChatRoom.setRightComponent(spChatRoom);
		
		spChatRoom.setToolTipText("user list");
		
		tpChatRoom.setToolTipText("chat room text pane");
		
		spChatRoom.setViewportView(tpChatRoom);
		spChatRoom.setMaximumSize(new Dimension(200, 300));
		spChatRoom.setMinimumSize(new Dimension(200, 300));
		spChatRoom.setPreferredSize(new Dimension(200, 300));
		
		splitPaneMessage.setResizeWeight(1);
		contentPane.add(splitPaneMessage, BorderLayout.SOUTH);
		splitPaneMessage.setLeftComponent(spMessage);
		spMessage.setToolTipText("input message scroll pane");
		spMessage.setViewportView(taMessage);
		taMessage.setToolTipText("input message here");
		splitPaneMessage.setRightComponent(pnlSendMessage);
		pnlSendMessage.setToolTipText("send message panel");
		pnlSendMessage.setLayout(new GridLayout(0, 1, 0, 0));
		btnSendToGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_gameView2GameModelAdapter.sendGameMessage(taMessage.getText());
			}
		});
		btnSendToGame.setToolTipText("send message to lobby");
		
		pnlSendMessage.add(btnSendToGame);
		btnSendToTeam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_gameView2GameModelAdapter.sendTeamMessage(taMessage.getText());
			}
		});
		btnSendToTeam.setToolTipText("send message to team");
		
		pnlSendMessage.add(btnSendToTeam);
	}
	
	/**
	 * List teams created by the game creator.
	 * @param teams - the teams to list.
	 */
	public void listTeams(Iterable<TeamObj> teams) {
		cbTeams.removeAllItems();
		for (TeamObj team : teams) {
			cbTeams.addItem(team);
		}
		if (cbTeams.getItemCount() > 0) {
			cbTeams.setSelectedIndex(0);
		}
	}
	
	/**
	 * start game view
	 */
	public void start() {
		listTeams(_gameView2GameModelAdapter.getTeams());
		mapPanel.start();
		setVisible(true);
	}	
	
	/**
	 * refresh map GUI
	 */
	public void refreshGUI(){
		this.mapPanel.getWWD().redraw();
	}
	
	/**
	 * add map layer to the map GUI
	 * @param layer the map layer to be added
	 */
	public void addMapLayer(MapLayer layer)
	{
		mapPanel.addLayer(layer);
	}
	
	/**
	 * Append text to chat room text area.
	 * @param text is the text to append to chat room text area.
	 * @param name The name of the person sending the text.
	 */
	public void appendMessage(String text, String name) {
		StyledDocument doc = tpChatRoom.getStyledDocument();
		//  Define a keyword attribute
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		StyleConstants.setBackground(keyWord, Color.YELLOW);
		StyleConstants.setBold(keyWord, true);
		//  Add some text
		try {
			doc.insertString(doc.getLength(), name + ": " + text + "\n", keyWord);
		} catch(Exception e) {
			System.out.println("failed to append message to chat room");
			e.printStackTrace();
		}
	}

	/**
	 * list users in the joined team.
	 * @param users the users in the joined team.
	 */
	public void listUsers(UserObj[] users) {
		userList.setListData(users);
	}
	
	/**
	 * Display the number of Troops can be distributed
	 * @param troops number of troops
	 */
	public void displayTroops(int troops) {
		lblTroopsToDistribute.setText(new Integer(troops).toString());
	}
	
	/**
	 * Display the round of the game
	 * @param round - round of the game
	 * @param maxRound - max number of round. 
	 */
	public void displayRound(int round, int maxRound) {
		lblRound.setText(round + "/" + maxRound);
	}
	
	/**
	 * Display round remain time 
	 * @param time the remain time
	 */
	public void displayTimer(int time) {
		lblTimeRemaining.setText(new Integer(time).toString());
	}
	
	
	/**
	 * Debug use main.
	 * @param args - input arguments.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { // Java specs say that the system must be constructed on the GUI event thread.
			public void run() {
				try {
					GameView<Object, Object> gameView = new GameView<>(null, null); // instantiate the system
					gameView.start(); // start the system
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Update users ready for next round.
	 * @param readyNumber - number of user ready for next round.
	 * @param userNumber - number of total user in the game.
	 */
	public void updataRoundReadyUsers(int readyNumber, int userNumber) {
		lblReady.setText(readyNumber + "/" + userNumber);
	}

	/**
	 * Update users ready for game.
	 * @param gameReadyNumber - number of user ready for game.
	 * @param userNumber - number of total user in the game.
	 */
	public void updataGameReadyUsers(int gameReadyNumber, int userNumber) {
		pbGameReady.setMaximum(userNumber);
		pbGameReady.setValue(gameReadyNumber);
	}
}