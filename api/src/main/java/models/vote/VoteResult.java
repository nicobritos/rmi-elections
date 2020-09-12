package models.vote;

public class VoteResult<WhileOpen extends VotingSystemResults, WhenFinished extends VotingSystemResults> {
    private final WhileOpen whileOpen;
    private final WhenFinished whenFinished;

    VoteResult(WhileOpen whileOpen, WhenFinished whenFinished) {
        this.whileOpen = whileOpen;
        this.whenFinished = whenFinished;
    }

    public WhileOpen getWhileOpenResults() {
        if (!isOpenElectionsResults()) throw new IllegalStateException();
        return this.whileOpen;
    }

    public WhenFinished getWhenFinishedResults() {
        if (isOpenElectionsResults()) throw new IllegalStateException();
        return this.whenFinished;
    }

    public boolean isOpenElectionsResults(){
        return whileOpen != null;
    }

}
