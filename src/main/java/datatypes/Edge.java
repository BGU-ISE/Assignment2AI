package datatypes;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
    private Integer id;

    private String comment;
    private double originalWeight;
    private boolean blocked=false;
    private double probability;

    public Edge(){ super(); }

    public Edge(Integer id){ super(); this.id=id; }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked, Graph<Vertex, Edge> graph) {
        this.blocked = blocked;
        if (blocked) {
            this.originalWeight = getWeight();
            graph.setEdgeWeight(this, Double.POSITIVE_INFINITY);
        }
        else {
            graph.setEdgeWeight(this, this.originalWeight);
            this.originalWeight = -1;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public double getWeight() {
        return super.getWeight();
    }

    @Override
    public Vertex getSource() {
        return (Vertex) super.getSource();
    }

    @Override
    public Vertex getTarget() {
        return (Vertex) super.getTarget();
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getOriginalWeight() {
        return originalWeight;
    }

    public void setOriginalWeight(double originalWeight) {
        this.originalWeight = originalWeight;
    }

    @Override
    public String toString() {
        return "Edge {\n" +
                "\tid = " + getId() + ",\n" +
                "\tweight = " + getWeight() + ",\n" +
                "\tprobability = "+ probability + ",\n" +
                "\tvertex = " + super.toString() + "" +
                "\n}";
    }
}
