package BeliefNetwork.data;

import java.util.Set;

public class Query {
    private Operation operation;
    private Integer itemId;
    private Double value;

    private Set<Integer> edges;

    public Query(Operation operation, Integer  itemId) {
        this.operation = operation;
        this.itemId = itemId;
    }

    public Query(Operation operation, Set<Integer> edges) {
        this.operation = operation;
        this.edges = edges;
    }

    public Query(Operation operation, Integer  itemId, Double value) {
        this.operation = operation;
        this.itemId = itemId;
        this.value = value;
    }

    public Query(Operation operation) {
        this.operation = operation;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Operation getOperation() {
        return operation;
    }

    public Double getValue() {
        return value;
    }

    public Set<Integer> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Query {\n" +
                "\toperation = '" + operation + '\'' +
                ", \n\titemId = " + itemId +
                "\n}";
    }

    public enum Operation {
        ResetEvidence,
        AddNodeEvidence,
        AddEdgeEvidence,
        VertexProb,
        EdgeProb,
        PathProb
    }
}
