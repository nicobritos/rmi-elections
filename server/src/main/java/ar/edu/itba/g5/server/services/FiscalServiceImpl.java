package ar.edu.itba.g5.server.services;

import ar.edu.itba.g5.server.services.utils.ElectionEventBus;
import ar.edu.itba.g5.server.services.utils.ElectionEvents;
import ar.edu.itba.g5.server.services.utils.ElectionStatusAware;
import exceptions.ElectionFinishedException;
import exceptions.ElectionStartedException;
import models.ElectionStatus;
import models.Party;
import models.PollingStation;
import service.FiscalService;
import service.FiscalVoteCallback;
import utils.Event;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FiscalServiceImpl implements FiscalService {
    private final static int MAX_THREADS = 15;

    private final ElectionStatusAware electionStatusAware;
    // FIXME: No se si es lo mejor pero por ahora va
    private final ExecutorService executorService;

    public FiscalServiceImpl(ElectionStatusAware electionStatusAware) {
        this.electionStatusAware = electionStatusAware;
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    @Override
    public void registerFiscal(PollingStation pollingStation, Party party, FiscalVoteCallback callback) throws RemoteException, ElectionStartedException, ElectionFinishedException {
        if (this.electionStatusAware.getElectionStatus() == ElectionStatus.OPEN)
            throw new ElectionStartedException();
        if (this.electionStatusAware.getElectionStatus() == ElectionStatus.FINISHED)
            throw new ElectionFinishedException();

        ElectionEventBus.eventBus.register(ElectionEvents.getEventName(party, pollingStation), event -> this.fireEvent(event, callback));
    }

    private void fireEvent(Event event, FiscalVoteCallback callback) {
        this.executorService.execute(() -> {
            try {
                callback.voteMade();
            } catch (RemoteException ignored) {
            }
        });
    }
}
