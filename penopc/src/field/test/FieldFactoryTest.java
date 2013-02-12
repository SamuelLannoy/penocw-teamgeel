package field.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import field.*;
import field.fromfile.FieldFactory;

public class FieldFactoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException {
		Field field = FieldFactory.fieldFromFile("D:\\test.txt");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++){
				Position pos = new Position(i, j);
				assertTrue(field.getTileMap().hasId(pos));
				assertTrue(field.getBorderMap().hasId(new BorderPosition(pos, Direction.BOTTOM.getPositionInDirection(pos))));
				assertTrue(field.getBorderMap().hasId(new BorderPosition(pos, Direction.TOP.getPositionInDirection(pos))));
				assertTrue(field.getBorderMap().hasId(new BorderPosition(pos, Direction.RIGHT.getPositionInDirection(pos))));
				assertTrue(field.getBorderMap().hasId(new BorderPosition(pos, Direction.LEFT.getPositionInDirection(pos))));
			}
		}
	}

}
