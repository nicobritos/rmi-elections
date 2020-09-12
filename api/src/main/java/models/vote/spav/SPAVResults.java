package models.vote.spav;

import models.vote.CountingSystem;

import java.io.Serializable;

public class SPAVResults implements Serializable, CountingSystem {
    private static final long serialVersionUID = 1L;

    private final SPAVRoundResult firstRound;
    private final SPAVRoundResult secondRound;
    private final SPAVRoundResult thirdRound;

    public SPAVResults(SPAVRoundResult firstRound, SPAVRoundResult secondRound, SPAVRoundResult thirdRound) {
        this.firstRound = firstRound;
        this.secondRound = secondRound;
        this.thirdRound = thirdRound;
    }

    public SPAVRoundResult getFirstRound() {
        return this.firstRound;
    }

    public SPAVRoundResult getSecondRound() {
        return this.secondRound;
    }

    public SPAVRoundResult getThirdRound() {
        return this.thirdRound;
    }
}
