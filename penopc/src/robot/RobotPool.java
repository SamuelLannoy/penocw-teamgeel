package robot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Een klasse die alle robots bijhoudt die in het spel zijn.
 * Met op de eerste plaats de eigen robot.
 * @author Dries
 *
 */

public class RobotPool implements Iterable<Robot>{
	
	private Map<String, Robot> robotPool;
	
	public RobotPool(Robot mainRobot){
		robotPool = new HashMap<String, Robot>();
		robotPool.put("main", mainRobot);
	}
	
	public Collection<Robot> getRobots(){
		return robotPool.values();
	}
	
	public Robot getMainRobot(){
		return robotPool.get("main");
	}
	
	public Collection<Robot> getOtherRobots(){
		Collection<Robot> others = new ArrayList<Robot>();
		for (String rob : robotPool.keySet()) {
			if (!rob.equals("main")) {
				others.add(robotPool.get(rob));
			}
		}
		return others;
	}
	
	public void addRobot(Robot robot, String id){
		robotPool.put(id, robot);
	}
	
	public void removeRobot(Robot robot){
		robotPool.remove(robot);
	}
	
	public int getRobotPoolSize(){
		return robotPool.size();
	}
	
	public void updatePosition(){
		/*for (String rob : robotPool.keySet()) {
			if (!rob.equals("main")) {*/
		robotPool.get("main").updatePosition();
			//}
		//}
	}
	
	public void updateRobot(String playerID, double x, double y, double angle) {
		if (robotPool.containsKey(playerID)) {
			robotPool.get(playerID).setSimLoc(x, y, angle);
		}
	}
	
	public void terminate(){
		for(Robot currentRobot:robotPool.values()){
			currentRobot.terminate();
		}
	}
	
	@Override
	public Iterator<Robot> iterator() {
		Iterator<Robot> robotIt = getRobots().iterator();
		return robotIt;
	}

}
