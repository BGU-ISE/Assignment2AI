package heuristics;

import agents.state.ZeroSumState;

public class ZeroSumCooperativeHeuristic implements ZSHeuristics {
    @Override
    public int evaluate(ZeroSumState state) {
        return state.getiSaved()+state.getEnemySaved();
    }
}
