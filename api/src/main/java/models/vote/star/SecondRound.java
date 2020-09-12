package models.vote.star;

import models.Party;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SecondRound implements Iterable<SecondRoundResult> {

    private final List<SecondRoundResult> results = new LinkedList<>();

    public SecondRound(SecondRoundResult resultA, SecondRoundResult resultB) {
        this.results.add(resultA);
        this.results.add(resultB);
    }

    public List<SecondRoundResult> getResults() {
        this.results.sort(SecondRoundResult::compareTo);
        return this.results;
    }

    public Party getWinner() {
        return this.getResults().get(0).getParty();
    }

    @Override
    public Iterator<SecondRoundResult> iterator() {
        return this.getResults().iterator();
    }
}