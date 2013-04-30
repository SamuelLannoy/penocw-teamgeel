package robot;

import corner.Corner;
import field.Field;
import field.Tile;
import field.TilePosition;
import field.simulation.FieldSimulation;

public class RobotModel {
	protected Tile currTile;
	
	public Tile getCurrTile() {
		return currTile;
	}
	
	public void setCurrTile(Tile tile) {
		this.currTile = tile;
	}

	protected Position position = new Position();

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public void setPosition(Position pos, Tile tile) {
		this.position = pos;
		this.currTile = tile;
	}
	
	public void setGlobalPosition(double x, double y, double angle) {
		/*double[] position = FieldSimulation.convertToInTilePos(new double[] {x, y });*/
		field.TilePosition tpos = new TilePosition((int)x, (int)y);

		//DebugBuffer.addInfo("Setting pos " + tpos);
		setPosition(new Position(0, 0, angle), new Tile(tpos));
	}
	
	private double width = 6;
	private double length = 10;
	
	public double[][] getCorners(double offset) {
		double ta = -(getPosition().getRotation() - offset) * Math.PI / 180;
		return Corner.getCorners(0, 0, width, length, ta);
	}
	
	public double[][] getCorners() {
		return getCorners(0);
	}
	
	public double[] getCornersX(double offset) {
		double[] xarr = new double[4];
		
		double[][] arr = getCorners(offset);
		
		xarr[0] = arr[0][0];
		xarr[1] = arr[1][0];
		xarr[2] = arr[2][0];
		xarr[3] = arr[3][0];
		
		return xarr;
	}
	
	public double[] getCornersX() {
		return getCornersX(0);
	}
	
	public double[] getCornersY(double offset) {
		double[] yarr = new double[4];
		
		double[][] arr = getCorners(offset);
		
		yarr[0] = arr[0][1];
		yarr[1] = arr[1][1];
		yarr[2] = arr[2][1];
		yarr[3] = arr[3][1];
		
		return yarr;
	}
	
	public double[] getCornersY() {
		return getCornersY(0);
	}
	
	//TODO: remove method
	public void test() {
		double[][] corners = getCorners();
		System.out.println("corners: x1:" + corners[0][0] + " y1: " + corners[0][1]
				 + " x2: " + corners[1][0] + " y2: " + corners[1][1]
						 + " x3: " + corners[2][0] + " y3: " + corners[2][1]
								 + " x4: " + corners[3][0] + " y4: " + corners[3][1]);
	}

	private boolean hasBall = false;

	public boolean hasBall() {
		return hasBall;
	}

	public void setHasBall(boolean hasBall) {
		this.hasBall = hasBall;
	}
	
	private int playerNr;

	public int getPlayerNr() {
		return playerNr;
	}

	public void setPlayerNr(int playerNr) {
		this.playerNr = playerNr;
	}
	
	private boolean isReady;

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
}
