package code.utility;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    static private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static void debug(String msg){
        logger.log(Level.INFO, msg);
    }
    public static void error(String msg){
        logger.log(Level.SEVERE, msg);
    }
    public static void warning(String msg){
        logger.log(Level.WARNING, msg);
    }
}
