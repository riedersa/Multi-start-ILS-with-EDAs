package Evaluation;

import DataStructures.CalculationInstance;
import DataStructures.Graph;
import DataStructures.ProblemInstance;
import EDA.*;
import LocalSearch.LocalSearch;
import LocalSearch.OrOpt;
import LocalSearch.TwoOpt;
import MultiStartILS.MultiStartILS;
import TSPLib.FileReader;
import TSPLib.FileReaderImplementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class can be used for executing some runs on the data specified in the file.
 */
public class EvaluatingClass {

    private static long threeMinutes = 180000000000L;


    public static void main(String[] args) throws IOException {
        //TODO call for some instances
        executeForFile("C:\\1_Daten\\Studium\\SS20\\IDP\\ProblemInstances\\TSP\\bayg29.tsp", 1610);
        System.out.println("Executed: bayg29");
        executeForFile("C:\\1_Daten\\Studium\\SS20\\IDP\\ProblemInstances\\TSP\\gr96.tsp", 55209);
        System.out.println("Executed: gr96");
        executeForFile("C:\\1_Daten\\Studium\\SS20\\IDP\\ProblemInstances\\TSP\\pr124.tsp", 59030);
        System.out.println("Executed: pr124");
    }


    /**
     * This executes an algorithm for some file.
     *
     * @param filename the file for which to execute
     * @param opt      the optimum result for this tour
     * @throws IOException if reading or writing goes wrong.
     */
    private static void executeForFile(String filename, double opt) throws IOException {
        FileReader fileReader = new FileReaderImplementation();
        ProblemInstance problemInstance = fileReader.createGraphFromFile(filename);
        //Todo let some instances run

        execute_EdgeBased_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 40, 0.001, 10, opt, threeMinutes, true);
        /*execute_EdgeBased_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 40, 0.1, 10, opt);*/

        //Todo: If i know what values are best suited, i could use OrOpt and Steepest Descent

        execute_EdgeBasedHistory_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 40, 0.001, 0.1, 10, opt,
                threeMinutes, true);

        /*execute_PBIL_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 0.50, 0.1, 10, opt);*/
        execute_PBIL_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 0.10, 0.1, 10, opt, threeMinutes, true);

        /*execute_PBILRefinement_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 0.10, 0.1, 10, opt);

        execute_UMDA_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 0.50,  10, opt);*/

        /*execute_EdgeBased_OrOpt(problemInstance, 20, 3, 200,
                100, 100, 40, 0.001, 10, opt);*/
        /*execute_EdgeBased_TwoOpt_Steepest(problemInstance, 20, 3, 200,
                100, 100, 40, 0.001, 10, opt, threeMinutes, true);
        /*execute_EdgeBasedHistory_OrOpt(problemInstance, 20, 3, 200,
                100, 100, 40, 0.001, 0.1,10, opt);*/
        execute_EdgeBasedHistory_TwoOpt_Steepest(problemInstance, 20, 3, 200,
                100, 100, 40, 0.001, 0.1, 10, opt, threeMinutes, true);

        /*execute_PBIL_OrOpt(problemInstance, 20, 3, 200,
                100, 100, 0.10, 0.1, 10, opt);*/
        execute_PBIL_TwoOpt_Steepest(problemInstance, 20, 3, 200,
                100, 100, 0.10, 0.1, 10, opt, threeMinutes, true);
        /*execute_PBIL_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 0.20, 0.1, 10, opt);*/
    }


    private static void execute_EdgeBased_TwoOpt_Steepest(ProblemInstance problemInstance,
                                                          int numberLS, int numberStuck,
                                                          int sampledPopulationSize,
                                                          int selectedPopulationSize,
                                                          int maxIterationsEDA,
                                                          int aPrioriEdges, double bRatio,
                                                          int numberRepetitions, double opt,
                                                          long maxExecutuinNanoSeconds, boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();

        EDA eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges);

        LocalSearch ls = new TwoOpt(LocalSearch.Method.STEEPEST_DESCENT);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutuinNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "EdgeBased_TwoOpt_Steepest_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_EdgeBasedHistory_TwoOpt_Steepest(ProblemInstance problemInstance,
                                                                 int numberLS,
                                                                 int numberStuck,
                                                                 int sampledPopulationSize,
                                                                 int selectedPopulationSize,
                                                                 int maxIterationsEDA,
                                                                 int aPrioriEdges, double bRatio,
                                                                 double alpha,
                                                                 int numberRepetitions, double opt,
                                                                 long maxExecutionNanoSeconds, boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new EdgeBasedEDA_UpdateWithWeight(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "EdgeBasedHistory_TwoOpt_Steepest_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_EdgeBased_TwoOpt_Descent(ProblemInstance problemInstance, int numberLS, int numberStuck,
                                                         int sampledPopulationSize,
                                                         int selectedPopulationSize,
                                                         int maxIterationsEDA,
                                                         int aPrioriEdges, double bRatio,
                                                         int numberRepetitions, double opt,
                                                         long maxExecutionNanoSeconds, boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "EdgeBased_TwoOpt_Descent_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_EdgeBasedHistory_TwoOpt_Descent(ProblemInstance problemInstance,
                                                                int numberLS,
                                                                int numberStuck,
                                                                int sampledPopulationSize,
                                                                int selectedPopulationSize,
                                                                int maxIterationsEDA,
                                                                int aPrioriEdges, double bRatio,
                                                                double alpha,
                                                                int numberRepetitions, double opt,
                                                                long maxExecutionNanoSeconds, boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new EdgeBasedEDA_UpdateWithWeight(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "EdgeBasedHistory_TwoOpt_Descent_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_EdgeBased_OrOpt(ProblemInstance problemInstance, int numberLS,
                                                int numberStuck,
                                                int sampledPopulationSize,
                                                int selectedPopulationSize,
                                                int maxIterationsEDA,
                                                int aPrioriEdges, double bRatio,
                                                int numberRepetitions, double opt, long maxExecutionNanoSeconds,
                                                boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        EDA eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges);

        LocalSearch ls = new OrOpt();

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "EdgeBased_OrOpt_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_EdgeBasedHistory_OrOpt(ProblemInstance problemInstance, int numberLS,
                                                       int numberStuck,
                                                       int sampledPopulationSize,
                                                       int selectedPopulationSize,
                                                       int maxIterationsEDA,
                                                       int aPrioriEdges, double bRatio,
                                                       double alpha,
                                                       int numberRepetitions, double opt,
                                                       long maxExecutionNanoSeconds, boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();

        EDA eda = new EdgeBasedEDA_UpdateWithWeight(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges, alpha);

        LocalSearch ls = new OrOpt();

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "EdgeBasedHistory_OrOpt_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_UMDA_TwoOpt_Steepest(ProblemInstance problemInstance, int numberLS,
                                                     int numberStuck,
                                                     int sampledPopulationSize,
                                                     int selectedPopulationSize,
                                                     int maxIterationsEDA,
                                                     double aPrioriProb,
                                                     int numberRepetitions, double opt, long maxExecutionNanoSeconds,
                                                     boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "UMDA_TwoOpt_Steepest_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_PBIL_TwoOpt_Steepest(ProblemInstance problemInstance, int numberLS,
                                                     int numberStuck,
                                                     int sampledPopulationSize,
                                                     int selectedPopulationSize,
                                                     int maxIterationsEDA,
                                                     double aPrioriProb, double alpha,
                                                     int numberRepetitions, double opt, long maxExecutionNanoSeconds,
                                                     boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new PositionBasedEDA_PBIL(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "PBIL_TwoOpt_Steepest_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_PBILRefinement_TwoOpt_Steepest(ProblemInstance problemInstance,
                                                               int numberLS,
                                                               int numberStuck,
                                                               int sampledPopulationSize,
                                                               int selectedPopulationSize,
                                                               int maxIterationsEDA,
                                                               double aPrioriProb, double alpha,
                                                               int numberRepetitions, double opt,
                                                               long maxExecutionNanoSeconds, boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.STEEPEST_DESCENT;

        EDA eda = new PositionBasedEDA_PBIL_newRefinement(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "PBILRefinement_TwoOpt_Steepest_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_UMDA_TwoOpt_Descent(ProblemInstance problemInstance, int numberLS,
                                                    int numberStuck,
                                                    int sampledPopulationSize,
                                                    int selectedPopulationSize,
                                                    int maxIterationsEDA,
                                                    double aPrioriProb,
                                                    int numberRepetitions, double opt, long maxExecutionNanoSeconds,
                                                    boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "UMDA_TwoOpt_Descent_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_PBIL_TwoOpt_Descent(ProblemInstance problemInstance, int numberLS,
                                                    int numberStuck,
                                                    int sampledPopulationSize,
                                                    int selectedPopulationSize,
                                                    int maxIterationsEDA,
                                                    double aPrioriProb, double alpha,
                                                    int numberRepetitions, double opt, long maxExecutionNanoSeconds,
                                                    boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new PositionBasedEDA_PBIL(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "PBIL_TwoOpt_Descent_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_PBILRefinement_TwoOpt_Descent(ProblemInstance problemInstance,
                                                              int numberLS,
                                                              int numberStuck,
                                                              int sampledPopulationSize,
                                                              int selectedPopulationSize,
                                                              int maxIterationsEDA,
                                                              double aPrioriProb, double alpha,
                                                              int numberRepetitions, double opt,
                                                              long maxExecutionNanoSeconds, boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        LocalSearch.Method lsMethod = LocalSearch.Method.DESCENT;

        EDA eda = new PositionBasedEDA_PBIL_newRefinement(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new TwoOpt(lsMethod);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "PBILRefinement_TwoOpt_Descent_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_UMDA_OrOpt(ProblemInstance problemInstance, int numberLS,
                                           int numberStuck,
                                           int sampledPopulationSize,
                                           int selectedPopulationSize,
                                           int maxIterationsEDA,
                                           double aPrioriProb,
                                           int numberRepetitions, double opt, long maxExecutionNanoSeconds,
                                           boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();
        EDA eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb);

        LocalSearch ls = new OrOpt();

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "UMDA_OrOpt_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_PBIL_OrOpt(ProblemInstance problemInstance, int numberLS,
                                           int numberStuck,
                                           int sampledPopulationSize,
                                           int selectedPopulationSize,
                                           int maxIterationsEDA,
                                           double aPrioriProb, double alpha,
                                           int numberRepetitions, double opt, long maxExecutionNanoSeconds,
                                           boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();

        EDA eda = new PositionBasedEDA_PBIL(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new OrOpt();

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "PBIL_OrOpt_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    private static void execute_PBILRefinement_OrOpt(ProblemInstance problemInstance, int numberLS,
                                                     int numberStuck,
                                                     int sampledPopulationSize,
                                                     int selectedPopulationSize,
                                                     int maxIterationsEDA,
                                                     double aPrioriProb, double alpha,
                                                     int numberRepetitions, double opt,
                                                     long maxExecutuinNanoSeconds, boolean runUntilOpt)
            throws IOException {
        ArrayList<Double> time = new ArrayList<>();
        ArrayList<CalculationInstance> calculationInstances = new ArrayList<>();
        ArrayList<String> filenames = new ArrayList<>();

        EDA eda = new PositionBasedEDA_PBIL_newRefinement(problemInstance.getGraph(),
                selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);

        LocalSearch ls = new OrOpt();

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutuinNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "PBILRefinement_OrOpt_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    /**
     * This method performs a multistart ILS based on the algorithms given.
     *
     * @param ls          the local search algorithm
     * @param eda         the eda
     * @param graph       the graph for which to find a TSP solution
     * @param numberLS    the maximum number to perform local search
     * @param numberStuck the maximum number to call an eda if the local search is stuck
     * @param time        the list in which to store the needed time
     * @return the calculation instance produced
     */
    private static CalculationInstance executeMulstistartILS(LocalSearch ls, EDA eda, Graph graph, int numberLS,
                                                             int numberStuck, List<Double> time, long maxNanoSeconds,
                                                             double opt, boolean runUntilOpt) {
        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, graph);
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);

        if (!runUntilOpt) {
            long startTime = System.nanoTime();
            CalculationInstance result = multiStartILS.performMultiStartILS();
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000.0;
            time.add(duration);
            return result;
        } else {
            long startTime = System.nanoTime();
            long finalTime = startTime + maxNanoSeconds;
            CalculationInstance result = multiStartILS.performMultiStartILSWithTime(finalTime, opt);
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000.0;
            time.add(duration);
            return result;
        }
    }

}
