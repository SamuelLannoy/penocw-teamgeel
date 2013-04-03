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
	
	public static TilePosition convertToTilePosition(double xpos, double ypos) {
		int xsign = xpos < 0? -1:1;
		int ysign = ypos < 0? -1:1;
		int x = (int)(Math.abs(xpos) + (TILE_SIZE / 2)) / TILE_SIZE;
		int y = (int)(Math.abs(ypos) + (TILE_SIZE / 2)) / TILE_SIZE;
		return new TilePosition(x*xsign, y*ysign);
	}
	
	public Tile getCurrentTile(double xpos, double ypos)
			throws IllegalArgumentException {
		TilePosition tilePos = convertToTilePosition(xpos, ypos);
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
	
	public boolean collidesWithBorder(double[][] corners)
			throws IllegalArgumentException {
		for (int i = 0; i < 4; i++) {
			Tile tile;
			try {
				tile = getCurrentTile(corners[i][0], corners[i][1]);
			} catch (IllegalArgumentException e) {
				return true;
			}
			Border border = getBorderOfPos(tile, corners[i][0], corners[i][1]);
			//DebugBuffer.addInfo("x= " + corners[i][0] + " y= " + corners[i][1] + "border = " + border);
			if (border != null && !border.isPassable()) {
				//System.out.println("border " + border.toString() + " pass " + border.isPassable());
				return !border.isPassable();
			}
		}
		
		return false;
	}
	
	public boolean collidesWithBall(double[][] corners) {
		for (int i = 0; i < 2; i++) {
			Tile tile;
			try {
				tile = getCurrentTile(corners[i][0], corners[i][1]);
			} catch (IllegalArgumentException e) {
				return true;
			}
			if (ballMap.hasId(tile.getPosition())) {
				Border border = getBorderOfPos(tile, corners[i][0], corners[i][1]);
				if (border != null && !border.isPassable()) {
					ballMap.removeObjectAtId(tile.getPosition());
					DebugBuffer.addInfo("robot collected ball");
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isOnWhiteBorder(double xpos, double ypos) {
		Tile tile = getCurrentTile(xpos, ypos);
		Border border = getBorderOfPos(tile, xpos, ypos);
		if (border != null) {
			return border instanceof WhiteBorder || border instanceof SeesawBorder;
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
		if (pos[i] > -12 && pos[i] < 12)
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
		SolidBorder border = getFirstPanelInDirection(tile, dir);
		if (border != null) {
			double[] pass = {x, y};
			double[] pos = convertToInTilePos(pass);
			int dist = dir.getDistance(pos[0], pos[1], tile, border.getBorderPos());
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
	
	public void updateField(RobotPool robotPool) {
		for (RobotModel model : robotPool.getRobots()) {
			if (getStartPos(model.getPlayerNr()) != null) {
				Tile tile = getCurrentTile(model.getCurrTile().getPosition().getX() * 40
						+ model.getPosition().getPosX()
						+ getStartPos(model.getPlayerNr()).getX() * 40,
						model.getCurrTile().getPosition().getY() * 40
						+ model.getPosition().getPosY()
						+ getStartPos(model.getPlayerNr()).getY() * 40);
				if (isSeesawTile(tile)) {
					SeesawBorder border = getSeesawBorder(tile);
					if (!border.isPassable()) {
						border.setDown();
						DebugBuffer.addInfo("SETTING DOWN");
					}
				}
			}
		}
	}
	
	public void makeUnsure(BorderPosition borderPos) {
		if (borderMap.hasId(borderPos)) {
			borderMap.overWrite(borderPos, new UnsureBorder(borderPos));
		}
	}
	
	private Map<Integer, TilePosition> startPos = new HashMap<Integer, TilePosition>();
	private Map<Integer, Direction> startDir = new HashMap<Integer, Direction>();
	
	public void setStartPos(int i, TilePosition pos) {
		startPos.put(i, pos);
		DebugBuffer.addInfo("adding " + pos  + " to " + i);
	}
	
	public void setStartDir(int i, Direction dir) {
		startDir.put(i, dir);
	}
	
	public TilePosition getStartPos(int i) {
		return startPos.get(i);
	}
	
	public Direction getStartDir(int i) {
		return startDir.get(i);
	}
	
	public peno.htttp.Tile toTileMessage(TilePosition pos) {
		return null;
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
