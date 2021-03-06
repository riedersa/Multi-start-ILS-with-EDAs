package Main;

import DataStructures.CalculationInstance;
import DataStructures.ProblemInstance;
import EDA.*;
import LocalSearch.LocalSearch;
import LocalSearch.OrOpt;
import LocalSearch.TwoOpt;
import MultiStartILS.MultiStartILS;
import Storage.StorageComponent;
import TSPLib.FileReader;
import TSPLib.FileReaderImplementation;

import java.io.IOException;

/**
 * This class controls the communication between the GUI and the Core.
 */
public class ControllerRunning implements ThreadCompleteListener {
    private MultiStartILS multiStartILS;
    private ProblemInstance problemInstance;
    private CalculationInstance calculationInstance;
    private boolean shouldStore;
    NotifyingThread thread = new NotifyingThread() {
        @Override
        public void doRun() {
            runMultiStartILS();
        }
    };


    /**
     * Creates the new MultiStartILS
     *
     * @param edaS                   the eda represented as string
     * @param lsS                    the local search algorithm represented as string
     * @param problemInstance        the problem instance
     * @param numberLS               the maximum number of times to performe LS
     * @param numberStuck            the maximum number of times to get stuck before moving to a different initial
     *                               solution
     * @param sampledPopulationSize  the size of the poupulation after sampling
     * @param selectedPopulationSize the size of the population after selecting
     * @param maxIterationsEDA       the maximum number of iterations for one run of the eda
     * @param aPrioriEdges           Value edges in the initial tour should get
     * @param bRatio                 slack for sampling in edge based eda
     * @param aPrioriProb            probability for a city to occur at the same place it is in the initial tour for
     *                               position based EDA
     * @param alpha                  weight, the tours cuttently in a population should get when updating the model. The
     *                               current model gets weight (1-alpha).
     * @param lsMethod               steepest descent or descent
     * @return the multiStartILS
     */
    public MultiStartILS createMultiStartILS(String edaS, String lsS, ProblemInstance problemInstance,
                                             int numberLS, int numberStuck,
                                             int sampledPopulationSize,
                                             int selectedPopulationSize, int maxIterationsEDA,
                                             int aPrioriEdges, double bRatio, double aPrioriProb, double alpha,
                                             TwoOpt.Method lsMethod) {
        EDA eda;
        if (edaS.equals(PositionBasedEDA_UMDA.getNameStatic())) {
            eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(),
                    selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb);
        } else if (edaS.equals(EdgeBasedEDA.getName())) {
            eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                    sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges);
        } else if (edaS.equals(PositionBasedEDA_PBIL.getNameStatic())) {
            eda = new PositionBasedEDA_PBIL(problemInstance.getGraph(),
                    selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);
        } else if (edaS.equals(EdgeBasedEDA_UpdateWithWeight.getName())) {
            eda = new EdgeBasedEDA_UpdateWithWeight(problemInstance.getGraph(), selectedPopulationSize,
                    sampledPopulationSize, maxIterationsEDA, bRatio, aPrioriEdges, alpha);
        } else if (edaS.equals(PositionBasedEDA_PBIL_newRefinement.getNameStatic())) {
            eda = new PositionBasedEDA_PBIL_newRefinement(problemInstance.getGraph(),
                    selectedPopulationSize, sampledPopulationSize, maxIterationsEDA, aPrioriProb, alpha);
        } else {
            throw new IllegalArgumentException("The name of the EDA was incorret. It was: " + edaS);
        }


        LocalSearch ls;
        if (lsS.equals(TwoOpt.getName())) {
            ls = new TwoOpt(lsMethod);
        } else {
            ls = new OrOpt();
        }


        MultiStartILS multiStartILS = new MultiStartILS(eda, ls, problemInstance.getGraph());
        multiStartILS.setMaxTimesLS(numberLS);
        multiStartILS.setMaxTimesStuck(numberStuck);
        return multiStartILS;
    }


    /**
     * runs the Multi-Start ILS
     */
    private void runMultiStartILS() {
        multiStartILS.setContinueRunning(true);
        CalculationInstance calculationInstance = multiStartILS.performMultiStartILS();
        System.out.println(calculationInstance.getMinimum());
        this.calculationInstance = calculationInstance;
    }


    /**
     * This method runs a multistart ILS
     *
     * @param filename               the name of the TSPLib file containing the problem instance
     * @param numberLS               the maximum number of local search tries
     * @param numberStuck            the maximum number of runs which get stuck before moving to a new initial solution
     * @param eda                    the eda to use
     * @param sampledPopulationSize  the size of the population after sampling in the eda
     * @param selectedPopulationSize the size of the population after selecting in the eda
     * @param maxIterationsEDA       the maximum number of iterations one step of the eda performs
     * @param aPrioriEdges           the value a priori edges get in EdgeBased sampling
     * @param bRatio                 the bRatio value for edge based sampling
     * @param aPrioriProb            the probability for a city to be at the position it was in the initial tour
     * @param alpha                  weight, the tours cuttently in a population should get when updating the model. The
     *                               current model gets weight (1-alpha).
     * @param ls                     the local search method to use
     * @param lsMethod               Descent or Steepest descent
     * @param shouldStore            true, if the result should be saved
     * @throws IOException if the file cannot be stored
     */
    public void run(String filename, int numberLS, int numberStuck,
                    String eda, int sampledPopulationSize, int selectedPopulationSize, int maxIterationsEDA,
                    int aPrioriEdges, double bRatio, double aPrioriProb, double alpha,
                    String ls, TwoOpt.Method lsMethod, boolean shouldStore) throws IOException {
        this.shouldStore = shouldStore;
        FileReader fileReader = new FileReaderImplementation();
        problemInstance = fileReader.createGraphFromFile(filename);

        multiStartILS = createMultiStartILS(eda, ls, problemInstance, numberLS, numberStuck,
                sampledPopulationSize, selectedPopulationSize, maxIterationsEDA, aPrioriEdges, bRatio, aPrioriProb,
                alpha, lsMethod);

        thread.addListener(this);
        thread.start();
    }


    /**
     * This method tells the current multiStartILS to stop running.
     */
    public void stopRunning() {
        if (multiStartILS != null) {
            multiStartILS.setContinueRunning(false);
        }
    }


    /**
     * This method kills the thread immediately. It should only be used if there is no other way.
     */
    public void kill() {
        thread.stop();
    }


    @Override
    public void notifyOfThreadComplete(Thread thread) {
        try {
            if (shouldStore) {
                StorageComponent.store(problemInstance, calculationInstance);
            }
        } catch (IOException ioException) {
            System.err.println("Could not store the tour. An exception occured: \n" + ioException.getMessage());
            ioException.printStackTrace();
        }
        GUI.RunPanel.programFinished();
    }
}
