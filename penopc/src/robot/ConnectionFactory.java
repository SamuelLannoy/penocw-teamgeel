package robot;

import communication.CommandEncoder;
import simulator.SimulatorConnector;
import simulator.VirtualRobotConnector;

public class ConnectionFactory {
	
	public static AbstractRobotConnector getConnection(int connectionType) {
		switch (connectionType) {
			case 1:
				return new VirtualRobotConnector();
			case 2:
				return new CommandEncoder();
			case 10:
				return new SimulatorConnector();
		}
		return null;
	}

}
