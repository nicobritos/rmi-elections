package ar.edu.itba.g5.server.vote.spav;

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
        this.currentWinner = null;
    }

    public void addResult(Party party, double approvalScore) {
        if (this.finished) {
            throw new IllegalStateException("Ya se finalizo esta votacion");
        }
        RoundResult result = new RoundResult(party, approvalScore);
        this.resultList.add(result);
        if (this.currentWinner == null || Double.compare(this.currentWinner.getApprovalScore(), result.getApprovalScore()) < 0) {
            this.currentWinner = result;
        }
    }

    public List<SPAVResult> getResultList() {
        this.resultList.sort(SPAVResult::compareTo);
        return this.resultList;
    }

    public List<Party> getWinners() {
        this.finished = true;
        if (this.currentWinner != null && !this.winners.contains(this.currentWinner.getParty())) {
            this.winners.add(this.currentWinner.getParty());
        }
        return this.winners;
    }

    @Override
    public Iterator<SPAVResult> iterator() {
        return this.getResultList().iterator();
    }

    private static class RoundResult implements SPAVResult {
        private final Party party;
        private final double approvalScore;

        public RoundResult(Party party, double approvalScore) {
            this.party = party;
            this.approvalScore = approvalScore;
        }

        @Override
        public Party getParty() {
            return this.party;
        }

        @Override
        public double getApprovalScore() {
            return this.approvalScore;
        }
    }
}
