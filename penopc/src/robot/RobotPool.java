package robot;

import java.awt.List;
import java.util.ArrayList;

/**
 * Een klasse die alle robots bijhoudt die in het spel zijn.
 * Met op de eerste plaats de eigen robot.
 * @author Dries
 *
 */

public class RobotPool {
	
	private ArrayList<Robot> robotPool = new ArrayList<Robot>();
	
	public RobotPool(Robot mainRobot){
		robotPool.set(0, mainRobot);
	}
	
	public ArrayList<Robot> getRobotPool(){
		return robotPool;
	}
	
	public Robot getMainRobot(){
		return robotPool.get(0);
	}
	
	public ArrayList<Robot> getOtherRobots(){
		ArrayList<Robot> others = robotPool;
		others.remove(0);
		return others;
	}
	
	public void addRobot(Robot robot){
		robotPool.add(robot);
	}
	
	public void removeRobot(Robot robot){
		robotPool.remove(robot);
	}
	
	public int getRobotPoolSize(){
		return robotPool.size();
	}
	
	public void updatePosition(){
		for(Robot currentRobot:robotPool){
			currentRobot.updatePosition();
		}
	}
}
