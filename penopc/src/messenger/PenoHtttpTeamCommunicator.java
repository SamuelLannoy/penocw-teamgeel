package messenger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import peno.htttp.PlayerClient;
import peno.htttp.Tile;
import robot.DebugBuffer;


public class PenoHtttpTeamCommunicator {
	
	public PenoHtttpTeamCommunicator(PlayerClient client) {
		setClient(client);
	}
	
	private PlayerClient client;

	private void setClient(PlayerClient client) {
		this.client = client;
	}
	
	public void sendTiles(Tile... tiles) {
		Collection<Tile> tilesC = new ArrayList<Tile>(Arrays.asList(tiles));
		sendTiles(tilesC);
	}
	
	public void sendTiles(Collection<Tile> tiles) {
		try {
			client.sendTiles(tiles);
			DebugBuffer.addComm("out: sending " + tiles.size() + " tiles");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void lockSeesaw(int barcode) {
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

	public void unlockSeesaw() {
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

	public void foundObject() {
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
	
	public void joinTeam(int teamNr) {
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
	
	public void updatePosition(double x, double y, double angle) {
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

	public boolean canSend() {
		return client.isPlaying();
	}
}
