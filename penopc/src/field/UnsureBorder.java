package field;

public class UnsureBorder extends Border {
	public UnsureBorder(BorderPosition borderPos)
			throws IllegalArgumentException {
		super(borderPos);
	}

	public UnsureBorder(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2);
	}

	public UnsureBorder(Tile tile, Direction dir) {
		super(tile, dir);
	}

	@Override
	public boolean isPassable() {
		return true;
	}
	
	public String toString() {
		return "unsure border";
	}

}
