package MultiStartILS;

import DataStructures.CalculationInstance;
import DataStructures.Graph;
import DataStructures.TSPTour;
import EDA.EDA;
import LocalSearch.LocalSearch;

/**
 * This class controls the Multi-start ILS. It calls an implementation of {@link LocalSearch} and {@link EDA}.
 * <p>
 * Todo: Think about storing the history of a run in a list for analysis
 * <p>
 * TODO: maybe use CalculationInstance as return value instead of TSPTour. This way, more information, like intermediate
 * tours, could be returned.
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
    public CalculationInstance performMultiStartILS() {
        long minLength = Long.MAX_VALUE;
        CalculationInstance calculationInstance = new CalculationInstance(eda.getClass().getName(),
                localSearchAlgorithm.getClass().getName(), localSearchAlgorithm.getMethod().toString());
        TSPTour minTour = null;

        int localSearchCounter = 0;
        while (localSearchCounter < maxTimesLS) {
            if (!continueRunning) {
                calculationInstance.setMinium(minTour);
                return calculationInstance;
            }
            TSPTour tour = eda.initiate();
            calculationInstance.addStep(tour, CalculationInstance.CalculationKind.INIT);
            tour = localSearchAlgorithm.performSearch(graph, tour);
            calculationInstance.addStep(tour, CalculationInstance.CalculationKind.LS);
            localSearchCounter++;
            int stuck = 0;

            //This is for updating the minimum. If this would not be there, aborting might lead to having no result.
            if (tour.getLength() < minLength) {
                minTour = tour;
                minLength = tour.getLength();
            }
            while (stuck < maxTimesStuck && localSearchCounter < maxTimesLS) {
                if (!continueRunning) {
                    calculationInstance.setMinium(minTour);
                    return calculationInstance;
                }
                TSPTour optimizedTour = tour;
                optimizedTour = eda.perturb(optimizedTour);
                calculationInstance.addStep(tour, CalculationInstance.CalculationKind.EDA);
                optimizedTour = localSearchAlgorithm.performSearch(graph, optimizedTour);
                calculationInstance.addStep(tour, CalculationInstance.CalculationKind.LS);
                localSearchCounter++;
                if (optimizedTour.getLength() < tour.getLength()) {
                    tour = optimizedTour;
                    stuck = 0;

                    //the following statements update the minimum if something better than before is found
                    if (tour.getLength() < minLength) {
                        minTour = tour;
                        minLength = tour.getLength();
                    }
                } else {
                    stuck++;
                }
            }

            if (tour.getLength() < minLength) {
                minTour = tour;
                minLength = tour.getLength();
            }
        }
        calculationInstance.setMinium(minTour);
        return calculationInstance;
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
