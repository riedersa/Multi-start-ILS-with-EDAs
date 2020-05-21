package LocalSearch;

import DataStructures.Graph;
import DataStructures.TSPTour;

public class TwoOpt implements LocalSearch {

    private static String name = "2-Opt";

    private Method method = Method.DESCENT;


    public enum Method {
        DESCENT,
        STEEPEST_DESCENT
    }


    public TwoOpt(Method method) {
        this.method = method;
    }


    public TwoOpt() {
    }


    public Method getMethod() {
        return method;
    }


    public void setMethod(Method method) {
        this.method = method;
    }


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
     * Finds the next tour to continue with.
     *
     * @param graph     the graph on which the problem is based on
     * @param startTour the tour for which to find the next
     * @return the next tour
     */
    protected TSPTour findNextTour(Graph graph, TSPTour startTour) {
        graph.setDistanceToTour(startTour);
        long minDistance = startTour.getLength();
        TSPTour bestTour = null;

        for (int i = 0; i < startTour.getNumberNodes(); i++) {
            for (int k = i + 1; k < startTour.getNumberNodes(); k++) {
                TSPTour newTour = startTour.twoOptSwap(i, k);
                graph.setDistanceToTour(newTour);
                long newDistance = newTour.getLength();
                if (newDistance < minDistance) {
                    minDistance = newDistance;
                    if (method.equals(Method.DESCENT)) {
                        return newTour;
                    } else {//Method equals Steepest_Descent
                        bestTour = newTour;
                    }
                }
            }
        }
        return bestTour;
    }


    public static String getName() {
        return name;
    }
}
