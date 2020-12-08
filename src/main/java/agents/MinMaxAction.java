package agents;

import datatypes.Vertex;

public class MinMaxAction extends Action{

    private Vertex toVertex;
    private boolean terminate=false;
    private int originalNumberOfPeople;
    private Integer edgeTime;

    public MinMaxAction(Vertex toVertex, Integer edgeTime) {
        this.toVertex = toVertex;
        this.originalNumberOfPeople = toVertex.getNumberOfPeople();
        this.edgeTime = edgeTime;
    }
    public MinMaxAction(Vertex toVertex, boolean terminate) {
        this.toVertex = toVertex;
        this.terminate=terminate;
        this.originalNumberOfPeople = toVertex.getNumberOfPeople();
    }


    public void GraphMovementAction(boolean terminate) {
        this.terminate = terminate;
    }

    public Vertex getToVertex() {
        return toVertex;
    }

    public void setToVertex(Vertex toVertex) {
        this.toVertex = toVertex;
    }

    public boolean isTerminate() {
        return terminate;
    }

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    public Integer getEdgeTime() {
        return edgeTime;
    }

    public void setEdgeTime(Integer edgeTime) {
        this.edgeTime = edgeTime;
    }

    @Override
    public String toString() {
        return "" + toVertex.getId();
    }
}
