package Storage;

import DataStructures.ProblemInstance;
import DataStructures.TSPTour;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class can store a given Problem instance with a found solution to a file for drawing it later.
 */
public class StorageComponent {

    /**
     * This method stores the solution and the problem instance to a file.
     *
     * @param problemInstance the problem instance for which a solution was found
     * @param tspTour         the found solution
     * @throws IOException if soring the file did not work
     */
    public static void store(ProblemInstance problemInstance, TSPTour tspTour) throws IOException {
        File file = createFile(problemInstance);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writePreamble(writer, problemInstance);
        writeTour(writer, tspTour);
        writer.write("EOF");
        writer.flush();
        writer.close();
    }


    /**
     * This method creates a file for a problem instance
     *
     * @param problemInstance the problem instance
     * @return the created file
     * @throws IOException if something went wrong during creation of the file
     */
    private static File createFile(ProblemInstance problemInstance) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        fileName += "_" + problemInstance.getName() + ".txt";

        String relativePath = "results" + fileSeparator + fileName;
        File file = new File(relativePath);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }


    /**
     * This method writes the important information of a problemInstance to a steam.
     *
     * @param writer          the stream
     * @param problemInstance the problem instancs
     * @throws IOException
     */
    private static void writePreamble(BufferedWriter writer, ProblemInstance problemInstance) throws IOException {
        writer.write(FileParameters.name + problemInstance.getName() + "\n");
        writer.write(FileParameters.numberNodes + problemInstance.getGraph().getNumberNodes() + "\n");
        writer.write(FileParameters.printable + problemInstance.isCanDraw() + "\n");
        if (problemInstance.isCanDraw()) {
            writer.write(FileParameters.nodeCoordinates+"\n");
            double[][] nodeCoordinates = problemInstance.getNodeCoordinates();
            for (int i = 0; i < nodeCoordinates.length; i++) {
                writer.write(writeDoubleArray(nodeCoordinates[i]) + "\n");
            }
        }
    }


    /**
     * This method writes a tour to the given stream.
     *
     * @param writer the stream
     * @param tour   the tour
     * @throws IOException
     */
    private static void writeTour(BufferedWriter writer, TSPTour tour) throws IOException {
        writer.write(FileParameters.foundLength + tour.getLength() + "\n");
        writer.write(FileParameters.tour+"\n");
        writer.write(writeIntArray(tour.getTour()) + "\n");
    }


    private static String writeIntArray(int[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i] + FileParameters.separator);
        }
        sb.append(array[array.length - 1]);
        return sb.toString();
    }


    private static String writeDoubleArray(double[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i] + FileParameters.separator);
        }
        sb.append(array[array.length - 1]);
        return sb.toString();
    }


    /**
     * This method stores one single TSPTour to a given file.
     *
     * @param file    the file in which to store
     * @param tspTour the tour to store
     * @throws IOException if storing did not work
     */
    public static void storeIntermediateResultToFile(File file, TSPTour tspTour) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writeTour(writer, tspTour);
        writer.flush();
        writer.close();
    }

}
