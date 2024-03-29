package field;

public class BorderPosition {

	private TilePosition position1 = null;
	private TilePosition position2 = null;
	
	public BorderPosition(TilePosition position1, TilePosition position2) {
		super();
		setPosition1(position1);
		setPosition2(position2);
	}
	
	public int getLowestX() {
		return Math.min(getPosition1().getX(), getPosition2().getX());
	}
	
	public int getLowestY() {
		return Math.min(getPosition1().getY(), getPosition2().getY());
	}
	
	public int getHighestX() {
		return Math.max(getPosition1().getX(), getPosition2().getX());
	}
	
	public int getHighestY() {
		return Math.max(getPosition1().getY(), getPosition2().getY());
	}
	
	public boolean isHorizontal() {
		return (getPosition1().getX() == getPosition2().getX());
	}
	
	public TilePosition getOtherPosition(TilePosition pos) {
		if (position1.equals(pos)) {
			return getPosition2();
		}
		if (position2.equals(pos)) {
			return getPosition1();
		}
		throw new IllegalArgumentException("borderpos does not have " + pos);
	}
	
	public TilePosition getPosition1() {
		return position1;
	}
	public void setPosition1(TilePosition position1) {
		if (!canHaveAsPosition(position1, 1)) {
			throw new IllegalArgumentException();
		}
		this.position1 = position1;
	}
	public TilePosition getPosition2() {
		return position2;
	}
	public void setPosition2(TilePosition position2) {
		if (!canHaveAsPosition(position2, 2)) {
			throw new IllegalArgumentException();
		}
		this.position2 = position2;
	}
	public boolean canHaveAsPosition(TilePosition position, int pos)
		throws IllegalArgumentException{
		if (position == null) {
			return false;
		}
		if (pos == 1) {
			if (position2 == null || position.manhattanDistance(position2) == 1) {
				return true;
			}
		} else if (pos == 2){
			if (position1 == null || position.manhattanDistance(position1) == 1) {
				return true;
			}
			return !position.equals(position1);
		} else {
			throw new IllegalArgumentException();
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		TilePosition pos = new TilePosition(position1, position2);
		result = prime * result
				+ ((pos == null) ? 0 : pos.hashCode());
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
		BorderPosition other = (BorderPosition) obj;
		if (position1 == null && other.position1 != null) {
			return false;
		}
		if (position2 == null && other.position2 != null) {
			return false;
		}
		if (position1 != null && position2 != null && other.position1 != null && other.position2 != null) {
			if (position1.equals(other.position1) && position2.equals(other.position2)){
				return true;
			} else if (position1.equals(other.position2) && position2.equals(other.position1)) {
				return true;
			}
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "1:[" + position1 + "] 2:["
				+ position2 + "]";
	}
	
	public BorderPosition moveX(int move) {
		return new BorderPosition(
				new TilePosition(getPosition1().getX() + move, getPosition1().getY()),
				new TilePosition(getPosition2().getX() + move, getPosition2().getY()));
	}
	
	public BorderPosition moveY(int move) {
		return new BorderPosition(
				new TilePosition(getPosition1().getX(), getPosition1().getY() + move),
				new TilePosition(getPosition2().getX(), getPosition2().getY() + move));
	}
	
	public BorderPosition rotate(int rotation, TilePosition pos) {
		int[] newpos1 = TilePosition.rotate(rotation, getPosition1(), pos);
		int[] newpos2 = TilePosition.rotate(rotation, getPosition2(), pos);
		return new BorderPosition(
				new TilePosition(newpos1[0], newpos1[1]),
				new TilePosition(newpos2[0], newpos2[1]));
	}
}
