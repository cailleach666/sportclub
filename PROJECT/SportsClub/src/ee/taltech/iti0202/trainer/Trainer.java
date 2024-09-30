package ee.taltech.iti0202.trainer;

import ee.taltech.iti0202.training.Training;
import ee.taltech.iti0202.training.TrainingSportType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Conducts trainings and training sessions.
 * Does not have to be sports club worker. Sometimes they just use sport club rooms
 * or do some sessions to earn extra money.
 */

public class Trainer {

    private final String name;
    private final List<TrainingSportType> trainingTypes;
    private List<Training> assignedTrainings;

    /**
     * Constructor for trainer.
     *
     * @param name
     * @param trainingTypes
     */
    public Trainer(String name, ArrayList<TrainingSportType> trainingTypes) {
        this.name = Objects.requireNonNull(name);
        this.trainingTypes = new ArrayList<>(trainingTypes);
        this.assignedTrainings = new ArrayList<>();
    }

    /**
     * Get the name of the trainer.
     * @return The name of the trainer.
     */
    public String getName() {
        return name;
    }

    /**
     * Add a training type that the trainer can conduct.
     * @param trainingType The training type to add.
     */
    public void addTrainingType(TrainingSportType trainingType) {
        if (trainingTypes.contains(trainingType)) {
            throw new IllegalArgumentException("Training type is already in the list.");
        }
        trainingTypes.add(trainingType);
    }

    /**
     * Remove a training type that the trainer can no longer conduct.
     * @param trainingType The training type to remove.
     */
    public void removeTrainingType(TrainingSportType trainingType) {
        if (!trainingTypes.contains(trainingType)) {
            throw new IllegalArgumentException("Training type is not in the list.");
        }
        trainingTypes.remove(trainingType);
    }

    /**
     * Get the list of training types that the trainer can conduct.
     * @return The list of training types.
     */
    public List<TrainingSportType> getTrainingTypes() {
        return trainingTypes;
    }

    /**
     * Get training all types as a number.
     * @return Integer.
     */
    public Integer getTotalTrainingTypes() {
        return trainingTypes.size();
    }

    /**
     * Get all sessions participants as a number.
     * @return Integer.
     */
    public Integer getTotalTrainerParticipants() {
        int totalParticipants = 0;
        for (Training training : getAssignedTrainings()) {
            int addParticipants = training.getTotalParticipants();
            totalParticipants += addParticipants;
        }
        return totalParticipants;
    }

    /**
     * Return list of trainings where trainer must work (was assigned).
     * @return trainers.
     */
    public List<Training> getAssignedTrainings() {
        return assignedTrainings;
    }

    /**
     * Builder class for creating Trainer objects.
     */
    public static class Builder {

        private String name;
        private List<TrainingSportType> trainingTypes = new ArrayList<>();

        /**
         * Set the name of the trainer.
         * @param name The name of the trainer.
         * @return The builder instance.
         */
        public Builder withName(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        /**
         * Add a training type that the trainer can conduct.
         *
         * @param trainingType The training type to add.
         * @return The builder instance.
         */
        public Builder addTrainingType(TrainingSportType trainingType) {
            if (trainingTypes.contains(trainingType)) {
                throw new IllegalArgumentException("Training type is already in the list.");
            }
            trainingTypes.add(trainingType);
            return this;
        }

        /**
         * Build the Trainer object.
         * @return The created Trainer object.
         */
        public Trainer build() {
            return new Trainer(name, (ArrayList<TrainingSportType>) trainingTypes);
        }
    }
}
