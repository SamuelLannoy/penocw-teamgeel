package field;

public class TilePosition {
	
	public final static TilePosition POSITION_ZERO = new TilePosition(0, 0);
	
	public TilePosition() {
		this(0, 0);
	}
	
	public TilePosition(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public TilePosition(TilePosition pos1, TilePosition pos2) {
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
	
	public int manhattanDistance(TilePosition pos) {
		return Math.abs(pos.getX() - this.getX()) + Math.abs(pos.getY() - this.getY());
	}
	
	public static double euclDistance(TilePosition pos1, TilePosition pos2) {
		return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2));
	}
	
	public TilePosition getNorthPosition() {
		return new TilePosition(getX(), getY() + 1);
	}
	
	public TilePosition getSouthPosition() {
		return new TilePosition(getX(), getY() - 1);
	}
	
	public TilePosition getRightPosition() {
		return new TilePosition(getX() + 1, getY());
	}
	
	public TilePosition getLeftPosition() {
		return new TilePosition(getX() - 1, getY());
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
		TilePosition other = (TilePosition) obj;
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
	
	public static int[] rotate(int rotation, TilePosition pos1, TilePosition center) {
		float s = (float) Math.sin(rotation * Math.PI / 180);
		float c = (float) Math.cos(rotation * Math.PI / 180);

		// translate point back to origin:
		int x = pos1.getX() - center.getX();
		int y = pos1.getY() - center.getY();

		// rotate point
		float xnew = x * c - y * s;
		float ynew = x * s + y * c;

		// translate point back:
		x = (int) (xnew + center.getX());
		y = (int) (ynew + center.getY());
		
		return new int[] {x, y};
	}

}
