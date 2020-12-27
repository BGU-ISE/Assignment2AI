package BeliefNetwork.data;

public class Query {
    private String operation;
    private Integer itemId;

    public Query(String operation, Integer  itemId) {
        this.operation = operation;
        this.itemId = itemId;
    }

    public Query(String operation) {
        this.operation = operation;
    }

    public Integer getItemId() {
        return itemId;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "Query {\n" +
                "\toperation = '" + operation + '\'' +
                ", \n\titemId = " + itemId +
                "\n}";
    }
}
