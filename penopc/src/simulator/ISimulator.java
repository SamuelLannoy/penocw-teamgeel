package simulator;

import field.Field;
import robot.AbstractRobotConnector;

public interface ISimulator extends AbstractRobotConnector {	
	public abstract double getTDistanceX();
	public abstract double getTDistanceY();
	public abstract double getTRotation();
	public abstract void setSimLoc(double x, double y, double angle);
	
	public abstract double getWidth();
	public abstract double getLength();
	
	public abstract Field getField();
	
	public abstract void setHasBall(boolean set);
	
	public abstract void setTeamNr(int nr);
}
