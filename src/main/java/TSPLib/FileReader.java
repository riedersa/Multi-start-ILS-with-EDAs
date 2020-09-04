package TSPLib;

import DataStructures.ProblemInstance;

import java.io.IOException;

/**
 * This class creates a {@link ProblemInstance} containing a graph form a file of the TSPLIB.
 */
public interface FileReader {

    /**
     * Creates a graph object from a given file
     *
     * @param filename the path leading to the file
     * @return the created graph
     * @throws IOException It there occurs a problem with the file, an IOException might be thrown.
     */
    public ProblemInstance createGraphFromFile(String filename) throws IOException;

}
