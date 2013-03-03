package simulator;

import corner.Corner;

public class MovementManager implements Tickable, IMovementManager {
	
	private ISimulator conn;
	private CommandManager cmdMger;
	
	public MovementManager(ISimulator conn, double movSpeed, double turnSpeed) {
		this.conn = conn;
		this.turnSpeed = turnSpeed;
		this.moveSpeed = movSpeed;
	}
	
	public void setCmdMger(CommandManager cmdMger) {
		this.cmdMger = cmdMger;
	}
	
	private double turnSpeed;
	
	private double moveSpeed;

	@Override
	public double getTurnSpeed() {
		return turnSpeed;
	}

	@Override
	public void setTurnSpeed(double turnSpeed) {
		this.turnSpeed = turnSpeed;
	}

	@Override
	public double getMoveSpeed() {
		return moveSpeed;
	}

	@Override
	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	private TurningMode turningMode = TurningMode.None;
	
	private MovingMode movingMode = MovingMode.None;

	public TurningMode getTurningMode() {
		return turningMode;
	}

	@Override
	public void setTurningMode(TurningMode turningMode) {
		this.turningMode = turningMode;
	}

	public MovingMode getMovingMode() {
		return movingMode;
	}

	@Override
	public void setMovingMode(MovingMode movingMode) {
		this.movingMode = movingMode;
	}
	
	private void clearModes() {
		setMovingMode(MovingMode.None);
		setTurningMode(TurningMode.None);
	}

	@Override
	public boolean isMoving() {
		return !(movingMode == MovingMode.None && turningMode == TurningMode.None);
	}
	
	private double tdistancex;
	private double tdistancey;
	private double trotation;

	@Override
	public double getTDistanceX() {
		return tdistancex;
	}
	
	public void setTDistanceX(double set) {
		tdistancex = set;
	}

	@Override
	public double getTDistanceY() {
		return tdistancey;
	}
	
	public void setTDistanceY(double set) {
		tdistancey = set;
	}

	@Override
	public double getTRotation() {
		return trotation;
	}
	
	public void setTRotation(double set) {
		trotation = set;
		if (trotation < 0)
			trotation += 360;
		if (trotation > 360) 
			trotation -= 360;
	}
	
	private double distancemoved;
	private double rotationturned;

	public double getDistanceMoved() {
		double temp = distancemoved;
		distancemoved = 0.0;
		return temp;
	}
	
	public void addDistanceMoved(double val) {
		distancemoved += val;
	}

	public double getRotationTurned() {
		double temp = rotationturned;
		rotationturned = 0.0;
		return temp;
	}
	
	public void addRotationTurned(double val) {
		rotationturned += val;
	}

	private void moveTick() {
		if (getMovingMode().sign() == 0)
			return;
		double newx = getTDistanceX() + getMoveSpeed() * getMovingMode().sign() * Math.sin(getTRotation());
		double newy = getTDistanceY() + getMoveSpeed() * getMovingMode().sign() * Math.cos(getTRotation());
		double[][] newcorners = Corner.getCorners(newx, newy, conn.getWidth(), conn.getLength(), getTRotation());
		
		if (conn.getField().collidesWithBall(newcorners)) {
			conn.setHasBall(true);
		}
		if (!collides(newcorners)) {
			addDistanceMoved(getMoveSpeed() * getMovingMode().sign());
			setTDistanceX(newx);
			setTDistanceX(newy);
			System.out.println("x: " + newx + " y: " + newy);
		}
	}
	
	private void turnTick() {
		if (getTurningMode().sign() == 0)
			return;
		double newrot = getTRotation() + getTurnSpeed() * getTurningMode().sign();
		
		double[][] newcorners = Corner.getCorners(getTDistanceX(), getTDistanceY(), conn.getWidth(), conn.getLength(), newrot);
		if (!collides(newcorners)) {
			addRotationTurned((getTurnSpeed() * getTurningMode().sign()));
			setTRotation(newrot);
		}
	}
	
	private boolean collides(double[][] corners) {
		try {
			return conn.getField().collidesWithBorder(corners);
		} catch (IllegalArgumentException e){
			return false;
		}
	}
	
	@Override
	public void tick() {
		moveTick();
		turnTick();
		checkTicksMoving();
		reduceTicksMoving();
		checkTicksTurning();
		reduceTicksTurning();
	}

	private int ticksMoving;
	
	private int getTicksMoving() {
		return ticksMoving;
	}
	
	@Override
	public void setTicksMoving(int ticksMoving) {
		this.ticksMoving = ticksMoving;
	}
	
	private void checkTicksMoving() {
		if (getTicksMoving() == 1) {
			cmdMger.resetCurrentCommand();
			clearModes();
		}
	}
	
	private void reduceTicksMoving() {
		if (getTicksMoving() >= 1)
			setTicksMoving(getTicksMoving() - 1);
	}

	private int ticksTurning;
	
	private int getTicksTurning() {
		return ticksTurning;
	}

	@Override
	public void setTicksTurning(int ticksTurning) {
		this.ticksTurning = ticksTurning;
	}
	
	private void checkTicksTurning() {
		if (getTicksTurning() == 1) {
			cmdMger.resetCurrentCommand();
			clearModes();
		}
	}
	
	private void reduceTicksTurning() {
		if (getTicksTurning() >= 1)
			setTicksTurning(getTicksTurning() - 1);
	}

	@Override
	public void orientOnWhiteLineExec(boolean scan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSimLoc(double x, double y, double angle) {
		setTDistanceX(x);
		setTDistanceY(y);
		setTRotation(angle);
	}
	
	
	public void stopMoving() {
		clearModes();
		setTicksMoving(0);
		setTicksTurning(0);
	}
	

}
