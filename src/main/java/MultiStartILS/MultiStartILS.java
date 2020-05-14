package MultiStartILS;

import DataStructures.Graph;
import DataStructures.TSPTour;
import EDA.EDA;
import LocalSearch.LocalSearch;

/**
 * This class controls the Multi-start ILS. It calls an implementation of {@link LocalSearch} and {@link EDA}.
 */
public class MultiStartILS {

    private EDA eda;
    private int maxTimesLS = 100;
    private int maxTimesStuck = 5;
    private LocalSearch localSearchAlgorithm;


    public MultiStartILS(EDA eda, LocalSearch localSearchAlgorithm) {
        this.eda = eda;
        this.localSearchAlgorithm = localSearchAlgorithm;
    }


    /**
     * @param graph the graph for which to perform the MultiStartILS
     * @return the best tour found
     */
    public TSPTour performMultiStartILS(final Graph graph) {
        long minLength = Long.MAX_VALUE;
        TSPTour minTour = null;

        int localSearchCounter = 0;
        while (localSearchCounter < maxTimesLS) {
            TSPTour tour = eda.initiate();
            tour = localSearchAlgorithm.performSearch(graph, tour);
            localSearchCounter++;
            int stuck = 0;
            while (stuck < maxTimesStuck && localSearchCounter < maxTimesLS) {
                TSPTour optimizedTour = tour;
                optimizedTour = eda.perturb(optimizedTour);
                optimizedTour = localSearchAlgorithm.performSearch(graph, optimizedTour);
                localSearchCounter++;
                if (optimizedTour.getLength() < tour.getLength()) {
                    tour = optimizedTour;
                    stuck = 0;
                } else {
                    stuck++;
                }
            }

            if (tour.getLength() < minLength) {
                minTour = tour;
                minLength = tour.getLength();
            }
        }
        return minTour;
    }


    public int getMaxTimesLS() {
        return maxTimesLS;
    }


    public void setMaxTimesLS(int maxTimesLS) {
        this.maxTimesLS = maxTimesLS;
    }


    public int getMaxTimesStuck() {
        return maxTimesStuck;
    }


    public void setMaxTimesStuck(int maxTimesStuck) {
        this.maxTimesStuck = maxTimesStuck;
    }
}
