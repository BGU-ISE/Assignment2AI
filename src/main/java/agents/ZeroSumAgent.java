package agents;

import agents.state.ZeroSumState;
import datatypes.Edge;
import datatypes.Vertex;
import environment.EnvironmentState;
import heuristics.Heuristic;
import heuristics.MSTHeuristic;
import heuristics.ZeroSumHeuristic;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.*;

public class ZeroSumAgent {

    private static final int MAX_EXP = 10;
    private EnvironmentState state;
    private Graph<Vertex, Edge> internal;
    private List<Vertex> pathStates;
    private List<Edge> currentPath;
    private ZeroSumHeuristic heuristic;
    private Agent enemy;
    private HashMap<ZeroSumState,ZeroSumState> sonToFather;

    public ZeroSumAgent(EnvironmentState state) {
        this.state = state;
        enemy=state.getAgents().get(0).equals(this) ? state.getAgents().get(1) :  state.getAgents().get(0);
        sonToFather=new HashMap<>();
    }


    private ZeroSumState AlphaBetaRec(ZeroSumState curr, Graph<Vertex, Edge> simulationGraph, Graph<Vertex, Edge> enemySimulationGraph, int alpha, int beta, boolean isMaxPlayer) {


        if (isMaxPlayer) {
            if (heuristic.evaluate(curr) < alpha) {
                return null;
            }
            if (curr.getMyTimeToReach() > 0) {
                ZeroSumState newstate;
                if(curr.getMyTimeToReach()==1) {
                    newstate = new ZeroSumState(curr.getCurrentVertex(), curr.getEnemyCurrentVertex(), curr.getVertexToPeople(),
                            curr.getiSaved() + curr.getVertexToPeople().get(curr.getCurrentVertex()), curr.getEnemySaved(), curr.getMyTimeToReach() - 1, curr.getEnemyTimeToReach());
                }
                else{
                    newstate = new ZeroSumState(curr.getCurrentVertex(), curr.getEnemyCurrentVertex(), curr.getVertexToPeople(),
                            curr.getiSaved() , curr.getEnemySaved(), curr.getMyTimeToReach() - 1, curr.getEnemyTimeToReach());
                }
                return AlphaBetaRec(newstate, enemySimulationGraph, simulationGraph , heuristic.evaluate(curr), beta, !isMaxPlayer);
            } else {
                List<ZeroSumState> statelst=new LinkedList<>();
                HashMap<Integer,Integer> newvertexToPeople= new HashMap<>( curr.getVertexToPeople());
                newvertexToPeople.put(curr.getCurrentVertex().getId(),0);
                Graph<Vertex,Edge> newgraph=EnvironmentState.cloneGraph(simulationGraph);
                newgraph.removeVertex(curr.getCurrentVertex());

                for (Vertex v:
                     simulationGraph.vertexSet()) {
                    if(!v.equals(curr.getCurrentVertex())){

                        ZeroSumState newstate=new ZeroSumState(v,curr.getEnemyCurrentVertex(),newvertexToPeople,curr.getiSaved(),curr.getEnemySaved(),(int)simulationGraph.getEdgeWeight(simulationGraph.getEdge(curr.getCurrentVertex(),v))-1,curr.getEnemyTimeToReach());
                        sonToFather.put(newstate,curr);
                        ZeroSumState toAdd=AlphaBetaRec(newstate,enemySimulationGraph,newgraph,heuristic.evaluate(curr),beta,!isMaxPlayer);
                        if(toAdd!=null){
                            statelst.add(toAdd);
                        }
                    }
                }

                Collections.sort(statelst, new Comparator<ZeroSumState>() {
                    @Override
                    public int compare(ZeroSumState o1, ZeroSumState o2) {
                        if(heuristic.evaluate(o1)<heuristic.evaluate(o2)){
                            return -1;
                        }
                        return 1;
                    }
                });

                return statelst.get(statelst.size()-1);

            }


        } else {
            if (heuristic.evaluate(curr) > beta) {
                return null;
            }
            if (curr.getEnemyTimeToReach() > 0) {
                ZeroSumState newstate;
                if(curr.getEnemyTimeToReach()==1) {
                    newstate = new ZeroSumState(curr.getCurrentVertex(), curr.getEnemyCurrentVertex(), curr.getVertexToPeople(),
                            curr.getiSaved() , curr.getEnemySaved()+ curr.getVertexToPeople().get(curr.getEnemyCurrentVertex()), curr.getMyTimeToReach(), curr.getEnemyTimeToReach()-1);
                }
                else{
                    newstate = new ZeroSumState(curr.getCurrentVertex(), curr.getEnemyCurrentVertex(), curr.getVertexToPeople(),
                            curr.getiSaved() , curr.getEnemySaved(), curr.getMyTimeToReach() , curr.getEnemyTimeToReach()-1);
                }
                return AlphaBetaRec(newstate, enemySimulationGraph, simulationGraph, alpha, heuristic.evaluate(curr), !isMaxPlayer);
            } else {
                List<ZeroSumState> statelst=new LinkedList<>();
                HashMap<Integer,Integer> newvertexToPeople= new HashMap<>( curr.getVertexToPeople());
                newvertexToPeople.put(curr.getEnemyCurrentVertex().getId(),0);
                Graph<Vertex,Edge> newgraph=EnvironmentState.cloneGraph(simulationGraph);
                newgraph.removeVertex(curr.getEnemyCurrentVertex());

                for (Vertex v:
                        simulationGraph.vertexSet()) {
                    if(!v.equals(curr.getEnemyCurrentVertex())){

                        ZeroSumState newstate=new ZeroSumState(v,curr.getEnemyCurrentVertex(),newvertexToPeople,curr.getiSaved(),curr.getEnemySaved(),curr.getMyTimeToReach(),(int)simulationGraph.getEdgeWeight(simulationGraph.getEdge(curr.getEnemyCurrentVertex(),v))-1);
                        sonToFather.put(newstate,curr);
                        ZeroSumState toAdd=AlphaBetaRec(newstate,enemySimulationGraph, newgraph,alpha,heuristic.evaluate(curr),!isMaxPlayer);
                        if(toAdd!=null){
                            statelst.add(toAdd);
                        }
                    }
                }

                Collections.sort(statelst, new Comparator<ZeroSumState>() {
                    @Override
                    public int compare(ZeroSumState o1, ZeroSumState o2) {
                        if(heuristic.evaluate(o1)<heuristic.evaluate(o2)){
                            return 1;
                        }
                        return -1;
                    }
                });

                return statelst.get(statelst.size()-1);

            }
        }

    }


    private void AlphaBeta(){

        sonToFather=new HashMap<>();

        Vertex myLocation = state.getAgentsLocation().get(this);
        Vertex enemyLocation = state.getAgentsLocation().get( enemy  );
        Graph<Vertex, Edge> sim=state.getSimulationTravelGraphCopy();
        Graph<Vertex, Edge> simEnemy=state.getSimulationTravelGraphCopy();
        ZeroSumState father=new ZeroSumState(myLocation,enemyLocation,state.getPeopleAtVertex(), state.getAgentScore().get(this), state.getAgentScore().get(enemy),state.getTravelTime().get(this),state.getAgentScore().get(enemy)    );
        sonToFather.put(father,father);
        ZeroSumState finalstate = AlphaBetaRec(father, sim, simEnemy, Integer.MIN_VALUE,Integer.MAX_VALUE,true);

        Vertex lastVertex=finalstate.getCurrentVertex();
        ZeroSumState current=finalstate;
        List<Vertex> vertexList=new LinkedList<>();
        vertexList.add(finalstate.getCurrentVertex());
        while(!current.equals(sonToFather.get(current))){
            ZeroSumState next=sonToFather.get(current);
            if(!lastVertex.equals(next.getCurrentVertex())){
                vertexList.add(next.getCurrentVertex());
            }
            current=next;
        }
        Collections.reverse(vertexList);
        vertexList.remove(0);
        pathStates=vertexList;

    }







    public Action processNextAction(Perception perception) {
        if ((currentPath == null || currentPath.size() == 0) && (this.pathStates == null || this.pathStates.size() == 0)) {
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
        for (int i=0;i<currentPath.size();i++) {
            if (currentPath.get(i).getSource().equals(this.state.getAgentsLocation().get(this))) {
                index = i;
                break;
            }
        }
        Edge e = currentPath.get(index);
        currentPath.remove(e);
        return new MinMaxAction(e.getTarget().equals(this.state.getAgentsLocation().get(this)) ? e.getSource() : e.getTarget());
    }



}
