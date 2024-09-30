package ee.taltech.iti0202;

import ee.taltech.iti0202.membership.FullMembership;
import ee.taltech.iti0202.membership.Membership;
import ee.taltech.iti0202.membership.StandardMembership;
import ee.taltech.iti0202.training.GroupTrainingSession;
import ee.taltech.iti0202.training.OnlineTrainingSession;
import ee.taltech.iti0202.training.PersonalTrainingSession;
import ee.taltech.iti0202.training.Training;
import ee.taltech.iti0202.training.TrainingSession;
import ee.taltech.iti0202.training.TrainingSessionLevel;
import ee.taltech.iti0202.training.TrainingSportType;
import org.junit.jupiter.api.Test;
import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;
import ee.taltech.iti0202.trainer.Trainer;
import org.junit.jupiter.api.BeforeEach;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mainly tests for the second and third parts of the project.
 * Separated to make testing easier so that I can see faster where to check.
 */

public class TrainingSessionExtraTest {

    private SportsClubSystem sportsClubSystem;
    private Training trainingGym;
    private Training privateTraining;
    private Training groupTraining;
    private Training onlineTraining;
    private Trainer gymTrainer;
    private Trainer privateTrainer;
    private Trainer groupTrainer;
    private Trainer onlineTrainer;
    private TrainingSession gymSession1;
    private TrainingSession gymSession2;
    private TrainingSession personalSession;
    private GroupTrainingSession groupSession;
    private GroupTrainingSession groupSession2;
    private OnlineTrainingSession onlineSession;
    private Member member1;
    private Member member2;
    private SportsClub sportsClub;
    private Membership standardMembership;
    private Membership fullMembership;

    /**
     * Creating setUp method.
     */
    @BeforeEach
    void setUp() {
        sportsClubSystem = SportsClubSystem.getInstance();
        sportsClubSystem.getSportsClubsInArea().clear();
        sportsClubSystem.getTrainingSessions().clear();

        member1 = new Member.Builder()
                .withName("Anton")
                .withBudget(75.0)
                .build();
        member2 = new Member.Builder()
                .withName("Nike")
                .build();

        trainingGym = new Training.Builder()
                .withName("Gym class")
                .withTrainingSportType(TrainingSportType.GYM)
                .build();
        privateTraining = new Training.Builder()
                .withName("Private class")
                .withTrainingSportType(TrainingSportType.GYM)
                .build();
        groupTraining = new Training.Builder()
                .withName("Group class")
                .withTrainingSportType(TrainingSportType.SWIMMING)
                .build();
        onlineTraining = new Training.Builder()
                .withName("Group class")
                .withTrainingSportType(TrainingSportType.GYM)
                .build();

        gymTrainer = new Trainer.Builder()
                .withName("Steve")
                .addTrainingType(TrainingSportType.GYM)
                .build();
        privateTrainer = new Trainer.Builder()
                .withName("Lucky")
                .addTrainingType(TrainingSportType.GYM)
                .addTrainingType(TrainingSportType.SWIMMING)
                .addTrainingType(TrainingSportType.TENNIS)
                .build();
        groupTrainer = new Trainer.Builder()
                .withName("Sammy")
                .addTrainingType(TrainingSportType.GYM)
                .addTrainingType(TrainingSportType.SWIMMING)
                .addTrainingType(TrainingSportType.TENNIS)
                .build();
        onlineTrainer = new Trainer.Builder()
                .withName("Samantha")
                .addTrainingType(TrainingSportType.GYM)
                .build();

        sportsClub = new SportsClub.Builder()
                .withTraining(trainingGym)
                .build();

        sportsClub.hireNewTrainer(gymTrainer);
        sportsClub.hireNewTrainer(privateTrainer);
        sportsClub.hireNewTrainer(onlineTrainer);
        sportsClub.hireNewTrainer(groupTrainer);

        sportsClub.addNewTraining(privateTraining);
        sportsClub.addNewTraining(groupTraining);
        sportsClub.addNewTraining(onlineTraining);

        trainingGym.assignTrainer(gymTrainer);
        privateTraining.assignTrainer(privateTrainer);
        groupTraining.assignTrainer(groupTrainer);
        onlineTraining.assignTrainer(onlineTrainer);

        gymSession1 = new TrainingSession.Builder()
                .withTraining(trainingGym)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(12, 15)
                .withMaxParticipants(6)
                .build();
        gymSession2 = new TrainingSession.Builder()
                .withTraining(trainingGym)
                .withLevel(TrainingSessionLevel.INTERMEDIATE)
                .withTime(10, 30)
                .withMaxParticipants(1)
                .build();
        personalSession = new PersonalTrainingSession.Builder()
                .withTraining(privateTraining)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(14, 15)
                .build();
        groupSession = new GroupTrainingSession.Builder()
                .withTraining(groupTraining)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(9, 30)
                .build();
        groupSession2 = new GroupTrainingSession.Builder()
                .withTraining(groupTraining)
                .withLevel(TrainingSessionLevel.ADVANCED)
                .withTime(10, 30)
                .build();
        onlineSession = new OnlineTrainingSession.Builder()
                .withTraining(onlineTraining)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(19, 30)
                .build();

        standardMembership = new StandardMembership(sportsClub);
        fullMembership = new FullMembership.Builder()
                .ofSportsClub(sportsClub)
                .build();
    }

    @Test
    void testGetTrainingSessionPrice() {
        assertEquals(0.0, gymSession1.getPrice());
    }

    @Test
    void testGetPrivateTrainingSessionPrice() {
        assertEquals(30.0, personalSession.getPrice());
    }

    @Test
    void testGetGroupTrainingSessionPrice() {
        assertEquals(0.0, groupSession.getPrice());
    }

    @Test
    void testGetOnlineTrainingSessionPrice() {
        assertEquals(5.0, onlineSession.getPrice());
    }

    @Test
    void testGetMaxParticipantsPersonalSession() {
        assertEquals(1, personalSession.getMaxParticipants());
    }

    @Test
    void testGetMaxParticipantsGroupSession() {
        assertEquals(25, groupSession.getMaxParticipants());
    }

    @Test
    void testGetMaxParticipantsOnlineSession() {
        assertEquals(Integer.MAX_VALUE, onlineSession.getMaxParticipants());
    }

    @Test
    void testInvalidTimeForPersonalSession() {
        assertThrows(IllegalArgumentException.class, () ->
                new PersonalTrainingSession.Builder()
                        .withTraining(privateTraining)
                        .withLevel(TrainingSessionLevel.BEGINNER)
                        .withTime(19, 0)
                        .build(),
                "Personal training sessions can only take place between 14:00 and 18:00."
        );
    }

    @Test
    void testInvalidTimeForPersonalSessionNotBuilder() {
        LocalDateTime startTime = LocalDateTime.now().withHour(8).withMinute(0);
        assertThrows(IllegalArgumentException.class, () ->
                        new PersonalTrainingSession(privateTraining, TrainingSessionLevel.BEGINNER, startTime),
                "Personal training sessions can only take place between 9:00 and 15:00."

        );
    }

    @Test
    void testInvalidTimeForGroupSession() {
        assertThrows(IllegalArgumentException.class, () ->
                new GroupTrainingSession.Builder()
                        .withTraining(groupTraining)
                        .withLevel(TrainingSessionLevel.BEGINNER)
                        .withTime(8, 0)
                        .build(),
                "Personal training sessions can only take place between 9:00 and 15:00."
        );
    }

    @Test
    void testInvalidTimeForGroupSessionNotBuilder() {
        LocalDateTime startTime = LocalDateTime.now().withHour(8).withMinute(0);
        assertThrows(IllegalArgumentException.class, () ->
                new GroupTrainingSession(groupTraining, TrainingSessionLevel.BEGINNER, startTime),
                "Personal training sessions can only take place between 9:00 and 15:00."

        );
    }

    @Test
    void testSearchSessionsByLevel() {
        List<TrainingSession> beginnerSessions = member1.searchSessionsByLevel(TrainingSessionLevel.BEGINNER);
        assertEquals(4, beginnerSessions.size());
    }

    @Test
    void testSearchSessionsByTime() {
        LocalDateTime startTime = LocalDateTime.now().withHour(10).withMinute(0);
        LocalDateTime endTime = LocalDateTime.now().withHour(13).withMinute(0);
        List<TrainingSession> sessionsInRange = member1.searchSessionsByTime(startTime, endTime);
        assertEquals(3, sessionsInRange.size());
    }

    @Test
    void testSearchSessionsByDatOfTheWeek() {
        personalSession.setDate(2024, 6, 19);
        onlineSession.setDate(2024, 6, 20);
        List<TrainingSession> sessionsInRange = member1
                .searchSessionsByDayOfWeek(DayOfWeek.WEDNESDAY);
        assertEquals(1, sessionsInRange.size());
    }

    @Test
    void testSearchSessionsByDateRange() {
        personalSession.setDate(2024, 6, 19);
        onlineSession.setDate(2024, 6, 21);
        LocalDate startDate = LocalDate.of(2024, 6, 17);
        LocalDate endDate = LocalDate.of(2024, 6, 22);
        List<TrainingSession> sessionsInRange = member1.searchSessionsByDateRange(startDate, endDate);
        assertEquals(2, sessionsInRange.size());
    }

    @Test
    void testSearchSessionsByType() {
        List<TrainingSession> gymSessions = member1.searchSessionsByType(TrainingSportType.GYM);
        assertEquals(4, gymSessions.size());
    }

    @Test
    void testGetClubSessionsByTime() {
        List<TrainingSession> sessionsByTime = sportsClub.getClubSessionsByTime();
        assertEquals(groupSession, sessionsByTime.get(0));
        assertEquals(gymSession2, sessionsByTime.get(1));
        assertEquals(groupSession2, sessionsByTime.get(2));
        assertEquals(gymSession1, sessionsByTime.get(3));
        assertEquals(personalSession, sessionsByTime.get(4));
        assertEquals(onlineSession, sessionsByTime.get(5));
    }

    @Test
    void testGetClubSessionsByTimeSizeOfAllSessions() {
        List<TrainingSession> sessionsByTime = sportsClub.getClubSessionsByTime();
        assertEquals(6, sessionsByTime.size());
    }

    @Test
    void testMemberMoneyDecreasedBuysSession() {
        member1.buyMembership(standardMembership);
        member1.registerToTrainingSession(personalSession);
        assertEquals(5.0, member1.getBudget());
    }

    @Test
    void testMemberMoneyNotDecreasedBuysGroupSession() {
        member1.buyMembership(standardMembership);
        member1.registerToTrainingSession(groupSession);
        assertEquals(35.0, member1.getBudget());
    }

    @Test
    void testMemberCanNotRegisterToSessionNotEnoughBudget() {
        member1.buyMembership(standardMembership);
        member1.registerToTrainingSession(personalSession);
        PersonalTrainingSession personalSession2 = new PersonalTrainingSession.Builder()
                .withTraining(privateTraining)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(14, 15)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                        member1.registerToTrainingSession(personalSession2),
                "Insufficient funds to register to this session"
        );
    }

    @Test
    void testMemberHasFullMembershipGetsDiscount() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(personalSession);
        assertEquals(15.0, member1.getBudget());
    }

    @Test
    void testMemberHasFullMembershipGetsDiscountSecondTime() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(personalSession);
        PersonalTrainingSession personalSession2 = new PersonalTrainingSession.Builder()
                .withTraining(privateTraining)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(14, 15)
                .build();
        member1.work();
        member1.registerToTrainingSession(personalSession2);
        assertEquals(45.0, member1.getBudget());
    }

    @Test
    void testGetMemberBonusPointsMustBeZeroByDefault() {
        assertEquals(0, member1.getBonusPoints());
    }

    @Test
    void testMemberBonusPointsAddedAfterRegistrationFullMembership() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(personalSession);
        assertEquals(20, member1.getBonusPoints());
    }

    @Test
    void testMemberBonusPointsAddedAfterRegistrationStandardMembership() {
        member1.buyMembership(standardMembership);
        member1.registerToTrainingSession(personalSession);
        assertEquals(10, member1.getBonusPoints());
    }

    @Test
    void testMemberBonusPointsAddedAfterRegistrationFullMembership2() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(onlineSession);
        assertEquals(8, member1.getBonusPoints());
    }

    @Test
    void testMemberBonusPointsAddedAfterRegistrationStandardMembership2() {
        member1.buyMembership(standardMembership);
        member1.registerToTrainingSession(onlineSession);
        assertEquals(4, member1.getBonusPoints());
    }

    @Test
    void testMemberBonusPointsAddedAfterRegistrationFullMembership3() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(groupSession);
        assertEquals(4, member1.getBonusPoints());
    }

    @Test
    void testMemberBonusPointsAddedAfterRegistrationStandardMembership3() {
        member1.buyMembership(standardMembership);
        member1.registerToTrainingSession(groupSession);
        assertEquals(2, member1.getBonusPoints());
    }

    @Test
    void testMemberBonusPointsAddedAfterRegistrationFullMembershipPointsStack() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        assertEquals(24, member1.getBonusPoints());
    }

    @Test
    void testGetSortedTrainingSessionsSize() {
        assertEquals(6, sportsClub.getSortedTrainingSessions().size());
    }

    @Test
    void testGetSortedTrainingSessionsOrder() {
        assertEquals(groupSession, sportsClub.getSortedTrainingSessions().get(0));
        assertEquals(gymSession1, sportsClub.getSortedTrainingSessions().get(1));
        assertEquals(personalSession, sportsClub.getSortedTrainingSessions().get(2));
        assertEquals(onlineSession, sportsClub.getSortedTrainingSessions().get(3));
        assertEquals(gymSession2, sportsClub.getSortedTrainingSessions().get(4));
        assertEquals(groupSession2, sportsClub.getSortedTrainingSessions().get(5));
    }

    @Test
    void testGetTotalTrainerParticipants() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(personalSession);
        assertEquals(1, privateTrainer.getTotalTrainerParticipants());
        assertEquals(0, gymTrainer.getTotalTrainerParticipants());
        assertEquals(0, groupTrainer.getTotalTrainerParticipants());
    }

    @Test
    void testGetTotalTrainerParticipantsExtra() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(groupSession);
        member2.work();
        member2.buyMembership(standardMembership);
        member2.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        assertEquals(1, privateTrainer.getTotalTrainerParticipants());
        assertEquals(2, groupTrainer.getTotalTrainerParticipants());
        assertEquals(0, gymTrainer.getTotalTrainerParticipants());
    }

    @Test
    void testGetTotalTrainerParticipantsDifferentSessions() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(groupSession2);
        assertEquals(2, groupTrainer.getTotalTrainerParticipants());
    }

    @Test
    void testGetSortedTrainersSize() {
        assertEquals(4, sportsClub.getSortedTrainers().size());
    }

    @Test
    void testGetSortedTrainersOrder() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(groupSession2);
        member2.work();
        member2.work();
        member2.buyMembership(standardMembership);
        member2.registerToTrainingSession(personalSession);
        assertEquals(groupTrainer.getName(), sportsClub.getSortedTrainers().get(0).getName());
        assertEquals(privateTrainer.getName(), sportsClub.getSortedTrainers().get(1).getName());
        assertEquals(onlineTrainer.getName(), sportsClub.getSortedTrainers().get(2).getName());
        assertEquals(gymTrainer.getName(), sportsClub.getSortedTrainers().get(3).getName());
    }

    @Test
    void testNextMonthCanClearMembership() {
        member2.work();
        member1.buyMembership(fullMembership);
        member2.buyMembership(standardMembership);
        assertEquals(1, member1.getBoughtMemberships().size());
        assertEquals(1, member2.getBoughtMemberships().size());
        sportsClubSystem.passMonthForAllClubs();
        assertEquals(0, member1.getBoughtMemberships().size());
        assertEquals(0, member2.getBoughtMemberships().size());
    }

    @Test
    void testMembersCanBuyMembershipAgainAfterOneMonth() {
        member1.buyMembership(fullMembership);
        sportsClubSystem.passMonthForAllClubs();
        member1.work();
        member1.buyMembership(fullMembership);
        assertEquals(1, member1.getBoughtMemberships().size());
        assertEquals(1, sportsClub.getAllMembers().size());
    }

    @Test
    void testMonthPassedMustBeZeroMembers() {
        member1.buyMembership(standardMembership);
        sportsClubSystem.passMonthForAllClubs();
        assertEquals(0, sportsClub.getAllMembers().size());
    }

    @Test
    void testGetAverageSessionsPerParticipant() {
        member2.work();
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        member2.registerToTrainingSession(groupSession);
        assertEquals(1.5, sportsClub.getAverageNum());
        member2.work();
        member2.registerToTrainingSession(onlineSession);
        assertEquals(2, sportsClub.getAverageNum());
    }

    @Test
    void testGetAverageSessionsPerParticipantZeroByDefault() {
        assertEquals(0, sportsClub.getAverageNum());
    }

    /**
     * Test nextMonth() method, includes:
     * Test method which calculates the average for sports club. After new month it is updated.
     * Test method which stores the registered sessions number of previous month.
     */
    @Test
    void testGetAverageSessionsPerParticipantNextMonth() {
        member2.work();
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        assertEquals(1.0, sportsClub.getAverageNum());
        assertEquals(2, member1.getRegisteredSessions().size());
        sportsClub.nextMonth();
        assertEquals(1.0, sportsClub.getAverageNumPreviousMonth());
        assertEquals(2, member1.getRegisteredSesPrevMonth());
        assertEquals(0, member1.getRegisteredSessions().size());
        member1.work();
        member1.work();
        member2.work();
        PersonalTrainingSession newPersonalSess = new PersonalTrainingSession.Builder()
                .withTraining(privateTraining)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(14, 15)
                .build();
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        member1.registerToTrainingSession(newPersonalSess);
        assertEquals(0.5, sportsClub.getAverageNum());
        assertEquals(1, member1.getRegisteredSessions().size());
        sportsClub.nextMonth();
        assertEquals(0.5, sportsClub.getAverageNumPreviousMonth());
    }

    @Test
    void testGetMemberParticipatedSportsType() {
        member1.buyMembership(fullMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        member1.registerToTrainingSession(groupSession2);
        assertEquals(2, member1.getParticipatedSports().size());
        assertEquals(0, member1.getParticipatedSportPrevMonth());
        sportsClub.nextMonth();
        member1.work();
        member1.buyMembership(standardMembership);
        assertEquals(2, member1.getParticipatedSportPrevMonth());
        assertEquals(0, member1.getParticipatedSports().size());
    }

    /**
     * Test member gets discount.
     */
    @Test
    void testGetParticipationDiscountMembership() {
        member2.work();
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        assertEquals(1.0, sportsClub.getAverageNum());
        assertEquals(2, member1.getRegisteredSessions().size());
        sportsClub.nextMonth();
        assertEquals(1.0, sportsClub.getAverageNumPreviousMonth());
        assertEquals(6.0, sportsClub.getAverageBonusPointsPrevMonth());
        assertEquals(2, member1.getRegisteredSesPrevMonth());
        assertEquals(0, member1.getRegisteredSessions().size());
        member1.work();
        member1.work();
        member2.work();
        assertEquals(105.0, member1.getBudget());
        assertEquals(60.0, member2.getBudget());
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        assertEquals(85.6, member1.getBudget());
        assertEquals(20.0, member2.getBudget());
    }

    @Test
    void testGetBonusPointsPreviousMonth() {
        member1.buyMembership(standardMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        assertEquals(12, member1.getBonusPoints());
        sportsClub.nextMonth();
        assertEquals(0, member1.getBonusPoints());
        assertEquals(12, member1.getBonusPointsPrevMonth());
        member1.work();
        member1.buyMembership(standardMembership);
        sportsClub.nextMonth();
        assertEquals(0, member1.getBonusPointsPrevMonth());
    }

    @Test
    void testGetAveragePointsPerParticipantZeroByDefault() {
        assertEquals(0, sportsClub.getAverageBonusPoints());
    }

    @Test
    void testGetAveragePointsPerParticipant() {
        member2.work();
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        member2.registerToTrainingSession(groupSession);
        assertEquals(7, sportsClub.getAverageBonusPoints());
    }

    @Test
    void testGetAveragePointsPerParticipantPreviousMonth() {
        member2.work();
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        member1.registerToTrainingSession(groupSession);
        member1.registerToTrainingSession(personalSession);
        member2.registerToTrainingSession(groupSession);
        sportsClub.nextMonth();
        member1.work();
        member2.work();
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        assertEquals(0, sportsClub.getAverageBonusPoints());
        assertEquals(7, sportsClub.getAverageBonusPointsPrevMonth());
    }

    @Test
    void testGetTrainingSessionDate() {
        personalSession.setDate(2024, 6, 19);
        assertEquals("2024-06-19", personalSession.getFormattedDate());
    }

    @Test
    void testGetSessionDayOfWeek() {
        personalSession.setDate(2024, 6, 19);
        assertEquals(DayOfWeek.WEDNESDAY, personalSession.getDayOfWeek());
    }
}
