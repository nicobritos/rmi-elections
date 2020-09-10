package service;

import models.Party;

public interface FiscalService {
    /**
     * Suscribe a un fiscal del partido y número de mesa indicados.
     */
    void registerFiscal(int table, Party party);

    /**
     * Notifica a los fiscales del partido y número de mesa indicados (si los hubiere)
     * que hubo un voto para su partido.
     */
    void notifyVote(int table, Party party);
}
