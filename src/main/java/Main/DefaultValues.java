package Main;

/**
 * Here, default values for each parameter of EDA and LS are stored.
 */
public class DefaultValues {

    //EDAs
    public static int selectedPopulationSize = 50;
    public static int sampledPopulationSize = 100;
    public static int maxCounterOtIterations = 100;

    //edge based eda
    public static int valueForAPrioriEdges = 40;
    public static double bRatio = 0.03;

    //position based eda, UMDA, PBIL
    public static double probForPriorTour = 0.3;

    //position based eda, PBIL
    public static double alpha = 0.9;

    //Multi-start ILS
    public static int maxTimesLS = 15;
    public static int maxTimesStuck = 3;


}
