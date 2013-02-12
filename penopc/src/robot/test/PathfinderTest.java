package robot.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import robot.*;
import robot.brain.Pathfinder;

import field.*;
import field.fromfile.FieldFactory;

public class PathfinderTest {
	private static Field maze;
	private static Robot robot;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		maze = FieldFactory.fieldFromFile("C:\\demo2.txt");
		robot = new Robot(1);
		robot.initialize();
		robot.setField(maze);
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
	public void test1() {
		System.out.println(Pathfinder.findShortestPath(robot, new Tile(3, 3)));
	}

}
