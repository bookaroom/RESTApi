package com.comeet.utilities;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApiLogger implements ServletContextListener {
    public static Logger logger;

    private static FileHandler fileTxt;

    /**
     * Sets up the logger
     * 
     * @throws IOException When the log file fails.
     */
    public static void setup() throws IOException {

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
