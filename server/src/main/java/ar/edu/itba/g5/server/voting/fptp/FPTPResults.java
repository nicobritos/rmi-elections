package ar.edu.itba.g5.server.voting.fptp;

import models.Party;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FPTPResults implements Iterable<FPTPResult>{
    private final List<FPTPResult> results = new LinkedList<>();

    public List<FPTPResult> getSortedResultsList() {
        results.sort(FPTPResult::compareTo);
        return new LinkedList<>(results);
    }

    public Party getWinner(){
        return getSortedResultsList().get(0).getParty();
    }

    void addResult(Party party, double percentage){
        results.add(new Entry(party, percentage));
    }

    @Override
    public Iterator<FPTPResult> iterator() {
        return getSortedResultsList().iterator();
    }

    private static class Entry implements FPTPResult {
        private final Party party;
        private final double percentage;

        public Entry(Party party, double percentage) {
            this.party = party;
            this.percentage = percentage;
        }

        @Override
        public Party getParty() {
            return party;
        }

        @Override
        public double getPercentage() {
            return percentage;
        }
    }
}
