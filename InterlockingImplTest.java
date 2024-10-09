import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterlockingImplTest {

    private Interlocking interlocking;

    @BeforeEach
    void setUp() {
        interlocking = new InterlockingImpl();
    }



    @Test
    void testMoveTrains() {
        // Setup trains
        interlocking.addTrain("PassengerTrainA", 1, 4);
        interlocking.addTrain("PassengerTrainB", 3, 11);
        interlocking.addTrain("FreightTrainC", 5, 9);

        String[] trainNames = {"PassengerTrainA", "PassengerTrainB", "FreightTrainC"};
        int movedTrains = interlocking.moveTrains(trainNames);
        assertEquals(3, movedTrains); // All trains should move

        assertDoesNotThrow(() -> interlocking.addTrain("PassengerTrainD", 6, 6)); // Train starts and ends at the same section
        String[] singleTrain = {"PassengerTrainD"};
        movedTrains = interlocking.moveTrains(singleTrain);
        assertEquals(1, movedTrains); // Train should reach its destination immediately and be removed

        assertThrows(IllegalArgumentException.class, () -> interlocking.moveTrains(new String[]{"NonExistentTrain"}));
    }

    @Test
    void testGetSection() {
        // Setup train
        interlocking.addTrain("PassengerTrainA", 1, 4);

        assertEquals("PassengerTrainA", interlocking.getSection(1));

        assertThrows(IllegalArgumentException.class, () -> interlocking.getSection(2));
    }

    @Test
    void testGetTrain() {
        interlocking.addTrain("PassengerTrainA", 1, 4);

        assertEquals(1, interlocking.getTrain("PassengerTrainA"));

        assertThrows(IllegalArgumentException.class, () -> interlocking.getTrain("UnknownTrain"));
    }

    @Test
    void testTrainReachesDestination() {
        interlocking.addTrain("PassengerTrainA", 1, 1); // Train starts at and has destination section 1

        String[] trainNames = {"PassengerTrainA"};
        interlocking.moveTrains(trainNames);
        assertThrows(IllegalArgumentException.class, () -> interlocking.getTrain("PassengerTrainA")); // Train should be removed
    }

    @Test
    void testFreightPriority() {
        interlocking.addTrain("PassengerTrainA", 1, 4);
        interlocking.addTrain("FreightTrainB", 2, 5); // Freight is in the next section


        String[] trainNames = {"PassengerTrainA", "FreightTrainB"};
        int movedTrains = interlocking.moveTrains(trainNames);
        assertEquals(1, movedTrains);
    }
}
