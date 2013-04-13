package field.simulation;

import java.io.IOException;

import com.rabbitmq.client.Connection;

import messenger.RabbitMQ;

import peno.htttp.SpectatorClient;
import peno.htttp.impl.SpectatorHandlerImplementation;
import robot.RobotPool;

public class PenoHtttpWorldCommunicator implements WorldCommunicator {
	
	private SpectatorClient client;
	private SpectatorHandlerImplementation handler;
	
	public PenoHtttpWorldCommunicator(String ownId, String gameId, RobotPool pool, FieldSimulation field) throws IOException {
		Connection connection = RabbitMQ.createConnection();
		handler = new SpectatorHandlerImplementation(pool, ownId, field);
		
		client = new SpectatorClient(connection, handler, gameId);
	}
	
	@Override
	public void connect() throws IOException {
		client.start();
	}

}
