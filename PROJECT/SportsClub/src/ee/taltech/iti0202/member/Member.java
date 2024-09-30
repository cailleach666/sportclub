package ee.taltech.iti0202.member;

import ee.taltech.iti0202.SportsClubSystem;
import ee.taltech.iti0202.logger.SportsClubLogger;
import ee.taltech.iti0202.membership.Membership;
import ee.taltech.iti0202.sportsclub.SportsClub;
import ee.taltech.iti0202.strategy.BonusPointsBasedDiscountStrategy;
import ee.taltech.iti0202.strategy.CombinedDiscountStrategy;
import ee.taltech.iti0202.strategy.DiscountStrategy;
import ee.taltech.iti0202.strategy.ParticipationBasedDiscountStrategy;
import ee.taltech.iti0202.strategy.SportsTypeDiscountStrategy;
import ee.taltech.iti0202.training.GroupTrainingSession;
import ee.taltech.iti0202.training.OnlineTrainingSession;
import ee.taltech.iti0202.training.PersonalTrainingSession;
import ee.taltech.iti0202.training.TrainingSession;
import ee.taltech.iti0202.training.TrainingSessionLevel;
import ee.taltech.iti0202.training.TrainingSportType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Class for member. Members can buy membership.
 * If not enough budget, members can go working. Members can register to training sessions.
 */

public class Member {

    private final String name;
    private double budget;
    private boolean isFirstSessionFree;
    static final double SALARY = 50;
    private int bonusPoints;
    private final List<TrainingSession> registeredSessions;
    private final List<Membership> boughtMemberships;
    private static final int FULL_PERSONAL_SES = 20;
    private static final int STANDARD_PERSONAL_SES = 10;
    private static final int FULL_ONLINE_SES = 8;
    private static final int STANDARD_ONLINE_SES = 4;
    private static final int FULL_GROUP_SES = 4;
    private static final int STANDARD_GROUP_SES = 2;
    private static final double FULL_PRIVATE_SES_PRICE = 20.0;
    private int registeredSesPrevMonth = 0;
    private DiscountStrategy participationBasedDiscount;
    private DiscountStrategy bonusPointsBasedDiscount;
    private DiscountStrategy combinedDiscountStrategy;
    private DiscountStrategy sportsTypeDiscountStrategy;
    private int bonusPointsPrevMonth = 0;
    private List<TrainingSportType> participatedSports;
    private int participatedSportPrevMonth = 0;

    /**
     * Constructor for member.
     * @param name
     * @param budget
     */
    public Member(String name, double budget) {
        this.name = Objects.requireNonNull(name);
        this.budget = budget;
        this.registeredSessions = new ArrayList<>();
        this.participatedSports = new ArrayList<>();
        this.boughtMemberships = new ArrayList<>();
        this.isFirstSessionFree = true;
        this.bonusPoints = 0;
    }

    /**
     * Get name.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get budget
     * @return budget.
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Get bonus points.
     * @return integer bonus points.
     */
    public Integer getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    /**
     * Check if member has membership.
     * @return boolean
     */
    public boolean hasMembership() {
        return !getBoughtMemberships().isEmpty();
    }

    /**
     * Boolean to check if members has membership of this training session club.
     * @param sportsClub
     * @return boolean
     */
    public boolean hasMembershipToSportsClub(SportsClub sportsClub) {
        return boughtMemberships.stream()
                .anyMatch(membership -> membership.getSportsClub() == sportsClub);
    }

    /**
     * Boolean to check if members has membership of this training session club.
     * @param membership
     * @return boolean
     */
    public boolean hasObtainedThisMembership(Membership membership) {
        return boughtMemberships.contains(membership);
    }

    /**
     * Method to buy membership if enough money or ahs not gotten one before.
     */
    public void buyMembership(Membership membership) {
        double membershipPrice = membership.getPrice();
        SportsClub sportsClub = membership.getSportsClub();

        if (budget < membership.getPrice()) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Member has insufficient funds to purchase membership");
            throw new IllegalArgumentException("Insufficient funds to purchase membership");
        }
        if (hasMembershipToSportsClub(membership.getSportsClub())) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Member has already gotten this membership");
            throw new IllegalArgumentException("Member has already gotten this membership");
        }

        double discountPercent = getDiscountStrategy(this, sportsClub);
        membershipPrice = membershipPrice * (1 - discountPercent / 100);

        boughtMemberships.add(membership);
        budget -= membershipPrice;

        membership.getSportsClub().addNewMember(this);
        SportsClubLogger.getInstance().getLogger()
                .log(Level.INFO, "Member has bought membership. Their budget is decreased by the membership price");
    }

    /**
     * Get discount strategy.
     * @param member
     * @param sportsClub
     * @return strategy.
     */
    private double getDiscountStrategy(Member member, SportsClub sportsClub) {
        this.participationBasedDiscount = new ParticipationBasedDiscountStrategy();
        this.bonusPointsBasedDiscount = new BonusPointsBasedDiscountStrategy();
        this.sportsTypeDiscountStrategy = new SportsTypeDiscountStrategy();
        this.combinedDiscountStrategy = new CombinedDiscountStrategy(participationBasedDiscount,
                bonusPointsBasedDiscount, sportsTypeDiscountStrategy);
        return combinedDiscountStrategy.calculateDiscount(member, sportsClub);
    }

    /**
     * Work to get money. Method made for fun. Everyone deserves to buy membership.
     */
    public void work() {
        budget += SALARY;
        SportsClubLogger.getInstance().getLogger().log(Level.INFO, "Member worked and got 50 bucks");
    }

    /**
     * Method to register to the training session.
     */
    public void registerToTrainingSession(TrainingSession trainingSession) {
        if (!hasMembership()) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Member does not have any membership");
            throw new NullPointerException("Member does not have any membership");
        }
        if (!hasMembershipToSportsClub(trainingSession.getWhatSportsClubItBelongsTo())) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Member does not have a membership to the "
                            + "same sports club as the training session");
            throw new NullPointerException("Member does not have a membership to the same "
                    + "sports club as the training session");
        }
        if (registeredSessions.contains(trainingSession)) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Member is already registered for this training session");
            throw new IllegalArgumentException("Member is already registered for this training session");
        }
        if (trainingSession.hasReachedMaxParticipants()) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Training session has reached maximum participants");
            throw new IllegalStateException("Training session has reached maximum participants");
        }

        double sessionPrice = trainingSession.getPrice();
        Membership sessionMembership = findMembershipForSportsClub(trainingSession.getWhatSportsClubItBelongsTo());
        if (hasMembership() && hasMembershipToSportsClub(trainingSession.getWhatSportsClubItBelongsTo())) {
            if (sessionMembership.getType().equals("standard")) {
                TrainingSessionLevel sessionLevel = trainingSession.getTrainingSessionLevel();
                if (sessionLevel == TrainingSessionLevel.ADVANCED) {
                    SportsClubLogger.getInstance().getLogger()
                            .log(Level.INFO, "Member with standard package can only participate in "
                                    + "BEGINNER or INTERMEDIATE training sessions");
                    throw new IllegalArgumentException("Member with standard package can only participate in "
                            + "BEGINNER or INTERMEDIATE training sessions");
                }
            }
        }

        if (trainingSession instanceof PersonalTrainingSession) {
            if (hasMembership() && hasMembershipToSportsClub(trainingSession.getWhatSportsClubItBelongsTo())) {
                if (isFirstSessionFree && sessionMembership.getType().equals("full")) {
                    sessionPrice = 0.0;
                    isFirstSessionFree = false;
                } else if (!isFirstSessionFree && sessionMembership.getType().equals("full")) {
                    sessionPrice = FULL_PRIVATE_SES_PRICE;
                }
            }
        }
        if (budget < sessionPrice) {
            SportsClubLogger.getInstance().getLogger()
                    .log(Level.INFO, "Insufficient funds to register to this session");
            throw new IllegalArgumentException("Insufficient funds to register to this session");
        }

        SportsClubLogger.getInstance().getLogger()
                .log(Level.INFO, "Member has registered to a new training session");
        budget -= sessionPrice;
        accumulateBonusPoints(trainingSession);
        registeredSessions.add(trainingSession);
        trainingSession.addParticipant(this);
    }

    /**
     * Method to unregister from a training session.
     */
    public void unregisterFromTrainingSession(TrainingSession trainingSession) {
        if (!registeredSessions.contains(trainingSession)) {
            throw new IllegalArgumentException("Member is not registered for this training session.");
        }

        SportsClubLogger.getInstance().getLogger()
                .log(Level.INFO, "Member has unregistered from a training session");
        registeredSessions.remove(trainingSession);
        trainingSession.removeParticipant(this);
    }

    /**
     * Helper method to find the membership for the specified sports club.
     * @param sportsClub The sports club to find the membership for.
     * @return The membership for the sports club.
     */
    private Membership findMembershipForSportsClub(SportsClub sportsClub) {
        return boughtMemberships.stream()
                .filter(membership -> membership.getSportsClub() == sportsClub)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Member does not have a membership for this sports club"));
    }

    /**
     * Accumulate bonus points based on the type of training session.
     * NB! Method is tested in TrainingSessionExtraTest.
     * @param trainingSession The training session to accumulate bonus points for.
     */
    public void accumulateBonusPoints(TrainingSession trainingSession) {
        Membership sessionMembership = findMembershipForSportsClub(trainingSession.getWhatSportsClubItBelongsTo());
        if (sessionMembership.getType().equals("full")) {
            if (trainingSession instanceof PersonalTrainingSession) {
                bonusPoints += FULL_PERSONAL_SES;
            } else if (trainingSession instanceof GroupTrainingSession) {
                bonusPoints += FULL_GROUP_SES;
            } else if (trainingSession instanceof OnlineTrainingSession) {
                bonusPoints += FULL_ONLINE_SES;
            }
        } else { // Regular membership
            if (trainingSession instanceof PersonalTrainingSession) {
                bonusPoints += STANDARD_PERSONAL_SES;
            } else if (trainingSession instanceof GroupTrainingSession) {
                bonusPoints += STANDARD_GROUP_SES;
            } else if (trainingSession instanceof OnlineTrainingSession) {
                bonusPoints += STANDARD_ONLINE_SES;
            }
        }
    }

    /**
     * Get all trainings to which member has registered to.
     * @return list.
     */
    public List<TrainingSession> getRegisteredSessions() {
        return registeredSessions;
    }

    /**
     * Get all sports type a member has participated.
     * @return hashset.
     */
    public List<TrainingSportType> getParticipatedSports() {
        participatedSports = registeredSessions.stream()
                .map(TrainingSession::getTrainingSportType)
                .distinct()
                .collect(Collectors.toList());
        return participatedSports;
    }

    /**
     * Get the number of sessions in previous month.
     * @return number.
     */
    public int getRegisteredSesPrevMonth() {
        return registeredSesPrevMonth;
    }

    public int getParticipatedSportPrevMonth() {
        return participatedSportPrevMonth;
    }

    /**
     * Sets and stores number of sessions in previous month.
     * @param registeredSesPrevMonth
     */
    public void setRegisteredSesPrevMonth(int registeredSesPrevMonth) {
        this.registeredSesPrevMonth = registeredSesPrevMonth;
    }

    public void setParticipatedSportPrevMonth(int participatedSportPrevMonth) {
        this.participatedSportPrevMonth = participatedSportPrevMonth;
    }

    public int getBonusPointsPrevMonth() {
        return bonusPointsPrevMonth;
    }

    public void setBonusPointsPrevMonth(int bonusPointsPrevMonth) {
        this.bonusPointsPrevMonth = bonusPointsPrevMonth;
    }

    /**
     * Get all memberships which member has bought.
     * @return list.
     */
    public List<Membership> getBoughtMemberships() {
        return boughtMemberships;
    }

    /**
     * Generate a string representation of the training sessions registered by the member.
     * @return A string containing the list of training sessions.
     */
    public String generateTrainingSessionsList() {
        if (registeredSessions.isEmpty()) {
            return "No training sessions registered";
        }
        return registeredSessions.stream()
                .map(session -> session.getName() + " - " + session.getTime().toLocalTime().toString())
                .collect(Collectors.joining("\n"));
    }

    /**
     * Get the membership type for the specified sports club.
     *
     * @param sportsClub The sports club to get the membership type for.
     * @return The membership type for the sports club.
     * @throws IllegalArgumentException If member does not have a membership for the specified sports club.
     */
    public Membership getMembershipForSportsClub(SportsClub sportsClub) {
        return boughtMemberships.stream()
                .filter(membership -> membership.getSportsClub() == sportsClub)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Member does not have a membership for this sports club"));
    }

    /**
     * Generate a string containing information about all sports clubs in the area
     * and whether the member has a membership there.
     *
     * NB! Since I used sports club system method, I tested it in SportsClubSystemTest.
     * @param sportsClubSystem The instance of SportsClubSystem containing all sports clubs.
     * @return The string containing information about sports clubs and memberships.
     */
    public String getSportsClubMembershipsInfo(SportsClubSystem sportsClubSystem) {
        StringBuilder info = new StringBuilder();
        info.append("All sports clubs in this area:\n");
        for (SportsClub club : sportsClubSystem.getSportsClubsInArea()) {
            info.append("Sports club ").append(club.getId()).append(" - ");
            if (hasMembershipToSportsClub(club)) {
                Membership membership = getMembershipForSportsClub(club);
                info.append(membership.getType()).append(" membership obtained\n");
            } else {
                info.append("No membership obtained\n");
            }
        }
        return info.toString();
    }

    /**
     * Search for training sessions based on the level.
     * @param level The level of the training session (BEGINNER, INTERMEDIATE, or ADVANCED).
     * @return A list of training sessions matching the specified level.
     */
    public List<TrainingSession> searchSessionsByLevel(TrainingSessionLevel level) {
        return SportsClubSystem.getInstance().getTrainingSessions().stream()
                .filter(session -> session.getTrainingSessionLevel() == level)
                .collect(Collectors.toList());
    }

    /**
     * Search for training sessions based on the time of day.
     * @param startTime The start time of the training session.
     * @param endTime The end time of the training session.
     * @return A list of training sessions within the specified time range.
     */
    public List<TrainingSession> searchSessionsByTime(LocalDateTime startTime, LocalDateTime endTime) {
        return SportsClubSystem.getInstance().getTrainingSessions().stream()
                .filter(session -> session.getTime().isAfter(startTime) && session.getTime().isBefore(endTime))
                .collect(Collectors.toList());
    }

    /**
     * Search for training sessions based on the type of training.
     * @param type The type of training session.
     * @return A list of training sessions matching the specified type.
     */
    public List<TrainingSession> searchSessionsByType(TrainingSportType type) {
        return SportsClubSystem.getInstance().getTrainingSessions().stream()
                .filter(session -> session.getTrainingSportType() == type)
                .collect(Collectors.toList());
    }

    /**
     * Search for training sessions based on the day of the week and date range.
     * @param dayOfWeek The day of the week to search for (e.g., DayOfWeek.MONDAY).
     * @return A list of training sessions matching the specified criteria.
     */
    public List<TrainingSession> searchSessionsByDayOfWeek(DayOfWeek dayOfWeek) {
        return SportsClubSystem.getInstance().getTrainingSessions().stream()
                .filter(session -> session.getDayOfWeek() == dayOfWeek)
                .collect(Collectors.toList());
    }

    /**
     * Search for training sessions within a specified date range.
     * @param startDate The start date (inclusive) of the date range.
     * @param endDate The end date (inclusive) of the date range.
     * @return A list of training sessions within the specified date range.
     */
    public List<TrainingSession> searchSessionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return SportsClubSystem.getInstance().getTrainingSessions().stream()
                .filter(session -> session.getDate().isAfter(startDate) && session.getDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Builder class for Member.
     */
    public static class Builder {
        private String name;
        private double budget;

        /**
         * Set name.
         * @param name
         * @return name.
         */
        public Builder withName(String name) {
            this.name = Objects.requireNonNull(name);
            return this;
        }

        /**
         * Set budget.
         * @param budget
         * @return budget.
         */
        public Builder withBudget(double budget) {
            this.budget = budget;
            return this;
        }

        /**
         * Builder for member.
         * @return new member.
         */
        public Member build() {
            return new Member(name, budget);
        }

    }

}
