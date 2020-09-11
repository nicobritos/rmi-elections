package ar.edu.itba.g5.server.vote.star;

import models.Party;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class STARResults {
    private final FirstRound firstRound;
    private final SecondRound secondRound;

    public STARResults(FirstRound firstRound, SecondRound secondRound) {
        this.firstRound = firstRound;
        this.secondRound = secondRound;
    }

    public FirstRound getFirstRound() {
        return firstRound;
    }

    public SecondRound getSecondRound() {
        return secondRound;
    }

    public Party getWinner(){
        return secondRound.getWinner();
    }

    public static class FirstRound implements Iterable<FirstRoundResult> {
        private final List<FirstRoundResult> results;
        private boolean finished = false;

        public FirstRound() {
            this.results = new LinkedList<>();
        }

        public void addResult(Party party, long score){
            if(finished){
                throw new IllegalStateException("Ya finalizo la votacion");
            }
            results.add(new Entry(party, score));
        }

        public List<FirstRoundResult> getResults() {
            results.sort(FirstRoundResult::compareTo);
            return results;
        }

        public List<Party> getTop2(){
            finished = true;
            List<FirstRoundResult> orderedResults = getResults();
            List<Party> top2 = new LinkedList<>();
            top2.add(orderedResults.get(0).getParty());
            top2.add(orderedResults.get(1).getParty());
            return top2;
        }

        @Override
        public Iterator<FirstRoundResult> iterator() {
            return getResults().iterator();
        }




        private static class Entry implements FirstRoundResult{
            private final Party party;
            private final long score;

            public Entry(Party party, long score) {
                this.party = party;
                this.score = score;
            }

            @Override
            public Party getParty() {
                return party;
            }

            @Override
            public long getScore() {
                return score;
            }
        }
    }

    public static class SecondRound implements Iterable<SecondRoundResult> {

        private final List<SecondRoundResult> results = new LinkedList<>();

        public SecondRound(Party partyA, double percentageA, Party partyB, double percentageB) {
            results.add(new Entry(partyA, percentageA));
            results.add(new Entry(partyB, percentageB));
        }

        public List<SecondRoundResult> getResults() {
            results.sort(SecondRoundResult::compareTo);
            return results;
        }

        public Party getWinner(){
            return getResults().get(0).getParty();
        }

        @Override
        public Iterator<SecondRoundResult> iterator() {
            return getResults().iterator();
        }



        private static class Entry implements SecondRoundResult {
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
}
