package robot.brain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import lejos.nxt.Button;

import communication.SeesawStatus;
import communication.Status;

import field.*;
import field.fieldmerge.FieldMerger;
import field.fieldmerge.TileConverter;

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
		/*if (!robot.isSim()) {
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
		}*/
		
		Field field = robot.getField();
		LinkedList<ExploreNode> toExplore = new LinkedList<ExploreNode>();
		HashSet<Position> explored = new HashSet<Position>();
		ExploreNode init = new ExploreNode(robot.getCurrTile(), null);
		toExplore.add(init);
		
		for (Tile tile : robot.getField().getTileMap()) {
			if (!robot.getField().isSure(tile.getPosition())
					&& !robot.getField().isExplored(tile.getPosition())) {
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

			boolean ignoreSeesaw = false;

			//while (!robot.getCurrTile().getPosition().equals(current.getTile().getPosition())) {
				List<Tile> tileList = Pathfinder.findShortestPath(robot, current.getTile(), ignoreSeesaw);
				//System.out.println("list: " + tileList.toString());
				robot.setAStartTileList(tileList);
				if (tileList.size() > 1) {
					boolean brokeLoop = false;
					robot.travelToNextTile(tileList.get(1));
					for (int i = 1; i < tileList.size() - 2; i++) {
						//DebugBuffer.addInfo("traveling to " + tileList.get(i+1).getPosition());

						// een tegel op het pad heeft de barcode van een wip.
						// het pad wordt tot dan afgelegd en vervolgens opnieuw berekend.

						//TODO:add
						/*Tile tile = robot.getField().getTileMap().getObjectAtId(tileList.get(i).getPosition());
						if (tile.getBarcode() != null && (tile.getBarcode().getDecimal() == 11 || tile.getBarcode().getDecimal() == 13 ||
								tile.getBarcode().getDecimal() == 15 || tile.getBarcode().getDecimal() == 17 || tile.getBarcode().getDecimal() == 19 ||
								tile.getBarcode().getDecimal() == 21)){
							brokeLoop = true;
							break;
						}*/
						robot.travelFromTileToTile(tileList.get(i), tileList.get(i+1), tileList.get(i-1));
					}
					//DebugBuffer.addInfo("turning on barcode read");
					waitTillRobotStops(robot, 500);
					waitTillRobotStops(robot, 250);
					waitTillRobotStops(robot, 250);
					//robot.scanOnlyLines(false);
					//TODO:add
					// de wip was open en de robot is erover gegaan. 
					// Zet de robot op de positie na de wip.
					/*if (brokeLoop) {
						waitTillRobotStops(robot, 1000);
					}
					Direction dirForw = Direction.fromAngle(robot.getPosition().getRotation());

					/*if (SensorBuffer.getInfrared() < 4) {
						Tile tile = robot.getCurrTile();

						robot.moveAcrossSeesaw();
						waitTillRobotStops(robot, 250);
						waitTillRobotStops(robot, 250);
						waitTillRobotStops(robot, 250);

						Position afterWipPos = dirForw.getPositionInDirection(tile.getPosition());
						afterWipPos = dirForw.getPositionInDirection(afterWipPos);
						afterWipPos = dirForw.getPositionInDirection(afterWipPos);
						afterWipPos = dirForw.getPositionInDirection(afterWipPos);
						robot.setPosition(new robot.Position(0, 0, robot.getPosition().getRotation()), new Tile(afterWipPos));
						ignoreSeesaw = false;
					} else {
						ignoreSeesaw = true;
					}*/


					if (tileList.size() > 2) {
						robot.hasWrongBarcode();
						robot.hasCorrectBarcode();
					}
					int r = tileList.size() - 1;
					robot.travelToNextTile(tileList.get(r));
					waitTillRobotStops(robot, 250);

					DebugBuffer.addInfo("done moving!");
				}
			//}
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
			System.out.println("correct "+correct);
			System.out.println("wrong "+wrong);

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
							
							//TODO bepalen of PICKUP OF
							
							//TODO wat doet dit?
							Direction dirForwLocal = dirForw;
							Direction dirBackLocal = dirBack;

							//Tile newT;

							/*try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}*/

							System.out.println("Teamnr: "+robot.getTeamNr());
							System.out.println("Gevonden: "+robot.hasFoundOwnBarcode());
							
							Tile tile = robot.getCurrTile();
							
							if (robot.getTeamNr() != -1 && !robot.hasFoundOwnBarcode()) {
								System.out.println("PICKUP");
								robot.setHasFoundOwnBarcode(true);
								robot.stopMoving();
								
								Tile newT = new Tile(dirForw.getPositionInDirection(tile.getPosition()));

								if (field.canHaveAsTile(newT.getPosition()))
									field.addTile(newT);
								
								if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(newT.getPosition())))
									field.addBorder(new PanelBorder(dirForw.getBorderPositionInDirection(newT.getPosition())));
								
								if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(newT.getPosition())))
									field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(newT.getPosition())));
								
								if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(newT.getPosition())))
									field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(newT.getPosition())));
								
								
								
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
								//if (!robot.isSim()) {
								newTilePos = dirForw.getPositionInDirection(robot.getCurrTile().getPosition());
								if (field.canHaveAsTile(newTilePos))
									field.addTile(new Tile(newTilePos));

								System.out.println("ObjectNr: "+Integer.parseInt(robot.getCurrTile().getBarcode().toString().substring(4, 5),2));
								System.out.println("OurObjectNr"+robot.getObjectNr());
								System.out.println("Barcode: "+robot.getCurrTile().getBarcode());
								//if(Integer.parseInt(robot.getCurrTile().getBarcode().toString().substring(4, 5),2) == robot.getObjectNr()){
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
								//}

								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);
								
								DebugBuffer.addInfo("OUT: my team is " + robot.getTeamNr());
								robot.getClient().hasFoundObject();
								try {
									robot.getClient().joinTeam(robot.getTeamNr());
								} catch (IllegalStateException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

								robot.setPosition(new robot.Position(0, 0, dirBack.toAngle()),
										field.getTileMap().getObjectAtId(
												dirBack.getPositionInDirection(tile.getPosition())));

								//}
								newT = robot.getField().getTileMap().getObjectAtId(newTilePos);
							} else {
								System.out.println("WRONG OBJ");
								
								Tile newT = new Tile(dirForw.getPositionInDirection(tile.getPosition()));

								if (field.canHaveAsTile(newT.getPosition()))
									field.addTile(newT);
								
								if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(newT.getPosition())))
									field.addBorder(new PanelBorder(dirForw.getBorderPositionInDirection(newT.getPosition())));
								
								if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(newT.getPosition())))
									field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(newT.getPosition())));
								
								if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(newT.getPosition())))
									field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(newT.getPosition())));
								
								if (!robot.isSim()) {
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
									robot.resumeLightSensor();robot.setPosition(new robot.Position(0, 0, dirBack.toAngle()),
											field.getTileMap().getObjectAtId(
													dirBack.getPositionInDirection(tile.getPosition())));
									
								}
								
								check = false;
								
							}
							System.out.println("tile: " + robot.getCurrTile().getPosition());
							break;
						case CHECKPOINT:
							break;
						case ILLEGAL:
							break;
						case OTHERPLAYERBARCODE:
							//TODO teken boorden en voeg volgende niet toe
							break;
						case PICKUP:
							
							break;
						case SEESAW:

							//field.addBorder(new SeesawBorder(dirForw.getBorderPositionInDirection(robot.getCurrTile().getPosition())));
							robot.pauseLightSensor();

							DebugBuffer.addInfo("SEESAW");

							Tile btile = robot.getCurrTile();
							Position pos = dirForw.getPositionInDirection(robot.getCurrTile().getPosition());
							//SeesawBorder borderFirst = null;
							//SeesawBorder borderLast = null;
							int bcode = btile.getBarcode().getDecimal();
							int ncode = 0;
							if (bcode == 11 || bcode == 15 || bcode == 19) {
								ncode = bcode + 2;
							} else if (bcode == 13 || bcode == 17 || bcode == 21) {
								ncode = bcode - 2;
							}

							//2de tegel
							if (field.canHaveAsTile(pos))
								field.addTile(new Tile(pos));
							if (field.canHaveAsTile(pos))
								field.addTile(new Tile(pos));
							if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(pos)))
								field.addBorder(new WhiteBorder(dirForw.getBorderPositionInDirection(pos)));

							if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(pos)))
								field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(pos)));

							if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(pos)))
								field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(pos)));

							pos = dirForw.getPositionInDirection(pos);

							//3de tegel
							if (field.canHaveAsTile(pos))
								field.addTile(new Tile(pos));
							if (field.canHaveAsTile(pos))
								field.addTile(new Tile(pos));
							if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(pos)))
								field.addBorder(new WhiteBorder(dirForw.getBorderPositionInDirection(pos)));


							if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(pos)))
								field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(pos)));

							if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(pos)))
								field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(pos)));


							pos = dirForw.getPositionInDirection(pos);


							//4de tegel
							Tile pLastTile = new Tile(pos);
							if (field.canHaveAsTile(pos))
								field.addTile(pLastTile);
							if (field.canHaveAsTile(pos))
								field.addTile(new Tile(pos));
							if (field.canHaveAsTile(pos))
								field.addTile(new Tile(pos));
							if (field.canHaveAsBorder(dirForw.getBorderPositionInDirection(pos)))
								field.addBorder(new WhiteBorder(dirForw.getBorderPositionInDirection(pos)));

							if (field.canHaveAsBorder(dirLeft.getBorderPositionInDirection(pos)))
								field.addBorder(new PanelBorder(dirLeft.getBorderPositionInDirection(pos)));

							if (field.canHaveAsBorder(dirRight.getBorderPositionInDirection(pos)))
								field.addBorder(new PanelBorder(dirRight.getBorderPositionInDirection(pos)));
							field.getTileMap().getObjectAtId(pos).setBarcode(new Barcode(ncode));

							pos = dirForw.getPositionInDirection(pos);

							//4de tegel
							Tile lastTile = new Tile(pos);
							if (field.canHaveAsTile(pos))
								field.addTile(lastTile);



							//TODO teken wip
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println(("INFRARED VALUE: " + SensorBuffer.getInfrared()));
							if(SensorBuffer.getInfrared() < 4 ){
								//Status.setSeesawStatus(SeesawStatus.ISOPEN);
								//TODO PAUSE lightsensorvigilante
								//TODO speed verlagen
								DebugBuffer.addInfo("MOVING");

								robot.moveAcrossSeesaw();
								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);
								waitTillRobotStops(robot, 250);
								//TODO speed terugzetten
								//Status.setSeesawStatus(SeesawStatus.ISNOTAPPLICABLE);
								//TODO resume lightsensorvigilante

								robot.setCurrTile(lastTile);
								robot.setPosition(new robot.Position(0, 0, robot.getPosition().getRotation()), robot.getCurrTile());

								toExplore.add(new ExploreNode(lastTile, pLastTile));
							}
							else{
								Status.setSeesawStatus(SeesawStatus.ISNOTAPPLICABLE);
							}
							robot.resumeLightSensor();
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

		DebugBuffer.addInfo("looking for friend");
		while (!robot.hasTeamMate()) { }
		
		DebugBuffer.addInfo("found friend");
		DebugBuffer.addInfo("sending tiles to friend");
		Collection<peno.htttp.Tile> tilesMsg = new ArrayList<peno.htttp.Tile>(field.getTileMap().getKeys().size());
		for (Tile tile : field.getTileMap()) {
			peno.htttp.Tile toadd = TileConverter.convertToTileMsg(tile, field);
			if (toadd != null)
				tilesMsg.add(toadd);
		}
		try {
			robot.getClient().sendTiles(tilesMsg);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DebugBuffer.addInfo("waiting for team tiles");
		while (!robot.receivedTeamTiles()) { }
		DebugBuffer.addInfo("received team tiles");
		
		try {
			Field merged = FieldMerger.mergeFields(robot.getField(), robot.getTeamMateField());

			robot.setField(merged);
		} catch (IllegalStateException e) {
			// explore more
		}

		// check merged field ?


		// TODO go to each other


		
		
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
