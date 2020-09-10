package ar.edu.itba.g5.server.voting;

import com.sun.istack.internal.NotNull;
import models.Party;
import utils.Pair;

import java.util.*;
import java.util.function.ToDoubleFunction;

public class SPAVVoting {
    private final Collection<Map<Party, Boolean>> votes = new LinkedList<>();

    public void registerVote(Map<Party, Boolean> vote){
        votes.add(vote);
    }

    public Pair<List<Pair<Party, Double>>,List<Party>> nextRound(@NotNull List<Party> previousWinners){
        Map<Party, Double> roundScores = new HashMap<>();

        for(Map<Party, Boolean> vote: votes){
            int n = 1;
            for(Party party: previousWinners){
                if(vote.getOrDefault(party, false)){
                    n++;
                }
            }

            for(Party party: vote.keySet()){
                if(!previousWinners.contains(party)){
                    roundScores.put(party, roundScores.getOrDefault(party, 0.0) + 1.0/n);
                }
            }
        }

        List<Pair<Party, Double>> results = new LinkedList<>();
        for(Party party: roundScores.keySet()){
            results.add(new Pair<>(party, roundScores.get(party)));
        }
        results.sort(Comparator
                .comparingDouble((ToDoubleFunction<Pair<Party, Double>>)Pair::getRight).reversed()
                .thenComparing(Pair::getLeft));

        List<Party> winners = new LinkedList<>(previousWinners);
        winners.add(results.get(0).getLeft());
        return new Pair<>(results, winners);
    }
}
