package ee.taltech.iti0202.training;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class OnlineTrainingSession extends TrainingSession {

    private static final double DEFAULT_PRICE = 5.0;

    /**
     * Constructor for training session.
     *
     * @param training
     * @param level
     * @param time
     */
    public OnlineTrainingSession(Training training, TrainingSessionLevel level, LocalDateTime time) {
        super(training, level, time, Integer.MAX_VALUE);
        setPrice(DEFAULT_PRICE);
    }

    /**
     * Builder for online training session.
     */
    public static class Builder {

        private Training training;
        private TrainingSessionLevel level;
        private LocalDateTime time;

        /**
         * Set the training for the session.
         *
         * @param training The training associated with the session.
         * @return The builder instance.
         */
        public Builder withTraining(Training training) {
            this.training = Objects.requireNonNull(training);
            return this;
        }

        /**
         * Set the level of the session.
         *
         * @param level The level of the training session.
         * @return The builder instance.
         */
        public Builder withLevel(TrainingSessionLevel level) {
            this.level = Objects.requireNonNull(level);
            return this;
        }

        /**
         * Set the time for the session.
         *
         * @param hour   The hour of the day (0-23).
         * @param minute The minute of the hour (0-59).
         * @return The builder instance.
         */
        public Builder withTime(int hour, int minute) {
            this.time = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(hour, minute));
            return this;
        }

        /**
         * Build the online training session with the provided settings.
         *
         * @return The online training session.
         */
        public OnlineTrainingSession build() {
            if (training == null || training.getTrainer() == null) {
                throw new IllegalStateException("Training or trainer cannot be null.");
            }
            if (!training.isAddedToSportsClub()) {
                throw new IllegalStateException("Training must be added to sports club.");
            }
            return new OnlineTrainingSession(training, level, time);
        }
    }
}
