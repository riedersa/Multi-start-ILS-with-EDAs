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
        int numberNodes = startTour.getNumberNodes();
        graph.setDistanceToTour(startTour);
        for (int start = 0; start < numberNodes; start++) {
            for (int length = 1; length <= 3; length++) {
                int end = (start + (length - 1)) % numberNodes;
                if (end < start) {
                    for (int position = end + 1; position < start - 1; position++) {
                        TSPTour newTour = startTour.orOptSwap(start, length, position);
                        graph.setDistanceToTour(newTour);
                        if (newTour.getLength() < startTour.getLength()) {
                            return newTour;
                        }
                    }
                } else {
                    for (int position = 0; position < start-1; position++) {
                        TSPTour newTour = startTour.orOptSwap(start, length, position);
                        graph.setDistanceToTour(newTour);
                        if (newTour.getLength() < startTour.getLength()) {
                            return newTour;
                        }
                    }
                    for (int position = end + 1; position < startTour.getNumberNodes(); position++) {
                        TSPTour newTour = startTour.orOptSwap(start, length, position);
                        graph.setDistanceToTour(newTour);
                        if (newTour.getLength() < startTour.getLength()) {
                            return newTour;
                        }
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
