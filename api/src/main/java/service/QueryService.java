package service;

import models.Province;

public interface QueryService {
    /**
     * Imprime los resultados (parciales si no terminaron) de las elecciones a nivel nacional en un CSV.
     *
     * Si las elecciones no terminaron, imprime solo los porcentajes de los votos FPTP.
     * Si terminaron, imprime primero el puntaje (STAR) de cada partido y luego los porcentajes.
     * Al final tambien imprime el ganador.
     */
    void nationalPercentages(String csvPath);

    /**
     * Imprime los resultados (parciales si no terminaron) de las elecciones a nivel provincial en un CSV.
     *
     * Si las elecciones no terminaron, imprime solo los porcentajes de los votos FPTP.
     * Si terminaron, imprime primero el indice de aprovación (SPAV) de cada ronda de cada partido
     * con los ganadores luego de cada ronda.
     */
    void provincialPercentages(String csvPath, Province province);

    /**
     * Imprime los resultados de las elecciones en una mesa en formato de porcentaje FPTP en un CSV.
     */
    void tablePercentages(String csvPath, int table);
}
