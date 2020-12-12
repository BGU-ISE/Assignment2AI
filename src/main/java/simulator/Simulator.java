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

import java.util.LinkedList;
import java.util.List;

public class Simulator {

    boolean terminate = false;

    public void start() {
        terminate = false;
        int time = 0;
        prepareSimulationGraph();
        do {
            if (time == EnvironmentState.getInstance().getWorldTimeout()) {
                System.out.println("Time is up: " + time);
                terminate = true;
            }
            for (Agent a : EnvironmentState.getInstance().getAgents()) {
                if (isGoal()) {
                    System.out.println("We are done");
                    terminate = true;
                }
                if (EnvironmentState.getInstance().getTravelTime().getOrDefault(a, 0) == 0) {
                    Integer score = EnvironmentState.getInstance().getPeopleAtVertex().remove(EnvironmentState.getInstance().getAgentsLocation().get(a).getId());
                    EnvironmentState.getInstance().getAgentScore().put(a, EnvironmentState.getInstance().getAgentScore().get(a) + (score == null? 0: score));
                    if (isGoal()) {
                        System.out.println("We are done");
                        terminate = true;
                        break;
                    }
                    Action action = a.processNextAction(null);
                    a.updateState(action);
                    System.out.print(a + " Moving to: ");
                    System.out.println(action + " travel time " + (EnvironmentState.getInstance().getTravelTime().get(a) + 1));

                }
                else {
//                    System.out.println(a + " on the way to " + EnvironmentState.getInstance().getAgentsLocation().get(a).getId() + ", arrive in " + EnvironmentState.getInstance().getTravelTime().get(a));
                    a.updateState(null);
                }
            }
            time++;

        }while (!needTermination());
        for (Agent a : EnvironmentState.getInstance().getAgents()) {
            System.out.println("Score " + a + " " + EnvironmentState.getInstance().getAgentScore().get(a));
        }
        System.out.println("Done");
    }

    private boolean needTermination() {
        return terminate;
    }

    private boolean isGoal() {
        return EnvironmentState.getInstance().getPeopleAtVertex().size() == 0;
    }

    private void prepareSimulationGraph() {
        Graph<Vertex, Edge> g = EnvironmentState.getInstance().getGraph();
        Graph<Vertex, Edge> internal = new DefaultDirectedWeightedGraph<>(Edge.class);
        Graph<Vertex, Edge> travel = new DefaultDirectedWeightedGraph<>(Edge.class);
        List<Vertex> verticesInGraph = new LinkedList<>();
        for (Vertex init : EnvironmentState.getInstance().getAgentsLocation().values()) {
            verticesInGraph.add(init);
            internal.addVertex(init);
            travel.addVertex(init);
        }
        for (Object v : g.vertexSet()) {
            Vertex vv = (Vertex) v;
            if (vv.getNumberOfPeople() > 0 && !verticesInGraph.contains(vv)) {
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
