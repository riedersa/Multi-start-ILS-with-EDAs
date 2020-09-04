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
 * <p>
 * Step 1: add a call of  {@code executeForFile} with the absolute path of test file to the main function.
 * <p>
 * Step 2: add all method calls to run to the method  {@code executeForFile}. Some examples can be found there.
 * <p>
 * Step 3: start the program
 */
public class EvaluatingClass {
    //The minutes need to be given in nanoseconds. This are fixed values for one minute and three minutes.
    private static long threeMinutes = 180000000000L;
    private static long oneMinute = 60000000000L;

    private static int numberRuns = 20;


    public static void main(String[] args) throws IOException {
        executeForFile("C:\\absPath\\bayg29.tsp", 1610);
        System.out.println("Executed: bayg29");
    }


    /**
     * This executes an algorithm for some file.
     *
     * @param filename the file for which to execute
     * @param opt      the optimum result for this tour
     * @throws IOException if reading or writing goes wrong.
     */
    private static void executeForFile(String filename, double opt) throws IOException {
        //The first two lines need to stay unchanged
        FileReader fileReader = new FileReaderImplementation();
        ProblemInstance problemInstance = fileReader.createGraphFromFile(filename);

        //Some examples
        execute_EdgeBased_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 40, 0.001, numberRuns, opt, threeMinutes, false);

        execute_UMDA_TwoOpt_Descent(problemInstance, 20, 3, 200,
                100, 100, 0.20, numberRuns, opt, oneMinute, true);
    }


    /**
     * This method executes the edge based eda with Two-Opt and steepest descent.
     *
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriEdges            the value, edges that are already in a tour, should get when initializing the
     *                                matrix
     * @param bRatio                  the bRatio value for the eda. If this value is high, the perturbation is high
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the fiven number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
    private static void execute_EdgeBased_TwoOpt_Steepest(ProblemInstance problemInstance,
                                                          int numberLS, int numberStuck,
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

        EDA eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges);

        LocalSearch ls = new TwoOpt(LocalSearch.Method.STEEPEST_DESCENT);

        for (int repetition = 0; repetition < numberRepetitions; repetition++) {
            CalculationInstance result = executeMulstistartILS(ls, eda, problemInstance.getGraph(), numberLS,
                    numberStuck, time, maxExecutionNanoSeconds, opt, runUntilOpt);
            calculationInstances.add(result);
            filenames.add(EvaluationStorage.storeCalculationInstance(problemInstance, result));
            System.out.println("Run" + repetition);
        }

        EvaluationStorage.store(calculationInstances, filenames, time, problemInstance, opt,
                "EdgeBased_TwoOpt_Steepest_" + numberRepetitions +
                        "_times_EDA[" + eda.toString() + "]_LS[" + ls.toString() + "]");
    }


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriEdges            the value, edges that are already in a tour, should get when initializing the
     *                                matrix
     * @param bRatio                  the bRatio value for the eda. If this value is high, the perturbation is high
     * @param alpha                   the weight with which the last computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the fiven number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriEdges            the value, edges that are already in a tour, should get when initializing the
     *                                matrix
     * @param bRatio                  the bRatio value for the eda. If this value is high, the perturbation is high
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriEdges            the value, edges that are already in a tour, should get when initializing the
     *                                matrix
     * @param bRatio                  the bRatio value for the eda. If this value is high, the perturbation is high
     * @param alpha                   the value with which the current computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the fiven number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriEdges            the value, edges that are already in a tour, should get when initializing the
     *                                matrix
     * @param bRatio                  the bRatio value for the eda. If this value is high, the perturbation is high
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriEdges            the value, edges that are already in a tour, should get when initializing the
     *                                matrix
     * @param bRatio                  the bRatio value for the eda. If this value is high, the perturbation is high
     * @param alpha                   the value with which the current computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param alpha                   the weight with which the current computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param alpha                   the weight with which the current computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again * @param sampledPopulationSize   the size a population in the eda should
     *                                have after sampling * @param selectedPopulationSize  the size a population should
     *                                have after selecting. Take care, that it is smaller * then the samplingSize. *
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param alpha                   the weight with which the current computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param alpha                   the weight with which the current computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param alpha                   the weight with which the current computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
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


    /**
     * @param problemInstance         the instance on which the algorithms should run
     * @param numberLS                the number of local searches
     * @param numberStuck             the number of times the algorithm can get stuck before starting with a new point
     *                                again
     * @param sampledPopulationSize   the size a population in the eda should have after sampling
     * @param selectedPopulationSize  the size a population should have after selecting. Take care, that it is smaller
     *                                then the samplingSize.
     * @param maxIterationsEDA        the number of iterations in one call of the eda
     * @param aPrioriProb             the probability for a node to occurat the position it was in the given tour
     * @param alpha                   the weight with which the current computation should be taken into account
     * @param numberRepetitions       the number of runs of the multi-start iterated local search that should be
     *                                performed
     * @param opt                     the optimal tour length for this problem instance. It can be found in the TSPLib
     * @param maxExecutionNanoSeconds the time each run should consume. This is only important if runUntilOpt is set
     * @param runUntilOpt             decides if the algorithm should use the given number of iterations in numberLS (if
     *                                the value is false) or if it should run until it finds the optimum or time is up.
     * @throws IOException if storing the results does not work
     */
    private static void execute_PBILRefinement_OrOpt(ProblemInstance problemInstance, int numberLS,
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

        EDA eda = new PositionBasedEDA_PBIL_newRefinement(problemInstance.getGraph(),
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
