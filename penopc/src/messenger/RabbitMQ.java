package messenger;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQ {
	private static String hostName = Config.HOST_NAME_LOCAL;
	
	public static void setConnectionType(String type) {
		if (type.equals("local")) {
			hostName = Config.HOST_NAME_LOCAL;
		} else if (type.equals("kul")) {
			hostName = Config.HOST_NAME_KUL;
		}
	}
	
	/**
	 * Create a connection to a AMQP server using the configuration in the
	 * Config class
	 * 
	 * @return An active connection
	 * @throws IOException
	 */
	public static Connection createConnection() throws IOException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostName);
		factory.setPort(Config.PORT);
		factory.setUsername(Config.USER_NAME);
		factory.setPassword(Config.PASSWORD);
		factory.setVirtualHost(Config.VIRTUAL_HOST);
		factory.setRequestedHeartbeat(0);
		
		Connection conn = factory.newConnection();

		return conn;
	}

	/**
	 * Create a channel on the given connection. This method will also configure
	 * a message exchange and message queue based on the configuration in the
	 * Config class. The routing key in the config class is used to route
	 * message from the exchange to the queue.
	 * 
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	public static Channel createChannel(Connection conn) throws IOException {
		Channel channel = conn.createChannel();
		channel.exchangeDeclare(Config.EXCHANGE_NAME, "topic");

		return channel;
	}
}