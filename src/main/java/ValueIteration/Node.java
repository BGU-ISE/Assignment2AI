package ValueIteration;

import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;

import java.util.LinkedList;
import java.util.List;

public class Node {
    Vertex location;
    BeliefAboutConnection[] beliefs;
    double value = 0;
    Node next = null;
    List<Node> neighbors = new LinkedList<>();
    boolean unknownNode;

    public Node(Vertex location, BeliefAboutConnection[] beliefs) {
        this.location = location;
        this.beliefs = beliefs;
        this.unknownNode = isAnUnknownNode();
    }


    public boolean update(Graph<Vertex, Edge> g) {

        if (!unknownNode) {
            Node n = getMaxNeighborNode();
            double weight = g.getEdgeWeight(g.getEdge(n.location, location));
            if (value >= -weight + n.value) {
                return false;
            }
            value = -weight + n.value;
            next = n;
            return true;
        } else {

            try {
                double newvalue = recUnknownNodeValue(new LinkedList<>(), 1.0, 0);

                if (value >= newvalue) {
                    return false;
                }
                value = newvalue;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;


        }

    }

    public Node getMaxNeighborNode() {
        double max = Double.NEGATIVE_INFINITY;
        Node ret = null;
        for (Node n :
                neighbors) {
            if (n.value > max) {
                max = value;
                ret = n;
            }
        }
        return ret;
    }


    public boolean isAnUnknownNode() {


        for (BeliefAboutConnection b :
                beliefs) {
            if (b.getState() == StateOfConnection.UNKNOWN && (location.equals(b.v1) || location.equals(b.v2))) {
                return true;
            }
        }
        return false;
    }


    private double recUnknownNodeValue(List<BeliefAboutConnection> carry, double probability, int iterator) throws Exception {
        if (iterator == beliefs.length) {
            for (Node n :
                    neighbors) {
                if (equalBeliefs(carry, n.beliefs)) {
                    return n.value * probability;
                }
            }
        }

        if (beliefs[iterator].getState() == StateOfConnection.UNKNOWN && (location.equals(beliefs[iterator].v1) || location.equals(beliefs[iterator].v2))) {
            Vertex v1 = beliefs[iterator].getV1();
            Vertex v2 = beliefs[iterator].getV2();
            double currProb = beliefs[iterator].probability;
            carry.add(new BeliefAboutConnection(v1, v2, StateOfConnection.OPEN, currProb));
            double value1 = recUnknownNodeValue(carry, probability * (1 - currProb), iterator + 1);
            carry.remove(carry.size() - 1);
            carry.add(new BeliefAboutConnection(v1, v2, StateOfConnection.CLOSED, currProb));
            return value1 + recUnknownNodeValue(carry, probability * (currProb), iterator + 1);
        } else {
            carry.add(beliefs[iterator]);
            return recUnknownNodeValue(carry, probability, iterator + 1);
        }
    }


    public Vertex getLocation() {
        return location;
    }

    public double getValue() {
        return value;
    }

    public BeliefAboutConnection[] getBeliefs() {
        return beliefs;
    }

    private boolean equalBeliefs(List<BeliefAboutConnection> lst, BeliefAboutConnection[] arr) throws Exception {
        if (lst.size() != arr.length) {
            throw new Exception("Beliefs not in the same length");
        }

        for (int i = 0; i < lst.size(); ++i) {
            if (lst.get(i).getState() != arr[i].getState()) {
                return false;
            }
        }
        return true;
    }

    public boolean canGoToVertex(Vertex v) {
        if (v.equals(location)) {
            return true;
        }

        boolean ret = true;
        for (int i = 0; i < beliefs.length; ++i) {
            if (beliefs[i].v1.equals(location) || beliefs[i].v2.equals(location)) {
                if (beliefs[i].state == StateOfConnection.CLOSED) {
                    ret = false;

                }
            }
        }
        return ret;

    }

}
