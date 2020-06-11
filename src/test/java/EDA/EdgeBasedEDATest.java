package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class EdgeBasedEDATest {

    private Graph graph;
    private int selectedPopulationSize = 2;
    private int sampledPopulationSize = 2;
    private int maxCounterOfIterations = 5;
    private int valueForAPrioriEdges = 10;
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
    public void testSelect() {
        PriorityQueue<TSPTour> pq = new PriorityQueue(Collections.reverseOrder());
        pq.addAll(tspTours);
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.select(2, pq);

        Assertions.assertEquals(2, pq.size());
        Assertions.assertTrue(pq.contains(tspTours.get(1)));
        Assertions.assertTrue(pq.contains(tspTours.get(3)));
    }


    @Test
    public void compareToTest(){
        Assertions.assertTrue(tspTours.get(1).compareTo(tspTours.get(2)) < 0);
        Assertions.assertTrue(tspTours.get(2).compareTo(tspTours.get(1)) > 0);
    }


    @Test
    public void testSample_sizeSmaller() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);


        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.sample(2, pq);

        Assertions.assertEquals(4, pq.size());
        assertPQContainsAll(pq);
    }


    @Test
    public void testSample_sizeEqual() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);


        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.sample(4, pq);

        Assertions.assertEquals(4, pq.size());
        assertPQContainsAll(pq);
    }


    @Test
    public void testSample_sizeGreater() {
        PriorityQueue<TSPTour> pq = new PriorityQueue();
        pq.addAll(tspTours);


        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.sample(8, pq);

        Assertions.assertEquals(8, pq.size());
        assertPQContainsAll(pq);
    }


    @Test
    public void testRouletteWheel() {
        graph = new Graph(new int[5][5]);
        double[][] edgeHistrogramMatrix = new double[][]{{}, {}, {5, 0.25, 0, 0.5, 0.25}, {}, {}};
        int[] tour = new int[]{0, 2, 0, 0, 0};
        double[] expected = new double[]{0, 0.25, 0, 0.5, 0.25};
        int nextToFill = 2;

        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);

        sut.setEdgeHistogramMatrix(edgeHistrogramMatrix);
        double[] vector = sut.rouletteWheelVector(tour, nextToFill);

        assertVectorSumsToOne(vector);
        Assertions.assertArrayEquals(expected, vector);
    }


    @Test
    public void testRouletteWheel_All() {
        graph = new Graph(new int[5][5]);
        double[][] edgeHistrogramMatrix = new double[][]{{5, 1, 3, 0.5, 0.5}, {}, {}, {}, {}};
        int[] tour = new int[]{0, 0, 0, 0, 0};
        double[] expected = new double[]{0, 0.2, 0.6, 0.1, 0.1};
        int nextToFill = 1;

        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);

        sut.setEdgeHistogramMatrix(edgeHistrogramMatrix);
        double[] vector = sut.rouletteWheelVector(tour, nextToFill);

        assertVectorSumsToOne(vector);
        Assertions.assertArrayEquals(expected, vector);
    }


    @Test
    public void testRouletteWheel_OneMissing() {
        graph = new Graph(new int[5][5]);
        double[][] edgeHistrogramMatrix = new double[][]{{}, {}, {}, {}, {5, 1, 3, 0.5, 0.5}};
        int[] tour = new int[]{1, 2, 3, 4, 0};
        double[] expected = new double[]{1, 0, 0, 0, 0};
        int nextToFill = 4;

        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);

        sut.setEdgeHistogramMatrix(edgeHistrogramMatrix);
        double[] vector = sut.rouletteWheelVector(tour, nextToFill);

        assertVectorSumsToOne(vector);
        Assertions.assertArrayEquals(expected, vector);
    }


    @Test
    public void testInitiateEdgeHistogramMatrix_Without() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.initiateEdgeHistogramMatrix();
        double[][] expected = new double[][]{{0, 1, 1, 1, 1, 1, 1, 1}, {1, 0, 1, 1, 1, 1, 1, 1}, {1, 1, 0, 1, 1, 1, 1
                , 1}, {1, 1, 1, 0, 1, 1, 1, 1}, {1, 1, 1, 1, 0, 1, 1, 1}, {1, 1, 1, 1, 1, 0, 1, 1}, {1, 1, 1, 1, 1, 1
                , 0, 1}, {1, 1, 1, 1, 1, 1, 1, 0}};

        double[][] eHM = sut.getEdgeHistogramMatrix();

        Assertions.assertArrayEquals(expected, eHM);
    }


    @Test
    public void testInitiateEdgeHistogramMatrix_With() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.initiateEdgeHistogramMatrix(tspTours.getLast());
        double valueOfApriori = sut.getValueForAPrioriEdges();
        double[][] expected = new double[][]{
                {0, valueOfApriori, 1, 1, 1, 1, valueOfApriori, 1},
                {valueOfApriori, 0, valueOfApriori, 1, 1, 1, 1, 1},
                {1, valueOfApriori, 0, 1, 1, 1, 1, valueOfApriori},
                {1, 1, 1, 0, valueOfApriori, 1, 1, valueOfApriori},
                {1, 1, 1, valueOfApriori, 0, valueOfApriori, 1, 1},
                {1, 1, 1, 1, valueOfApriori, 0, valueOfApriori, 1},
                {valueOfApriori, 1, 1, 1, 1, valueOfApriori, 0, 1},
                {1, 1, valueOfApriori, valueOfApriori, 1, 1, 1, 0}};

        double[][] eHM = sut.getEdgeHistogramMatrix();

        Assertions.assertArrayEquals(expected, eHM);
    }


    @Test(expected = IllegalArgumentException.class)
    public void setValueOfAPrioriEdges_thrwsNull() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setValueForAPrioriEdges(0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void setValueOfAPrioriEdges_thrwsNeg() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setValueForAPrioriEdges(-1);
    }


    @Test
    public void setValueOfAPrioriEdges() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setValueForAPrioriEdges(2);
        Assertions.assertEquals(2, sut.getValueForAPrioriEdges());
        sut.setValueForAPrioriEdges(110);
        Assertions.assertEquals(110, sut.getValueForAPrioriEdges());
    }


    @Test
    public void testCreateTour() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.initiateEdgeHistogramMatrix();

        assertValidTour(sut.createTour());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrows() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, selectedPopulationSize - 1,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetSampledThrows() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setSampledPopulationSize(0);
    }


    @Test
    public void testSetSampled() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setSampledPopulationSize(5);
        Assertions.assertEquals(5, sut.getSampledPopulationSize());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedThrows() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setSelectedPopulationSize(-1);
    }


    @Test
    public void testSetSelected() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setSelectedPopulationSize(0);
        Assertions.assertEquals(0, sut.getSelectedPopulationSize());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCounterThrows() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setMaxCounterOtIterations(0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCounterNegThrows() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setMaxCounterOtIterations(-5);
    }


    @Test
    public void testSetMaxCounter() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        sut.setMaxCounterOtIterations(5);
        Assertions.assertEquals(5, sut.getMaxCounterOtIterations());
    }


    @Test
    public void testSetBRatio_Epsilon() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        Assertions.assertEquals(0.08571428571, sut.getEpsilon(), 0.000001);
        sut.setbRatio(1);
        Assertions.assertEquals(0.5714285714, sut.getEpsilon(), 0.000001);
        Assertions.assertEquals(1, sut.getbRatio());
    }


    @Test
    public void testSetSelected_Epsilon() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        Assertions.assertEquals(0.08571428571, sut.getEpsilon(), 0.000001);
        sut.setSelectedPopulationSize(1);
        Assertions.assertEquals(0.04285714286, sut.getEpsilon(), 0.000001);
    }


    @Test
    public void testEstimate() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        PriorityQueue<TSPTour> pq = new PriorityQueue<>();
        pq.addAll(tspTours);

        sut.estimate(pq);

        double[][] ehm = sut.getEdgeHistogramMatrix();
        double epsilon = sut.getEpsilon();

        double[][] expected = new double[][]{
                {0, 3, 0, 0, 0, 0, 3, 2},
                {3, 0, 3, 0, 0, 0, 0, 2},
                {0, 3, 0, 3, 0, 0, 0, 2},
                {0, 0, 3, 0, 4, 0, 0, 1},
                {0, 0, 0, 4, 0, 4, 0, 0},
                {0, 0, 0, 0, 4, 0, 4, 0},
                {3, 0, 0, 0, 0, 4, 0, 1},
                {2, 2, 2, 1, 0, 0, 1, 0}
        };
        addToArrayExceptDiag(expected, epsilon);
        assertDoubleArrayEquals(expected, ehm, 0.0000001);
    }


    @Test
    public void testPertubeNoCrash() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        TSPTour tspTour = sut.perturb(tspTours.getFirst());
        assertValidTour(tspTour);
    }


    @Test
    public void testInitiateNoCrash() {
        EdgeBasedEDA sut = new EdgeBasedEDA(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges);
        TSPTour tspTour = sut.initiate();
        assertValidTour(tspTour);
    }


    private void updateLengthTours() {
        for (TSPTour tour : tspTours) {
            graph.setDistanceToTour(tour);
        }
    }


    private void assertPQContainsAll(PriorityQueue<TSPTour> pq) {
        for (TSPTour tour : tspTours) {
            Assertions.assertTrue(pq.contains(tour));
        }
    }


    private void assertVectorSumsToOne(double[] vector) {
        double sum = 0;
        for (int i = 0; i < vector.length; i++) {
            sum += vector[i];
        }
        Assertions.assertEquals(1, sum);
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


    private void addToArrayExceptDiag(double[][] array, double value) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (i != j) {
                    array[i][j] += value;
                }
            }
        }
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

}