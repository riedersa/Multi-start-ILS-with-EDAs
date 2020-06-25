package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.LinkedList;

public class PositionBasedEDA_PBIL_newRefinementTest {
    private Graph graph;
    private int selectedPopulationSize = 2;
    private int sampledPopulationSize = 2;
    private int maxCounterOfIterations = 5;
    private double probForPriorTour = 0.2;
    private double bratio = 0.15;
    private LinkedList<TSPTour> tspTours;


    @Before
    public void setUpGraph() {
        int[][] distances = new int[8][8];
        distances[0][0] = 0;
        distances[0][1] = 2;
        distances[0][2] = 5;
        distances[0][3] = 5;
        distances[0][4] = 5;
        distances[0][5] = 5;
        distances[0][6] = 5;
        distances[0][7] = 2;

        distances[1][0] = 2;
        distances[1][1] = 0;
        distances[1][2] = 2;
        distances[1][3] = 5;
        distances[1][4] = 5;
        distances[1][5] = 5;
        distances[1][6] = 5;
        distances[1][7] = 5;

        distances[2][0] = 5;
        distances[2][1] = 2;
        distances[2][2] = 0;
        distances[2][3] = 2;
        distances[2][4] = 5;
        distances[2][5] = 5;
        distances[2][6] = 5;
        distances[2][7] = 5;

        distances[3][0] = 5;
        distances[3][1] = 5;
        distances[3][2] = 2;
        distances[3][3] = 0;
        distances[3][4] = 2;
        distances[3][5] = 5;
        distances[3][6] = 5;
        distances[3][7] = 5;

        distances[4][0] = 5;
        distances[4][1] = 5;
        distances[4][2] = 5;
        distances[4][3] = 2;
        distances[4][4] = 0;
        distances[4][5] = 2;
        distances[4][6] = 5;
        distances[4][7] = 5;

        distances[5][0] = 5;
        distances[5][1] = 5;
        distances[5][2] = 5;
        distances[5][3] = 5;
        distances[5][4] = 2;
        distances[5][5] = 0;
        distances[5][6] = 2;
        distances[5][7] = 5;

        distances[6][0] = 5;
        distances[6][1] = 5;
        distances[6][2] = 5;
        distances[6][3] = 5;
        distances[6][4] = 5;
        distances[6][5] = 2;
        distances[6][6] = 0;
        distances[6][7] = 2;

        distances[7][0] = 2;
        distances[7][1] = 5;
        distances[7][2] = 5;
        distances[7][3] = 5;
        distances[7][4] = 5;
        distances[7][5] = 5;
        distances[7][6] = 2;
        distances[7][7] = 0;

        graph = new Graph(distances);
    }


    @Before
    public void setUpTours() {
        tspTours = new LinkedList<>();
        TSPTour tsptour1 = new TSPTour(new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 7);
        TSPTour tsptour2 = new TSPTour(new int[]{1, 2, 3, 4, 5, 6, 0, 7}, 4);
        TSPTour tsptour3 = new TSPTour(new int[]{2, 3, 4, 5, 6, 0, 1, 7}, 8);
        TSPTour tsptour4 = new TSPTour(new int[]{3, 4, 5, 6, 0, 1, 2, 7}, 6);

        tspTours.add(tsptour1);
        tspTours.add(tsptour2);
        tspTours.add(tsptour3);
        tspTours.add(tsptour4);
    }


    @Test
    public void testCreateTour() {
        PositionBasedEDA_PBIL_newRefinement sut = new PositionBasedEDA_PBIL_newRefinement(graph,
                selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, 0.9);
        sut.initiateModel();

        assertValidTour(sut.createTour());
    }


    @Test
    public void testCreateTour_probOne() {
        double[][] edgeHistogramMatrix = new double[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 0, 0, 0}};
        PositionBasedEDA_PBIL_newRefinement sut = new PositionBasedEDA_PBIL_newRefinement(new Graph(new int[][]{{1, 1
                , 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1,
                1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1,
                1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
                , {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}}),
                selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, 0.9);
        sut.setModel(edgeHistogramMatrix);
        TSPTour createdTour = sut.createTour();
        Assertions.assertArrayEquals(new int[]{0, 1, 9, 2, 3, 4, 5, 6, 7, 8}, makeZeroStartNode(createdTour.getTour()));
    }


    @Test
    public void testRouletteWheelVector() {
        double[][] model = new double[][]{{}, {1, 1, 4, 1, 1, 2, 1, 1, 1, 1}, {}};
        PositionBasedEDA_PBIL_newRefinement sut = new PositionBasedEDA_PBIL_newRefinement(graph,
                selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, 0.9);
        double[] rouletteWheel = sut.rouletteWheelVector(new int[]{2, 0, 0, 0, 1, 1, 1, 1, 1, 1}, 1, model);
        Assertions.assertArrayEquals(new double[]{0.1, 0.1, 0, 0.1, 0.1, 0.2, 0.1, 0.1, 0.1, 0.1}, rouletteWheel);
    }


    @Test
    public void testRouletteWheelVector_Identity() {
        double[][] model = new double[][]{{0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        PositionBasedEDA_PBIL_newRefinement sut = new PositionBasedEDA_PBIL_newRefinement(graph,
                selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, 0.9);
        double[] rouletteWheel = sut.rouletteWheelVector(new int[]{2, 0, 0, 0, 1, 1, 1, 1, 1, 1}, 1, model);
        Assertions.assertArrayEquals(new double[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, rouletteWheel);
    }


    @Test
    public void testRouletteWheel() {
        graph = new Graph(new int[5][5]);
        double[][] model = new double[][]{{}, {}, {5, 0.25, 0, 0.5, 0.25}, {}, {}};
        int[] tour = new int[]{0, 2, 0, 0, 0};
        double[] expected = new double[]{0, 0.25, 0, 0.5, 0.25};
        int nextToFill = 2;

        PositionBasedEDA_PBIL_newRefinement sut = new PositionBasedEDA_PBIL_newRefinement(graph,
                selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, 0.9);

        double[] vector = sut.rouletteWheelVector(tour, nextToFill, model);

        assertVectorSumsToOne(vector);
        Assertions.assertArrayEquals(expected, vector);
    }


    @Test
    public void testRouletteWheel_All() {
        graph = new Graph(new int[5][5]);
        double[][] model = new double[][]{{}, {5, 1, 3, 0.5, 0.5}, {}, {}, {}};
        int[] tour = new int[]{0, 0, 0, 0, 0};
        double[] expected = new double[]{0, 0.2, 0.6, 0.1, 0.1};
        int nextToFill = 1;

        PositionBasedEDA_PBIL_newRefinement sut = new PositionBasedEDA_PBIL_newRefinement(graph,
                selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, 0.9);
        double[] vector = sut.rouletteWheelVector(tour, nextToFill, model);

        assertVectorSumsToOne(vector);
        Assertions.assertArrayEquals(expected, vector);
    }


    @Test
    public void testRouletteWheel_OneMissing() {
        graph = new Graph(new int[5][5]);
        double[][] model = new double[][]{{}, {}, {}, {}, {5, 1, 3, 0.5, 0.5}};
        int[] tour = new int[]{1, 2, 3, 4, 0};
        double[] expected = new double[]{1, 0, 0, 0, 0};
        int nextToFill = 4;

        PositionBasedEDA_PBIL_newRefinement sut = new PositionBasedEDA_PBIL_newRefinement(graph,
                selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, 0.9);
        double[] vector = sut.rouletteWheelVector(tour, nextToFill, model);

        assertVectorSumsToOne(vector);
        Assertions.assertArrayEquals(expected, vector);
    }


    private void assertVectorSumsToOne(double[] vector) {
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        Assertions.assertEquals(1, sum);
    }


    public int[] makeZeroStartNode(int[] tour) {
        int[] newTour = new int[tour.length];
        int index = 0;
        for (int i = 0; i < tour.length; i++) {
            if (tour[i] == 0) {
                index = i;
                break;
            }
        }
        for (int i = 0; i < tour.length; i++) {
            newTour[i] = tour[(i + index) % tour.length];
        }
        return newTour;
    }


    private void assertValidTour(TSPTour tspTour) {
        int[] tour = tspTour.getTour();
        boolean[] contains = new boolean[graph.getNumberNodes()];
        for (int i = 0; i < tour.length; i++) {
            contains[tour[i]] = true;
        }
        boolean[] expected = new boolean[graph.getNumberNodes()];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = true;
        }
        Assertions.assertArrayEquals(expected, contains);
        Assertions.assertEquals(graph.evaluateTour(tour), tspTour.getLength());
    }


}