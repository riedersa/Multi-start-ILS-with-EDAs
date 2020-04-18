package DataStructures;

/**
 * This class represents a TSP-tour. It also stores its length.
 */
public class TSPTour {

    final int[] tour;
    final double length;

    /**
     * Creates a tour object
     * @param tour The tour
     * @param length the length of the tour
     */
    public TSPTour(int[] tour, double length) {
        this.tour = tour;
        this.length = length;
    }

    public int[] getTour() {
        return tour;
    }

    public double getLength() {
        return length;
    }
}
