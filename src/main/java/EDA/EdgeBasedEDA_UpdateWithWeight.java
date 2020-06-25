package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;
import Storage.FileParameters;

import java.util.PriorityQueue;

public class EdgeBasedEDA_UpdateWithWeight extends EdgeBasedEDA {


    private static String name = "Edge based EDA (with Weights)";

    private double alpha = 0.9;


    /**
     * This method creates a new instance of an edge based EDA with weights for updating.
     *
     * @param graph                  the graph on which the TSP instance is based.
     * @param selectedPopulationSize the size, the population should have after selecting the best ones.
     * @param sampledPopulationSize  the size the population should have after sampling the new ones.
     * @param maxCounterOtIterations the maximum number of iterations the algorithm should perform before stopping.
     * @param bRatio                 the bRatio value. If this value is high, the pertubation is high
     * @param valueForAPrioriEdges   the value, edges that are already in a tour, should get
     * @param alpha                  this is the weight, the tours in the population should get when updating the model.
     *                               The current model gets (1-alpha) as wight.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the sampledPopulationSize is smaller than
     *                                  the selectedPopulationSize.
     */
    public EdgeBasedEDA_UpdateWithWeight(Graph graph, int selectedPopulationSize, int sampledPopulationSize,
                                         int maxCounterOtIterations, double bRatio, int valueForAPrioriEdges,
                                         double alpha) throws IllegalArgumentException {
        super(graph, selectedPopulationSize, sampledPopulationSize, maxCounterOtIterations, bRatio,
                valueForAPrioriEdges);
        setAlpha(alpha);
    }


    /**
     * This method creates the edge histogram matrix. It can only be used for symmetric TSPs.
     *
     * @param tspTours the population for which to create the matrix
     */
    @Override
    protected double[][] estimate(PriorityQueue<TSPTour> tspTours, double[][] edgeHistogramMatrix, int numberNodes) {
        setEpsilon();
        double epsilon = getEpsilon();
        double newEdgeHistogramMatrix[][] = new double[numberNodes][numberNodes];
        for (TSPTour tspTour : tspTours) {
            int[] tour = tspTour.getTour();
            for (int position = 0; position < numberNodes; position++) {
                newEdgeHistogramMatrix[tour[position]][tour[(position + 1) % numberNodes]]++;
                newEdgeHistogramMatrix[tour[(position + 1) % numberNodes]][tour[position]]++;
            }
        }

        for (int i = 0; i < numberNodes; i++) {
            for (int j = 0; j < numberNodes; j++) {
                if (i != j) {
                    edgeHistogramMatrix[i][j] = alpha * (epsilon + newEdgeHistogramMatrix[i][j]) +
                            (1 - alpha) * edgeHistogramMatrix[i][j];
                } else {
                    newEdgeHistogramMatrix[i][j] = 0;
                }
            }
        }

        return edgeHistogramMatrix;
    }


    public static String getName() {
        return name;
    }


    @Override
    public String toString() {
        return super.toString() +
                "Alpha" + FileParameters.separator + alpha + "\n";
    }


    /**
     * This method sets the weighting, the current population should have for updating the model.
     *
     * @param alpha the weighting.
     * @throws IllegalArgumentException if the value of alpha is smaller than 0 or greater than 1
     */
    public void setAlpha(double alpha) {
        if (alpha > 1.0 || alpha < 0) {
            throw new IllegalArgumentException("The value for alpha must be between 0 and 1, including these two " +
                    "values, but it was: " + alpha);
        }
        this.alpha = alpha;
    }


    public double getAlpha() {
        return alpha;
    }

}
