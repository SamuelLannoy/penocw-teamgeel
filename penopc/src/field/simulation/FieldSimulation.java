package field.simulation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import robot.DebugBuffer;
import robot.RobotModel;
import robot.RobotPool;

import field.*;
import field.fromfile.MazePart;

public class FieldSimulation extends Field {

	private RobotPool robotPool;
	
	public FieldSimulation(String path) {
		this(null, path);
	}
	
	public FieldSimulation(RobotPool robotPool, String path) {
		setRobotPool(robotPool);
		try {
			initialize(path);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setRobotPool(RobotPool robotPool) {
		this.robotPool = robotPool;
	}

	public void initialize(String path) throws NumberFormatException, IOException {
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int lineNr = 0;
		int dimX = 0;
		int dimY = 0;	
		int currY = 0;		
		while ((strLine = br.readLine()) != null)   {
			//System.out.println(""+lineNr);
			if (lineNr == 0) {
				String[] dim = strLine.split("( |\t)+");
				dimX = Integer.parseInt(dim[0]);
				dimY = Integer.parseInt(dim[1]);	
				currY = dimY-1;
			} else {
				if (!strLine.isEmpty() && !strLine.startsWith("#")) {
					String[] sections = strLine.split("( |\t)+");
					for (int i = 0; i < dimX; i++) {
						//System.out.println("line: "+sections[i]);
						String[] parts = sections[i].split("\\.");
						//System.out.println("section: " + sections[i] + " parts " + parts.length);
						MazePart part = MazePart.getPartFromString(parts[0]);
						Tile tile = new Tile(i, currY);
						//System.out.println("x: " + i + " y: " + currY);
						addTile(tile);
						if (parts.length >= 3 && !parts[2].isEmpty()) {
							if (parts[2].equals("V")) {
								addBall(new Ball(1), tile.getPosition());
							} else if (parts[2].startsWith("S")) {
								String start = parts[2];
								int id = Integer.parseInt(start.substring(1, 2));
								setStartPos(id, tile.getPosition());
								String dir = start.substring(2, 3);
								setStartDir(id, Direction.fromString(dir));
							} else {
								tile.setBarcode(new Barcode(Integer.parseInt(parts[2])));
							}
						}
						if (parts.length == 2) {
							if (parts[1].startsWith("S") && parts[1].length() > 1) {
								String start = parts[1];
								//System.out.println("tt " + start);
								int id = Integer.parseInt(start.substring(1, 2));
								setStartPos(id, tile.getPosition());
								String dir = start.substring(2, 3);
								setStartDir(id, Direction.fromString(dir));
							}
						}
						String param = parts.length >= 2 ? parts[1] : "";
						List<Border> borders = part.getBorders(param, tile);
						//System.out.println("" + borders + " param: " + param);
						addBorders(borders);
					}
					currY--;
				}
			}
			lineNr++;
			
		}
		in.close();
		
		for (Tile tile : tileMap) {
			//DebugBuffer.addInfo("a " + tile.getPosition() + " " + hasSeesawBorder(tile) + " " + tile.getBarcode());
			if (hasSeesawBorder(tile) && tile.getBarcode() != null) {
				SeesawBorder border = getSeesawBorder(tile);
				//DebugBuffer.addInfo("b " + tile.getPosition());
				Barcode barcode = tile.getBarcode();
				if (barcode.isSeesawDownCode()) {
					//DebugBuffer.addInfo("c " + tile.getPosition());
					border.setDown();
				} else if (barcode.isSeesawUpCode()) {
					border.setUp();
				}
			}
		}
	}
	
	public void update() {
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
	
	
	
	/*
	 * Simulation methods
	 */
	
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
	
	
	
	/*
	 * Starting position / direction
	 */
	
	private Map<Integer, TilePosition> startPos = new HashMap<Integer, TilePosition>();
	private Map<Integer, Direction> startDir = new HashMap<Integer, Direction>();
	
	public void setStartPos(int i, TilePosition pos) {
		startPos.put(i, pos);
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
	
}
