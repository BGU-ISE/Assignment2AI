package ValueIteration;

import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;

import java.util.LinkedList;
import java.util.List;

public class Node {
    Vertex location;
    BeliefAboutConnection[] beliefs;
    double value=0;
    Node next=null;
    List<Node> neighbors=new LinkedList<>();

    public Node(Vertex location, BeliefAboutConnection[] beliefs) {
        this.location = location;
        this.beliefs = beliefs;
    }



    public boolean update(Graph<Vertex, Edge> g){
        Node n=getMaxNeighborNode();
        double weight=g.getEdgeWeight(g.getEdge(n.location,location));
        if(value>=-weight+n.value){
            return false;
        }
        value=-weight+n.value;
        next=n;
        return true;

    }

    public Node getMaxNeighborNode(){
        double max=Double.NEGATIVE_INFINITY;
        Node ret=null;
        for (Node n:
             neighbors) {
            if(n.value>max){
                max=value;
                ret=n;
            }
        }
        return ret;
    }


    public Vertex getLocation() {
        return location;
    }
    public double getValue(){
        return value;
    }
    public BeliefAboutConnection[] getBeliefs() {
        return beliefs;
    }

    public boolean canGoToVertex(Vertex v){
        if(v.equals(location)){
            return true;
        }

        boolean ret=true;
        for(int i=0;i<beliefs.length;++i){
            if(beliefs[i].v1.equals(location)||beliefs[i].v2.equals(location)){
                if(beliefs[i].state==StateOfConnection.CLOSED){
                    ret=false;

                }
            }
        }
        return ret;

    }

}
