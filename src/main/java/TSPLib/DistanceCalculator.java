package TSPLib;

/**
 * This class can be used to calculate distances from node coordinates as given in the TSPLib. There is no test for this
 * class since we do not have any test data. One can only use the generic tours given in the TSPLib.
 */
public class DistanceCalculator {

    private static int xCoordinate = 1;
    private static int yCoordinate = 2;
    private static double pi = 3.141592;
    private static double rrr = 6378.388;


    /**
     * This function calculates the distance matrix for EDGE_FORMAT given in EUC_2D.
     *
     * @param nodeCoordinates the coordinates of the nodes
     * @return the distance matrix
     */
    protected static int[][] calculateDistancesEUC2D(double[][] nodeCoordinates) {
        int numberNodes = nodeCoordinates.length;
        int[][] distances = new int[numberNodes][numberNodes];

        for (int i = 0; i < numberNodes; i++) {
            distances[i][i] = 0;

            for (int j = i + 1; j < numberNodes; j++) {
                double xd = nodeCoordinates[i][xCoordinate] - nodeCoordinates[j][xCoordinate];
                double yd = nodeCoordinates[i][yCoordinate] - nodeCoordinates[j][yCoordinate];

                double dij = Math.sqrt((xd * xd + yd * yd));

                distances[i][j] = nint(dij);
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }


    /**
     * This function calculates the distance matrix for EDGE_FORMAT given in ATT.
     *
     * @param nodeCoordinates the coordinates of the nodes
     * @return the distance matrix
     */
    protected static int[][] calculateDistancesATT(double[][] nodeCoordinates) {
        int numberNodes = nodeCoordinates.length;
        int[][] distances = new int[numberNodes][numberNodes];

        for (int i = 0; i < numberNodes; i++) {
            distances[i][i] = 0;

            for (int j = i + 1; j < numberNodes; j++) {
                double xd = nodeCoordinates[i][xCoordinate] - nodeCoordinates[j][xCoordinate];
                double yd = nodeCoordinates[i][yCoordinate] - nodeCoordinates[j][yCoordinate];

                double rij = Math.sqrt((xd * xd + yd * yd) / 10.0);
                int tij = nint(rij);
                int dij;
                if (tij < rij) {
                    dij = tij + 1;
                } else {
                    dij = tij;
                }

                distances[i][j] = dij;
                distances[j][i] = dij;
            }
        }
        return distances;
    }


    /**
     * This function calculates the distance matrix for EDGE_FORMAT given in CEIL_2D.
     *
     * @param nodeCoordinates the coordinates of the nodes
     * @return the distance matrix
     */
    protected static int[][] calculateDistancesCEIL2D(double[][] nodeCoordinates) {
        int numberNodes = nodeCoordinates.length;
        int[][] distances = new int[numberNodes][numberNodes];

        for (int i = 0; i < numberNodes; i++) {
            distances[i][i] = 0;

            for (int j = i + 1; j < numberNodes; j++) {
                double xd = nodeCoordinates[i][xCoordinate] - nodeCoordinates[j][xCoordinate];
                double yd = nodeCoordinates[i][yCoordinate] - nodeCoordinates[j][yCoordinate];

                double dij = Math.sqrt((xd * xd + yd * yd));

                distances[i][j] = roundUp(dij);
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }


    /**
     * This function calculates the distance matrix for EDGE_FORMAT given in GEO.
     *
     * @param nodeCoordinates the coordinates of the nodes
     * @return the distance matrix
     */
    protected static int[][] calculateDistancesGEO(double[][] nodeCoordinates) {
        int numberNodes = nodeCoordinates.length;
        int[][] distances = new int[numberNodes][numberNodes];

        for (int i = 0; i < numberNodes; i++) {
            distances[i][i] = 0;

            double latitude_i = radCompuatation(nodeCoordinates[i][xCoordinate]);
            double longitude_i = radCompuatation(nodeCoordinates[i][yCoordinate]);

            for (int j = i + 1; j < numberNodes; j++) {
                double latitude_j = radCompuatation(nodeCoordinates[j][xCoordinate]);
                double longitude_j = radCompuatation(nodeCoordinates[j][yCoordinate]);

                double q1 = Math.cos(longitude_i - longitude_j);
                double q2 = Math.cos(latitude_i - latitude_j);
                double q3 = Math.cos(latitude_i + latitude_j);

                int dij = (int) (rrr * Math.acos(0.5 * ((1.0 + q1) * q2 - (1.0 - q1) * q3)) + 1.0);

                distances[i][j] = roundUp(dij);
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }


    /**
     * This is a rounding function as described in the TSPLib.
     *
     * @param x the value to round
     * @return the rounded result
     */
    private static int nint(double x) {
        return (int) (x + 0.5);
    }


    /**
     * This function round the value up.
     *
     * @param x the value to round up
     * @return the rounded value
     */
    private static int roundUp(double x) {
        if (((int) x) < x) {
            return (int) x + 1;
        } else {
            return ((int) x);
        }
    }


    /**
     * This is used for radiant computation. It can be found in the TSPLib
     *
     * @param coordinate the coordinate for which to do the computation
     * @return the result of the computation
     */
    private static double radCompuatation(double coordinate) {
        int deg = (int) coordinate;
        double min = coordinate - deg;
        double rad = pi * (deg + 5.0 * min / 3.0) / 180.0;
        return rad;
    }
}
