package LocalSearch;

import DataStructures.Graph;
import DataStructures.TSPTour;

/**
 * This class implements Or-Opt and uses first improvement.
 */
public class OrOpt implements LocalSearch {

    @Override
    public TSPTour performSearch(Graph graph, TSPTour startTour) {
        graph.setDistanceToTour(startTour);
        TSPTour bestTour = startTour;

        while (true) {
            TSPTour nextTour = findNextTour(graph, bestTour);
            if (nextTour == null) {
                return bestTour;
            } else {
                bestTour = nextTour;
            }
        }
    }


    /**
     * Finds the next tour to continue with using first improvement.
     *
     * @param graph     the graph on which the problem is based on
     * @param startTour the tour for which to find the next
     * @return the next tour
     */
    private TSPTour findNextTour(Graph graph, TSPTour startTour) {
        graph.setDistanceToTour(startTour);
        for (int i = 0; i < startTour.getLength(); i++) {
            for (int j = i; j < startTour.getLength(); j++) {
                for (int k = 0; k < i; k++) {
                    TSPTour newTour = startTour.orOptSwap(i, j, k);
                    graph.setDistanceToTour(newTour);
                    if (newTour.getLength() < startTour.getLength()) {
                        return newTour;
                    }
                }
            }
        }
        return null;
    }
}
