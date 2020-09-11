package ar.edu.itba.g5.server.services;

import ar.edu.itba.g5.server.services.utils.ElectionEvents;
import ar.edu.itba.g5.server.services.utils.ElectionStatusAware;
import ar.edu.itba.g5.server.services.utils.ElectionEventBus;
import ar.edu.itba.g5.server.vote.fptp.FPTPResult;
import ar.edu.itba.g5.server.vote.fptp.FPTPResults;
import ar.edu.itba.g5.server.vote.fptp.FPTPVoting;
import ar.edu.itba.g5.server.vote.spav.SPAVResult;
import ar.edu.itba.g5.server.vote.spav.SPAVResults;
import ar.edu.itba.g5.server.vote.spav.SPAVVoting;
import ar.edu.itba.g5.server.vote.star.FirstRoundResult;
import ar.edu.itba.g5.server.vote.star.STARResults;
import ar.edu.itba.g5.server.vote.star.STARVoting;
import ar.edu.itba.g5.server.vote.star.SecondRoundResult;
import exceptions.ElectionFinishedException;
import models.*;
import service.QueryService;
import service.VoteService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class VoteServiceImpl extends UnicastRemoteObject implements VoteService, QueryService {
    private final ElectionStatusAware electionStatusAware;

    private static final int SPAV_WINNER_QTY = 3;

    private final STARVoting nationalSTARVoting = new STARVoting();
    private final FPTPVoting nationalFPTPVoting = new FPTPVoting();

    private Map<Province, SPAVVoting> provinceSPAVVoting = new HashMap<>();
    private Map<Province, FPTPVoting> provinceFPTPVoting = new HashMap<>();

    private final Map<PollingStation, FPTPVoting> pollingStationFPTPVoting = new HashMap<>();
    private final Lock pollingStationLock = new ReentrantLock();

    public VoteServiceImpl(ElectionStatusAware electionStatusAware) throws RemoteException {
        this.electionStatusAware = electionStatusAware;

        for (Province province : Province.values()) {
            this.provinceSPAVVoting.put(province, new SPAVVoting());
            this.provinceFPTPVoting.put(province, new FPTPVoting());
        }

        this.provinceSPAVVoting = Collections.unmodifiableMap(this.provinceSPAVVoting);
        this.provinceFPTPVoting = Collections.unmodifiableMap(this.provinceFPTPVoting);
    }

    @Override
    public void vote(Vote vote) throws RemoteException {
        if (this.electionStatusAware.willClose()) throw new ElectionFinishedException();

        // El register permite saber que un thread llego, es como incrementar un contador
        // Permite que si justo se llama a cerrar comicios, se quede esperando hasta realmente cerrarlos
        this.electionStatusAware.getElectionPhaser().register();

        if (this.electionStatusAware.getElectionStatus() == ElectionStatus.UNINITIALIZED
                || this.electionStatusAware.getElectionStatus() == ElectionStatus.FINISHED)
        {

            // Marco que arriva y deregistro el thread actual para desbloquear ejecucion
            // en AdminService. Es como decrementar el contador
            this.electionStatusAware.getElectionPhaser().arriveAndDeregister();
            return;
        }

        this.registerVote(vote);

        // Marco que arriva y deregistro el thread actual para desbloquear ejecucion
        // en AdminService. Es como decrementar el contador
        this.electionStatusAware.getElectionPhaser().arriveAndDeregister();
    }

    @Override
    public String nationalResults() {
        if (this.isElectionOpen()) {
            return this.fptpResultsString(this.nationalFPTPVoting, false);
        } else {
            STARResults starResults = this.nationalSTARVoting.getResults();
            STARResults.FirstRound firstRound = starResults.getFirstRound();
            StringBuilder s = new StringBuilder();
            if (firstRound.getResults().isEmpty()) {
                s.append("No votes");
            } else {
                s.append("Score;Party");
            }
            for (FirstRoundResult result : firstRound) {
                s.append(String.format("\n%d;%s", result.getScore(), result.getParty()));
            }

            STARResults.SecondRound secondRoundResults = starResults.getSecondRound();

            s.append("\nPercentage;Party");
            for (SecondRoundResult result : secondRoundResults) {
                s.append(String.format("\n%.2f%%;%s", result.getPercentage(), result.getParty()));
            }
            s.append("\nWinner\n").append(starResults.getWinner());
            return s.toString();
        }
    }

    @Override
    public String provinceResults(Province province) {
        if (this.isElectionOpen()) {
            return this.fptpResultsString(this.provinceFPTPVoting.get(province), false);
        } else {
            List<Party> previousWinners = Collections.emptyList();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < SPAV_WINNER_QTY; i++) {
                SPAVResults roundResults = this.provinceSPAVVoting.get(province).nextRound(previousWinners);
                if (i != 0) {
                    s.append('\n');
                }
                s.append("Round ").append(i + 1);
                s.append("\nApproval;Party");
                for (SPAVResult approvals : roundResults) {
                    s.append(String.format("\n%.2f;%s", approvals.getApprovalScore(), approvals.getParty()));
                }
                s.append("\nWinners\n");
                for (Party winner : roundResults.getWinners()) {
                    s.append(winner.name()).append(",");
                }
                s.deleteCharAt(s.length() - 1); // borro la ultima coma
            }
            return s.toString();
        }
    }

    @Override
    public String pollingStationResults(PollingStation pollingStation) {
        return this.fptpResultsString(this.pollingStationFPTPVoting.getOrDefault(pollingStation, new FPTPVoting()), true);
    }

    private void registerVote(Vote vote) {
        this.nationalFPTPVoting.registerVote(vote.getFPTPVote());
        this.nationalSTARVoting.registerVote(vote.getSTARVotes());

        this.provinceSPAVVoting.get(vote.getProvince()).registerVote(vote.getSPAVVotes());
        this.provinceFPTPVoting.get(vote.getProvince()).registerVote(vote.getFPTPVote());

        // Bloqueo porque puedo llegar a reemplazar un valor en el mapa
        this.pollingStationLock.lock();
        if (!this.pollingStationFPTPVoting.containsKey(vote.getPollingStation())) {
            this.pollingStationFPTPVoting.put(vote.getPollingStation(), new FPTPVoting());
        }
        this.pollingStationLock.unlock();

        this.pollingStationFPTPVoting.get(vote.getPollingStation()).registerVote(vote.getFPTPVote());

        ElectionEventBus.eventBus.fire(ElectionEvents.getEvent(vote.getFPTPVote(), vote.getPollingStation()));
    }

    private boolean isElectionOpen() {
        return this.electionStatusAware.getElectionStatus() == ElectionStatus.OPEN;
    }

    private String fptpResultsString(FPTPVoting fptpVoting, boolean includeWinner) {
        FPTPResults results = fptpVoting.getResults();
        StringBuilder s = new StringBuilder();
        if (results.getSortedResultsList().isEmpty()) {
            s.append("No votes");
        } else {
            s.append("Percentage;Party");
            for (FPTPResult result : results) {
                s.append(String.format("\n%.2f%%;%s", result.getPercentage(), result.getParty()));
            }
            if (includeWinner) {
                s.append("\nWinner\n").append(results.getWinner());
            }
        }
        return s.toString();
    }
}
