package Evaluation;

import DataStructures.CalculationInstance;
import DataStructures.ProblemInstance;
import EDA.*;
import LocalSearch.LocalSearch;
import LocalSearch.OrOpt;
import LocalSearch.TwoOpt;
import MultiStartILS.MultiStartILS;
import Storage.FileParameters;
import Storage.StorageComponent;
import TSPLib.FileReader;
import TSPLib.FileReaderImplementation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class can be used for executing some runs on the data specified in the file.
 */
public class EvaluatingClass {

    public static void main(String[] args) {
        //TODO call for some instances
    }


    private static void executeForFile(String filename, double opt) throws IOException {
        FileReader fileReader = new FileReaderImplementation();
        ProblemInstance problemInstance = fileReader.createGraphFromFile(filename);
        ArrayList<String> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        //TODO: add some stuff to do
        //TODO: remember to store Files and calculation instances
        long startTime = System.nanoTime();
        //TODO methods here
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1000000.0;
        //TODO: remember to store time

        store(calculationInstances, filenames, time, problemInstance, opt);
    }


    private static CalculationInstance execute_EdgeBased_TwoOpt_Steepest(ProblemInstance problemInstance,
                                                                         int numberLS, int numberStuck,
                                                                         int sampledPopulationSize,
                                                                         int selectedPopulationSize,
                                                                         int maxIterationsEDA,
                                                                         int aPrioriEdges, double bRatio) {
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_EdgeBasedHistory_TwoOpt_Steepest(ProblemInstance problemInstance,
                                                                                int numberLS,
                                                                                int numberStuck,
                                                                                int sampledPopulationSize,
                                                                                int selectedPopulationSize,
                                                                                int maxIterationsEDA,
                                                                                int aPrioriEdges, double bRatio,
                                                                                double alpha)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new EdgeBasedEDA_UpdateWithWeight(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_EdgeBased_TwoOpt_Descent(ProblemInstance problemInstance, int numberLS
            , int numberStuck,
                                                                        int sampledPopulationSize,
                                                                        int selectedPopulationSize,
                                                                        int maxIterationsEDA,
                                                                        int aPrioriEdges, double bRatio)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_EdgeBasedHistory_TwoOpt_Descent(ProblemInstance problemInstance,
                                                                               int numberLS,
                                                                               int numberStuck,
                                                                               int sampledPopulationSize,
                                                                               int selectedPopulationSize,
                                                                               int maxIterationsEDA,
                                                                               int aPrioriEdges, double bRatio,
                                                                               double alpha)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new EdgeBasedEDA_UpdateWithWeight(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_EdgeBased_OrOpt(ProblemInstance problemInstance, int numberLS,
                                                               int numberStuck,
                                                               int sampledPopulationSize,
                                                               int selectedPopulationSize,
                                                               int maxIterationsEDA,
                                                               int aPrioriEdges, double bRatio)
            throws IOException {
        EDA eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges);

        LocalSearch ls = new OrOpt();

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_EdgeBasedHistory_OrOpt(ProblemInstance problemInstance, int numberLS,
                                                                      int numberStuck,
                                                                      int sampledPopulationSize,
                                                                      int selectedPopulationSize,
                                                                      int maxIterationsEDA,
                                                                      int aPrioriEdges, double bRatio,
                                                                      double alpha)
            throws IOException {

        EDA eda = new EdgeBasedEDA_UpdateWithWeight(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges, alpha);

        LocalSearch ls = new OrOpt();

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_UMDA_TwoOpt_Steepest(ProblemInstance problemInstance, int numberLS,
                                                                    int numberStuck,
                                                                    int sampledPopulationSize,
                                                                    int selectedPopulationSize,
                                                                    int maxIterationsEDA,
                                                                    double aPrioriProb)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_PBIL_TwoOpt_Steepest(ProblemInstance problemInstance, int numberLS,
                                                                    int numberStuck,
                                                                    int sampledPopulationSize,
                                                                    int selectedPopulationSize,
                                                                    int maxIterationsEDA,
                                                                    double aPrioriProb, double alpha)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new PositionBasedEDA_PBIL(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_PBILRefinement_TwoOpt_Steepest(ProblemInstance problemInstance,
                                                                              int numberLS,
                                                                              int numberStuck,
                                                                              int sampledPopulationSize,
                                                                              int selectedPopulationSize,
                                                                              int maxIterationsEDA,
                                                                              double aPrioriProb, double alpha)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new PositionBasedEDA_PBIL_newRefinement(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_UMDA_TwoOpt_Descent(ProblemInstance problemInstance, int numberLS,
                                                                   int numberStuck,
                                                                   int sampledPopulationSize,
                                                                   int selectedPopulationSize,
                                                                   int maxIterationsEDA,
                                                                   double aPrioriProb)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_PBIL_TwoOpt_Descent(ProblemInstance problemInstance, int numberLS,
                                                                   int numberStuck,
                                                                   int sampledPopulationSize,
                                                                   int selectedPopulationSize,
                                                                   int maxIterationsEDA,
                                                                   double aPrioriProb, double alpha)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new PositionBasedEDA_PBIL(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_PBILRefinement_TwoOpt_Descent(ProblemInstance problemInstance,
                                                                             int numberLS,
                                                                             int numberStuck,
                                                                             int sampledPopulationSize,
                                                                             int selectedPopulationSize,
                                                                             int maxIterationsEDA,
                                                                             double aPrioriProb, double alpha)
            throws IOException {
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new PositionBasedEDA_PBIL_newRefinement(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_UMDA_OrOpt(ProblemInstance problemInstance, int numberLS,
                                                          int numberStuck,
                                                          int sampledPopulationSize,
                                                          int selectedPopulationSize,
                                                          int maxIterationsEDA,
                                                          double aPrioriProb)
            throws IOException {

        EDA eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb);

        LocalSearch ls = new OrOpt();

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_PBIL_OrOpt(ProblemInstance problemInstance, int numberLS,
                                                          int numberStuck,
                                                          int sampledPopulationSize,
                                                          int selectedPopulationSize,
                                                          int maxIterationsEDA,
                                                          double aPrioriProb, double alpha)
            throws IOException {

        EDA eda = new PositionBasedEDA_PBIL(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new OrOpt();

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    private static CalculationInstance execute_PBILRefinement_OrOpt(ProblemInstance problemInstance, int numberLS,
                                                                    int numberStuck,
                                                                    int sampledPopulationSize,
                                                                    int selectedPopulationSize,
                                                                    int maxIterationsEDA,
                                                                    double aPrioriProb, double alpha)
            throws IOException {

        EDA eda = new PositionBasedEDA_PBIL_newRefinement(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new OrOpt();

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        return multiStartILS.performMultiStartILS();
    }


    /**
     * This method stores a calculationInstance.
     *
     * @param problemInstance     the problem to which the calculation refers
     * @param calculationInstance the instance to  store
     * @return the filename
     */
    private static String storeCalculationInstance(ProblemInstance problemInstance,
                                                   CalculationInstance calculationInstance)
            throws IOException {
        return StorageComponent.store(problemInstance, calculationInstance);
    }


    private static void addResults(BufferedWriter writer, List<CalculationInstance> calculationInstances,
                                   List<String> filenames, List<String> time, ProblemInstance problemInstance,
                                   double opt)
            throws IOException {
        writer.write("ProblemName:" + problemInstance.getName() + "\n");
        writer.write("Opt:" + opt + "\n");

        CalculationInstance min = findMinimum(calculationInstances);
        writer.write("Min:" + min.toString() + "\n");
        writer.write("AbsMinError:" + (min.getMinimum().getLength() - opt) + "\n");
        double relMinError = (min.getMinimum().getLength() - opt) / opt;
        writer.write("RelMinError:" + relMinError + "\n");

        double avg = computeAvgDistance(calculationInstances);
        writer.write("Avg:" + avg + "\n");
        writer.write("AbsAvgError:" + (avg - opt) + "\n");
        double relAvgError = (avg - opt) / opt;
        writer.write("RelAvgError:" + relAvgError + "\n");

        writer.write("Calculations" + "\n");
        for (int i = 0; i < calculationInstances.size(); i++) {
            writer.write("Filename:" + filenames.get(i) + "\n");
            writer.write("Time:" + time.get(i) + "\n");
            writer.write("FoundMin:" + calculationInstances.get(i).getMinimum().toString());
        }

    }


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


    private static double computeAvgDistance(List<CalculationInstance> calculationInstances) {
        double distance = 0;
        for (CalculationInstance calculationInstance : calculationInstances) {
            distance += calculationInstance.getMinimum().getLength();
        }
        return distance / calculationInstances.size();
    }


    public static String store(List<CalculationInstance> calculationInstances,
                               List<String> filenames, List<String> time, ProblemInstance problemInstance,
                               double opt) throws IOException {
        File file = createFile(problemInstance);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        addResults(writer, calculationInstances, filenames, time, problemInstance, opt);
        writer.write(FileParameters.eof);
        writer.flush();
        writer.close();
        return file.getName();
    }


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

}
