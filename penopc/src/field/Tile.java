package field;


public class Tile {
	
	public Tile() {
		this(TilePosition.POSITION_ZERO);
	}
	
	public Tile(TilePosition position) {
		setPosition(position);
	}
	
	public Tile(int x, int y) {
		setPosition(new TilePosition(x, y));
	}
	
	public TilePosition getPosition() {
		return position;
	}

	private void setPosition(TilePosition position) {
		if (!isValidPosition(position)) {
			throw new IllegalArgumentException("given position is invalid");
		}
		this.position = position;
	}
	
	private boolean isValidPosition(TilePosition position) {
		return position != null;
	}

	private TilePosition position;

	private Barcode barcode;

	public Barcode getBarcode() {
		return barcode;
	}

	public void setBarcode(Barcode barcode) {
		this.barcode = barcode;
	}
	
	public boolean hasBarcode() {
		return getBarcode() != null;
	}
	
	@Override
	public String toString() {
		return "x:" + getPosition().getX() + " y:" + getPosition().getY();
	}
	
	public Tile moveX(int move) {
		Tile ret = new Tile(this.getPosition().getX() + move, this.getPosition().getY());
		ret.setBarcode(getBarcode());
		return ret;
	}
	
	public Tile moveY(int move) {
		Tile ret = new Tile(this.getPosition().getX(), this.getPosition().getY() + move);
		ret.setBarcode(getBarcode());
		return ret;
	}
	
	public Tile rotate(int rotation, TilePosition pos) {
		int[] newpos = TilePosition.rotate(rotation, getPosition(), pos);

		Tile ret = new Tile(newpos[0], newpos[1]);
		ret.setBarcode(getBarcode());
		return ret;
	}
	
}
