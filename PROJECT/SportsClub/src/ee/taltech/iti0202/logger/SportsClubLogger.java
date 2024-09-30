package ee.taltech.iti0202.logger;

import java.util.logging.Logger;

public class SportsClubLogger {

    private static SportsClubLogger instance;
    private final Logger logger = Logger.getLogger(SportsClubLogger.class.getName());

    /**
     * Get instance.
     * @return instance.
     */
    public static SportsClubLogger getInstance() {
        if (instance == null) {
            instance = new SportsClubLogger();
        }
        return instance;
    }

    /**
     * Get logger.
     * @return logger.
     */
    public Logger getLogger() {
        return logger;
    }
}
