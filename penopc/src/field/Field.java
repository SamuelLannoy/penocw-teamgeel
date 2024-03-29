package field;

import java.util.Collection;
import java.util.Iterator;

import robot.DebugBuffer;


public abstract class Field{
	
	public final static int TILE_SIZE = 40;
	public final static double BORDER_SIZE = 2;
	
	
	
	public Field() {
		
	}
	

	/*
	 * Tile Methods
	 */
	
	protected ObjectMap<TilePosition, Tile> tileMap = new ObjectMap<TilePosition, Tile>();
	
	public boolean hasTileAt(TilePosition tilePosition) {
		return tileMap.hasId(tilePosition);
	}
	
	public Tile getTileAt(TilePosition tilePosition) {
		return tileMap.getObjectAtId(tilePosition);
	}
	
	protected void addTile(Tile tile) {
		if (!hasTileAt(tile.getPosition()))
			tileMap.addObject(tile.getPosition(), tile);
	}
	
	protected void addTile(TilePosition tilePos) {
		if (!hasTileAt(tilePos))
			tileMap.addObject(tilePos, new Tile(tilePos));
	}

	/*
	 * Border Methods
	 */

	protected ObjectMap<BorderPosition, Border> borderMap = new ObjectMap<BorderPosition, Border>();
	
	protected boolean hasBorderAt(BorderPosition borderPosition) {
		return borderMap.hasId(borderPosition);
	}
	
	protected Border getBorderAt(BorderPosition borderPosition) {
		return borderMap.getObjectAtId(borderPosition);
	}
	
	public Border getBorderBetweenTiles(Tile tile1, Tile tile2) {
		if (tile1.getPosition().manhattanDistance(tile2.getPosition()) != 1)
			throw new IllegalArgumentException("tiles are not next to each other");
		return borderMap.getObjectAtId(new BorderPosition(tile1.getPosition(), tile2.getPosition()));
	}
	
	protected void addBorder(Border border) {
		if (!hasBorderAt(border.getBorderPos()))
			borderMap.addObject(border.getBorderPos(), border);
	}
	
	protected void overWriteBorder(Border border) {
		borderMap.overWrite(border.getBorderPos(), border);
	}
	
	protected void addBorders(Collection<Border> borders) {
		for (Border border : borders) {
			addBorder(border);
		}
	}

	public Border getBorderInDirection(TilePosition tilePos, Direction dir) {
		return borderMap.getObjectAtId(dir.getBorderPositionInDirection(tilePos));
	}
	
	public Border getBorderInDirection(Tile tile, Direction dir) {
		return getBorderInDirection(tile.getPosition(), dir);
	}
	
	public boolean hasBorderInDirection(Tile tile, Direction dir) {
		return borderMap.hasId(dir.getBorderPositionInDirection(tile.getPosition()));
	}
	
	public Border getTopBorderOfTile(Tile tile)
			throws IllegalArgumentException {
		BorderPosition pos = new BorderPosition(tile.getPosition(), tile.getPosition().getNorthPosition());
		return borderMap.getObjectAtId(pos);
	}
	
	public Border getBottomBorderOfTile(Tile tile)
			throws IllegalArgumentException {
		BorderPosition pos = new BorderPosition(tile.getPosition(), tile.getPosition().getSouthPosition());
		return borderMap.getObjectAtId(pos);
	}
	
	public Border getLeftBorderOfTile(Tile tile)
			throws IllegalArgumentException {
		BorderPosition pos = new BorderPosition(tile.getPosition(), tile.getPosition().getLeftPosition());
		return borderMap.getObjectAtId(pos);
	}
	
	public Border getRightBorderOfTile(Tile tile)
			throws IllegalArgumentException {
		BorderPosition pos = new BorderPosition(tile.getPosition(), tile.getPosition().getRightPosition());
		return borderMap.getObjectAtId(pos);
	}
	
	/*
	 * Ball Methods
	 */

	protected ObjectMap<TilePosition, Ball> ballMap = new ObjectMap<TilePosition, Ball>();
	
	protected boolean hasBallAt(TilePosition tilePosition) {
		return ballMap.hasId(tilePosition);
	}
	
	protected void addBall(Ball ball, TilePosition tilePosition) {
		if (hasBallAt(tilePosition)) {
			ballMap.addObject(tilePosition, ball);
		}
	}
	
	protected void removeBall(TilePosition pos) {
		ballMap.removeObjectAtId(pos);
	}
	
	/*
	 * Iterators
	 */
	
	public Iterator<Tile> tileIterator() {
		return tileMap.iterator();
	}
	
	public Iterator<Border> borderIterator() {
		return borderMap.iterator();
	}
	
	public Iterator<Ball> ballIterator() {
		return ballMap.iterator();
	}
	
	/*
	 * Seesaw methods
	 */
	
	public boolean hasSeesawBorder(Tile tile) {
		for (Direction dir : Direction.values()) {
			Border border = getBorderInDirection(tile, dir);
			if (border instanceof SeesawBorder) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isSeesawTile(Tile tile) {
		return (hasSeesawBorder(tile) && tile.getBarcode() == null);
	}
	
	public SeesawBorder getSeesawBorder(Tile tile) {
		for (Direction dir : Direction.values()) {
			Border border = getBorderInDirection(tile, dir);
			System.out.println("border " + border.toString());
			if (border instanceof SeesawBorder) {
				return (SeesawBorder)border;
			}
		}
		throw new IllegalArgumentException("no seesaw border on tile " + tile.getPosition());
	}
	
	protected Direction getDirectionOfSeesawBorder(Tile tile) {
		for (Direction dir : Direction.values()) {
			Border border = getBorderInDirection(tile, dir);
			if (border instanceof SeesawBorder) {
				return dir;
			}
		}
		throw new IllegalArgumentException("no seesaw border on tile " + tile.getPosition());
	}
	
	/*
	 * TODO REMOVE
	 */

	public ObjectMap<TilePosition, Tile> getTileMap() {
		synchronized(tileMap) {
			return tileMap;
		}
	}

	public ObjectMap<BorderPosition, Border> getBorderMap() {
		return borderMap;
	}
	
	public ObjectMap<TilePosition, Ball> getBallMap() {
		return ballMap;
	}
}
