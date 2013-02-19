package simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.Timer;

import lejos.robotics.navigation.Move.MoveType;

import field.*;
import field.fromfile.FieldFactory;

import robot.AbstractRobotConnector;
import robot.Robot;
import robot.SensorBuffer;
import simulator.lightsensor.LightSensor;
import simulator.lightsensor.LightSensorUpdate;
import simulator.touchsensor.TouchSensor;
import simulator.ultrasonicsensor.UltrasonicSensor;

/**
 * @author  Samuel
 */
public class VirtualRobotConnector implements AbstractRobotConnector {
	
	private static VirtualRobotConnector instance;
	
	public static VirtualRobotConnector getInstance() {
		return instance;
	}

	private Timer timer = new Timer(1, new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
	    		tick();
	    		TouchSensor.getInstance().isPressed();
	        }
	    });

	public VirtualRobotConnector() {
		this(0.5, 2);
	}

	public VirtualRobotConnector(double mvSpeed, double trnSpeed) {
		setMoveSpeed(mvSpeed);
		setTurnSpeed(trnSpeed);
		instance = this;
	}
	
	
	private double width = 2;
	private double length = 2;
	
	public double[][] getCorners() {
		double[][] arr = new double[4][2];
		
		double tx = getTDistanceX();
		double ty = getTDistanceY();
		double ta = -getTRotation() * Math.PI / 180;
		
		System.out.println("rot: " + ta);
		
		//left upper
		arr[0][0] = tx + ( width * Math.cos(ta + Math.PI) + length * Math.sin(ta + Math.PI)); 
		arr[0][1] = ty + ( length * Math.sin(ta + Math.PI / 2)  + width * Math.cos(ta + Math.PI / 2)); 
		//right upper
		arr[1][0] = tx + ( width * Math.cos(ta)  + length * Math.sin(ta)); 
		arr[1][1] = ty + ( length * Math.sin(ta + Math.PI / 2)  + width * Math.cos(ta + Math.PI / 2)); 
		//left lower
		arr[2][0] = tx + ( width * Math.cos(ta + Math.PI)  + length * Math.sin(ta + Math.PI)); 
		arr[2][1] = ty + ( length * Math.sin(ta + 3 * Math.PI / 2)  + width * Math.cos(ta + 3 * Math.PI / 2)); 
		//right lower
		arr[3][0] = tx + ( width * Math.cos(ta)  + length * Math.sin(ta)); 
		arr[3][1] = ty + ( length * Math.sin(ta + 3 * Math.PI / 2)  + width * Math.cos(ta + 3 * Math.PI / 2)); 
		
		
		
		return arr;
	}

	private double moveSpeed = 0.0;
	
	public double getMoveSpeed() {
		return moveSpeed;
	}
	
	public void setMoveSpeed(double moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	private double turnSpeed = 0.0;
	
	public double getTurnSpeed() {
		return turnSpeed;
	}
	
	public void setTurnSpeed(double turnSpeed) {
		this.turnSpeed = turnSpeed;
	}
	
	private TurningMode turningMode = TurningMode.None;
	
	public void setTurningMode(TurningMode mode) {
		turningMode = mode;
	}
	
	private TurningMode getTurningMode() {
		return turningMode;
	}
	
	private double rotationTurned = 0.0;
	private double tRotation = 0.0;
	
	public double getTRotation() {
		return tRotation;
	}

	public double getRotationTurned() {
		double temp = rotationTurned;
		rotationTurned = 0.0;
		return temp;
	}

	public double getRotationTurnedRadian() {
		return rotationTurned * Math.PI / 180;
	}
	
	private double angle = 0;
	
	public void turn() {
		int sign = 0;
		if (getTurningMode() == TurningMode.Right) {
			sign = 1;
		} else if (getTurningMode() == TurningMode.Left){
			sign = -1;
		}
		rotationTurned += getTurnSpeed() * sign;
		tRotation += getTurnSpeed() * sign;
		if (tRotation < 0)
			tRotation += 360;
		if (tRotation > 360) 
			tRotation -= 360;
		angle += getTurnSpeed() * sign * Math.PI / 180;
	}
	
	public void turnLeft(double angle) {
		Command cmd = new Command((int)(angle / getTurnSpeed()), CommandType.LEFT);
		System.out.println("d "  + (int)(angle / getTurnSpeed()));
		addCmd(cmd);
	}
	
	public void turnRight(double angle) {
		Command cmd = new Command((int)(angle / getTurnSpeed()), CommandType.RIGHT);
		addCmd(cmd);
	}
	
	private int ticksTurning;
	
	private int getTicksTurning() {
		return ticksTurning;
	}

	protected void setTicksTurning(int ticksTurning) {
		this.ticksTurning = ticksTurning;
	}
	
	private void checkTicksTurning() {
		if (getTicksTurning() == 1) {
			//resetCurrCmd();
			stopMoving();
		}
	}
	
	private void reduceTicksTurning() {
		if (getTicksTurning() >= 1)
			setTicksTurning(getTicksTurning() - 1);
	}
	
	public void startTurningLeft() {
		Command cmd = new Command(CommandType.STARTLEFT);
		addCmd(cmd);
	}
	
	public void startTurningRight() {
		Command cmd = new Command(CommandType.STARTRIGHT);
		addCmd(cmd);
	}
	
	private MovingMode movingMode = MovingMode.None;
	
	public void setMovingMode(MovingMode mode) {
		movingMode = mode;
	}
	
	private MovingMode getMovingMode() {
		return movingMode;
	}

	@Override
	public void moveForward(double distance) {
		if (distance < 0) throw new RuntimeException();
		distance = distance / 10;
		Command cmd = new Command((int)Math.round(distance / getMoveSpeed()), CommandType.FORWARD);
		addCmd(cmd);

	}
	
	@Override
	public void moveBackward(double distance) {
		if (distance < 0) throw new RuntimeException();
		distance = distance / 10;
		Command cmd = new Command((int)Math.round(distance / getMoveSpeed()), CommandType.BACKWARD);
		addCmd(cmd);
	}
	
	private int ticksMoving;
	
	private int getTicksMoving() {
		return ticksMoving;
	}

	protected void setTicksMoving(int ticksMoving) {
		this.ticksMoving = ticksMoving;
	}
	
	private void checkTicksMoving() {
		if (getTicksMoving() == 1) {
			stopMoving();
		}
	}
	
	private void reduceTicksMoving() {
		if (getTicksMoving() >= 1)
			setTicksMoving(getTicksMoving() - 1);
	}

	public void startMovingForward() {
		Command cmd = new Command(CommandType.STARTFORWARD);
		addCmd(cmd);
	}
	
	public void startMovingBackward() {
		Command cmd = new Command(CommandType.STARTBACKWARD);
		addCmd(cmd);
	}
	
	public void stopMoving() {
		resetCurrCmd();
		setMovingMode(MovingMode.None);
		setTurningMode(TurningMode.None);
	}
	
	private boolean stopOnWhite = false;
	
	@Override
	public void orientOnWhiteLine(boolean b) {
		moveForward(170);
	}
	
	public void orientOnWhiteLineExec(boolean b) {
		stopOnWhite = true;
		startMovingForward();
	}

	public boolean isMoving() {
		return !(movingMode == MovingMode.None && turningMode == TurningMode.None);
	}

	private double distanceMoved = 0.0;

	public double getDistanceMoved() {
		double temp = distanceMoved;
		distanceMoved = 0.0;
		return temp;
	}
	
	private double tdistancex = 0;
	public double getTDistanceX() {
		return tdistancex;
	}

	private double tdistancey = 0;
	
	public double getTDistanceY() {
		return tdistancey;
	}

	public void move() {
		int sign = 0;
		if (getMovingMode() == MovingMode.Forward) {
			sign = 1;
		} else if (getMovingMode() == MovingMode.Backward){
			sign = -1;
		}
		double newx = tdistancex + getMoveSpeed() * sign * Math.sin(angle);
		double newy = tdistancey + getMoveSpeed() * sign * Math.cos(angle);
		if (newx != 0.0 || newy != 0.0)
			//System.out.println("newx " + newx + " newy " + newy);
		if (!checkCollision(newx, newy)) {
			distanceMoved += getMoveSpeed() * sign;
			tdistancex = newx;
			tdistancey = newy;
		} else {
			//System.out.println("wall");
		}
	}
	
	private boolean checkCollision(double x, double y) {
		try {
			return maze.collidesWithBorder(x, y);
		} catch (IllegalArgumentException e){
			return false;
		}
	}
	
	public void tick() {
		move();
		turn();
		checkTicksMoving();
		reduceTicksMoving();
		checkTicksTurning();
		reduceTicksTurning();
		if (maze.isOnWhiteBorder(tdistancex, tdistancey) && stopOnWhite) {
			stopOnWhite = false;
			stopMoving();
			
		}
		if (!cmdQueue.isEmpty() && currCmd == null) {
			currCmd = cmdQueue.poll();
			currCmd.execute(this);
		}
		updatePassing();
		//System.out.println(getDistanceXMoved() + "  " + getDistanceYMoved() + " " + getRotation());
		//System.out.println(distanceXMoved + "  " + distanceYMoved + " " + rotation);
	}
	
	private Queue<Command> cmdQueue = new LinkedList<Command>();
	
	private Command currCmd = null;
	
	private void addCmd(Command cmd) {
		if (cmd == null)
			throw new IllegalArgumentException();
		cmdQueue.add(cmd);
	}
	
	public void resetCurrCmd() {
		currCmd = null;
	}

	@Override
	public void initialize() {
		timer.start();
		setupField();
		Thread lsThr = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
		    		LightSensorUpdate update = LightSensor.getInstance().lightSensorVigilanteLoop();
		    		SensorBuffer.addLightUpdate(update.ordinal());
				}
				
			}
		});
		lsThr.start();
		/*Thread lsThrVal = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
		    		LightSensor.getInstance().readValue();
		    		try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		lsThrVal.start();*/
	}

	@Override
	public void terminate() {
		timer.stop();
		
	}
	
	private Field maze;
	
	public Field getMaze() {
		return maze;
	}

	private void setupField() {
		if (maze == null) {

			try {
				maze = FieldFactory.fieldFromFile("C:\\demo2.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*maze = new Field();
			maze.addTileWithBorders(new Tile(0, 0), true, false, true, true);
			maze.addTileWithBorders(new Tile(1, 0), false, false, true, false);
			maze.addTileWithBorders(new Tile(0, 1), true, false, true, true);
			maze.addTileWithBorders(new Tile(1, 1), true, true, false, false);
			maze.addTileWithBorders(new Tile(2, 0), true, false, true, false);
			maze.addTileWithBorders(new Tile(3, 0), true, false, true, false);
			maze.addTileWithBorders(new Tile(4, 0), true, false, true, false);
			maze.addTileWithBorders(new Tile(5, 0), true, false, true, false);
			maze.addTileWithBorders(new Tile(6, 0), true, false, true, false);
			maze.addTileWithBorders(new Tile(7, 0), true, false, true, false);
			maze.addTileWithBorders(new Tile(8, 0), true, false, true, false);
			maze.addTileWithBorders(new Tile(9, 0), true, true, true, false);*/
			/*maze.addTile(new Tile(0, 0));
			maze.addTile(new Tile(0, 1));
			maze.addTile(new Tile(1, 0));
			maze.addTile(new Tile(1, 1));
			maze.addTile(new Tile(2, 0));
			maze.addBorder(new PanelBorder(0, 0, 0, 1));
			maze.addBorder(new WhiteBorder(0, 0, 1, 0));
			maze.addBorder(new WhiteBorder(1, 0, 1, 1));
			maze.addBorder(new WhiteBorder(0, 1, 1, 1));
			maze.addBorder(new PanelBorder(0, 0, 0, -1));
			maze.addBorder(new PanelBorder(0, 0, -1, 0));
			maze.addBorder(new PanelBorder(1, 0, 1, -1));
			maze.addBorder(new WhiteBorder(1, 0, 2, 0));
			maze.addBorder(new PanelBorder(0, 1, 0, 2));
			maze.addBorder(new PanelBorder(0, 1, -1, 1));
			maze.addBorder(new PanelBorder(1, 1, 1, 2));
			maze.addBorder(new PanelBorder(1, 1, 2, 1));
			maze.addBorder(new PanelBorder(2, 0, 2, 1));
			maze.addBorder(new PanelBorder(2, 0, 2, -1));
			maze.addBorder(new PanelBorder(2, 0, 3, 0));*/
		}
	}
	
	public Field getField() {
		return maze;
	}
	
	public void SetupFieldFile(String filename) {
		try {
			maze = FieldFactory.fieldFromFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Border> getBorderSurroundings() {
		List<Border> list = new ArrayList<Border>();
		Tile tile = maze.getCurrentTile(tdistancex, tdistancey);
		list.add(maze.getBottomBorderOfTile(tile));
		list.add(maze.getTopBorderOfTile(tile));
		list.add(maze.getLeftBorderOfTile(tile));
		list.add(maze.getRightBorderOfTile(tile));
		return list;
	}
	
	boolean passedWhiteBorder = false;
	
	public boolean passedWhiteBorder() {
		if (passedWhiteBorder) {
			passedWhiteBorder = false;
			return true;
		}
		return false;
	}
	
	boolean wasWhite = false;
	
	public void updatePassing() {
		if (maze.isOnWhiteBorder(tdistancex, tdistancey)) {
			wasWhite = true;
		} else {
			if (wasWhite) {
				passedWhiteBorder = true;
				wasWhite = false;
			}
		}
	}

	public void zeroPos() {
		tdistancex = 0;
		tdistancey = 0;
		tRotation = 0;
		rotationTurned = 0;
		distanceMoved = 0;
		setMovingMode(MovingMode.None);
		setTurningMode(TurningMode.None);
		ticksMoving = 0;
		ticksTurning = 0;
		maze = null;
		setupField();
		SensorBuffer.getDistances().clear();
	}
	
	@Override
	public void scanSonar() {
		UltrasonicSensor.getInstance().scanForWalls();		
	}
	
	@Override
	public void setOnCenterTile(Robot robot){
		// TODO Auto-generated method stub
	}

	@Override
	public void newTileScan() {
		UltrasonicSensor.getInstance().newTileScan();
	}

	@Override
	public boolean isStartTile() {
		return false;
	}

	@Override
	public boolean isFinishTile() {
		return false;
	}

	@Override
	public boolean isScanning() {
		return false;
	}

	@Override
	public void scanOnlyLines(boolean flag) {
		
	}

	@Override
	public void checkScan() {
		UltrasonicSensor.getInstance().checkScan();
	}	
}
