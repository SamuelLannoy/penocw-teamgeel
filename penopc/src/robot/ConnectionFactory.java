package robot;

import communication.CommandEncoder;
import simulator.VirtualRobotConnector;

public class ConnectionFactory {
	
	public static AbstractRobotConnector getConnection(int connectionType) {
		switch (connectionType) {
			case 1:
				return new VirtualRobotConnector();
			case 2:
				return new CommandEncoder();
		}
		return null;
	}

}
