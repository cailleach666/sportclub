package ee.taltech.iti0202.sportsclub;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.membership.Membership;
import ee.taltech.iti0202.membership.StandardMembership;
import ee.taltech.iti0202.trainer.Trainer;
import ee.taltech.iti0202.training.Training;
import ee.taltech.iti0202.training.TrainingSession;
import ee.taltech.iti0202.training.TrainingSessionLevel;
import ee.taltech.iti0202.training.TrainingSportType;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SportsClubTest {

    /**
     * Test which includes all classes. Can be considered as main test.
     */
    private Training trainingGym;
    private Training trainingTennis;
    private Training trainingSwimming;
    private Trainer gymTrainer;
    private Trainer tennisTrainer;
    private Trainer proTrainer;
    private TrainingSession gymSession1;
    private TrainingSession gymSession2;
    private TrainingSession tennisSession1;
    private TrainingSession swimmingSession1;
    private Member member1;
    private Member member2;
    private Member member3;
    private SportsClub sportsClub;
    private Membership standardMembership;

    /**
     * Creating setUp method.
     */
    @BeforeEach
    void setUp() {
        member1 = new Member.Builder()
                .withName("Anton")
                .withBudget(75.0)
                .build();
        member2 = new Member.Builder()
                .withName("Nike")
                .build();
        member3 = new Member.Builder()
                .withName("Sass")
                .withBudget(75.0)
                .build();
        trainingGym = new Training.Builder()
                .withName("Gym class")
                .withTrainingSportType(TrainingSportType.GYM)
                .build();
        trainingTennis = new Training.Builder()
                .withName("Tennis playground")
                .withTrainingSportType(TrainingSportType.TENNIS)
                .build();
        trainingSwimming = new Training.Builder()
                .withName("Swimming")
                .withTrainingSportType(TrainingSportType.SWIMMING)
                .build();
        gymTrainer = new Trainer.Builder()
                .withName("Steve")
                .addTrainingType(TrainingSportType.GYM)
                .build();
        tennisTrainer = new Trainer.Builder()
                .withName("Elen")
                .addTrainingType(TrainingSportType.TENNIS)
                .build();
        proTrainer = new Trainer.Builder()
                .withName("Sigma")
                .addTrainingType(TrainingSportType.GYM)
                .addTrainingType(TrainingSportType.SWIMMING)
                .build();
        sportsClub = new SportsClub.Builder()
                .withTrainer(gymTrainer)
                .build();
        standardMembership = new StandardMembership(sportsClub);
    }

    /**
     * Helper method to test methods.
     */
    private void setUpMembersBuyMembership() {
        member2.work();
        member1.buyMembership(standardMembership);
        member2.buyMembership(standardMembership);
        member3.buyMembership(standardMembership);
    }

    /**
     * Helper method to test methods.
     */
    private void setUpGymTrainingSessions() {
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

    /**
     * Helper method to test methods.
     */
    private void setUpTennisSession() {
        sportsClub.addNewTraining(trainingTennis);
        trainingTennis.assignTrainer(tennisTrainer);
        tennisSession1 = new TrainingSession.Builder()
                .withTraining(trainingTennis)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(15, 45)
                .withMaxParticipants(2)
                .build();
    }

    /**
     * Helper method to test methods.
     */
    private void setUpSwimmingSession() {
        sportsClub.addNewTraining(trainingSwimming);
        trainingSwimming.assignTrainer(proTrainer);
        swimmingSession1 = new TrainingSession.Builder()
                .withTraining(trainingSwimming)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(9, 45)
                .withMaxParticipants(8)
                .build();
    }

    /**
     * Test if sports club contains two memberships.
     */
    @org.junit.jupiter.api.Test
    void testSportsClubContainsTwoMemberships() {
        assertEquals(2, sportsClub.getMemberships().size());
    }

    @org.junit.jupiter.api.Test
    public void testGetAllMembersZeroByDefault() {
        assertEquals(0, sportsClub.getAllMembers().size());
    }

    @org.junit.jupiter.api.Test
    public void testGetAllMembersMembersAddedAfterBuyingMembership() {
        member1.buyMembership(standardMembership);
        assertTrue(sportsClub.getAllMembers().contains(member1));
    }

    @org.junit.jupiter.api.Test
    public void testGetAllMembersAddedAfterBuyingMembership() {
        member1.buyMembership(standardMembership);
        assertEquals(1, sportsClub.getAllMembers().size());
    }

    @org.junit.jupiter.api.Test
    public void testGetAllTrainers() {
        assertEquals(1, sportsClub.getAllTrainers().size());
    }

    @org.junit.jupiter.api.Test
    public void testGetTrainersHasTrainer() {
        assertTrue(sportsClub.getAllTrainers().contains(gymTrainer));
    }

    @org.junit.jupiter.api.Test
    public void testCanHireNewTrainer() {
        sportsClub.hireNewTrainer(tennisTrainer);
        assertTrue(sportsClub.getAllTrainers().contains(tennisTrainer));
    }

    @org.junit.jupiter.api.Test
    public void testCanHireNewTrainerAddsToList() {
        sportsClub.hireNewTrainer(tennisTrainer);
        assertEquals(2, sportsClub.getAllTrainers().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanNotHireNewTrainerAlreadyHired() {
        try {
            sportsClub.hireNewTrainer(gymTrainer);
        } catch (IllegalArgumentException ignored) {

        }
        assertEquals(1, sportsClub.getAllTrainers().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanNotHireNewTrainerAlreadyHiredThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> sportsClub.hireNewTrainer(gymTrainer),
                "The trainer is already working here"
        );
    }

    @org.junit.jupiter.api.Test
    public void testGetAllTrainings() {
        sportsClub.addNewTraining(trainingGym);
        assertEquals(1, sportsClub.getAllTrainings().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanAddNewTraining() {
        sportsClub.addNewTraining(trainingGym);
        sportsClub.addNewTraining(trainingTennis);
        assertEquals(2, sportsClub.getAllTrainings().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanNotAddTrainingAlreadyExists() {
        try {
            sportsClub.addNewTraining(trainingGym);
        } catch (IllegalArgumentException ignored) {

        }
        assertEquals(1, sportsClub.getAllTrainings().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanNotAddTrainingAlreadyExistsThrowsException() {
        sportsClub.addNewTraining(trainingGym);
        assertThrows(IllegalArgumentException.class,
                () -> sportsClub.addNewTraining(trainingGym),
                "The training already exist"
        );
    }

    /**
     * Helper method to test methods.
     * All needed trainers are hired. Sessions are made.
     * Tests below must check that sessions count to specific training.
     * Other trainings are not affected by other sessions.
     */
    private void setUpMembersTrainingsRegister() {
        sportsClub.hireNewTrainer(tennisTrainer);
        sportsClub.hireNewTrainer(proTrainer);
        setUpMembersBuyMembership();
        setUpGymTrainingSessions();
        setUpTennisSession();
        setUpSwimmingSession();
        member1.registerToTrainingSession(gymSession1);
        member1.registerToTrainingSession(gymSession2);
        member2.registerToTrainingSession(gymSession1);
        member2.registerToTrainingSession(tennisSession1);
        member1.registerToTrainingSession(tennisSession1);
        member1.registerToTrainingSession(swimmingSession1);
        member2.registerToTrainingSession(swimmingSession1);
        member3.registerToTrainingSession(swimmingSession1);
    }

    @org.junit.jupiter.api.Test
    void test() {
        setUpGymTrainingSessions();
        assertEquals(sportsClub.getId(), gymSession1.getWhatSportsClubItBelongsTo().getId());
    }


    /**
     * Test that gym training has only two sessions. Other sessions are not taken into account.
     */
    @org.junit.jupiter.api.Test
    public void testGetGymTrainingAllSessions() {
        setUpMembersTrainingsRegister();
        assertEquals(2, trainingGym.getTotalSessionsNumber());
    }

    @org.junit.jupiter.api.Test
    public void testGetGymTrainingAllParticipants() {
        setUpMembersTrainingsRegister();
        assertEquals(3, trainingGym.getTotalParticipants());
    }

    /**
     * Test that tennis training has only one session. Other sessions are not taken into account.
     */
    @org.junit.jupiter.api.Test
    public void testGetTennisTrainingAllSessions() {
        setUpMembersTrainingsRegister();
        assertEquals(1, trainingTennis.getTotalSessionsNumber());
    }

    @org.junit.jupiter.api.Test
    public void testGetTennisTrainingAllParticipants() {
        setUpMembersTrainingsRegister();
        assertEquals(2, trainingTennis.getTotalParticipants());
    }

    /**
     * Test that swimming training has only one session. Other sessions are not taken into account.
     */
    @org.junit.jupiter.api.Test
    public void testGetSwimmingTrainingAllSessions() {
        setUpMembersTrainingsRegister();
        assertEquals(1, trainingSwimming.getTotalSessionsNumber());
    }

    @org.junit.jupiter.api.Test
    public void testGetSwimmingTrainingAllParticipants() {
        setUpMembersTrainingsRegister();
        assertEquals(3, trainingSwimming.getTotalParticipants());
    }

    /**
     * Sports club has 3 trainings. Sorted list of trainings must be the same length.
     */
    @org.junit.jupiter.api.Test
    public void testSequenceTrainingSessions() {
        setUpMembersTrainingsRegister();
        List<Training> sequencedTrainings = sportsClub.sequenceTrainingSessions();

        assertEquals(3, sequencedTrainings.size());
    }

    /**
     * Gym and swimming trainings have the same amount of participants, but gym has more sessions.
     * Correct order must be: gym training, swimming training, tennis training.
     */
    @org.junit.jupiter.api.Test
    public void testSequenceTrainingSessionsGetPlaces() {
        setUpMembersTrainingsRegister();
        List<Training> sequencedTrainings = sportsClub.sequenceTrainingSessions();

        assertEquals(trainingGym.getName(), sequencedTrainings.get(0).getName());
        assertEquals(trainingSwimming.getName(), sequencedTrainings.get(1).getName());
        assertEquals(trainingTennis.getName(), sequencedTrainings.get(2).getName());
    }

    @org.junit.jupiter.api.Test
    public void testGetOverview() {
        String expectedOverview = "Offered Trainings:\n"
                + "Gym class, Tennis playground, Swimming\n"
                + "Training Sessions:\n"
                + "Gym class - 10:30\n"
                + "Gym class - 12:15\n"
                + "Swimming - 09:45\n"
                + "Tennis playground - 15:45\n"
                + "Participants:\n"
                + "Anton, Nike, Sass\n"
                + "Members:\n"
                + "Anton, Nike, Sass\n"
                + "Trainers:\n"
                + "Steve, Elen, Sigma";
        setUpMembersTrainingsRegister();
        String actualOverview = sportsClub.getOverview();
        assertEquals(expectedOverview, actualOverview);
    }

    @org.junit.jupiter.api.Test
    void testCreateSportClubAddTrainerTwiceThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SportsClub.Builder()
                        .withTrainer(gymTrainer)
                        .withTrainer(gymTrainer)
                        .build(),
                "he trainer is already working here"
        );
    }

    @org.junit.jupiter.api.Test
    void testCreateSportClubAddTrainingTwiceThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new SportsClub.Builder()
                        .withTraining(trainingGym)
                        .withTraining(trainingGym)
                        .build(),
                "The training already exists"
        );
    }
}
