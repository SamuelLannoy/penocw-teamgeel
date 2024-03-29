package robot.brain;

import field.Direction;
import field.TilePosition;
import field.Tile;

public class Node {
	
	private Tile tile;
	private int C;
	private int H;
	private int T;
	private Node prev;
	private Direction direction;
	
	public Node(Tile tile, int c, int h, Node prev) {
		super();
		this.tile = tile;
		C = c;
		H = h;
		this.prev = prev;
	}
	public Tile getTile() {
		return tile;
	}
	public TilePosition getPos() {
		return getTile().getPosition();
	}
	public int getC() {
		return C;
	}
	public int getH() {
		return H;
	}
	public int getF() {
		return C+H;
	}
	public int getT() {
		return T;
	}
	public void setT(int t) {
		T = t;
	}
	public Node getPrev() {
		return prev;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + C;
		result = prime * result + H;
		result = prime * result + ((tile.getPosition() == null) ? 0 : tile.getPosition().hashCode());
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
		Node other = (Node) obj;
		if (tile == null) {
			if (other.tile != null)
				return false;
		} else if (!tile.getPosition().equals(other.tile.getPosition()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return tile.getPosition().toString() + " c: " + getC() + " h: " + getH() + " t: " + getT();
	}

}
