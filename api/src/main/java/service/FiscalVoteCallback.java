package service;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FiscalVoteCallback extends Remote, Serializable {
    /**
     * Metodo llamado por el SERVIDOR cada vez que se registra un voto
     */
    void voteMade() throws RemoteException;
}
