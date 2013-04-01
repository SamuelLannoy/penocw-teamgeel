package robot.brain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import robot.Robot;

import field.*;

public class Pathfinder {
	
	public static List<Tile> findShortestPath(Robot robot, Tile endTile) {
		return findShortestPath(robot, endTile, false);
	}
	
	public static List<Tile> findShortestPath(Robot robot, Tile endTile, boolean ignoreSeesaw) {
		Field field = robot.getField();
		
		int h = endTile.getPosition().manhattanDistance(robot.getCurrTile().getPosition());
		Node start = new Node(robot.getCurrTile(), 0, h, null);
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
			List<Tile> toAdd = field.getPassableNeighbours(current.getTile());
			for (Tile tile : toAdd) {
				//ignore seesaw
				Direction dirForw = Direction.fromAngle(robot.getPosition().getRotation());
				if (ignoreSeesaw && current.equals(start) && tile.getPosition()
						.equals(dirForw.getPositionInDirection(current.getPos()))) {
					System.out.println("ignored seesaw " + tile.getPosition());
					continue;
				}
				
				int cAdd = current.getC() + 1;
				int hAdd = endTile.getPosition().manhattanDistance(tile.getPosition());
				Node add = new Node(tile, cAdd, hAdd, current);
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
					return 0;
				}
			});
		}
		if (closedList.isEmpty()) {
			throw new RuntimeException();
		}
		
		System.out.println("size " + closedList.size());
		Node currNode = closedList.get(closedList.size()-1);
		
		if (!endTile.getPosition().equals(currNode.getPos())) {
			throw new RuntimeException();
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
