package ar.edu.itba.g5.server.vote.fptp;

import models.Party;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FPTPVoting {
    private final Map<Party, Long> votes = new HashMap<>();
    private final Lock lock = new ReentrantLock();
    private long totalVotes = 0;

    public void registerVote(Party party) {
        this.lock.lock();

        this.votes.put(party, this.votes.getOrDefault(party, 0L) + 1);
        this.totalVotes += 1;

        this.lock.unlock();
    }

    // Tengo que sincronizar porque puede llamarse mientras se registran votos
    public FPTPResults getResults() {
        FPTPResults results = new FPTPResults();

        // Bloqueo porque necesito un snapshot del mapa
        // y del counter
        this.lock.lock();

        Map<Party, Long> votes = new HashMap<>(this.votes);
        long totalVotes = this.totalVotes;

        // Puedo iterar tranquilamente porque el snapshot ya lo tengo
        this.lock.unlock();

        for (Party party : votes.keySet()) {
            results.addResult(party, votes.get(party).doubleValue() / totalVotes);
        }

        return results;
    }
}
