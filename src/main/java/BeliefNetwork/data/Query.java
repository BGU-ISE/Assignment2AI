package BeliefNetwork.data;

public class Query {
    private Operation operation;
    private Integer itemId;
    private Double value;

    private Integer fromId;
    private Integer toId;

    public Query(Operation operation, Integer  itemId) {
        this.operation = operation;
        this.itemId = itemId;
    }

    public Query(Operation operation, Integer  fromId, Integer toId) {
        this.operation = operation;
        this.fromId = fromId;
        this.toId = toId;
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

    public Integer getFromId() {
        return fromId;
    }

    public Integer getToId() {
        return toId;
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
