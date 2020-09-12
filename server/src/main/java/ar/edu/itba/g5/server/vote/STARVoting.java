package ar.edu.itba.g5.server.vote;

import models.Party;
import models.vote.star.*;

import java.util.*;

public class STARVoting {
    private final Collection<Map<Party, Integer>> votes = new LinkedList<>();

    public synchronized void registerVote(final Map<Party, Integer> vote) {
        this.votes.add(vote);
    }

    // No hace falta sincronizar porque no puede llamarse mientras se registran votos
    public STARResults getResults() {
        FirstRound firstRound = this.firstRound();

        return new STARResults(firstRound, this.secondRound(firstRound));
    }

    // No hace falta sincronizar porque no puede llamarse mientras se registran votos
    private FirstRound firstRound() {
        Map<Party, Long> count = new HashMap<>();

        for (Map<Party, Integer> vote : this.votes) {
            for (Party party : vote.keySet()) {
                count.put(party, count.getOrDefault(party, 0L) + vote.get(party));
            }
        }

        List<FirstRoundResult> results = new LinkedList<>();
        for (Party party : count.keySet()) {
            results.add(new FirstRoundResult(party, count.get(party)));
        }

        return new FirstRound(results);
    }

    // No hace falta sincronizar porque no puede llamarse mientras se registran votos
    private SecondRound secondRound(FirstRound firstRound) {
        Party winnerA = firstRound.getTop2().get(0);
        Party winnerB = firstRound.getTop2().get(1);
        long totalVotes = 0;
        int totalA = 0;
        int totalB = 0;

        for (Map<Party, Integer> vote : this.votes) {
            int scoreForA = vote.getOrDefault(winnerA, 0);
            int scoreForB = vote.getOrDefault(winnerB, 0);
            if (scoreForA > scoreForB) {
                totalA++;
                totalVotes++;
            } else if (scoreForB > scoreForA) {
                totalB++;
                totalVotes++;
            }
        }

        double percentageA = (double) totalA / totalVotes;
        double percentageB = (double) totalB / totalVotes;

        return new SecondRound(new SecondRoundResult(winnerA, percentageA), new SecondRoundResult(winnerB, percentageB));
    }
}
