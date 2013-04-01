package field;


public class Tile {
	
	public Tile() {
		this(Position.POSITION_ZERO);
	}
	
	public Tile(Position position) {
		setPosition(position);
	}
	
	public Tile(int x, int y) {
		setPosition(new Position(x, y));
	}
	
	public Position getPosition() {
		return position;
	}

	private void setPosition(Position position) {
		if (!isValidPosition(position)) {
			throw new IllegalArgumentException("given position is invalid");
		}
		this.position = position;
	}
	
	private boolean isValidPosition(Position position) {
		return position != null;
	}

	private Position position;

	private Barcode barcode;

	public Barcode getBarcode() {
		return barcode;
	}

	public void setBarcode(Barcode barcode) {
		this.barcode = barcode;
	}
	
	public boolean hasBarcocde() {
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
	
	public Tile rotate(int rotation, Position pos) {
		int[] newpos = Position.rotate(rotation, getPosition(), pos);

		Tile ret = new Tile(newpos[0], newpos[1]);
		ret.setBarcode(getBarcode());
		return ret;
	}
	
}
