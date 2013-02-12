package simulator;

public class Command {
			
	public Command(int param, CommandType cmd) {
		super();
		setParam(param);
		setCmd(cmd);
	}
	
	public Command(CommandType cmd) {
		this(0, cmd);
	}

	private int param;

	public int getParam() {
		return param;
	}

	public void setParam(int param) {
		this.param = param;
	}
	
	private CommandType cmd;

	public CommandType getCmd() {
		return cmd;
	}

	public void setCmd(CommandType cmd) {
		this.cmd = cmd;
	}
	
	public void execute(VirtualRobotConnector conn) {
		cmd.execute(conn, this);
	}
	
	@Override
	public String toString() {
		return "" + getCmd().toString() + " param " + getParam();
	}
}
