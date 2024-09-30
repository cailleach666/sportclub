package ee.taltech.iti0202.member;


import ee.taltech.iti0202.SportsClubSystem;
import ee.taltech.iti0202.membership.Membership;
import ee.taltech.iti0202.membership.StandardMembership;
import ee.taltech.iti0202.sportsclub.SportsClub;
import ee.taltech.iti0202.trainer.Trainer;
import ee.taltech.iti0202.training.Training;
import ee.taltech.iti0202.training.TrainingSession;
import ee.taltech.iti0202.training.TrainingSessionLevel;
import ee.taltech.iti0202.training.TrainingSportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {

    private SportsClubSystem sportsClubSystem;
    private Member member1;
    private Member member2;
    private SportsClub sportsClub;
    private SportsClub sportsClub2;
    private Membership standardMembership;
    private Training trainingGym;
    private TrainingSession gymSession1;
    private TrainingSession gymSession2;
    private Trainer gymTrainer;
    private Trainer newBalley;
    private Training balley;
    private TrainingSession balleySes;

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
        gymTrainer = new Trainer.Builder()
                .withName("Steve")
                .addTrainingType(TrainingSportType.GYM)
                .build();
        sportsClub = new SportsClub.Builder()
                .build();
        standardMembership = new StandardMembership(sportsClub);
        trainingGym = new Training.Builder()
                .withName("Gym class")
                .withTrainingSportType(TrainingSportType.GYM)
                .build();
    }

    /**
     * Helper method to test few methods.
     */
    private void setUpTrainingSessions() {
        sportsClub.addNewTraining(trainingGym);
        trainingGym.assignTrainer(gymTrainer);
        gymSession1 = new TrainingSession.Builder()
                .withTraining(trainingGym)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(12, 15)
                .withMaxParticipants(6)
                .build();
        gymSession2 = new TrainingSession.Builder()
                .withTraining(trainingGym)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(10, 30)
                .withMaxParticipants(1)
                .build();
    }

    @org.junit.jupiter.api.Test
    void testGetMemberName() {
        assertEquals("Anton", member1.getName());
    }

    @org.junit.jupiter.api.Test
    void testGetMemberBudget() {
        assertEquals(75.0, member1.getBudget());
    }

    @org.junit.jupiter.api.Test
    void testGetMemberBudgetBuilderIsNullBudgetIsZero() {
        assertEquals(0.0, member2.getBudget());
    }

    @org.junit.jupiter.api.Test
    void testGetMemberIfHasMembershipMustBeFalseByDefault() {
        assertFalse(member1.hasMembership());
    }

    @org.junit.jupiter.api.Test
    void testMemberCanBuyMembership() {
        member1.buyMembership(standardMembership);
        assertTrue(member1.getBoughtMemberships().contains(standardMembership));
        assertTrue(member1.hasMembership());
    }

    @org.junit.jupiter.api.Test
    void testMemberCanBuyMembershipMoneyDecreased() {
        member1.buyMembership(standardMembership);
        assertEquals(35.0, member1.getBudget());
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotBuyMembershipAlreadyHasOne() {
        member1.buyMembership(standardMembership);
        assertThrows(IllegalArgumentException.class,
                () -> member1.buyMembership(standardMembership),
                "Member has already gotten this membership");
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotBuyMembershipNotEnoughBudget() {
        try {
            member2.buyMembership(standardMembership);
        } catch (IllegalArgumentException ignored) {

        }
        assertFalse(member2.getBoughtMemberships().contains(standardMembership));
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotBuyMembershipNotEnoughBudgetThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> member2.buyMembership(standardMembership),
                "Insufficient funds to purchase membership");
    }

    @org.junit.jupiter.api.Test
    void testMemberWorkBudgetIncreased() {
        member2.work();
        assertEquals(50.0, member2.getBudget());
    }

    @org.junit.jupiter.api.Test
    void testMemberWorkedCanBuysMembership() {
        member2.work();
        member2.buyMembership(standardMembership);
        assertTrue(member2.getBoughtMemberships().contains(standardMembership));
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotBuysMembershipAlreadyHaveOneThrowsException() {
        member1.work();
        member1.buyMembership(standardMembership);
        assertThrows(IllegalArgumentException.class,
                () -> member1.buyMembership(standardMembership),
                "Cannot buy membership, already have one"
        );
    }

    @org.junit.jupiter.api.Test
    void testMemberCanRegisterToTrainingSession() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession1);
        assertTrue(member1.getRegisteredSessions().contains(gymSession1));
    }

    @org.junit.jupiter.api.Test
    void testMemberCanRegisterToTrainingSessionSessionHasParticipant() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession1);
        assertTrue(gymSession1.getParticipants().contains(member1));
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotRegisterToTrainingSessionNoMembership() {
        setUpTrainingSessions();
        try {
            member1.registerToTrainingSession(gymSession1);
        } catch (NullPointerException ignored) {

        }
        assertFalse(member1.getRegisteredSessions().contains(gymSession1));
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotRegisterToTrainingSessionNoMembershipThrowsException() {
        setUpTrainingSessions();
        assertThrows(NullPointerException.class,
                () -> member1.registerToTrainingSession(gymSession1),
                "Member does not have a membership to the same sports club as the training session"
        );
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotRegisterToTrainingSessionAlreadyRegisteredThrowsException() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession1);
        assertThrows(IllegalArgumentException.class,
                () -> member1.registerToTrainingSession(gymSession1),
                "Member is already registered for this training session"
        );
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotRegisterToTrainingSessionSessionReachedMaxParticipant() {
        member1.buyMembership(standardMembership);
        member2.work();
        member2.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession2);
        assertThrows(IllegalStateException.class,
                () -> member2.registerToTrainingSession(gymSession2),
                "Training session has reached maximum participants"
        );
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotRegisterToTrainingSessionSessionNeededFullMembership() {
        member1.buyMembership(standardMembership);
        sportsClub.addNewTraining(trainingGym);
        trainingGym.assignTrainer(gymTrainer);
        TrainingSession gymSession3 = new TrainingSession.Builder()
                .withTraining(trainingGym)
                .withLevel(TrainingSessionLevel.ADVANCED)
                .withTime(12, 30)
                .withMaxParticipants(4)
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> member1.registerToTrainingSession(gymSession3),
                "Member with standard package can only participate in BEGINNER or INTERMEDIATE training sessions"
        );
    }

    @org.junit.jupiter.api.Test
    void testMemberCanUnregisterFromTrainingSession() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession1);
        member1.unregisterFromTrainingSession(gymSession1);
        assertFalse(gymSession1.getParticipants().contains(member1));
    }

    @org.junit.jupiter.api.Test
    void testMemberCanUnregisterFromTrainingSessionParticipantRemoved() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession1);
        member1.unregisterFromTrainingSession(gymSession1);
        assertEquals(0, gymSession1.getParticipants().size());
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotUnregisterFromTrainingSessionNotParticipant() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession1);
        assertThrows(IllegalArgumentException.class,
                () -> member2.unregisterFromTrainingSession(gymSession2),
                "Member is not registered for this training session."
        );
    }

    @org.junit.jupiter.api.Test
    void testMemberGetRegisteredSessions() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession1);
        member1.registerToTrainingSession(gymSession2);
        assertEquals(2, member1.getRegisteredSessions().size());
    }

    @org.junit.jupiter.api.Test
    void testGenerateTrainingSessionsList() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        member1.registerToTrainingSession(gymSession1);
        member1.registerToTrainingSession(gymSession2);
        String expectedList = "Gym class - 12:15\nGym class - 10:30";
        assertEquals(expectedList, member1.generateTrainingSessionsList());
    }

    @org.junit.jupiter.api.Test
    void testGenerateTrainingSessionsListNoTrainingSessions() {
        member1.buyMembership(standardMembership);
        setUpTrainingSessions();
        String expectedList = "No training sessions registered";
        assertEquals(expectedList, member1.generateTrainingSessionsList());
    }

    @org.junit.jupiter.api.Test
    void testBuildMemberWithNullNameThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            new Member.Builder()
                    .build();
        });
    }

    /**
     * Main tests for 2nd part.
     * However, previous tests may have been changed a bit.
     */

    void setUpSecondClubAndTrainings() {
        newBalley = new Trainer.Builder()
                .withName("Ball")
                .addTrainingType(TrainingSportType.GYM)
                .build();
        sportsClub2 = new SportsClub.Builder()
                .withTrainer(newBalley)
                .build();
        balley = new Training.Builder()
                .withName("Balley")
                .withTrainingSportType(TrainingSportType.GYM).build();
        balley.assignTrainer(newBalley);
        sportsClub2.addNewTraining(balley);
        balleySes = new TrainingSession.Builder()
                .withTraining(balley)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withMaxParticipants(2)
                .withTime(2, 30)
                .build();
    }

    @org.junit.jupiter.api.Test
    void testMemberCanSeeIfBoughtMembership() {
        member1.buyMembership(standardMembership);
        assertTrue(member1.hasObtainedThisMembership(standardMembership));
    }

    @org.junit.jupiter.api.Test
    void testMemberHasMembershipInOneSportsClub() {
        member1.buyMembership(standardMembership);
        setUpSecondClubAndTrainings();
        assertTrue(sportsClub.getAllMembers().contains(member1));
        assertFalse(sportsClub2.getAllMembers().contains(member1));
    }

    @org.junit.jupiter.api.Test
    void testMemberHasMembershipCanBuySecondMembership() {
        member1.buyMembership(standardMembership);
        member1.work();
        setUpSecondClubAndTrainings();
        member1.buyMembership(new StandardMembership(sportsClub2));
        assertEquals(2, member1.getBoughtMemberships().size());
    }

    @org.junit.jupiter.api.Test
    void testMemberCanRegisterToSessionsDifferentMembership() {
        member1.buyMembership(standardMembership);
        member1.work();
        setUpSecondClubAndTrainings();
        member1.buyMembership(new StandardMembership(sportsClub2));
        member1.registerToTrainingSession(balleySes);
    }

    @org.junit.jupiter.api.Test
    void testMemberCanNotRegisterToSessionsDifferentMembership() {
        member1.buyMembership(standardMembership);
        setUpSecondClubAndTrainings();
        assertThrows(NullPointerException.class, () ->
            member1.registerToTrainingSession(balleySes),
                    "Member does not have a membership to the same sports club as the training session"
        );
    }

    @org.junit.jupiter.api.Test
    void testMemberCanRegisterToSessionsDifferentClubs() {
        member1.buyMembership(standardMembership);
        member1.work();
        setUpTrainingSessions();
        setUpSecondClubAndTrainings();
        member1.buyMembership(new StandardMembership(sportsClub2));
        member1.registerToTrainingSession(gymSession1);
        member1.registerToTrainingSession(balleySes);
        assertEquals(2, member1.getRegisteredSessions().size());
    }

    @Test
    void testSearchSessionsByLevel() {
        setUpTrainingSessions();
        List<TrainingSession> beginnerSessions = member1.searchSessionsByLevel(TrainingSessionLevel.BEGINNER);
        assertEquals(2, beginnerSessions.size());
    }

    @Test
    void testSearchSessionsByTime() {
        setUpTrainingSessions();
        LocalDateTime startTime = LocalDateTime.now().withHour(10).withMinute(0);
        LocalDateTime endTime = LocalDateTime.now().withHour(13).withMinute(0);
        List<TrainingSession> sessionsInRange = member1.searchSessionsByTime(startTime, endTime);
        assertEquals(2, sessionsInRange.size());
    }

    @Test
    void testSearchSessionsByType() {
        setUpTrainingSessions();
        List<TrainingSession> gymSessions = member1.searchSessionsByType(TrainingSportType.GYM);
        assertEquals(2, gymSessions.size());
    }
}
