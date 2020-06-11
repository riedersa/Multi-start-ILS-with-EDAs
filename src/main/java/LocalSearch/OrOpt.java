package LocalSearch;

import DataStructures.Graph;
import DataStructures.TSPTour;
import Storage.FileParameters;

/**
 * This class implements Or-Opt and uses first improvement.
 */
public class OrOpt implements LocalSearch {

    private static String name = "Or-Opr";


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


    @Override
    public Method getMethod() {
        return Method.DESCENT;
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
        for (int i = 0; i < startTour.getNumberNodes(); i++) {
            for (int j = i; j < startTour.getNumberNodes(); j++) {
                for (int k = 0; k < i; k++) {
                    TSPTour newTour = startTour.orOptSwap(i, j, k);
                    graph.setDistanceToTour(newTour);
                    if (newTour.getLength() < startTour.getLength()) {
                        return newTour;
                    }
                }
                for (int k = j + 1; k < startTour.getNumberNodes(); k++) {
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


    public static String getName() {
        return name;
    }


    public String toString() {
        return "LocalSearchAlgorithm" + FileParameters.separator + getName() + "\n" +
                "LocalSearchMethod" + FileParameters.separator + Method.DESCENT.toString() + "\n";
    }

}
