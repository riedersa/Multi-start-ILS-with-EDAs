package TSPLib;

import DataStructures.ProblemInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class FileReaderImplementationTest {

    /**
     * This can be used for testing the implementation of the distance functions. Some length are given in the TSPLIB
     * documentation.
     *
     * @throws IOException
     */
    public void localTest() throws IOException {
        String fileName = "C:\\PathToFile\\att532.tsp";
        FileReaderImplementation fileReaderImplementation = new FileReaderImplementation();
        ProblemInstance problemInstance = fileReaderImplementation.createGraphFromFile(fileName);
        long length = problemInstance.getGraph().evaluateGenericTour();
        System.out.println(length);
    }


    @Test
    public void testCreateNodeCoordinates() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new StringReader("1 565.0 575.0\n" +
                "2 25.0 185.0\n" +
                "3 345.0 750.0\n"));
        FileReaderImplementation sut = new FileReaderImplementation();
        double[][] result = sut.createNodeCoordinates(bufferedReader, 3);
        double[][] expected = {{1, 565.0, 575.0}, {2, 25.0, 185.0,}, {3, 345.0, 750.0}};
        Assertions.assertArrayEquals(expected, result);
        ;

    }


    @Test
    public void testReadEdgeSection_Function() throws IOException {
        FileReaderImplementation sut = new FileReaderImplementation();
        BufferedReader bufferedReader = new BufferedReader(new StringReader("Hello"));
        sut.readEdgeSection(bufferedReader, 5, "FUNCTION");
        Assertions.assertNull(sut.readEdgeSection(bufferedReader, 5, "FUNCTION"));
    }


    @Test
    public void testReadFullMatrix() throws IOException {
        FileReaderImplementation sut = new FileReaderImplementation();
        int[][] edges = new int[4][4];
        sut.readFullMatrix(createFullMatrix(), edges);
        int[][] expected = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        Assertions.assertArrayEquals(expected, edges);
    }


    @Test
    public void testReadUpperRow() {
        int[] edgeWeights = {1, 2, 3, 4, 5, 6};
        FileReaderImplementation sut = new FileReaderImplementation();
        int[][] edges = new int[4][4];
        sut.readUpperRow(edgeWeights, edges);
        int[][] expected = {{0, 1, 2, 3}, {1, 0, 4, 5}, {2, 4, 0, 6}, {3, 5, 6, 0}};
        Assertions.assertArrayEquals(expected, edges);
    }


    @Test
    public void testReadUpperDiagRow() {
        int[] edgeWeights = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        FileReaderImplementation sut = new FileReaderImplementation();
        int[][] edges = new int[4][4];
        sut.readUpperDiagRow(edgeWeights, edges);
        int[][] expected = {{1, 2, 3, 4}, {2, 5, 6, 7}, {3, 6, 8, 9}, {4, 7, 9, 10}};
        Assertions.assertArrayEquals(expected, edges);
    }


    @Test
    public void testReadLowerDiagRow() {
        int[] edgeWeights = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        FileReaderImplementation sut = new FileReaderImplementation();
        int[][] edges = new int[4][4];
        sut.readLowerDiagRow(edgeWeights, edges);
        int[][] expected = {{1, 2, 4, 7}, {2, 3, 5, 8}, {4, 5, 6, 9}, {7, 8, 9, 10}};
        Assertions.assertArrayEquals(expected, edges);
    }


    @Test
    public void testFormatInputEdges() throws IOException {
        FileReaderImplementation sut = new FileReaderImplementation();
        int[] result = sut.formatInputEdges(createLowerDiagRowReader(), 36);
        int[] expected = {0, 8, 0, 39, 45, 0, 37, 47, 9, 0, 50, 49, 21, 15, 0, 61, 62, 21, 20, 17, 0, 58, 60, 16, 17,
                18, 6, 0, 59, 60, 15, 20, 26, 17, 10, 2};
        Assertions.assertArrayEquals(expected, result);
    }


    private BufferedReader createLowerDiagRowReader() {
        return new BufferedReader(new StringReader(
                "0   8   0  39  45   0  37  47   9   0  50  49  21  15   0  61  62  21\n" +
                        "  20  17   0  58  60  16  17  18   6   0  59  60  15  20  26  17  10   2\n"));
    }


    private int[] createFullMatrix() {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    }
}