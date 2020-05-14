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


    /**
     * Evaluates the tour length of 1,...,n.
     *
     * @return the tour length
     */
    public long evalueteGenericTour() {
        long result = 0L;
        for (int i = 0; i < graph.length - 1; i++) {
            result += graph[i][i + 1];
        }
        result += graph[graph.length - 1][0];
        return result;
    }


    /**
     * This method computes and sets the length of the given tour, if this is not already known.
     *
     * @param tour the tour for which to set the length.
     */
    public void setDistanceToTour(TSPTour tour) {
        if (!tour.isLengthKnown()) {
            tour.setLength(evaluateTour(tour));
        }
    }


    /**
     * Returns the direct distance from one node to another.
     *
     * @param from the start node
     * @param to   the end node
     * @return the distance
     */
    public int getDistance(int from, int to) {
        return graph[from][to];
    }


    /**
     * This function returns the number of nodes of the graph.
     *
     * @return the number of nodes.
     */
    public int getNumberNodes() {
        return graph.length;
    }

}
