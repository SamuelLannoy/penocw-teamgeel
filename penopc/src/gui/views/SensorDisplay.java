package gui.views;

import field.Barcode;
import field.BarcodeType;
import gui.tools.PlotCanvas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import robot.Robot;
import robot.SensorBuffer;

import javax.swing.border.TitledBorder;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SensorDisplay extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Robot robot;
	private Main parent;
	private Timer sensorTimer;
	private List<Integer> plotList = Collections.synchronizedList(new ArrayList<Integer>());
	
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
						
						//update infrared sensor data.
						textArea_infrared.setText("Ahead: "+SensorBuffer.getInfrared());
						
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
	private JTextArea textArea_light;
	private JTextArea textArea_ultrasonic_front;
	private JTextArea textArea_pressure;
	private JTextArea textArea_ultrasonic_left;
	private JTextArea textArea_ultrasonic_back;
	private JTextArea textArea_ultrasonic_right;
	private PlotCanvas canvas_Light;
	private JTextArea textArea_infrared;
	private JTextArea textArea_multiscan;
	/**
	 * Create the frame.
	 */
	public SensorDisplay(Robot robot, Main parent) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				sensorTimer.stop();
			}
		});
		this.robot = robot;
		this.parent = parent;
		initComponents();
		createEvents();
		this.setVisible(true);
		sensorthread.start();
	}
	
	private void initComponents(){
		// General
		setTitle("Sensor Display");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Pressure
		JPanel panel_pressure = new JPanel();
		panel_pressure.setBounds(10, 10, 280, 75);
		panel_pressure.setBorder(new TitledBorder(null, "Pressure sensor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_pressure);
		panel_pressure.setLayout(null);
		
		JScrollPane scrollPane_pressure = new JScrollPane();
		scrollPane_pressure.setBounds(10, 20, 260, 40);
		panel_pressure.add(scrollPane_pressure);
		
		textArea_pressure = new JTextArea();
		scrollPane_pressure.setViewportView(textArea_pressure);
		
		// Ultrasonic
		JPanel panel_ultrasonic = new JPanel();
		panel_ultrasonic.setBounds(10, 375, 580, 290);
		panel_ultrasonic.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Ultrasonic sensor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_ultrasonic);
		panel_ultrasonic.setLayout(null);
		
		JScrollPane scrollPane_ultrasonic_front = new JScrollPane();
		scrollPane_ultrasonic_front.setBounds(10, 35, 260, 40);
		panel_ultrasonic.add(scrollPane_ultrasonic_front);
		
		textArea_ultrasonic_front = new JTextArea();
		textArea_ultrasonic_front.setText("");
		scrollPane_ultrasonic_front.setViewportView(textArea_ultrasonic_front);
		
		JLabel lblFront = new JLabel("front");
		lblFront.setBounds(10, 20, 46, 14);
		panel_ultrasonic.add(lblFront);
		
		JLabel lblBack = new JLabel("back");
		lblBack.setBounds(300, 20, 46, 14);
		panel_ultrasonic.add(lblBack);
		
		JLabel lblLeft = new JLabel("left");
		lblLeft.setBounds(10, 85, 46, 14);
		panel_ultrasonic.add(lblLeft);
		
		JLabel lblRight = new JLabel("right");
		lblRight.setBounds(300, 85, 46, 14);
		panel_ultrasonic.add(lblRight);
		
		JScrollPane scrollPane_ultrasonic_back = new JScrollPane();
		scrollPane_ultrasonic_back.setBounds(300, 35, 260, 40);
		panel_ultrasonic.add(scrollPane_ultrasonic_back);
		
		textArea_ultrasonic_back = new JTextArea();
		textArea_ultrasonic_back.setText("");
		scrollPane_ultrasonic_back.setViewportView(textArea_ultrasonic_back);
		
		JScrollPane scrollPane_ultrasonic_left = new JScrollPane();
		scrollPane_ultrasonic_left.setBounds(10, 100, 260, 40);
		panel_ultrasonic.add(scrollPane_ultrasonic_left);
		
		textArea_ultrasonic_left = new JTextArea();
		textArea_ultrasonic_left.setText("");
		scrollPane_ultrasonic_left.setViewportView(textArea_ultrasonic_left);
		
		JScrollPane scrollPane_ultrasonic_right = new JScrollPane();
		scrollPane_ultrasonic_right.setBounds(300, 100, 260, 40);
		panel_ultrasonic.add(scrollPane_ultrasonic_right);
		
		textArea_ultrasonic_right = new JTextArea();
		textArea_ultrasonic_right.setText("");
		scrollPane_ultrasonic_right.setViewportView(textArea_ultrasonic_right);
		
		JScrollPane scrollPane_multiscan = new JScrollPane();
		scrollPane_multiscan.setBounds(10, 165, 260, 100);
		panel_ultrasonic.add(scrollPane_multiscan);
		
		textArea_multiscan = new JTextArea();
		scrollPane_multiscan.setViewportView(textArea_multiscan);
		
		JLabel lblMultiscanValues = new JLabel("multiscan values");
		lblMultiscanValues.setBounds(10, 150, 150, 14);
		panel_ultrasonic.add(lblMultiscanValues);
		
		// Light
		JPanel panel_light = new JPanel();
		panel_light.setBounds(10, 90, 580, 280);
		panel_light.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Light sensor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_light);
		panel_light.setLayout(null);
		
		JScrollPane scrollPane_light = new JScrollPane();
		scrollPane_light.setBounds(10, 20, 260, 40);
		panel_light.add(scrollPane_light);
		
		textArea_light = new JTextArea();
		textArea_light.setText("");
		scrollPane_light.setViewportView(textArea_light);
		
		canvas_Light = new PlotCanvas(plotList);
		canvas_Light.setBounds(10, 70, 560, 200);
		canvas_Light.setBackground(new Color(160, 82, 45));
		panel_light.add(canvas_Light);
		
		// Infrared
		JPanel panel_infrared = new JPanel();
		panel_infrared.setBounds(305, 10, 280, 75);
		panel_infrared.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Infrared sensor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_infrared);
		panel_infrared.setLayout(null);
		
		JScrollPane scrollPane_infrared = new JScrollPane();
		scrollPane_infrared.setBounds(10, 20, 260, 40);
		panel_infrared.add(scrollPane_infrared);
		
		textArea_infrared = new JTextArea();
		scrollPane_infrared.setViewportView(textArea_infrared);
		
	}
	
	private void createEvents(){
		
	}
}
