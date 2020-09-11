package ar.edu.itba.g5.server.voting.spav;

import models.Party;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SPAVResults implements Iterable<SPAVResult> {
    private final List<SPAVResult> resultList;
    private boolean finished = false;
    private final List<Party> winners;
    private SPAVResult currentWinner;

    public SPAVResults(List<Party> previousWinners) {
        this.resultList = new LinkedList<>();
        this.winners = previousWinners;
        currentWinner = null;
    }

    public void addResult(Party party, double approvalScore){
        if(finished){
            throw new IllegalStateException("Ya se finalizo esta votacion");
        }
        RoundResult result = new RoundResult(party, approvalScore);
        resultList.add(result);
        if(currentWinner == null || Double.compare(currentWinner.getApprovalScore(), result.getApprovalScore()) < 0) {
            currentWinner = result;
        }
    }

    public List<SPAVResult> getResultList() {
        resultList.sort(SPAVResult::compareTo);
        return resultList;
    }

    public List<Party> getWinners() {
        finished = true;
        if(currentWinner != null && !winners.contains(currentWinner.getParty())){
            winners.add(currentWinner.getParty());
        }
        return winners;
    }

    @Override
    public Iterator<SPAVResult> iterator() {
        return getResultList().iterator();
    }

    private static class RoundResult implements SPAVResult{
        private final Party party;
        private final double approvalScore;

        public RoundResult(Party party, double approvalScore) {
            this.party = party;
            this.approvalScore = approvalScore;
        }

        @Override
        public Party getParty() {
            return party;
        }

        @Override
        public double getApprovalScore() {
            return approvalScore;
        }
    }
}
