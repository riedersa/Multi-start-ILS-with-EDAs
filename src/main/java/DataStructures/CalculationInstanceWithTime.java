package DataStructures;

public class CalculationInstanceWithTime extends CalculationInstance {

    //Execution time in milli seconds
    private double executionTime;
    private boolean optFound;
    private int numberLS;


    public CalculationInstanceWithTime(String eda, String ls, String multiStartILS) {
        super(eda, ls, multiStartILS);
    }


    public double getExecutionTime() {
        return executionTime;
    }


    /**
     * This receives the execution time in Nano-seconds and sets it as milli seconds.
     *
     * @param executionTime time in nano seconds
     */
    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime / 1000000.0;
    }


    public boolean isOptFound() {
        return optFound;
    }


    public void setOptFound(boolean optFound) {
        this.optFound = optFound;
    }


    public int getNumberLS() {
        return numberLS;
    }


    public void setNumberLS(int numberLS) {
        this.numberLS = numberLS;
    }


    public String toString() {
        return "[" + super.toString() + "OptFound:" + optFound + "\nExecutionTime:" + executionTime +
                "\nNumberLS:" + numberLS + "\n]\n";
    }
}
