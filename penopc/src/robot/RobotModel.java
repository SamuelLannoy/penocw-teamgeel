package robot;

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
		field.Position tpos = Field.convertToTilePosition(x, y);
		setPosition(new Position(position[0], position[1], angle), new Tile(tpos));
	}
	
	private double width = 6;
	private double length = 10;
	
	public double[][] getCorners() {
		double[][] arr = new double[4][2];
		
		double tx = 0;//getPosition().getPosX();
		double ty = 0;//getPosition().getPosY();
		double ta = -getPosition().getRotation() * Math.PI / 180;
		
		
		//left upper
		double lux = - width;
		double luy = length;
		
		arr[0][0] = tx + ( lux * Math.cos(ta) - luy * Math.sin(ta)); 
		arr[0][1] = ty + ( lux * Math.sin(ta)  + luy * Math.cos(ta)); 
		//right upper
		double rux = width;
		double ruy = length;
		
		arr[1][0] = tx + ( rux * Math.cos(ta)  - ruy * Math.sin(ta));
		arr[1][1] = ty + ( rux * Math.sin(ta)  + ruy * Math.cos(ta)); 
		//left lower
		double llx = width;
		double lly = - length;
		
		arr[2][0] = tx + ( llx * Math.cos(ta)  - lly * Math.sin(ta));  
		arr[2][1] = ty + ( llx * Math.sin(ta)  + lly * Math.cos(ta));
		//right lower
		double rlx = - width;
		double rly = - length;
		
		arr[3][0] = tx + ( rlx * Math.cos(ta)  - rly * Math.sin(ta));
		arr[3][1] = ty + ( rlx * Math.sin(ta)  + rly * Math.cos(ta)); 
		
		return arr;
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
}
