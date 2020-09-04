package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;
import Storage.FileParameters;

import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * This ist he abstract implementation for the algorithms based on "XU, Zhe, et al. Immune algorithm combined with
 * estimation of distribution for traveling salesman problem. IEEJ Transactions on Electrical and Electronic
 * Engineering, 2016, 11. Jg., S. S142-S154.". The only method, the subclasses implement is the method estimate for
 * updating the model.
 */
public abstract class PositionBasedEDA implements EDA {
    /* This is the probabilistic model used by the EDA. model[i][j] is the probability that node i is at position j
        of the tour*/
    private double[][] model;
    private int selectedPopulationSize;
    private int sampledPopulationSize;
    private final int numberNodes;
    private int maxCounterOtIterations;

    private double probForPriorTour = 0.2;

    private final Graph graph;
    private boolean firstUpdate = true;


    /**
     * This method creates a new instance of an UMDA.
     *
     * @param graph                  the graph on which the TSP instance is based.
     * @param selectedPopulationSize the size, the population should have after selecting the best ones.
     * @param sampledPopulationSize  the size the population should have after sampling the new ones.
     * @param maxCounterOtIterations the maximum number of iterations the algorithm should perform before stopping.
     * @param probForPriorTour       the probability for a node to occure at the position it was in the given tour
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the sampledPopulationSize is smaller thant
     *                                  the selectedPopulationSize.
     */
    public PositionBasedEDA(Graph graph, int selectedPopulationSize, int sampledPopulationSize,
                            int maxCounterOtIterations, double probForPriorTour) throws IllegalArgumentException {
        checkSampledAndSelectedSize(sampledPopulationSize, selectedPopulationSize);
        this.graph = graph;
        this.numberNodes = graph.getNumberNodes();
        this.model = new double[numberNodes][numberNodes];
        this.selectedPopulationSize = selectedPopulationSize;
        this.sampledPopulationSize = sampledPopulationSize;
        setProbForPriorTour(probForPriorTour);
        setMaxCounterOtIterations(maxCounterOtIterations);
    }


    @Override
    public TSPTour perturb(final TSPTour tour) {
        initiateModel(tour);
        firstUpdate = false;
        return performEDA();
    }


    @Override
    public TSPTour initiate() {
        initiateModel();
        firstUpdate = true;
        return performEDA();
    }


    /**
     * Initiates the model if there is no prior knowledge. Then each node is equally likely at each position.
     */
    protected void initiateModel() {
        double prob = 1.0 / numberNodes;
        for (int node = 0; node < numberNodes; node++) {
            for (int position = 0; position < numberNodes; position++) {
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
    protected void initiateModel(final TSPTour tspTour) {
        double prob = (1.0 - probForPriorTour) / (numberNodes - 1.0);
        for (int node = 0; node < numberNodes; node++) {
            for (int position = 0; position < numberNodes; position++) {
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
    protected TSPTour performEDA() {
        PriorityQueue<TSPTour> tspTours = new PriorityQueue<TSPTour>(Collections.reverseOrder());
        sample(sampledPopulationSize, tspTours);
        int numberIterations = 0;
        while (numberIterations < maxCounterOtIterations) {
            tspTours = PriorityQueueHelper.select(selectedPopulationSize, tspTours);
            model = estimate(tspTours, model, numberNodes, firstUpdate);
            firstUpdate = false;
            sample(sampledPopulationSize, tspTours);
            numberIterations++;
        }
        tspTours = PriorityQueueHelper.select(1, tspTours);
        return tspTours.poll();
    }


    /**
     * This methods updates the probability model.
     * <p>
     * I think, there is an error in the paper. It should be divided by m+1 and not by m+(m/N)
     *
     * @param tspTours    the current population that should be used to update the model.
     * @param model       the current model
     * @param numberNodes the number of nodes of the given problem
     * @param firstUpdate this tells the method whether the model is still empty or already filled.
     * @return the new model
     */
    protected abstract double[][] estimate(PriorityQueue<TSPTour> tspTours, double[][] model, int numberNodes,
                                           boolean firstUpdate);


    /**
     * This method creates new solutions according to the probability model and adds it to the tspTours.
     *
     * @param sizeOfPopulationAfterSampling the size, the population should have after sampling
     * @param tspTours                      the current population
     */
    protected void sample(int sizeOfPopulationAfterSampling, PriorityQueue<TSPTour> tspTours) {
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
    protected int[] rouletteWheelForCreation() {
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
    protected int[] refineTour(int[] tour) {
        //Is true, if node i is already found in the tour.
        boolean[] alreadyFound = new boolean[numberNodes];

        HashSet<Integer> missingNodes = new HashSet<>();

        HashSet<Integer> doubleLocations = new HashSet<>();

        Random randomGenerator = new Random();

        for (int i = 0; i < numberNodes; i++) {
            if (alreadyFound[tour[i]]) {
                doubleLocations.add(i);
            }
            alreadyFound[tour[i]] = true;
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
                    tour[position] = node;
                    break;
                }
                random -= ((double) distanceAddedIfNode[node]) / overallDistance;
            }

            missingNodes.remove(tour[position]);
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
        checkSampledAndSelectedSize(sampledPopulationSize, selectedPopulationSize);
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


    private void checkSampledAndSelectedSize(int sampledPopulationSize, int selectedPopulationSize) {
        if (sampledPopulationSize < selectedPopulationSize || sampledPopulationSize <= 0 || selectedPopulationSize < 0) {
            throw new IllegalArgumentException(String.format("The size of the population after sampling should be " +
                    "bigger than the size after selecting the best individuals. The sizes were %d for sampling and %d" +
                    " for selecting.", sampledPopulationSize, selectedPopulationSize));
        }
    }


    public abstract String getName();


    public String toString() {
        return "EDA" + FileParameters.separator + getName() + "\n" +
                "SampledPopulationSize" + FileParameters.separator + sampledPopulationSize + "\n" +
                "SelectedPopulationSize" + FileParameters.separator + selectedPopulationSize + "\n" +
                "MaxIterationsEDA" + FileParameters.separator + maxCounterOtIterations + "\n" +
                "APrioriProbsPositions" + FileParameters.separator + probForPriorTour + "\n";
    }


    //For new refinement
    protected Graph getGraph() {
        return graph;
    }


    //For testing only
    protected double[][] getModel() {
        return model;
    }


    protected int getNumberNodes() {
        return numberNodes;
    }


    protected void setModel(double[][] model) {
        this.model = model;
    }
}

