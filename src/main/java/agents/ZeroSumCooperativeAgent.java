package agents;

import agents.state.ZeroSumState;
import datatypes.Edge;
import datatypes.Vertex;
import environment.EnvironmentState;
import heuristics.ZSHeuristics;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;
import java.util.stream.Collectors;

public class ZeroSumCooperativeAgent extends Agent {

    private EnvironmentState state;
    private Graph<Vertex, Edge> internal;
    private List<Vertex> pathStates;
    private List<Edge> currentPath;
    private ZSHeuristics heuristic;
    private Agent enemy;
    private HashMap<ZeroSumState, ZeroSumState> sonToFather;

    public ZeroSumCooperativeAgent(EnvironmentState state, String name, ZSHeuristics heuristics) {
        this.state = state;
        this.name = name;
//        enemy=state.getAgents().get(0).equals(this) ? state.getAgents().get(1) :  state.getAgents().get(0);
        sonToFather = new HashMap<>();
        this.heuristic = heuristics;
    }


    private ZeroSumState AlphaBetaRec(ZeroSumState curr, Graph<Vertex, Edge> simulationGraph, Graph<Vertex, Edge> enemySimulationGraph, int alpha, int beta, boolean isMaxPlayer, int time) {
        int sum = 0;

        if (EnvironmentState.getInstance().getWorldTimeout() == time) {
            curr.setScore(heuristic.evaluate(curr));
            return curr;
        }
        if (isMaxPlayer) {

            Integer value = Integer.MIN_VALUE;
            if (curr.getMyTimeToReach() > 0) {
                ZeroSumState newstate;
                curr.getVertexToPeople().putIfAbsent(curr.getCurrentVertex().getId(), 0);
                if (curr.getMyTimeToReach() == 1) {
                    HashMap<Integer, Integer> newvertexToPeople = (HashMap<Integer, Integer>) curr.getVertexToPeople().entrySet()
                            .stream()
                            .collect(Collectors.toMap(integerIntegerEntry -> integerIntegerEntry.getKey(), integerIntegerEntry -> integerIntegerEntry.getValue()));
                    newvertexToPeople.put(curr.getCurrentVertex().getId(), 0);
                    newstate = new ZeroSumState(curr.getCurrentVertex(), curr.getEnemyCurrentVertex(), newvertexToPeople,
                            curr.getiSaved() + curr.getVertexToPeople().get(curr.getCurrentVertex().getId()), curr.getEnemySaved(), curr.getMyTimeToReach() - 1, curr.getEnemyTimeToReach(), null);
                } else {
                    newstate = new ZeroSumState(curr.getCurrentVertex(), curr.getEnemyCurrentVertex(), curr.getVertexToPeople(),
                            curr.getiSaved(), curr.getEnemySaved(), curr.getMyTimeToReach() - 1, curr.getEnemyTimeToReach(), null);
                }
                sonToFather.put(newstate, curr);
                return AlphaBetaRec(newstate,  simulationGraph,enemySimulationGraph, alpha, beta, !isMaxPlayer, time);
            } else {
                HashMap<Integer, Integer> newvertexToPeople = (HashMap<Integer, Integer>) curr.getVertexToPeople().entrySet()
                        .stream()
                        .collect(Collectors.toMap(integerIntegerEntry -> integerIntegerEntry.getKey(), integerIntegerEntry -> integerIntegerEntry.getValue()));
                newvertexToPeople.put(curr.getCurrentVertex().getId(), 0);
                Graph<Vertex, Edge> newgraph = EnvironmentState.cloneGraph(simulationGraph);
                if(!curr.getCurrentVertex().equals(curr.getEnemyCurrentVertex())) {
                    newgraph.removeVertex(curr.getCurrentVertex());
                }
                for (Integer i : newvertexToPeople.values()) {
                    sum += i;
                }
                if (sum == 0) {
                    curr.setScore(heuristic.evaluate(curr));
                    return curr;
                }

                ZeroSumState chosen = null;
                for (Vertex v :
                        simulationGraph.vertexSet()) {
                    if (!v.getId().equals(curr.getCurrentVertex().getId())) {
                        boolean shortest = false;
                        if (simulationGraph.getEdgeWeight(simulationGraph.getEdge(curr.getCurrentVertex(), v)) == 1) {
                            shortest = true;
                            newvertexToPeople.putIfAbsent(v.getId(), 0);
                        }

                        ZeroSumState newstate = new ZeroSumState(v,
                                curr.getEnemyCurrentVertex(),
                                newvertexToPeople,
                                curr.getiSaved() + (shortest ? newvertexToPeople.get(v.getId()) : 0),
                                curr.getEnemySaved(),
                                (int) simulationGraph.getEdgeWeight(simulationGraph.getEdge(curr.getCurrentVertex(), v)) - 1,
                                curr.getEnemyTimeToReach(), null);
                        sonToFather.put(newstate, curr);

//                            System.out.println(" player at: " + curr.getCurrentVertex().getId() + " to: " + v.getId());
                        ZeroSumState toAdd = AlphaBetaRec(newstate,  newgraph,enemySimulationGraph, alpha, beta, !isMaxPlayer, time);
                        if (curr.getCurrentVertex().getId() == 7 && name.equals("Player 1"))
                            System.out.println("player " + toAdd.getScore() + " at: " + curr.getCurrentVertex().getId() + " to: " + toAdd.getCurrentVertex().getId() + " " +
                                    "my saved: " + toAdd.getiSaved() + " enemy saved " + toAdd.getEnemySaved());
                        if (Math.max(value, toAdd.getScore()) != value) {
                            alpha = Math.max(toAdd.getScore(), alpha);
                            value = toAdd.getScore();
                            chosen = toAdd;
                        }
                    }

                }
//               System.out.println("chosen - player at: " + curr.getCurrentVertex().getId() + " choose: " + chosen.getCurrentVertex().getId() + " score: " + chosen.getScore());

                return chosen;


            }


        } else {
            Integer value = Integer.MIN_VALUE;
            if (curr.getEnemyTimeToReach() > 0) {
                ZeroSumState newstate;
                curr.getVertexToPeople().putIfAbsent(curr.getEnemyCurrentVertex().getId(), 0);
                if (curr.getEnemyTimeToReach() == 1) {
                    HashMap<Integer, Integer> newvertexToPeople = (HashMap<Integer, Integer>) curr.getVertexToPeople().entrySet()
                            .stream()
                            .collect(Collectors.toMap(integerIntegerEntry -> integerIntegerEntry.getKey(), integerIntegerEntry -> integerIntegerEntry.getValue()));
                    newvertexToPeople.put(curr.getEnemyCurrentVertex().getId(), 0);
                    newstate = new ZeroSumState(curr.getCurrentVertex(), curr.getEnemyCurrentVertex(), newvertexToPeople,
                            curr.getiSaved(), curr.getEnemySaved() + curr.getVertexToPeople().get(curr.getEnemyCurrentVertex().getId()), curr.getMyTimeToReach(), curr.getEnemyTimeToReach() - 1, null);
                } else {
                    newstate = new ZeroSumState(curr.getCurrentVertex(), curr.getEnemyCurrentVertex(), curr.getVertexToPeople(),
                            curr.getiSaved(), curr.getEnemySaved(), curr.getMyTimeToReach(), curr.getEnemyTimeToReach() - 1, null);
                }
                sonToFather.put(newstate, curr);
                return AlphaBetaRec(newstate,  simulationGraph,enemySimulationGraph, alpha, beta, !isMaxPlayer, time+1);
            } else {
                HashMap<Integer, Integer> newvertexToPeople = (HashMap<Integer, Integer>) curr.getVertexToPeople().entrySet()
                        .stream()
                        .collect(Collectors.toMap(integerIntegerEntry -> integerIntegerEntry.getKey(), integerIntegerEntry -> integerIntegerEntry.getValue()));
                newvertexToPeople.put(curr.getEnemyCurrentVertex().getId(), 0);
                Graph<Vertex, Edge> newgraph = EnvironmentState.cloneGraph(simulationGraph);
                if(!curr.getEnemyCurrentVertex().equals(curr.getCurrentVertex())) {
                    newgraph.removeVertex(curr.getEnemyCurrentVertex());
                }
                for (Integer i : newvertexToPeople.values()) {
                    sum += i;
                }
                if (sum == 0) {
                    curr.setScore(heuristic.evaluate(curr));
                    return curr;
                }
                ZeroSumState chosen = null;
                for (Vertex v :
                        simulationGraph.vertexSet()) {
                    if (!v.getId().equals(curr.getEnemyCurrentVertex().getId())) {
                        boolean shortest = false;
                        if (simulationGraph.getEdgeWeight(simulationGraph.getEdge(curr.getEnemyCurrentVertex(), v)) == 1) {
                            shortest = true;
                            newvertexToPeople.putIfAbsent(v.getId(), 0);
                        }
                        ZeroSumState newstate = new ZeroSumState(curr.getCurrentVertex(),
                                v,
                                newvertexToPeople,
                                curr.getiSaved(),
                                curr.getEnemySaved() + (shortest ? newvertexToPeople.get(v.getId()) : 0),
                                curr.getMyTimeToReach(),
                                (int) simulationGraph.getEdgeWeight(simulationGraph.getEdge(curr.getEnemyCurrentVertex(), v)) - 1, null);
                        sonToFather.put(newstate, curr);
//                        System.out.println(" enemy at: " + curr.getEnemyCurrentVertex().getId() + " to: " + v.getId());
                        ZeroSumState toAdd = AlphaBetaRec(newstate,  newgraph, enemySimulationGraph, alpha, beta, !isMaxPlayer, time+1);
//                        System.out.println("enemy: " + toAdd.getScore() + " at: " + curr.getEnemyCurrentVertex().getId() + " to: " + toAdd.getEnemyCurrentVertex().getId());
                        if (Math.max(value, toAdd.getScore()) != value) {
                            chosen = toAdd;
                            beta = Math.max(toAdd.getScore(), beta);
                            value = toAdd.getScore();
                        }
                    }

                }

//               System.out.println("chosen - enemy at: " + curr.getEnemyCurrentVertex().getId() + " choose: " + chosen.getEnemyCurrentVertex().getId() + " score: " + chosen.getScore());
                return chosen;
            }
        }

    }


    private void AlphaBeta() {

        sonToFather = new HashMap<>();

        Vertex myLocation = state.getAgentsLocation().get(this);
        Vertex enemyLocation = state.getAgentsLocation().get(enemy);
        Graph<Vertex, Edge> sim = state.getSimulationTravelGraphCopy();
        Graph<Vertex, Edge> simEnemy = state.getSimulationTravelGraphCopy();
        ZeroSumState father = new ZeroSumState(myLocation, enemyLocation, state.getPeopleAtVertex(), state.getAgentScore().get(this), state.getAgentScore().get(enemy), state.getTravelTime().get(this), state.getTravelTime().get(enemy), null);
        sonToFather.put(father, father);
        ZeroSumState finalstate = AlphaBetaRec(father, sim, simEnemy, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 0);

        Vertex lastVertex = finalstate.getCurrentVertex();
        ZeroSumState current = finalstate;
        List<Vertex> vertexList = new LinkedList<>();
        vertexList.add(finalstate.getCurrentVertex());
        while (!current.equals(sonToFather.get(current))) {
            ZeroSumState next = sonToFather.get(current);
            if (!lastVertex.equals(next.getCurrentVertex())) {
                vertexList.add(next.getCurrentVertex());
                lastVertex = next.getCurrentVertex();
            }
            current = next;
        }
        Collections.reverse(vertexList);
        vertexList.remove(0);
        pathStates = vertexList;

    }

    @Override
    public Action processNextAction(Perception perception) {
        if ((currentPath == null || currentPath.size() == 0) && (this.pathStates == null || this.pathStates.size() == 0)) {
            System.out.println(this.name + " searching for path");
            this.AlphaBeta();
        }
        if (currentPath == null || currentPath.size() == 0) {
            Graph<Vertex, Edge> g = EnvironmentState.getInstance().getGraph();
            DijkstraShortestPath<Vertex, Edge> dijkstra = new DijkstraShortestPath<>(g);
            Vertex next = this.pathStates.remove(0);
            GraphPath<Vertex, Edge> path = dijkstra.getPath(this.state.getAgentsLocation().get(this), next);
            currentPath = new ArrayList<>(path.getEdgeList());
        }
        int index = 0;
        for (int i = 0; i < currentPath.size(); i++) {
            if (currentPath.get(i).getSource().equals(this.state.getAgentsLocation().get(this))) {
                index = i;
                break;
            }
        }
        Edge e = currentPath.get(index);
        currentPath.remove(e);
        return new MinMaxAction(e.getTarget().equals(this.state.getAgentsLocation().get(this)) ? e.getSource() : e.getTarget(), (int) e.getWeight());
    }

    @Override
    public void updateState(Action action) {
        if (EnvironmentState.getInstance().getTravelTime().getOrDefault(this, 0) == 0) {
            MinMaxAction a = (MinMaxAction) action;
            EnvironmentState.getInstance().getAgentsLocation().put(this, a.getToVertex());
            EnvironmentState.getInstance().getTravelTime().put(this, a.getEdgeTime() - 1);
        } else {
            EnvironmentState.getInstance().getTravelTime().put(this, EnvironmentState.getInstance().getTravelTime().get(this) - 1);
        }

    }

    public void setEnemy(Agent enemy) {
        this.enemy = enemy;
    }

    @Override
    public String toString() {
        return name;
    }
}
