package EDA;

import DataStructures.Graph;
import DataStructures.TSPTour;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class EdgeBasedEDA_UpdateWithWeightTest {
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


    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrows_alphaNegative() {
        EdgeBasedEDA_UpdateWithWeight sut = new EdgeBasedEDA_UpdateWithWeight(graph, selectedPopulationSize,
                selectedPopulationSize - 1,
                maxCounterOfIterations, bratio, valueForAPrioriEdges, -0.1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrows_alphaLarge() {
        EdgeBasedEDA_UpdateWithWeight sut = new EdgeBasedEDA_UpdateWithWeight(graph, selectedPopulationSize,
                selectedPopulationSize - 1,
                maxCounterOfIterations, bratio, valueForAPrioriEdges, 1.000001);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetAlpha_regularAlpha() {
        EdgeBasedEDA_UpdateWithWeight sut = new EdgeBasedEDA_UpdateWithWeight(graph, selectedPopulationSize,
                selectedPopulationSize - 1,
                maxCounterOfIterations, bratio, valueForAPrioriEdges, 1);
        Assertions.assertEquals(1, sut.getAlpha());

        sut.setAlpha(0.4);
        Assertions.assertEquals(0.4, sut.getAlpha());

        sut.setAlpha(0);
        Assertions.assertEquals(0, sut.getAlpha());
    }


    @Test
    public void testEstimate() {
        EdgeBasedEDA sut = new EdgeBasedEDA_UpdateWithWeight(graph, selectedPopulationSize, sampledPopulationSize,
                maxCounterOfIterations, bratio, valueForAPrioriEdges, 0.9);
        PriorityQueue<TSPTour> pq = new PriorityQueue<>();
        pq.addAll(tspTours);

        double result[][] = sut.estimate(pq, sut.getEdgeHistogramMatrix(), sut.getNumberNodes());
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
        updateEdgeHistogramMatrix(expected, epsilon, 0.9);
        assertDoubleArrayEquals(expected, result, 0.0000001);
    }


    private void updateEdgeHistogramMatrix(double[][] array, double value, double mul) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (i != j) {
                    array[i][j] += value;
                    array[i][j] *= mul;
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