package messenger;

import java.io.IOException;
import java.util.Date;

import com.rabbitmq.client.*;

public class Messenger {
	
	Connection conn = null;
	Channel channel = null;
	AMQP.Queue.DeclareOk queue = null;
	
	public void connect() throws IOException {
		conn = RabbitMQ.createConnection();
		channel = RabbitMQ.createChannel(conn);
		queue = channel.queueDeclare();
	}
	
	// TODO: foutafhandeling hier of ergens anders?
	public void send(String message) throws IOException {
		AMQP.BasicProperties props = new AMQP.BasicProperties();
		props.setTimestamp(new Date());
		props.setContentType("text/plain");
		props.setDeliveryMode(1);
		
		channel.basicPublish(Config.EXCHANGE_NAME, Config.LAUNCH_ROUTING_KEY, 
				props, message.getBytes());
	}
	
	public String receiveTimer(String key) throws IOException {				
		// TODO: implementeren (of zelfs niet indien niet nodig)
		
		return null;
	}
	
	public void receivePush(String key) throws IOException {
		// bind the queue to all routing keys that match the given key
		channel.queueBind(queue.getQueue(), Config.EXCHANGE_NAME, key);
				
		boolean noAck = false;
		
		// ask the server to notify us of new message and do not send ack message automatically
		// WARNING: This code is called from the thread that does the communication with the 
		// server so sufficient locking is required. Also do not use any blocking calls to
		// the server such as queueDeclare, txCommit, or basicCancel. Basicly only "basicAck"
		// should be called here!!!
		channel.basicConsume(queue.getQueue(), noAck, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, 
					AMQP.BasicProperties properties, byte[] body) throws IOException {
				
				// get the delivery tag to ack that we processed the message successfully
				long deliveryTag = envelope.getDeliveryTag();

				// properties.getTimestamp() contains the timestamp
				// that the sender added when the message was published. This 
				// time is the time on the sender and NOT the time on the 
				// AMQP server. This implies that clients are possibly out of sync!
				System.out.println(String.format("@%d: %s -> %s", 
						properties.getTimestamp().getTime(),
						envelope.getRoutingKey(),
						new String(body)));
				
				// send an ack to the server so it can remove the message from the queue.	
				channel.basicAck(deliveryTag, false);
			}
		});
	}
	
}
