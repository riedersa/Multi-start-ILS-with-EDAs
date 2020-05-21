package MultiStartILS;

import DataStructures.Graph;
import DataStructures.TSPTour;
import EDA.EDA;
import LocalSearch.LocalSearch;

/**
 * This class controls the Multi-start ILS. It calls an implementation of {@link LocalSearch} and {@link EDA}.
 * <p>
 * Todo: Think about storing the history of a run in a list for analysis
 */
public class MultiStartILS {

    private EDA eda;
    private LocalSearch localSearchAlgorithm;
    private Graph graph;
    private int maxTimesLS = 100;
    private int maxTimesStuck = 5;

    private boolean continueRunning = true;


    public MultiStartILS(EDA eda, LocalSearch localSearchAlgorithm, Graph graph) {
        this.eda = eda;
        this.localSearchAlgorithm = localSearchAlgorithm;
        this.graph = graph;
    }


    /**
     * @return the best tour found
     */
    public TSPTour performMultiStartILS() {
        long minLength = Long.MAX_VALUE;
        TSPTour minTour = null;

        int localSearchCounter = 0;
        while (localSearchCounter < maxTimesLS) {
            if (!continueRunning) {
                return minTour;
            }
            TSPTour tour = eda.initiate();
            tour = localSearchAlgorithm.performSearch(graph, tour);
            localSearchCounter++;
            int stuck = 0;
            while (stuck < maxTimesStuck && localSearchCounter < maxTimesLS) {
                if (!continueRunning) {
                    return minTour;
                }
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


    public void setContinueRunning(boolean continueRunning) {
        this.continueRunning = continueRunning;
    }
}
