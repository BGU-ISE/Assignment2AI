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

    public void updateEvidence(List<BeliefNode> l1, List<Double> l2){
        for(int i=0;i<l1.size(); ++i){
            l1.get(i).addEvidence(l2.get(i));
        }
    }

    public double importanceSampling(List<BeliefNode> ancestors, List<BeliefNode> evidence, List<Double> evidenceValues, BeliefNode toCheck ){
        double weightsSumFalse=0;
        double weightSumTrue=0;
        for(int i=0;i < 1000; ++i){
            updateEvidence(evidence, evidenceValues);
            for (BeliefNode ancestor:
                 ancestors) {
                ancestor.startMonteCarlo();
            }
            double weight=1;
            for (BeliefNode e:
                evidence ) {
                weight=weight*e.probability();
                
            }
            if(toCheck.value()==1.0){
                weightSumTrue+=weight;
            }
            else{
                weightsSumFalse+=weight;
            }
            for (BeliefNode ancestor:
                    ancestors) {
                ancestor.clear();
            }

        }

        return weightSumTrue/(weightsSumFalse+weightSumTrue);
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
