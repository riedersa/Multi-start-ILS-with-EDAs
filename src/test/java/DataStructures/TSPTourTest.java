package DataStructures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TSPTourTest {

    @Test
    public void testTwoOptSwap() {
        int[] tourArray = {1, 2, 3, 4, 5};
        TSPTour tour = new TSPTour(tourArray, 0);

        TSPTour tspTour2 = tour.twoOptSwap(0, 0);
        Assertions.assertArrayEquals(tourArray, tspTour2.getTour());

        tspTour2 = tour.twoOptSwap(0, 1);
        Assertions.assertArrayEquals(new int[]{2, 1, 3, 4, 5}, tspTour2.getTour());

        tspTour2 = tour.twoOptSwap(0, 4);
        Assertions.assertArrayEquals(new int[]{5, 4, 3, 2, 1}, tspTour2.getTour());

        tspTour2 = tour.twoOptSwap(2, 4);
        Assertions.assertArrayEquals(new int[]{1, 2, 5, 4, 3}, tspTour2.getTour());

        tspTour2 = tour.twoOptSwap(1, 3);
        Assertions.assertArrayEquals(new int[]{1, 4, 3, 2, 5}, tspTour2.getTour());

        tspTour2 = tour.twoOptSwap(2, 3);
        Assertions.assertArrayEquals(new int[]{1, 2, 4, 3, 5}, tspTour2.getTour());

        tspTour2 = tour.twoOptSwap(1, 3);
        Assertions.assertArrayEquals(new int[]{1, 4, 3, 2, 5}, tspTour2.getTour());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            tour.twoOptSwap(-1, 0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            tour.twoOptSwap(0, 5);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            tour.twoOptSwap(2, 1);
        });
    }


    @Test
    public void testGetLength() {
        TSPTour tspTour1 = new TSPTour(new int[]{1, 4, 3, 2, 5}, 8);
        Assertions.assertEquals(8, tspTour1.getLength());

        TSPTour tspTour2 = new TSPTour(new int[]{1, 4, 3, 2, 5});
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            tspTour2.getLength();
        });
        tspTour2.setLength(7);
        Assertions.assertEquals(7, tspTour2.getLength());
    }
}