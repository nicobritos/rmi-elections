package models.vote;

public abstract class VoteResultFactory {
    public static <WhileOpen extends CountingSystem, WhenFinished extends CountingSystem>
    VoteResult<WhileOpen, WhenFinished>
    withOpenElection(WhileOpen whileOpen)
    {
        return new VoteResult<>(whileOpen, null);
    }

    public static <WhileOpen extends CountingSystem, WhenFinished extends CountingSystem>
    VoteResult<WhileOpen, WhenFinished>
    withFinishedElection(WhenFinished whenFinished)
    {
        return new VoteResult<>(null, whenFinished);
    }
}
