package ee.taltech.iti0202.sportsclub;

import ee.taltech.iti0202.SportsClubSystem;
import ee.taltech.iti0202.logger.SportsClubLogger;
import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.membership.FullMembership;
import ee.taltech.iti0202.membership.Membership;
import ee.taltech.iti0202.membership.StandardMembership;
import ee.taltech.iti0202.trainer.Trainer;
import ee.taltech.iti0202.training.Training;
import ee.taltech.iti0202.training.TrainingSession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Sets up new sports club and holds all members, trainers, trainings and sessions in the system.
 */
public class SportsClub {

    private List<Member> allMembers;
    private List<Trainer> allTrainers;
    private List<Training> allTrainings;
    private List<Membership> memberships;
    static int nextId = 0;
    private final int id;
    int totalSessions = 0;
    private double averageNumPreviousMonth = 0.0;
    private double averageNum = 0.0;
    private double averageBonusPointsPrevMonth = 0.0;
    private double averageBonusPoints = 0.0;

    /**
     * Constructor for sports club.
     * @param allTrainers
     * @param allTrainings
     */
    public SportsClub(ArrayList<Trainer> allTrainers, ArrayList<Training> allTrainings) {
        this.id = nextId;
        nextId++;
        this.allMembers = new ArrayList<>();
        this.allTrainers = allTrainers;
        this.allTrainings = allTrainings;

        // Initialize memberships
        this.memberships = new ArrayList<>();
        memberships.add(new FullMembership(this));
        memberships.add(new StandardMembership(this));
        for (Membership membership : memberships) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Membership type: " + membership.getClass().getSimpleName());
        }

        SportsClubSystem.getInstance().addSportsClub(this);
        SportsClubLogger.getInstance().getLogger().log(Level.INFO, "New sports club has been registered");
    }

    /**
     * Get the ID of the sports club.
     * @return The ID of the sports club.
     */
    public int getId() {
        return id;
    }

    /**
     * Get all members of the club.
     * @return list of members.
     */
    public List<Member> getAllMembers() {
        return allMembers;
    }

    /**
     * Method to add new members to the system.
     * @param member
     */
    public void addNewMember(Member member) {
        SportsClubLogger.getInstance().getLogger().log(Level.INFO, "New member is added to the system");
        allMembers.add(member);
    }


    /**
     * Get all trainers of the club.
     * @return list of trainers.
     */
    public List<Trainer> getAllTrainers() {
        return allTrainers;
    }

    /**
     * Method to hire new trainers if they are not hired.
     * @param trainer
     */
    public void hireNewTrainer(Trainer trainer) {
        if (allTrainers.contains(trainer)) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Trainer is already working here, cannot be added to the system again");
            throw new IllegalArgumentException("The trainer is already working here");
        }
        SportsClubLogger.getInstance().getLogger().log(Level.INFO, "New trainer has been hired");
        allTrainers.add(trainer);
    }


    /**
     * Get all trainings of the club.
     * @return list of trainings.
     */
    public List<Training> getAllTrainings() {
        return allTrainings;
    }

    /**
     * Method to add new training to the system.
     * @param training
     */
    public void addNewTraining(Training training) {
        if (allTrainings.contains(training)) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Sports club already has this training, it cannot be added to the system");
            throw new IllegalArgumentException("The training already exists");
        }
        SportsClubLogger.getInstance().getLogger().log(Level.INFO, "New training has been added");
        allTrainings.add(training);
        training.setIsAddedToSportsClub(true);
        training.setSportsClubItBelongsTo(this);
    }

    /**
     * Get all memberships of the club.
     * @return list of memberships.
     */
    public List<Membership> getMemberships() {
        return memberships;
    }

    /**
     * Method to sequence training sessions based on the number of participants and sessions.
     * @return A list of trainings with the largest number of participants, sorted by session count.
     */
    public List<Training> sequenceTrainingSessions() {
        return allTrainings.stream()
                .sorted(Comparator.<Training>comparingInt(training -> training.getTotalParticipants())
                        .thenComparingInt(training -> training.getTotalSessionsNumber()).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Provides an overview of the offered trainings, training sessions, participants, members, and trainers.
     * @return An overview string containing the information.
     */
    public String getOverview() {
        StringBuilder overview = new StringBuilder();
        overview.append("Offered Trainings:\n");
        overview.append(getOfferedTrainingsOverview());
        overview.append("\nTraining Sessions:\n");
        overview.append(getTrainingSessionsOverview());
        overview.append("\nParticipants:\n");
        overview.append(getParticipantsOverview());
        overview.append("\nMembers:\n");
        overview.append(getMembersOverview());
        overview.append("\nTrainers:\n");
        overview.append(getTrainersOverview());
        return overview.toString();
    }

    private String getOfferedTrainingsOverview() {
        List<String> trainingNames = allTrainings.stream()
                .map(Training::getName)
                .collect(Collectors.toList());
        return String.join(", ", trainingNames);
    }

    private String getTrainingSessionsOverview() {
        List<String> sessionDetails = allTrainings.stream()
                .flatMap(training -> training.getTotalSessions().stream())
                .map(session -> session.getName() + " - " + session.getTime().toLocalTime().toString())
                .sorted()
                .collect(Collectors.toList());
        return String.join("\n", sessionDetails);
    }

    private String getParticipantsOverview() {
        List<String> participantNames = allTrainings.stream()
                .flatMap(training -> training.getTotalSessions().stream())
                .flatMap(session -> session.getParticipants().stream())
                .map(Member::getName)
                .distinct()
                .collect(Collectors.toList());
        return String.join(", ", participantNames);
    }

    private String getMembersOverview() {
        List<String> memberNames = allMembers.stream()
                .map(Member::getName)
                .collect(Collectors.toList());
        return String.join(", ", memberNames);
    }

    private String getTrainersOverview() {
        List<String> trainerNames = allTrainers.stream()
                .map(Trainer::getName)
                .collect(Collectors.toList());
        return String.join(", ", trainerNames);
    }

    /**
     * Method to show training sessions arranged by the sports club sorted by time.
     * NB! THIS METHOD IS TESTED IN TrainingSessionExtraTestclass.
     * @return List of training sessions arranged by the sports club sorted by time.
     */
    public List<TrainingSession> getClubSessionsByTime() {
        return allTrainings.stream()
                .filter(Training::isAddedToSportsClub) // Filter only the trainings arranged by the sports club
                .flatMap(training -> training.getTotalSessions().stream())
                .sorted(Comparator.comparing(session -> session.getTime().toLocalTime()))
                .collect(Collectors.toList());
    }

    /**
     * Rank training sessions by type (BEGINNER, INTERMEDIATE; ADVANCED).
     * If the level is the same, the training sessions must be ordered according to the number of participants.
     * If the number of participants is also the same, the training sessions must be ordered by time.
     * @return sorted list of sessions
     */
    public List<TrainingSession> getSortedTrainingSessions() {
        Comparator<TrainingSession> comparator = Comparator
                .comparing(TrainingSession::getTrainingSessionLevel)
                .thenComparingInt(session -> session.getParticipants().size())
                .thenComparing(TrainingSession::getTime);

        // Get all training sessions from all trainings in this sports club
        List<TrainingSession> allSessions = getAllTrainings().stream()
                .flatMap(training -> training.getTotalSessions().stream())
                .toList();

        List<TrainingSession> sortedSessions = allSessions.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        return sortedSessions;
    }

    /**
     * Sorts trainers based on the number of specializations (training types),
     * number of registered members, and alphabetical order of first name.
     * @return List of trainers sorted by the specified criteria.
     */
    public List<Trainer> getSortedTrainers() {
        return allTrainers.stream()
                .sorted(Comparator
                        .<Trainer>comparingInt(trainer -> -trainer.getTotalTrainingTypes())
                        .thenComparingInt(trainer -> -trainer.getTotalTrainerParticipants())
                        .thenComparing(Trainer::getName))
                .collect(Collectors.toList());
    }

    /**
     * Simulate the passage of a month for this sports club.
     * This method removes memberships for all members in each sports club.
     * This method works as helper method for sports club system.
     */
    public void nextMonth() {
        setAverageNumPreviousMonth(calculateAverageSessionsPerParticipant());
        setAverageBonusPointsPrevMonth(calculateAverageBonusPoints());
        for (Member member : allMembers) {
            int regSesPrev = member.getRegisteredSessions().size();
            int sportTypePrev = member.getParticipatedSports().size();
            int bonusPointsPrev = member.getBonusPoints();
            member.setRegisteredSesPrevMonth(regSesPrev);
            member.setParticipatedSportPrevMonth(sportTypePrev);
            member.setBonusPointsPrevMonth(bonusPointsPrev);
            member.getBoughtMemberships().clear();
            member.getRegisteredSessions().clear();
            member.getParticipatedSports().clear();
            member.setBonusPoints(0);
        }
        this.allMembers.clear();
    }

    /**
     * Get sports club average participation.
     * @return number.
     */
    public double getAverageNum() {
        averageNum = calculateAverageSessionsPerParticipant();
        return averageNum;
    }

    /**
     * Get average points for members.
     * @return number.
     */
    public double getAverageBonusPoints() {
        averageBonusPoints = calculateAverageBonusPoints();
        return averageBonusPoints;
    }

    /**
     * Sets and stores number of previous month for discounts.
     * @param averageNum
     */
    public void setAverageNumPreviousMonth(double averageNum) {
        this.averageNumPreviousMonth = averageNum;
    }

    public void setAverageBonusPointsPrevMonth(double averageBonusPoints) {
        this.averageBonusPointsPrevMonth = averageBonusPoints;
    }

    /**
     * Get average number from previous month.
     * @return number.
     */
    public double getAverageNumPreviousMonth() {
        return averageNumPreviousMonth;
    }

    public double getAverageBonusPointsPrevMonth() {
        return averageBonusPointsPrevMonth;
    }

    /**
     * Calculate the average number of sessions per participant.
     * Total number of registration on session / number of people.
     * @return The average sessions per participant.
     */
    public double calculateAverageSessionsPerParticipant() {
        if (allMembers.isEmpty()) {
            return 0.0;
        }
        int totalSessions = 0;
        int totalParticipants = 0;
        for (Member member : allMembers) {
            // Filter the member's registered sessions that belong to this sports club
            long clubSessionsCount = member.getRegisteredSessions().stream()
                    .filter(session -> session.getTraining().sportsClubItBelongsTo() == this)
                    .count();
            totalSessions += clubSessionsCount;
            totalParticipants += 1;
        }
        return totalSessions > 0 ? (double) totalSessions / totalParticipants : 0.0;
    }

    /**
     * Calculate the average number of points per participant.
     * Total number of registration on points / number of people.
     * @return The average points per participant.
     */
    public double calculateAverageBonusPoints() {
        if (allMembers.isEmpty()) {
            return 0.0;
        }
        int totalPoints = 0;
        int totalParticipants = 0;

        for (Member member : allMembers) {
            totalPoints += member.getBonusPoints();
            totalParticipants++;
        }

        return totalParticipants > 0 ? (double) totalPoints / totalParticipants : 0.0;
    }

    public static class Builder {

        private final List<Trainer> allTrainers = new ArrayList<>();
        private final List<Training> allTrainings = new ArrayList<>();

        /**
         * Builder with trainer, adds to the list.
         * @param trainer
         * @return this.
         */
        public Builder withTrainer(Trainer trainer) {
            hireNewTrainer(trainer);
            return this;
        }

        /**
         * Method to hire new trainers if they are not hired.
         * @param trainer
         */
        private void hireNewTrainer(Trainer trainer) {
            if (allTrainers.contains(trainer)) {
                // we can throw customized exception later
                throw new IllegalArgumentException("The trainer is already working here");
            }
            allTrainers.add(trainer);
        }

        /**
         * Builder with training, adds to the list.
         * @param training
         * @return this.
         */
        public Builder withTraining(Training training) {
            addNewTraining(training);
            training.setIsAddedToSportsClub(true);
            training.setSportsClubItBelongsTo(this.build());
            return this;
        }

        /**
         * Method to add new training to the system.
         * @param training
         */
        private void addNewTraining(Training training) {
            if (allTrainings.contains(training)) {
                // we can throw customized exception later
                throw new IllegalArgumentException("The training already exists");
            }
            allTrainings.add(training);
        }

        /**
         * Byilder for sports club.
         * @return new Sports club.
         */
        public SportsClub build() {
            SportsClubLogger.getInstance().getLogger().log(Level.INFO, "New sports club created");
            SportsClub sportsClub = new SportsClub((ArrayList<Trainer>) allTrainers,
                    (ArrayList<Training>) allTrainings);
            SportsClubSystem.getInstance().addSportsClub(sportsClub);
            return sportsClub;
        }
    }

}
