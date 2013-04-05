package robot.brain;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import communication.SeesawStatus;
import communication.Status;

import field.*;
import field.representation.FieldRepresentation;

import robot.DebugBuffer;
import robot.Robot;
import robot.SensorBuffer;

public class Explorer {
	
	private static boolean pause = false;
	
	public static void explore(final Robot robot) {
		explore(robot, new EndingCondition() {

			@Override
			public boolean isLastTile(Robot robot) {
				//return false;
				return robot.hasBall();
			}
			
		});
	}
	
	public static void exploreTillObjectFound(final Robot robot) {
		explore(robot, new EndingCondition() {

			@Override
			public boolean isLastTile(Robot robot) {
				return robot.hasBall();
			}
			
		});
	}
	
	public static void explore(final Robot robot, EndingCondition endCond) {
		
		FieldRepresentation field = robot.getField();
		// list of nodes that need to be explored
		LinkedList<TilePosition> toExplore = new LinkedList<TilePosition>();
		// list of tiles that are explored
		HashSet<TilePosition> explored = new HashSet<TilePosition>();
		// first explore tile defined
		TilePosition init = robot.getCurrTile().getPosition();
		// add to toexplore list
		toExplore.add(init);
		
		// add all tiles that have gray borders or that need to be explored
		Iterator<Tile> it = robot.getField().tileIterator();
		while (it.hasNext()) {
			Tile tile = it.next();
			if (!robot.getField().isSure(tile.getPosition())
					&& !robot.getField().isExplored(tile.getPosition())) {
				TilePosition node = tile.getPosition();
				toExplore.add(node);
			}
		}
		
		// main loop : stop when explore list or last tile has been met due to ending condition
		while (!toExplore.isEmpty() && !endCond.isLastTile(robot)) {
			// pop first
			TilePosition current = toExplore.removeFirst();
			
			
			boolean quit = false;
			// TODO: do not remove if straight tile
			while  (robot.getField().isSure(current)) {
				if (toExplore.size() > 0) {
					DebugBuffer.addInfo("exploring " + current);
					current = toExplore.removeFirst();
					explored.add(current);
				} else {
					quit = true;
					break;
				}
			}
			if (quit)
				break;
			//DebugBuffer.addInfo("explore " + current.getTile().getPosition());
			
			
			robot.goToTile(current);
			
			Collection<TilePosition> toAdd = robot.exploreTile();
			
			for (TilePosition pos : toAdd) {
				if (!field.isExplored(pos) && !explored.contains(pos) && !toExplore.contains(pos)) {
					toExplore.add(pos);
				}
			}
			
			// sort tiles on a* length
			Collections.sort(toExplore, new Comparator<TilePosition>() {

				@Override
				public int compare(TilePosition arg0, TilePosition arg1) {
					int mh1 = Pathfinder.findShortestPath(robot, robot.getField().getTileAt(arg0)).size();
					int mh2 = Pathfinder.findShortestPath(robot, robot.getField().getTileAt(arg1)).size();
					//int mh1 = arg0.getTile().getPosition().manhattanDistance(robot.getCurrTile().getPosition());
					//int mh2 = arg1.getTile().getPosition().manhattanDistance(robot.getCurrTile().getPosition());
					if (mh1 < mh2) return -1;
					if (mh1 > mh2) return 1;
					return 0;
				}
				
			});
			
			
			DebugBuffer.addInfo("list " + toExplore);
			// reset barcode values
			robot.hasCorrectBarcode();
			robot.hasWrongBarcode();
			
			// if pause was selected wait here
			while (pause) {
				
			}
		}

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

}
