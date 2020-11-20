package heuristics;

import agents.state.State;
import agents.state.ZeroSumState;
import datatypes.Edge;
import datatypes.Vertex;
import org.jgrapht.Graph;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.Map;
import java.util.Set;

public class ZeroSumHeuristic {
    public double evaluate(ZeroSumState state) {
        return state.getiSaved()-state.getEnemySaved();
    }
}
