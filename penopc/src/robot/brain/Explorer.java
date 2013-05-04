package robot.brain;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import field.*;
import field.representation.FieldRepresentation;

import robot.DebugBuffer;
import robot.Robot;

public class Explorer {
	
	private static boolean pause = false;
	
	public static void explore(final Robot robot) {
		explore(robot, new EndingCondition() {

			@Override
			public boolean isLastTile(Robot robot) {
				return false;
			}

			@Override
			public boolean checkEveryTile() {
				return false;
			}
			
		});
	}
	
	public static void exploreTillObjectFound(final Robot robot) {
		explore(robot, new EndingCondition() {

			@Override
			public boolean isLastTile(Robot robot) {
				return robot.hasBall();
			}

			@Override
			public boolean checkEveryTile() {
				return false;
			}
			
		});
	}
	
	private static LinkedList<TilePosition> toExplore = new LinkedList<TilePosition>();
	
	
	public static Collection<TilePosition> getToExplore() {
		return toExplore;
	}
	
	private static TilePosition current;


	private static boolean otherReachable = false;
	
	public static TilePosition recalcExplore(Robot robot, TilePosition current, Collection<Integer> ignoredSeesaws) {
		setOtherReachable(false);
		toExplore.add(current);
		robot.getToExplore().add(current);
		
		sortExplore(robot, ignoredSeesaws);
		
		current = toExplore.removeFirst();
		robot.getToExplore().remove(current);
		return current;
	}
	
	public static void sortExplore(final Robot robot) {
		sortExplore(robot, Collections.<Integer>emptyList());
	}
	
	public static void sortExplore(final Robot robot, final Collection<Integer> ignoredSeesaws) {
		// sort tiles on a* length
		Collections.sort(toExplore, new Comparator<TilePosition>() {
			@Override
			public int compare(TilePosition arg0, TilePosition arg1) {
				int mh1, mh2;
				List<Tile> list1, list2;
				int t1 = 0, t2 = 0;
				try {
					list1 = Pathfinder.findShortestPath(robot, robot.getField().getTileAt(arg0),
							ignoredSeesaws, robot.getRobotSpottedTiles());
					t1 = Pathfinder.getLastT();
					mh1 = list1.size();
					Explorer.setOtherReachable(true);
				} catch (IllegalArgumentException e) {
					mh1 = Integer.MAX_VALUE;
				}
				try {
					list2 = Pathfinder.findShortestPath(robot, robot.getField().getTileAt(arg1),
							ignoredSeesaws, robot.getRobotSpottedTiles());
					t2 = Pathfinder.getLastT();
					mh2 = list2.size();
					Explorer.setOtherReachable(true);
				} catch (IllegalArgumentException e) {
					mh2 = Integer.MAX_VALUE;
				}
				//int mh1 = arg0.getTile().getPosition().manhattanDistance(robot.getCurrTile().getPosition());
				//int mh2 = arg1.getTile().getPosition().manhattanDistance(robot.getCurrTile().getPosition());
				if (mh1 < mh2) return -1;
				if (mh1 > mh2) return 1;
				if (t1 < t2) return -1;
				if (t1 > t2) return 1;
				return 0;
			}
			
		});
	}
	
	public static void clear(Robot robot) {
		toExplore.clear();
		robot.getToExplore().clear();
	}
	
	public static void setExploreTiles(Robot robot) {
		// add all tiles that have gray borders or that need to be explored
		Iterator<Tile> it = robot.getField().tileIterator();
		while (it.hasNext()) {
			Tile tile = it.next();
			if (!robot.getField().isSure(tile.getPosition())
					&& !robot.getField().isExplored(tile.getPosition())) {
				TilePosition node = tile.getPosition();
				toExplore.add(node);
				robot.getToExplore().add(node);
			}
		}
	}
	
	public static void explore(Robot robot, EndingCondition endCond) {
		
		FieldRepresentation field = robot.getField();
		// list of nodes that need to be explored
		toExplore = new LinkedList<TilePosition>();
		// list of tiles that are explored
		HashSet<TilePosition> explored = new HashSet<TilePosition>();
		// first explore tile defined
		TilePosition init = robot.getCurrTile().getPosition();
		// add to toexplore list
		toExplore.add(init);

		setExploreTiles(robot);
		
		// main loop : stop when explore list or last tile has been met due to ending condition
		while (!toExplore.isEmpty() && !endCond.isLastTile(robot)) {
			// pop first
			current = toExplore.removeFirst();
			robot.getToExplore().remove(current);
			
			
			boolean quit = false;
			// TODO: do not remove if straight tile
			while  (robot.getField().isSure(current)) {
				if (toExplore.size() > 0) {
					//DebugBuffer.addInfo("exploring " + current);
					current = toExplore.removeFirst();
					robot.getToExplore().remove(current);
					explored.add(current);
				} else {
					quit = true;
					break;
				}
			}
			if (quit)
				break;
			//DebugBuffer.addInfo("explore " + current.getTile().getPosition());
			
			
			robot.goToTile(current, endCond);
			
			Collection<TilePosition> toAdd = robot.exploreTile();
			
			for (TilePosition pos : toAdd) {
				if (!field.isExplored(pos) && !explored.contains(pos) && !toExplore.contains(pos)) {
					toExplore.add(pos);
					robot.getToExplore().add(pos);
				}
			}
			
			//sortExplore(robot);
			
			
			//DebugBuffer.addInfo("list " + toExplore);
			// reset barcode values
			robot.hasCorrectBarcode();
			robot.hasWrongBarcode();
			
			// if pause was selected wait here
			while (pause) {
				
			}
		}

		robot.getToExplore().clear();
		DebugBuffer.addInfo("finish");
	}
	
	public static boolean isPaused() {
		return pause;
	}
	
	public static void pause() {
		pause = true;
	}
	
	public static void resume() {
		pause = false;
	}

	public static boolean isOtherReachable() {
		return otherReachable;
	}

	public static void setOtherReachable(boolean otherReachable) {
		Explorer.otherReachable = otherReachable;
	}

}
