package ar.edu.itba.g5.server.voting;

import models.Party;
import utils.Pair;

import java.util.*;
import java.util.function.ToLongFunction;

public class STARVoting {
    private final Collection<Map<Party, Integer>> votes = new LinkedList<>();

    public void registerVote(Map<Party, Integer> vote){
        votes.add(vote);
    }

    public List<Pair<Party, Long>> firstRound(){
        Map<Party, Long> count = new HashMap<>();
        for (Map<Party, Integer> vote: votes){
            for(Party party: vote.keySet()){
                count.put(party, count.getOrDefault(party, 0L) + vote.get(party));
            }
        }

        List<Pair<Party, Long>> result = new LinkedList<>();
        for(Party party: count.keySet()){
            result.add(new Pair<>(party, count.get(party)));
        }
        result.sort(Comparator
                .comparingLong((ToLongFunction<Pair<Party, Long>>) Pair::getRight).reversed() // Mayor cantidad de votos primero
                .thenComparing(Pair::getLeft)); // En caso de empate, ordeno alfabeticamente
        return result;
    }

    public List<Pair<Party, Double>> secondRound(Party winnerA, Party winnerB){
        long totalVotes = 0;
        List<Pair<Party, Long>> result = new ArrayList<>(2);
        result.add(new Pair<>(winnerA, 0L));
        result.add(new Pair<>(winnerB, 0L));

        for (Map<Party, Integer> vote: votes){
            int scoreForA = vote.getOrDefault(winnerA, 0);
            int scoreForB = vote.getOrDefault(winnerB, 0);
            if(scoreForA > scoreForB){
                result.get(0).setRight(result.get(0).getRight() + 1);
                totalVotes++;
            } else if(scoreForB > scoreForA){
                result.get(1).setRight(result.get(1).getRight() + 1);
                totalVotes++;
            }
        }
        result.sort(Comparator
                .comparingLong((ToLongFunction<Pair<Party, Long>>) Pair::getRight).reversed() // Mayor cantidad de votos primero
                .thenComparing(Pair::getLeft)); // En caso de empate, ordeno alfabeticamente

        List<Pair<Party, Double>> answer = new LinkedList<>();
        answer.add(new Pair<>(result.get(0).getLeft(), (double) result.get(0).getRight() / totalVotes));
        answer.add(new Pair<>(result.get(1).getLeft(), (double) result.get(1).getRight() / totalVotes));
        return answer;
    }

}
