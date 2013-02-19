package gui.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import field.Border;
import field.PanelBorder;
import field.Tile;
import field.WhiteBorder;

import robot.DebugBuffer;
import robot.Robot;

public class Advanced extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JRadioButton enable_button_controls;
	private JButton btn_90_left;
	private JButton button_UP;
	private JButton btn_90_right;
	private JButton button_LEFT;
	private JButton button_DOWN;
	private JTextField textField_move;
	private JButton btn_move;
	private JTextField textField_length;
	private JTextField textField_corners;
	private JButton button_ride_polygon;
	private JButton btn_sensor_scan;
	private JButton btn_white_line;
	private JButton btn_scan_forward;
	private JButton btn_center;
	private JCheckBox chckbx_with_scan;
	private JButton btn_reset;
	private JTextField textField_load;
	private JButton btn_load;
	private JButton button_RIGHT;
	private Robot robot;
	private JTextField textField_speed_motor;
	private JTextField textField_turn_speed;
	private JButton button_apply_motor_speed;
	private JButton button_apply_turn_speed;
	private JRadioButton enable_key_controls;
	
	private Thread gui;
	private Thread receive;
	private Thread mapthread;
	private Thread polygonthread;
	private Thread debugthread;
	
	private Main parent;
	private JButton map;
	private JButton sensor;
	private JTextField co1_x;
	private JTextField co2_x;
	private JTextField co1_y;
	private JTextField co2_y;
	private JButton btn_add_square;
	private JButton btn_add_wall;
	private JButton btn_add_white;
	private JButton btn_start;
	private JButton btn_finish;
	private JButton btn_race;

	/**
	 * Create the frame.
	 */
	public Advanced(Robot robot, Main parent) {
		this.robot = robot;
		this.parent = parent;
		initialize();
		createEvents();
		setVisible(true);
	}
	
	private void initialize(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 413);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		enable_button_controls = new JRadioButton("enable button controls");
		enable_button_controls.setBounds(6, 97, 147, 23);
		contentPane.add(enable_button_controls);
		
		btn_90_left = new JButton("90L");
		btn_90_left.setBounds(10, 127, 60, 23);
		contentPane.add(btn_90_left);
		
		button_UP = new JButton("/|\\");
		button_UP.setBounds(80, 127, 45, 23);
		contentPane.add(button_UP);
		
		btn_90_right = new JButton("90R");
		btn_90_right.setBounds(135, 127, 60, 23);
		contentPane.add(btn_90_right);
		
		button_LEFT = new JButton("<-");
		button_LEFT.setBounds(20, 161, 45, 23);
		contentPane.add(button_LEFT);
		
		button_DOWN = new JButton("\\|/");
		button_DOWN.setBounds(80, 161, 45, 23);
		contentPane.add(button_DOWN);
		
		button_RIGHT = new JButton("->");
		button_RIGHT.setBounds(132, 161, 45, 23);
		contentPane.add(button_RIGHT);
		
		textField_move = new JTextField();
		textField_move.setBounds(10, 195, 86, 20);
		contentPane.add(textField_move);
		textField_move.setColumns(10);
		
		btn_move = new JButton("Move");
		btn_move.setBounds(106, 195, 80, 23);
		contentPane.add(btn_move);
		
		JLabel lblLengte = new JLabel("length");
		lblLengte.setBounds(159, 74, 44, 17);
		contentPane.add(lblLengte);
		
		JLabel lblHoeken = new JLabel("corners");
		lblHoeken.setBounds(159, 100, 56, 17);
		contentPane.add(lblHoeken);
		
		textField_length = new JTextField();
		textField_length.setBounds(210, 72, 74, 20);
		contentPane.add(textField_length);
		textField_length.setColumns(10);
		
		textField_corners = new JTextField();
		textField_corners.setBounds(210, 98, 74, 20);
		contentPane.add(textField_corners);
		textField_corners.setColumns(10);
				
		button_ride_polygon = new JButton("Ride polygon");
		button_ride_polygon.setBounds(294, 73, 117, 48);
		contentPane.add(button_ride_polygon);
		
		btn_sensor_scan = new JButton("scan all");
		btn_sensor_scan.setBounds(197, 127, 80, 23);
		contentPane.add(btn_sensor_scan);
		
		btn_scan_forward = new JButton("scan forward");
		btn_scan_forward.setBounds(294, 127, 117, 23);
		contentPane.add(btn_scan_forward);
		
		btn_white_line = new JButton("_|_ white line");
		btn_white_line.setBounds(187, 161, 117, 23);
		contentPane.add(btn_white_line);
		
		chckbx_with_scan = new JCheckBox("with scan");
		chckbx_with_scan.setBounds(203, 194, 97, 23);
		contentPane.add(chckbx_with_scan);
		
		btn_center = new JButton("center");
		btn_center.setBounds(308, 161, 103, 23);
		contentPane.add(btn_center);
		
		btn_reset = new JButton("reset");
		btn_reset.setBounds(308, 194, 103, 23);
		contentPane.add(btn_reset);
		
		textField_load = new JTextField();
		textField_load.setBounds(10, 226, 86, 20);
		contentPane.add(textField_load);
		textField_load.setColumns(10);
		
		textField_speed_motor = new JTextField();
		textField_speed_motor.setBounds(247, 8, 86, 20);
		contentPane.add(textField_speed_motor);
		textField_speed_motor.setColumns(10);
		
		textField_turn_speed = new JTextField();
		textField_turn_speed.setBounds(247, 42, 86, 20);
		contentPane.add(textField_turn_speed);
		textField_turn_speed.setColumns(10);
		
		button_apply_motor_speed = new JButton("Apply");
		button_apply_motor_speed.setBounds(343, 8, 68, 20);
		contentPane.add(button_apply_motor_speed);
		
		button_apply_turn_speed = new JButton("Apply");
		button_apply_turn_speed.setBounds(343, 39, 68, 23);
		contentPane.add(button_apply_turn_speed);
		
		btn_load = new JButton("load");
		btn_load.setBounds(105, 225, 80, 23);
		contentPane.add(btn_load);
		
		enable_key_controls = new JRadioButton("enable key controls");
		enable_key_controls.setBounds(6, 71, 147, 23);
		contentPane.add(enable_key_controls);
		
		JLabel lblNewLabel = new JLabel("motor speed");
		lblNewLabel.setBounds(159, 11, 78, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblTurnSpeed = new JLabel("turn speed");
		lblTurnSpeed.setBounds(159, 45, 74, 14);
		contentPane.add(lblTurnSpeed);
		
		map = new JButton("Map");
		map.setBounds(6, 7, 89, 23);
		contentPane.add(map);
		
		sensor = new JButton("Sensor");
		sensor.setBounds(6, 41, 89, 23);
		contentPane.add(sensor);
		
		co1_x = new JTextField();
		co1_x.setBounds(36, 270, 40, 20);
		contentPane.add(co1_x);
		co1_x.setColumns(10);
		
		JLabel lblCoordinate = new JLabel("coordinate 1");
		lblCoordinate.setBounds(10, 257, 86, 14);
		contentPane.add(lblCoordinate);
		
		JLabel lblX = new JLabel("X:");
		lblX.setBounds(10, 273, 16, 14);
		contentPane.add(lblX);
		
		JLabel lblCoordinate_1 = new JLabel("coordinate 2");
		lblCoordinate_1.setBounds(10, 290, 86, 14);
		contentPane.add(lblCoordinate_1);
		
		JLabel label = new JLabel("X:");
		label.setBounds(10, 308, 16, 14);
		contentPane.add(label);
		
		JLabel lblY_1 = new JLabel("Y:");
		lblY_1.setBounds(96, 273, 16, 14);
		contentPane.add(lblY_1);
		
		JLabel lblY = new JLabel("Y:");
		lblY.setBounds(96, 308, 16, 14);
		contentPane.add(lblY);
		
		co2_x = new JTextField();
		co2_x.setColumns(10);
		co2_x.setBounds(36, 305, 40, 20);
		contentPane.add(co2_x);
		
		co1_y = new JTextField();
		co1_y.setColumns(10);
		co1_y.setBounds(113, 270, 40, 20);
		contentPane.add(co1_y);
		
		co2_y = new JTextField();
		co2_y.setColumns(10);
		co2_y.setBounds(113, 305, 40, 20);
		contentPane.add(co2_y);
		
		btn_add_square = new JButton("Add square");

		btn_add_square.setBounds(174, 269, 89, 23);
		contentPane.add(btn_add_square);
		
		btn_add_wall = new JButton("Add wall");

		btn_add_wall.setBounds(174, 304, 89, 23);
		contentPane.add(btn_add_wall);
		
		btn_add_white = new JButton("Add white");

		btn_add_white.setBounds(174, 338, 89, 23);
		contentPane.add(btn_add_white);
		
		btn_start = new JButton("Go to start");

		btn_start.setBounds(308, 269, 103, 23);
		contentPane.add(btn_start);
		
		btn_finish = new JButton("Go to finish");

		btn_finish.setBounds(308, 304, 103, 23);
		contentPane.add(btn_finish);
		
		btn_race = new JButton("Race");

		btn_race.setBounds(308, 338, 103, 23);
		contentPane.add(btn_race);
	}
	
	private void createEvents(){
		// voeg een vakje toe met de opgegeven coordinaten
		btn_add_square.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Tile tile = new Tile(Integer.parseInt(co1_x.getText()),Integer.parseInt(co1_y.getText()));
				robot.getField().addTile(tile);
			}
		});
		
		//voeg een muur toe tussen de opgegeven coordinaten
		btn_add_wall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Border border = new PanelBorder(Integer.parseInt(co1_x.getText()),Integer.parseInt(co1_y.getText()),Integer.parseInt(co2_x.getText()),Integer.parseInt(co2_y.getText()));
				robot.getField().addBorder(border);
			}
		});
		
		// voeg een witte lijn toe tussen de opgegeven coordinaten
		btn_add_white.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Border border = new WhiteBorder(Integer.parseInt(co1_x.getText()),Integer.parseInt(co1_y.getText()),Integer.parseInt(co2_x.getText()),Integer.parseInt(co2_y.getText()));
				robot.getField().addBorder(border);
			}
		});
		
		// ga naar de tegel met de start barcode
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (robot.getStartTile() != null){
					robot.moveToTile(robot.getStartTile());
				}
			}
		});
		
		// ga naar de tegel met de finish barcode
		btn_finish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (robot.getFinishTile() != null){
					robot.moveToTile(robot.getFinishTile());
				}
			}
		});
		
		// ga naar de tegel met de start barcode en vervolgens naar de tegel met de finish barcode
		btn_race.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((robot.getStartTile() != null) && (robot.getFinishTile() != null)){
					robot.moveToTile(robot.getStartTile());
					robot.moveToTile(robot.getFinishTile());
				}
			}
		});
		// besturing met pijltjesknoppen op GUI aan/uit.
				enable_button_controls.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (enable_button_controls.isSelected()){
							enable_key_controls.setEnabled(false);
							button_ride_polygon.setEnabled(false);
							button_apply_turn_speed.setEnabled(false);
							button_apply_motor_speed.setEnabled(false);
							button_UP.setEnabled(true);
							button_DOWN.setEnabled(true);
							button_LEFT.setEnabled(true);
							button_RIGHT.setEnabled(true);
							btn_90_left.setEnabled(true);
							btn_90_right.setEnabled(true);
							btn_move.setEnabled(true);
							DebugBuffer.addInfo("U heeft gekozen voor besturing met pijltjesknoppen.\n");
						}
						if (!enable_button_controls.isSelected()){
							enable_key_controls.setEnabled(true);
							button_ride_polygon.setEnabled(true);
							button_apply_turn_speed.setEnabled(true);
							button_apply_motor_speed.setEnabled(true);
							button_UP.setEnabled(false);
							button_DOWN.setEnabled(false);
							button_LEFT.setEnabled(false);
							button_RIGHT.setEnabled(false);
							btn_90_left.setEnabled(false);
							btn_90_right.setEnabled(false);
							btn_move.setEnabled(false);
							DebugBuffer.addInfo("Maak een keuze uit de opdrachten.\n");
						}
					}
				});
				
				button_UP.addMouseListener(new MouseAdapter() { 
					@Override
					public void mouseReleased(MouseEvent arg0) {
						robot.stopMoving();
					}
					@Override
					public void mousePressed(MouseEvent e) {
						robot.startMovingForward();
					}
				});
				
				button_DOWN.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						robot.stopMoving();
					}
					@Override
					public void mousePressed(MouseEvent e) {
						robot.startMovingBackward();
					}
				});
				
				button_LEFT.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						robot.stopMoving();
					}
					@Override
					public void mousePressed(MouseEvent e) {
						robot.startTurningLeft();
					}
				});
				
				button_RIGHT.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						robot.stopMoving();
					}
					@Override
					public void mousePressed(MouseEvent e) {
						robot.startTurningRight();
					}
				});
				
				// Draai 90 graden links.
				btn_90_left.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						robot.turnLeft(90);
					}
				});
				
				// Draai 90 graden rechts.
				btn_90_right.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						robot.turnRight(90);
					}
				});
				
				// Beweeg vooruit, input.
				btn_move.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
						robot.moveForward(10*Double.parseDouble(textField_move.getText()));
						} catch (NumberFormatException Ne) {
							DebugBuffer.addInfo("Geen getal in invoervelden ingegeven!\n");
						} catch (IllegalArgumentException a){
							DebugBuffer.addInfo("Ongeldige parameters voor veelhoek rijden!\n");
						}
					}
				});
				
				// besturing met toetsenbord aan/uit.
				enable_key_controls.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (enable_key_controls.isSelected()){
							enable_button_controls.setEnabled(false);
							button_ride_polygon.setEnabled(false);
							button_apply_turn_speed.setEnabled(false);
							button_apply_motor_speed.setEnabled(false);
							DebugBuffer.addInfo("U heeft gekozen voor besturing met toetsenbord.\n");
						}
						if (!enable_key_controls.isSelected()){
							enable_button_controls.setEnabled(true);
							button_ride_polygon.setEnabled(true);
							button_apply_turn_speed.setEnabled(true);
							button_apply_motor_speed.setEnabled(true);
							DebugBuffer.addInfo("Maak een keuze uit de opdrachten.\n");
						}
					}
				});
				
				
				// rijdt veelhoek.
				button_ride_polygon.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						
						polygonthread = new Thread(new Runnable() {
							public void run() {
								try {
									robot.drivePolygon(Integer.parseInt(textField_length.getText()), Integer.parseInt(textField_corners.getText()));
									while(robot.isBusy()){}
									DebugBuffer.addInfo("De veelhoek is gereden.\n");
									enable_key_controls.setEnabled(true);
									enable_button_controls.setEnabled(true);
									button_apply_turn_speed.setEnabled(true);
									button_apply_motor_speed.setEnabled(true);
									button_ride_polygon.setEnabled(true);
									DebugBuffer.addInfo("Maak een keuze uit de opdrachten.\n");
								} catch (NumberFormatException e) {
									DebugBuffer.addInfo("Geen getal in invoervelden ingegeven!\n");
									enable_key_controls.setEnabled(true);
									enable_button_controls.setEnabled(true);
									button_apply_turn_speed.setEnabled(true);
									button_apply_motor_speed.setEnabled(true);
									button_ride_polygon.setEnabled(true);
									DebugBuffer.addInfo("Maak een keuze uit de opdrachten.\n");
								} catch (IllegalArgumentException a){
									DebugBuffer.addInfo("Ongeldige parameters voor veelhoek rijden!\n"); 
									enable_key_controls.setEnabled(true);
									enable_button_controls.setEnabled(true);
									button_apply_turn_speed.setEnabled(true);
									button_apply_motor_speed.setEnabled(true);
									button_ride_polygon.setEnabled(true);
								}
							}
						});
						enable_key_controls.setEnabled(false);
						enable_button_controls.setEnabled(false);
						button_apply_turn_speed.setEnabled(false);
						button_apply_motor_speed.setEnabled(false);
						button_ride_polygon.setEnabled(false);
						DebugBuffer.addInfo("U heeft gekozen voor een veelhoek rijden.\n");
						polygonthread.start();
					}
				});
				
				sensor.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SensorDisplay sensor = new SensorDisplay(robot);
					}
				});
				map.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Map map = new Map(robot);
					}
				});
				
				// stelt de motorsnelheid in.
				button_apply_motor_speed.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try{
							robot.setMoveSpeed(Double.parseDouble(textField_speed_motor.getText()));
						} catch (NumberFormatException e) {
							DebugBuffer.addInfo("Geen getal in invoerveld ingegeven!\n");
						}
					}
				});
				
				// stelt de rotatiesnelheid in.
				button_apply_turn_speed.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try{
							robot.setTurnSpeed(Double.parseDouble(textField_turn_speed.getText()));
						} catch (NumberFormatException e) {
							DebugBuffer.addInfo("Geen getal in invoerveld ingegeven!\n");
						}
					}
				});
				
				// registreert toetsaanslagen.
				enable_key_controls.addKeyListener(new KeyAdapter() {
					boolean keyPressed = false;
					@Override
					public void keyPressed(KeyEvent arg0) {
						if(enable_key_controls.isSelected()){
							switch(arg0.getKeyCode()) {
								case KeyEvent.VK_UP:
									if (keyPressed == false) {
										keyPressed = true;
										robot.startMovingForward();
									}
									break;
								case KeyEvent.VK_DOWN:
									if (keyPressed == false) {
										keyPressed = true;
										robot.startMovingBackward();
									}
									break;
								case KeyEvent.VK_LEFT:
									if (keyPressed == false) {
										keyPressed = true;
										robot.startTurningLeft();
									}
									break;
								case KeyEvent.VK_RIGHT:
									if (keyPressed == false) {
										keyPressed = true;;
										robot.startTurningRight();
									}
									break;
								
							}
						}
					}
					@Override
					public void keyReleased(KeyEvent e) {
						if(enable_key_controls.isSelected()){
							switch(e.getKeyCode()) {
								case KeyEvent.VK_UP:
									keyPressed = false;
									robot.stopMoving();
									break;
								case KeyEvent.VK_DOWN:
									keyPressed = false;
									robot.stopMoving();
									break;
								case KeyEvent.VK_LEFT:
									keyPressed = false;
									robot.stopMoving();
									break;
								case KeyEvent.VK_RIGHT:
									keyPressed = false;
									robot.stopMoving();
									break;
							}
						}
					}
				});
				
				// zet loodrecht op witte lijn.
				btn_white_line.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						robot.orientOnWhiteLine(chckbx_with_scan.isSelected());
						enable_key_controls.requestFocus();
					}
				});
				
				// zet in het midden van het vakje.
				btn_center.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						robot.setOnCenterTile();
						enable_key_controls.requestFocus();
					}
				});

				
				// zet positie van robot op 0.
				btn_reset.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						robot.zeroPos();
						parent.resetCanvas();
					}
				});
				
				// scan met de sonar in 4 richtingen.
				btn_sensor_scan.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						enable_key_controls.requestFocus();
						robot.scanSonar();
					}
				});		
				
				// scan met de sonar in de 3 voorwaartse richtingen.
				btn_scan_forward.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						robot.newTileScan();
					}
				});
				
				// laad maze van file path.
				btn_load.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
							String path = textField_load.getText();
							robot.fieldFromFile(path);
					}
				});
	}
}
