package service;

import exceptions.ElectionFinishedException;
import exceptions.ElectionStartedException;
import models.Party;
import models.Table;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FiscalService extends Remote, Serializable {
    /**
     * Suscribe a un fiscal del partido y número de mesa indicado, llamando al
     * callback cada vez que un voto se realiza para su partido.
     * Solo se puede hacer si las elecciones no iniciaron.
     */
    void registerFiscal(Table table, Party party, int callback) throws RemoteException, ElectionStartedException, ElectionFinishedException;
}
