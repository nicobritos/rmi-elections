package models.vote;

public class VoteResult<WhileOpen extends CountingSystem, WhenFinished extends CountingSystem> {
    private final WhileOpen whileOpen;
    private final WhenFinished whenFinished;

    VoteResult(WhileOpen whileOpen, WhenFinished whenFinished) {
        this.whileOpen = whileOpen;
        this.whenFinished = whenFinished;
    }

    public WhileOpen getWhileOpen() {
        return this.whileOpen;
    }

    public WhenFinished getWhenFinished() {
        return this.whenFinished;
    }
}
