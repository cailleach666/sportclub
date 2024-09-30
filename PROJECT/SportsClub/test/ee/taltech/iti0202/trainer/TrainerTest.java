package ee.taltech.iti0202.trainer;

import ee.taltech.iti0202.training.TrainingSportType;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrainerTest {

    private Trainer gymTrainer;
    private Trainer tennisTrainer;
    private Trainer proTrainer;

    /**
     * Creating setUp method.
     */
    @BeforeEach
    void setUp() {
        gymTrainer = new Trainer.Builder()
                .withName("Steve")
                .addTrainingType(TrainingSportType.GYM)
                .build();
        tennisTrainer = new Trainer.Builder()
                .withName("Elen")
                .build();
        proTrainer = new Trainer.Builder()
                .withName("Sigma")
                .addTrainingType(TrainingSportType.GYM)
                .addTrainingType(TrainingSportType.SWIMMING)
                .build();
    }

    @org.junit.jupiter.api.Test
    public void testGetTrainerName() {
        assertEquals("Steve", gymTrainer.getName());
    }

    @org.junit.jupiter.api.Test
    public void testCanAddTrainingType() {
        gymTrainer.addTrainingType(TrainingSportType.BOX);
        assertEquals(2, gymTrainer.getTrainingTypes().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanAddTrainingTypeIsInTheList() {
        gymTrainer.addTrainingType(TrainingSportType.BOX);
        assertTrue(gymTrainer.getTrainingTypes().contains(TrainingSportType.GYM));
        assertTrue(gymTrainer.getTrainingTypes().contains(TrainingSportType.BOX));
    }

    @org.junit.jupiter.api.Test
    public void testCanNotAddTrainingTypeTrainerAlreadyHasOne() {
        try {
            gymTrainer.addTrainingType(TrainingSportType.GYM);
        } catch (IllegalArgumentException ignored) {

        }
        assertEquals(1, gymTrainer.getTrainingTypes().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanNotAddTrainingTypeTrainerAlreadyHasOneThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> gymTrainer.addTrainingType(TrainingSportType.GYM),
                "Training type is already in the list."
        );
    }

    @org.junit.jupiter.api.Test
    public void testCanRemoveTrainingType() {
        gymTrainer.removeTrainingType(TrainingSportType.GYM);
        assertEquals(0, gymTrainer.getTrainingTypes().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanRemoveTrainingTypeHasOtherType() {
        proTrainer.removeTrainingType(TrainingSportType.GYM);
        assertEquals(1, proTrainer.getTrainingTypes().size());
    }

    @org.junit.jupiter.api.Test
    public void testCanNotRemoveTrainingTypeNotInTheListThrowsException() {
        gymTrainer.removeTrainingType(TrainingSportType.GYM);
        assertThrows(IllegalArgumentException.class,
                () -> gymTrainer.removeTrainingType(TrainingSportType.GYM),
                "Training type is not in the list."
        );
    }

    @org.junit.jupiter.api.Test
    public void getTrainerTrainingTypesDefaultZero() {
        assertEquals(0, tennisTrainer.getTrainingTypes().size());
    }

    @org.junit.jupiter.api.Test
    public void getTrainerTrainingTypesDefaultNotZero() {
        assertEquals(2, proTrainer.getTrainingTypes().size());
    }

    @org.junit.jupiter.api.Test
    void testBuildTrainerWithNoNameThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            new Trainer.Builder()
                    .build();
        });
    }

    @org.junit.jupiter.api.Test
    void testBuildTrainerAddTypeAlreadyAddedThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Trainer.Builder()
                    .withName("Alex")
                    .addTrainingType(TrainingSportType.GYM)
                    .addTrainingType(TrainingSportType.GYM)
                    .build();
        });
    }
}
