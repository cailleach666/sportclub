package ee.taltech.iti0202.training;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;
import ee.taltech.iti0202.trainer.Trainer;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainingTest {

    private Training trainingGym;
    private Training trainingTennis;
    private Training trainingSwimming;
    private Trainer gymTrainer;
    private Trainer tennisTrainer;
    private Trainer proTrainer;
    private TrainingSession gymSession1;
    private TrainingSession gymSession2;
    private TrainingSession swimmingSession1;
    private Member member1;
    private Member member2;
    private SportsClub sportsClub;

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
                .withTraining(trainingGym)
                .build();
    }

    /**
     * Helper method to test few methods.
     */
    private void setUpTrainingSessions() {
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
    void testGetTrainingName() {
        assertEquals("Gym class", trainingGym.getName());
    }

    @org.junit.jupiter.api.Test
    void testGetTrainingSportType() {
        assertEquals(TrainingSportType.GYM, trainingGym.getTrainingSportType());
    }

    @org.junit.jupiter.api.Test
    void testCanAssignTrainer() {
        trainingGym.assignTrainer(gymTrainer);
        assertEquals(gymTrainer, trainingGym.getTrainer());
    }

    @org.junit.jupiter.api.Test
    void testCanAssignTrainerToDifferentTrainings() {
        trainingGym.assignTrainer(proTrainer);
        trainingSwimming.assignTrainer(proTrainer);
        assertEquals(proTrainer, trainingGym.getTrainer());
        assertEquals(proTrainer, trainingSwimming.getTrainer());
    }

    @org.junit.jupiter.api.Test
    void testCanNotAssignTrainerAlreadyHaveTrainer() {
        trainingGym.assignTrainer(gymTrainer);
        assertThrows(IllegalArgumentException.class,
                () -> trainingGym.assignTrainer(proTrainer),
                "Training already has a trainer"
        );
    }

    @org.junit.jupiter.api.Test
    void testCanNotAssignTrainerTrainerCannotConductTrainingType() {
        assertThrows(IllegalArgumentException.class,
                () -> trainingGym.assignTrainer(tennisTrainer),
                "Trainer cannot conduct this training type"
        );
    }

    @org.junit.jupiter.api.Test
    void testGetTrainingDescription() {
        trainingGym.assignTrainer(gymTrainer);
        String expectedDescription = "Training: Gym class (Trainer: Steve, Sport: GYM)";
        assertEquals(expectedDescription, trainingGym.getDescription());
    }

    @org.junit.jupiter.api.Test
    void testGetTrainingDescriptionThrowsExceptionTrainerNotAssigned() {
        assertThrows(IllegalStateException.class,
                () -> trainingGym.getDescription(),
                "No trainer assigned to this training"
        );
    }

    @org.junit.jupiter.api.Test
    void testRegisterTrainingSession() {
        setUpTrainingSessions();
        assertTrue(trainingGym.getTotalSessions().contains(gymSession1));
        assertTrue(trainingGym.getTotalSessions().contains(gymSession2));
    }

    @org.junit.jupiter.api.Test
    void testRegisterTrainingSessionOtherTrainingsAreNotAffected() {
        setUpTrainingSessions();
        assertEquals(0, trainingTennis.getTotalSessionsNumber());
    }

    @org.junit.jupiter.api.Test
    void testGetTotalSessions() {
        setUpTrainingSessions();
        assertEquals(2, trainingGym.getTotalSessionsNumber());
    }

    @org.junit.jupiter.api.Test
    void testGetTotalParticipantsMustBeZeroByDefault() {
        setUpTrainingSessions();
        assertEquals(0, trainingGym.getTotalParticipants());
    }

    @org.junit.jupiter.api.Test
    void testGetTotalParticipantsOneSession() {
        setUpTrainingSessions();
        gymSession1.addParticipant(member1);
        gymSession1.addParticipant(member2);
        assertEquals(2, trainingGym.getTotalParticipants());
    }

    @org.junit.jupiter.api.Test
    void testGetTotalParticipantsAllSession() {
        setUpTrainingSessions();
        gymSession1.addParticipant(member1);
        gymSession1.addParticipant(member2);
        gymSession2.addParticipant(member1);
        assertEquals(3, trainingGym.getTotalParticipants());
    }

    @org.junit.jupiter.api.Test
    void testGetTrainingSummary() {
        setUpTrainingSessions();
        gymSession1.addParticipant(member1);
        gymSession1.addParticipant(member2);
        gymSession2.addParticipant(member1);
        String string = "This training has 2 session(s), 3 participant(s)";
        assertEquals(string, trainingGym.getTrainingSummary());
    }

    @org.junit.jupiter.api.Test
    void testGetTotalParticipantsDoesNotAffectOtherSession() {
        setUpTrainingSessions();
        gymSession1.addParticipant(member1);
        gymSession1.addParticipant(member2);
        gymSession2.addParticipant(member1);
        assertEquals(0, trainingTennis.getTotalParticipants());
    }

    @org.junit.jupiter.api.Test
    void testBuildTrainingWithNullNameThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            new Training.Builder()
                    .withTrainingSportType(TrainingSportType.GYM)
                    .build();
        });
    }

    @org.junit.jupiter.api.Test
    void testBuildTrainingWithTypeNameThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            new Training.Builder()
                    .withName("Basketball")
                    .build();
        });
    }
}
