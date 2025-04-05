package controlador.excepciones;

/**
 * Excepcion en caso de que el formato del archivo no sea el correcto.
 */
public class FormatoIncorrectoExcepcion extends Exception {

    /**
     * Constructor de la Excepcion.
     *
     */
    public FormatoIncorrectoExcepcion() {
        super("Formato del archivo no valido.");
    }
}
