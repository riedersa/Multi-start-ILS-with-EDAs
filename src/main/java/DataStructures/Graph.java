package DataStructures;

/**
 * This class represents a graph.
 */
public class Graph {

    private final double[][] graph;


    /**
     * Initializes a graph object
     * @param graph the underlying graph
     */
    public Graph(double[][] graph) {
        this.graph = graph;
    }

    /**
     * Evaluates the length of the tour for this graph.
     *
     * @param tour the tour
     * @return the length
     */
    public double evaluateTour(final TSPTour tour){
        throw new UnsupportedOperationException("Not implemented, yet");
    }

}
