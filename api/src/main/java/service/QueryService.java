package service;

import exceptions.ElectionNotStartedException;
import models.Province;
import models.PollingStation;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QueryService extends Remote {
    /**
     * Devuelve los resultados (parciales si no terminaron) de las elecciones a nivel nacional.
     *
     * Si las elecciones no terminaron, devuelve solo los porcentajes de los votos FPTP.
     * Si terminaron, devuelve el puntaje (STAR) de cada partido y los porcentajes.
     * Al final tambien incluye el ganador.
     *
     * @return un String en formato csv. En caso de que sea FPTP devuelve por ejemplo:
     * Percentage;Party\n
     * 20.00%;LYNX\n
     * 80.00%;TURTLE
     *
     * En caso de que sea STAR devuelve por ejemplo:
     * Score;Party\n
     * 18;LYNX\n
     * 18;TURTLE\n
     * 12;LEOPARD\n
     * 4;JACKALOPE\n
     * Percentage;Party\n
     * 66.67%;LYNX\n
     * 33.33%;TURTLE\n
     * Winner\n
     * LYNX
     */
    String nationalResults() throws RemoteException, ElectionNotStartedException;

    /**
     * Devuelve los resultados (parciales si no terminaron) de las elecciones a nivel provincial.
     *
     * Si las elecciones no terminaron, imprime solo los porcentajes de los votos FPTP.
     * Si terminaron, imprime primero el indice de aprovación (SPAV) de cada ronda de cada partido
     * con los ganadores luego de cada ronda.
     *
     * @return un String en formato csv. En caso de que sea FPTP devuelve por ejemplo:
     * Percentage;Party\n
     * 20.00%;LYNX\n
     * 80.00%;TURTLE
     *
     * En caso de que sea SPAV devuelve por ejemplo:
     * Round 1\n
     * Approval;Party\n
     * 2.00;LEOPARD\n
     * 2.00;TURTLE\n
     * 1.00;LYNX\n
     * 1.00;TIGER\n
     * Winners\n
     * LEOPARD\n
     * Round 2\n
     * Approval;Party\n
     * 1.50;TURTLE\n
     * 1.00;LYNX\n
     * 0.50;TIGER\n
     * Winners\n
     * LEOPARD,TURTLE\n
     * Round 3\n
     * Approval;Party\n
     * 0.50;LYNX\n
     * 0.33;TIGER\n
     * Winners\n
     * LEOPARD,TURTLE,LYNX
     */
    String provinceResults(Province province) throws RemoteException, ElectionNotStartedException;

    /**
     * Devuelve los resultados de las elecciones en una mesa (polling station)
     * en formato de porcentaje FPTP.
     *
     * @return un String en formato csv. Por ejemplo:
     * Percentage;Party\n
     * 20.00%;LYNX\n
     * 80.00%;TURTLE\n
     * Winner\n
     * TURTLE
     */
    String pollingStationResults(PollingStation pollingStation) throws RemoteException, ElectionNotStartedException;
}
