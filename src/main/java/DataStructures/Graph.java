package DataStructures;

/**
 * This class represents a graph.
 */
public class Graph {

    private final int[][] graph;


    /**
     * Initializes a graph object
     *
     * @param graph the underlying graph
     */
    public Graph(int[][] graph) {
        this.graph = graph;
    }


    /**
     * Evaluates the length of the tour for this graph.
     *
     * @param tour the tour
     * @return the length
     */
    public long evaluateTour(final TSPTour tour) {
        return evaluateTour(tour.getTour());
    }


    /**
     * Evaluates the length of the tour for this graph.
     *
     * @param tour the tour
     * @return the length
     */
    public long evaluateTour(final int[] tour) {
        long result = 0L;
        for (int i = 0; i < tour.length - 1; i++) {
            result += graph[tour[i]][tour[i + 1]];
        }
        result += graph[tour[tour.length - 1]][tour[0]];
        return result;
    }


    public long evalueteGenericTour() {
        long result = 0L;
        for (int i = 0; i < graph.length - 1; i++) {
            result += graph[i][i + 1];
        }
        result += graph[graph.length - 1][0];
        return result;
    }


    public void setDistanceToTour(TSPTour tour) {
        if (!tour.isLengthKnown()) {
            tour.setLength(evaluateTour(tour));
        }
    }

}
