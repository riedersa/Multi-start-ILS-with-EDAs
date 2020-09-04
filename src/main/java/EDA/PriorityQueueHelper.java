package EDA;

import DataStructures.TSPTour;

import java.util.PriorityQueue;

/**
 * This class helps to select the best individuals of a population.
 */
public class PriorityQueueHelper {

    /**
     * This method selects the worst individuals of the population. The worst numberElements individuals are left in the
     * queue, all others are removed. Notice, that for using this method in an appropriate way, the priority queue
     * should have reversed order. This way was chosen to make the implementation more efficient.
     *
     * @param numberElements the number of elements, that should be left in the queue.
     * @param tspTours       the priority queue from which elements are deleted.
     * @return a priority queue containing the worst individuals of the population
     */
    protected static PriorityQueue<TSPTour> select(int numberElements, PriorityQueue<TSPTour> tspTours) {
        while (tspTours.size() > numberElements) {
            tspTours.poll();
        }
        return tspTours;
    }
}
