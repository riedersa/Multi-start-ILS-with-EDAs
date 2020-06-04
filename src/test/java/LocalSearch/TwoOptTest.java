package LocalSearch;

import DataStructures.Graph;
import DataStructures.TSPTour;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TwoOptTest {

    private Graph graph;
    private Graph graph2;


    @Before
    public void setUp() {
        int[][] distances = new int[4][4];
        distances[0][0] = 0;
        distances[0][1] = 1;
        distances[0][2] = 4;
        distances[0][3] = 3;

        distances[1][0] = 1;
        distances[1][1] = 0;
        distances[1][2] = 3;
        distances[1][3] = 4;

        distances[2][0] = 4;
        distances[2][1] = 3;
        distances[2][2] = 0;
        distances[2][3] = 1;

        distances[3][0] = 3;
        distances[3][1] = 4;
        distances[3][2] = 1;
        distances[3][3] = 0;

        graph = new Graph(distances);

        int[][] distances2 = new int[4][4];
        distances2[0][0] = 0;
        distances2[0][1] = 2;
        distances2[0][2] = 5;
        distances2[0][3] = 1;

        distances2[1][0] = 1;
        distances2[1][1] = 0;
        distances2[1][2] = 2;
        distances2[1][3] = 5;

        distances2[2][0] = 5;
        distances2[2][1] = 1;
        distances2[2][2] = 0;
        distances2[2][3] = 2;

        distances2[3][0] = 2;
        distances2[3][1] = 5;
        distances2[3][2] = 1;
        distances2[3][3] = 0;

        graph2 = new Graph(distances2);
    }


    @Test
    public void performSearch_STEEPEST() {
        TSPTour tour = new TSPTour(new int[]{0, 2, 1, 3});
        TwoOpt sut = new TwoOpt(TwoOpt.Method.STEEPEST_DESCENT);

        TSPTour result = sut.performSearch(graph, tour);
        int[] expected = {0, 1, 2, 3};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(8, result.getLength());
    }


    @Test
    public void performSearch_NoImprovement() {
        TSPTour tour = new TSPTour(new int[]{0, 1, 2, 3});
        TwoOpt sut = new TwoOpt(TwoOpt.Method.DESCENT);

        TSPTour result = sut.performSearch(graph, tour);
        int[] expected = {0, 1, 2, 3};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(8, result.getLength());
    }


    @Test
    public void performSearch_DESCENT() {
        TSPTour tour = new TSPTour(new int[]{0, 2, 1, 3});
        TwoOpt sut = new TwoOpt(TwoOpt.Method.DESCENT);

        TSPTour result = sut.performSearch(graph, tour);
        int[] expected = {2, 1, 0, 3};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(8, result.getLength());
    }


    @Test
    public void findNextTour_DESCENT() {
        TSPTour tour = new TSPTour(new int[]{0, 2, 1, 3});
        TwoOpt sut = new TwoOpt(TwoOpt.Method.DESCENT);

        TSPTour result = sut.findNextTour(graph, tour);
        int[] expected = {2, 0, 1, 3};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(10, result.getLength());
    }


    @Test
    public void findNextTour_STEEPEST_Swap3() {
        TSPTour tour = new TSPTour(new int[]{0, 2, 1, 3});
        TwoOpt sut = new TwoOpt(TwoOpt.Method.STEEPEST_DESCENT);

        TSPTour result = sut.findNextTour(graph, tour);
        int[] expected = {0, 1, 2, 3};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(8, result.getLength());
    }
}