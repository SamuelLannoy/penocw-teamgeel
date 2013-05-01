package gui.views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import robot.Robot;

public class PositionDisplay extends JFrame {

	private JPanel contentPane;
	private Robot robot;
	private Main parent;
	private JTextArea movement_window;
	private Timer posTimer;
	
	private Thread posthread = new Thread(new Runnable() {
		private int incr = 0;
		
		public void run() {
			posTimer = new Timer(100, new ActionListener() { // TODO timer 1 voor sim
			    public void actionPerformed(ActionEvent evt) {
			    	if (robot != null) {
						robot.updatePosition();
						incr++;
						if(incr > 10) {
							robot.sendPosition();
							incr = 0;
						}
						movement_window.append(robot.getPosition() + "\n");
			    	}
			    }    
			});
			posTimer.start();
		}
	});
	
	
	/**
	 * Create the frame.
	 */
	public PositionDisplay(Robot robot, Main parent) {
		this.robot = robot;
		this.parent = parent;
		setTitle("Position Display");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(500, 196, 300, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane movePane = new JScrollPane();
		movePane.setBounds(10, 10, 280, 280);
		contentPane.add(movePane);
		
		movement_window = new JTextArea();
		movePane.setViewportView(movement_window);
		
		posthread.start();
		this.setVisible(true);
	}

}
