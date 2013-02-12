package robot;

import field.Direction;

/**
 * @author  Samuel
 */
public class Position {

	private double posX;
	private double posY;
	private double angle;
	
	public Position() {
		this(0,0,0);
	}
	
	public Position(double x, double y, double angle){
		setPosX(x);
		setPosY(y);
		setRotation(angle);
	}

	public double getPosX() {
		return posX;
	}

	private void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	private void setPosY(double posY) {
		this.posY = posY;
	}
	public double getRotation() {
		return angle;
	}
	public double getRotationRadian() {
		return angle * Math.PI / 180;
	}
	private void setRotation(double angle) {
		this.angle = angle % 360;
	}
	
	public void updatePosition(double d) {
		setPosX(getPosX() + d * Math.sin(getRotationRadian()));
		setPosY(getPosY() + d * Math.cos(getRotationRadian()));
	}
	
	public void updateRotation(double rot) {
		setRotation(getRotation() + rot);
	}
	
	public double getRoundedFrom(double nr) {
		return (double)Math.round(nr * 10000) /10000;
	}
	
	@Override
	public String toString() {
		return "x: " + getRoundedFrom(getPosX()) + " y: " + getRoundedFrom(getPosY()) + " rotation: " + getRoundedFrom(getRotation());
	}
	
	public void resetPosition(Direction dir) {
		switch (dir) {
			case TOP:
				setPosY(-17);
				break;
			case BOTTOM:
				setPosY(17);
				break;
			case RIGHT:
				setPosX(-17);
				break;
			case LEFT:
				setPosX(17);
				break;
		}
	}
	
	public Position getPositionAhead(double d){
		Position thePosition = new Position(this.getPosX(),this.getPosY(), Math.signum(d)*this.getRotation());
		thePosition.updatePosition(d);
		return thePosition;
	}
	
	public double getManhattanDistance(Position position2){
		double x = Math.pow(this.getPosX()-position2.getPosX(),2);
		double y = Math.pow(this.getPosY()-position2.getPosY(),2);
		double distance = Math.sqrt(  x + y );
		return distance;
	}

	public void zeroPos() {
		posX = 0;
		posY = 0;
		angle = 0;
	}
}
