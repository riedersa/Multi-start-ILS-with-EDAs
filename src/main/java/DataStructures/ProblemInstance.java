package DataStructures;

public class ProblemInstance {
    private String name;
    private String type;
    private Graph graph;
    //Number of nodes
    private int dimension;
    private double[][] nodeCoordinates;
    private boolean canDraw;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public Graph getGraph() {
        return graph;
    }


    public void setGraph(Graph graph) {
        this.graph = graph;
    }


    public int getDimension() {
        return dimension;
    }


    public void setDimension(int dimension) {
        this.dimension = dimension;
    }


    public double[][] getNodeCoordinates() {
        return nodeCoordinates;
    }


    public void setNodeCoordinates(double[][] nodeCoordinates) {
        this.nodeCoordinates = nodeCoordinates;
    }


    public boolean isCanDraw() {
        return canDraw;
    }


    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }
}
