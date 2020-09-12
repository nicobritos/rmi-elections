package models.vote.spav;

import models.vote.VotingSystemResults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SPAVResults implements Serializable, VotingSystemResults {
    private static final long serialVersionUID = 1L;

    private static final int TOTAL_ROUNDS = 3;

    private final List<SPAVRoundResult> rounds = new ArrayList<>(TOTAL_ROUNDS);

    public SPAVResults(SPAVRoundResult firstRound, SPAVRoundResult secondRound, SPAVRoundResult thirdRound) {
        rounds.add(firstRound);
        rounds.add(secondRound);
        rounds.add(thirdRound);
    }

    public SPAVRoundResult getRound(int i){
        if(i >= 1 && i <= TOTAL_ROUNDS){
            return rounds.get(i-1);
        } else {
            throw new IllegalArgumentException("La votacion solo tiene " + TOTAL_ROUNDS + " rondas");
        }
    }
}
