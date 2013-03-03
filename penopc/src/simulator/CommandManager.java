package simulator;

import java.util.LinkedList;
import java.util.Queue;

public class CommandManager implements Tickable {
	
	private ISimulator conn;
	private IMovementManager movMger;
	
	public CommandManager(ISimulator conn) {
		this.conn = conn;
	}
	
	public void setMovMger(IMovementManager movMger) {
		this.movMger = movMger;
	}
	
	private Queue<Command> cmdQueue = new LinkedList<Command>();
	
	private Command currCmd = null;

	public Command getCurrentCommand() {
		return currCmd;
	}
	
	public void addCommand(Command cmd) {
		if (cmd == null)
			throw new IllegalArgumentException();
		cmdQueue.add(cmd);
	}
	
	public void resetCurrentCommand() {
		currCmd = null;
	}
	
	public void clearCommandQueue() {
		cmdQueue.clear();
	}
	
	public void clear() {
		resetCurrentCommand();
		clearCommandQueue();
	}
	
	public void tick() {
		if (!cmdQueue.isEmpty() && currCmd == null) {
			currCmd = cmdQueue.poll();
			currCmd.execute(movMger);
		}
	}
}
