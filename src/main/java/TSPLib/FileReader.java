package TSPLib;

import DataStructures.Graph;

/**
 * This class creates a {@link Graph} form a file of the TSPLIB.
 */
public interface FileReader {

    /**
     * Creates a graph object from a given file
     *
     * @param filename the path leading to the file
     * @return the created graph
     */
    public Graph createGraphFromFile(String filename);

}
