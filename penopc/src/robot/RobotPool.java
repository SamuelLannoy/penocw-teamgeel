package robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import field.Tile;

/**
 * Een klasse die alle robots bijhoudt die in het spel zijn.
 * Met op de eerste plaats de eigen robot.
 * @author Dries
 *
 */

public class RobotPool implements Iterable<RobotModel>{
	
	private Map<String, RobotModel> robotPool;
	
	private String ownId;
	
	public RobotPool(Robot mainRobot, String ownId){
		robotPool = new HashMap<String, RobotModel>();
		robotPool.put("main", mainRobot);
		this.ownId = ownId;
	}
	
	public Collection<RobotModel> getRobots(){
		return robotPool.values();
	}
	
	public Robot getMainRobot(){
		return (Robot)robotPool.get("main");
	}
	
	public Collection<RobotModel> getOtherRobots(){
		Collection<RobotModel> others = new ArrayList<RobotModel>();
		for (String rob : robotPool.keySet()) {
			if (!rob.equals("main")) {
				others.add(robotPool.get(rob));
			}
		}
		return others;
	}
	
	public void addRobot(RobotModel robot, String id){
		robotPool.put(id, robot);
	}
	
	public void removeRobot(String id){
		robotPool.remove(id);
	}
	
	public RobotModel getRobot(String id) {
		if (!robotPool.containsKey(id)) {
			if (id.equals(ownId)) {
				return getMainRobot();
			}
			RobotModel newModel = new RobotModel();
			newModel.setCurrTile(new Tile(0, 0));
			addRobot(newModel, id);
		}
		return robotPool.get(id);
	}
	
	public int getRobotPoolSize(){
		return robotPool.size();
	}
	
	public void updatePosition(){
		/*for (String rob : robotPool.keySet()) {
			if (!rob.equals("main")) {*/
		getMainRobot().updatePosition();
			//}
		//}
	}
	
	public void updateRobot(String playerID, double x, double y, double angle) {
		if (!playerID.equals("main")) {
			getRobot(playerID).setGlobalPosition(x, y, angle);
			//DebugBuffer.addInfo(playerID + ": " + x + " " + y + " " + angle);
		}
	}
	
	public void terminate(){
		/*for(Robot currentRobot:robotPool.values()){
			currentRobot.terminate();
		}*/
		getMainRobot().terminate();
	}
	
	@Override
	public Iterator<RobotModel> iterator() {
		return getRobots().iterator();
	}

}
