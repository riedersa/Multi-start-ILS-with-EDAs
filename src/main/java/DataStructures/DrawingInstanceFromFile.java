package DataStructures;

/**
 * This class is used to store all necessary information for drawing a tour.
 */
public class DrawingInstanceFromFile {

    //the name of the problem
    private String name;
    //the tour to draw
    private TSPTour tspTour;
    //the coordinates of the nodes
    private double[][] nodeCoordinates;
    //the number of nodes
    private int numberNodes;
    //if the tour can be printed, this value is true. Some problems only have a distance matrix and cannot be printed
    private boolean printable;


    public DrawingInstanceFromFile(String name, TSPTour tspTour, double[][] nodeCoordinates, int numberNodes,
                                   boolean printable) {
        this.name = name;
        this.tspTour = tspTour;
        this.nodeCoordinates = nodeCoordinates;
        this.numberNodes = numberNodes;
        this.printable = printable;
    }


    public DrawingInstanceFromFile() {
    }


    public String getName() {
        return name;
    }


    public TSPTour getTspTour() {
        return tspTour;
    }


    public double[][] getNodeCoordinates() {
        return nodeCoordinates;
    }


    public int getNumberNodes() {
        return numberNodes;
    }


    public boolean isPrintable() {
        return printable;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setTspTour(TSPTour tspTour) {
        this.tspTour = tspTour;
    }


    public void setNodeCoordinates(double[][] nodeCoordinates) {
        this.nodeCoordinates = nodeCoordinates;
    }


    public void setNumberNodes(int numberNodes) {
        this.numberNodes = numberNodes;
    }


    public void setPrintable(boolean printable) {
        this.printable = printable;
    }
}
