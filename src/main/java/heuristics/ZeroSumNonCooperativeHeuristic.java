package heuristics;

import agents.state.ZeroSumState;

public class ZeroSumNonCooperativeHeuristic implements ZSHeuristics {
    @Override
    public int evaluate(ZeroSumState state) {
        return state.getiSaved()-state.getEnemySaved();
    }
}
