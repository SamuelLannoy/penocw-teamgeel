package field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import field.fieldmerge.BarcodeNode;
import field.fieldmerge.Fieldable;

import robot.DebugBuffer;
import robot.RobotModel;
import robot.RobotPool;


public abstract class Field implements Fieldable {
	
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
	
	
	
	
	
	
	
	
	
	
	
	public List<BarcodeNode> getBarcodes() {
		List<BarcodeNode> ret = new ArrayList<BarcodeNode>(tileMap.getObjectCollection().size() / 5);
		for (Tile tile : tileMap) {
			if (tile.getBarcode() != null) {
				ret.add(new BarcodeNode(tile.getBarcode(), tile.getPosition()));
			}
		}
		return ret;
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
	
	public Border getBorderInDirection(Tile tile, Direction dir) {
		return borderMap.getObjectAtId(dir.getBorderPositionInDirection(tile.getPosition()));
	}
	
	public SolidBorder getFirstPanelInDirection(Tile tile, Direction dir) {
		boolean found = false;
		TilePosition currPos = tile.getPosition();
		while (!found) {
			BorderPosition pos = dir.getBorderPositionInDirection(currPos);
			try {
				Border border = null;
				border = borderMap.getObjectAtId(pos);
				if (border instanceof SolidBorder && !border.isPassable()) {
					return (SolidBorder)border;
				}
			} catch (IllegalArgumentException e) {
				return null;
			}
			currPos = dir.getPositionInDirection(currPos);
		}
		return null;
	}

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
	

	
	public List<Tile> getPassableNeighbours(Tile tile) {
		List<Tile> ret = new ArrayList<Tile>();
		
		for (Direction dir : Direction.values()) {
			BorderPosition pos;
			pos = dir.getBorderPositionInDirection(tile.getPosition());
			if (borderMap.hasId(pos) && borderMap.getObjectAtId(pos).isPassable()) {
				TilePosition tpos = pos.getOtherPosition(tile.getPosition());
				if (tileMap.hasId(tpos)) {
					ret.add(tileMap.getObjectAtId(tpos));
				}
			}
		}
		
		
		
		return ret;
	}
	


	@Override
	public Field toField() {
		return this;
	}
	
	
	public void makeUnsure(BorderPosition borderPos) {
		if (borderMap.hasId(borderPos)) {
			borderMap.overWrite(borderPos, new UnsureBorder(borderPos));
		}
	}
	
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
			if (border instanceof SeesawBorder) {
				return (SeesawBorder)border;
			}
		}
		throw new IllegalArgumentException("no seesaw border on tile " + tile.getPosition());
	}
}
