package TSPLib;

import DataStructures.Graph;
import DataStructures.ProblemInstance;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class FileReaderImplementation implements FileReader {

    private final Logger logger = Logger.getLogger(FileReaderImplementation.class.getName());


    public FileReaderImplementation() {
        logger.setUseParentHandlers(false);
    }


    @Override
    public ProblemInstance createGraphFromFile(String filename) throws IOException {
        File file = new File(filename);

        BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(file));

        ProblemInstance problemInstance = new ProblemInstance();

        String edge_weight_type = "?";
        String edge_weight_format = "?";

        boolean continueLoop = true;

        int[][] edges = new int[0][0];

        while (continueLoop) {
            String line = bufferedReader.readLine();
            String[] fragments = line.split(" ");
            switch (fragments[0]) {
                case "NAME":
                    problemInstance.setName(fragments[2]);
                    break;
                case "NAME:":
                    problemInstance.setName(fragments[1]);
                    break;
                case "TYPE":
                    problemInstance.setType(fragments[2]);
                    break;
                case "TYPE:":
                    problemInstance.setType(fragments[1]);
                    break;
                case "DIMENSION":
                    problemInstance.setDimension(Integer.parseInt(fragments[2]));
                    break;
                case "DIMENSION:":
                    problemInstance.setDimension(Integer.parseInt(fragments[1]));
                    break;
                case "EDGE_WEIGHT_TYPE":
                    edge_weight_type = fragments[2];
                    break;
                case "EDGE_WEIGHT_TYPE:":
                    edge_weight_type = fragments[1];
                    break;
                case "EDGE_WEIGHT_FORMAT":
                    edge_weight_format = fragments[2];
                    break;
                case "EDGE_WEIGHT_FORMAT:":
                    edge_weight_format = fragments[1];
                    break;
                case "NODE_COORD_SECTION":
                    problemInstance.setNodeCoordinates(createNodeCoordinates(bufferedReader,
                            problemInstance.getDimension()));
                    problemInstance.setCanDraw(true);
                    break;
                case "EDGE_WEIGHT_SECTION":
                    edges = readEdgeSection(bufferedReader, problemInstance.getDimension(), edge_weight_format);
                    break;
                case "DISPLAY_DATA_SECTION":
                    problemInstance.setNodeCoordinates(createNodeCoordinates(bufferedReader,
                            problemInstance.getDimension()));
                    problemInstance.setCanDraw(true);
                    break;
                case "EOF":
                    continueLoop = false;
                    break;
                default:
                    logger.info(String.format("There was an unknown line command in the fine %s: %s", filename,
                            fragments[0]));
                    break;
            }
        }
        setGraph(problemInstance, edge_weight_type, edges);
        return problemInstance;
    }


    /**
     * creates the graph based on the edges
     *
     * @param problemInstance  where to put the graph
     * @param edge_weight_type the edge format in which they are given
     * @param edges            the edge matrix
     */
    protected void setGraph(ProblemInstance problemInstance, String edge_weight_type, int[][] edges) {
        switch (edge_weight_type) {
            case "EXPLICIT":
                problemInstance.setGraph(new Graph(edges));
                break;
            case "EUC_2D":
                edges = DistanceCalculator.calculateDistancesEUC2D(problemInstance.getNodeCoordinates());
                problemInstance.setGraph(new Graph(edges));
                break;
            case "ATT":
                edges = DistanceCalculator.calculateDistancesATT(problemInstance.getNodeCoordinates());
                problemInstance.setGraph(new Graph(edges));
                break;
            case "GEO":
                edges = DistanceCalculator.calculateDistancesGEO(problemInstance.getNodeCoordinates());
                problemInstance.setGraph(new Graph(edges));
                break;
            case "CEIL_2D":
                edges = DistanceCalculator.calculateDistancesCEIL2D(problemInstance.getNodeCoordinates());
                problemInstance.setGraph(new Graph(edges));
                break;
            default:
                logger.warning(String.format("EDGE_WEIGHT_TYPE %s not implemented", edge_weight_type));
                break;
        }
    }


    /**
     * Reads the node coordinates
     *
     * @param bufferedReader reader from which to read
     * @param dimension      2D or 3D
     * @return an array containing the coordinates
     * @throws IOException if the format in the BufferedReader is wrong
     */
    protected double[][] createNodeCoordinates(BufferedReader bufferedReader, int dimension) throws IOException {
        double[][] coordinates = new double[dimension][3];
        for (int i = 0; i < dimension; i++) {
            String[] fragments = bufferedReader.readLine().split(" ");
            fragments = removeEmptyStrings(fragments);
            coordinates[i][0] = Integer.parseInt(fragments[0]);
            coordinates[i][1] = Double.parseDouble(fragments[1]);
            coordinates[i][2] = Double.parseDouble(fragments[2]);
        }
        return coordinates;
    }


    /**
     * reads the edges and returns an array containing the distances
     *
     * @param bufferedReader from where to read
     * @param dimension      the number of nodes
     * @param edgeFormat     the format in which the edges are given
     * @return the distance matrix
     * @throws IOException it the format of the input is wrong
     */
    protected int[][] readEdgeSection(BufferedReader bufferedReader, int dimension, String edgeFormat) throws IOException {
        int[][] edges = new int[dimension][dimension];
        switch (edgeFormat) {
            case "FUNCTION":
                logger.warning(String.format("There should not be an EDGE_WEIGHT_SECTION for EDGE_WEIGHT_FORMAT %s",
                        edgeFormat));
                return null;
            case "FULL_MATRIX":
                readFullMatrix(bufferedReader, edges);
                break;
            case "UPPER_ROW":
                readUpperRow(formatInputEdges(bufferedReader, (dimension * (dimension - 1) / 2)), edges);
                break;
            case "UPPER_DIAG_ROW":
                readUpperDiagRow(formatInputEdges(bufferedReader, (dimension * (dimension + 1) / 2)), edges);
                break;
            case "LOWER_DIAG_ROW":
                readLowerDiagRow(formatInputEdges(bufferedReader, (dimension * (dimension + 1) / 2)), edges);
                break;
            default:
                logger.warning(String.format("The EDGE_WEIGHT_FORMAT for %s is not implementd", edgeFormat));
                break;
        }
        return edges;
    }


    protected void readFullMatrix(BufferedReader bufferedReader, int[][] edges) throws IOException {
        for (int i = 0; i < edges.length; i++) {
            String[] fragments = bufferedReader.readLine().split(" ");

            for (int j = 0; j < edges.length; j++) {
                edges[i][j] = Integer.parseInt(fragments[j]);
            }
        }
    }


    protected void readUpperRow(int[] edgeWeights, int[][] edges) {
        int index = 0;
        for (int i = 0; i < edges.length; i++) {
            for (int j = i + 1; j < edges.length; j++) {
                edges[i][j] = edgeWeights[index++];
                edges[j][i] = edges[i][j];
            }
        }
    }


    protected void readUpperDiagRow(int[] edgeWeights, int[][] edges) {
        int index = 0;
        for (int i = 0; i < edges.length; i++) {
            for (int j = i; j < edges.length; j++) {
                edges[i][j] = edgeWeights[index++];
                edges[j][i] = edges[i][j];
            }
        }
    }


    protected void readLowerDiagRow(int[] edgeWeights, int[][] edges) {
        int index = 0;
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j <= i; j++) {
                edges[i][j] = edgeWeights[index++];
                edges[j][i] = edges[i][j];
            }
        }
    }


    /**
     * Formats the edges in a matrix properly
     *
     * @param bufferedReader the source from which to read the edges
     * @param expectedNumers the expected number of edges
     * @return the values in an array
     * @throws IOException If the format of the input is wrong
     */
    protected int[] formatInputEdges(BufferedReader bufferedReader, int expectedNumers) throws IOException {
        String[] fragments = bufferedReader.readLine().split(" ");
        int[] result = new int[expectedNumers];
        int index = 0;
        for (int i = 0; i < expectedNumers; i++) {
            if (index >= fragments.length) {
                index = 0;
                fragments = bufferedReader.readLine().split(" ");
            }
            if (fragments[index].length() == 0) {
                index++;
                i--;
            } else {
                result[i] = Integer.parseInt(fragments[index]);
                index++;
            }
        }
        return result;
    }


    /**
     * This method removes empty strings from an array
     *
     * @param strings the array from which to remove the strings.
     * @return the new generated array
     */
    private String[] removeEmptyStrings(String[] strings) {
        int counter = 0;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] != null && strings[i].length() > 0) {
                counter++;
            }
        }
        String[] result = new String[counter];
        int position = 0;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] != null && strings[i].length() > 0) {
                result[position++] = strings[i];
            }
        }
        return result;
    }

}
