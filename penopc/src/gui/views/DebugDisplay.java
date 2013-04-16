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

import robot.DebugBuffer;
import robot.Robot;

public class DebugDisplay extends JFrame {

	private JPanel contentPane;
	private Robot robot;
	private Main parent;
	private JTextArea robot_current_action;
	private JTextArea debugwindow;
	private Timer debugTimer;
	
	private Thread debugthread = new Thread(new Runnable() {
		public void run() {
			debugTimer = new Timer(100, new ActionListener() {
			    public void actionPerformed(ActionEvent evt) {
			    	if (robot != null) {
						robot_current_action.setText(robot.getCurrentAction());
						try {
							// update debug info.
				    		synchronized(DebugBuffer.getDebuginfo()) {
								for(String debuginfo : DebugBuffer.getDebuginfo()) {
									debugwindow.append(""+debuginfo+"\n");
								}
								DebugBuffer.getDebuginfo().clear();
				    		}
				    		/**
							// update comm info.
				    		synchronized(DebugBuffer.getComminfo()) {
								for(String comminfo : DebugBuffer.getComminfo()) {
									textArea_messages.append(""+comminfo+"\n");
								}
								DebugBuffer.getComminfo().clear();
				    		}
				    		**/
						}
						catch (NullPointerException e) {
							
						}
			    	}
			    }    
			});
			debugTimer.start();
		}
	});
	/**
	 * Create the frame.
	 */
	public DebugDisplay(Robot robot, Main parent) {
		this.robot = robot;
		this.parent = parent;
		setTitle("Debug Display");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane_debugwindow = new JScrollPane();
		scrollPane_debugwindow.setBounds(10, 10, 280, 280);
		contentPane.add(scrollPane_debugwindow);
		
		debugwindow = new JTextArea();
		scrollPane_debugwindow.setViewportView(debugwindow);
		debugwindow.append("Maak een keuze uit robot of simulator.\n");
		contentPane.add(robot_current_action);
		debugthread.start();
	}

}
