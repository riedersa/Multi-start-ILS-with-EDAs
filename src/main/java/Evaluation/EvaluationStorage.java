package Evaluation;

import DataStructures.CalculationInstance;
import DataStructures.ProblemInstance;
import Storage.FileParameters;
import Storage.StorageComponent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class is ues for storing the results of the EvaluatingClass to memory.
 */
public class EvaluationStorage {

    /**
     * This method stores a calculationInstance.
     *
     * @param problemInstance     the problem to which the calculation refers
     * @param calculationInstance the instance to  store
     * @return the filename
     * @throws IOException if storing does not work out
     */
    public static String storeCalculationInstance(ProblemInstance problemInstance,
                                                  CalculationInstance calculationInstance)
            throws IOException {
        return StorageComponent.store(problemInstance, calculationInstance);
    }


    /**
     * This file stores the result of one evaluation run.
     *
     * @param calculationInstances the calculation instances
     * @param filenames            the names of the files in which te calculationInstances are stored
     * @param time                 the time each calculation needed
     * @param problemInstance      the problem instance on which the run is based
     * @param opt                  the optimum for the given graph
     * @param generalInformation   some information to store
     * @return the name of the file in which everything is stored
     * @throws IOException if storing does not work out
     */
    public static String store(List<CalculationInstance> calculationInstances,
                               List<String> filenames, List<Double> time, ProblemInstance problemInstance,
                               double opt, String generalInformation) throws IOException {
        File file = createFile(problemInstance);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        addResults(writer, calculationInstances, filenames, time, problemInstance, opt, generalInformation);
        writer.write(FileParameters.eof);
        writer.flush();
        writer.close();
        return file.getName();
    }


    /**
     * This method creates an new file and returns it.
     *
     * @param problemInstance the instance for which to create the file. The name of the problem is needed.
     * @return the newly created file
     * @throws IOException if creating does not work out.
     */
    private static File createFile(ProblemInstance problemInstance) throws IOException {
        String fileSeparator = System.getProperty("file.separator");
        String fileName = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
        fileName += "_" + problemInstance.getName() + "_automation.txt";

        String relativePath = "results" + fileSeparator + "automation" + fileSeparator + fileName;
        File file = new File(relativePath);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }


    /**
     * This method adds all information to the file
     *
     * @param writer               where to write the information
     * @param calculationInstances a list of the calculations to store
     * @param filenames            the names of the files storing data for the single runs
     * @param time                 a list containing the runtimes of each run
     * @param problemInstance      the problem
     * @param opt                  the optimal value for the length of a tour
     * @param generalInformation   information on the algorithms
     * @throws IOException if writing does not work out
     */
    private static void addResults(BufferedWriter writer, List<CalculationInstance> calculationInstances,
                                   List<String> filenames, List<Double> time, ProblemInstance problemInstance,
                                   double opt, String generalInformation)
            throws IOException {
        writer.write("ProblemName:" + problemInstance.getName() + "\n");
        writer.write("Opt:" + opt + "\n");
        writer.write("GeneralInformation:" + generalInformation + "\n");

        CalculationInstance min = findMinimum(calculationInstances);
        writer.write("TimesOptFound:" + timesOptWasFound(calculationInstances, opt) + "\n");
        writer.write("Min:" + min.toString() + "\n");
        writer.write("AbsMinError:" + (min.getMinimum().getLength() - opt) + "\n");
        double relMinError = (min.getMinimum().getLength() - opt) / opt;
        writer.write("RelMinError:" + relMinError + "\n");

        double avg = computeAvgDistance(calculationInstances);
        writer.write("Avg:" + avg + "\n");
        writer.write("AbsAvgError:" + (avg - opt) + "\n");
        double relAvgError = (avg - opt) / opt;
        writer.write("RelAvgError:" + relAvgError + "\n");
        writer.write("AvgTime:" + getAvgTime(time) + "\n");

        writer.write("Calculations" + "\n");
        for (int i = 0; i < calculationInstances.size(); i++) {
            writer.write("Filename:" + filenames.get(i) + "\n");
            writer.write("Time:" + time.get(i) + "\n");
            writer.write("FoundMin:" + calculationInstances.get(i).getMinimum().toString());
        }

    }


    /**
     * Counts the number of times, a run found the optimal tour
     *
     * @param calculationInstances list of calculations
     * @param opt                  the optimal value
     * @return the number of optimal tours
     */
    private static int timesOptWasFound(List<CalculationInstance> calculationInstances, double opt) {
        int counter = 0;
        for (CalculationInstance calculationInstance : calculationInstances) {
            if (calculationInstance.getMinimum().getLength() == opt) {
                counter++;
            }
        }
        return counter;
    }


    /**
     * Finds the tour with minimal length in all runs
     *
     * @param calculationInstances information on the single runs
     * @return the minimal length
     */
    private static CalculationInstance findMinimum(List<CalculationInstance> calculationInstances) {
        CalculationInstance optInstance = null;
        double distance = Double.MAX_VALUE;
        for (CalculationInstance calculationInstance : calculationInstances) {
            if (calculationInstance.getMinimum().getLength() < distance) {
                distance = calculationInstance.getMinimum().getLength();
                optInstance = calculationInstance;
            }
        }
        return optInstance;
    }


    /**
     * Computes the average distance
     *
     * @param calculationInstances information on the runs
     * @return the average distance
     */
    private static double computeAvgDistance(List<CalculationInstance> calculationInstances) {
        double distance = 0;
        for (CalculationInstance calculationInstance : calculationInstances) {
            distance += calculationInstance.getMinimum().getLength();
        }
        return distance / calculationInstances.size();
    }


    /**
     * Computes the average time needed
     *
     * @param time the list containing all runtimes
     * @return
     */
    private static double getAvgTime(List<Double> time) {
        int value = 0;
        for (Double t : time) {
            value += t;
        }
        return value / time.size();
    }
}
