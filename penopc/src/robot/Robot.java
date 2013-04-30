package robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.border.Border;

import com.sun.java.util.jar.pack.Package.Class.Field;


import communication.CommandEncoder;
import communication.SeesawStatus;
import communication.Status;

import robot.brain.EndingCondition;
import robot.brain.Explorer;
import robot.brain.Pathfinder;
import simulator.ISimulator;
import simulator.VirtualRobotConnector;
import simulator.lightsensor.LightSensor;
import team.communication.PenoHtttpTeamCommunicator;
import team.communication.TeamCommunicator;

import exception.CommunicationException;
import field.Barcode;
import field.Direction;
import field.PanelBorder;
import field.SolidBorder;
import field.Tile;
import field.TilePosition;
import field.UnsureBorder;
import field.WhiteBorder;
import field.representation.FieldRepresentation;
import field.simulation.FieldSimulation;

/**
 * @author  Samuel
 */
public class Robot extends RobotModel{
	
	private AbstractRobotConnector robotConn;
	//private Position position = new Position();
	private FieldRepresentation fieldRepresentation = new FieldRepresentation();
	private FieldSimulation fieldSimulation;
	private boolean isBusy = false;
	private Tile startTile;
	private Tile endTile;
	private TeamCommunicator comm;

	public Robot(int connectionType) {
		robotConn = ConnectionFactory.getConnection(connectionType);
		comm = new PenoHtttpTeamCommunicator();
	}
	
	public void connectToGame(String playerId, String gameId) {
		try {
			comm.connect(this, gameId, playerId);
			comm.joinGame();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setReady() {
		comm.setReady(true);
	}
	
	/*public void setClient(PlayerClient client) {
		comm = new PenoHtttpTeamCommunicator(client);
	}*/
	
	public void setRobotPool(RobotPool robotPool) {
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
		setCurrentAction("Idle");
		
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
		if (!isSim() && getCurrTile().hasBarcocde() && angle >= 90) {
			CommandEncoder.getInstance().turnOnBarcode();
		} else {
			robotConn.turnLeft(angle);
		}
	}
	
	public void turnRight(double angle) {
		if (!isSim() && getCurrTile().hasBarcocde() && angle >= 90) {
			CommandEncoder.getInstance().turnOnBarcode();
		} else {
			robotConn.turnRight(angle);
		}
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
	
	public void ultimateCenter(){
		robotConn.ultimateCenter();
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
		/*pauseLightSensor();
		setCurrentAction("centering after seesaw");
		CommandEncoder.getInstance().setOnCenterTileAfterSeesaw(getLeftFlag());
		waitTillStandby(1000);
		while(Status.isCentering());
		setCurrentAction("done!");
		resumeLightSensor();*/
		Thread r = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Explorer.exploreTillObjectFound(Robot.this);
				teamComm();
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
			//DebugBuffer.addInfo("l " + delta);
			//System.out.println("l " + delta);
		} else {
			turnRight(delta);
			//DebugBuffer.addInfo("r " + delta);
			//System.out.println("r " + delta);
		}
	}
	
	public void travelToNextTile(Tile tile) {
		if (tile.getPosition().manhattanDistance(getCurrTile().getPosition()) > 1)
			throw new IllegalArgumentException("tile is not next to current tile " + tile.getPosition());
		sendPosition();
		if (tile.getPosition().manhattanDistance(getCurrTile().getPosition()) == 0)
			return;
		//turnToTile(tile.getPosition());
		moveNext();
		//System.out.println("moveto " + tile.getPosition());
	}
	
	private void turnToTile(TilePosition tilePos) {
		int diffx = tilePos.getX() - getCurrTile().getPosition().getX();
		int diffy = tilePos.getY() - getCurrTile().getPosition().getY();
		if (diffx == 0 && diffy == 1) {
			turnToAngle(0);
			System.out.println("angle " + 0);
			//turnLeft(getPosition().getRotation());
		} else if (diffx == 0 && diffy == -1) {
			turnToAngle(180);
			System.out.println("angle " + 180);
			//turnRight(180-getPosition().getRotation());
		} else if (diffx == 1 && diffy == 0) {
			turnToAngle(90);
			System.out.println("angle " + 90);
			//turnRight(90-getPosition().getRotation());
		} else if (diffx == -1 && diffy == 0) {
			turnToAngle(-90);
			System.out.println("angle " + -90);
			//turnRight(270-getPosition().getRotation());
		}
	}
	
	public void travelFromTileToTile(Tile start, Tile finish, Tile prev) {
		if(incr%3==1){
			Direction cur = getDirection();
			Direction left;
			Direction right;
			if (cur==Direction.BOTTOM){
				left = Direction.RIGHT;
				right = Direction.LEFT;
			} else if (cur==Direction.LEFT){
				left = Direction.BOTTOM;
				right = Direction.TOP;
			} if (cur==Direction.RIGHT){
				left = Direction.TOP;
				right = Direction.BOTTOM;
			} if (cur==Direction.TOP){
				left = Direction.LEFT;
				right = Direction.RIGHT;
			} 
			field.Border leftB = getField().getBorderInDirection(getCurrTile(), left);
			field.Border rightB  = getField().getBorderInDirection(getCurrTile(), right);
			ultimateCenter(true);
			if(leftB instanceof SolidBorder){
				turnLeft(90);
				moveForward(300);
				moveBackward(120);
				turnRight(90);
			} else if(rightB instanceof SolidBorder){
				turnRight(90);
				moveForward(300);
				moveBackward(120);
				turnLeft(90);
			}
			ultimatecenter(false);
			
		}
		
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
		Robot.turned = turn !=0;
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

		if (Math.abs(turn) > 0 && getField().getTileAt(start.getPosition()).getBarcode() != null) {
			moveForward(60);
			//DebugBuffer.addInfo("testcode");
		}
		moveNext();
		//DebugBuffer.addInfo("traveling from "+ start.getPosition() + " to " + finish.getPosition());
	}
	
	private int incr = 0;
	public static boolean turned = false;
	
	public void moveNext() {
		//if (counter == 0){
//			orientOnWhiteLine(false);
//			moveForward(230);
			if(incr%3 == 0){
				orientOnWhiteLine(false);
				moveForward(230);
				ultimateCenter();
				incr++;
			} 
//			else if(get){
//				
//			}
			else{
				moveForward(400);
				incr++;
			}
			
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
	
	
	public void updatePosition() {		
		getPosition().updatePosition(robotConn.getDistanceMoved());
		getPosition().updateRotation(robotConn.getRotationTurned());
		
		if (robotConn.hasBall() && !hasBall()) {
			setHasBall(true);
		}
		
		if (!SensorBuffer.canClear() && SensorBuffer.getDistances().size() >= 4) {
			findBorderObjects();
		}
		
		passedWhiteBorder();
		if (passedWhite/* || Math.abs(getPosition().getPosX()) > 30 || Math.abs(getPosition().getPosY()) > 30*/) {
			passedWhite = false;
			Direction dir = Direction.fromPos(getPosition());
			field.TilePosition newPos = dir.getPositionInDirection(currTile.getPosition());

			getField().registerNewTile(getDirection(), getCurrTile().getPosition());
			currTile = getField().getTileAt(newPos);

			getPosition().resetPosition(dir);
			sendPosition();
		}
	}
	
	public void sendPosition() {
		if (comm != null) { 
			comm.updatePosition(
					getCurrTile().getPosition().getX(),
					getCurrTile().getPosition().getY(),
					(-getPosition().getRotation()) + 90);
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
			//DebugBuffer.addInfo("has correct barcode!");
			return true;
		}
		return false;
	}
	
	private boolean wrongBarcode = false;
	
	public boolean hasWrongBarcode() {
		if (wrongBarcode) {
			wrongBarcode = false;
			//DebugBuffer.addInfo("has wrong barcode!");
			return true;
		}
		return false;
	}
	
	public void passedWhiteBorder() {
		passedWhite = SensorBuffer.getLightUpdates().contains(0);
		correctBarcode = SensorBuffer.getLightUpdates().contains(1) || correctBarcode;
		wrongBarcode = SensorBuffer.getLightUpdates().contains(3) || wrongBarcode;
		if (SensorBuffer.getLightUpdates().size() > 0) {
			//DebugBuffer.addInfo("lijst "+SensorBuffer.getLightUpdates());
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
		return fieldRepresentation;
	}
	public void setField(FieldRepresentation f) {
		fieldRepresentation = f;
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
				boolean interference = false;
				if (i == 1) { // left
					dir = dir.left();
					interference = fieldSimulation.isRobotLeft();
				} else if (i == 2) { // back
					dir = dir.opposite();
				} else if (i == 3) { // right
					dir = dir.right();
					interference = fieldSimulation.isRobotRight();
				} else { // front
					interference = fieldSimulation.isRobotInFront();
				}
				int distance = SensorBuffer.getDistances().get(distances.size() - 4 + i);
				if (distance != -1) {
					if (distance < 21 && !interference){
						getField().registerBorder(getCurrTile().getPosition(),
								dir, PanelBorder.class);
					} else if (distance >= 21 && distance < 45 && !interference){
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
		return getTeamMateID() != null && !getTeamMateID().equals("");
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

	public String getTeamMateID() {
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
	
	public void pauseLightSensor() {
		System.out.println("pause lightsensor");
		robotConn.pauseLightSensor();
	}
	
	public void resumeLightSensor() {
		robotConn.resumeLightSensor();
	}
	
	private boolean getLeftFlag() {
		switch (getDirection()) {
			case TOP:
				return getPosition().getPosX() < 0;
			case BOTTOM:
				return getPosition().getPosX() > 0;
			case LEFT:
				return getPosition().getPosY() < 0;
			case RIGHT:
				return getPosition().getPosY() > 0;
			default:
				throw new IllegalStateException();
		}
	}
	
	public void moveAcrossSeesawPhysical() {
		if (!isSim()) {
			pauseLightSensor();
			moveForward(800);
			waitTillStandby(2500);
			moveForward(400);
			waitTillStandby(380);
			//this.orientOnWhiteLine(false);
			
			// flush barcode values before moving
			hasCorrectBarcode();
			hasWrongBarcode();
			
			//waitTillStandby(400);
			//moveForward(190);
			waitTillStandby(400);
			setCurrentAction("centering on tile after seesaw");
			CommandEncoder.getInstance().setOnCenterTileAfterSeesaw(getLeftFlag());
			waitTillStandby(4000);
			while(Status.isCentering()){
				System.out.println("is centering");
			}
			System.out.println("is no longer centering");
			resumeLightSensor();
			waitTillStandby(4000);
			System.out.println("lightsensor resumed");
		} else {
			moveForward(1200);
			waitTillStandby(400);
			// flush barcode values before moving
			hasCorrectBarcode();
			hasWrongBarcode();
			moveForward(400);
			waitTillStandby(400);
		}
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
	
	public void setSimField(FieldSimulation world) {
		if (isSim()) {
			((ISimulator)robotConn).setSimField(world);
		} else {
			SimulatedPhysicalRobot spr = new SimulatedPhysicalRobot(this);
			spr.setSimField(world);
		}
		setFieldSimulation(world);
	}
	
	public void goToTeamMate() {
		// TODO never go to teammate on seesaw?
		Collection<Integer> ignoredSeesaws = new ArrayList<Integer>(6);
		boolean reachedDestination = false;
		// redo this till we have found our destination
		while (!reachedDestination) {
			setCurrentAction("Moving to teammate at " + getTeamMate().getCurrTile().getPosition());
			decreaseSpottedRobotTiles();
			reachedDestination = goToTileLoop(getTeamMate().getCurrTile().getPosition(), ignoredSeesaws);
			// TODO check win
		}
	}
	
	public void goToTile(TilePosition tilePos) {
		Collection<Integer> ignoredSeesaws = new ArrayList<Integer>(6);
		boolean reachedDestination = false;
		// redo this till we have found our destination
		while (!reachedDestination) {
			decreaseSpottedRobotTiles();
			tilePos = Explorer.recalcExplore(this, tilePos, ignoredSeesaws);
			setCurrentAction("Moving to tile " + tilePos);
			reachedDestination = goToTileLoop(tilePos, ignoredSeesaws);
		}
	}
	
	private boolean goToTileLoop(TilePosition tilePos, Collection<Integer> ignoredSeesaws) {
		// can we find a path to the tile ?
		try {
			// yes we can
			List<Tile> tileList = Pathfinder.findShortestPath(this, getField().getTileAt(tilePos), ignoredSeesaws, getRobotSpottedTiles());
			// update path list for gui
			setAStartTileList(tileList);
			
			if (tileList.size() == 1) { // this means we arrived
				return true;
			}
			
			// check if there is a robot in front of us
			turnToTile(tileList.get(1).getPosition());
			waitTillStandby(1000);
			boolean safe = checkIfSafe();
			
			if (safe) {
				// before moving flush barcode values
				hasWrongBarcode();
				hasCorrectBarcode();
				

				if (getField().pathRunsThroughSeesaw(getAStarTileList()) &&
						getField().isExplored(getCurrTile().getPosition()) &&
						(getCurrTile().hasBarcocde() &&
								getCurrTile().getBarcode().isSeesaw())) {
					// travel across seesaw if necessary
					// boolean for A*
					boolean ignore = seesawAction();
					// don't cross seesaw in A* if we couldn't cross it
					if (ignore) {
						ignoredSeesaws.add(getCurrTile().getBarcode().getDecimal());
					}
				} else {
					// travel to second tile, because first one is always our own tile
					//DebugBuffer.addInfo("moving to " + tileList.get(1).getPosition());
					travelToNextTile(tileList.get(1));
					waitTillStandby(750);
				}

				if (getField().isExplored(getCurrTile().getPosition())){
					// is the tile I moved on a seesaw barcode tile?
					if (getCurrTile().hasBarcocde() && getCurrTile().getBarcode().isSeesaw()) {
						// boolean for A*
						boolean ignore = seesawAction();
						// don't cross seesaw in A* if we couldn't cross it
						if (ignore) {
							ignoredSeesaws.add(getCurrTile().getBarcode().getDecimal());
						}
					}
				}
			} else {
				DebugBuffer.addInfo("Robot spotted at " + tileList.get(1).getPosition());
				robotSpottedTiles.put(tileList.get(1).getPosition(), 3);
				return false;
			}
			
		} catch (IllegalArgumentException e) {
			// no we can't
			e.printStackTrace();
		}
		return false;
	}
	
	// Used for robot detection
	private boolean checkIfSafe() {
		return true;
		//return getFieldSimulation().checkIfSafe();
	}
	
	/**
	 * 
	 * @return returns tilepositions that need to be explored
	 */
	public Collection<TilePosition> exploreTile() {
		Collection<TilePosition> toExplore = new ArrayList<TilePosition>(4);
		// ask if barcode was read
		boolean correct = hasCorrectBarcode();
		boolean wrong = hasWrongBarcode();

		// barcode has been detected
		if (correct || wrong || isScanning()) {
			getField().registerBarcode(getCurrTile().getPosition(), getDirection());
			
			// is not scanning anymore
			if (!isScanning()) {
				if (correct) {
					// has received correct => wait till it appears on tile
					while (getCurrTile().getBarcode() == null);
				} else {
					// if wrong barcode wait 5s
					waitTillStandby(5000);
				}
			} else { // still scanning
				DebugBuffer.addInfo("still scanning!");
				waitTillStandby(5000);

				// reupdate correct / wrong
				correct = hasCorrectBarcode();
				wrong = hasWrongBarcode();
			}
			
			if (correct) {// correcte barcode
				//DebugBuffer.addInfo("correct barcode");
				
				// get barcode of current tile
				Barcode code = getCurrTile().getBarcode();
				
				//DebugBuffer.addInfo("test: " + code.getType());
				
				// do action based on the barcode type
				switch (code.getType()) {
					case OBJECT:
						System.out.println("Teamnr: "+getTeamNr());
						System.out.println("Gevonden: "+hasFoundOwnBarcode());
						
						// keep current tile of robot as reference
						Tile tile = getCurrTile();
						getField().registerBall(tile.getPosition(), getDirection());
						
						if (getTeamNr() != -1 && !hasFoundOwnBarcode()) {
							pickUpObjectAction();
						} else {
							wrongObjectAction();
						}
						
						System.out.println("tile: " + getCurrTile().getPosition());
						break;
					case CHECKPOINT:
						TilePosition afterBarcodePos = getDirection().getPositionInDirection(getCurrTile().getPosition());
						toExplore.add(afterBarcodePos);
						break;
					case ILLEGAL:
						break;
					case OTHERPLAYERBARCODE:
						break;
					case PICKUP:
						break;
					case SEESAW:
						// keep current tile as reference
						Tile ctile = getCurrTile();
						getField().registerSeesaw(ctile.getPosition(), getDirection());
						
						//seesawAction();
						
						TilePosition afterWipPos = getTilePositionAfterSeesaw(ctile);
						toExplore.add(afterWipPos);
						break;
					default:
						break;
				}
				
			} else if (wrong) {
				throw new IllegalStateException("robot heeft foute barcode gelezen");
			}
		} else {
			TilePosition current = getCurrTile().getPosition();
			// if border at back is defined do new tile scan
			Direction dirx = getDirection().opposite();
			if (fieldRepresentation.hasBackBorder(current, dirx) &&
					!(fieldRepresentation.getBorderInDirection(current, dirx) instanceof UnsureBorder)) {
				newTileScan();

			} else { // else scan 360
				scanSonar();
			}

			System.out.println("scan command given " + current + " rt " + getCurrTile());

			// wait till tile border results have been given
			while (!fieldRepresentation.isExplored(current)) {
				waitTillStandby(1000);
			}
			System.out.println("done scanning");

			// check for gray borders in every direction
			for (Direction dir : Direction.values()) {
				if (fieldRepresentation.getBorderInDirection(current, dir) instanceof UnsureBorder) {
					// turn and move to gray border
					turnToAngle(dir.toAngle());
					moveForward(55);
					waitTillStandby(1000);
					// scan border again, time outs with 3 tries
					// after 3 tries add white border with new tile
					int counter = 0;
					while (fieldRepresentation.getBorderInDirection(current, dir) instanceof UnsureBorder) {
						checkScan();
						waitTillStandby(900);
						if (counter == 2 && fieldRepresentation.getBorderInDirection(current, dir) instanceof UnsureBorder) {
							getField().registerNewTile(dir, current);
							break;
						}
						counter++;
					}
					// move back
					moveBackward(55);
					waitTillStandby(1000);
				}
				
				// if a white border was scanned add it to explore list
				if (fieldRepresentation.getBorderInDirection(current, dir) instanceof WhiteBorder) {
					TilePosition pos = dir.getPositionInDirection(current);
					toExplore.add(pos);
				}
			}
		}
		
		return toExplore;
	}
	
	
	
	private Collection<TilePosition> toExplore = new ArrayList<TilePosition>();
	
	public Collection<TilePosition> getToExplore() {
		return toExplore;
	}

	/**
	 * 
	 * @return true if crossed seesaw, false if we didn't cross it
	 */
	public boolean seesawAction() {
		// keep current tile and orientation as reference
		Tile ctile = getCurrTile();
		Direction dirForw = getDirection();
		// wait a bit for infrared
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// is seesaw open?
		if(SensorBuffer.getInfrared() < 4 ){
			setCurrentAction("Crossing seesaw at " + ctile.getPosition());
			// yes, this means we have to cross it
			// register open seesaw position
			getField().registerSeesawPosition(ctile.getPosition(), dirForw, ctile.getBarcode().isSeesawDownCode());
			// lock seesaw
			comm.lockSeesaw(ctile.getBarcode().getDecimal());
			// move across the seesaw
			moveAcrossSeesawPhysical();
			waitTillStandby(500);
			
			// register closed seesaw position
			getField().registerSeesawPosition(ctile.getPosition(), dirForw, ctile.getBarcode().isSeesawUpCode());
			// unlock seesaw
			comm.unlockSeesaw();

			// we know we are at end of seesaw position
			TilePosition afterWipPos = getTilePositionAfterSeesaw(ctile);
			setPosition(new robot.Position(0, 0, getPosition().getRotation()), getField().getTileAt(afterWipPos));
			
			return false;
		} else {
			setCurrentAction("Can not cross seesaw at " + ctile.getPosition());
			// not open
			// register closed seesaw position
			getField().registerSeesawPosition(ctile.getPosition(), dirForw, ctile.getBarcode().isSeesawUpCode());
			return true;
		}
	}
	
	public void pickUpObjectAction() {
		// keep current tile and orientation as reference
		Tile ctile = getCurrTile();
		Direction dirForw = getDirection();
		setCurrentAction("Picking up object at " + ctile.getPosition());
		
		System.out.println("PICKUP");
		setHasFoundOwnBarcode(true);
		stopMoving();

		System.out.println("ObjectNr: "+Integer.parseInt(getCurrTile().getBarcode().toString().substring(4, 5),2));
		System.out.println("OurObjectNr"+getObjectNr());
		System.out.println("Barcode: "+getCurrTile().getBarcode());

		// execute pickup

		pauseLightSensor();
		/*robot.turnLeft(90);
				waitTillRobotStops(robot, 250);
				robot.startMovingForward();
				DebugBuffer.addInfo("touch");

				while(!SensorBuffer.getTouched()){}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			DebugBuffer.addInfo("after touch");
				robot.stopMoving();
				robot.moveBackward(100);
				robot.turnRight(90);
				waitTillRobotStops(robot, 250);*/
		//DebugBuffer.addInfo("pick obj up");
		startMovingForward();
		while(!SensorBuffer.getTouched()){
			System.out.print("");
		};
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//DebugBuffer.addInfo("picked up");
		stopMoving();
		moveBackward(100);
		turnLeft(180);
		moveForward(800);
		resumeLightSensor();
		setHasBall(true);
		// execute pickup

		waitTillStandby(500);

		DebugBuffer.addInfo("OUT: my team is " + getTeamNr());
		// send object found + join team via rabbitmq
		comm.foundObject();
		comm.joinTeam(getTeamNr());

		setPosition(new robot.Position(0, 0, dirForw.opposite().toAngle()),
				getField().getTileAt(
						dirForw.opposite().getPositionInDirection(ctile.getPosition())));
	}
	
	public void wrongObjectAction() {
		// keep current tile and orientation as reference
		Tile ctile = getCurrTile();
		Direction dirForw = getDirection();
		setCurrentAction("Found other player object at " + ctile.getPosition());
		//System.out.println("WRONG OBJ");
		
		if (!isSim()) {
			// execute move away from wrong object
			//pauseLightSensor();
			scanOnlyLines(true);
			//DebugBuffer.addInfo("PAUSE");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			moveForward(200);
			waitTillStandby(250);
			turnLeft(180);
			waitTillStandby(250);
			moveForward(750);
			waitTillStandby(250);
			//DebugBuffer.addInfo("RESUME");
			scanOnlyLines(false);
			//resumeLightSensor();
			setPosition(new robot.Position(0, 0, dirForw.opposite().toAngle()),
					getField().getTileAt(
							dirForw.opposite().getPositionInDirection(ctile.getPosition())));
			
		}

		//check is false when next tile (in direction of robot) need not be explored
		// this is the case when we cross the seesaw or when we come across any object barcode
	}
	
	public void teamComm() {
		setCurrentAction("Looking for friend");
		waitTillStandby(2000);
		// wait till teammate is set, meanwhile go explore
		Explorer.explore(this, new EndingCondition() {
			@Override
			public boolean isLastTile(Robot robot) {
				return robot.hasTeamMate();
			}
		});
		
		// wait till my teammate is here
		while (!hasTeamMate());
		
		getField().foundTeamMate(comm);

		setCurrentAction("Sending tiles to friend");
		// make collection of tilesmsges
		comm.sendInitialField(getField());

		setCurrentAction("Waiting for teammate tiles");
		// wait till teammate has sent tiles
		while (!receivedTeamTiles()) { }
		setCurrentAction("Merging tiles");

		try {
			// merge fields
			getField().mergeFields(getTeamMate().getField());
		} catch (IllegalStateException e) {
			DebugBuffer.addInfo("exploring more");
			//e.printStackTrace();
			Explorer.explore(this, new EndingCondition() {
				
				@Override
				public boolean isLastTile(Robot robot) {
					try {
						// merge fields
						getField().mergeFields(getTeamMate().getField());
						return true;
					} catch (IllegalStateException e) {
						DebugBuffer.addInfo("could not merge fields");
						return false;
					}
				}
			});
		}
	
		// TODO check merged field ?
	
	
		waitTillStandby(2000);
		
		goToTeamMate();
	}
	
	public void waitTillStandby(int base) {
		waitTillStandbyInner(base/3);
		waitTillStandbyInner(base/3);
		waitTillStandbyInner(base/3);
	}
	
	private void waitTillStandbyInner(int base) {
		try {
			if (!isSim()) {
				Thread.sleep(base);
			} else {
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (isMoving()) {
			try {
				if (!isSim()) {
					Thread.sleep(100);
				} else {
					Thread.sleep(50);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String currentAction;

	public String getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}
	
	private Map<TilePosition, Integer> robotSpottedTiles = new HashMap<TilePosition, Integer>();
	
	public Collection<TilePosition> getRobotSpottedTiles() {
		return robotSpottedTiles.keySet();
	}
	
	private void decreaseSpottedRobotTiles() {
		Iterator<TilePosition> it = robotSpottedTiles.keySet().iterator();
		while(it.hasNext()) {
			TilePosition tilePos = it.next();
			if (robotSpottedTiles.get(tilePos) == 0) {
				it.remove();
			} else {
				robotSpottedTiles.put(tilePos, robotSpottedTiles.get(tilePos) - 1);
			}
		}
	}

	public FieldSimulation getFieldSimulation() {
		return fieldSimulation;
	}

	public void setFieldSimulation(FieldSimulation fieldSimulation) {
		this.fieldSimulation = fieldSimulation;
	}
}
