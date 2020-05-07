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
 * TODO: Tests
 * <p>
 * TODO: stopping criterion
 */
public class EdgeBasedEDA implements EDA {

    private double[][] edgeHistogramMatrix;
    private int selectedPopulationSize;
    private int sampledPopulationSize;
    private final int numberNodes;
    private int maxCounterOtIterations;

    private double probForPriorTour = 0.2;
    private double bRatio = 0.15; //Used to create the noise. If b is high, then the perturbation is high.
    private double epsilon; //noise for the matrix according to the paper

    private final Graph graph;


    /**
     * This method creates a new instance of an EDA.
     *
     * @param graph                  the graph on which the TSP instance is based.
     * @param numberNodes            the number of nodes the current TSP instance has.
     * @param selectedPopulationSize the size, the population should have after selecting the best ones.
     * @param sampledPopulationSize  the size the population should have after sampling the new ones.
     * @param maxCounterOtIterations the maximum number of iterations the algorithm should perform before stopping.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the sampledPopulationSize is smaller thant
     *                                  the selectedPopulationSize.
     */
    public EdgeBasedEDA(Graph graph, int numberNodes, int selectedPopulationSize, int sampledPopulationSize,
                        int maxCounterOtIterations) throws IllegalArgumentException {
        if (sampledPopulationSize < selectedPopulationSize) {
            throw new IllegalArgumentException(String.format("The size of the population after sampling should be " +
                    "bigger than the size after selecting the best individuals. The sizes were %d for sampling and %d" +
                    " for selecting.", sampledPopulationSize, selectedPopulationSize));
        }
        this.graph = graph;
        this.numberNodes = numberNodes;
        this.edgeHistogramMatrix = new double[numberNodes][numberNodes];
        this.selectedPopulationSize = selectedPopulationSize;
        this.sampledPopulationSize = sampledPopulationSize;
        this.maxCounterOtIterations = maxCounterOtIterations;
        setEpsilon();
    }


    @Override
    public TSPTour perturb(TSPTour tour) {
        return null;
    }


    @Override
    public TSPTour initiate() {
        return null;
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
     */
    private PriorityQueue<TSPTour> select(int numberElements, PriorityQueue<TSPTour> tspTours) {
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
    private void estimate(PriorityQueue<TSPTour> tspTours) {
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
     * @param tspTours the current population. It is extended by new individuals.
     */
    private void sample(int sampledPopulationSize, PriorityQueue<TSPTour> tspTours) {
        while (tspTours.size() < sampledPopulationSize) {
            tspTours.add(createTour());
        }
    }


    /**
     * This method creates a new tour according to the edge histogram matrix.
     *
     * @return the newly generated tour.
     */
    private TSPTour createTour() {
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
     * @param counter the next position to fill
     * @return
     */
    private double[] rouletteWheelVector(int[] tour, int counter) {
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
        if (sampledPopulationSize < selectedPopulationSize) {
            throw new IllegalArgumentException(String.format("The size of the population after sampling should be " +
                    "bigger than the size after selecting the best individuals. The sizes were %d for sampling and %d" +
                    " for selecting.", sampledPopulationSize, selectedPopulationSize));
        }
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
        if (sampledPopulationSize < selectedPopulationSize) {
            throw new IllegalArgumentException(String.format("The size of the population after sampling should be " +
                    "bigger than the size after selecting the best individuals. The sizes were %d for sampling and %d" +
                    " for selecting.", sampledPopulationSize, selectedPopulationSize));
        }
        this.sampledPopulationSize = sampledPopulationSize;
    }


    public int getMaxCounterOtIterations() {
        return maxCounterOtIterations;
    }


    public void setMaxCounterOtIterations(int maxCounterOtIterations) {
        this.maxCounterOtIterations = maxCounterOtIterations;
    }


    public double getProbForPriorTour() {
        return probForPriorTour;
    }


    /**
     * Sets the {@code probForPriorTour}
     *
     * @param probForPriorTour the probability the positions in a given tour should get a priori in the models.
     * @throws IllegalArgumentException If the probability is larger than 1.
     */
    public void setProbForPriorTour(double probForPriorTour) throws IllegalArgumentException {
        if (probForPriorTour > 1.0) {
            throw new IllegalArgumentException(String.format("The probability is too large: %e", probForPriorTour));
        }
        this.probForPriorTour = probForPriorTour;
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
}
