package simulator;

import communication.SeesawStatus;

import field.Field;
import field.simulation.FieldSimulation;
import robot.AbstractRobotConnector;
import robot.RobotPool;

public interface ISimulator extends AbstractRobotConnector {	
	public abstract double getTDistanceX();
	public abstract double getTDistanceY();
	public abstract double getStartx();
	public abstract double getStarty();
	public abstract double getTRotation();
	public abstract void setSimLoc(double x, double y, double angle);
	
	public abstract double getWidth();
	public abstract double getLength();
	
	public abstract FieldSimulation getField();
	
	public abstract void setHasBall(boolean set);
	
	public abstract void setTeamNr(int nr);
	
	public abstract RobotPool getRobotPool();
	public abstract void setRobotPool(RobotPool robotPool);
	
	public abstract void setSeesawStatus(SeesawStatus status);
	
	public abstract void setSimField(FieldSimulation world);
}
