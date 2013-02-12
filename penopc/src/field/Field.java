package field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Field {
	
	public final static int TILE_SIZE = 40;
	public final static double BORDER_SIZE = 2.5;
	//public final static double BORDER_SIZE = 5.5;
	
	public Field() {
	}
	
	private ObjectMap<Position, Tile> tileMap = new ObjectMap<Position, Tile>();
	private ObjectMap<BorderPosition, Border> borderMap = new ObjectMap<BorderPosition, Border>();
	
	
	public void clear() {
		tileMap = new ObjectMap<Position, Tile>();
		borderMap = new ObjectMap<BorderPosition, Border>();
	}
	
	public void addTile(Tile tile){
		if (!canHaveAsTile(tile.getPosition()))
			throw new IllegalArgumentException("given position already used");
		tileMap.addObject(tile.getPosition(), tile);
	}
	
	public void addTileWithBorders(Tile tile, boolean top, boolean right, boolean bot, boolean left) {
		if (!canHaveAsTile(tile.getPosition()))
			throw new IllegalArgumentException("given position already used");
		tileMap.addObject(tile.getPosition(), tile);
		BorderPosition north = new BorderPosition(tile.getPosition(), tile.getPosition().getNorthPosition());
		if (canHaveAsBorder(north)) {
			borderMap.addObject(north, top ? new PanelBorder(north) : new WhiteBorder(north));
		}
		BorderPosition south = new BorderPosition(tile.getPosition(), tile.getPosition().getSouthPosition());
		if (canHaveAsBorder(south)) {
			borderMap.addObject(south, bot ? new PanelBorder(south) : new WhiteBorder(south));
		}
		BorderPosition leftB = new BorderPosition(tile.getPosition(), tile.getPosition().getLeftPosition());
		if (canHaveAsBorder(leftB)) {
			borderMap.addObject(leftB, left ? new PanelBorder(leftB) : new WhiteBorder(leftB));
		}
		BorderPosition rightB = new BorderPosition(tile.getPosition(), tile.getPosition().getRightPosition());
		if (canHaveAsBorder(rightB)) {
			borderMap.addObject(rightB, right ? new PanelBorder(rightB) : new WhiteBorder(rightB));
		}
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
	
	public PanelBorder getFirstPanelInDirection(Tile tile, Direction dir) {
		boolean found = false;
		Position currPos = tile.getPosition();
		while (!found) {
			BorderPosition pos = dir.getBorderPositionInDirection(currPos);
			try {
				Border border = null;
				border = borderMap.getObjectAtId(pos);
				if (border instanceof PanelBorder) {
					return (PanelBorder)border;
				}
			} catch (IllegalArgumentException e) {
				return null;
			}
			currPos = dir.getPositionInDirection(currPos);
		}
		return null;
	}
	
	public boolean canHaveAsTile(Position pos) {
		return !tileMap.hasId(pos);
	}
	
	public void addBorder(Border border) {
		if (!borderMap.hasId(border.getBorderPos()) || borderMap.getObjectAtId(border.getBorderPos()) instanceof UnsureBorder) {
			borderMap.overWrite(border.getBorderPos(), border);
		}else {
			if (!canHaveAsBorder(border.getBorderPos()))
				throw new IllegalArgumentException("given border position already used " + border.getBorderPos());
			borderMap.addObject(border.getBorderPos(), border);
		}
	}
	
	public void addBorders(Collection<Border> borders) {
		for (Border border : borders) {
			if (canHaveAsBorder(border.getBorderPos())) {
				borderMap.addObject(border.getBorderPos(), border);
			}
		}
	}
	
	public boolean canHaveAsBorder(BorderPosition pos) {
		if (!borderMap.hasId(pos))
			return true;
		return (borderMap.getObjectAtId(pos) instanceof UnsureBorder);
	}

	public ObjectMap<Position, Tile> getTileMap() {
		synchronized(tileMap) {
			return tileMap;
		}
	}

	public ObjectMap<BorderPosition, Border> getBorderMap() {
		return borderMap;
	}
	
	public static Position convertToTilePosition(double xpos, double ypos) {
		int xsign = xpos < 0? -1:1;
		int ysign = ypos < 0? -1:1;
		int x = (int)(Math.abs(xpos) + (TILE_SIZE / 2)) / TILE_SIZE;
		int y = (int)(Math.abs(ypos) + (TILE_SIZE / 2)) / TILE_SIZE;
		return new Position(x*xsign, y*ysign);
	}
	
	public Tile getCurrentTile(double xpos, double ypos)
			throws IllegalArgumentException {
		Position tilePos = convertToTilePosition(xpos, ypos);
		return tileMap.getObjectAtId(tilePos);
	}
	
	public static double[] convertToInTilePos(double[] pos) {
		double[] ret = new double[2];
		int xsign = pos[0] < 0? -1:1;
		int ysign = pos[1] < 0? -1:1;
		ret[0] = (Math.abs(pos[0]) + TILE_SIZE / 2) % TILE_SIZE;
		ret[1] = (Math.abs(pos[1]) + TILE_SIZE / 2) % TILE_SIZE;
		ret[0] -= TILE_SIZE / 2;
		ret[1] -= TILE_SIZE / 2;
		ret[0] *= xsign;
		ret[1] *= ysign;
		return ret;
	}
	
	public Border getBorderOfPos(Tile tile, double xpos, double ypos) {
		Border border = null;
		double[] pass = {xpos, ypos};
		double[] pos = convertToInTilePos(pass);
		double x = pos[0];
		double y = pos[1];
		if (y <= (-TILE_SIZE/2) + BORDER_SIZE) {
			border = getBottomBorderOfTile(tile);
		} else if (y >= (TILE_SIZE/2) - BORDER_SIZE) {
			border = getTopBorderOfTile(tile);
		} else if (x <= (-TILE_SIZE/2) + BORDER_SIZE) {
			border = getLeftBorderOfTile(tile);
		} else if (x >= (TILE_SIZE/2) - BORDER_SIZE) {
			border = getRightBorderOfTile(tile);
		}
		return border;
	}
	
	public boolean collidesWithBorder(double xpos, double ypos)
			throws IllegalArgumentException {
		Tile tile = getCurrentTile(xpos, ypos);
		Border border = getBorderOfPos(tile, xpos, ypos);
		if (border != null) {
			//System.out.println("border " + border.toString() + " pass " + border.isPassable());
			return !border.isPassable();
		}
		
		return false;
	}
	
	public boolean isOnWhiteBorder(double xpos, double ypos) {
		Tile tile = getCurrentTile(xpos, ypos);
		Border border = getBorderOfPos(tile, xpos, ypos);
		if (border != null) {
			return border instanceof WhiteBorder;
		}
		
		return false;
	}
	
	public boolean isOnBarcode(double xpos, double ypos) {
		Tile tile = getCurrentTile(xpos, ypos);
		if (tile.getBarcode() == null)
			return false;
		double[] pass = {xpos, ypos};
		double[] pos = convertToInTilePos(pass);
		int i = 0;
		if (getLeftBorderOfTile(tile) instanceof PanelBorder) {
			i = 1;
		}
		if (pos[i] > -12 && pos[0] < 12)
			return true;
		return false;
	}
	
	public boolean isOnBlack(double xpos, double ypos) {
		Tile tile = getCurrentTile(xpos, ypos);
		if (tile.getBarcode() == null)
			return false;
		Barcode bar = tile.getBarcode();
		double[] pass = {xpos, ypos};
		double[] pos = convertToInTilePos(pass);
		int i = 0;
		if (getLeftBorderOfTile(tile) instanceof PanelBorder) {
			i = 1;
		}
		int codeNr = (int)((pos[i] + 12) / 3);
		if (codeNr <= 0 || codeNr >= 7)
			return true;
		if (bar.getCode()[codeNr - 1] == 0)
			return true;
		return false;
	}
	
	public int distanceFromPanel(double x, double y, Direction dir) {
		Tile tile = getCurrentTile(x, y);
		Border border = getFirstPanelInDirection(tile, dir);
		if (border != null) {
			double[] pass = {x, y};
			double[] pos = convertToInTilePos(pass);
			int dist = dir.getDistance(pos[0], pos[1], tile, border);
			return dist-2;
		} else {
			return 9999;
		}
	}
	
	public List<Tile> getPassableNeighbours(Tile tile) {
		List<Tile> ret = new ArrayList<Tile>();
		
		for (Direction dir : Direction.values()) {
			BorderPosition pos;
			pos = dir.getBorderPositionInDirection(tile.getPosition());
			if (borderMap.hasId(pos) && borderMap.getObjectAtId(pos).isPassable()) {
				Position tpos = pos.getOtherPosition(tile.getPosition());
				if (tileMap.hasId(tpos)) {
					ret.add(tileMap.getObjectAtId(tpos));
				}
			}
		}
		
		
		
		return ret;
	}
	
	public boolean isExplored(Position pos) {
		return getBorderMap().hasId(Direction.TOP.getBorderPositionInDirection(pos)) && 
				getBorderMap().hasId(Direction.BOTTOM.getBorderPositionInDirection(pos)) && 
				getBorderMap().hasId(Direction.RIGHT.getBorderPositionInDirection(pos)) && 
				getBorderMap().hasId(Direction.LEFT.getBorderPositionInDirection(pos));
	}
	
	public boolean isSure(Position pos) {
		return isExplored(pos) &&
				!(getBorderInDirection(getTileMap().getObjectAtId(pos), Direction.TOP) instanceof UnsureBorder) &&
				!(getBorderInDirection(getTileMap().getObjectAtId(pos), Direction.BOTTOM) instanceof UnsureBorder) &&
				!(getBorderInDirection(getTileMap().getObjectAtId(pos), Direction.LEFT) instanceof UnsureBorder) &&
				!(getBorderInDirection(getTileMap().getObjectAtId(pos), Direction.RIGHT) instanceof UnsureBorder);
	}
	
}
