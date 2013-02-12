package robot.brain;

import field.Tile;

public class ExploreNode {
	
	public ExploreNode(Tile tile, Tile parent) {
		super();
		this.tile = tile;
		this.parent = parent;
	}
	
	private Tile tile;
	public Tile getTile() {
		return tile;
	}
	
	private Tile parent;
	public Tile getParent() {
		return parent;
	}
	
	@Override
	public String toString() {
		return ("explorenode " + getTile().getPosition());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tile == null) ? 0 : tile.hashCode());
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
		ExploreNode other = (ExploreNode) obj;
		if (tile == null) {
			if (other.tile != null)
				return false;
		} else if (!tile.getPosition().equals(other.tile.getPosition()))
			return false;
		return true;
	}

}
