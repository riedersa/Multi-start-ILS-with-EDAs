package DataStructures;

import java.util.ArrayList;

public class CalculationInstance {

    public enum CalculationKind {
        INIT,
        EDA,
        LS
    }


    //This is the list of tours discovered during a run of the Multi-StartILS
    private ArrayList<TSPTour> tours;

    //This list describes the kind of the Tour, like Initiating, Local search or result of the EDA
    private ArrayList<CalculationKind> kinds;

    private TSPTour minimum;

    private String eda;
    private String ls;
    private String multiStartILS;


    public CalculationInstance(String eda, String ls, String multiStartILS) {
        tours = new ArrayList<>();
        kinds = new ArrayList<>();
        this.eda = eda;
        this.ls = ls;
        this.multiStartILS = multiStartILS;
    }


    public void addStep(TSPTour tour, CalculationKind kind) {
        tours.add(tour);
        kinds.add(kind);
    }


    public ArrayList<TSPTour> getTours() {
        return tours;
    }


    public ArrayList<CalculationKind> getKinds() {
        return kinds;
    }


    public TSPTour getMinimum() {
        return minimum;
    }


    /**
     * This sets the minimum for this instance. Be carefully, the ,ethod does not check the length.
     *
     * @param minimum the minium to set.
     */
    public void setMinimum(TSPTour minimum) {
        this.minimum = minimum;
    }


    public String getEda() {
        return eda;
    }


    public String getLs() {
        return ls;
    }


    public String getMultiStartILS() {
        return multiStartILS;
    }
}
