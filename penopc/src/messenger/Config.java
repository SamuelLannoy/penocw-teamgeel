package messenger;

/**
 * An interface that contains some configuration data for the examples. 
 */
public interface Config {
	public static final String USER_NAME = "guest";
	public static final String PASSWORD = "guest";
	public static final String VIRTUAL_HOST = "/";
	public static final String HOST_NAME_LOCAL = "localhost";
	public static final String HOST_NAME_KUL = "leuven.cs.kotnet.kuleuven.be";
	public static final int PORT = 5672;
	
	public static final String EXCHANGE_NAME = "Exchange";
}
