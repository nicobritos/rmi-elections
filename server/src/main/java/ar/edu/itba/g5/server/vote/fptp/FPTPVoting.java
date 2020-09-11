package ar.edu.itba.g5.server.vote.fptp;

import models.Party;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FPTPVoting{
    private final Map<Party, Long> votes = new HashMap<>();
    private final AtomicLong totalVotes = new AtomicLong(0);

    public void registerVote(Party party){
        votes.put(party, votes.getOrDefault(party, 0L) + 1);
        totalVotes.incrementAndGet();
    }

    public FPTPResults getResults(){
        FPTPResults results = new FPTPResults();
        for (Party party: votes.keySet()){
            results.addResult(party, votes.get(party).doubleValue()/totalVotes.get());
        }
        return results;
    }
}
