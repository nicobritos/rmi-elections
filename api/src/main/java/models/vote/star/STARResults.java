package models.vote.star;

import models.Party;
import models.vote.CountingSystem;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class STARResults implements Serializable, CountingSystem {
    private static final long serialVersionUID = 1L;

    private final FirstRound firstRound;
    private final SecondRound secondRound;

    public STARResults(FirstRound firstRound, SecondRound secondRound) {
        this.firstRound = firstRound;
        this.secondRound = secondRound;
    }

    public FirstRound getFirstRound() {
        return this.firstRound;
    }

    public SecondRound getSecondRound() {
        return this.secondRound;
    }

    public Party getWinner() {
        return this.secondRound.getWinner();
    }
}
