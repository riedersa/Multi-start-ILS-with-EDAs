package MultiStartILS;

import DataStructures.Graph;
import DataStructures.TSPTour;
import EDA.*;
import LocalSearch.*;

/**
 * This class controls the Multi-start ILS. It calls an implementation of {@link LocalSearch} and {@link
 * EDA}.
 */
public class MultiStartILS {

    private EDA eda;
    private int maxTimesLS;
    private int maxTimesStuck;
    private LocalSearch localSearchAlgorithm;


    public TSPTour performMultiStartILS(final Graph graph) {
        long minLength = Long.MAX_VALUE;
        TSPTour minTour = null;

        int ls = 0;
        while (ls < maxTimesLS) {
            TSPTour tour = eda.initiate(graph);
            tour = localSearchAlgorithm.performSearch(graph, tour);
            ls++;
            int stuck = 0;
            while (stuck < maxTimesStuck && ls < maxTimesLS) {
                TSPTour optimizedTour = tour;
                optimizedTour = eda.perturb(graph, optimizedTour);
                optimizedTour = localSearchAlgorithm.performSearch(graph, optimizedTour);
                ls++;
                if (optimizedTour.getLength() < tour.getLength()) {
                    tour = optimizedTour;
                    stuck = 0;
                } else {
                    stuck++;
                }
            }

            if(tour.getLength() < minLength){
                minTour = tour;
                minLength = tour.getLength();
            }
        }
        return minTour;
    }

}
