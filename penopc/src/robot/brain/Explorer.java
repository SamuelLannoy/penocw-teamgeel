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
		LinkedList<ExploreNode> toExplore = new LinkedList<ExploreNode>();
		// list of tiles that are explored
		HashSet<TilePosition> explored = new HashSet<TilePosition>();
		// first explore tile defined
		ExploreNode init = new ExploreNode(robot.getCurrTile(), null);
		// add to toexplore list
		toExplore.add(init);
		
		// add all tiles that have gray borders or that need to be explored
		Iterator<Tile> it = robot.getField().tileIterator();
		while (it.hasNext()) {
			Tile tile = it.next();
			if (!robot.getField().isSure(tile.getPosition())
					&& !robot.getField().isExplored(tile.getPosition())) {
				ExploreNode node = new ExploreNode(tile, null);
				toExplore.add(node);
			}
		}
		
		// main loop : stop when explore list or last tile has been met due to ending condition
		while (!toExplore.isEmpty() && !endCond.isLastTile(robot)) {
			// pop first
			ExploreNode current = toExplore.removeFirst();
			
			
			boolean quit = false;
			// TODO: do not remove if straight tile
			while  (robot.getField().isSure(current.getTile().getPosition())) {
				if (toExplore.size() > 0) {
					DebugBuffer.addInfo("exploring " + current.getTile().getPosition());
					current = toExplore.removeFirst();
					explored.add(current.getTile().getPosition());
				} else {
					quit = true;
					break;
				}
			}
			if (quit)
				break;
			//DebugBuffer.addInfo("explore " + current.getTile().getPosition());

			boolean ignoreSeesaw = false;
			boolean reachedDestination = false;
			
			while (!reachedDestination) {
				List<Tile> tileList = Pathfinder.findShortestPath(robot, current.getTile(), ignoreSeesaw);
				robot.setAStartTileList(tileList);
	
				if (tileList.size() > 2) {
					robot.hasWrongBarcode();
					robot.hasCorrectBarcode();
				}
				
				int i = 0;
				for (Tile tile : tileList) {
					robot.travelToNextTile(tile);
					waitTillRobotStops(robot, 500);
					waitTillRobotStops(robot, 250);
					waitTillRobotStops(robot, 250);
					waitTillRobotStops(robot, 250);
					DebugBuffer.addInfo("moved tile");
					

					if (i != tileList.size() - 1 && i != 0) {

						robot.hasWrongBarcode();
						robot.hasCorrectBarcode();
						if (robot.getCurrTile().hasBarcocde() && robot.getCurrTile().getBarcode().isSeesaw()) {
							if (SensorBuffer.getInfrared() < 4) {
								Tile ctile = robot.getCurrTile();
		
								robot.moveAcrossSeesaw();
								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);
		
								TilePosition afterWipPos = robot.getTilePositionAfterSeesaw(ctile);
								robot.setPosition(new robot.Position(0, 0, robot.getPosition().getRotation()), new Tile(afterWipPos));
								ignoreSeesaw = false;
							} else {
								ignoreSeesaw = true;
							}
							break;
						}
					}
					else {
						if (i == tileList.size() - 1) {
							reachedDestination = true;
							DebugBuffer.addInfo("reached destination");
						}
					}
					i++;
				}
			}
			
			Direction dirForw = Direction.fromAngle(robot.getPosition().getRotation());
			//System.out.println("dirforw " + dirForw);
			Direction dirLeft = Direction.fromAngle(robot.getPosition().getRotation() - 90); 
			Direction dirRight = Direction.fromAngle(robot.getPosition().getRotation() + 90);
			Direction dirBack = Direction.fromAngle(robot.getPosition().getRotation() + 180);
			// reset gui variable for astar
			robot.resetAStartTileList();

			// ask barcode correctness
			boolean correct = robot.hasCorrectBarcode();
			boolean wrong = robot.hasWrongBarcode();
			
			// barcode has been detected
			if (correct || wrong || robot.isScanning()) {

				robot.getField().registerBarcode(robot.getCurrTile().getPosition(), dirForw);
				
				// is not scanning anymore
				if (!robot.isScanning()) {
					if (correct) {
						// has received correct => wait till it appears onscreen
						while (robot.getCurrTile().getBarcode() == null);
					} else {
						// if wrong barcode wait 5s
						waitTillRobotStops(robot, 5000);
					}
				} else { // still scanning
					DebugBuffer.addInfo("still scanning!");
					waitTillRobotStops(robot, 5000);

					// reupdate correct / wrong
					correct = robot.hasCorrectBarcode();
					wrong = robot.hasWrongBarcode();
				}

				//check is false when next tile (in direction of robot) need not be explored
				// this is the case when we cross the seesaw or when we come across any object barcode
				boolean check = true;
				
				if (correct) {// correcte barcode
					DebugBuffer.addInfo("correct barcode");
					
					// get barcode of current tile
					Barcode code = robot.getCurrTile().getBarcode();
					
					DebugBuffer.addInfo("test: " + code.getType());
					
					// do action based on the barcode type
					switch (code.getType()) {
						case OBJECT:


							System.out.println("Teamnr: "+robot.getTeamNr());
							System.out.println("Gevonden: "+robot.hasFoundOwnBarcode());
							
							// keep current tile of robot as reference
							Tile tile = robot.getCurrTile();
							robot.getField().registerBall(tile.getPosition(), dirForw);
							
							// pick up our object
							if (robot.getTeamNr() != -1 && !robot.hasFoundOwnBarcode()) {
								System.out.println("PICKUP");
								robot.setHasFoundOwnBarcode(true);
								robot.stopMoving();

								System.out.println("ObjectNr: "+Integer.parseInt(robot.getCurrTile().getBarcode().toString().substring(4, 5),2));
								System.out.println("OurObjectNr"+robot.getObjectNr());
								System.out.println("Barcode: "+robot.getCurrTile().getBarcode());
								
								// execute pickup
								
								robot.pauseLightSensor();
								robot.getCurrTile().getBarcode();
								/*robot.turnLeft(90);
										waitTillRobotStops(robot, 250);
										robot.startMovingForward();
										DebugBuffer.addInfo("touch");

										while(!SensorBuffer.getTouched()){}
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									DebugBuffer.addInfo("after touch");
										robot.stopMoving();
										robot.moveBackward(100);
										robot.turnRight(90);
										waitTillRobotStops(robot, 250);*/
								DebugBuffer.addInfo("pick obj up");
								robot.startMovingForward();
								while(!SensorBuffer.getTouched()){};
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								DebugBuffer.addInfo("picked up");
								robot.stopMoving();
								robot.moveBackward(100);
								robot.turnLeft(180);
								robot.moveForward(800);
								robot.resumeLightSensor();
								robot.setHasBall(true);
								// execute pickup

								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);
								
								DebugBuffer.addInfo("OUT: my team is " + robot.getTeamNr());
								// send object found + join team via rabbitmq
								robot.getClient().hasFoundObject();
								try {
									robot.getClient().joinTeam(robot.getTeamNr());
								} catch (IllegalStateException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

								
								
								robot.setPosition(new robot.Position(0, 0, dirBack.toAngle()),
										field.getTileAt(
												dirBack.getPositionInDirection(tile.getPosition())));
							} else {
								System.out.println("WRONG OBJ");
								
								if (!robot.isSim()) {
									// execute move away from wrong object
									robot.pauseLightSensor();
									robot.scanOnlyLines(true);
									DebugBuffer.addInfo("PAUSE");
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									robot.moveForward(200);
									waitTillRobotStops(robot, 250);
									robot.turnLeft(180);
									waitTillRobotStops(robot, 250);
									robot.moveForward(750);
									waitTillRobotStops(robot, 250);
									DebugBuffer.addInfo("RESUME");
									robot.scanOnlyLines(false);
									robot.resumeLightSensor();
									robot.setPosition(new robot.Position(0, 0, dirBack.toAngle()),
											field.getTileAt(
													dirBack.getPositionInDirection(tile.getPosition())));
									
								}

								//check is false when next tile (in direction of robot) need not be explored
								// this is the case when we cross the seesaw or when we come across any object barcode
								check = false;
							}
							
							System.out.println("tile: " + robot.getCurrTile().getPosition());
							break;
						case CHECKPOINT:
							break;
						case ILLEGAL:
							break;
						case OTHERPLAYERBARCODE:
							break;
						case PICKUP:
							
							break;
						case SEESAW:
							robot.pauseLightSensor();

							DebugBuffer.addInfo("SEESAW");

							// keep current tile as reference
							Tile btile = robot.getCurrTile();
							TilePosition pos = dirForw.getPositionInDirection(robot.getCurrTile().getPosition());

							// register seesaw
							robot.getField().registerSeesaw(btile.getPosition(), dirForw);


							// wait for infrared values
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println(("INFRARED VALUE: " + SensorBuffer.getInfrared()));
							// < 4 => open
							if(SensorBuffer.getInfrared() < 4 ){
								
								// execute move across seesaw
								DebugBuffer.addInfo("MOVING");

								robot.moveAcrossSeesaw();
								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);

								TilePosition afterWipPos = robot.getTilePositionAfterSeesaw(btile);
								Tile afterWipTile = new Tile(afterWipPos);
								robot.setPosition(new robot.Position(0, 0, robot.getPosition().getRotation()), afterWipTile);

								toExplore.add(new ExploreNode(afterWipTile, robot.getField().getTileAt(robot.getBarcodePositionAfterSeesaw(btile))));
							}// seesaw closed
							else{
								Status.setSeesawStatus(SeesawStatus.ISNOTAPPLICABLE);
							}
							robot.resumeLightSensor();
							break;
						default:
							break;
					}
					
				} else if (wrong) {
					throw new IllegalStateException("robot heeft foute barcode gelezen");
					/*DebugBuffer.addInfo("wrong barcode");
					robot.moveForward(40);
					waitTillRobotStops(robot, 200);
					robot.moveBackward(270);
					waitTillRobotStops(robot, 200);
					robot.moveForward(300);
					waitTillRobotStops(robot, 200);
					waitTillRobotStops(robot, 5000);
					while (!correct && !wrong) {
						correct = robot.hasCorrectBarcode();
						wrong = robot.hasWrongBarcode();
						System.out.println("waiting for barcode result");
					}*/
				}
				
				// add tile in direction of robot
				if (check) {
					TilePosition pos = dirForw.getPositionInDirection(current.getTile().getPosition());
					ExploreNode newNode = new ExploreNode(new Tile(pos), current.getTile());
					if (!field.isExplored(pos) && !explored.contains(pos) && !toExplore.contains(newNode)) {
						toExplore.add(newNode);
					}
				}
			} else {

				// if border at back is defined do new tile scan
				Direction dirx = Direction.fromAngle(robot.getPosition().getRotation() + 180);
				if (field.hasBackBorder(current.getTile().getPosition(), dirx) &&
						!(field.getBorderInDirection(current.getTile(), dirx) instanceof UnsureBorder)) {
					robot.newTileScan();

				} else { // else scan 360
					robot.scanSonar();
				}

				System.out.println("scan command given " + current.getTile() + " rt " + robot.getCurrTile());

				// wait till tile border results have been given
				while (!field.isExplored(current.getTile().getPosition())) {
					waitTillRobotStops(robot, 1000);
				}
				System.out.println("done scanning");

				// check for gray borders in every direction
				for (Direction dir : Direction.values()) {
					if (field.getBorderInDirection(current.getTile(), dir) instanceof UnsureBorder) {
						// turn and move to gray border
						robot.turnToAngle(dir.toAngle());
						robot.moveForward(55);
						waitTillRobotStops(robot, 1000);
						// scan border again, time outs with 3 tries
						// after 3 tries add white border with new tile
						int counter = 0;
						while (field.getBorderInDirection(current.getTile(), dir) instanceof UnsureBorder) {
							robot.checkScan();
							waitTillRobotStops(robot, 900);
							if (counter == 2 && field.getBorderInDirection(current.getTile(), dir) instanceof UnsureBorder) {
								robot.getField().registerNewTile(dir, current.getTile().getPosition());
								break;
							}
							counter++;
						}
						// move back
						robot.moveBackward(55);
						waitTillRobotStops(robot, 1000);
					}
					
					// if a white border was scanned add a new tile and add it to explore list
					if (field.getBorderInDirection(current.getTile(), dir) instanceof WhiteBorder) {
						TilePosition pos = dir.getPositionInDirection(current.getTile().getPosition());
						ExploreNode newNode = new ExploreNode(new Tile(pos), current.getTile());
						if (!field.isExplored(pos) && !explored.contains(pos) && !toExplore.contains(newNode)) {
							toExplore.add(newNode);
						}
					}
				}
			}

			
			// sort tiles on a* length
			Collections.sort(toExplore, new Comparator<ExploreNode>() {

				@Override
				public int compare(ExploreNode arg0, ExploreNode arg1) {
					int mh1 = Pathfinder.findShortestPath(robot, arg0.getTile()).size();
					int mh2 = Pathfinder.findShortestPath(robot, arg1.getTile()).size();
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

		DebugBuffer.addInfo("looking for friend");
		// wait till teammate is set
		while (!robot.hasTeamMate()) { }
		
		DebugBuffer.addInfo("found friend");
		DebugBuffer.addInfo("sending tiles to friend");
		// make collection of tilesmsges
		Collection<peno.htttp.Tile> tilesMsg = robot.getField().convertToMessage();
		try {
			// send tiles
			robot.getClient().sendTiles(tilesMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DebugBuffer.addInfo("waiting for team tiles");
		// wait till teammate has sent tiles
		while (!robot.receivedTeamTiles()) { }
		DebugBuffer.addInfo("received team tiles");
		
		boolean mergedFields = false;
		try {
			// merge fields
			robot.getField().mergeFields(robot.getTeamMate().getField());

			DebugBuffer.addInfo("fields merged");
			
			mergedFields = true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			// explore more
		}

		// check merged field ?


		waitTillRobotStops(robot, 1000);
		
		boolean ignoreSeesaw = false;
		boolean reachedDestination = false;
		
		while (!reachedDestination) {
			DebugBuffer.addInfo("moving from " + robot.getCurrTile().getPosition() + " to " + robot.getTeamMate().getCurrTile().getPosition());
			List<Tile> tileList = Pathfinder.findShortestPath(robot, robot.getTeamMate().getCurrTile(), ignoreSeesaw);
			robot.setAStartTileList(tileList);

			if (tileList.size() > 2) {
				robot.hasWrongBarcode();
				robot.hasCorrectBarcode();
			}
			
			int i = 0;
			for (Tile tile : tileList) {
				robot.travelToNextTile(tile);
				waitTillRobotStops(robot, 500);
				waitTillRobotStops(robot, 250);
				waitTillRobotStops(robot, 250);
				waitTillRobotStops(robot, 250);
				DebugBuffer.addInfo("moved tile");
				

				if (i != tileList.size() - 1 && i != 0) {
					robot.hasWrongBarcode();
					robot.hasCorrectBarcode();
					if (robot.getCurrTile().hasBarcocde() && robot.getCurrTile().getBarcode().isSeesaw()) {
						if (SensorBuffer.getInfrared() < 4) {
							Tile ctile = robot.getCurrTile();
	
							robot.moveAcrossSeesaw();
							waitTillRobotStops(robot, 250);
							waitTillRobotStops(robot, 250);
							waitTillRobotStops(robot, 250);
	
							Direction dirForw = Direction.fromAngle(robot.getPosition().getRotation());
							TilePosition afterWipPos = dirForw.getPositionInDirection(ctile.getPosition());
							afterWipPos = dirForw.getPositionInDirection(afterWipPos);
							afterWipPos = dirForw.getPositionInDirection(afterWipPos);
							afterWipPos = dirForw.getPositionInDirection(afterWipPos);
							robot.setPosition(new robot.Position(0, 0, robot.getPosition().getRotation()), new Tile(afterWipPos));
							ignoreSeesaw = false;
						} else {
							ignoreSeesaw = true;
						}
						break;
					}
					if (i == 1) {
						break;
					}
				}
				else {
					if (i == tileList.size() - 1) {
						reachedDestination = true;
						DebugBuffer.addInfo("reached destination");
					}
				}
				i++;
			}
		}
		
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
	
	public static void waitTillRobotStops(Robot robot, int base) {
		try {
			if (!robot.isSim()) {
				Thread.sleep(base);
			} else {
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (robot.isMoving()) {
			try {
				if (!robot.isSim()) {
					Thread.sleep(100);
				} else {
					Thread.sleep(20);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
