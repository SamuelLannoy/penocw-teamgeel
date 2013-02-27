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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import peno.htttp.Client;

import messenger.HandlerImplementation;
import messenger.Messenger;
import messenger.RabbitMQ;

import robot.DebugBuffer;
import robot.Robot;
import robot.RobotPool;
import robot.SensorBuffer;

import communication.Bluetooth;

import exception.CommunicationException;
import field.Barcode;
import gui.tools.BarCodeCanvas;
import gui.tools.DrawCanvas;
import gui.tools.PlotCanvas;

@SuppressWarnings("serial")
public class Main extends JFrame {
	
	private final static String BROADCAST_ID = "teamGeel";
	private final static String LOBBY_ID = "teamGeelLobby";
	
	private JPanel contentPane;
	private JTextArea debugwindow;
	
	private Robot robot;
	private RobotPool robotPool;
	private Timer simulatorTimer;
	private Timer robotGuiTimer;
	private Timer robotReceiveTimer;
	private Timer mapTimer;
	private Timer debugTimer;
	private Timer sensorTimer;
	private HandlerImplementation handler;
	private Client client;
	
	private Thread simulatorthread = new Thread(new Runnable() {
		public void run() {
			simulatorTimer = new Timer(1, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robotPool != null) {
						robotPool.updatePosition();
						movement_window.append(robot.getPosition() + "\n");
						last_movement_window.setText(robot.getPosition() + "\n");
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
						movement_window.append(robot.getPosition() + "\n");
						last_movement_window.setText(robot.getPosition() + "\n");
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
			mapTimer = new Timer(100, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robot != null) {
						try {
							//robot.updatePosition();
							canvas.update(canvas.getGraphics());
						} catch (NullPointerException e) {
							// nothing
						}
			    	}
			    }    
			});
			mapTimer.start();
		}
	});
			
	private Thread debugthread = new Thread(new Runnable() {
		public void run() {
			debugTimer = new Timer(100, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robot != null) {
						try {
							// update debug info.
				    		synchronized(DebugBuffer.getDebuginfo()) {
								for(String debuginfo : DebugBuffer.getDebuginfo()) {
									debugwindow.append(""+debuginfo+"\n");
								}
								DebugBuffer.getDebuginfo().clear();
				    		}
						}
						catch (NullPointerException e) {
							
						}
			    	}
			    }    
			});
			debugTimer.start();
		}
	});
	
	private Thread messagethread = new Thread(new Runnable() {
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
	});
	
	private Thread sensorthread = new Thread(new Runnable() {
		public void run() {
			sensorTimer = new Timer(100, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robot != null) {
						// update light sensor data.
			    		synchronized(SensorBuffer.getLightValues()) {
							for(int val: SensorBuffer.getLightValues()) {
								textArea_light.setText(""+val+"\n");
								plotList.add(val);
							}
							SensorBuffer.getLightValues().clear();
			    		}
						// random values test.
						/*for (int j = 0; j < 1; j++){
							double randNumber = Math.random();
							double d = randNumber * 100;
							int randomInt = (int)d;
							plotList.add(randomInt);
						}*/
						for (int i = plotList.size(); i > 100; i--){
							plotList.remove(plotList.size() - i);
						}
						canvas_Light.setData(plotList);
						canvas_Light.update(canvas_Light.getGraphics());
	
						// update ultrasonic sensor data.
						synchronized(SensorBuffer.getDistances()) {
							if (SensorBuffer.lastDist.size() >= 4){
								textArea_ultrasonic_front.setText(""+SensorBuffer.lastDist.get(0)+"\n");
								textArea_ultrasonic_left.setText(""+SensorBuffer.lastDist.get(1)+"\n");
								textArea_ultrasonic_back.setText(""+SensorBuffer.lastDist.get(2)+"\n");
								textArea_ultrasonic_right.setText(""+SensorBuffer.lastDist.get(3)+"\n");
								SensorBuffer.lastDist.clear();
							}
						}
	
						// update pressure sensor data.
						textArea_pressure.setText(""+SensorBuffer.getTouched()+"\n");
						if (!SensorBuffer.getBarcodes().isEmpty()){
							Barcode barcode = new Barcode(SensorBuffer.getBarcodes().get(0));
							robot.getCurrTile().setBarcode(barcode);
							SensorBuffer.getBarcodes().clear();
						}
						
						// update scanned data
						synchronized(SensorBuffer.getDistancesAD()){
							if (SensorBuffer.getDistancesAD().size() >= 10){
								for (int i = 0; i < 10; i++){
									//textArea_multiscan.append("scanned on position:" + "\n" + robot.getPosition() + "\n");
									textArea_multiscan.append((36 * i) + " graden, " + SensorBuffer.getDistancesAD().get(i) + " mm" +"\n");
								}
								SensorBuffer.getDistancesAD().clear();
							}
						}
						
			    	}
			    }    
			});
			sensorTimer.start();
		}
	});

	private JToggleButton toggle_robot;
	private JToggleButton toggle_simulator;
	private JButton button_terminate;
	private JTextArea movement_window;
	private JTextArea last_movement_window;
	private JButton btnExplore;
	private List<Integer> plotList = Collections.synchronizedList(new ArrayList<Integer>());
	private PlotCanvas canvas_Light;
	private JTextArea textArea_ultrasonic_left;
	private JTextArea textArea_ultrasonic_right;
	private JTextArea textArea_light;
	private JTextArea textArea_pressure;
	private JTextArea textArea_ultrasonic_back;
	private JTextArea textArea_ultrasonic_front;
	private DrawCanvas canvas;
	private JMenuItem mntmAdvanced;
	private BarCodeCanvas barcode_canvas;
	private JButton btn_resume;
	private JButton btn_pause;
	private JTextArea textArea_multiscan;
	private JLabel lblPosition;
	private JTextArea textArea_messages;
	private JButton btnSubmitBarcodes;
	private JTextArea teamTextArea;
	private JTextArea teamMateTextArea;
	private JTextArea otherTeamTextArea1;
	private JTextArea otherTeamTextArea2;

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
		debugthread.start();
		messagethread.start();
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
		
		mntmAdvanced = new JMenuItem("Advanced");

		File.add(mntmAdvanced);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane_debugwindow = new JScrollPane();
		scrollPane_debugwindow.setBounds(10, 275, 460, 100);
		
		last_movement_window = new JTextArea();
		last_movement_window.setBounds(10, 480, 250, 23);

		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 400, 250, 78);
		contentPane.add(scrollPane);
		
		movement_window = new JTextArea();
		scrollPane.setViewportView(movement_window);
		contentPane.add(scrollPane_debugwindow);
		
		debugwindow = new JTextArea();
		scrollPane_debugwindow.setViewportView(debugwindow);
		debugwindow.append("Maak een keuze uit robot of simulator.\n");
		contentPane.add(last_movement_window);
		
		toggle_robot = new JToggleButton("robot");
		toggle_robot.setBounds(10, 7, 67, 23);
		contentPane.add(toggle_robot);
		
		toggle_simulator = new JToggleButton("simulator");
		toggle_simulator.setBounds(87, 7, 90, 23);
		contentPane.add(toggle_simulator);
		
		button_terminate = new JButton("terminate");
		button_terminate.setBounds(10, 41, 167, 23);
		contentPane.add(button_terminate);
				
		btnExplore = new JButton("Explore");
		btnExplore.setBounds(10, 75, 167, 43);
		contentPane.add(btnExplore);
		
		canvas_Light = new PlotCanvas(plotList);
		canvas_Light.setBounds(474, 513, 500, 128);
		canvas_Light.setBackground(Color.black);
		contentPane.add(canvas_Light);
		
		textArea_pressure = new JTextArea();
		textArea_pressure.setBounds(10, 536, 128, 30);
		contentPane.add(textArea_pressure);
		
		textArea_ultrasonic_left = new JTextArea();
		textArea_ultrasonic_left.setText("");
		textArea_ultrasonic_left.setBounds(148, 600, 128, 30);
		contentPane.add(textArea_ultrasonic_left);
		
		Label label_left = new Label("left");
		label_left.setBounds(148, 572, 62, 22);
		contentPane.add(label_left);
		
		textArea_ultrasonic_right = new JTextArea();
		textArea_ultrasonic_right.setText("");
		textArea_ultrasonic_right.setBounds(286, 600, 128, 30);
		contentPane.add(textArea_ultrasonic_right);
		
		textArea_ultrasonic_front = new JTextArea();
		textArea_ultrasonic_front.setText("");
		textArea_ultrasonic_front.setBounds(148, 536, 128, 30);
		contentPane.add(textArea_ultrasonic_front);
		
		textArea_ultrasonic_back = new JTextArea();
		textArea_ultrasonic_back.setText("");
		textArea_ultrasonic_back.setBounds(283, 536, 128, 30);
		contentPane.add(textArea_ultrasonic_back);
		
		Label label_right = new Label("right");
		label_right.setBounds(286, 572, 62, 22);
		contentPane.add(label_right);
		
		Label label_front = new Label("front");
		label_front.setBounds(148, 508, 62, 22);
		contentPane.add(label_front);
		
		Label label_back = new Label("back");
		label_back.setBounds(286, 508, 62, 22);
		contentPane.add(label_back);
		
		Label label_pressure = new Label("pressure");
		label_pressure.setBounds(10, 508, 62, 22);
		contentPane.add(label_pressure);
		
		Label label_light = new Label("light");
		label_light.setBounds(10, 572, 62, 22);
		contentPane.add(label_light);
		
		textArea_light = new JTextArea();
		textArea_light.setText("");
		textArea_light.setBounds(10, 600, 128, 30);
		contentPane.add(textArea_light);
		
		canvas = new DrawCanvas(robotPool);
		canvas.setBounds(474, 7, 500, 500);
		canvas.setBackground(new Color(160, 82, 45));
		contentPane.add(canvas);
		
		btn_pause = new JButton("Pause");

		btn_pause.setBounds(187, 61, 89, 23);
		contentPane.add(btn_pause);
		
		btn_resume = new JButton("Resume");

		btn_resume.setBounds(187, 95, 89, 23);
		contentPane.add(btn_resume);
		
		JScrollPane scrollPane_multiscan = new JScrollPane();
		scrollPane_multiscan.setBounds(266, 400, 202, 100);
		contentPane.add(scrollPane_multiscan);
		
		textArea_multiscan = new JTextArea();
		scrollPane_multiscan.setViewportView(textArea_multiscan);
		
		JLabel lblMultiscanValues = new JLabel("multiscan values");
		lblMultiscanValues.setBounds(266, 380, 104, 14);
		contentPane.add(lblMultiscanValues);
		
		lblPosition = new JLabel("position");
		lblPosition.setBounds(10, 380, 54, 14);
		contentPane.add(lblPosition);
		
		JLabel lblObjectNr = new JLabel("Object Number");
		lblObjectNr.setBounds(286, 7, 90, 14);
		contentPane.add(lblObjectNr);
		
		JLabel lblTeammate = new JLabel("Teammate barcode");
		lblTeammate.setBounds(286, 45, 140, 14);
		contentPane.add(lblTeammate);
		
		teamTextArea = new JTextArea();
		teamTextArea.setBounds(286, 20, 90, 24);
		contentPane.add(teamTextArea);
		
		teamMateTextArea = new JTextArea();
		teamMateTextArea.setBounds(286, 60, 90, 24);
		contentPane.add(teamMateTextArea);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 150, 460, 100);
		contentPane.add(scrollPane_1);
		
		textArea_messages = new JTextArea();
		scrollPane_1.setViewportView(textArea_messages);
		
		JLabel lblMessages = new JLabel("messages");
		lblMessages.setBounds(10, 129, 67, 14);
		contentPane.add(lblMessages);
		
		JLabel lblDebugwindow = new JLabel("debugwindow");
		lblDebugwindow.setBounds(10, 255, 67, 14);
		contentPane.add(lblDebugwindow);
		
		btnSubmitBarcodes = new JButton("Submit barcodes");
		btnSubmitBarcodes.setBounds(300, 125, 150, 23);
		contentPane.add(btnSubmitBarcodes);
		
		JLabel lblOtherTeamBarcode = new JLabel("Other team barcodes");
		lblOtherTeamBarcode.setBounds(286, 85, 140, 14);
		contentPane.add(lblOtherTeamBarcode);
		
		otherTeamTextArea1 = new JTextArea();
		otherTeamTextArea1.setBounds(286, 100, 90, 24);
		contentPane.add(otherTeamTextArea1);
		
		otherTeamTextArea2 = new JTextArea();
		otherTeamTextArea2.setBounds(380, 100, 90, 24);
		contentPane.add(otherTeamTextArea2);
		
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
					init();
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
		
		mntmAdvanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (robot != null){
					Advanced advanced = new Advanced(robotPool, Main.this);
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
		
		btnSubmitBarcodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int nr = Integer.parseInt(teamTextArea.getText());
				if (nr < 4){
					if(robot!=null){
						robot.setObjectNr(nr);
						System.out.println("OK, nr = "+nr);
					}
				}
//				if (teamMateTextArea.getText().length() == 6){
//				for (int i = 0; i < teamMateTextArea.getText().length(); i++) {
//					code[i] = Integer.parseInt(teamMateTextArea.getText().substring(i, i+1));
//				}
//				Barcode teamBarcode = new Barcode(code);
//				robot.setTeamMateBarcode(teamBarcode);
//				}
//				if (otherTeamTextArea1.getText().length() == 6){
//				for (int i = 0; i < otherTeamTextArea1.getText().length(); i++) {
//					code[i] = Integer.parseInt(otherTeamTextArea1.getText().substring(i, i+1));
//				}
//				Barcode otherTeamBarcode = new Barcode(code);
//				robot.addOtherTeamBarcode(otherTeamBarcode);
//				}
//				if (otherTeamTextArea2.getText().length() == 6){
//					for (int i = 0; i < otherTeamTextArea2.getText().length(); i++) {
//						code[i] = Integer.parseInt(otherTeamTextArea2.getText().substring(i, i+1));
//					}
//					Barcode otherTeamBarcode = new Barcode(code);
//					robot.addOtherTeamBarcode(otherTeamBarcode);
//				}
			}
		});
		
	}

	
	public void init() {
		robotPool = new RobotPool(robot);
		robot.initialize();
		canvas.setRobotPool(robotPool);
		handler = new HandlerImplementation(robotPool, BROADCAST_ID);
		try {
			client = new Client(RabbitMQ.createConnection(), handler, LOBBY_ID, BROADCAST_ID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void resetCanvas() {
		canvas.setRobotPool(robotPool);
	}
}
