package gui.views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.rabbitmq.client.Connection;

import peno.htttp.*;
import peno.htttp.impl.PlayerHandlerImplementation;
import peno.htttp.impl.SpectatorHandlerImplementation;

import messenger.Messenger;
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
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class Main extends JFrame {
	
	private final static String BROADCAST_ID = "teamgeellobby";
	private final static String LOBBY_ID = "Exchange";
	
	private JPanel contentPane;
	
	private Robot robot;
	private RobotPool robotPool;
	private Timer simulatorTimer;
	private Timer robotGuiTimer;
	private Timer robotReceiveTimer;
	private Timer mapTimer;
	private Timer debugTimer;
	private Timer sensorTimer;
	private JFrame frame2;
	private JPanel contentPane2;
	private FieldSimulation world;
	
	private Thread simulatorthread = new Thread(new Runnable() {
		public void run() {
			simulatorTimer = new Timer(1, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robotPool != null) {
						robotPool.updatePosition();
						//movement_window.append(robot.getPosition() + "\n");
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
						//movement_window.append(robot.getPosition() + "\n");
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
			    	if (robot != null) {
			    		try {
			    			Bluetooth.getInstance().receive();
			    		} catch (Exception e){
			    			e.printStackTrace();
			    			debugwindow.append("Verbinding verbroken.");
			    			button_terminate.doClick();
			    		}
			    	}
			    }    
			});
			robotReceiveTimer.start();
		}
	});
	
	private Thread mapthread = new Thread(new Runnable() {
		public void run() {
			mapTimer = new Timer(200, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robot != null) {
						try {
							//robot.updatePosition();
							canvas.update(canvas.getGraphics());
							if (robot.receivedTeamTiles()){
								if(frame2 == null){
									frame2 = new JFrame();
									frame2.setBounds ( 0 , 0 , 500 ,500);
									frame2.setVisible(true);
									contentPane2 = new JPanel();
									contentPane2.setBorder(new EmptyBorder(5, 5, 5, 5));
									frame2.setContentPane(contentPane2);
									canvas2 = new DrawCanvas(new RobotPool(robot.getTeamMate()));
									canvas2.setBounds(0, 0, 500, 500);
									canvas2.setBackground(new Color(160, 82, 45));
									contentPane2.add(canvas2);
								} else{
									canvas2.update(canvas2.getGraphics());
								}
							}
						} catch (NullPointerException e) {
							// nothing
						}
			    	}
			    }    
			});
			mapTimer.start();
		}
	});
			
	/**private Thread debugthread = new Thread(new Runnable() {
		public void run() {
			debugTimer = new Timer(100, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robot != null) {
						robot_current_action.setText(robot.getCurrentAction());
						try {
							// update debug info.
				    		synchronized(DebugBuffer.getDebuginfo()) {
								for(String debuginfo : DebugBuffer.getDebuginfo()) {
									//debugwindow.append(""+debuginfo+"\n");
								}
								DebugBuffer.getDebuginfo().clear();
				    		}
							// update comm info.
				    		synchronized(DebugBuffer.getComminfo()) {
								for(String comminfo : DebugBuffer.getComminfo()) {
									//textArea_messages.append(""+comminfo+"\n");
								}
								DebugBuffer.getComminfo().clear();
				    		}
						}
						catch (NullPointerException e) {
							
						}
			    	}
			    }    
			});
			debugTimer.start();
		}
	});**/
	
	/*private Thread messagethread = new Thread(new Runnable() {
		public void run() {
			try {
				Messenger.connect();
				Messenger.send("Iets random");
				while(true) {
					Messenger.receivePush("test.*");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	});*/
	
	private Thread sensorthread = new Thread(new Runnable() {
		public void run() {
			sensorTimer = new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
			    	if (robot != null) {
						// update light sensor data.
			    		synchronized(SensorBuffer.getLightValues()) {
							for(int val: SensorBuffer.getLightValues()) {
								//textArea_light.setText(""+val+"\n");
								plotList.add(val);
							}
							SensorBuffer.getLightValues().clear();
			    		}
						for (int i = plotList.size(); i > 100; i--){
							plotList.remove(plotList.size() - i);
						}
						//canvas_Light.setData(plotList);
						//canvas_Light.update(canvas_Light.getGraphics());
	
						// update ultrasonic sensor data.
						synchronized(SensorBuffer.getDistances()) {
							if (SensorBuffer.lastDist.size() >= 4){
								//textArea_ultrasonic_front.setText(""+SensorBuffer.lastDist.get(0)+"\n");
								//textArea_ultrasonic_left.setText(""+SensorBuffer.lastDist.get(1)+"\n");
								//textArea_ultrasonic_back.setText(""+SensorBuffer.lastDist.get(2)+"\n");
								//textArea_ultrasonic_right.setText(""+SensorBuffer.lastDist.get(3)+"\n");
								if (!sensorDispActive) SensorBuffer.lastDist.clear();
							}
						}
	
						// update pressure sensor data.
						//textArea_pressure.setText(""+SensorBuffer.getTouched()+"\n");
						
						//update infrared sensor data.
						//textArea_infrared.setText("Ahead: "+SensorBuffer.getInfrared());
						
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
								for (int i = 0; i < 10; i++){
									//textArea_multiscan.append("scanned on position:" + "\n" + robot.getPosition() + "\n");
									//textArea_multiscan.append((36 * i) + " graden, " + SensorBuffer.getDistancesAD().get(i) + " mm" +"\n");
								}
								if (!sensorDispActive) SensorBuffer.getDistancesAD().clear();
							}
						}
						
			    	}
			    }    
			});
			sensorTimer.start();
		}
	});
	
	private JMenuItem sensorDisp;
	private JToggleButton toggle_robot;
	private JToggleButton toggle_simulator;
	private JButton button_terminate;
	private JButton btnExplore;
	private List<Integer> plotList = Collections.synchronizedList(new ArrayList<Integer>());
	private PlotCanvas canvas_Light;
	private DrawCanvas canvas;
	private DrawCanvas canvas2;
	private JMenuItem advanced;
	private BarCodeCanvas barcode_canvas;
	private JButton btn_resume;
	private JButton btn_pause;
	private JTextArea teamMateTextArea;
	private JTextArea otherTeamTextArea1;
	private JMenuItem debugWindow;
	private JMenuItem positionDisp;
	private JMenuItem debugDisp;
	private boolean sensorDispActive;
	private JPanel panel_1;
	
	protected List<Integer> getPlot(){
		return plotList;
	}
	
	protected void deactivateSensorDisp(){
		sensorDispActive = false;
	}
	
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
		//debugthread.start();
		//messagethread.start();
	}
	
	private void initComponents(){
		this.setFocusable(true);
		setTitle("Robot control centre");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("");
		menuBar.add(menu);
		
		JMenu File = new JMenu("File");
		menuBar.add(File);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robot.terminate();
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
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(null);
		
		canvas_Light = new PlotCanvas(plotList);
		canvas_Light.setBounds(474, 513, 500, 128);
		canvas_Light.setBackground(Color.black);
		contentPane.add(canvas_Light);
		
		canvas = new DrawCanvas(robotPool);
		canvas.setBounds(474, 7, 500, 500);
		canvas.setBackground(new Color(160, 82, 45));
		contentPane.add(canvas);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 290, 153);
		contentPane.add(panel);
		panel.setLayout(null);
		
		btnExplore = new JButton("Start Game");
		btnExplore.setBounds(10, 90, 167, 43);
		panel.add(btnExplore);
		
		toggle_robot = new JToggleButton("robot");
		toggle_robot.setBounds(10, 22, 67, 23);
		panel.add(toggle_robot);
		
		toggle_simulator = new JToggleButton("simulator");
		toggle_simulator.setBounds(87, 22, 90, 23);
		panel.add(toggle_simulator);
		
		button_terminate = new JButton("terminate");
		button_terminate.setBounds(10, 56, 167, 23);
		panel.add(button_terminate);
		
		btn_resume = new JButton("Resume");
		btn_resume.setBounds(187, 110, 89, 23);
		panel.add(btn_resume);
		
		btn_pause = new JButton("Pause");
		btn_pause.setBounds(187, 76, 89, 23);
		panel.add(btn_pause);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Lobby", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(41, 329, 290, 153);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnJoinLobby = new JButton("Join lobby");
		btnJoinLobby.setBounds(10, 21, 89, 23);
		panel_1.add(btnJoinLobby);
		
		JButton btnLeaveLobby = new JButton("Leave lobby");
		btnLeaveLobby.setBounds(10, 55, 89, 23);
		panel_1.add(btnLeaveLobby);
		
		JRadioButton rdbtnLocal = new JRadioButton("local");
		rdbtnLocal.setBounds(10, 87, 109, 23);
		panel_1.add(rdbtnLocal);
		
		JRadioButton rdbtnKuleuven = new JRadioButton("kuleuven");
		rdbtnKuleuven.setBounds(10, 113, 109, 23);
		panel_1.add(rdbtnKuleuven);
		
		JLabel lblTeammate = new JLabel("Login name");
		lblTeammate.setBounds(150, 59, 140, 14);
		panel_1.add(lblTeammate);
		
		teamMateTextArea = new JTextArea();
		teamMateTextArea.setBounds(117, 112, 90, 24);
		panel_1.add(teamMateTextArea);
		
		JLabel lblOtherTeamBarcode = new JLabel("Lobby name");
		lblOtherTeamBarcode.setBounds(140, 21, 140, 14);
		panel_1.add(lblOtherTeamBarcode);
		
		otherTeamTextArea1 = new JTextArea();
		otherTeamTextArea1.setBounds(125, 86, 90, 24);
		panel_1.add(otherTeamTextArea1);
		
		// Controls
		JPanel panel_controls = new JPanel();
		panel_controls.setBounds(10, 10, 280, 150);
		panel_controls.setBorder(new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//contentPane.add(panel_controls);
		panel_controls.setLayout(null);
		
		// Lobby
		JPanel panel_lobby = new JPanel();
		panel_lobby.setBounds(10, 160, 280, 75);
		panel_lobby.setBorder(new TitledBorder(null, "RabbitMQ lobby", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//contentPane.add(panel_lobby);
		panel_lobby.setLayout(null);
		
		JTable table_lobby = new JTable(4,4);
		panel_lobby.add(table_lobby);
		
	}
	
	private void createEvents(){
		
		// simulator gekozen.
		toggle_simulator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					toggle_simulator.setSelected(true);
					toggle_simulator.setEnabled(false);
					toggle_robot.setEnabled(false);
					debugwindow.append("U heeft gekozen voor de simulator.\n");
					debugwindow.append("Maak een keuze uit de opdracht en.\n");
					robot = new Robot(1);
					init();
					simulatorthread.start();
				} catch (Exception a) {
					a.printStackTrace();
					debugwindow.append("Kan de simulator niet initialiseren.\n");
				}
			}
		});
		
		// robot gekozen.
		toggle_robot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					toggle_robot.setSelected(true);
					toggle_robot.setEnabled(false);
					toggle_simulator.setEnabled(false);
					debugwindow.append("U heeft gekozen voor de robot.\n");
					debugwindow.append("Maak een keuze uit de opdrachten.\n");
					robot = new Robot(2);
					try {
						init();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					robotguithread.start();
					robotreceivethread.start();
					
				} catch (CommunicationException norobot){
					toggle_robot.setSelected(false);
					toggle_robot.setEnabled(true);
					toggle_simulator.setEnabled(true);
					norobot.printStackTrace();
					debugwindow.append("Er kan geen verbinding gemaakt worden met de robot.\n");
				}
				
			}
		});
		
		// keuze robot/simulator ongedaan.
		button_terminate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggle_robot.setSelected(false);
				toggle_simulator.setSelected(false);
				toggle_robot.setEnabled(true);
				toggle_simulator.setEnabled(true);
				debugwindow.append("Maak een keuze uit robot of simulator.\n");
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
		
		advanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (robot != null){
					Advanced advanced = new Advanced(robotPool, Main.this);
				}
			}
		});
		
		sensorDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (robot != null){
					SensorDisplay sensorDisplay = new SensorDisplay(robot, Main.this);
					sensorDispActive = true;
				}
			}
		});
		
		debugDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (robot != null){
					DebugDisplay debugDisplay = new DebugDisplay(robot, Main.this);
				}
			}
		});
		
		positionDisp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (robot != null){
					PositionDisplay positionDisplay = new PositionDisplay(robot, Main.this);
				}
			}
		});
		
		btnExplore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				robot.explore();
				resetCanvas();
			}
		});
		
		btn_pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robot.pauseExplore();
			}
		});
		
		btn_resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robot.resumeExplore();
			}
		});
		
	}

	
	public void init() throws IOException {
		String playerID = teamMateTextArea.getText();
		
		
		robotPool = new RobotPool(robot);
		world = new FieldSimulation(robotPool, "C:\\demo2.txt");
//		world = new FieldSimulation(robotPool, "/Users/elinetje2/Documents/2012-2013/Semester 2/P&O/demo2.txt");
		robot.initialize();
		if (robot.isSim()) {
			robot.setSimField(world);
		}
		double x = 0;
		double y = 0;
		try {
			x = Integer.parseInt(otherTeamTextArea1.getText());
			y = Integer.parseInt(otherTeamTextArea2.getText());
		} catch( Exception e ) {
			
		}
		
		if (robot.isSim()) {
			robot.setSimLoc(x, y, 0);
		}
		canvas.setRobotPool(robotPool);
		
		world.connectToGame(BROADCAST_ID, playerID);
		
		robot.connectToGame(playerID, BROADCAST_ID);


	}
	
	public void resetCanvas() {
		canvas.setRobotPool(robotPool);
	}
}
