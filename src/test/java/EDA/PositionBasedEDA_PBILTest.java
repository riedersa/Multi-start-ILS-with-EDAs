package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PositionBasedEDA_PBILTest {

    private Graph graph;
    private int selectedPopulationSize = 2;
    private int sampledPopulationSize = 2;
    private int maxCounterOfIterations = 5;
    private double probForPriorTour = 0.2;
    double alpha = 0.9;
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


    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_Throws() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, selectedPopulationSize - 1,
                maxCounterOfIterations, probForPriorTour, alpha);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ThrowsMaxCounter() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, selectedPopulationSize - 1,
                -1, probForPriorTour, alpha);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ThrowsAlphaNeg() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, selectedPopulationSize,
                10, probForPriorTour, -1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ThrowsAlphaLarge() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, selectedPopulationSize ,
                10, probForPriorTour, 1.00001);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testsetSampled_Throws() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, 0, 1,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.setSampledPopulationSize(0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testsetSelected_Throws() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, 1, 1,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.setSelectedPopulationSize(-1);
    }


    @Test
    public void testsetSampled() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.setSampledPopulationSize(5);
        Assertions.assertEquals(5, sut.getSampledPopulationSize());
    }


    @Test
    public void testsetSelected() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.setSelectedPopulationSize(1);
        Assertions.assertEquals(1, sut.getSelectedPopulationSize());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetProbForPriorTour_throws() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.setProbForPriorTour(1.01);
    }


    @Test
    public void testSetProbForPriorTour() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.setProbForPriorTour(1.0);
        Assertions.assertEquals(1.0, sut.getProbForPriorTour(), 0.00001);

        sut.setProbForPriorTour(0);
        Assertions.assertEquals(0, sut.getProbForPriorTour(), 0.00001);

        sut.setProbForPriorTour(0.3);
        Assertions.assertEquals(0.3, sut.getProbForPriorTour(), 0.00001);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCounter_throwsNull() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.setMaxCounterOtIterations(0);
    }


    @Test
    public void testSetMaxCounter() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.setMaxCounterOtIterations(5);
        Assertions.assertEquals(5, sut.getMaxCounterOtIterations());
    }


    @Test
    public void testRouletteWheelForCreation_NoCrash() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.initiateModel();

        sut.initiateModel();
        int[] tsptour = sut.rouletteWheelForCreation();
    }


    @Test
    public void testInitiateModel() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.initiateModel();
        double[][] model = sut.getModel();
        double[][] expected = {{0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125},
                {0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125},
                {0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125},
                {0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125},
                {0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125},
                {0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125},
                {0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125},
                {0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125},
        };
        assertDoubleArrayEquals(expected, model, 0.00001);
    }


    @Test
    public void testInitiateModel_With() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.initiateModel(tspTours.getLast());
        double[][] model = sut.getModel();
        double[][] expected = {
                {0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.2, 0.1142857, 0.1142857, 0.1142857},
                {0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.2, 0.1142857, 0.1142857},
                {0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.2, 0.1142857},
                {0.2, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857},
                {0.1142857, 0.2, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857},
                {0.1142857, 0.1142857, 0.2, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857},
                {0.1142857, 0.1142857, 0.1142857, 0.2, 0.1142857, 0.1142857, 0.1142857, 0.1142857},
                {0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.1142857, 0.2},
        };
        assertDoubleArrayEquals(expected, model, 0.00001);
    }


    @Test
    public void testSample_sizeSmaller() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);


        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.sample(2, pq);

        Assertions.assertEquals(4, pq.size());
        assertPQContainsAll(pq);
        assertValidTours(pq);
    }


    @Test
    public void testSample_sizeEqual() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);


        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.sample(4, pq);

        Assertions.assertEquals(4, pq.size());
        assertPQContainsAll(pq);
        assertValidTours(pq);
    }


    @Test
    public void testSample_sizeGreater() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);

        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        sut.initiateModel();
        sut.sample(8, pq);

        Assertions.assertEquals(8, pq.size());
        assertPQContainsAll(pq);
        assertValidTours(pq);
    }


    @Test
    public void testRefineTour() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        int[] tour = sut.refineTour(new int[graph.getNumberNodes()]);
        assertAllNodesInTour(tour);


        tour = sut.refineTour(tspTours.get(1).getTour());
        Assertions.assertArrayEquals(tspTours.get(1).getTour(), tour);
    }


    @Test
    public void testEstimate_First() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);

        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        double[][] result = sut.estimate(pq, sut.getModel(), sut.getNumberNodes(), true);

        double[][] expected = new double[][]{
                {0.225, 0.025, 0.025, 0.025, 0.225, 0.225, 0.225, 0.025},
                {0.225, 0.225, 0.025, 0.025, 0.025, 0.225, 0.225, 0.025},
                {0.225, 0.225, 0.225, 0.025, 0.025, 0.025, 0.225, 0.025},
                {0.225, 0.225, 0.225, 0.225, 0.025, 0.025, 0.025, 0.025},
                {0.025, 0.225, 0.225, 0.225, 0.225, 0.025, 0.025, 0.025},
                {0.025, 0.025, 0.225, 0.225, 0.225, 0.225, 0.025, 0.025},
                {0.025, 0.025, 0.025, 0.225, 0.225, 0.225, 0.225, 0.025},
                {0.025, 0.025, 0.025, 0.025, 0.025, 0.025, 0.025, 0.825}};

        Assertions.assertArrayEquals(expected, sut.getModel());
    }


    @Test
    public void testEstimate_WithModel() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);

        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        double[][] model = {{0.5, 0.5, 0, 0, 0, 0, 0, 0},
                {0.5, 0.5, 0, 0, 0, 0, 0, 0},
                {0, 0, 0.5, 0.5, 0, 0, 0, 0},
                {0, 0, 0.5, 0.5, 0, 0, 0, 0},
                {0, 0, 0, 0, 0.5, 0.5, 0, 0},
                {0, 0, 0, 0, 0.5, 0.5, 0, 0},
                {0, 0, 0, 0, 0, 0, 0.5, 0.5},
                {0, 0, 0, 0, 0, 0, 0.5, 0.5}};

        double[][] result = sut.estimate(pq, model, sut.getNumberNodes(), false);

        double[][] expected = new double[][]{
                {0.225 * 0.9 + 0.5 * 0.1, 0.025 * 0.9 + 0.5 * 0.1, 0.025 * 0.9, 0.025 * 0.9, 0.225 * 0.9, 0.225 * 0.9
                        , 0.225 * 0.9, 0.025 * 0.9},
                {0.225 * 0.9 + 0.5 * 0.1, 0.225 * 0.9 + 0.5 * 0.1, 0.025 * 0.9, 0.025 * 0.9, 0.025 * 0.9, 0.225 * 0.9
                        , 0.225 * 0.9, 0.025 * 0.9},
                {0.225 * 0.9, 0.225 * 0.9, 0.225 * 0.9 + 0.5 * 0.1, 0.025 * 0.9 + 0.5 * 0.1, 0.025 * 0.9, 0.025 * 0.9
                        , 0.225 * 0.9, 0.025 * 0.9},
                {0.225 * 0.9, 0.225 * 0.9, 0.225 * 0.9 + 0.5 * 0.1, 0.225 * 0.9 + 0.5 * 0.1, 0.025 * 0.9, 0.025 * 0.9
                        , 0.025 * 0.9, 0.025 * 0.9},
                {0.025 * 0.9, 0.225 * 0.9, 0.225 * 0.9, 0.225 * 0.9, 0.225 * 0.9 + 0.5 * 0.1, 0.025 * 0.9 + 0.5 * 0.1
                        , 0.025 * 0.9, 0.025 * 0.9},
                {0.025 * 0.9, 0.025 * 0.9, 0.225 * 0.9, 0.225 * 0.9, 0.225 * 0.9 + 0.5 * 0.1, 0.225 * 0.9 + 0.5 * 0.1
                        , 0.025 * 0.9, 0.025 * 0.9},
                {0.025 * 0.9, 0.025 * 0.9, 0.025 * 0.9, 0.225 * 0.9, 0.225 * 0.9, 0.225 * 0.9,
                        0.225 * 0.9 + 0.5 * 0.1, 0.025 * 0.9 + 0.5 * 0.1},
                {0.025 * 0.9, 0.025 * 0.9, 0.025 * 0.9, 0.025 * 0.9, 0.025 * 0.9, 0.025 * 0.9,
                        0.025 * 0.9 + 0.5 * 0.1, 0.825 * 0.9 + 0.5 * 0.1}};

        double delta = 0.00000001;
        assertDoubleArrayEquals(expected, result, delta);
    }


    @Test
    public void testPertubeNoCrash() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        TSPTour tspTour = sut.perturb(tspTours.getFirst());
        assertValidTour(tspTour);
    }


    @Test
    public void testInitiateNoCrash() {
        PositionBasedEDA_PBIL sut = new PositionBasedEDA_PBIL(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, probForPriorTour, alpha);
        TSPTour tspTour = sut.initiate();
        assertValidTour(tspTour);
    }


    private void assertDoubleArrayEquals(double[][] expected, double[][] actual, double difference) {
        Assertions.assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i].length, actual[i].length);
            for (int j = 0; j < expected[i].length; j++) {
                Assertions.assertEquals(expected[i][j], actual[i][j], difference);
            }
        }
    }


    private void assertPQContainsAll(PriorityQueue<TSPTour> pq) {
        for (TSPTour tour : tspTours) {
            Assertions.assertTrue(pq.contains(tour));
        }
    }


    private void assertValidTours(PriorityQueue<TSPTour> pq) {
        for (TSPTour tspTour : pq) {
            if (tspTours.contains(tspTour)) {
                continue;
            }
            assertValidTour(tspTour);
        }
    }


    private void assertValidTour(TSPTour tspTour) {
        int[] tour = tspTour.getTour();
        assertAllNodesInTour(tour);
        Assertions.assertEquals(graph.evaluateTour(tour), tspTour.getLength());
    }


    private void assertAllNodesInTour(int[] tour) {
        boolean[] contains = new boolean[graph.getNumberNodes()];
        for (int i = 0; i < tour.length; i++) {
            contains[tour[i]] = true;
        }
        boolean[] expected = new boolean[graph.getNumberNodes()];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = true;
        }
        Assertions.assertArrayEquals(expected, contains);
    }


    private void addToArrayExceptDiag(double[][] array, double value) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (i != j) {
                    array[i][j] += value;
                }
            }
        }
    }
}