package agents.state;

import datatypes.Vertex;

import java.util.*;

public class ZeroSumState {



    protected Vertex currentVertex;
    protected Vertex enemyCurrentVertex;
    protected Map<Integer, Integer> vertexToPeople;
    protected int iSaved;
    protected int enemySaved;
    protected int myTimeToReach;
    protected int enemyTimeToReach;

    public ZeroSumState(Vertex currentVertex, Vertex enemyCurrentVertex, Map<Integer, Integer> vertexToPeople, int iSaved, int enemySaved, int myTimeToReach, int enemyTimeToReach) {
        this.currentVertex = currentVertex;
        this.enemyCurrentVertex = enemyCurrentVertex;
        this.vertexToPeople = vertexToPeople;
        this.iSaved = iSaved;
        this.enemySaved = enemySaved;
        this.myTimeToReach = myTimeToReach;
        this.enemyTimeToReach = enemyTimeToReach;
    }


    public Vertex getCurrentVertex() {
        return currentVertex;
    }

    public void setCurrentVertex(Vertex currentVertex) {
        this.currentVertex = currentVertex;
    }

    public Vertex getEnemyCurrentVertex() {
        return enemyCurrentVertex;
    }

    public void setEnemyCurrentVertex(Vertex enemyCurrentVertex) {
        this.enemyCurrentVertex = enemyCurrentVertex;
    }

    public Map<Integer, Integer> getVertexToPeople() {
        return vertexToPeople;
    }

    public void setVertexToPeople(Map<Integer, Integer> vertexToPeople) {
        this.vertexToPeople = vertexToPeople;
    }

    public int getiSaved() {
        return iSaved;
    }

    public void setiSaved(int iSaved) {
        this.iSaved = iSaved;
    }

    public int getEnemySaved() {
        return enemySaved;
    }

    public void setEnemySaved(int enemySaved) {
        this.enemySaved = enemySaved;
    }

    public int getMyTimeToReach() {
        return myTimeToReach;
    }

    public void setMyTimeToReach(int myTimeToReach) {
        this.myTimeToReach = myTimeToReach;
    }

    public int getEnemyTimeToReach() {
        return enemyTimeToReach;
    }

    public void setEnemyTimeToReach(int enemyTimeToReach) {
        this.enemyTimeToReach = enemyTimeToReach;
    }

    @Override
    public String toString() {
        return "ZeroSumState{" +
                "currentVertex=" + currentVertex +
                ", enemyCurrentVertex=" + enemyCurrentVertex +
                ", vertexToPeople=" + vertexToPeople +
                ", iSaved=" + iSaved +
                ", enemySaved=" + enemySaved +
                ", myTimeToReach=" + myTimeToReach +
                ", enemyTimeToReach=" + enemyTimeToReach +
                '}';
    }
}
