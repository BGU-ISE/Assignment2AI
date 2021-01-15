package ValueIteration;

import datatypes.Vertex;

public class BeliefAboutConnection {
    Vertex v1;
    Vertex v2;
    StateOfConnection state;
    double probability;

    public Vertex getV1() {
        return v1;
    }


    public Vertex getV2() {
        return v2;
    }


    public StateOfConnection getState() {
        return state;
    }

    public void setState(StateOfConnection state) {
        this.state = state;
    }

    public BeliefAboutConnection(Vertex v1, Vertex v2, StateOfConnection state, double probability) {
        this.v1 = v1;
        this.v2 = v2;
        this.state = state;
        this.probability=probability;
    }
}
