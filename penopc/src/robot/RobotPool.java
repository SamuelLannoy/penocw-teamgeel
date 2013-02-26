package robot;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Een klasse die alle robots bijhoudt die in het spel zijn.
 * Met op de eerste plaats de eigen robot.
 * @author Dries
 *
 */

public class RobotPool implements Iterable<Robot>{
	
	private ArrayList<Robot> robotPool;
	
	public RobotPool(Robot mainRobot){
		robotPool = new ArrayList<Robot>();
		robotPool.add(mainRobot);
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
	
	public void terminate(){
		for(Robot currentRobot:robotPool){
			currentRobot.terminate();
		}
	}
	
	@Override
	public Iterator<Robot> iterator() {
		Iterator<Robot> robotIt = robotPool.iterator();
		return robotIt;
	}

}
