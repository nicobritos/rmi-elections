package models.vote.spav;

import models.Party;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SPAVRoundResult implements Iterable<SPAVResult>, Serializable {
    private static final long serialVersionUID = 1L;

    private final List<SPAVResult> resultList;
    private boolean finished = false;
    private final List<Party> winners;
    private SPAVResult currentWinner;

    public SPAVRoundResult(List<Party> previousWinners) {
        this.resultList = new LinkedList<>();
        this.winners = previousWinners;
        this.currentWinner = null;
    }

    public void addResult(Party party, double approvalScore) {
        if (this.finished) {
            throw new IllegalStateException("Ya se finalizo esta votacion");
        }
        SPAVResult result = new SPAVResult(party, approvalScore);
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
}
