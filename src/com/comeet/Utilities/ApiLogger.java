package com.comeet.Utilities;

import java.io.IOException;
import java.util.logging.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApiLogger implements ServletContextListener {
	static private FileHandler fileTxt;
	static public Logger logger;
	
	static public void setup() throws IOException {

        // get the global logger to configure it
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
                rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler("Logging.txt");

        // create a TXT formatter
        logger.addHandler(fileTxt);

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			setup();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
	

