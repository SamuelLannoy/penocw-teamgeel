package robot;

import java.io.IOException;
import java.util.List;

import communication.SeesawStatus;

import peno.htttp.PlayerClient;

import robot.brain.Explorer;
import robot.brain.Pathfinder;
import simulator.ISimulator;
import simulator.VirtualRobotConnector;

import exception.CommunicationException;
import field.Barcode;
import field.Border;
import field.Direction;
import field.Field;
import field.PanelBorder;
import field.Tile;
import field.TilePosition;
import field.UnsureBorder;
import field.WhiteBorder;
import field.fieldmerge.FieldConverter;
import field.fieldmerge.FieldMerger;
import field.fieldmerge.FieldMessage;
import field.fromfile.FieldFactory;
import field.representation.FieldRepresentation;

/**
 * @author  Samuel
 */
public class Robot extends RobotModel{
	
	private AbstractRobotConnector robotConn;
	//private Position position = new Position();
	private FieldRepresentation field = new FieldRepresentation();
	private boolean isBusy = false;
	private Tile startTile;
	private Tile endTile;
	private PlayerClient client;
	private RobotPool robotPool;

	public Robot(int connectionType) {
		robotConn = ConnectionFactory.getConnection(connectionType);
	}
	
	public void setClient(PlayerClient client) {
		this.client = client;
	}
	
	public PlayerClient getClient() {
		return client;
	}
	
	public void setRobotPool(RobotPool robotPool) {
		this.robotPool = robotPool;
		if (isSim()) {
			((ISimulator)robotConn).setRobotPool(robotPool);
		}
	}
	
	public void drivePolygon(double length, int corners) throws IllegalArgumentException {
		if(corners==0 || length == 0){
			throw new IllegalArgumentException();
		}
		else{
			setBusy(true);
			double angle = 360/corners;
			for (int i = 0; i < corners; i++) {
				moveForward(length);
				turnRight(angle);
			}
			setBusy(false);
		}
	}

	public boolean isBusy() {
		return isBusy;
	}

	protected void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
	
	public Tile getStartTile() {
		return startTile;
	}

	private void setStartTile(Tile startTile) {
		this.startTile = startTile;
	}
	
	public Tile getFinishTile() {
		return endTile;
	}

	private void setFinishTile(Tile endTile) {
		this.endTile = endTile;
	}

	public void initialize() throws CommunicationException, IOException {
		robotConn.initialize();
		
		/*setField(FieldFactory.fieldFromFile("c:\\demo2.txt"));
		currTile = getField().getTileMap().getObjectAtId(new field.Position(0, 0));*/
		
		/*Field mazex = FieldFactory.fieldFromFile("c:\\merge1.txt");
		Field mazey = FieldFactory.fieldFromFile("c:\\merge2.txt");
		
		Field merged = FieldMerger.mergeFields(mazex, mazey);
		
		setField(merged);
		currTile = getField().getTileMap().getObjectAtId(new field.Position(0, 0));*/
		
		
		getField().initialize();
		setCurrTile(getField().getTileAt(TilePosition.POSITION_ZERO));
	}
	
	public void terminate() {
		robotConn.terminate();
	}
	
	public void startMovingForward() {
		robotConn.startMovingForward();
	}
	
	public void startMovingBackward() {
		robotConn.startMovingBackward();
	}
	
	public void stopMoving() {
		robotConn.stopMoving();
		updatePosition();
	}
	
	public void moveForward(double distance) {
		robotConn.moveForward(distance);
	}
	
	public void moveBackward(double distance) {
		robotConn.moveBackward(distance);
	}

	public double getDistanceMoved() {
		return robotConn.getDistanceMoved();
	}
	
	public void startTurningLeft() {
		robotConn.startTurningLeft();
	}
	
	public void startTurningRight() {
		robotConn.startTurningRight();
	}
	
	public void turnLeft(double angle) {
		robotConn.turnLeft(angle);
	}
	
	public void turnRight(double angle) {
		robotConn.turnRight(angle);
	}
	
	public double getRotationTurned() {
		return robotConn.getRotationTurned();
	}
	
	public void setMoveSpeed(double moveSpeed) {
		robotConn.setMoveSpeed(moveSpeed);
	}
	
	public void setTurnSpeed(double turnSpeed) {
		robotConn.setTurnSpeed(turnSpeed);
	}

	
	public void orientOnWhiteLine(boolean b) {
		robotConn.orientOnWhiteLine(b);
	}
	
	public void scanSonar(){
		robotConn.scanSonar();
	}
	
	public void setOnCenterTile(){
		robotConn.setOnCenterTile(this);
		//test();
	}
	
	public void newTileScan() {
		robotConn.newTileScan();
	}
	
	public void checkScan() {
		robotConn.checkScan();
	}
	
	public void explore() {
		Thread r = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Explorer.explore(Robot.this);
				
			}
		});
		r.start();
	}
	
	List<Tile> tilelistref = null;
	
	public List<Tile> getAStarTileList() {
		return tilelistref;
	}
	
	public void setAStartTileList(List<Tile> input) {
		tilelistref = input;
	}
	
	public void resetAStartTileList() {
		tilelistref = null;
	}
	
	public void pauseExplore() {
		Explorer.pause();
	}
	
	public void resumeExplore() {
		Explorer.resume();
	}
	
	public boolean isMoving() {
		return robotConn.isMoving();
	}
	
	/**
	 * @return if the robot is scanning a barcode
	 */
	public boolean isScanning() {
		return robotConn.isScanning();
	}
	
	public void turnToAngle(double angle) {
		if (angle < 0)
			angle += 360;
		double currAngle = getPosition().getRotation();
		if (currAngle < 0)
			currAngle += 360;
		double delta = angle - currAngle;
		if (Math.abs(delta) < 1)
			return;
		if (delta > 180)
			delta -= 360;
		if (delta < -180) 
			delta += 360;
		if (delta < 0) {
			turnLeft(-delta);
			DebugBuffer.addInfo("l " + delta);
			//System.out.println("l " + delta);
		} else {
			turnRight(delta);
			DebugBuffer.addInfo("r " + delta);
			//System.out.println("r " + delta);
		}
	}
	
	public void travelToNextTile(Tile tile) {
		if (tile.getPosition().manhattanDistance(getCurrTile().getPosition()) > 1)
			throw new IllegalArgumentException("tile is not next to current tile " + tile.getPosition());
		if (tile.getPosition().manhattanDistance(getCurrTile().getPosition()) == 0)
			return;
		int diffx = tile.getPosition().getX() - getCurrTile().getPosition().getX();
		int diffy = tile.getPosition().getY() - getCurrTile().getPosition().getY();
		if (diffx == 0 && diffy == 1) {
			turnToAngle(0);
			//turnLeft(getPosition().getRotation());
		} else if (diffx == 0 && diffy == -1) {
			turnToAngle(180);
			//turnRight(180-getPosition().getRotation());
		} else if (diffx == 1 && diffy == 0) {
			turnToAngle(90);
			//turnRight(90-getPosition().getRotation());
		} else if (diffx == -1 && diffy == 0) {
			turnToAngle(-90);
			//turnRight(270-getPosition().getRotation());
		}
		moveNext();
		//System.out.println("moveto " + tile.getPosition());
	}
	
	public void travelFromTileToTile(Tile start, Tile finish, Tile prev) {
		int diffx = finish.getPosition().getX() - start.getPosition().getX();
		int diffy = finish.getPosition().getY() - start.getPosition().getY();
		int diffxprev = start.getPosition().getX() - prev.getPosition().getX();
		int diffyprev = start.getPosition().getY() - prev.getPosition().getY();
		/*if (diffx + diffxprev == 2 || diffy + diffyprev == 2) {
			orientOnWhiteLine(false);
			moveForward(200);
			return;
		}*/
		Direction from = Direction.fromDiffPos(diffxprev, diffyprev);
		Direction to = Direction.fromDiffPos(diffx, diffy);
		
		int turn = from.turnTo(to);
		//DebugBuffer.addInfo("turn "+ turn);
		if (turn > 0)
			turnRight(turn);
		if (turn < 0)
			turnLeft(-turn);
		
		/*if (diffx == 0 && diffy == 1) {
			//turnToAngle(0);
			//turnLeft(getPosition().getRotation());
		} else if (diffx == 0 && diffy == -1) {
			//turnToAngle(180);
			//turnRight(180-getPosition().getRotation());
		} else if (diffx == 1 && diffy == 0) {
			//turnToAngle(90);
			//turnRight(90-getPosition().getRotation());
		} else if (diffx == -1 && diffy == 0) {
			//turnToAngle(-90);
			//turnRight(270-getPosition().getRotation());
		}*/

		if (Math.abs(turn) > 0 && getField().getTileMap().getObjectAtId(start.getPosition()).getBarcode() != null) {
			moveForward(60);
			DebugBuffer.addInfo("testcode");
		}
		moveNext();
		//DebugBuffer.addInfo("traveling from "+ start.getPosition() + " to " + finish.getPosition());
	}
	
	private int counter = 0;
	
	public void moveNext() {
		//if (counter == 0){
			orientOnWhiteLine(false);
			moveForward(230);
			counter++;
		/*} else {
			moveForward(430);
			counter = (counter + 1) % 2;
		}*/
	}
	
	public void travelListOfTiles(final List<Tile> list) {
		Thread r = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (Tile tile : list) {
					travelToNextTile(tile);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (Robot.this.isMoving()) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		r.start();
	}
	
	public void moveToTile(Tile tile) {
		if (tile == null) return;
		travelListOfTiles(Pathfinder.findShortestPath(this, tile));
	}
	
	private boolean isWall(int x) {
		return (x<40);
	}
	
	public void updatePosition() {		
		getPosition().updatePosition(robotConn.getDistanceMoved());
		getPosition().updateRotation(robotConn.getRotationTurned());
    	if (client != null && client.isPlaying()) { 
    		try {
    			client.updatePosition(
    					robotPool.getMainRobot().getCurrTile().getPosition().getX() * 40 +
    					robotPool.getMainRobot().getPosition().getPosX(),
    					robotPool.getMainRobot().getCurrTile().getPosition().getY() * 40 +
    					robotPool.getMainRobot().getPosition().getPosY(),
    					robotPool.getMainRobot().getPosition().getRotation());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		
		if (robotConn.hasBall() && !hasBall()) {
			setHasBall(true);
			if (client.isPlaying()) {
				try {
					client.foundObject();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Set whether the current tile is a start tile, a finish tile or an ordinary tile
		if(robotConn.isStartTile())
			addAction(0);
		else if(robotConn.isFinishTile())
			addAction(1);
		
		//System.out.println("x " + SensorBuffer.canClear());
		if (!SensorBuffer.canClear() && SensorBuffer.getDistances().size() >= 4) {
			//System.out.println("scan values given");
			findBorderObjects();
		}
		
		passedWhiteBorder();
		if (passedWhite && !isSeesawMode()/* || Math.abs(getPosition().getPosX()) > 30 || Math.abs(getPosition().getPosY()) > 30*/) {
			passedWhite = false;
			//System.out.println("LINE");
			Direction dir = Direction.fromPos(getPosition());
			field.TilePosition newPos = dir.getPositionInDirection(currTile.getPosition());
			//System.out.println("dit " + dir);

			getField().registerNewTile(getDirection(), getCurrTile().getPosition());
			currTile = getField().getTileAt(newPos);

			getPosition().resetPosition(dir);
		}
	}
	
	public void scanOnlyLines(boolean flag) {
		robotConn.scanOnlyLines(flag);
	}
	
	private boolean passedWhite = false;
	
	private boolean correctBarcode = false;
	
	public boolean hasCorrectBarcode() {
		if (correctBarcode) {
			correctBarcode = false;
			DebugBuffer.addInfo("has correct barcode!");
			return true;
		}
		return false;
	}
	
	private boolean wrongBarcode = false;
	
	public boolean hasWrongBarcode() {
		if (wrongBarcode) {
			wrongBarcode = false;
			DebugBuffer.addInfo("has wrong barcode!");
			return true;
		}
		return false;
	}
	
	public void passedWhiteBorder() {
		passedWhite = SensorBuffer.getLightUpdates().contains(0);
		correctBarcode = SensorBuffer.getLightUpdates().contains(1) || correctBarcode;
		wrongBarcode = SensorBuffer.getLightUpdates().contains(3) || wrongBarcode;
		if (SensorBuffer.getLightUpdates().size() > 0) {
			DebugBuffer.addInfo("lijst "+SensorBuffer.getLightUpdates());
			//DebugBuffer.addInfo("cb " + correctBarcode + " c1 " + SensorBuffer.getLightUpdates().contains(1));
			//DebugBuffer.addInfo("wb " + wrongBarcode + " c3 " + SensorBuffer.getLightUpdates().contains(3));
		}
		//System.out.println("cb " + correctBarcode);
		SensorBuffer.getLightUpdates().clear();
		/*boolean passedWhiteBorder = SensorBuffer.getLightUpdates().contains(0);
		SensorBuffer.getLightUpdates().remove((Integer)0);
		return passedWhiteBorder;*/
	}
	
	public void zeroPos() {
		getField().clearRepresentation();
		setCurrTile(getField().getTileAt(TilePosition.POSITION_ZERO));
		if (robotConn instanceof VirtualRobotConnector) {
			((VirtualRobotConnector)robotConn).zeroPos();
		}
		getPosition().zeroPos();
	}
	
	public void fieldFromFile(String filename) {
		((VirtualRobotConnector)robotConn).SetupFieldFile(filename);
		zeroPos();
	}
	
	public Direction getDirection() {
		return Direction.fromAngle(getPosition().getRotation());
	}
	
	/**
	 * @return
	 * @uml.property  name="position"
	 */
	public Position getPosition() {
		return position;
	}
	
	public FieldRepresentation getField() {
		return field;
	}
	public void setField(FieldRepresentation f) {
		field = f;
	}
	
	private void findBorderObjects(){
		// maakt nieuwe borders aan wanneer ontdekt.
		// aanroepen na scan met ultrasone sensor.
		// klopt niet altijd wanneer scheef => telkens witte lijn => rechtzetten.
		// probeer ook midden van tegels te rijden.
		// rotatie is hier 0 als naar boven gericht.
		List<Integer> distances = SensorBuffer.getDistances();
		if (distances.size() >= 4) {
			for (int i = 0; i < 4; i++){
				Direction dir = getDirection();
				if (i == 1)
					dir = dir.left();
				if (i == 2)
					dir = dir.opposite();
				if (i == 3)
					dir = dir.right();
				int distance = SensorBuffer.getDistances().get(distances.size() - 4 + i);
				if (distance != -1) {
					if (distance < 25){
						getField().registerBorder(getCurrTile().getPosition(),
								dir, PanelBorder.class);
					} else if (distance >= 25 && distance < 45){
						getField().registerBorder(getCurrTile().getPosition(),
								dir, UnsureBorder.class);
					} else {
						getField().registerNewTile(dir, getCurrTile().getPosition());
					}
				}
			}
			SensorBuffer.getDistances().clear();
			SensorBuffer.setClear(false);
		}
	}
	
	public void waitTillRobotStops() {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (isMoving()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isSim() {
		return robotConn instanceof ISimulator;
	}
	
	public double getSimX() {
		return ((ISimulator)robotConn).getTDistanceX();
	}
	
	public double getSimY() {
		return ((ISimulator)robotConn).getTDistanceY();
	}
	
	public double getStartx() {
		return ((ISimulator)robotConn).getStartx();
	}
	
	public double getStarty() {
		return ((ISimulator)robotConn).getStarty();
	}
	
	public double getSimAngle() {
		return ((ISimulator)robotConn).getTRotation() * Math.PI / 180;
	}
	
	public double getSimAngleGr() {
		return ((ISimulator)robotConn).getTRotation();
	}
	
	public void setSimLoc(double x, double y, double angle) {
		((ISimulator)robotConn).setSimLoc(x, y, angle);
	}
	

	
	public void addAction(int action) {
		System.out.println("action: " + action);
		switch(action) {
			case 0: 
				setStartTile(currTile);
				DebugBuffer.addInfo("Start tile set: " + currTile.getPosition());
				break;
			case 1:
				setFinishTile(currTile);
				DebugBuffer.addInfo("Finish tile set: " + currTile.getPosition());
				break;
		}
		
	}
	
	public void setObjectNr(int nr){
		robotConn.setObjectNr(nr);
	}
	
	public int getTeamNr() {
		return robotConn.getTeam();
	}
	
	public boolean hasTeamMate() {
		return getTeamMateNr() != null && !getTeamMateNr().equals("");
	}
	
	private FieldRepresentation teamMateField = new FieldRepresentation();
	
	public boolean hasTeamMateField() {
		return teamMateField != null;
	}
	
	public FieldRepresentation getTeamMateField() {
		return teamMateField;
	}
	
	private Robot teamMate;
	
	public Robot getTeamMate() {
		return teamMate;
	}

	public void setTeamMate(Robot teamMate) {
		this.teamMate = teamMate;
	}
	
	private String teamMateID;

	public String getTeamMateNr() {
		return teamMateID;
	}

	public void setTeamMateID(String teamMateID) {
		this.teamMateID = teamMateID;
		Robot tm = new Robot(1);
		setTeamMate(tm);
		tm.setField(teamMateField);
		//tm.setCurrTile(teamMateField.getTileMap().getObjectAtId(new field.Position(0, 0)));
	}
	
	private boolean receivedTeamTiles;

	public boolean receivedTeamTiles() {
		return receivedTeamTiles;
	}

	public void setReceivedTeamTiles(boolean receivedTeamTiles) {
		this.receivedTeamTiles = receivedTeamTiles;
		getTeamMate().setPosition(new Position(0, 0, 0), new Tile(new field.TilePosition(0,0)));
	}

	private boolean hasFoundOwnBarcode;

	public boolean hasFoundOwnBarcode() {
		return hasFoundOwnBarcode;
	}

	public void setHasFoundOwnBarcode(boolean hasFoundOwnBarcode) {
		this.hasFoundOwnBarcode = hasFoundOwnBarcode;
	}
	
	public SeesawStatus getSeesawStatus() {
		return robotConn.getSeesawStatus();
	}
	
	private Position startPos;

	public Position getStartPos() {
		return startPos;
	}

	public void setStartPos(Position startPos) {
		this.startPos = startPos;
	}
	
	private boolean seesawMode = false;

	public boolean isSeesawMode() {
		return seesawMode;
	}

	public void setSeesawMode(boolean seesawMode) {
		this.seesawMode = seesawMode;
	}
	
	public void pauseLightSensor() {
		robotConn.pauseLightSensor();
	}
	
	public void resumeLightSensor() {
		robotConn.resumeLightSensor();
	}
	
	public void moveAcrossSeesaw() {
		moveForward(800);
		Explorer.waitTillRobotStops(this, 2500);
		moveForward(400);
		Explorer.waitTillRobotStops(this, 400);
		this.orientOnWhiteLine(false);
		Explorer.waitTillRobotStops(this, 400);
		moveForward(190);
		Explorer.waitTillRobotStops(this, 400);
//		moveForward(400);
//		try {
//			Thread.sleep(250);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		moveForward(200);
//		try {
//			Thread.sleep(250);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		moveForward(200);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		moveForward(800);
	}

	public TilePosition getBarcodePositionAfterSeesaw(Tile currTile) {
		Direction dirForw = getDirection();
		TilePosition afterWipPos = dirForw.getPositionInDirection(currTile.getPosition());
		afterWipPos = dirForw.getPositionInDirection(afterWipPos);
		afterWipPos = dirForw.getPositionInDirection(afterWipPos);
		return afterWipPos;
	}
	
	public TilePosition getTilePositionAfterSeesaw(Tile currTile) {
		Direction dirForw = getDirection();
		TilePosition afterWipPos = dirForw.getPositionInDirection(getBarcodePositionAfterSeesaw(currTile));
		return afterWipPos;
	}

	public int getObjectNr() {
		return robotConn.getObjectNr();
	}
	
}
