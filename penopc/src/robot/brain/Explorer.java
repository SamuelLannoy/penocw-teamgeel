package robot.brain;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import communication.SeesawStatus;
import communication.Status;

import field.*;
import field.fieldmerge.FieldMerger;

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
		// TODO: set on center tile
		if (!robot.isSim()) {
			//robot.setOnCenterTile();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (Status.isCentering()) {
				
			}
			waitTillRobotStops(robot, 1000);
			//DebugBuffer.addInfo("resetting pos");
			robot.zeroPos();
		}
		
		Field field = robot.getField();
		LinkedList<ExploreNode> toExplore = new LinkedList<ExploreNode>();
		HashSet<Position> explored = new HashSet<Position>();
		ExploreNode init = new ExploreNode(robot.getCurrTile(), null);
		toExplore.add(init);
		
		for (Tile tile : robot.getField().getTileMap()) {
			if (!robot.getField().isSure(tile.getPosition())) {
				ExploreNode node = new ExploreNode(tile, null);
				toExplore.add(node);
			}
		}
		
		while (!toExplore.isEmpty() && !endCond.isLastTile(robot)) {
			boolean quit = false;
			ExploreNode current = toExplore.removeFirst();
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


			List<Tile> tileList = Pathfinder.findShortestPath(robot, current.getTile());
			//System.out.println("list: " + tileList.toString());
			robot.setAStartTileList(tileList);
			if (tileList.size() > 1) {
				if (tileList.size() > 2)
					robot.scanOnlyLines(true);
				robot.travelToNextTile(tileList.get(1));
				robot.scanOnlyLines(true);
				//DebugBuffer.addInfo("turning off barcode read");
				for (int i = 1; i < tileList.size() - 2; i++) {
					//DebugBuffer.addInfo("traveling to " + tileList.get(i+1).getPosition());
					if (i == tileList.size() - 2){
						//robot.scanOnlyLines(false);
						//DebugBuffer.addInfo("turning off barcode read");
					}
					robot.travelFromTileToTile(tileList.get(i), tileList.get(i+1), tileList.get(i-1));
				}
				//DebugBuffer.addInfo("turning on barcode read");
				waitTillRobotStops(robot, 500);
				waitTillRobotStops(robot, 250);
				waitTillRobotStops(robot, 250);
				robot.scanOnlyLines(false);
				if (tileList.size() > 2) {
					robot.hasWrongBarcode();
					robot.hasCorrectBarcode();
				}
				int r = tileList.size() - 1;
				robot.travelToNextTile(tileList.get(r));
				waitTillRobotStops(robot, 250);
				
				DebugBuffer.addInfo("done moving!");
			}
			Direction dirForw = Direction.fromAngle(robot.getPosition().getRotation());
			System.out.println("dirforw " + dirForw);
			Direction dirLeft = Direction.fromAngle(robot.getPosition().getRotation() - 90); 
			Direction dirRight = Direction.fromAngle(robot.getPosition().getRotation() + 90);
			Direction dirBack = Direction.fromAngle(robot.getPosition().getRotation() + 180);
			/*for (Tile tile : tileList) {
				robot.travelToNextTile(tile);
				waitTillRobotStops(robot, 2000);
			}*/
			robot.resetAStartTileList();
			
			boolean correct = robot.hasCorrectBarcode();
			boolean wrong = robot.hasWrongBarcode();
			//DebugBuffer.addInfo("barcode check " + robot.isScanning() + " c " + correct + " w " + wrong);
			/*if (correct || wrong) {
				DebugBuffer.addInfo("barcode detected!");
			}*/
			if (correct || wrong || robot.isScanning()) {
				//DebugBuffer.addInfo("barcode detected!");
				if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(robot.getCurrTile().getPosition())))
					field.addBorder(new WhiteBorder(dirForw.getBorderPositionInDirection(robot.getCurrTile().getPosition())));
				
				Position newTilePos = dirForw.getPositionInDirection(robot.getCurrTile().getPosition());
				System.out.println("newtilepos " + newTilePos);
				if (field.canHaveAsTile(dirForw.getPositionInDirection(robot.getCurrTile().getPosition())))
					field.addTile(new Tile(newTilePos));
				
				if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(robot.getCurrTile().getPosition())))
					field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(robot.getCurrTile().getPosition())));
				if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(robot.getCurrTile().getPosition())))
					field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(robot.getCurrTile().getPosition())));
				if (!robot.isScanning()) {
					if (correct) {
						while (robot.getCurrTile().getBarcode() == null);
					} else {
						waitTillRobotStops(robot, 5000);
					}
				} else {
					// experimental
					DebugBuffer.addInfo("still scanning!");
					//robot.moveForward(20);
					//waitTillRobotStops(robot, 100);
					waitTillRobotStops(robot, 5000);
					
					//while (!correct && !wrong) {
					//while (robot.isScanning()) {
						correct = robot.hasCorrectBarcode();
						wrong = robot.hasWrongBarcode();
						//System.out.println("waiting for barcode result");
					//}
					
					//DebugBuffer.addInfo("barcode check " + robot.isScanning() + " c " + correct + " w " + wrong);
				}

				boolean check = true;
				
				if (correct) {
					DebugBuffer.addInfo("correct barcode");
					
					Barcode code = robot.getCurrTile().getBarcode();
					
					DebugBuffer.addInfo("test: " + code.getType());
					
					switch (code.getType()) {
						case OBJECT:
							/*if (robot.getTeamNr() == -1 || (robot.hasBall() && robot.getTeamNr() != -1)){

								
							} else {
								
							}*/
							
							Direction dirForwLocal = dirForw;
							Direction dirBackLocal = dirBack;

							Tile newT;
							if (robot.getTeamNr() != -1 && !robot.hasFoundOwnBarcode()) {
								robot.setHasFoundOwnBarcode(true);
								robot.stopMoving();
								Direction temp = dirForw;
								dirForwLocal = dirBack;
								dirBackLocal = temp;
								
								//Position pos = dirBackLocal.getPositionInDirection(robot.getCurrTile().getPosition());
								//pos = dirBackLocal.getPositionInDirection(pos);
								//Tile tile = robot.getField().getTileMap().getObjectAtId(pos);
								
								
								robot.setPosition(new robot.Position(0, 0, dirBackLocal.toAngle()), robot.getCurrTile());
								
								//pos = dirForwLocal.getPositionInDirection(pos);
								//pos = dirForwLocal.getPositionInDirection(pos);
								System.out.println("adding tile: " + newTilePos);
								if (!robot.isSim()) {
									newTilePos = dirForwLocal.getPositionInDirection(robot.getCurrTile().getPosition());
									if (field.canHaveAsTile(newTilePos))
										field.addTile(new Tile(newTilePos));
								}
								newT = robot.getField().getTileMap().getObjectAtId(newTilePos);
							} else {
								if (field.canHaveAsTile(dirForwLocal.getPositionInDirection(robot.getCurrTile().getPosition())))
									field.addTile(new Tile(dirForwLocal.getPositionInDirection(robot.getCurrTile().getPosition())));
								newT = robot.getField().getTileMap().getObjectAtId(dirForwLocal.getPositionInDirection(robot.getCurrTile().getPosition()));
								System.out.println("adding tile: " + newT.getPosition());
							}
							System.out.println("tile: " + robot.getCurrTile().getPosition());
							
							
							if (field.canHaveAsBorder(dirForwLocal.getBorderPositionInDirection(newT.getPosition())))
								field.addBorder(new PanelBorder(dirForwLocal.getBorderPositionInDirection(newT.getPosition())));
							
							if (field.canHaveAsBorder(dirBackLocal.getBorderPositionInDirection(newT.getPosition())))
								field.addBorder(new WhiteBorder(dirBackLocal.getBorderPositionInDirection(newT.getPosition())));
							
							if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(newT.getPosition())))
								field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(newT.getPosition())));
							
							if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(newT.getPosition())))
								field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(newT.getPosition())));
							check = false;
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
							//field.addBorder(new SeesawBorder(dirForw.getBorderPositionInDirection(robot.getCurrTile().getPosition())));
							
							
							//DebugBuffer.addInfo("waiting for seesaw");
							
							boolean over = false;
							if (!robot.isSim()) {
								while (robot.getSeesawStatus() != SeesawStatus.ISOPEN);
							} else {
								if (robot.getSeesawStatus() == SeesawStatus.ISOPEN) {
									robot.setSeesawMode(true);
									robot.moveForward(1600);
									waitTillRobotStops(robot, 250);
									waitTillRobotStops(robot, 250);
									over = true;
								}
							}
							/*if (robot.getSeesawStatus() != SeesawStatus.ISCLOSED &&
									robot.getSeesawStatus() != SeesawStatus.ISOVER) {
								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);
							}
							DebugBuffer.addInfo("seesaw done");*/
							check = false;
							if (robot.getSeesawStatus() == SeesawStatus.ISCLOSED) {
								Tile btile = robot.getCurrTile();
								Position pos = dirForw.getPositionInDirection(robot.getCurrTile().getPosition());
								for (int i = 0; i < 4; i++) {
									if (field.canHaveAsTile(pos))
										field.addTile(new Tile(pos));
									
									if (i == 2) {
										int bcode = btile.getBarcode().getDecimal();
										int ncode = 0;
										if (bcode == 11 || bcode == 15 || bcode == 19) {
											ncode = bcode + 2;
										} else if (bcode == 13 || bcode == 17 || bcode == 21) {
											ncode = bcode - 2;
										}
										field.getTileMap().getObjectAtId(pos).setBarcode(new Barcode(ncode));
									}
									
									if (i != 3) {
										if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(pos)))
											field.addBorder(new WhiteBorder(dirForw.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirBack.getBorderPositionInDirection(pos)))
											field.addBorder(new WhiteBorder(dirBack.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(pos)))
											field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(pos)))
											field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(pos)));
									}
									pos = dirForw.getPositionInDirection(pos);
								}
							} else if (robot.getSeesawStatus() == SeesawStatus.ISOVER || over) {
								robot.setSeesawMode(false);

								Position pos = dirForw.getPositionInDirection(robot.getCurrTile().getPosition());
								Tile lastTile = null;
								Tile pLastTile = null;
								for (int i = 0; i < 4; i++) {
									Tile newTile = new Tile(pos);
									if (i != 3) {
										pLastTile = newTile;
									}
									lastTile = newTile;
									if (field.canHaveAsTile(pos))
										field.addTile(newTile);
									
									if (i != 3) {
										if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(pos)))
											field.addBorder(new WhiteBorder(dirForw.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirBack.getBorderPositionInDirection(pos)))
											field.addBorder(new WhiteBorder(dirBack.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(pos)))
											field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(pos)))
											field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(pos)));
									}
									pos = dirForw.getPositionInDirection(pos);
								}
								
								robot.setCurrTile(lastTile);
								robot.setPosition(new robot.Position(0, 0, robot.getPosition().getRotation()), robot.getCurrTile());
								
								toExplore.add(new ExploreNode(lastTile, pLastTile));
								
								
								/*Position pos = dirForw.getPositionInDirection(robot.getCurrTile().getPosition());
								int bcode = robot.getCurrTile().getBarcode().getDecimal();
								robot.scanOnlyLines(true);
								Tile lasttile = null;
								Tile slasttile = null;
								for (int i = 0; i < 4; i++) {
									lasttile = new Tile(pos);
									if (i != 3)
										slasttile = lasttile;
									robot.moveForward(400);
									waitTillRobotStops(robot, 250);

									if (field.canHaveAsTile(pos))
										field.addTile(new Tile(pos));

									if (i == 2) {
										int ncode = 0;
										if (bcode == 11 || bcode == 15 || bcode == 19) {
											ncode = bcode + 2;
										} else if (bcode == 13 || bcode == 17 || bcode == 21) {
											ncode = bcode - 2;
										}
										field.getTileMap().getObjectAtId(pos).setBarcode(new Barcode(ncode));
									}
									
									if (i != 3) {
										if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(pos)))
											field.addBorder(new WhiteBorder(dirForw.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirBack.getBorderPositionInDirection(pos)))
											field.addBorder(new WhiteBorder(dirBack.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(pos)))
											field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(pos)))
											field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(pos)));
									}
								}*/
								/*ExploreNode newNode = new ExploreNode(lasttile, slasttile);
								toExplore.add(lasttile);
								robot.scanOnlyLines(false);*/
								
								/*Position pos = dirForw.getPositionInDirection(robot.getCurrTile().getPosition());
								Tile lasttile = null;
								for (int i = 0; i < 4; i++) {
									lasttile = new Tile(pos);
									if (field.canHaveAsTile(pos))
										field.addTile(new Tile(pos));
									
									if (i != 3) {
										if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(pos)))
											field.addBorder(new WhiteBorder(dirForw.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirBack.getBorderPositionInDirection(pos)))
											field.addBorder(new WhiteBorder(dirBack.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(pos)))
											field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(pos)));
										
										if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(pos)))
											field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(pos)));
									}
								}

								robot.setPosition(new robot.Position(0, 0, dirForw.toAngle()), lasttile);*/
							}
							break;
						default:
							break;
					}
					
				} else if (wrong) {
					DebugBuffer.addInfo("wrong barcode");
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
					}
					/*correct = robot.hasCorrectBarcode();
					wrong = robot.hasWrongBarcode();*/
				}
				
				if (check) {
					Position pos = dirForw.getPositionInDirection(current.getTile().getPosition());
					ExploreNode newNode = new ExploreNode(new Tile(pos), current.getTile());
					if (!field.isExplored(pos) && !explored.contains(pos) && !toExplore.contains(newNode)) {
						toExplore.add(newNode);
					}
				}
			} else {

				Direction dirx = Direction.fromAngle(robot.getPosition().getRotation() + 180);
				if (field.getBorderMap().hasId(dirx.getBorderPositionInDirection(current.getTile().getPosition())) &&
						!(field.getBorderInDirection(current.getTile(), dirx) instanceof UnsureBorder)) {
					robot.newTileScan();

				} else {
					robot.scanSonar();
				}

				System.out.println("scan command given " + current.getTile() + " rt " + robot.getCurrTile());

				while (!field.isExplored(current.getTile().getPosition())) {
					waitTillRobotStops(robot, 1000);
				}
				System.out.println("done scanning");

				for (Direction dir : Direction.values()) {
					if (field.getBorderInDirection(current.getTile(), dir) instanceof UnsureBorder) {
						robot.turnToAngle(dir.toAngle());
						robot.moveForward(55);
						waitTillRobotStops(robot, 1000);
						int counter = 0;
						while (field.getBorderInDirection(current.getTile(), dir) instanceof UnsureBorder) {
							robot.checkScan();
							waitTillRobotStops(robot, 900);
							if (counter == 2 && field.getBorderInDirection(current.getTile(), dir) instanceof UnsureBorder) {
								field.addBorder(new WhiteBorder(dir.getBorderPositionInDirection(current.getTile().getPosition())));
								if (field.canHaveAsTile(dir.getPositionInDirection(current.getTile().getPosition()))){
									field.addTile(new Tile(dir.getPositionInDirection(current.getTile().getPosition())));
								}
								break;
							}
							counter++;
						}
						robot.moveBackward(55);
						waitTillRobotStops(robot, 1000);
					}
					if (field.getBorderInDirection(current.getTile(), dir) instanceof WhiteBorder) {
						Position pos = dir.getPositionInDirection(current.getTile().getPosition());
						ExploreNode newNode = new ExploreNode(new Tile(pos), current.getTile());
						if (!field.isExplored(pos) && !explored.contains(pos) && !toExplore.contains(newNode)) {
							//System.out.println("add " + pos);
							toExplore.add(newNode);
						}
					}
				}
			}

			
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
			
			while (pause) {
				
			}
		}

		DebugBuffer.addInfo("finish");
		
		if (!robot.hasTeamMate()) {
			// try to find friend
		}
		
		if (robot.hasTeamMate()) {
			if (robot.hasTeamMateField()) {
				Field merged = FieldMerger.mergeFields(robot.getField(), robot.getTeamMateField());
				
				// check merged field ?
				
				robot.setField(merged);
			} else {
				// request field
			}
			
			
			// TODO go to each other
		}
		
		
		
		/*if (robot.getStartTile() != null)
			DebugBuffer.addInfo("start " + robot.getStartTile().getPosition());
		if (robot.getFinishTile() != null)
			DebugBuffer.addInfo("stop " + robot.getFinishTile().getPosition());
		if (robot.getStartTile() != null && robot.getFinishTile() != null) {
			if (!robot.isSim()) {
				robot.setMoveSpeed(250);
				robot.setTurnSpeed(100);
			}
			robot.scanOnlyLines(true);
			List<Tile> tileList = Pathfinder.findShortestPath(robot, robot.getStartTile());
			robot.setAStartTileList(tileList);
			DebugBuffer.addInfo("moving to start");
			if (tileList.size() > 1) {
				robot.travelToNextTile(tileList.get(1));
				for (int i = 1; i < tileList.size() - 1; i++) {
					robot.travelFromTileToTile(tileList.get(i), tileList.get(i+1), tileList.get(i-1));
				}
				waitTillRobotStops(robot, 1000);
				waitTillRobotStops(robot, 500);
				waitTillRobotStops(robot, 500);
				DebugBuffer.addInfo("done moving to start");
			}
			DebugBuffer.addInfo("moving to finish");
			robot.resetAStartTileList();
			tileList = Pathfinder.findShortestPath(robot, robot.getFinishTile());
			robot.setAStartTileList(tileList);
			if (tileList.size() > 1) {
				robot.travelToNextTile(tileList.get(1));
				for (int i = 1; i < tileList.size() - 1; i++) {
					robot.travelFromTileToTile(tileList.get(i), tileList.get(i+1), tileList.get(i-1));
				}
				waitTillRobotStops(robot, 1000);
				waitTillRobotStops(robot, 500);
				waitTillRobotStops(robot, 500);
				DebugBuffer.addInfo("done moving to finish");
			}
			robot.resetAStartTileList();
			robot.scanOnlyLines(false);
		}*/
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
