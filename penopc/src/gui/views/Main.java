package gui.views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;
import java.awt.Canvas;
import javax.swing.border.BevelBorder;
import java.awt.Color;

import java.awt.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import com.rabbitmq.client.Connection;

import peno.htttp.*;
import peno.htttp.impl.PlayerHandlerImplementation;
import peno.htttp.impl.SpectatorHandlerImplementation;

import messenger.RabbitMQ;

import robot.DebugBuffer;
import robot.Robot;
import robot.RobotModel;
import robot.RobotPool;
import robot.SensorBuffer;

import communication.Bluetooth;

import exception.CommunicationException;
import field.Barcode;
import field.BarcodeType;
import field.simulation.FieldSimulation;
import gui.tools.BarCodeCanvas;
import gui.tools.DrawCanvas;
import gui.tools.PlotCanvas;
import gui.tools.MyTableModel;
import gui.tools.WorldCanvas;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class Main extends JFrame {
	
	private Robot robot;
	private RobotPool robotPool;
	private Timer simulatorTimer;
	private Timer robotGuiTimer;
	private Timer robotReceiveTimer;
	private Timer mapTimer;
	private Timer debugTimer;
	private Timer sensorTimer;
	private Timer lobbyTimer;
	private JFrame frame2;
	private JPanel contentPane2;
	private FieldSimulation world;
	private List<Integer> plotList = new ArrayList<Integer>();
	private DrawCanvas canvas;
	private WorldCanvas canvas2;
	private boolean sensorDispActive;
	private Object[][] lobbyData = new Object[5][6];
	private MyTableModel lobbyTable;
	private JPanel lobbyPanel;
	
	private Thread simulatorthread = new Thread(new Runnable() {
		public void run() {
			simulatorTimer = new Timer(1, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robotPool != null) {
						robotPool.updatePosition();
			    	}
			    }    
			});
			simulatorTimer.start();
		}
	});
		
	private Thread robotguithread = new Thread(new Runnable() {
		public void run() {
			robotGuiTimer = new Timer(100, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robotPool != null) {
						robotPool.updatePosition();
			    	}
			    }    
			});
			robotGuiTimer.start();
		}
	});
	
	private Thread robotreceivethread = new Thread(new Runnable() {
		public void run() {
			robotReceiveTimer = new Timer(100, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (canShowData()) {
			    		try {
			    			Bluetooth.getInstance().receive();
			    		} catch (Exception e){
			    			e.printStackTrace();
			    			btnTerminate.doClick();
			    		}
			    	}
			    }    
			});
			robotReceiveTimer.start();
		}
	});
	
	int counter = 0;
	int updatecount = 2;
	
	private Thread mapthread = new Thread(new Runnable() {
		public void run() {
			mapTimer = new Timer(200, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			    	if (canShowData()) {
						try {
							canvas.update(canvas.getGraphics());
							if (counter == 0) {
								canvas2.update(canvas2.getGraphics());
							}
							counter = (counter + 1)%updatecount;
							/*if (robot.receivedTeamTiles()){
								if(canvas2.getTitle().equals("World view")){
									canvas2.setRobotPool(new RobotPool(robot.getTeamMate(), ""));
									canvas2.setTitle("Teammate's view");
								} else{
									canvas2.update(canvas2.getGraphics());
								}
							}*/
							/**
							 else { //back to world view after map merge
							 	canvas2.setRobotPool(worldPool);
								canvas2.setTitle("World view");
							 }
							 */
						} catch (NullPointerException e) {
						}
			    	}
			    }    
			});
			mapTimer.start();
		}
	});
	
	private Thread sensorthread = new Thread(new Runnable() {
		public void run() {
			sensorTimer = new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			    	if (canShowData()) {
						// update light sensor data.
			    		synchronized(SensorBuffer.getLightValues()) {
							for(int val: SensorBuffer.getLightValues()) {
								plotList.add(val);
							}
							SensorBuffer.getLightValues().clear();
			    		}
						for (int i = plotList.size(); i > 100; i--){
							plotList.remove(plotList.size() - i);
						}
	
						// update ultrasonic sensor data.
						synchronized(SensorBuffer.getDistances()) {
							if (SensorBuffer.lastDist.size() >= 4){
								if (!sensorDispActive) SensorBuffer.lastDist.clear();
							}
						}
						
						if (!SensorBuffer.getBarcodes().isEmpty()){
							Barcode barcode = new Barcode(SensorBuffer.getBarcodes().get(0));
							barcode.setType(BarcodeType.valueOf(SensorBuffer.getBarcodeTypes().get(0)));
							robot.getCurrTile().setBarcode(barcode);
							SensorBuffer.getBarcodes().clear();
							SensorBuffer.getBarcodeTypes().clear();
						}
						
						// update scanned data
						synchronized(SensorBuffer.getDistancesAD()){
							if (SensorBuffer.getDistancesAD().size() >= 10){
								if (!sensorDispActive) SensorBuffer.getDistancesAD().clear();
							}
						}
						
			    	}
			    }    
			});
			sensorTimer.start();
		}
	});
	
	private boolean createdLobbyTable = false;
	
	// TODO update lobby data
	private Thread lobbythread = new Thread(new Runnable() {
		public void run() {
			lobbyTimer = new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (canShowData()) {
						int pl = 0;
						for (String playerId : robot.getLobbyViewer().getPlayerData()) {
			    			lobbyData[pl][0] = playerId;
			    			if (robotPool.isMainRobot(playerId)) {
				    			lobbyData[pl][1] = robotPool.getMainRobot().getTeamMateID() == null?
				    					"" : robotPool.getMainRobot().getTeamMateID();
				    			lobbyData[pl][2] = robotPool.getMainRobot().getPlayerNr();
				    			lobbyData[pl][3] = robotPool.getMainRobot().getObjectNr();
			    			} else {
				    			lobbyData[pl][1] = "";
				    			lobbyData[pl][2] = "";
				    			lobbyData[pl][3] = "";
			    			}
			    			lobbyData[pl][4] = robotPool.getRobot(playerId).isReady();
			    			lobbyData[pl][5] = robotPool.getRobot(playerId).hasBall();
			    			pl++;
			    		}
						if (!createdLobbyTable && pl > 0) {
							String[] columns = {"player","team","objectNR","playerNR","ready","object found"};
							lobbyTable = new MyTableModel(columns,lobbyData);
							tableLobby = new JTable(lobbyTable);
							tableLobby.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
							tableLobby.setBackground(Color.WHITE);
							tableLobby.setForeground(Color.BLACK);
							tableLobby.setBounds(0,0, 396, 153);
							lobbyScroll = new JScrollPane(tableLobby);
							lobbyScroll.setBounds(249, 16, 396, 153);
							lobbyPanel.add(lobbyScroll);
							createdLobbyTable = true;
						}
			    		/*for(int pl = 0; pl < robotPool.getRobotPoolSize(); pl++){
			    			Robot currentRobot = robotPool.getRobots().get(pl);
			    			lobbyData[0][pl] = currentRobot.getID();
			    			lobbyData[1][pl] = currentRobot.getTeamNr();
			    			lobbyData[2][pl] = currentRobot.getObjectNr();
			    			lobbyData[3][pl] = currentRobot.getPlayerNr();
			    			lobbyData[4][pl] = currentRobot.getReady();
			    			lobbyData[5][pl] = currentRobot.getObjectFound();
			    		}*/
			    		for(int o = robot.getLobbyViewer().getPlayerData().size(); o < 4;o++){
			    			for(int p = 0; p < lobbyData.length; p++){
			    				lobbyData[o][p] = null;
			    			}
			    		}
			    		for(int i = 0; i < lobbyData.length; i++){
			    			for(int j = 0; j < lobbyData[0].length; j++){
			    				lobbyTable.setValueAt(lobbyData[i][j],i,j);
			    			}
			    		}
			    		tableLobby.revalidate();
			    	}
			    }    
			});
			lobbyTimer.start();
		}
	});
	
	protected List<Integer> getPlot(){
		return plotList;
	}
	
	protected void deactivateSensorDisp(){
		sensorDispActive = false;
	}
	
	private JPanel contentPane;
	private JMenuItem debugWindow;
	private JMenuItem positionDisp;
	private JMenuItem debugDisp;
	private JMenuItem sensorDisp;
	private JMenuItem advanced;
	private JTextField textFieldLobbyName;
	private JTextField textFieldPlayerName;
	private JTable tableLobby;
	private JButton toggleRobot;
	private JButton toggleSim;
	private JButton btnTerminate;
	private JButton btnStartGame;
	private JButton btnPause;
	private JButton btnResume;
	private JButton btnJoinLobby;
	private JButton btnLeaveLobby;
	private JButton btnReady;
	private JRadioButton rdbtnKuleuven;
	private JRadioButton rdbtnLocal;
	private JScrollPane lobbyScroll;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		initComponents();
		createEvents();
		mapthread.start();
		sensorthread.start();
		lobbythread.start();
	}

	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 10, 1010, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("");
		menuBar.add(menu);
		
		JMenu File = new JMenu("File");
		menuBar.add(File);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//robot.terminate();
				dispose();
			}
		});
		File.add(mntmExit);
		
		JMenu views = new JMenu("Views");
		menuBar.add(views);
		
		advanced = new JMenuItem("Advanced");
		views.add(advanced);
		
		sensorDisp = new JMenuItem("SensorDisplay");
		views.add(sensorDisp);
		
		debugDisp = new JMenuItem("DebugDisplay");
		views.add(debugDisp);
		
		positionDisp = new JMenuItem("PositionDisplay");
	
		views.add(positionDisp);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		controlPanel.setBounds(10, 10, 310, 180);
		contentPane.add(controlPanel);
		controlPanel.setLayout(null);
		
		toggleRobot = new JButton("robot");

		toggleRobot.setBounds(10, 16, 89, 30);
		controlPanel.add(toggleRobot);
		
		toggleSim = new JButton("simulator");
		toggleSim.setBounds(109, 16, 89, 30);
		controlPanel.add(toggleSim);
		
		btnTerminate = new JButton("terminate");
		btnTerminate.setBounds(10, 57, 188, 30);
		controlPanel.add(btnTerminate);
		
		btnStartGame = new JButton("Start Game");
		btnStartGame.setBounds(10, 98, 188, 71);
		controlPanel.add(btnStartGame);
		
		btnResume = new JButton("Resume");
		btnResume.setBounds(208, 139, 89, 30);
		controlPanel.add(btnResume);
		
		btnPause = new JButton("Pause");
		btnPause.setBounds(208, 98, 89, 30);
		controlPanel.add(btnPause);
		
		lobbyPanel = new JPanel();
		lobbyPanel.setBorder(new TitledBorder(null, "Lobby", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lobbyPanel.setBounds(329, 10, 655, 180);
		contentPane.add(lobbyPanel);
		lobbyPanel.setLayout(null);
		
		btnJoinLobby = new JButton("Join lobby");
		btnJoinLobby.setBounds(10, 75, 109, 30);
		lobbyPanel.add(btnJoinLobby);
		
		btnLeaveLobby = new JButton("Leave lobby");
		btnLeaveLobby.setBounds(10, 115, 109, 30);
		lobbyPanel.add(btnLeaveLobby);
		
		rdbtnLocal = new JRadioButton("local");
		rdbtnLocal.setBounds(10, 16, 109, 23);
		lobbyPanel.add(rdbtnLocal);
		
		rdbtnKuleuven = new JRadioButton("kuleuven");
		rdbtnKuleuven.setBounds(10, 40, 109, 23);
		lobbyPanel.add(rdbtnKuleuven);
		
		JLabel lblLobbyName = new JLabel("Lobby name");
		lblLobbyName.setBounds(129, 16, 109, 14);
		lobbyPanel.add(lblLobbyName);
		
		textFieldLobbyName = new JTextField();
		textFieldLobbyName.setBounds(129, 41, 109, 20);
		lobbyPanel.add(textFieldLobbyName);
		textFieldLobbyName.setColumns(10);
		
		JLabel lblPlayerName = new JLabel("Player name");
		lblPlayerName.setBounds(129, 72, 109, 14);
		lobbyPanel.add(lblPlayerName);
		
		textFieldPlayerName = new JTextField();
		textFieldPlayerName.setBounds(129, 95, 109, 20);
		lobbyPanel.add(textFieldPlayerName);
		textFieldPlayerName.setColumns(10);
		
		btnReady = new JButton();
		btnReady.setBounds(129,126,109,30);
		btnReady.setText("Ready");
		lobbyPanel.add(btnReady);
		
		canvas = new DrawCanvas(robotPool,"Player's view");
		canvas.setBackground(new Color(139, 69, 19));
		canvas.setBounds(10, 196, 480, 480);
		contentPane.add(canvas);
		
		canvas2 = new WorldCanvas();
		canvas2.setBackground(new Color(139, 69, 19));
		canvas2.setBounds(500, 196, 480, 480);
		contentPane.add(canvas2);
		
		enableButtons(false);
		
	}
	
	private void enableButtons(Boolean bool){
		btnStartGame.setEnabled(bool);
		btnTerminate.setEnabled(bool);
		btnPause.setEnabled(bool);
		btnResume.setEnabled(bool);
		
		btnJoinLobby.setEnabled(bool);
		btnLeaveLobby.setEnabled(bool);
		rdbtnLocal.setEnabled(bool);
		rdbtnKuleuven.setEnabled(bool);
	}
	
	private void createEvents(){
		
		// simulator gekozen.
		toggleSim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					toggleSim.setSelected(true);
					toggleSim.setEnabled(false);
					toggleRobot.setEnabled(false);
					btnStartGame.setEnabled(true);
					rdbtnLocal.setEnabled(true);
					rdbtnKuleuven.setEnabled(true);
					robot = new Robot(1);
					//init();
					simulatorthread.start();
				} catch (Exception a) {
					a.printStackTrace();
				}
			}
		});
		
		// robot gekozen.
		toggleRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					toggleRobot.setSelected(true);
					toggleRobot.setEnabled(false);
					toggleSim.setEnabled(false);
					btnStartGame.setEnabled(true);
					rdbtnLocal.setEnabled(true);
					rdbtnKuleuven.setEnabled(true);
					robot = new Robot(2);
					/*try {
						init();
					} catch (IOException e1) {
						e1.printStackTrace();
					}*/
					robotguithread.start();
					robotreceivethread.start();
					
				} catch (CommunicationException norobot){
					toggleRobot.setSelected(false);
					toggleRobot.setEnabled(true);
					toggleSim.setEnabled(true);
					btnStartGame.setEnabled(false);
					rdbtnLocal.setEnabled(false);
					rdbtnKuleuven.setEnabled(false);
					norobot.printStackTrace();
				}
				
			}
		});
		
		// keuze robot/simulator ongedaan.
		btnTerminate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleRobot.setSelected(false);
				toggleSim.setSelected(false);
				toggleRobot.setEnabled(true);
				toggleSim.setEnabled(true);
				enableButtons(false);
				if(robot.isSim()) {
					simulatorTimer.stop();
				} else {
					robotGuiTimer.stop();
					robotReceiveTimer.stop();
				}
				robot.terminate();
				robotPool.terminate();
				robot = null;
				robotPool = null;
				canvas.setRobotPool(robotPool);
			}
		});
		
		// Join lobby
		btnJoinLobby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnJoinLobby.setEnabled(false);
				btnLeaveLobby.setEnabled(true);
				rdbtnLocal.setEnabled(false);
				rdbtnKuleuven.setEnabled(false);
				btnStartGame.setEnabled(false);
				if (rdbtnLocal.isSelected()){
					RabbitMQ.setConnectionType("local");
				}
				if (rdbtnKuleuven.isSelected()){
					RabbitMQ.setConnectionType("kul");
				}
				try {
					init();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// Leave lobby
		btnLeaveLobby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnLeaveLobby.setEnabled(false);
				btnStartGame.setEnabled(true);
				rdbtnKuleuven.setEnabled(true);
				rdbtnLocal.setEnabled(true);
				rdbtnKuleuven.setSelected(false);
				rdbtnLocal.setSelected(false);
				// TODO disconnect from lobby
				if (rdbtnLocal.isSelected()){
				}
				if (rdbtnKuleuven.isSelected()){
				}
			}
		});
		
		// Select local lobby
		rdbtnLocal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnKuleuven.setSelected(false);
				btnJoinLobby.setEnabled(true);
			}
		});
		
		// Select remote lobby
		rdbtnKuleuven.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnLocal.setSelected(false);
				btnJoinLobby.setEnabled(true);
			}
		});
		
		// Open advanced view
		advanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canShowData()){
					Advanced advanced = new Advanced(robotPool, Main.this);
				}
			}
		});
		
		// Open sensor view
		sensorDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canShowData()){
					SensorDisplay sensorDisplay = new SensorDisplay(robot, Main.this);
					sensorDispActive = true;
				}
			}
		});
		
		// Open debug view
		debugDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canShowData()){
					DebugDisplay debugDisplay = new DebugDisplay(robot, Main.this);
				}
			}
		});
		
		// Open position view
		positionDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canShowData()){
					PositionDisplay positionDisplay = new PositionDisplay(robot, Main.this);
				}
			}
		});
		
		// Start the game
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnStartGame.setEnabled(false);
				btnPause.setEnabled(true);
				btnJoinLobby.setEnabled(false);
				rdbtnKuleuven.setSelected(false);
				rdbtnLocal.setSelected(false);
				rdbtnLocal.setEnabled(false);
				rdbtnKuleuven.setEnabled(false);
				// TODO possible ready btn when connected to lobby
				robot.explore();
				resetCanvas();
			}
		});
		
		// Pause the game
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPause.setEnabled(false);
				btnResume.setEnabled(true);
				btnTerminate.setEnabled(true);
				robot.pauseExplore();
			}
		});
		
		// Resume the game
		btnResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPause.setEnabled(true);
				btnResume.setEnabled(false);
				btnTerminate.setEnabled(false);
				robot.resumeExplore();

			}
		});

		
		// ready
		btnReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					robot.setReady();
				} catch (Exception a) {
					a.printStackTrace();
				}
			}
		});
	}
	
	public void init() throws IOException {
		String playerId = textFieldPlayerName.getText();
		
		
		robotPool = new RobotPool(robot, playerId);
		world = new FieldSimulation(robotPool, "C:\\demo2.txt");
		canvas.setField(world);
		canvas2.setWorld(world);
		canvas2.setRobotPool(robotPool);
//		world = new FieldSimulation(robotPool, "/Users/elinetje2/Documents/2012-2013/Semester 2/P&O/demo2.txt");
		robot.initialize();
		robot.setSimField(world);
		
		if (robot.isSim()) {
			robot.setSimLoc(0, 0, 0);
		}
		canvas.setRobotPool(robotPool);
		
		world.connectToGame(textFieldLobbyName.getText(), playerId);
		
		robot.connectToGame(playerId, textFieldLobbyName.getText());


	}
	
	public boolean canShowData() {
		return robot != null && robot.isConnectedToGame();
	}
	
	public void resetCanvas() {
		canvas.setRobotPool(robotPool);
	}
}
