package models.vote.star;

import models.Party;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FirstRound implements Iterable<FirstRoundResult> {
    private final List<FirstRoundResult> results;

    public FirstRound(List<FirstRoundResult> firstRoundResults) {
        this.results = new LinkedList<>(firstRoundResults);
    }

    public List<FirstRoundResult> getResults() {
        this.results.sort(FirstRoundResult::compareTo);
        return this.results;
    }

    public List<Party> getTop2() {
        List<FirstRoundResult> orderedResults = this.getResults();
        List<Party> top2 = new LinkedList<>();
        top2.add(orderedResults.get(0).getParty());
        top2.add(orderedResults.get(1).getParty());
        return top2;
    }

    @Override
    public Iterator<FirstRoundResult> iterator() {
        return this.getResults().iterator();
    }
}