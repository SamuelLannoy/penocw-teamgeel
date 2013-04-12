package messenger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.rabbitmq.client.Connection;

import field.Border;
import field.Direction;
import field.TilePosition;
import field.fromfile.MazePart;
import field.representation.FieldRepresentation;

import peno.htttp.Callback;
import peno.htttp.PlayerClient;
import peno.htttp.Tile;
import robot.DebugBuffer;
import robot.Robot;


public class PenoHtttpTeamCommunicator extends TeamCommunicator {
	
	public PenoHtttpTeamCommunicator() {
		
	}
	
	private PlayerHandlerImplementation handler;
	
	@Override
	public void connect(Robot robot, String gameId, String playerId) throws IOException {
		Connection connection = RabbitMQ.createConnection();
		handler = new PlayerHandlerImplementation(robot);
		
		PlayerClient client = new PlayerClient(connection, handler, gameId, playerId);
		setClient(client);
		connectionSuccesful();
	}
	
	private PlayerClient client;

	private void setClient(PlayerClient client) {
		this.client = client;
	}

	@Override
	public void joinGame() {
		checkConnected();
		try {
			client.join(new Callback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					DebugBuffer.addInfo("joined lobby with " + client.getNbPlayers() + " player(s)");
					setReady(true);
				}
				
				@Override
				public void onFailure(Throwable t) {
					
				}
			});
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setReady(boolean isReady) {
		checkConnected();
		try {
			client.setReady(isReady);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendInitialField(FieldRepresentation fieldRepresentation) {
		checkConnected();
		sendTiles(convertToMessage(fieldRepresentation));
	}

	@Override
	public void sendNewTiles(FieldRepresentation fieldRepresentation, TilePosition... tilePositions) {
		checkConnected();
		for (TilePosition tilePosition :  tilePositions) {
			sendTiles(convertToTileMsg(fieldRepresentation, tilePosition));
		}
	}
	
	private void sendTiles(Tile... tiles) {
		checkConnected();
		Collection<Tile> tilesC = new ArrayList<Tile>(Arrays.asList(tiles));
		sendTiles(tilesC);
	}
	
	private void sendTiles(Collection<Tile> tiles) {
		checkConnected();
		try {
			client.sendTiles(tiles);
			DebugBuffer.addComm("out: sending " + tiles.size() + " tiles");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void lockSeesaw(int barcode) {
		checkConnected();
		if (canSend()) {
			try {
				client.lockSeesaw(barcode);
				DebugBuffer.addComm("out: locking seesaw " + barcode);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void unlockSeesaw() {
		checkConnected();
		if (canSend()) {
			try {
				client.unlockSeesaw();
				DebugBuffer.addComm("out: unlocking seesaw");
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean sentFoundObject = false;
	
	private boolean foundObject = false;

	@Override
	public void foundObject() {
		checkConnected();
		if (canSend()) {
			try {
				client.foundObject();
				sentFoundObject = true;
				DebugBuffer.addComm("out: found object");
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean sentJoin = false;
	
	private int teamNr = -1;

	@Override
	public void joinTeam(int teamNr) {
		checkConnected();
		this.teamNr = teamNr;
		if (canSend()) {
			try {
				client.joinTeam(teamNr);
				sentJoin = true;
				DebugBuffer.addComm("out: joining team " + teamNr);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updatePosition(double x, double y, double angle) {
		checkConnected();
		if (canSend()) {
			try {
				client.updatePosition(x, y, angle);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			retryBufferedSends();
		}
	}

	private void retryBufferedSends() {
		if (!sentJoin && teamNr != -1) {
			joinTeam(teamNr);
		}
		if (!sentFoundObject && foundObject) {
			foundObject();
		}
	}

	private boolean canSend() {
		return client.isPlaying();
	}
	
	
	private Collection<peno.htttp.Tile> convertToMessage(FieldRepresentation fieldRepresentation) {
		Collection<peno.htttp.Tile> tilesMsg = new ArrayList<peno.htttp.Tile>();
		Iterator<field.Tile> it = fieldRepresentation.tileIterator();
		while (it.hasNext()) {
			field.Tile tile = it.next();
			peno.htttp.Tile toadd = convertToTileMsg(fieldRepresentation, tile.getPosition());
			if (toadd != null)
				tilesMsg.add(toadd);
		}
		return tilesMsg;
	}
	
	private peno.htttp.Tile convertToTileMsg(FieldRepresentation fieldRepresentation, TilePosition tilePos) {
		Map<Direction, Border> borders = new HashMap<Direction, Border>();
		field.Tile tile = fieldRepresentation.getTileAt(tilePos);

		try {
			borders.put(Direction.TOP, fieldRepresentation.getTopBorderOfTile(tile));
			borders.put(Direction.BOTTOM, fieldRepresentation.getBottomBorderOfTile(tile));
			borders.put(Direction.LEFT, fieldRepresentation.getLeftBorderOfTile(tile));
			borders.put(Direction.RIGHT, fieldRepresentation.getRightBorderOfTile(tile));

			String sentToken = MazePart.getToken(borders, tile);
			System.out.println("sent: " + sentToken);
			if (sentToken.equals(""))
				return null;

			return new peno.htttp.Tile(tile.getPosition().getX(),
					tile.getPosition().getY(),
					sentToken);

		} catch (IllegalArgumentException e) {


			return new peno.htttp.Tile(tile.getPosition().getX(),
					tile.getPosition().getY(),
					"unknown");
		}
	}
}