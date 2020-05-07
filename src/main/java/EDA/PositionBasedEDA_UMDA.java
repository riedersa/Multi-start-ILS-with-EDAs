package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;

import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * This class performs UMDA. The algorithm stops, after a specified number of iterations. The implementation is based on
 * "XU, Zhe, et al. Immune algorithm combined with estimation of distribution for traveling salesman problem. IEEJ
 * Transactions on Electrical and Electronic Engineering, 2016, 11. Jg., S. S142-S154.".
 * <p>
 * TODO: add another stopping criterion like no improvement
 * <p>
 * TODO: Tests
 */
public class PositionBasedEDA_UMDA implements EDA {

    /* This is the probabilistic model used by the EDA. model[i][j] is the probability that node i is at position j
    of the tour*/
    private double[][] model;
    private int selectedPopulationSize;
    private int sampledPopulationSize;
    private final int numberNodes;
    private int maxCounterOtIterations;

    private double probForPriorTour = 0.2;

    private final Graph graph;


    /**
     * This method creates a new instance of an UMDA.
     *
     * @param graph                  the graph on which the TSP instance is based.
     * @param numberNodes            the number of nodes the current TSP instance has.
     * @param selectedPopulationSize the size, the population should have after selecting the best ones.
     * @param sampledPopulationSize  the size the population should have after sampling the new ones.
     * @param maxCounterOtIterations the maximum number of iterations the algorithm should perform before stopping.
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the sampledPopulationSize is smaller thant
     *                                  the selectedPopulationSize.
     */
    public PositionBasedEDA_UMDA(Graph graph, int numberNodes, int selectedPopulationSize, int sampledPopulationSize,
                                 int maxCounterOtIterations) throws IllegalArgumentException {
        if (sampledPopulationSize < selectedPopulationSize) {
            throw new IllegalArgumentException(String.format("The size of the population after sampling should be " +
                    "bigger than the size after selecting the best individuals. The sizes were %d for sampling and %d" +
                    " for selecting.", sampledPopulationSize, selectedPopulationSize));
        }
        this.graph = graph;
        this.numberNodes = numberNodes;
        this.model = new double[numberNodes][numberNodes];
        this.selectedPopulationSize = selectedPopulationSize;
        this.sampledPopulationSize = sampledPopulationSize;
        this.maxCounterOtIterations = maxCounterOtIterations;
    }


    @Override
    public TSPTour perturb(TSPTour tour) {
        initiateModel(tour);
        return performEDA();
    }


    @Override
    public TSPTour initiate() {
        initiateModel();
        return performEDA();
    }


    /**
     * Initiates the model if there is no prior knowledge. Then each node is equally likely at each position.
     */
    private void initiateModel() {
        double prob = 1.0 / numberNodes;
        for (int node = 0; node < numberNodes; node++) {
            for (int position = 0; position < numberNodes; node++) {
                model[node][position] = prob;
            }
        }
    }


    /**
     * Initiates the model if there is one tour given. Every node gets {@code probForPriorTour} probability for the
     * position it is assigned in the given tour.
     *
     * @param tspTour the given tour
     */
    private void initiateModel(TSPTour tspTour) {
        double prob = (1.0 - probForPriorTour) / (numberNodes - 1.0);
        for (int node = 0; node < numberNodes; node++) {
            for (int position = 0; position < numberNodes; node++) {
                model[node][position] = prob;
            }
        }
        int[] tour = tspTour.getTour();
        for (int position = 0; position < numberNodes; position++) {
            model[tour[position]][position] = probForPriorTour;
        }
    }


    /**
     * This method performs the EDA and returns the best value found. It uses the model to sample the first population,
     * so make sure, that the model was updated before calling this method. The constructor does not do this!
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
     * This methods updates the probability model.
     *
     * @param tspTours the current population that shoulc be used to update the model.
     */
    private void estimate(PriorityQueue<TSPTour> tspTours) {
        int populationSize = tspTours.size();

        int[][] numberOccurrencesAtPosition = new int[numberNodes][numberNodes];

        for (TSPTour tspTour : tspTours) {
            int[] tour = tspTour.getTour();
            for (int position = 0; position < numberNodes; position++) {
                numberOccurrencesAtPosition[tour[position]][position]++;
            }
        }

        double noiseNumerator = 1.0 / numberNodes;
        double denominator = populationSize + (((double) populationSize) / numberNodes);

        for (int node = 0; node < numberNodes; node++) {
            for (int position = 0; position < numberNodes; position++) {
                model[node][position] = (numberOccurrencesAtPosition[node][position] + noiseNumerator) / denominator;
            }
        }
    }


    /**
     * This method creates new solutions according to the probability model and adds it to the tspTours.
     *
     * @param sizeOfPopulationAfterSampling the size, the population should have after sampling
     * @param tspTours                      the current population
     */
    private void sample(int sizeOfPopulationAfterSampling, PriorityQueue<TSPTour> tspTours) {
        while (tspTours.size() < sizeOfPopulationAfterSampling) {
            int[] tourArray = rouletteWheelForCreation();
            tourArray = refineTour(tourArray);
            TSPTour tour = new TSPTour(tourArray);
            graph.setDistanceToTour(tour);
            tspTours.add(tour);
        }
    }


    /**
     * Returns a tour, that was sampled using the roulette wheel technique and the model. Notice, that this tour must
     * not be feasible.
     *
     * @return the generated Tour. This must not be feasible.
     */
    private int[] rouletteWheelForCreation() {
        int[] tour = new int[numberNodes];
        Random randomGenerator = new Random();
        for (int position = 0; position < numberNodes; position++) {
            double randomNumber = randomGenerator.nextDouble();
            for (int node = 0; node < numberNodes; node++) {
                if (randomNumber <= model[node][position]) {
                    tour[position] = node;
                    break;
                } else {
                    randomNumber -= model[node][position];
                }
            }
        }
        return tour;
    }


    /**
     * Takes an possibly infeasible tour and makes it feasible.
     *
     * @param tour the tour to change
     * @return the feasible tour based on the given one
     */
    private int[] refineTour(int[] tour) {
        //Is true, if node i is already found in the tour.
        boolean[] alreadyFound = new boolean[numberNodes];

        HashSet<Integer> missingNodes = new HashSet<>();

        HashSet<Integer> doubleLocations = new HashSet<>();

        Random randomGenerator = new Random();

        for (int i = 0; i < numberNodes; i++) {
            if (alreadyFound[tour[i]]) {
                doubleLocations.add(i);
            }
        }

        for (int i = 0; i < numberNodes; i++) {
            if (!alreadyFound[i]) {
                missingNodes.add(i);
            }
        }


        for (Integer position : doubleLocations) {
            //this is the distance between pos-1 and the node plus pos+1 and the node.
            long[] distanceAddedIfNode = new long[numberNodes];
            //the distances summed up
            double overallDistance = 0;

            for (Integer node : missingNodes) {
                distanceAddedIfNode[node] = graph.getDistance(tour[(position - 1) % numberNodes], node)
                        + graph.getDistance(node, tour[(position + 1) % numberNodes]);
                overallDistance += distanceAddedIfNode[node];
            }

            double random = randomGenerator.nextDouble();

            for (int node : missingNodes) {
                if (random <= ((double) distanceAddedIfNode[node]) / overallDistance) {
                    missingNodes.remove(node);
                    tour[position] = node;
                }
            }
        }

        return tour;
    }


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
}