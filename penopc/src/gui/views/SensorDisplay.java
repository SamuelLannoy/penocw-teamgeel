package gui.views;

import gui.tools.PlotCanvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
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
import java.awt.Canvas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SensorDisplay extends JFrame {

	private JPanel contentPane;
	private Robot robot;
	private List<Integer> plotList = Collections.synchronizedList(new ArrayList<Integer>());
	
	private Timer timer = new Timer(500, new ActionListener() {
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
					if (SensorBuffer.canClear()){
						textArea_ultrasonic_front.setText(""+SensorBuffer.getDistances().get(0)+"\n");
						textArea_ultrasonic_left.setText(""+SensorBuffer.getDistances().get(1)+"\n");
						textArea_ultrasonic_back.setText(""+SensorBuffer.getDistances().get(2)+"\n");
						textArea_ultrasonic_right.setText(""+SensorBuffer.getDistances().get(3)+"\n");
						SensorBuffer.getDistances().clear();
						SensorBuffer.setClear(false);
					}
				}

				// update pressure sensor data.
				textArea_pressure.setText(""+SensorBuffer.getTouched()+"\n");
	    	}
	    }    
	});
	private JTextArea textArea_light;
	private JTextArea textArea_ultrasonic_front;
	private JTextArea textArea_pressure;
	private JTextArea textArea_ultrasonic_left;
	private JTextArea textArea_ultrasonic_back;
	private JTextArea textArea_ultrasonic_right;
	private PlotCanvas canvas_Light;
	/**
	 * Create the frame.
	 */
	public SensorDisplay(Robot robot) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				timer.stop();
			}
		});
		this.robot = robot;
		initComponents();
		createEvents();
		this.setVisible(true);
		timer.start();
	}
	
	private void initComponents(){
		setTitle("Sensor Display");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(5, 5, 600, 637);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_pressure = new JPanel();
		panel_pressure.setBorder(new TitledBorder(null, "Pressure sensor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_pressure.setBounds(10, 11, 270, 80);
		contentPane.add(panel_pressure);
		panel_pressure.setLayout(null);
		
		JScrollPane scrollPane_pressure = new JScrollPane();
		scrollPane_pressure.setBounds(10, 21, 250, 50);
		panel_pressure.add(scrollPane_pressure);
		
		textArea_pressure = new JTextArea();
		scrollPane_pressure.setViewportView(textArea_pressure);
		
		JPanel panel_ultrasonic = new JPanel();
		panel_ultrasonic.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Ultrasonic sensor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_ultrasonic.setBounds(10, 379, 564, 290);
		contentPane.add(panel_ultrasonic);
		panel_ultrasonic.setLayout(null);
		
		JScrollPane scrollPane_ultrasonic_front = new JScrollPane();
		scrollPane_ultrasonic_front.setBounds(10, 45, 250, 50);
		panel_ultrasonic.add(scrollPane_ultrasonic_front);
		
		textArea_ultrasonic_front = new JTextArea();
		textArea_ultrasonic_front.setText("");
		scrollPane_ultrasonic_front.setViewportView(textArea_ultrasonic_front);
		
		JLabel lblFront = new JLabel("front");
		lblFront.setBounds(10, 20, 46, 14);
		panel_ultrasonic.add(lblFront);
		
		JLabel lblBack = new JLabel("back");
		lblBack.setBounds(271, 20, 46, 14);
		panel_ultrasonic.add(lblBack);
		
		JLabel lblLeft = new JLabel("left");
		lblLeft.setBounds(10, 106, 46, 14);
		panel_ultrasonic.add(lblLeft);
		
		JLabel lblRight = new JLabel("right");
		lblRight.setBounds(271, 106, 46, 14);
		panel_ultrasonic.add(lblRight);
		
		JScrollPane scrollPane_ultrasonic_back = new JScrollPane();
		scrollPane_ultrasonic_back.setBounds(270, 45, 250, 50);
		panel_ultrasonic.add(scrollPane_ultrasonic_back);
		
		textArea_ultrasonic_back = new JTextArea();
		textArea_ultrasonic_back.setText("");
		scrollPane_ultrasonic_back.setViewportView(textArea_ultrasonic_back);
		
		JScrollPane scrollPane_ultrasonic_left = new JScrollPane();
		scrollPane_ultrasonic_left.setBounds(10, 131, 250, 50);
		panel_ultrasonic.add(scrollPane_ultrasonic_left);
		
		textArea_ultrasonic_left = new JTextArea();
		textArea_ultrasonic_left.setText("");
		scrollPane_ultrasonic_left.setViewportView(textArea_ultrasonic_left);
		
		JScrollPane scrollPane_ultrasonic_right = new JScrollPane();
		scrollPane_ultrasonic_right.setBounds(271, 131, 250, 50);
		panel_ultrasonic.add(scrollPane_ultrasonic_right);
		
		textArea_ultrasonic_right = new JTextArea();
		textArea_ultrasonic_right.setText("");
		scrollPane_ultrasonic_right.setViewportView(textArea_ultrasonic_right);
		
		JPanel panel_light = new JPanel();
		panel_light.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Light sensor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_light.setBounds(10, 89, 564, 290);
		contentPane.add(panel_light);
		panel_light.setLayout(null);
		
		JScrollPane scrollPane_light = new JScrollPane();
		scrollPane_light.setBounds(10, 23, 250, 50);
		panel_light.add(scrollPane_light);
		
		textArea_light = new JTextArea();
		textArea_light.setText("");
		scrollPane_light.setViewportView(textArea_light);
		
		canvas_Light = new PlotCanvas(plotList);
		canvas_Light.setBounds(10, 79, 544, 200);
		canvas_Light.setBackground(new Color(160, 82, 45));
		panel_light.add(canvas_Light);
		//canvas_Light.paint(canvas_Light.getGraphics());
	}
	
	private List<Integer> getPlotList(){
		return plotList;
	}
	private void createEvents(){
		
	}
}
