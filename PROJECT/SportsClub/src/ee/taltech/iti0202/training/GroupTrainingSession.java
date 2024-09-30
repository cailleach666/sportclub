package ee.taltech.iti0202.training;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class GroupTrainingSession extends TrainingSession {

    private static final int DEFAULT_MAX_PARTICIPANTS = 25;
    private static final double DEFAULT_PRICE = 0.0;
    private static final Duration SESSION_DURATION = Duration.ofHours(1).plusMinutes(30);
    private static final int FIFTEEN = 15;
    private static final int NINE = 9;

    /**
     * Constructor for group sessions.
     * @param training
     * @param level
     * @param time
     */
    public GroupTrainingSession(Training training, TrainingSessionLevel level, LocalDateTime time) {
        super(training, level, time, DEFAULT_MAX_PARTICIPANTS);
        if (!isTimeValid(time)) {
            throw new IllegalArgumentException("Personal training sessions "
                    + "can only take place between 14:00 and 18:00.");
        }
        setPrice(DEFAULT_PRICE);
    }

    /**
     * Check if time is between 9:00 and 15:00
     * @param time
     * @return boolean.
     */
    private boolean isTimeValid(LocalDateTime time) {
        LocalDateTime endTime = time.plus(SESSION_DURATION);
        return time.toLocalTime().isAfter(LocalTime.of(NINE, 0))
                && endTime.toLocalTime().isBefore(LocalTime.of(FIFTEEN, 0));
    }

    /**
     * Builder for group training session.
     */
    public static class Builder {

        private Training training;
        private TrainingSessionLevel level;
        private LocalDateTime time;
        private static final int FIFTEEN = 15;
        private static final int NINE = 9;

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
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime providedTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.of(hour, minute));
            if (!isTimeValid(providedTime)) {
                throw new IllegalArgumentException("Personal training sessions "
                        + "can only take place between 9:00 and 15:00.");
            }
            this.time = providedTime;
            return this;
        }

        /**
         * Check if time is between 9:00 and 15:00
         * @param time
         * @return boolean.
         */
        private boolean isTimeValid(LocalDateTime time) {
            LocalDateTime endTime = time.plus(SESSION_DURATION);
            return time.toLocalTime().isAfter(LocalTime.of(NINE, 0))
                    && endTime.toLocalTime().isBefore(LocalTime.of(FIFTEEN, 0));
        }

        /**
         * Build the group training session with the provided settings.
         *
         * @return The group training session.
         */
        public GroupTrainingSession build() {
            if (training == null || training.getTrainer() == null) {
                throw new IllegalStateException("Training or trainer cannot be null.");
            }
            if (!training.isAddedToSportsClub()) {
                throw new IllegalStateException("Training must be added to sports club.");
            }
            return new GroupTrainingSession(training, level, time);
        }
    }
}
