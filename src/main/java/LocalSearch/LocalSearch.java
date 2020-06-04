package LocalSearch;

import DataStructures.Graph;
import DataStructures.TSPTour;

/**
 * This classes can perform a local search for a given tour.
 */
public interface LocalSearch {

    /**
     * Performs a local search on the given starting point.
     *
     * @param graph     the underlying graph
     * @param startTour the starting point
     * @return the best tour found
     */
    public TSPTour performSearch(final Graph graph, TSPTour startTour);

    /**
     * This function returns the method, the algorithm is using.
     *
     * @return the used Method
     */
    public Method getMethod();

    //This enum describes which method is used by the LS. Or-OPT always uses Descent.
    public enum Method {
        DESCENT,
        STEEPEST_DESCENT
    }

}
