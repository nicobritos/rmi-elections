package ar.edu.itba.g5.server.voting.spav;

import models.Party;

import java.util.*;

public class SPAVVoting {
    private final Collection<Map<Party, Boolean>> votes = new LinkedList<>();

    public void registerVote(Map<Party, Boolean> vote){
        votes.add(vote);
    }

    public SPAVResults nextRound(List<Party> previousWinners){
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

        SPAVResults spavResults = new SPAVResults(previousWinners);
        for(Party party: roundScores.keySet()){
            spavResults.addResult(party, roundScores.get(party));
        }
        return spavResults;
    }
}
