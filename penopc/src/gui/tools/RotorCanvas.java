package gui.tools;

import java.awt.Canvas;
import java.awt.Graphics;

import robot.Robot;

//Tekent de rotatierichting van de rotor.
@SuppressWarnings("serial")
public class RotorCanvas extends Canvas{
	
	private Robot robot;
	
	public RotorCanvas(Robot robot){
		setRobot(robot);
	}
	
	public void setRobot(Robot robot){
		this.robot = robot;
	}
	
	public void paint(Graphics g){
		paintRotorPos(g);
	}
	
	public void paintRotorPos(Graphics g){
		double r = (Math.PI/2); //rotatie van rotor.
		g.fillOval(22,22, 5, 5);
		int x = (int) (25 - (10 * Math.cos(r)));
		int y = (int) (25 - (10 * Math.sin(r)));
		g.drawLine(25, 25, x, y);
	}
}	
