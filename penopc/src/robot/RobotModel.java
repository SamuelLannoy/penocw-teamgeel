package robot;

import corner.Corner;
import field.Field;
import field.Tile;

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
		double[] position = Field.convertToInTilePos(new double[] {x, y });
		field.TilePosition tpos = Field.convertToTilePosition(x, y);
		setPosition(new Position(position[0], position[1], angle), new Tile(tpos));
	}
	
	private double width = 6;
	private double length = 10;
	
	public double[][] getCorners() {
		double ta = -getPosition().getRotation() * Math.PI / 180;
		return Corner.getCorners(0, 0, width, length, ta);
	}
	
	public double[] getCornersX() {
		double[] xarr = new double[4];
		
		double[][] arr = getCorners();
		
		xarr[0] = arr[0][0];
		xarr[1] = arr[1][0];
		xarr[2] = arr[2][0];
		xarr[3] = arr[3][0];
		
		return xarr;
	}
	
	public double[] getCornersY() {
		double[] yarr = new double[4];
		
		double[][] arr = getCorners();
		
		yarr[0] = arr[0][1];
		yarr[1] = arr[1][1];
		yarr[2] = arr[2][1];
		yarr[3] = arr[3][1];
		
		return yarr;
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
}
