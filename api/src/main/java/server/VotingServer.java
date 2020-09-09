package server;

import com.sun.istack.internal.NotNull;
import models.Vote;

import java.util.Collection;

public interface VotingServer {
    /**
     * Registra un voto.
     */
    void vote(@NotNull Vote vote);

    /**
     * Registra los votos e imprime en stdout cuantos votos se registraron.
     * @param votes colección de votos a registrar.
     */
    default void votes(@NotNull Collection<Vote> votes){
        for(Vote vote: votes){
            vote(vote);
        }

        System.out.println(votes.size() + " votes registered");
    }
}
