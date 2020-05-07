package EDA;

import DataStructures.TSPTour;

/**
 * This class performs an EDA.
 */
public interface EDA {

    /**
     * This method perturbs a given tour.
     *
     * @param tour the tour, that should be perturbed
     * @return the perturbed solution
     */
    public TSPTour perturb(TSPTour tour);


    /**
     * This method creates an initial solution for the multi-start ILS.
     *
     * @return an initial tour
     */
    public TSPTour initiate();

}
