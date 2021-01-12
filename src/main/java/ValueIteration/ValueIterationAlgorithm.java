package ValueIteration;

import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;

import java.util.List;
import java.util.Stack;

public class ValueIterationAlgorithm {


    public List<Node> valueIteration(List<Node> network, Graph<Vertex, Edge> g){
        boolean somethingChanged=false;

        do {
            somethingChanged=false;
            for (Node n:
                    network) {
                somethingChanged=somethingChanged||n.update(g);
            }
        }while(somethingChanged);

        return network;
    }



    public List<Node> generateBeliefNetwork(List<Vertex> blocked1, List<Vertex> blocked2, Graph<Vertex,Edge> g, List<BeliefAboutConnection> carry, int iterator ){
            if(blocked1.size()==iterator){
            return generateBeliefFromGraphAndCarry(g, carry);
        }
        Vertex v1=blocked1.get(iterator);
        Vertex v2=blocked2.get(iterator);
        carry.add(new BeliefAboutConnection(v1,v2,StateOfConnection.OPEN));
        List<Node> ret = generateBeliefNetwork(blocked1,blocked2,g,carry,iterator+1);
        carry.remove(new BeliefAboutConnection(v1,v2,StateOfConnection.OPEN));
        carry.add(new BeliefAboutConnection(v1,v2,StateOfConnection.CLOSED));
        ret.addAll(generateBeliefNetwork(blocked1,blocked2,g,carry,iterator+1));
        carry.remove(new BeliefAboutConnection(v1,v2,StateOfConnection.CLOSED));
        carry.add(new BeliefAboutConnection(v1,v2,StateOfConnection.UNKNOWN));
        ret.addAll(generateBeliefNetwork(blocked1,blocked2,g,carry,iterator+1));
        return ret;





    }
}
