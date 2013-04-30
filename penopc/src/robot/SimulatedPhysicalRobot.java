package robot;

import communication.SeesawStatus;

import exception.CommunicationException;
import field.simulation.FieldSimulation;
import simulator.ISimulator;

public class SimulatedPhysicalRobot implements ISimulator {
	
	private Robot robot;
	
	public SimulatedPhysicalRobot(Robot robot) {
		this.robot = robot;
	}

	@Override
	public void startMovingForward() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startMovingBackward() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveForward(double distance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveBackward(double distance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopMoving() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startTurningLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startTurningRight() {
		// TODO Auto-generated method stub

	}

	@Override
	public void turnLeft(double angle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void turnRight(double angle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTurnSpeed(double turnSpeed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMoveSpeed(double moveSpeed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void orientOnWhiteLine(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scanSonar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOnCenterTile(Robot robot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newTileScan() {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkScan() {
		// TODO Auto-generated method stub

	}

	@Override
	public double getDistanceMoved() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRotationTurned() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isMoving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isScanning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void scanOnlyLines(boolean flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() throws CommunicationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isStartTile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFinishTile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasBall() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getTeam() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getObjectNr() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setObjectNr(int nr) {
		// TODO Auto-generated method stub

	}

	@Override
	public SeesawStatus getSeesawStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pauseLightSensor() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resumeLightSensor() {
		// TODO Auto-generated method stub

	}

	@Override
	public double getTDistanceX() {
		updateStartPos();
		return robot.getCurrTile().getPosition().getX() * 40
				+ robot.getPosition().getPosX()
				+ startx;
	}

	@Override
	public double getTDistanceY() {
		updateStartPos();
		return robot.getCurrTile().getPosition().getY() * 40
				+ robot.getPosition().getPosY()
				+ starty;
	}
	
	private void updateStartPos() {
		startx = world.getStartPos(robot.getPlayerNr()).getX() * 40;
		starty = world.getStartPos(robot.getPlayerNr()).getY() * 40;
	}

	private double startx, starty;
	
	@Override
	public double getStartx() {
		return startx;
	}

	@Override
	public double getStarty() {
		return starty;
	}

	@Override
	public double getTRotation() {
		return robot.getPosition().getRotation();
	}

	@Override
	public void setSimLoc(double x, double y, double angle) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FieldSimulation getField() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHasBall(boolean set) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTeamNr(int nr) {
		// TODO Auto-generated method stub

	}

	@Override
	public RobotPool getRobotPool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRobotPool(RobotPool robotPool) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSeesawStatus(SeesawStatus status) {
		// TODO Auto-generated method stub

	}

	private FieldSimulation world;
	
	@Override
	public void setSimField(FieldSimulation world) {
		this.world = world;
		world.setLocalSimulator(this);
		
	}

}
