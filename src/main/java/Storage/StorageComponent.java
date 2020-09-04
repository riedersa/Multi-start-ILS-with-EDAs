package Storage;

import DataStructures.CalculationInstance;
import DataStructures.ProblemInstance;
import DataStructures.TSPTour;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class can store a given Problem instance with a found solution to a file for drawing it later.
 */
public class StorageComponent {

    /**
     * This method stores the solution and the problem instance to a file.
     *
     * @param problemInstance     the problem instance for which a solution was found
     * @param calculationInstance the information on the calculation
     * @return the name of the file in which the results were stored
     * @throws IOException if soring the file did not work
     */
    public static String store(ProblemInstance problemInstance, CalculationInstance calculationInstance) throws IOException {
        File file = createFile(problemInstance);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writePreamble(writer, problemInstance);
        writeUsedMethods(writer, calculationInstance);
        writeTour(writer, calculationInstance.getMinimum());
        writeCalculationPath(writer, calculationInstance);
        writer.write(FileParameters.eof);
        writer.flush();
        writer.close();
        return file.getName();
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
            writer.write(FileParameters.nodeCoordinates + "\n");
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
        if (tour == null) {
            System.err.println("Could not write minimal tour since there was none");
            return;
        }
        writer.write(FileParameters.foundLength + tour.getLength() + "\n");
        writer.write(FileParameters.tour + "\n");
        writer.write(writeIntArray(tour.getTour()) + "\n");
    }


    /**
     * This method writes an int array. Each entry is separated using the separator given in {@link FileParameters}.
     *
     * @param array the array to write
     * @return the String representing the array
     */
    private static String writeIntArray(int[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i] + FileParameters.separator);
        }
        sb.append(array[array.length - 1]);
        return sb.toString();
    }


    /**
     * This method returns a string representing the double array. Each entry is separated using the separator given in
     * {@link FileParameters}.
     *
     * @param array the array to represent
     * @return the string representation
     */
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


    /**
     * this method writes the instances of the list contained in the calculation instance to the given writer.
     *
     * @param writer              where to write the calculations
     * @param calculationInstance the instance that should be written
     * @throws IOException if writing gos wrong
     */
    private static void writeCalculationPath(BufferedWriter writer, CalculationInstance calculationInstance)
            throws IOException {
        List<TSPTour> tours = calculationInstance.getTours();
        List<CalculationInstance.CalculationKind> kinds = calculationInstance.getKinds();
        for (int i = 0; i < tours.size(); i++) {
            writer.write(FileParameters.pathTour + kinds.get(i) + "\n");
            writeIntermediateTour(writer, tours.get(i));
        }
    }


    /**
     * This function writes one tour.
     *
     * @param writer the place to which to write the tour
     * @param tour   the tour to write
     * @throws IOException if writing does not work
     */
    private static void writeIntermediateTour(BufferedWriter writer, TSPTour tour) throws IOException {
        writer.write(FileParameters.iFoundLength + tour.getLength() + "\n");
        writer.write(writeIntArray(tour.getTour()) + "\n");
    }


    /**
     * This function writes the used methods to the file
     *
     * @param writer              where to write
     * @param calculationInstance the calculation
     * @throws IOException if something does not work out
     */
    private static void writeUsedMethods(BufferedWriter writer, CalculationInstance calculationInstance)
            throws IOException {
        writer.write(calculationInstance.getEda());
        writer.write(calculationInstance.getLs());
        writer.write(calculationInstance.getMultiStartILS());
    }

}
