package simulator;

import agents.Action;
import agents.Agent;
import datatypes.Edge;
import datatypes.Vertex;
import environment.EnvironmentState;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulator {

    boolean terminate = false;

    public void start() {
        terminate = false;
        int time = 0;
        prepareSimulationGraph();
        do {
            List<Agent> agentsToRemove = new ArrayList<>();
            for (Agent a : EnvironmentState.getInstance().getAgents()) {
                if (a.isFailed()) {
                    agentsToRemove.add(a);
                    continue;
                }
                Action action = a.processNextAction(null);
                a.updateState(action);
                System.out.print("Move to: ");
                System.out.println(action);
                if (a.getGoal().isGoalSucceeded()) {
                    terminate = true;
                    agentsToRemove.add(a);
                }
            }
            time++;
            EnvironmentState.getInstance().getAgents().removeAll(agentsToRemove);
            agentsToRemove.clear();
            if (time == EnvironmentState.getInstance().getWorldTimeout()) {
                System.out.println("Time is up: " + time);
                terminate = true;
            }
        }while (!needTermination());
        System.out.println("Done");
    }

    private boolean needTermination() {
        return terminate;
    }

    private void prepareSimulationGraph() {
        Graph<Vertex, Edge> g = EnvironmentState.getInstance().getGraph();
        Graph<Vertex, Edge> internal = new DefaultDirectedWeightedGraph<>(Edge.class);
        Graph<Vertex, Edge> travel = new DefaultDirectedWeightedGraph<>(Edge.class);
        List<Vertex> verticesInGraph = new LinkedList<>();
        Vertex init = EnvironmentState.getInstance().getInit();
        verticesInGraph.add(init);
        internal.addVertex(init);
        travel.addVertex(init);
        for (Object v : g.vertexSet()) {
            Vertex vv = (Vertex) v;
            if (vv.getNumberOfPeople() > 0 && !vv.getId().equals(init.getId())) {
                verticesInGraph.add(vv);
                internal.addVertex(vv);
                travel.addVertex(vv);
            }

        }

        for (Vertex v : verticesInGraph) {
            DijkstraShortestPath<Vertex, Edge> dijkstra = new DijkstraShortestPath<>(g);
            ShortestPathAlgorithm.SingleSourcePaths<Vertex, Edge> paths = dijkstra.getPaths(v);
            for (Vertex vv : verticesInGraph) {
                if (!v.equals(vv)) {
                    Edge e = new Edge();
                    internal.addEdge(v, vv, e);
                    internal.setEdgeWeight(e, paths.getPath(vv).getWeight() / (vv.getNumberOfPeople() + 1)); // For minimal spanning tree
                    travel.addEdge(v, vv, e);
                    travel.setEdgeWeight(e, paths.getPath(vv).getWeight());
                    e.setComment("Path weight: " + paths.getPath(vv).getWeight());
                }
            }
        }

        EnvironmentState.getInstance().setSimulationGraph(internal);
        EnvironmentState.getInstance().setSimulationTravelGraph(travel);
    }
}
