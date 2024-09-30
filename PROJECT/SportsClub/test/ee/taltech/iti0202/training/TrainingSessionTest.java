package ee.taltech.iti0202.training;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;
import ee.taltech.iti0202.trainer.Trainer;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainingSessionTest {

    private Training trainingGym;
    private Training trainingSwimming;
    private Trainer gymTrainer;
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
        trainingSwimming = new Training.Builder()
                .withName("Swimming")
                .withTrainingSportType(TrainingSportType.SWIMMING)
                .build();
        gymTrainer = new Trainer.Builder()
                .withName("Steve")
                .addTrainingType(TrainingSportType.GYM)
                .build();
        proTrainer = new Trainer.Builder()
                .withName("Sigma")
                .addTrainingType(TrainingSportType.GYM)
                .addTrainingType(TrainingSportType.SWIMMING)
                .build();
        sportsClub = new SportsClub.Builder()
                .withTraining(trainingGym)
                .build();
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
    void testGetSessionTraining() {
        assertEquals(trainingGym, gymSession1.getTraining());
    }

    @org.junit.jupiter.api.Test
    void testGetSessionNameMustBeTrainingName() {
        assertEquals("Gym class", gymSession1.getName());
    }

    @org.junit.jupiter.api.Test
    void testGetSessionTrainerMustGiveTrainingTrainer() {
        assertEquals(gymTrainer, gymSession1.getTrainer());
    }

    @org.junit.jupiter.api.Test
    void testGetSessionLevel() {
        assertEquals(TrainingSessionLevel.BEGINNER, gymSession1.getTrainingSessionLevel());
    }

    @org.junit.jupiter.api.Test
    void testGetSessionSportsTypeMustBeTrainingSportType() {
        assertEquals(TrainingSportType.GYM, gymSession1.getTrainingSportType());
    }

    @org.junit.jupiter.api.Test
    void testCanGetIsAddedToSportsClubFalseByDefault() {
        assertFalse(trainingSwimming.isAddedToSportsClub());
    }

    @org.junit.jupiter.api.Test
    void testCanGetIsAddedToSportsBecomesTrueAddedToClub() {
        sportsClub.addNewTraining(trainingSwimming);
        assertTrue(trainingSwimming.isAddedToSportsClub());
    }

    @org.junit.jupiter.api.Test
    void testGetSessionDescription() {
        String expectedDescription = "Training: Gym class (Trainer: Steve, Sport: GYM)"
                + ", Time: 12:15, Max Participants: 6";
        assertEquals(expectedDescription, gymSession1.getDescription());
    }

    @org.junit.jupiter.api.Test
    void testGetSessionTime() {
        LocalDateTime time = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(12, 15));
        assertEquals(time, gymSession1.getTime());
    }

    @org.junit.jupiter.api.Test
    void testGetSessionMaxParticipants() {
        assertEquals(6, gymSession1.getMaxParticipants());
    }

    /**
     * Small part of the functionality.
     * Test if members are added.
     */
    @org.junit.jupiter.api.Test
    void testGetSessionParticipants() {
        gymSession1.addParticipant(member1);
        assertEquals(1, gymSession1.getParticipants().size());
    }

    @org.junit.jupiter.api.Test
    void testSessionHasReachedMaxParticipants() {
        gymSession2.addParticipant(member1);
        assertTrue(gymSession2.hasReachedMaxParticipants());
    }

    /**
     * Small part of the functionality.
     * Test if members are added.
     */
    @org.junit.jupiter.api.Test
    void testSessionCanAddParticipant() {
        gymSession1.addParticipant(member1);
        assertTrue(gymSession1.getParticipants().contains(member1));
    }

    /**
     * Small part of the functionality.
     * Test if members can be removed.
     */
    @org.junit.jupiter.api.Test
    void testSessionCanRemoveParticipant() {
        gymSession1.addParticipant(member1);
        gymSession1.removeParticipant(member1);
        assertEquals(0, gymSession1.getParticipants().size());
    }

    @org.junit.jupiter.api.Test
    void testCreateSessionBuilderWithoutTrainerThrowsException() {
        assertThrows(IllegalStateException.class,
                () -> new TrainingSession.Builder()
                        .withTraining(trainingSwimming)
                        .withLevel(TrainingSessionLevel.BEGINNER)
                        .withTime(12, 15)
                        .withMaxParticipants(6)
                        .build(),
                "Training cannot be conducted without a trainer."
        );
    }

    @org.junit.jupiter.api.Test
    void testCreateSessionWithoutTrainerThrowsException() {
        LocalDateTime time = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(12, 15));
        assertThrows(IllegalStateException.class,
                () -> new TrainingSession(trainingSwimming, TrainingSessionLevel.BEGINNER, time, 7),
                "Training cannot be conducted without a trainer."
        );
    }

    @org.junit.jupiter.api.Test
    void testCreateSessionWithNullName() {
        assertThrows(IllegalStateException.class,
                () -> new TrainingSession.Builder()
                        .withLevel(TrainingSessionLevel.BEGINNER)
                        .withTime(12, 15)
                        .withMaxParticipants(6)
                        .build(),
                "Training parent cannot be null."
        );
    }

    @org.junit.jupiter.api.Test
    void testCreateSessionWithLevelNull() {
        assertThrows(NullPointerException.class,
                () -> new TrainingSession.Builder()
                        .withTraining(trainingGym)
                        .withTime(12, 15)
                        .withMaxParticipants(6)
                        .build(),
                "Level of difficulty cannot be null."
        );
    }

    @org.junit.jupiter.api.Test
    void testCreateSessionWithNegativeMaxParticipants() {
        assertThrows(IllegalArgumentException.class,
                () -> new TrainingSession.Builder()
                        .withTraining(trainingGym)
                        .withLevel(TrainingSessionLevel.BEGINNER)
                        .withTime(12, 15)
                        .withMaxParticipants(-6)
                        .build(),
                "Maximum participants must be greater than zero."
        );
    }

    @org.junit.jupiter.api.Test
    void testCreateSessionWithNoMaxParticipants() {
        assertThrows(NullPointerException.class,
                () -> new TrainingSession.Builder()
                        .withTraining(trainingGym)
                        .withLevel(TrainingSessionLevel.BEGINNER)
                        .withTime(12, 15)
                        .build(),
                "Maximum participants cannot be null."
        );
    }

    @org.junit.jupiter.api.Test
    void testGetDurationIsOneHourThirtyMinutes() {
        Duration expectedDuration = Duration.ofHours(1).plusMinutes(30);
        assertEquals(expectedDuration, gymSession1.getDuration());
    }

    @org.junit.jupiter.api.Test
    void testGetEndTime() {
        LocalDateTime expectedEndTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(13, 45));
        assertEquals(expectedEndTime, gymSession1.getEndTime());
    }

    @org.junit.jupiter.api.Test
    void testGetWhatSportsClubItBelongsTo() {
        Training newTraining = new Training.Builder()
                .withName("Skiing")
                .withTrainingSportType(TrainingSportType.GYM)
                .build();
        newTraining.assignTrainer(gymTrainer);
        sportsClub.addNewTraining(newTraining);
        TrainingSession newSession = new TrainingSession.Builder()
                .withTraining(newTraining)
                .withLevel(TrainingSessionLevel.BEGINNER)
                .withTime(12, 00)
                .withMaxParticipants(3)
                .build();
        assertEquals(sportsClub, newSession.getWhatSportsClubItBelongsTo());
    }

    @org.junit.jupiter.api.Test
    void testGetPrice() {
        assertEquals(0, gymSession1.getPrice());
    }

    @org.junit.jupiter.api.Test
    void testSetPriceWithNegativeValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> gymSession1.setPrice(-50.0),
                "Price cannot be negative."
        );
    }

    @org.junit.jupiter.api.Test
    void testSetPriceWithValidValue() {
        double newPrice = 20.0;
        gymSession1.setPrice(newPrice);
        assertEquals(newPrice, gymSession1.getPrice());
    }

}
