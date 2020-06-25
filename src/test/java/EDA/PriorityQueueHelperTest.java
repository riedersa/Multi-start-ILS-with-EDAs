package EDA;

import DataStructures.TSPTour;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.LinkedList;
import java.util.PriorityQueue;

import static org.junit.Assert.*;

public class PriorityQueueHelperTest {

    private LinkedList<TSPTour> tspTours;


    @Before
    public void setUpTours() {
        tspTours = new LinkedList<>();
        TSPTour tsptour1 = new TSPTour(new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 7);
        TSPTour tsptour2 = new TSPTour(new int[]{1, 2, 3, 4, 5, 6, 0, 7}, 4);
        TSPTour tsptour3 = new TSPTour(new int[]{2, 3, 4, 5, 6, 0, 1, 7}, 8);
        TSPTour tsptour4 = new TSPTour(new int[]{3, 4, 5, 6, 0, 1, 2, 7}, 6);

        tspTours.add(tsptour1);
        tspTours.add(tsptour2);
        tspTours.add(tsptour3);
        tspTours.add(tsptour4);
    }

    @Test
    public void testSelect() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);
        PriorityQueueHelper.select(2, pq);

        Assertions.assertEquals(2, pq.size());
        Assertions.assertTrue(pq.contains(tspTours.get(2)));
        Assertions.assertTrue(pq.contains(tspTours.get(0)));
    }


    @Test
    public void testSelect_All() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);
        PriorityQueueHelper.select(5, pq);

        Assertions.assertEquals(4, pq.size());
        Assertions.assertTrue(pq.contains(tspTours.get(2)));
        Assertions.assertTrue(pq.contains(tspTours.get(0)));
        Assertions.assertTrue(pq.contains(tspTours.get(1)));
        Assertions.assertTrue(pq.contains(tspTours.get(3)));
    }

}