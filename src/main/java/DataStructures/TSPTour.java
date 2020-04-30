package DataStructures;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class represents a TSP-tour. It also stores its length.
 */
public class TSPTour {

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

    //Insert [start, ..., end] after insertionPoint.
    public TSPTour orOptSwap(int start, int end, int insertionPoint) {
        if (start < 0 || end < start || end >= tour.length) {
            throw new IllegalArgumentException(String.format("The values of start or end don't match the " +
                    "specification: start " +
                    "is %d and end is %d", start, end));
        }
        if ((insertionPoint >= start && end >= insertionPoint) || insertionPoint < 0 || insertionPoint >= tour.length) {
            throw new IllegalArgumentException(String.format("The values of start, end or insertionPoint don't match " +
                    "the " +
                    "specification: start " +
                    "is %d, end is %d and insertionPoint is %d", start, end));
        }
        ArrayList<Integer> begin = new ArrayList();
        if (start < insertionPoint) {
            for (int i = 0; i < start; i++) {
                begin.add(tour[i]);
            }
            for (int i = end + 1; i <= insertionPoint; i++) {
                begin.add(tour[i]);
            }
        } else {
            for (int i = 0; i < insertionPoint; i++) {
                begin.add(tour[i]);
            }
        }

        ArrayList<Integer> insertion = new ArrayList();
        for(int i = start; i<end; i++){
            insertion.add(tour[i]);
        }

        ArrayList<Integer> endList = new ArrayList();
        if(end < insertionPoint){
            for(int i = insertionPoint +1; i < tour.length; i++){
                endList.add(tour[i]);
            }
        } else {
            for (int i = insertionPoint + 1; i < start; i++) {
                endList.add(tour[i]);
            }
            for (int i = end + 1; i < tour.length; i++) {
                endList.add(tour[i]);
            }
        }
        begin.addAll(insertion);
        begin.addAll(endList);

        Object[] tourO = begin.toArray();

        int[] tour = new int[this.tour.length];
        for(int i = 0; i< tour.length; i++){
            tour[i] = (int) tourO[i];
        }
        TSPTour newTSPTour = new TSPTour(tour);
        return newTSPTour;
    }


    @Override
    public String toString() {
        return Arrays.toString(tour) + " of length " + length + "\n";
    }
}
