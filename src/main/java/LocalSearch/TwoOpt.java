package LocalSearch;

import DataStructures.Graph;
import DataStructures.TSPTour;

public class TwoOpt implements LocalSearch {

    private static String name = "2-Opt";

    private Method method = Method.DESCENT;


    public TwoOpt(Method method) {
        this.method = method;
    }


    public TwoOpt() {
    }


    @Override
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
     * <p>
     * Attention: This method can only be used for symmetric TSP. For efficiency reason, some formula for calculating
     * the distance is used, that does only work on symmetric instances.
     *
     * @param graph     the graph on which the problem is based on
     * @param startTour the tour for which to find the next
     * @return the next tour of null if nothing was found
     */
    protected TSPTour findNextTour(Graph graph, TSPTour startTour) {
        graph.setDistanceToTour(startTour);
        long minDistance = startTour.getLength();
        int numberNodes = startTour.getNumberNodes();
        int[] tour = startTour.getTour();
        long startTourLenght = startTour.getLength();
        TSPTour bestTour = null;

        for (int i = 0; i < numberNodes; i++) {
            for (int k = i + 1; k < numberNodes; k++) {
                if (i == 0 && k == numberNodes - 1) {
                    continue;
                }
                long calculatedDistance = graph.getNewLengthAfterSwap(startTourLenght, tour, i, k, numberNodes);
                if (calculatedDistance < minDistance) {
                    TSPTour newTour = startTour.twoOptSwap(i, k);
                    newTour.setLength(calculatedDistance);
                    minDistance = calculatedDistance;
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


    /**
     * this function computes a modulo b and always returns positive values.
     *
     * @param a the nominator
     * @param b the denominator
     * @return a mod b
     */
    private static int posModulo(int a, int b) {
        int result = a % b;
        if (result < 0) {
            result += b;
        }
        return result;
    }
}
