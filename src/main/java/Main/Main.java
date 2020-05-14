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
 * This is the main class.
 * <p>
 * TODO: think about usage of illegalArgumentException. Maybe checked one would be better (Create One).
 */
public class Main {

    /**
     * This is the main function. It starts the program.
     *
     * @param args The first argument should be the path to the file. Afterwards one can write UMDA if one wants to use
     *             the UMDA instead of the edge based EDA, and OrOpt makes the algorithm use OrOpt instead of 2Opt
     * @throws IOException if the file cannot be read
     */
    public static void main(String[] args) throws IOException {
        String edaS = "PosBas";
        String lsS = "2Opt";

        FileReader fileReader = new FileReaderImplementation();
        ProblemInstance problemInstance = fileReader.createGraphFromFile(args[0]);

        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "UMDA":
                    edaS = args[i];
                    break;
                case "OrOpt":
                    lsS = args[i];
                    break;
            }
        }

        MultiStartILS multiStartILS = createMultiStartILS(edaS, lsS, problemInstance);
        TSPTour result = multiStartILS.performMultiStartILS(problemInstance.getGraph());
        System.out.println(result.toString());

    }


    /**
     * Creates the new MultiStartILS
     *
     * @param edaS            the eda represented as string
     * @param lsS             the local search algorithm represented as string
     * @param problemInstance the problem instance
     * @return the multiStartILS
     */
    private static MultiStartILS createMultiStartILS(String edaS, String lsS, ProblemInstance problemInstance) {
        EDA eda;
        switch (edaS) {
            case "UMDA":
                eda = new PositionBasedEDA_UMDA(problemInstance.getGraph(), 100, 150, 10);
                break;
            default:
                eda = new EdgeBasedEDA(problemInstance.getGraph(), 100, 150, 10);
        }

        LocalSearch ls;
        switch (lsS) {
            case "OrOpt":
                ls = new OrOpt();
                break;
            default:
                ls = new TwoOpt();
        }

        MultiStartILS multiStartILS = new MultiStartILS(eda, ls);
        return multiStartILS;
    }

}
