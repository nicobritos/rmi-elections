package service;

import exceptions.ElectionNotStartedException;
import models.Province;
import models.Table;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QueryService extends Remote, Serializable {
    /**
     * Devuelve los resultados (parciales si no terminaron) de las elecciones a nivel nacional.
     *
     * Si las elecciones no terminaron, devuelve solo los porcentajes de los votos FPTP.
     * Si terminaron, devuelve el puntaje (STAR) de cada partido y los porcentajes.
     * Al final tambien imprime el ganador.
     */
    void nationalResults() throws RemoteException, ElectionNotStartedException;

    /**
     * Devuelve los resultados (parciales si no terminaron) de las elecciones a nivel provincial.
     *
     * Si las elecciones no terminaron, imprime solo los porcentajes de los votos FPTP.
     * Si terminaron, imprime primero el indice de aprovación (SPAV) de cada ronda de cada partido
     * con los ganadores luego de cada ronda.
     */
    void provinceResults(Province province) throws RemoteException, ElectionNotStartedException;

    /**
     * Devuelve los resultados de las elecciones en una mesa (polling station)
     * en formato de porcentaje FPTP.
     */
    void pollingStationResults(Table table) throws RemoteException, ElectionNotStartedException;
}
