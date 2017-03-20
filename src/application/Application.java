package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Application {	
	
	private static final String LOG_PROPERTIES_FILE = "log4j.properties";
	
	private final static Logger logger = Logger.getLogger(Application.class);
	
	public static void main(String args[]) {				
		new Application().start();
	}
	
	public Application() {
		init();
	}
	
	public void start() {
		new Menu(Menu.SIGNIN_SIGNUP_MENU).start();
	}
	
	private static void init() {
		initLogger();
	}
	
	private static void initLogger()
	{
		Properties logProperties = new Properties();

		try{
			logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
			PropertyConfigurator.configure(logProperties);
			logger.debug("Logging initialized.");
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to load logging property " + LOG_PROPERTIES_FILE);
		}
	}
}
