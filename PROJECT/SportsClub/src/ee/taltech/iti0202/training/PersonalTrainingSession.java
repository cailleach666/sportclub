package ee.taltech.iti0202.training;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class PersonalTrainingSession extends TrainingSession {

    private static final int DEFAULT_MAX_PARTICIPANTS = 1;
    private static final double DEFAULT_PRICE = 30.0;
    private static final Duration SESSION_DURATION = Duration.ofHours(1).plusMinutes(30);
    private static final int FOURTEEN = 14;
    private static final int EIGHTEEN = 18;

    /**
     * Constructor for training session.
     *
     * @param training
     * @param level
     * @param time
     */
    public PersonalTrainingSession(Training training, TrainingSessionLevel level, LocalDateTime time) {
        super(training, level, time, DEFAULT_MAX_PARTICIPANTS);
        if (!isTimeValid(time)) {
            throw new IllegalArgumentException("Personal training sessions can "
                    + "only take place between 14:00 and 18:00.");
        }
        setPrice(DEFAULT_PRICE);
    }

    /**
     * // Check if time is between 14:00 and 18:00. End time also must be before 18:00.
     * @param time
     * @return boolean.
     */
    private boolean isTimeValid(LocalDateTime time) {
        LocalDateTime endTime = time.plus(SESSION_DURATION);
        return time.toLocalTime().isAfter(LocalTime.of(FOURTEEN, 0))
                && endTime.toLocalTime().isBefore(LocalTime.of(EIGHTEEN, 0));
    }

    /**
    * Builder for personal training session.
     */
    public static class Builder {

        private Training training;
        private TrainingSessionLevel level;
        private LocalDateTime time;
        private static final int FOURTEEN = 14;
        private static final int EIGHTEEN = 18;

        /**
         * Implement training session.
         *
         * @param training
         * @return training.
         */
        public Builder withTraining(Training training) {
            this.training = Objects.requireNonNull(training);
            return this;
        }

        /**
         * Implement training session level.
         *
         * @param level
         * @return level of difficulty.
         */
        public Builder withLevel(TrainingSessionLevel level) {
            this.level = Objects.requireNonNull(level);
            return this;
        }

        /**
         * Set time for session.
         *
         * @param hour   The hour of the day (0-23).
         * @param minute The minute of the hour (0-59).
         * @return time.
         */
        public Builder withTime(int hour, int minute) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime providedTime = LocalDateTime.of(currentTime.toLocalDate(), LocalTime.of(hour, minute));
            if (!isTimeValid(providedTime)) {
                throw new IllegalArgumentException("Personal training sessions can "
                        + "only take place between 14:00 and 18:00.");
            }
            this.time = providedTime;
            return this;
        }

        /**
         * // Check if time is between 14:00 and 18:00. End time also must be before 18:00.
         *
         * @param time
         * @return boolean.
         */
        private boolean isTimeValid(LocalDateTime time) {
            LocalDateTime endTime = time.plus(SESSION_DURATION);
            return time.toLocalTime().isAfter(LocalTime.of(FOURTEEN, 0))
                    && endTime.toLocalTime().isBefore(LocalTime.of(EIGHTEEN, 0));
        }

        /**
         * Build personal training session with the provided settings.
         *
         * @return The personal training session.
         */
        public PersonalTrainingSession build() {
            if (training == null || training.getTrainer() == null) {
                throw new IllegalStateException("Training or trainer cannot be null.");
            }
            if (!training.isAddedToSportsClub()) {
                throw new IllegalStateException("Training must be added to sports club.");
            }
            return new PersonalTrainingSession(training, level, time);
        }
    }
}
