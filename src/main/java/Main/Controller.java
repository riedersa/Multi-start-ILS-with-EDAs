package Main;

import DataStructures.ProblemInstance;
import DataStructures.TSPTour;
import EDA.EDA;
import EDA.EdgeBasedEDA;
import EDA.PositionBasedEDA_UMDA;
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
public class Controller implements ThreadCompleteListener {
    private MultiStartILS multiStartILS;
    private ProblemInstance problemInstance;
    private TSPTour optTourFound;
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
     * @param sampledPopulationSiue  the size of the poupulation after sampling
     * @param selectedPopulationSize the size of the population after selecting
     * @param maxIterationsEDA       the maximum number of iterations for one run of the eda
     * @param aPrioriEdges           Value edges in the initial tour should get
     * @param bRatio                 slack for sampling in edge based eda
     * @param aPrioriProb            probability for a city to occur at the same place it is in the initial tour for
     *                               position based EDA
     * @param lsMethod               steepest descent or descent
     * @return the multiStartILS
     */
    private MultiStartILS createMultiStartILS(String edaS, String lsS, ProblemInstance problemInstance,
                                              int numberLS, int numberStuck,
                                              int sampledPopulationSiue,
                                              int selectedPopulationSize, int maxIterationsEDA,
                                              int aPrioriEdges, double bRatio, double aPrioriProb,
                                              TwoOpt.Method lsMethod) {
        EDA eda;
        if (edaS.equals(PositionBasedEDA_UMDA.getName())) {
            eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(),
                    selectedPopulationSize, sampledPopulationSiue, maxIterationsEDA, aPrioriProb);
        } else {
            eda = new EdgeBasedEDA(problemInstance.getGraph(), selectedPopulationSize,
                    sampledPopulationSiue, maxIterationsEDA, bRatio, aPrioriEdges);
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
        TSPTour tour = multiStartILS.performMultiStartILS();
        System.out.println(tour);
        optTourFound = tour;
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
     * @param ls                     the local search method to use
     * @param lsMethod               Descent or Steepest descent
     * @param shouldStore            true, if the result should be saved
     * @throws IOException if the file cannot be stored
     */
    public void run(String filename, int numberLS, int numberStuck,
                    String eda, int sampledPopulationSize, int selectedPopulationSize, int maxIterationsEDA,
                    int aPrioriEdges, double bRatio, double aPrioriProb,
                    String ls, TwoOpt.Method lsMethod, boolean shouldStore) throws IOException {
        this.shouldStore = shouldStore;
        FileReader fileReader = new FileReaderImplementation();
        problemInstance = fileReader.createGraphFromFile(filename);

        multiStartILS = createMultiStartILS(eda, ls, problemInstance, numberLS, numberStuck,
                sampledPopulationSize, selectedPopulationSize, maxIterationsEDA, aPrioriEdges, bRatio, aPrioriProb,
                lsMethod);

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
                StorageComponent.store(problemInstance, optTourFound);
            }
        } catch (IOException ioException) {
            System.err.println("Could not store the tour. An exception occured: \n" + ioException.getMessage());
            ioException.printStackTrace();
        }
        GUI.GUI.programFinished();
    }
}
