package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;
import Storage.FileParameters;

import java.util.PriorityQueue;
import java.util.Random;

/**
 * This class performs PBIL. All methods other than estimate ofr updating the probability model are located at the
 * superclass {@code PositionBasedEDA}. The implementation is based on "XU, Zhe, et al. Immune algorithm combined with
 * estimation of distribution for traveling salesman problem. IEEJ Transactions on Electrical and Electronic
 * Engineering, 2016, 11. Jg., S. S142-S154.".
 * <p>
 * Furthermore, this method implements some new type of refinement, similar to the one for edge based EDAs. The original
 * one takes too much time.
 */
public class PositionBasedEDA_PBIL_newRefinement extends PositionBasedEDA {

    private static String name = "Position based EDA (PBIL, new refinement)";

    private double alpha = 0.9;


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
        super(graph, selectedPopulationSize, sampledPopulationSize, maxCounterOtIterations, probForPriorTour);

        setAlpha(alpha);
    }


    /**
     * This methods updates the probability model.
     * <p>
     * I think, there is an error in the paper. It should be divided by m+1 and not by m+(m/N)
     *
     * @param tspTours    the current population that should be used to update the model.
     * @param firstUpdate this tells the method whether the model is still empty or already filled.
     */
    @Override
    protected double[][] estimate(PriorityQueue<TSPTour> tspTours, double[][] model, int numberNodes,
                                  boolean firstUpdate) {
        int populationSize = tspTours.size();
        double alpha = firstUpdate ? 1.0 : this.alpha;

        int[][] numberOccurrencesAtPosition = new int[numberNodes][numberNodes];

        for (TSPTour tspTour : tspTours) {
            int[] tour = tspTour.getTour();
            for (int position = 0; position < numberNodes; position++) {
                numberOccurrencesAtPosition[tour[position]][position]++;
            }
        }

        double noiseNumerator = 1.0 / numberNodes;
        double denominator = populationSize + 1;

        for (int node = 0; node < numberNodes; node++) {
            for (int position = 0; position < numberNodes; position++) {
                model[node][position] =
                        alpha * (numberOccurrencesAtPosition[node][position] + noiseNumerator) / denominator +
                                (1.0 - alpha) * model[node][position];
            }
        }

        return model;
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
