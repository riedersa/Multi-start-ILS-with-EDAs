package Storage;

import DataStructures.DrawingInstanceFromFile;
import DataStructures.TSPTour;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    /**
     * This method reads a file created by this program. The return value can be used for drawing the stored instance.
     * <p>
     * The numberNodes must be written before the nodeCoordinates.
     *
     * @param file the file to read
     * @return the instance for drawing
     * @throws IOException if reading gos wrong
     */
    public static DrawingInstanceFromFile read(File file) throws IOException {
        DrawingInstanceFromFile returnValue = new DrawingInstanceFromFile();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int foundLength = Integer.MAX_VALUE;
        while (true) {
            String line = reader.readLine();
            if (line.startsWith(FileParameters.name)) {
                String[] fragments = line.split(FileParameters.separator);
                returnValue.setName(fragments[1]);
            } else if (line.startsWith(FileParameters.numberNodes)) {
                String[] fragments = line.split(FileParameters.separator);
                returnValue.setNumberNodes(Integer.parseInt(fragments[1]));
            } else if (line.startsWith(FileParameters.printable)) {
                String[] fragments = line.split(FileParameters.separator);
                returnValue.setPrintable(Boolean.parseBoolean(fragments[1]));
            } else if (line.startsWith(FileParameters.nodeCoordinates)) {
                returnValue.setNodeCoordinates(readNodeCoordinates(reader, returnValue.getNumberNodes()));
            } else if (line.startsWith(FileParameters.foundLength)) {
                String[] fragments = line.split(FileParameters.separator);
                foundLength = Integer.parseInt(fragments[1]);
                if (returnValue.getTspTour() != null) {
                    returnValue.getTspTour().setLength(foundLength);
                }
            } else if (line.startsWith(FileParameters.tour)) {
                TSPTour tour = new TSPTour(readTour(reader));
                tour.setLength(foundLength);
                returnValue.setTspTour(tour);
            } else if(line.startsWith(FileParameters.eof)) {
                break;
            }
        }
        return returnValue;
    }


    /**
     * This method reads the coordinates of the nodes
     *
     * @param reader      the stream from which to read
     * @param numberNodes the number of nodes
     * @return the coordinates
     * @throws IOException if reading gos wrong
     */
    private static double[][] readNodeCoordinates(BufferedReader reader, int numberNodes) throws IOException {
        double[][] result = new double[numberNodes][3];
        for (int i = 0; i < numberNodes; i++) {
            String[] fragments = reader.readLine().split(FileParameters.separator);
            for (int j = 0; j < 3; j++) {
                result[i][j] = Double.parseDouble(fragments[j]);
            }
        }
        return result;
    }


    /**
     * This method reads a tour from the given stream.
     *
     * @param reader the stream containing the tour
     * @return the tour
     * @throws IOException if reading gos wrong
     */
    private static int[] readTour(BufferedReader reader) throws IOException {
        String[] fragments = reader.readLine().split(FileParameters.separator);
        int[] result = new int[fragments.length];
        for (int i = 0; i < fragments.length; i++) {
            result[i] = Integer.parseInt(fragments[i]);
        }
        return result;
    }

}
