package logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class serves all other server side classes for logging system
 * operations, all the while writing each operation to a file. The instance of
 * the class is a singleton by nature, thus ensuring centralized documentation
 * for all system users.
 */
public class MyLogger {

	private static MyLogger instance;
	private static Logger logger = null;
	private FileHandler fileHandler;
	private SimpleFormatter simpleFormatter;

	/**
	 * public MyLogger(), CTOR which retrieves the actual Logger, and handles
	 * the file writing definitions (using a FileHandler and a SimpleFormatter).
	 */
	public MyLogger() {
		logger = Logger.getLogger(MyLogger.class.getName());
		try {
			fileHandler = new FileHandler("couponSystemLog.log");
			logger.addHandler(fileHandler);
			simpleFormatter = new SimpleFormatter();
			fileHandler.setFormatter(simpleFormatter);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * public Logger getLogger(), getter for the class single instance.
	 */
	public static synchronized MyLogger getInstance() {
		if (instance == null)
			instance = new MyLogger();
		return instance;
	}

	/**
	 * public Logger getLogger(), getter for the system Logger.
	 */
	public Logger getLogger() {
		return logger;
	}

}