package DataStructures;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents a TSP-tour. It also stores its length.
 */
public class TSPTour implements Comparable<TSPTour> {

    private final int[] tour;
    private long length;
    private boolean lengthKnown;


    /**
     * Creates a tour object
     *
     * @param tour   The tour
     * @param length the length of the tour
     */
    public TSPTour(int[] tour, long length) {
        this.tour = tour;
        this.length = length;
        lengthKnown = true;
    }


    /**
     * Creates a tour object. If the length is not known, it is set to the maximum value of long.
     *
     * @param tour The tour
     */
    public TSPTour(int[] tour) {
        this.tour = tour;
        this.length = Long.MAX_VALUE;
        lengthKnown = false;
    }


    public int[] getTour() {
        return tour;
    }


    public long getLength() {
        if (!lengthKnown) {
            throw new UnsupportedOperationException("The length is not known");
        }
        return length;
    }


    public boolean isLengthKnown() {
        return lengthKnown;
    }


    public void setLength(long length) {
        this.length = length;
        lengthKnown = true;
    }


    public int getNumberNodes() {
        return tour.length;
    }


    /**
     * Reverses the cities between index start and end. All cities until position start-1 are added as they are, then
     * the cities in between start and end, including start and end, are reversed and the cities until the end are added
     * as they are.
     * <p>
     * It is assumed, that start is at least 0, start is smaller than end and end is smaller than the maximum number of
     * cities. Numbering starts by zero.
     *
     * @param start the starting index
     * @param end   the ending index, until which cities are reversed
     * @return the new Tour. It has maximum length, since the tour does not know anything of the graph;
     * @throws IllegalArgumentException if start is too small, end is too large or end is smaller than start
     */
    public TSPTour twoOptSwap(int start, int end) throws IllegalArgumentException {
        if (start < 0 || end < start || end >= tour.length) {
            throw new IllegalArgumentException(String.format("The values of start or end don't match the " +
                    "specification: start " +
                    "is %d and end is %d", start, end));
        }

        int[] newTour = new int[tour.length];
        for (int i = 0; i < start; i++) {
            newTour[i] = tour[i];
        }
        for (int i = start; i <= end; i++) {
            newTour[i] = tour[end - i + start];
        }
        for (int i = end + 1; i < tour.length; i++) {
            newTour[i] = tour[i];
        }
        TSPTour newTSPTour = new TSPTour(newTour);
        return newTSPTour;
    }


    /**
     * This method cuts [start, ..., end] out of the list and inserts it after the position insertion Point.
     * <p>
     * It is assumed, that start is smaller than end, insertionPoint is smaller than start of bigger than end and all
     * arguments lie between 0 and the length of the tour minus one.
     *
     * @param start          the start of the cut sequence (index)
     * @param length         the length of the cut sequence. It should be 1, 2, or 3
     * @param insertionPoint the index, after which the sequence should be inserted
     * @return the new TSP tour
     */
    public TSPTour orOptSwap(int start, int length, int insertionPoint) {
        if (start < 0 || length <= 0 || length > 3) {
            throw new IllegalArgumentException(String.format("The values of start or length don't match the " +
                    "specification: start " +
                    "is %d and length is %d", start, length));
        }
        int end = (start + (length - 1)) % tour.length;
        if ((insertionPoint >= start && end > insertionPoint) || insertionPoint < 0 || insertionPoint >= tour.length ||
                (insertionPoint >= start && insertionPoint > end && end < start) || (end < start && insertionPoint < end)) {
            throw new IllegalArgumentException(String.format("The values of start, end or insertionPoint don't match " +
                    "the " +
                    "specification: start " +
                    "is %d, end is %d and insertionPoint is %d", start, end, insertionPoint));
        }
        ArrayList<Integer> newTour = new ArrayList();
        if (start < insertionPoint && start <= end) {
            addTotour(newTour, 0, start-1);
            addTotour(newTour, end + 1, insertionPoint);
            addTotour(newTour, start, end);
            addTotour(newTour, insertionPoint + 1, tour.length-1);
        } else if (start == insertionPoint) { //then also end == insertion point, otherwise an exception would arise
            addTotour(newTour, 0, tour.length-1);
        } else if (insertionPoint < start && start <= end) {
            addTotour(newTour, 0, insertionPoint);
            addTotour(newTour, start, end);
            addTotour(newTour, insertionPoint + 1, start - 1);
            addTotour(newTour, end + 1, tour.length-1);
        } else if (end <= insertionPoint && insertionPoint < start) {
            addTotour(newTour, end + 1, insertionPoint);
            addTotour(newTour, start, tour.length - 1);
            addTotour(newTour, 0, end);
            addTotour(newTour, insertionPoint + 1, start - 1);
        }

        Object[] tourO = newTour.toArray();

        int[] tour = new int[this.tour.length];
        for (int i = 0; i < tour.length; i++) {
            tour[i] = (int) tourO[i];
        }
        TSPTour newTSPTour = new TSPTour(tour);
        return newTSPTour;
    }


    /**
     * This method adds every node between start and end (including these two) to the arrayList.
     *
     * @param arrayList the arraylist to add to
     * @param start     the starting point
     * @param end       the end point
     */
    private void addTotour(ArrayList<Integer> arrayList, int start, int end) {
        for (int i = start; i <= end; i++) {
            arrayList.add(tour[i]);
        }
    }


    @Override
    public String toString() {
        return Arrays.toString(tour) + " of length " + length + "\n";
    }


    @Override
    public int compareTo(TSPTour o) {
        if (this.getLength() > o.getLength()) {
            return 1;
        } else if (this.getLength() < o.getLength()) {
            return -1;
        } else {
            return 0;
        }
    }
}
