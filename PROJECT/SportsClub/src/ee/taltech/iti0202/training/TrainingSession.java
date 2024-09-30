package ee.taltech.iti0202.training;

import ee.taltech.iti0202.SportsClubSystem;
import ee.taltech.iti0202.logger.SportsClubLogger;
import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;
import ee.taltech.iti0202.trainer.Trainer;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Session is a training at specific time. Can be created if training has assigned trainer
 * and if sports club added training to its system. Has time and max participants number.
 */

public class TrainingSession {

    private final Training training;
    private final TrainingSessionLevel level;
    private final LocalDateTime time;
    private final Integer maxParticipants;
    private final List<Member> participants;
    private SportsClub sportsClub;
    private Duration duration;
    private double price;
    private static final int MINUTES = 30;

    // Date components
    private int dayOfMonth;
    private int month;
    private int year;
    private static final int LAST_DAY = 31;
    private static final int LAST_MONTH = 12;

    /**
     * Constructor for training session.
     * @param training
     * @param time
     * @param maxParticipants
     */
    public TrainingSession(Training training, TrainingSessionLevel level, LocalDateTime time, Integer maxParticipants) {
        this.training = Objects.requireNonNull(training);
        if (training.getTrainer() == null) {
            throw new IllegalStateException("Training cannot be conducted without a trainer.");
        }
        this.level = Objects.requireNonNull(level);
        this.time = Objects.requireNonNull(time);
        this.duration = Duration.ofHours(1).plusMinutes(MINUTES);
        this.maxParticipants = Objects.requireNonNull(maxParticipants);
        this.price = 0;
        this.participants = new ArrayList<>();

        // Initialize date components with current date
        LocalDate currentDate = LocalDate.now();
        this.dayOfMonth = currentDate.getDayOfMonth();
        this.month = currentDate.getMonthValue();
        this.year = currentDate.getYear();

        training.registerTrainingSession(this);
        SportsClubSystem.getInstance().addTrainingSession(this);
        SportsClubLogger.getInstance().getLogger().log(Level.INFO, "New training session has been registered");
    }

    /**
     * Get the parent training of this session.
     * @return The parent training.
     */
    public Training getTraining() {
        return training;
    }

    /**
     * Get the level of this session.
     * @return The parent training.
     */
    public TrainingSessionLevel getTrainingSessionLevel() {
        return level;
    }

    /**
     * Get the name of a training session.
     * @return String name.
     */
    public String getName() {
        return training.getName();
    }

    /**
     * Get the name of a training session.
     * @return String name.
     */
    public Trainer getTrainer() {
        return training.getTrainer();
    }

    /**
     * Get type of the training session.
     * @return enum type.
     */
    public TrainingSportType getTrainingSportType() {
        return training.getTrainingSportType();
    }

    public SportsClub getWhatSportsClubItBelongsTo() {
        return training.sportsClubItBelongsTo();
    }

    /**
     * Set date to session.
     * @param year
     * @param month
     * @param dayOfMonth
     */
    public void setDate(int year, int month, int dayOfMonth) {
        if (dayOfMonth < 1 || dayOfMonth > LAST_DAY) {
            throw new IllegalArgumentException("Invalid day of month.");
        }
        if (month < 1 || month > LAST_MONTH) {
            throw new IllegalArgumentException("Invalid month.");
        }
        if (year < 0) {
            throw new IllegalArgumentException("Invalid year.");
        }
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
    }

    /**
     * Get the day of the week derived from the set date components.
     *
     * @return The day of the week.
     */
    public DayOfWeek getDayOfWeek() {
        LocalDate date = LocalDate.of(year, month, dayOfMonth);
        return date.getDayOfWeek();
    }

    /**
     * Get the date components of the session.
     *
     * @return LocalDate representing the date components.
     */
    public LocalDate getDate() {
        return LocalDate.of(year, month, dayOfMonth);
    }

    /**
     * Get the date of the session as a formatted string.
     *
     * @return The date as a formatted string.
     */
    public String getFormattedDate() {
        LocalDate date = LocalDate.of(year, month, dayOfMonth);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    /**
     * Gets improved description with time and max participants.
     * @return description.
     */
    public String getDescription() {
        String formattedTime = time.toLocalTime().toString();
        return training.getDescription() + ", Time: " + formattedTime + ", Max Participants: " + maxParticipants;
    }

    /**
     * Get time of the session.
     * @return time.
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Get duration of the session.
     * @return duration. Always 1.5 hours.
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Get end time of the session.
     * @return time.
     */
    public LocalDateTime getEndTime() {
        return time.plus(duration);
    }

    /**
     * Get the limit for participants of the session.
     * @return limit.
     */
    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Get price for the session. In most cases it is free.
     * @return price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set new price for the session.
     * @param price
     */
    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
    }

    /**
     * Return list who registered to the session.
     * @return list.
     */
    public List<Member> getParticipants() {
        return participants;
    }

    /**
     * Checks if the maximum number of participants has been reached.
     * @return True if max participants reached, otherwise false.
     */
    public boolean hasReachedMaxParticipants() {
        return participants.size() >= maxParticipants;
    }

    /**
     * Method to add participant to the training session.
     * No need to check if member owns membership since we add participants
     * to the session through Member class.
     * @param member
     */
    public void addParticipant(Member member) {
        SportsClubLogger.getInstance().getLogger()
                .log(Level.INFO, "Member is added to a new training session");
        participants.add(member);
        training.incrementTotalParticipants(1);
    }

    /**
     * Method to remove participants.
     * @param member
     */
    public void removeParticipant(Member member) {
        SportsClubLogger.getInstance().getLogger()
                .log(Level.INFO, "Member is removed from this training session");
        participants.remove(member);
        training.decrementTotalParticipants(1);
    }

    /**
     * Builder for training session.
     */
    public static class Builder {
        Training training;
        TrainingSessionLevel level;
        LocalDateTime time;
        Integer maxParticipants;

        /**
         * Implement training session.
         * @param training
         * @return training.
         */
        public Builder withTraining(Training training) {
            this.training = Objects.requireNonNull(training);
            return this;
        }

        /**
         * Implement training session level.
         * @param level
         * @return level of difficulty.
         */
        public Builder withLevel(TrainingSessionLevel level) {
            this.level = Objects.requireNonNull(level);
            return this;
        }

        /**
         * Set time for session.
         * @param hour The hour of the day (0-23).
         * @param minute The minute of the hour (0-59).
         * @return time.
         */
        public Builder withTime(int hour, int minute) {
            this.time = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(hour, minute));
            return this;
        }

        /**
         * Set the max participants amount.
         * @param maxParticipants
         * @return int.
         */
        public Builder withMaxParticipants(Integer maxParticipants) {
            this.maxParticipants = Objects.requireNonNull(maxParticipants);
            if (maxParticipants <= 0) {
                throw new IllegalArgumentException("Maximum participants must be greater than zero.");
            }
            return this;
        }

        /**
         * Build training session.
         * @return new session.
         */
        public TrainingSession build() {
            if (training == null || training.getTrainer() == null) {
                throw new IllegalStateException("Training or trainer cannot be null.");
            }
            if (!training.isAddedToSportsClub()) {
                throw new IllegalStateException("Training must be added to sports club.");
            }
            TrainingSession session = new TrainingSession(training, level, time, maxParticipants);
            training.registerTrainingSession(session); // Automatically register the session with the training
            return session;
        }
    }
}
