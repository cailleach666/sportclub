package ee.taltech.iti0202;

import ee.taltech.iti0202.logger.SportsClubLogger;
import ee.taltech.iti0202.sportsclub.SportsClub;
import ee.taltech.iti0202.training.TrainingSession;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Class to hold all created sports clubs. Singleton method used to create only one system.
 */

public final class SportsClubSystem {

    private static SportsClubSystem instance;
    private final List<SportsClub> sportsClubsInArea;
    private final List<TrainingSession> trainingSessions;

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private SportsClubSystem() {
        sportsClubsInArea = new ArrayList<>();
        trainingSessions = new ArrayList<>();
    }

    /**
     * Get the instance of SportsClubSystem.
     * @return The instance of SportsClubSystem.
     */
    public static SportsClubSystem getInstance() {
        if (instance == null) {
            instance = new SportsClubSystem();
        }
        return instance;
    }

    /**
     * Add a new sports club to the system.
     * @param sportsClub The sports club to add.
     */
    public void addSportsClub(SportsClub sportsClub) {
        if (!sportsClubsInArea.contains(sportsClub)) {
            sportsClubsInArea.add(sportsClub);
        }
    }

    /**
     * Add new training session to the system.
     * @param trainingSession to add.
     */
    public void addTrainingSession(TrainingSession trainingSession) {
        if (!trainingSessions.contains(trainingSession)) {
            trainingSessions.add(trainingSession);
        }
    }

    /**
     * Get the list of sports clubs in the area.
     * @return The list of sports clubs.
     */
    public List<SportsClub> getSportsClubsInArea() {
        return sportsClubsInArea;
    }

    /**
     * Get the list of training sessions in the area.
     * @return all training sessions.
     */
    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    /**
     * Generate a string containing information about all sports clubs in the area.
     * @return The string containing information about all sports clubs.
     */
    public String getAllSportsClubsInfo() {
        StringBuilder info = new StringBuilder();
        info.append("All sports clubs in this area:\n");
        for (SportsClub club : sportsClubsInArea) {
            info.append("Sports club ").append(club.getId()).append("\n");
        }
        return info.toString();
    }

    /**
     * Simulate the passage of a month for all sports clubs in the system.
     * This method removes memberships for all members in each sports club.
     */
    public void passMonthForAllClubs() {
        for (SportsClub club : sportsClubsInArea) {
            club.nextMonth();
        }
        SportsClubLogger.getInstance().getLogger()
                .log(Level.INFO, "One month passed and members need to renew memberships");
    }

}
