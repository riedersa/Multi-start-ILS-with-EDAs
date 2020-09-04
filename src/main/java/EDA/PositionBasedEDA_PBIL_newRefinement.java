package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;

import java.util.PriorityQueue;
import java.util.Random;

/**
 * This class performs PBIL. All methods other than estimate ofr updating the probability model are located at the
 * superclass {@code PositionBasedEDA}. The implementation is based on "XU, Zhe, et al. Immune algorithm combined with
 * estimation of distribution for traveling salesman problem. IEEJ Transactions on Electrical and Electronic
 * Engineering, 2016, 11. Jg., S. S142-S154.".
 * <p>
 * Furthermore, this method implements some new type of refinement, similar to the one for edge based EDAs. Instead of
 * creating an unfeasible tour, a feasible one is created using a roulette wheel technique.
 */
public class PositionBasedEDA_PBIL_newRefinement extends PositionBasedEDA_PBIL {

    private static String name = "Position based EDA (PBIL, new refinement)";


    /**
     * This method creates a new instance of an PBIL.
     *
     * @param graph                  the graph on which the TSP instance is based.
     * @param selectedPopulationSize the size, the population should have after selecting the best ones.
     * @param sampledPopulationSize  the size the population should have after sampling the new ones.
     * @param maxCounterOtIterations the maximum number of iterations the algorithm should perform before stopping.
     * @param probForPriorTour       the probability for a node to occure at the position it was in the given tour
     * @param alpha                  the weighting, the current population should get when updating the model
     * @throws IllegalArgumentException Throws an IllegalArgumentException if the sampledPopulationSize is smaller than
     *                                  the selectedPopulationSize or the value of alpha is smaller than 0 or bigger
     *                                  than 1.
     */
    public PositionBasedEDA_PBIL_newRefinement(Graph graph, int selectedPopulationSize, int sampledPopulationSize,
                                               int maxCounterOtIterations, double probForPriorTour, double alpha) throws IllegalArgumentException {
        super(graph, selectedPopulationSize, sampledPopulationSize, maxCounterOtIterations, probForPriorTour, alpha);
    }


    /**
     * Samples new individuals for the population until there are {@code sampledPopulationSize} individuals in the
     * population.
     *
     * @param sampledPopulationSize the size the population should have after sampling
     * @param tspTours              the current population. It is extended by new individuals.
     */
    @Override
    protected void sample(int sampledPopulationSize, PriorityQueue<TSPTour> tspTours) {
        while (tspTours.size() < sampledPopulationSize) {
            tspTours.add(createTour());
        }
    }


    /**
     * This method creates a new tour according to the probabilities in the model.
     *
     * @return the newly generated tour.
     */
    protected TSPTour createTour() {
        Random randomGenerator = new Random();
        Graph graph = super.getGraph();
        double[][] model = super.getModel();
        int numberNodes = super.getNumberNodes();

        int[] tour = new int[numberNodes];
        int positionCounter = 0;//The next position to be filled

        for (int position = positionCounter; position < numberNodes; position++) {
            double[] vector = rouletteWheelVector(tour, positionCounter, model);
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
     * @param model   the current model
     * @return the roulette vector
     */
    protected double[] rouletteWheelVector(int[] tour, int counter, double[][] model) {
        double[] vector = model[counter].clone();
        for (int i = 0; i < counter; i++) {
            vector[tour[i]] = 0;
        }
        double sum = 0;
        for (int i = 0; i < tour.length; i++) {
            sum += vector[i];
        }
        for (int i = 0; i < tour.length; i++) {
            vector[i] = vector[i] / sum;
        }
        return vector;
    }


    public static String getNameStatic() {
        return name;
    }


    public String getName() {
        return name;
    }

}
