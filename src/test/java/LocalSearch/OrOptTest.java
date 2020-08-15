package LocalSearch;

import DataStructures.Graph;
import DataStructures.TSPTour;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class OrOptTest {

    private Graph graph;


    @Before
    public void setUp() {
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


    @Test
    public void performSearch_NoImprovement() {
        TSPTour tour = new TSPTour(new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        OrOpt sut = new OrOpt();

        TSPTour result = sut.performSearch(graph, tour);
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(16, result.getLength());
    }


    @Test
    public void performSearch() {
        TSPTour tour = new TSPTour(new int[]{0, 2, 3, 4, 1, 5, 6, 7});
        OrOpt sut = new OrOpt();

        TSPTour result = sut.performSearch(graph, tour);
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(16, result.getLength());
    }


    @Test
    public void performSearch_ShiftFirst() {
        TSPTour tour = new TSPTour(new int[]{1, 0, 2, 3, 4, 5, 6, 7});
        OrOpt sut = new OrOpt();

        TSPTour result = sut.performSearch(graph, tour);
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(16, result.getLength());
    }

    @Test
    public void performSearch_ShiftLast() {
        TSPTour tour = new TSPTour(new int[]{0, 1, 2, 3, 4, 5, 7, 6});
        OrOpt sut = new OrOpt();

        TSPTour result = sut.performSearch(graph, tour);
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7};
        Assertions.assertArrayEquals(expected, result.getTour());
        Assertions.assertEquals(16, result.getLength());
    }
}