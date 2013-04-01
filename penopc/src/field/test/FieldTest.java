package field.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import field.BorderPosition;
import field.Direction;
import field.Field;
import field.PanelBorder;
import field.Position;
import field.SolidBorder;
import field.Tile;
import field.UnsureBorder;
import field.WhiteBorder;
import field.fieldmerge.FieldMerger;
import field.fromfile.FieldFactory;

public class FieldTest {

	Field maze;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		maze = new Field();
		maze.addTile(new Tile(0, 0));
		maze.addTile(new Tile(0, 1));
		maze.addTile(new Tile(1, 0));
		maze.addTile(new Tile(1, 1));
		maze.addBorder(new PanelBorder(0, 0, 0, 1));
		maze.addBorder(new WhiteBorder(0, 0, 1, 0));
		maze.addBorder(new WhiteBorder(1, 0, 1, 1));
		maze.addBorder(new WhiteBorder(0, 1, 1, 1));
		maze.addBorder(new PanelBorder(0, 0, 0, -1));
		maze.addBorder(new PanelBorder(0, 0, -1, 0));
		maze.addBorder(new PanelBorder(1, 0, 1, -1));
		maze.addBorder(new PanelBorder(1, 0, 2, 0));
		maze.addBorder(new PanelBorder(0, 1, 0, 2));
		maze.addBorder(new PanelBorder(0, 1, -1, 1));
		maze.addBorder(new PanelBorder(1, 1, 1, 2));
		maze.addBorder(new PanelBorder(1, 1, 2, 1));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void convertToTilePosition_normalCases(){
		Position pos = null;
		pos = Field.convertToTilePosition(0, 0);
		assertEquals(new Position(0,0), pos);
		pos = Field.convertToTilePosition(Field.TILE_SIZE, Field.TILE_SIZE);
		assertEquals(new Position(1,1), pos);
		pos = Field.convertToTilePosition(Field.TILE_SIZE, 0);
		assertEquals(new Position(1,0), pos);
		pos = Field.convertToTilePosition(0, Field.TILE_SIZE);
		assertEquals(new Position(0,1), pos);
		pos = Field.convertToTilePosition(-Field.TILE_SIZE, -Field.TILE_SIZE);
		assertEquals(new Position(-1,-1), pos);
		pos = Field.convertToTilePosition(-Field.TILE_SIZE * 2, -Field.TILE_SIZE * 2);
		assertEquals(new Position(-2,-2), pos);
	}
	
	@Test
	public void getCurrentTile_normalCases() {
		Tile tile = null;
		tile = maze.getCurrentTile(0,0);
		assertEquals(new Position(0,0), tile.getPosition());
		tile = maze.getCurrentTile(Field.TILE_SIZE + 1 ,0);
		assertEquals(new Position(1,0), tile.getPosition());
		tile = maze.getCurrentTile(0,Field.TILE_SIZE + 1);
		assertEquals(new Position(0,1), tile.getPosition());
		tile = maze.getCurrentTile(Field.TILE_SIZE + 1,Field.TILE_SIZE + 1);
		assertEquals(new Position(1,1), tile.getPosition());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void getCurrentTile_nonExistantTile() {
		@SuppressWarnings("unused")
		Tile tile = maze.getCurrentTile(-Field.TILE_SIZE,-Field.TILE_SIZE);
	}
	
	@Test
	public void convertToInTilePos_normalCase1() {
		double[] pos = {1, 1};
		double[] exRet = {1, 1};
		double[] ret = Field.convertToInTilePos(pos);
		assertEquals(exRet[0], ret[0], 0.01);
		assertEquals(exRet[1], ret[1], 0.01);
	}
	
	@Test
	public void convertToInTilePos_normalCase2() {
		double[] pos = {16, 16};
		double[] exRet = {16, 16};
		double[] ret = Field.convertToInTilePos(pos);
		assertEquals(exRet[0], ret[0], 0.01);
		assertEquals(exRet[1], ret[1], 0.01);
	}
	
	@Test
	public void convertToInTilePos_normalCase3() {
		double[] pos = {44, 44};
		double[] exRet = {4, 4};
		double[] ret = Field.convertToInTilePos(pos);
		assertEquals(exRet[0], ret[0], 0.01);
		assertEquals(exRet[1], ret[1], 0.01);
	}
	
	@Test
	public void convertToInTilePos_normalCase4() {
		double[] pos = {45, 45};
		double[] exRet = {5, 5};
		double[] ret = Field.convertToInTilePos(pos);
		assertEquals(exRet[0], ret[0], 0.01);
		assertEquals(exRet[1], ret[1], 0.01);
	}
	
	@Test
	public void getFirstPanelInDirection_normalCase1() {
		Tile tile = maze.getCurrentTile(0,0);
		SolidBorder border = maze.getFirstPanelInDirection(tile, Direction.TOP);
		assertEquals(border.getBorderPos(), new BorderPosition(new Position(0, 0), new Position(0, 1)));
	}
	
	@Test
	public void getFirstPanelInDirection_normalCase2() {
		Tile tile = maze.getCurrentTile(0,0);
		SolidBorder border = maze.getFirstPanelInDirection(tile, Direction.RIGHT);
		assertEquals(border.getBorderPos(), new BorderPosition(new Position(1, 0), new Position(2, 0)));
	}
	@Test
	public void getFirstPanelInDirection_normalCase3() {
		Tile tile = maze.getCurrentTile(0,40);
		SolidBorder border = maze.getFirstPanelInDirection(tile, Direction.BOTTOM);
		assertEquals(border.getBorderPos(), new BorderPosition(new Position(0, 0), new Position(0, 1)));
	}
	
	@Test
	public void isOnWhiteBorder_normalCase1() {
		Field mazex = new Field();
		mazex.addTile(new Tile(0, 0));
		mazex.addTile(new Tile(0, 1));
		mazex.addTile(new Tile(1, 0));
		mazex.addTile(new Tile(1, 1));
		mazex.addBorder(new WhiteBorder(0, 0, 0, 1));
		mazex.addBorder(new WhiteBorder(0, 0, 1, 0));
		mazex.addBorder(new WhiteBorder(1, 0, 1, 1));
		mazex.addBorder(new WhiteBorder(0, 1, 1, 1));
		mazex.addBorder(new PanelBorder(0, 0, 0, -1));
		mazex.addBorder(new PanelBorder(0, 0, -1, 0));
		mazex.addBorder(new PanelBorder(1, 0, 1, -1));
		mazex.addBorder(new PanelBorder(1, 0, 2, 0));
		mazex.addBorder(new PanelBorder(0, 1, 0, 2));
		mazex.addBorder(new PanelBorder(0, 1, -1, 1));
		mazex.addBorder(new PanelBorder(1, 1, 1, 2));
		mazex.addBorder(new PanelBorder(1, 1, 2, 1));
		assertTrue(mazex.isOnWhiteBorder(0, 20));
		assertTrue(mazex.isOnWhiteBorder(0, 22));
		assertTrue(mazex.isOnWhiteBorder(0, 18));
		assertTrue(!mazex.isOnWhiteBorder(0, 24));
		assertTrue(!mazex.isOnWhiteBorder(0, 17));
	}
	
	@Test
	public void addBorder_overwriting() {
		Field mazex = new Field();
		mazex.addBorder(new UnsureBorder(0, 0, 0, 1));
		mazex.addBorder(new WhiteBorder(0, 0, 0, 1));
		assertTrue(mazex.getBorderMap().getObjectAtId(new BorderPosition(new Position(0, 0), new Position(0, 1))) instanceof WhiteBorder);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addBorder_overwriting2() {
		Field mazex = new Field();
		mazex.addBorder(new WhiteBorder(0, 0, 0, 1));
		mazex.addBorder(new UnsureBorder(0, 0, 0, 1));
		assertTrue(mazex.getBorderMap().getObjectAtId(new BorderPosition(new Position(0, 0), new Position(0, 1))) instanceof WhiteBorder);
	}

	@Test
	public void rotate() {
		Tile center = new Tile(0, 0);
		Tile tile1 = new Tile(3, -1);
		Tile tile2 = new Tile(3, -1);
		
		tile1 = tile1.rotate(180, center.getPosition());
		tile2 = tile2.rotate(90, center.getPosition());
		tile2 = tile2.rotate(90, center.getPosition());
		
		assertEquals(tile1.getPosition(), tile2.getPosition());
	}

}
