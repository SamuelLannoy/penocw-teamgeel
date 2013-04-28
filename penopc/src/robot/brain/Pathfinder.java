package robot.brain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import robot.DebugBuffer;
import robot.Robot;

import field.*;
import field.representation.FieldRepresentation;

public class Pathfinder {
	
	public static List<Tile> findShortestPath(Robot robot, Tile endTile) {
		return findShortestPath(robot, endTile, new ArrayList<Integer>());
	}
	
	public static List<Tile> findShortestPath(Robot robot, Tile endTile, Collection<Integer> ignoredSeesaws) {
		return findShortestPath(robot, endTile, ignoredSeesaws, new ArrayList<TilePosition>());
	}
	
	public static List<Tile> findShortestPathWithRobotCollision(Robot robot, Tile endTile, Collection<TilePosition> ignoredTiles) {
		return findShortestPath(robot, endTile, new ArrayList<Integer>(), ignoredTiles);
	}
	
	private static int lastT = 0;
	
	public static int getLastT() {
		return lastT;
	}
	
	public static List<Tile> findShortestPath(Robot robot, Tile endTile, Collection<Integer> ignoredSeesaws, Collection<TilePosition> ignoredTiles) {
		FieldRepresentation field = robot.getField();
		
		int h = endTile.getPosition().manhattanDistance(robot.getCurrTile().getPosition());
		Node start = new Node(robot.getCurrTile(), 0, h, null);
		start.setDirection(robot.getDirection());
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		openList.add(start);
		while (!openList.isEmpty()) {
			Node current = openList.get(0);
			closedList.add(current);
			openList.remove(0);
			if (current.getPos().equals(endTile.getPosition())) {
				break;
			}
			Map<Direction, Tile> toAdd = field.getPassableNeighbours(current.getTile());
			for (Direction dir : toAdd.keySet()) {
				Tile tile = toAdd.get(dir);
				//ignore seesaw
				if (robot.getField().getTileAt(current.getPos()).hasBarcocde()) {
					int barcode = robot.getField().getTileAt(current.getPos()).getBarcode().getDecimal();
					if (ignoredSeesaws.contains(barcode) && robot.getField().hasSeesawBorder(tile)) {
						continue;
					}
				}
				
				if (ignoredTiles.contains(tile.getPosition()))
					continue;		
				
				if (current.getPrev() != null && current.getPrev().getTile().getPosition().equals(tile.getPosition()))
					continue;
				
				int turns = 0;
				int prevT = 0;
				if (current.getPrev() != null){
					turns = current.getPrev().getDirection().turnsTo(dir);
					prevT = current.getPrev().getT();
				} else {
					turns = robot.getDirection().turnsTo(dir);
				}
				int cAdd = current.getC() + 1;
				int hAdd = endTile.getPosition().manhattanDistance(tile.getPosition());
				Node add = new Node(tile, cAdd, hAdd, current);
				add.setDirection(dir);
				DebugBuffer.addInfo(turns + " " + prevT);
				add.setT(prevT + turns);
				if (!closedList.contains(add)) {
					int i = openList.indexOf(add);
					if (i == -1 || openList.get(i).getF() > add.getF()) {
						openList.remove(add);
						openList.add(add);
					}
				}
			}
			Collections.sort(openList, new Comparator<Node>() {
				@Override
				public int compare(Node o1, Node o2) {
					if (o1.getF() < o2.getF())
						return -1;
					else if (o1.getF() > o2.getF())
						return 1;
					else{
						if (o1.getT() < o2.getT())
							return -1;
						else if (o1.getT() > o2.getT())
							return 1;
						return 0;
					}
				}
			});
		}
		if (closedList.isEmpty()) {
			throw new RuntimeException();
		}
		
		//System.out.println("size " + closedList.size());
		Node currNode = closedList.get(closedList.size()-1);
		lastT = currNode.getT();
		
		if (!endTile.getPosition().equals(currNode.getPos())) {
			throw new IllegalArgumentException("no path found from " + robot.getCurrTile().getPosition() + " to " + endTile.getPosition());
		}
		
		LinkedList<Tile> ret = new LinkedList<Tile>();
		boolean finished = false;
		ret.addFirst(currNode.getTile());
		while (!finished) {
			currNode = currNode.getPrev();
			if (currNode != null) {
				ret.addFirst(currNode.getTile());
			} else {
				finished = true;
			}
		}
		
		return ret;
	}

}
