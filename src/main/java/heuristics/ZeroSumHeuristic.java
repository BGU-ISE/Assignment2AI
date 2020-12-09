package heuristics;

import agents.state.ZeroSumState;

public class ZeroSumHeuristic {
    public int evaluate(ZeroSumState state) {
        return state.getiSaved()-state.getEnemySaved();
    }
}
