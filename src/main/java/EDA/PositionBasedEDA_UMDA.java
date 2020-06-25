package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;

import java.util.PriorityQueue;

/**
 * This class performs UMDA. All methods other than estimate ofr updating the probability model are located at the
 * supercalss {@code PositionBasedEDA}. The algorithm stops after a specified number of iterations. The implementation
 * is based on "XU, Zhe, et al. Immune algorithm combined with estimation of distribution for traveling salesman
 * problem. IEEJ Transactions on Electrical and Electronic Engineering, 2016, 11. Jg., S. S142-S154.".
 */
public class PositionBasedEDA_UMDA extends PositionBasedEDA {

    private static String name = "Position based EDA (UMDA)";


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
    public PositionBasedEDA_UMDA(Graph graph, int selectedPopulationSize, int sampledPopulationSize,
                                 int maxCounterOtIterations, double probForPriorTour) throws IllegalArgumentException {
        super(graph, selectedPopulationSize, sampledPopulationSize, maxCounterOtIterations, probForPriorTour);
    }


    @Override
    protected double[][] estimate(PriorityQueue<TSPTour> tspTours, double[][] model, int numberNodes,
                                  boolean firstUpdate) {
        int populationSize = tspTours.size();

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
                model[node][position] = (numberOccurrencesAtPosition[node][position] + noiseNumerator) / denominator;
            }
        }
        return model;
    }


    /**
     * This method returns the name of the used algorithm.
     *
     * @return the name of the algorithm
     */
    public static String getNameStatic() {
        return name;
    }


    @Override
    public String getName() {
        return name;
    }
}
