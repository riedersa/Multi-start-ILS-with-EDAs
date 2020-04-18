package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;

/**
 * This class performs an EDA.
 */
public interface EDA {

    /**
     * This method perturbs a given tour.
     *
     * @param graph the graph of the TSP instance
     * @param tour  the tour, that should be perturbed
     * @return the perturbed solution
     */
    public TSPTour perturb(final Graph graph, TSPTour tour);


    /**
     * This method creates an initial solution for the multi-start ILS.
     *
     * @param graph the graph on which the instance is based
     * @return an initial tour
     */
    public TSPTour initiate(final Graph graph);

}
