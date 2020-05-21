package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * This class is an edge based EDA. It can only be used for symmetric TSP. It is based on "TSUTSUI, Shigeyoshi.
 * Probabilistic model-building genetic algorithms in permutation representation domain using edge histogram. In:
 * International Conference on Parallel Problem Solving from Nature. Springer, Berlin, Heidelberg, 2002. S. 224-233".
 * <p>
 * TODO: stopping criterion
 */
public class EdgeBasedEDA implements EDA {

    private static String name = "Edge based EDA";

    private double[][] edgeHistogramMatrix;
    private int selectedPopulationSize;
    private int sampledPopulationSize;
    private final int numberNodes;
    private int maxCounterOtIterations;

    private int valueForAPrioriEdges = 10;
    private double bRatio = 0.15; //Used to create the noise. If b is high, then the perturbation is high.
    private double epsilon; //noise for the matrix according to the paper

    private final Graph graph;


    /**
     * This method creates a new instance of an EDA.
     *
     * @param graph                  the graph on which the TSP instance is based.
     * @param selectedPopulationSize the size, the population should have after selecting the best ones.
     * @param sampledPopulationSize  the size the population should have after sampling the new ones.
     * @param maxCounterOtIterations the maximum number of iterations the algorithm should perform before stopping.
     * @param bRatio                 the bRatio value. If this value is high, the pertubation is high
     * @param valueForAPrioriEdges   the value, edges that are already in a tour, should get
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the sampledPopulationSize is smaller thant
     *                                  the selectedPopulationSize.
     */
    public EdgeBasedEDA(Graph graph, int selectedPopulationSize, int sampledPopulationSize,
                        int maxCounterOtIterations, double bRatio, int valueForAPrioriEdges) throws IllegalArgumentException {
        checkSampledAndSelectedSize(sampledPopulationSize, selectedPopulationSize);
        this.graph = graph;
        this.numberNodes = graph.getNumberNodes();
        this.edgeHistogramMatrix = new double[numberNodes][numberNodes];
        this.selectedPopulationSize = selectedPopulationSize;
        this.sampledPopulationSize = sampledPopulationSize;
        setValueForAPrioriEdges(valueForAPrioriEdges);
        setbRatio(bRatio);
        setMaxCounterOtIterations(maxCounterOtIterations);
        setEpsilon();
    }


    @Override
    public TSPTour perturb(final TSPTour tour) {
        initiateEdgeHistogramMatrix(tour);
        return performEDA();
    }


    @Override
    public TSPTour initiate() {
        initiateEdgeHistogramMatrix();
        return performEDA();
    }


    /**
     * This method fills the edge histogram matrix if nothing is known a priori. Every edge gets the same probability.
     */
    protected void initiateEdgeHistogramMatrix() {
        for (int i = 0; i < numberNodes; i++) {
            for (int j = 0; j < numberNodes; j++) {
                if (i != j) {
                    edgeHistogramMatrix[i][j] = 1;
                }
            }
        }
    }


    /**
     * This method fills the edge histogram matrix if one tour is known a priori. The edges in the tour are more likely
     * than the others. They get the value {@code valueForAPrioriEdges}, the others get 1.
     *
     * @param tspTour the tour given.
     */
    protected void initiateEdgeHistogramMatrix(final TSPTour tspTour) {
        for (int i = 0; i < numberNodes; i++) {
            for (int j = 0; j < numberNodes; j++) {
                if (i != j) {
                    edgeHistogramMatrix[i][j] = 1;
                }
            }
        }
        int[] tour = tspTour.getTour();
        for (int position = 0; position < numberNodes; position++) {
            edgeHistogramMatrix[tour[position]][tour[(position + 1) % numberNodes]] = valueForAPrioriEdges;
            edgeHistogramMatrix[tour[(position + 1) % numberNodes]][tour[position]] = valueForAPrioriEdges;
        }
    }


    /**
     * This method performs an EDA. Notice, that before invoking this method, the edge histogram matrix must be filled.
     *
     * @return the best tour found
     */
    private TSPTour performEDA() {
        PriorityQueue<TSPTour> tspTours = new PriorityQueue<TSPTour>(Collections.reverseOrder());
        sample(sampledPopulationSize, tspTours);
        int numberIterations = 0;
        while (numberIterations < maxCounterOtIterations) {
            tspTours = select(selectedPopulationSize, tspTours);
            estimate(tspTours);
            sample(sampledPopulationSize, tspTours);
            numberIterations++;
        }
        tspTours = select(1, tspTours);
        return tspTours.poll();
    }


    /**
     * This method selects the worst individuals of the population. The worst numberElements individuals are left in the
     * queue, all others are removed. Notice, that for using this method in an appropriate way, the priority queue
     * should have reversed order. This way was chosen to make the implementation more efficient.
     *
     * @param numberElements the number of elements, that should be left in the queue.
     * @param tspTours       the priority queue from which elements are deleted.
     * @return a priority queue containing then worst elements of the initial one
     */
    protected PriorityQueue<TSPTour> select(int numberElements, PriorityQueue<TSPTour> tspTours) {
        while (tspTours.size() > numberElements) {
            tspTours.poll();
        }
        return tspTours;
    }


    /**
     * This method creates the edge histogram matrix. It can only be used for symmetric TSPs.
     *
     * @param tspTours the population for which to create the matrix
     */
    protected void estimate(PriorityQueue<TSPTour> tspTours) {
        setEpsilon();
        //empty the matrix to fill it later again via just addition
        for (int i = 0; i < numberNodes; i++) {
            for (int j = 0; j < numberNodes; j++) {
                if (i != j) {
                    edgeHistogramMatrix[i][j] = epsilon;
                } else {
                    edgeHistogramMatrix[i][j] = 0;
                }
            }
        }
        for (TSPTour tspTour : tspTours) {
            int[] tour = tspTour.getTour();
            for (int position = 0; position < numberNodes; position++) {
                edgeHistogramMatrix[tour[position]][tour[(position + 1) % numberNodes]]++;
                edgeHistogramMatrix[tour[(position + 1) % numberNodes]][tour[position]]++;
            }
        }
    }


    /**
     * Samples new individuals for the population until there are {@code sampledPopulationSize} individuals in the
     * population.
     *
     * @param sampledPopulationSize the size the population should have after sampling
     * @param tspTours              the current population. It is extended by new individuals.
     */
    protected void sample(int sampledPopulationSize, PriorityQueue<TSPTour> tspTours) {
        while (tspTours.size() < sampledPopulationSize) {
            tspTours.add(createTour());
        }
    }


    /**
     * This method creates a new tour according to the edge histogram matrix.
     *
     * @return the newly generated tour.
     */
    protected TSPTour createTour() {
        Random randomGenerator = new Random();
        int[] tour = new int[numberNodes];
        int positionCounter = 0;//The next position to be filled

        tour[0] = randomGenerator.nextInt(numberNodes);
        positionCounter++;

        for (int position = positionCounter; position < numberNodes; position++) {
            double[] vector = rouletteWheelVector(tour, positionCounter);
            double random = randomGenerator.nextDouble();
            for (int node = 0; node < numberNodes; node++) {
                if (vector[node] > 0 && random <= vector[node]) {
                    tour[positionCounter] = node;
                    positionCounter++;
                    break;
                }
                random -= vector[node];
            }
        }

        TSPTour tspTour = new TSPTour(tour);
        graph.setDistanceToTour(tspTour);
        return tspTour;
    }


    /**
     * Constructs the roulette wheel vector for the tour, when the next position to fill is {@code counter}.
     *
     * @param tour    the current tour
     * @param counter the next position to fill.This should always be at least 1.
     * @return the roulette vector
     */
    protected double[] rouletteWheelVector(int[] tour, int counter) {
        double[] vector = edgeHistogramMatrix[tour[counter - 1]].clone();
        for (int i = 0; i < counter; i++) {
            vector[tour[i]] = 0;
        }
        double sum = 0;
        for (int i = 0; i < numberNodes; i++) {
            sum += vector[i];
        }
        for (int i = 0; i < numberNodes; i++) {
            vector[i] = vector[i] / sum;
        }
        return vector;
    }


    /*
     ----------------------------------------------------------------------------------------------------------------------
     Getter and Setter
     */


    public int getSelectedPopulationSize() {
        return selectedPopulationSize;
    }


    /**
     * @param selectedPopulationSize the size the population should have after selection.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the sampledPopulationSize is smaller thant
     *                                  the selectedPopulationSize.
     */
    public void setSelectedPopulationSize(int selectedPopulationSize) {
        checkSampledAndSelectedSize(sampledPopulationSize, selectedPopulationSize);
        this.selectedPopulationSize = selectedPopulationSize;
        setEpsilon();
    }


    public int getSampledPopulationSize() {
        return sampledPopulationSize;
    }


    /**
     * @param sampledPopulationSize the size the population should have after sampling.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the sampledPopulationSize is smaller thant
     *                                  * * the selectedPopulationSize.
     */
    public void setSampledPopulationSize(int sampledPopulationSize) throws IllegalArgumentException {
        checkSampledAndSelectedSize(sampledPopulationSize, selectedPopulationSize);
        this.sampledPopulationSize = sampledPopulationSize;
    }


    public int getMaxCounterOtIterations() {
        return maxCounterOtIterations;
    }


    public void setMaxCounterOtIterations(int maxCounterOtIterations) {
        if (maxCounterOtIterations <= 0) {
            throw new IllegalArgumentException(String.format("There must be at least 1 iteration. " +
                    "MaxCounterOfIterations was :", maxCounterOtIterations));
        }
        this.maxCounterOtIterations = maxCounterOtIterations;
    }


    public double getValueForAPrioriEdges() {
        return valueForAPrioriEdges;
    }


    /**
     * Sets the {@code probForPriorTour}
     *
     * @param valueForAPrioriEdges the probability the positions in a given tour should get a priori in the models.
     */
    public void setValueForAPrioriEdges(int valueForAPrioriEdges) {
        if (valueForAPrioriEdges <= 0) {
            throw new IllegalArgumentException(String.format("The value for the edges in the given tours should be " +
                    "bigger than 0. It was: %d", valueForAPrioriEdges));
        }
        this.valueForAPrioriEdges = valueForAPrioriEdges;
    }


    public double getbRatio() {
        return bRatio;
    }


    public void setbRatio(double bRatio) {
        this.bRatio = bRatio;
        setEpsilon();
    }


    private void setEpsilon() {
        this.epsilon = (2.0 * selectedPopulationSize) / (numberNodes - 1) * bRatio;
    }


    private void checkSampledAndSelectedSize(int sampledPopulationSize, int selectedPopulationSize) {
        if (sampledPopulationSize < selectedPopulationSize || sampledPopulationSize <= 0 || selectedPopulationSize < 0) {
            throw new IllegalArgumentException(String.format("The size of the population after sampling should be " +
                    "bigger than the size after selecting the best individuals. The sizes were %d for sampling and %d" +
                    " for selecting.", sampledPopulationSize, selectedPopulationSize));
        }
    }


    public static String getName() {
        return name;
    }


    //For testing
    protected void setEdgeHistogramMatrix(double[][] edgeHistogramMatrix) {
        this.edgeHistogramMatrix = edgeHistogramMatrix;
    }


    protected double[][] getEdgeHistogramMatrix() {
        return edgeHistogramMatrix;
    }


    protected double getEpsilon() {
        return epsilon;
    }
}
