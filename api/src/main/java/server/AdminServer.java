package server;

public interface AdminServer {
    /**
     * Abre los comicios. Imprime en stdout el estado de los comicios o el error correspondiente.
     * @return  true en caso de que se hayan abierto los comicios satisfactoriamente.
     *          false en caso que las elecciones ya hayan terminado.
     */
    boolean open();

    /**
     * Cierra los comicios. Imprime en stdout el estado de los comicios o el error correspondiente.
     * @return  true en caso de que se hayan cerrado los comicios satisfactoriamente.
     *          false en caso de que no se hayan abierto los comicios antes.
     */
    boolean close();

    /**
     * Consulta el estado de los comicios. Imprime en stdout el estado de los comicios en el momento.
     */
    void state();
}
