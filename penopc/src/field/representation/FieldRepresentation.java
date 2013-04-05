package field.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import messenger.PenoHtttpTeamCommunicator;


import field.*;
import field.fieldmerge.BarcodeNode;
import field.fromfile.MazePart;

public class FieldRepresentation extends Field {
	public FieldRepresentation() {
		super();
	}
	
	public void initialize() {
		addTile(TilePosition.POSITION_ZERO);
	}
	
	private boolean teamMateMode = false;
	
	private PenoHtttpTeamCommunicator comm;
	
	public void foundTeamMate(PenoHtttpTeamCommunicator comm) {
		teamMateMode = true;
		this.comm = comm;
		
	}
	
	/*
	 * Register methods when discovering the maze
	 */
	
	public void registerNewTile(Direction discoverDirection, TilePosition discoveredFromPosition) {
		TilePosition newTilePosition = discoverDirection.getPositionInDirection(discoveredFromPosition);
		addTile(newTilePosition);
		
		registerBorder(discoveredFromPosition, discoverDirection, WhiteBorder.class);
		
		if (teamMateMode) {
			comm.sendTiles(convertToTileMsg(newTilePosition));
		}
	}
	
	public void registerSeesaw(TilePosition barcodePosition, Direction directionOfSeesaw) {
		Direction left = directionOfSeesaw.left();
		Direction right = directionOfSeesaw.right();
		
		if (getBorderInDirection(getTileAt(barcodePosition), directionOfSeesaw) instanceof WhiteBorder) {
			SeesawBorder firstSeesawBorder = new SeesawBorder(getTileAt(barcodePosition), directionOfSeesaw);
			overWriteBorder(firstSeesawBorder);
		}

		//2de tegel wip toevoegen
		TilePosition secondTilePosition = directionOfSeesaw.getPositionInDirection(barcodePosition);
		System.out.println("adding " + secondTilePosition);
		addTile(secondTilePosition);
		registerBorder(secondTilePosition, directionOfSeesaw, WhiteBorder.class);
		registerBorder(secondTilePosition, left, PanelBorder.class);
		registerBorder(secondTilePosition, right, PanelBorder.class);

		//3de tegel wip toevoegen
		TilePosition thirdTilePosition = directionOfSeesaw.getPositionInDirection(secondTilePosition);
		System.out.println("adding " + thirdTilePosition);
		addTile(thirdTilePosition);
		registerBorder(thirdTilePosition, directionOfSeesaw, SeesawBorder.class);
		registerBorder(thirdTilePosition, left, PanelBorder.class);
		registerBorder(thirdTilePosition, right, PanelBorder.class);

		//4de tegel wip toevoegen
		TilePosition fourthTilePosition = directionOfSeesaw.getPositionInDirection(thirdTilePosition);
		System.out.println("adding " + fourthTilePosition);
		addTile(fourthTilePosition);
		Barcode otherSeesawBarcode = getTileAt(barcodePosition).getBarcode().otherSeesawBarcode();
		registerBarcode(fourthTilePosition, otherSeesawBarcode, directionOfSeesaw);
		
		//5de tegel wip toevoegen
		TilePosition fifthTilePosition = directionOfSeesaw.getPositionInDirection(fourthTilePosition);
		System.out.println("adding " + fifthTilePosition);
		addTile(fifthTilePosition);
		
		if (teamMateMode) {
			comm.sendTiles(convertToTileMsg(barcodePosition),
					convertToTileMsg(secondTilePosition),
					convertToTileMsg(thirdTilePosition),
					convertToTileMsg(fourthTilePosition),
					convertToTileMsg(fifthTilePosition));
		}
	}
	
	public void registerSeesawPosition(TilePosition barcodePosition, Direction directionOfSeesaw, boolean standardPosition) {
		Barcode bc = getTileAt(barcodePosition).getBarcode();
		SeesawBorder firstSeesawBorder = getSeesawBorder(getTileAt(barcodePosition));
		TilePosition firstSeesawTilePos = directionOfSeesaw.getPositionInDirection(barcodePosition);
		TilePosition secondSeesawTilePos = directionOfSeesaw.getPositionInDirection(firstSeesawTilePos);
		SeesawBorder secondSeesawBorder = getSeesawBorder(getTileAt(secondSeesawTilePos));
		if (bc.isSeesawDownCode()) {
			if (standardPosition) {
				firstSeesawBorder.setDown();
				secondSeesawBorder.setUp();
			} else {
				firstSeesawBorder.setUp();
				secondSeesawBorder.setDown();
			}
		} else if (bc.isSeesawUpCode()) {
			if (standardPosition) {
				firstSeesawBorder.setUp();
				secondSeesawBorder.setDown();
			} else {
				firstSeesawBorder.setDown();
				secondSeesawBorder.setUp();
			}
		}
		
	}
	
	public void registerBall(TilePosition barcodePosition, Direction directionOfObject) {
		Direction left = directionOfObject.left();
		Direction right = directionOfObject.right();
		TilePosition objectTilePosition = directionOfObject.getPositionInDirection(barcodePosition);
		addTile(objectTilePosition);
		registerBorder(objectTilePosition, directionOfObject, PanelBorder.class);
		registerBorder(objectTilePosition, left, PanelBorder.class);
		registerBorder(objectTilePosition, right, PanelBorder.class);

		if (teamMateMode) {
			comm.sendTiles(convertToTileMsg(barcodePosition),
					convertToTileMsg(objectTilePosition));
		}
	}
	
	public void registerBorder(TilePosition tilePosition, Direction directionOfBorder, Class<? extends Border> borderType) {
		Border newBorder = null;
		try {
			newBorder = borderType.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		BorderPosition newBorderPos = directionOfBorder.getBorderPositionInDirection(tilePosition);
		newBorder.setBorderPos(newBorderPos);
		if (hasBorderAt(newBorderPos)) {
			if (getBorderAt(newBorderPos) instanceof UnsureBorder) {
				overWriteBorder(newBorder);
			}
		} else {
			addBorder(newBorder);
		}
		if (teamMateMode) {
			comm.sendTiles(convertToTileMsg(tilePosition));
		}
	}
	
	public void registerBarcode(TilePosition barcodePosition, Barcode discoveredBarcode, Direction directionOfBarcode) {
		registerBarcode(barcodePosition, directionOfBarcode);
		Tile barcodeTile = getTileAt(barcodePosition);
		barcodeTile.setBarcode(discoveredBarcode);
	}
	
	public void registerBarcode(TilePosition barcodePosition, Direction directionOfBarcode) {
		Direction left = directionOfBarcode.left();
		Direction right = directionOfBarcode.right();

		registerBorder(barcodePosition, directionOfBarcode, WhiteBorder.class);
		registerBorder(barcodePosition, left, PanelBorder.class);
		registerBorder(barcodePosition, right, PanelBorder.class);
		
		TilePosition nextTilePos = directionOfBarcode.getPositionInDirection(barcodePosition);
		addTile(nextTilePos);
		

		if (teamMateMode) {
			comm.sendTiles(convertToTileMsg(barcodePosition));
		}
	}
	
	
	
	public void clearRepresentation() {
		tileMap = new ObjectMap<TilePosition, Tile>();
		borderMap = new ObjectMap<BorderPosition, Border>();
		ballMap = new ObjectMap<TilePosition, Ball>();
		initialize();
	}
	
	/**
	 * returns true when this tile has a border defined in every direction
	 * @param pos
	 * @return
	 */
	public boolean isExplored(TilePosition pos) {
		return hasBorderAt(Direction.TOP.getBorderPositionInDirection(pos)) && 
				hasBorderAt(Direction.BOTTOM.getBorderPositionInDirection(pos)) && 
				hasBorderAt(Direction.RIGHT.getBorderPositionInDirection(pos)) && 
				hasBorderAt(Direction.LEFT.getBorderPositionInDirection(pos));
	}
	
	/**
	 * true when no gray borders are on this tile
	 * @param pos
	 * @return
	 */
	public boolean isSure(TilePosition pos) {
		return isExplored(pos) &&
				!(getBorderInDirection(getTileAt(pos), Direction.TOP) instanceof UnsureBorder) &&
				!(getBorderInDirection(getTileAt(pos), Direction.BOTTOM) instanceof UnsureBorder) &&
				!(getBorderInDirection(getTileAt(pos), Direction.LEFT) instanceof UnsureBorder) &&
				!(getBorderInDirection(getTileAt(pos), Direction.RIGHT) instanceof UnsureBorder);
	}
	
	public boolean hasBackBorder(TilePosition tilePos, Direction backDirection) {
		return hasBorderAt(backDirection.getBorderPositionInDirection(tilePos));
	}

	/*
	 * get methods for A*
	 */
	
	public List<Tile> getPassableNeighbours(Tile tile) {
		List<Tile> ret = new ArrayList<Tile>();
		
		for (Direction dir : Direction.values()) {
			BorderPosition pos = dir.getBorderPositionInDirection(tile.getPosition());
			if (hasBorderAt(pos) &&
					(getBorderAt(pos).isPassable() || getBorderAt(pos) instanceof SeesawBorder)) {
				TilePosition tpos = pos.getOtherPosition(tile.getPosition());
				if (hasTileAt(tpos)) {
					ret.add(getTileAt(tpos));
				}
			}
		}
		
		return ret;
	}
	
	/*
	 * Translation and rotation methods for field merging
	 */
	
	private FieldRepresentation moveX(int x) {
		FieldRepresentation retF = new FieldRepresentation();
		for (Tile tile : tileMap) {
			retF.addTile(tile.moveX(x));
		}
		for (Border border : borderMap) {
			retF.addBorder(border.moveX(x));
		}
		return retF;
	}
	
	private FieldRepresentation moveY(int y) {
		FieldRepresentation retF = new FieldRepresentation();
		for (Tile tile : tileMap) {
			retF.addTile(tile.moveY(y));
		}
		for (Border border : borderMap) {
			retF.addBorder(border.moveY(y));
		}
		return retF;
	}
	
	private FieldRepresentation rotate(int rotation, TilePosition pos) {
		FieldRepresentation retF = new FieldRepresentation();
		for (Tile tile : tileMap) {
			retF.addTile(tile.rotate(rotation, pos));
		}
		for (Border border : borderMap) {
			retF.addBorder(border.rotate(rotation, pos));
		}
		return retF;
	}
	
	/*
	 * Communication methods
	 */
	
	public FieldRepresentation(Collection<peno.htttp.Tile> tileList) {
		super();
		addFromComm(tileList);
	}
	
	public void addFromTeammate(Collection<peno.htttp.Tile> tileList) {
		addFromComm(tileList);
	}
	
	private void addFromComm(Collection<peno.htttp.Tile> tileList) {
		for (peno.htttp.Tile tileMsg : tileList) {
			String[] split = tileMsg.getToken().split("\\.");
			MazePart part = MazePart.getPartFromString(split[0]);
			
			TilePosition tilePos = new TilePosition((int)tileMsg.getX(), (int)tileMsg.getY());
			Tile added = null;
			if (!hasTileAt(tilePos)){
				added = new Tile(tilePos);
				addTile(added);
			} else {
				added = getTileAt(tilePos);
			}
			
			if (split.length >= 3) {
				int nr = Integer.parseInt(split[2]);
				added.setBarcode(new Barcode(nr));
			}
			
			Collection<Border> borders = part.getBorders(split.length >= 2 ? split[1] : "", added);

			for (Border border : borders) {
				if (border instanceof SeesawBorder) {
					overWriteBorder(border);
				} else {
					addBorder(border);
				}
			}
		}
	}
	
	public void mergeFields(FieldRepresentation field2) {
		List<BarcodeNode> bc1 = getBarcodes();
		List<BarcodeNode> bc2 = field2.getBarcodes();
		
		bc1.retainAll(bc2);
		bc2.retainAll(bc1);

		if (bc1.size() >= 2) {
			BarcodeNode barcodeNode1 = bc1.get(0);
			System.out.println("first " + barcodeNode1);
			
			for (BarcodeNode barcodeNode2 : bc2) {
				if (barcodeNode1.equals(barcodeNode2)) {
					System.out.println("first " + barcodeNode2);
					int diffX = barcodeNode1.getPosition().getX() - barcodeNode2.getPosition().getX();
					int diffY = barcodeNode1.getPosition().getY() - barcodeNode2.getPosition().getY();
					field2 = field2.moveX(diffX);
					field2 = field2.moveY(diffY);
					setTranslX(diffX);
					setTranslY(diffY);
				}
			}
			
			bc2 = field2.getBarcodes();
			bc2.retainAll(bc1);
			
			BarcodeNode barcodeNode1_2 = bc1.get(1);
			System.out.println("second " + barcodeNode1_2);

			for (BarcodeNode barcodeNode2 : bc2) {
				if (barcodeNode1_2.equals(barcodeNode2)) {
					System.out.println("second " + barcodeNode2);
					double P12 = TilePosition.euclDistance(barcodeNode1.getPosition(), barcodeNode1_2.getPosition());
					System.out.println("rot " + P12);
					double P13 = TilePosition.euclDistance(barcodeNode1.getPosition(), barcodeNode2.getPosition());
					System.out.println("rot " + P13);
					double P23 = TilePosition.euclDistance(barcodeNode1_2.getPosition(), barcodeNode2.getPosition());
					System.out.println("rot " + P23 + " " + barcodeNode1_2.getPosition() + " " + barcodeNode2.getPosition());
					int rotation = (int)(Math.acos((Math.pow(P12, 2) + Math.pow(P13, 2) - Math.pow(P23, 2)) / (2 * P12 * P13)) / Math.PI * 180 + .5);
					System.out.println("rot " + rotation);
					field2 = field2.rotate(rotation, barcodeNode1.getPosition());
					setRotation(rotation);
				}
			}
			

			for (Tile tile : field2.tileMap) {
				if (!hasTileAt(tile.getPosition())) {
					addTile(tile);
					System.out.println("adding " + tile.getPosition());
				}
			}
			
			for (Border border : field2.borderMap) {
				if (!hasBorderAt(border.getBorderPos())) {
					addBorder(border);
					System.out.println("adding " + border.getBorderPos());
				} else {
					if (getBorderAt(border.getBorderPos()).getClass() != border.getClass()) {
						overWriteBorder(new UnsureBorder(border.getBorderPos()));
					}
				}
			}
			
			setMerged(true);
		} else {
			throw new IllegalStateException();
		}
	}

	private List<BarcodeNode> getBarcodes() {
		List<BarcodeNode> ret = new ArrayList<BarcodeNode>(tileMap.getObjectCollection().size() / 5);
		for (Tile tile : tileMap) {
			if (tile.getBarcode() != null) {
				ret.add(new BarcodeNode(tile.getBarcode(), tile.getPosition()));
			}
		}
		return ret;
	}
	
	public Collection<peno.htttp.Tile> convertToMessage() {
		Collection<peno.htttp.Tile> tilesMsg = new ArrayList<peno.htttp.Tile>(tileMap.getKeys().size());
		for (Tile tile : tileMap) {
			peno.htttp.Tile toadd = convertToTileMsg(tile.getPosition());
			if (toadd != null)
				tilesMsg.add(toadd);
		}
		return tilesMsg;
	}
	
	private peno.htttp.Tile convertToTileMsg(TilePosition tilePos) {
		Map<Direction, Border> borders = new HashMap<Direction, Border>();
		Tile tile = getTileAt(tilePos);

		try {
			borders.put(Direction.TOP, getTopBorderOfTile(tile));
			borders.put(Direction.BOTTOM, getBottomBorderOfTile(tile));
			borders.put(Direction.LEFT, getLeftBorderOfTile(tile));
			borders.put(Direction.RIGHT, getRightBorderOfTile(tile));

			String sentToken = MazePart.getToken(borders, tile);
			System.out.println("sent: " + sentToken);
			if (sentToken.equals(""))
				return null;

			return new peno.htttp.Tile(tile.getPosition().getX(),
					tile.getPosition().getY(),
					sentToken);

		} catch (IllegalArgumentException e) {


			return new peno.htttp.Tile(tile.getPosition().getX(),
					tile.getPosition().getY(),
					"unknown");
		}
	}

	/*
	 * Keep track of translation/rotation to map teammate field to ours
	 */
	
	private boolean isMerged;
	
	private int translX;
	
	private int translY;
	
	private float rotation;

	public boolean isMerged() {
		return isMerged;
	}

	public void setMerged(boolean isMerged) {
		this.isMerged = isMerged;
	}

	public int getTranslX() {
		return translX;
	}

	public void setTranslX(int translX) {
		this.translX = translX;
	}

	public int getTranslY() {
		return translY;
	}

	public void setTranslY(int translY) {
		this.translY = translY;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
}
