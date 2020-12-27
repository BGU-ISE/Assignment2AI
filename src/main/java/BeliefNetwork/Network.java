package BeliefNetwork;

import BeliefNetwork.data.Query;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private Integer time;
    private List<BeliefNode> nodes;

    public Network() {
        nodes = new ArrayList<>();
        time = 0;
    }

    public Network(List<BeliefNode> nodes) {
        this.nodes = nodes;
        this.time = 0;
    }

    public void ask(Query query) {
        System.out.println(nodes);
    }
}
