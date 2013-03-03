package simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

import exception.CommunicationException;
import field.Field;
import field.fromfile.FieldFactory;
import robot.Robot;

public class SimulatorConnector implements ISimulator {
	private Timer timer = new Timer(1, new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
	    		cmdMger.tick();
	    		movMger.tick();
	        }
	    });
	
	private CommandManager cmdMger;
	private IMovementManager movMger;
	
	private double width;
	private double length;
	
	public double getWidth() {
		return width;
	}

	public double getLength() {
		return length;
	}
	
	private Field field;
	
	public Field getField() {
		return field;
	}
	
	public SimulatorConnector() {
		this(.5, 2);
	}

	public SimulatorConnector(double mov, double turn) {
		movMger = new MovementManager(this, mov, turn);
		cmdMger = new CommandManager(this);
		movMger.setCmdMger(cmdMger);
		cmdMger.setMovMger(movMger);
		width = 6;
		length = 10;
	}

	@Override
	public void startMovingForward() {
		cmdMger.addCommand(new Command(CommandType.STARTFORWARD));		
	}

	@Override
	public void startMovingBackward() {
		cmdMger.addCommand(new Command(CommandType.STARTBACKWARD));		
	}

	@Override
	public void moveForward(double distance) {
		cmdMger.addCommand(new Command((int)(distance/movMger.getMoveSpeed()), CommandType.FORWARD));		
	}

	@Override
	public void moveBackward(double distance) {
		cmdMger.addCommand(new Command((int)(distance/movMger.getMoveSpeed()), CommandType.BACKWARD));
	}

	@Override
	public void stopMoving() {
		cmdMger.clear();
		movMger.stopMoving();
	}

	@Override
	public void startTurningLeft() {
		cmdMger.addCommand(new Command(CommandType.STARTLEFT));		
	}

	@Override
	public void startTurningRight() {
		cmdMger.addCommand(new Command(CommandType.STARTRIGHT));		
	}

	@Override
	public void turnLeft(double angle) {
		cmdMger.addCommand(new Command((int)(angle/movMger.getTurnSpeed()), CommandType.LEFT));	
	}

	@Override
	public void turnRight(double angle) {
		cmdMger.addCommand(new Command((int)(angle/movMger.getTurnSpeed()), CommandType.RIGHT));	
	}

	@Override
	public void setTurnSpeed(double turnSpeed) {
		movMger.setTurnSpeed(turnSpeed);
	}

	@Override
	public void setMoveSpeed(double moveSpeed) {
		movMger.setMoveSpeed(moveSpeed);
	}

	@Override
	public void orientOnWhiteLine(boolean b) {
		// TODO fix this
		moveForward(170);
	}

	@Override
	public void scanSonar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnCenterTile(Robot robot) {
		// TODO implement
		
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
		return movMger.getDistanceMoved();
	}

	@Override
	public double getRotationTurned() {
		return movMger.getRotationTurned();
	}

	@Override
	public boolean isMoving() {
		return movMger.isMoving();
	}

	@Override
	public boolean isScanning() {
		return false;
	}

	@Override
	public void scanOnlyLines(boolean flag) {
		// TODO implement
	}

	@Override
	public void initialize() throws CommunicationException {
		timer.start();
		setupField();
	}
	
	private void setupField() {
		if (getField() == null) {

			try {
				field = FieldFactory.fieldFromFile("C:\\demo2.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
			//maze.addBall(new Ball(1), new Position(0, 7));
		}
	}

	@Override
	public void terminate() {
		timer.stop();
	}

	@Override
	public boolean isStartTile() {
		return false;
	}

	@Override
	public boolean isFinishTile() {
		return false;
	}

	private boolean hasBall;
	
	@Override
	public void setHasBall(boolean set) {
		hasBall = set;
	}

	@Override
	public boolean hasBall() {
		return hasBall;
	}

	private int objectNr;
	
	@Override
	public int getObjectNr() {
		return objectNr;
	}
	
	@Override
	public void setObjectNr(int objectNr) {
		this.objectNr = objectNr;
	}

	private int teamNr = -1;
	
	@Override
	public int getTeam() {
		return teamNr;
	}

	@Override
	public void setTeamNr(int nr) {
		teamNr = nr;
	}

	@Override
	public double getTDistanceX() {
		return movMger.getTDistanceX();
	}

	@Override
	public double getTDistanceY() {
		return movMger.getTDistanceY();
	}

	@Override
	public double getTRotation() {
		return movMger.getTRotation();
	}

	@Override
	public void setSimLoc(double x, double y, double angle) {
		movMger.setSimLoc(x, y, angle);
	}

}
