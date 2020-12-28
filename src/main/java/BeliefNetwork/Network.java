package BeliefNetwork;

import BeliefNetwork.data.Query;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private List<BeliefNode> nodes;

    public Network() {
        nodes = new ArrayList<>();
    }

    public Network(List<BeliefNode> nodes) {
        this.nodes = nodes;
    }

    public void ask(Query query) {
        switch (query.getOperation()) {
            case EdgeProb:
                for (BeliefNode n : nodes) {
                    if (n.getId().equals( query.getItemId()) && n instanceof BlockNode) {
                        System.out.println(n);
                        break;
                    }
                }
                break;
            case PathProb:
                System.out.println();
                System.out.println("TBD");
                System.out.println();
                break;
            case VertexProb:
                for (BeliefNode n : nodes) {
                    if (n.getId().equals(query.getItemId()) && n instanceof EvacuteesNode) {
                        System.out.println(n);
                        break;
                    }
                }
                break;
            case ResetEvidence:
                for (BeliefNode n : nodes) {
                    n.clear();
                }
                break;
            case AddEdgeEvidence:
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).getId().equals( query.getItemId()) && nodes.get(i) instanceof BlockNode) {
                        nodes.get(i).addEvidence(query.getValue());
                        break;
                    }
                }
                break;
            case AddNodeEvidence:
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i).getId().equals(query.getItemId()) && nodes.get(i) instanceof EvacuteesNode) {
                        nodes.get(i).addEvidence(query.getValue());
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
