package gui.views;

import gui.tools.DrawCanvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import robot.Robot;
import robot.RobotPool;

public class Map extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Component canvas;
	private RobotPool robotPool;
	
	private Timer timer = new Timer(500, new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
	    	if (robotPool != null) {
				Thread thread = new Thread(new Runnable() {
					public void run() {
						try {
						robotPool.updatePosition();
						canvas.update(canvas.getGraphics());
						}
						catch (NullPointerException e) {
							
						}
					}
				});
				thread.start();
	    	}
	    }    
	});
	
	/**
	 * Create the frame.
	 */
	public Map(RobotPool robotPool) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				timer.stop();
			}
		});
		this.robotPool = robotPool;
		initComponents();
		createEvents();
		this.setVisible(true);
		timer.start();
	}
	
	private void initComponents(){
		setTitle("Map");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(610, 5, 700, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		canvas = new DrawCanvas(robotPool);
		canvas.setBackground(new Color(160, 82, 45));
		canvas.setBounds(1062, 290, 265, 241);
		contentPane.add(canvas);
	}
	
	private void createEvents(){
		
	}

}
