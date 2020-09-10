package ar.edu.itba.g5.server.voting;

import models.Party;
import utils.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.ToDoubleFunction;

public class FPTPVoting{
    private final Map<Party, Long> votes = new HashMap<>();
    private final AtomicLong totalVotes = new AtomicLong(0);

    public void registerVote(Party party){
        votes.put(party, votes.getOrDefault(party, 0L) + 1);
        totalVotes.incrementAndGet();
    }

    public List<Pair<Party, Double>> results(){
        List<Pair<Party, Double>> results = new LinkedList<>();
        for (Party party: votes.keySet()){
            results.add(new Pair<>(party, votes.get(party).doubleValue()/totalVotes.get()));
        }
        results.sort(Comparator
                .comparingDouble((ToDoubleFunction<Pair<Party, Double>>) Pair::getRight).reversed() // porcentajes mas altos primero
                .thenComparing(Pair::getLeft)); // en caso de empate, alfabeticamente
        return results;
    }
}
