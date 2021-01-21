package ValueIteration;

import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;

public class ValueIterationAlgorithm {


    public List<Node> valueIteration(List<Node> network, Graph<Vertex, Edge> g, Vertex goal) {
        boolean somethingChanged = false;

        do {
            somethingChanged = false;

            for (Node n : network) {
                if (n.location.getId() != goal.getId()) {
                    somethingChanged = somethingChanged | n.update(g);
                }
                else if (n.value != 0) {
                    n.value = 0;
                    somethingChanged = true;
                }
            }
        } while (somethingChanged);

        return network;
    }

    /**
     *
     * @param blocked1 - vertex 1 of blocked edge
     * @param blocked2 - vertex 2 of blocked edge
     * @param probabilities
     * @param g
     * @param carry - belief state
     * @param iterator
     * @return
     */
    public List<Node> generateBeliefNetwork(List<Vertex> blocked1, List<Vertex> blocked2, List<Double> probabilities, Graph<Vertex, Edge> g, List<BeliefAboutConnection> carry, int iterator) {
        if (blocked1.size() == iterator) {
            return generateBeliefFromGraphAndCarry(g, carry);
        }
        Vertex v1 = blocked1.get(iterator);
        Vertex v2 = blocked2.get(iterator);
        double probability = probabilities.get(iterator);
        carry.add(new BeliefAboutConnection(v1, v2, StateOfConnection.OPEN, probability));
        List<Node> network = generateBeliefNetwork(blocked1, blocked2, probabilities, g, carry, iterator + 1);
        carry.remove(carry.size() - 1);
        carry.add(new BeliefAboutConnection(v1, v2, StateOfConnection.CLOSED, probability));
        network.addAll(generateBeliefNetwork(blocked1, blocked2, probabilities, g, carry, iterator + 1));
        carry.remove(carry.size() - 1);
        carry.add(new BeliefAboutConnection(v1, v2, StateOfConnection.UNKNOWN, probability));
        network.addAll(generateBeliefNetwork(blocked1, blocked2, probabilities, g, carry, iterator + 1));
        carry.remove(carry.size() - 1);
        return network;
    }

    private List<Node> generateBeliefFromGraphAndCarry(Graph<Vertex, Edge> g, List<BeliefAboutConnection> carry) {
        List<Node> netPart = new ArrayList<>();
        for (Vertex v : g.vertexSet()) {
            netPart.addAll(createNodeFromCarry(g, v, carry));
        }
        return netPart;
    }


    public void updateNeighbors(Graph<Vertex, Edge> g, List<Node> network, List<Vertex> blocked1, List<Vertex> blocked2, List<Double> probabilities) {
        for (Node n : network) {
            List<Node> neighbords = new ArrayList<>();
            for (Vertex v : g.vertexSet()) {
                if (v.getId() != n.location.getId() && !isClosed(n, v)) {
                    Edge e = g.getEdge(n.location, v);
                    if (e != null) {
                        for (Node nn : network) {
                            if (nn.location.getId() == v.getId()) {
                                neighbords.add(nn);
                                break;
                            }
                        }
                    }
                }
            }
            n.neighbors.addAll(neighbords);
            for (int i = 0; i < blocked1.size(); i++) {
                Vertex v1 = blocked1.get(i);
                Vertex v2 = blocked2.get(i);
                for (BeliefAboutConnection bac : n.beliefs) {
                    if (bac.state == StateOfConnection.UNKNOWN &&
                            bac.v1.getId() == v1.getId() && bac.v2.getId() == v2.getId() &&
                            (n.location.getId() == v1.getId() || n.location.getId() == v2.getId())) {
                        for (Node nn : network) {
                            if (nn.location.getId() == n.location.getId()) {
                                for (BeliefAboutConnection bac2 : nn.beliefs) {
                                    if (bac.v1.getId() == bac2.v1.getId() &&
                                            bac.v2.getId() == bac2.v2.getId() && bac2.state != StateOfConnection.UNKNOWN) {
                                        n.neighbors.add(nn);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isClosed(Node n, Vertex v2) {
        for (BeliefAboutConnection bac : n.beliefs) {
            if ((bac.v1.getId() == n.location.getId() && bac.v2.getId() == v2.getId()) ||
                    (bac.v2.getId() == n.location.getId() && bac.v1.getId() == v2.getId())) {
                return bac.state == StateOfConnection.CLOSED;
            }
        }
        return false;
    }

    private List<Node> createNodeFromCarry(Graph<Vertex, Edge> g, Vertex vertex, List<BeliefAboutConnection> carry) {
        List<Node> nodes = new ArrayList<>();
        BeliefAboutConnection []bs = new BeliefAboutConnection[carry.size()];
        nodes.add(new Node(vertex, carry.toArray(bs)));
        return nodes;
    }

}
