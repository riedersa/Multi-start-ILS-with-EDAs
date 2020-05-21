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
import TSPLib.FileReader;
import TSPLib.FileReaderImplementation;

import java.io.IOException;

/**
 * This class controls the communication between the GUI and the Core.
 */
public class Controller implements ThreadCompleteListener {
    private MultiStartILS multiStartILS;
    private ProblemInstance problemInstance;
    NotifyingThread thread = new NotifyingThread() {
        @Override
        public void doRun() {
            runMultiStartILS();
        }
    };


    /**
     * Creates the new MultiStartILS
     *
     * @param edaS            the eda represented as string
     * @param lsS             the local search algorithm represented as string
     * @param problemInstance the problem instance
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


    private void runMultiStartILS() {
        multiStartILS.setContinueRunning(true);
        TSPTour tour = multiStartILS.performMultiStartILS();
        System.out.println(tour);
    }


    public void run(String filename, int numberLS, int numberStuck,
                    String eda, int sampledPopulationSiue, int selectedPopulationSize, int maxIterationsEDA,
                    int aPrioriEdges, double bRatio, double aPrioriProb,
                    String ls, TwoOpt.Method lsMethod) throws IOException {
        FileReader fileReader = new FileReaderImplementation();
        problemInstance = fileReader.createGraphFromFile(filename);

        multiStartILS = createMultiStartILS(eda, ls, problemInstance, numberLS, numberStuck,
                sampledPopulationSiue, selectedPopulationSize, maxIterationsEDA, aPrioriEdges, bRatio, aPrioriProb,
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
        GUI.GUI.programFinished();
    }
}
