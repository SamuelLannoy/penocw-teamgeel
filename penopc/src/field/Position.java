package field;

public class Position {
	
	public final static Position POSITION_ZERO = new Position(0, 0);
	
	public Position() {
		this(0, 0);
	}
	
	public Position(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public Position(Position pos1, Position pos2) {
		setX(pos1.getX() + pos2.getX());
		setY(pos1.getY() + pos2.getY());
	}

	private int x;
	
	public int getX() {
		return x;
	}

	private void setX(int x) {
		this.x = x;
	}
	
	private int y;

	public int getY() {
		return y;
	}

	private void setY(int y) {
		this.y = y;
	}
	
	public int manhattanDistance(Position pos) {
		return Math.abs(pos.getX() - this.getX()) + Math.abs(pos.getY() - this.getY());
	}
	
	public Position getNorthPosition() {
		return new Position(getX(), getY() + 1);
	}
	
	public Position getSouthPosition() {
		return new Position(getX(), getY() - 1);
	}
	
	public Position getRightPosition() {
		return new Position(getX() + 1, getY());
	}
	
	public Position getLeftPosition() {
		return new Position(getX() - 1, getY());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Position x: " + Integer.toString(x) + " y: " + Integer.toString(y);
	}

}
