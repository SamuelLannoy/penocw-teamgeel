package simulator;

public interface IMovementManager extends Tickable {

	public abstract double getTurnSpeed();
	public abstract double getMoveSpeed();
	
	public abstract void setMoveSpeed(double set);
	public abstract void setTurnSpeed(double set);
	
	public abstract double getTDistanceX();
	public abstract double getTDistanceY();
	public abstract double getTRotation();
	public abstract void setSimLoc(double x, double y, double angle);
	
	public abstract double getDistanceMoved();
	public abstract double getRotationTurned();
	
	public abstract boolean isMoving();
	
	public abstract void setTicksMoving(int ticks);
	public abstract void setTicksTurning(int ticks);
	public abstract void setMovingMode(MovingMode mode);
	public abstract void setTurningMode(TurningMode mode);
	
	public abstract void orientOnWhiteLineExec(boolean scan);
	
	public abstract void setCmdMger(CommandManager cmdMger);
	
	public abstract void stopMoving();
}
