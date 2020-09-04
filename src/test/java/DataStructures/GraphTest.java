package DataStructures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphTest {

    private int[][] distances;
    private int[] tour = {0, 1, 2, 3, 4};
    private int distance1 = 12;
    private int[] tour2 = {2, 4, 3, 0, 1};
    private int distance2 = 17;
    private TSPTour tspTour1 = new TSPTour(tour, 0);
    private TSPTour tspTour2 = new TSPTour(tour2, 0);


    @BeforeEach
    public void setUpDistances() {
        distances = new int[5][5];
        distances[0][0] = 0;
        distances[0][1] = 1;
        distances[0][2] = 4;
        distances[0][3] = 1;
        distances[0][4] = 3;

        distances[1][0] = 1;
        distances[1][1] = 0;
        distances[1][2] = 2;
        distances[1][3] = 1;
        distances[1][4] = 10;

        distances[2][0] = 4;
        distances[2][1] = 2;
        distances[2][2] = 0;
        distances[2][3] = 1;
        distances[2][4] = 8;

        distances[3][0] = 1;
        distances[3][1] = 1;
        distances[3][2] = 1;
        distances[3][3] = 0;
        distances[3][4] = 5;

        distances[4][0] = 3;
        distances[4][1] = 10;
        distances[4][2] = 8;
        distances[4][3] = 5;
        distances[4][4] = 0;
    }


    @Test
    public void testEvaluateTour_TSP() {
        Graph sut = new Graph(distances);
        Assertions.assertEquals((int) sut.evaluateTour(tspTour1), distance1);
        Assertions.assertEquals((int) sut.evaluateTour(tspTour2), distance2);
    }


    @Test
    public void testEvaluateTour_int() {
        Graph sut = new Graph(distances);
        Assertions.assertEquals((int) sut.evaluateTour(tour), distance1);
        Assertions.assertEquals((int) sut.evaluateTour(tour2), distance2);
    }


    @Test
    public void testEvaluateGenericTour() {
        Graph sut = new Graph(distances);
        Assertions.assertEquals(12, sut.evaluateGenericTour());
    }
}