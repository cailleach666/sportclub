package ee.taltech.iti0202.training;

import ee.taltech.iti0202.sportsclub.SportsClub;
import ee.taltech.iti0202.trainer.Trainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Training sets up information about sports club activities.
 */

public class Training {

    private final String name;
    private Trainer trainer;
    private final TrainingSportType type;
    private final Map<TrainingSession, Integer> sessionsMap;
    private int totalParticipants = 0;
    private boolean isAddedToSportsClub;
    private SportsClub sportsClub;

    /**
     * Constructor for training.
     * @param name
     * @param type
     */
    public Training(String name, TrainingSportType type) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
        this.sessionsMap = new HashMap<>();
        this.isAddedToSportsClub = false;
    }

    /**
     * Get name of the training.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get sports type of the training.
     * @return sports type.
     */
    public TrainingSportType getTrainingSportType() {
        return type;
    }

    /**
     * Get description of the training, including the name of the trainer.
     * @return Description of the training.
     * @throws IllegalStateException If no trainer is assigned to the training.
     */
    public String getDescription() {
        if (trainer == null) {
            throw new IllegalStateException("No trainer assigned to this training");
        }
        return "Training: " + name + " (Trainer: " + trainer.getName() + ", Sport: " + type + ")";
    }

    /**
     * Get the trainer of the training.
     * @return The trainer.
     */
    public Trainer getTrainer() {
        return trainer;
    }

    /**
     * Assign a trainer to conduct this training.
     * @param trainer The trainer to assign.
     * @throws IllegalArgumentException If the trainer cannot conduct this training type.
     */
    public void assignTrainer(Trainer trainer) {
        if (!trainer.getTrainingTypes().contains(type)) {
            throw new IllegalArgumentException("Trainer cannot conduct this training type");
        }
        if (!(getTrainer() == null)) {
            throw new IllegalArgumentException("Training already has a trainer");
        }
        this.trainer = trainer;
        trainer.getAssignedTrainings().add(this);
    }

    /**
     * Register a new training session and update session and participant count.
     * @param session The training session to register.
     * @throws IllegalArgumentException If the session is not associated with this training.
     */
    public void registerTrainingSession(TrainingSession session) {
        if (!session.getTraining().equals(this)) {
            throw new IllegalArgumentException("The session is not associated with this training.");
        }
        sessionsMap.put(session, session.getParticipants().size());
        incrementTotalParticipants(session.getParticipants().size());
    }

    /**
     * Return if it is added to sports club.
     * @return boolean.
     */
    public boolean isAddedToSportsClub() {
        return isAddedToSportsClub;
    }

    /**
     * Set isAddedToSportsClub.
     * @param isAddedToSportsClub The value to set.
     */
    public void setIsAddedToSportsClub(boolean isAddedToSportsClub) {
        this.isAddedToSportsClub = isAddedToSportsClub;
    }

    /**
     * Get sport club it belongs to.
     * @return sports club.
     */
    public SportsClub sportsClubItBelongsTo() {
        return sportsClub;
    }

    public void setSportsClubItBelongsTo(SportsClub sportsClub) {
        this.sportsClub = sportsClub;
    }

    /**
     * Create a summary of training sessions.
     * @return
     */
    public String getTrainingSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("This training has ")
                .append(getTotalSessionsNumber())
                .append(" session(s), ")
                .append(getTotalParticipants())
                .append(" participant(s)");

        return summary.toString();
    }

    protected void incrementTotalParticipants(int count) {
        totalParticipants += count;
    }

    protected void decrementTotalParticipants(int count) {
        totalParticipants -= count;
    }

    /**
     * Get the total number of participants across all sessions.
     * @return The total number of participants.
     */
    public int getTotalParticipants() {
        return totalParticipants;
    }

    /**
     * Get the total number of sessions created from this training.
     * @return The total number of sessions.
     */
    public int getTotalSessionsNumber() {
        return sessionsMap.size();
    }

    /**
     * Get the total number of sessions created from this training.
     * @return The total number of sessions.
     */
    public Set<TrainingSession> getTotalSessions() {
        return sessionsMap.keySet();
    }

    /**
     * Builder class for creating Training objects.
     */
    public static class Builder {

        private String name;
        private TrainingSportType type;

        /**
         * Set the name of the training.
         * @param name The name of the training.
         * @return The builder instance.
         */
        public Builder withName(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        /**
         * Set the sports type of the training.
         * @param type The sports type of the training.
         * @return The builder instance.
         */
        public Builder withTrainingSportType(TrainingSportType type) {
            this.type = Objects.requireNonNull(type);
            return this;
        }

        /**
         * Build the Training object.
         * @return The created Training object.
         */
        public Training build() {
            return new Training(name, type);
        }
    }
}
