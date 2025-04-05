package controlador.excepciones;

/**
 * Excepcion en caso de que no exista una ruta desde el vertice origen hasta el
 * vertice destino.
 */
public class SinCaminoExcepcion extends Exception {

    /**
     * Constructor de la Excepcion.
     *
     * @param x String nombre de la parada origen.
     * @param y String nombre de la parada destino.
     */
    public SinCaminoExcepcion(String x, String y) {
        super("No existe una ruta desde "+x+" hasta "+y+".");
    }
}
