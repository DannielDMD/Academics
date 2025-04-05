package controlador.excepciones;

/**
 * Excepcion en caso de que el elemento seleccionado no exista.
 */
public class ComponenteNuloExcepcion extends Exception {

    /**
     * Constructor de la Excepcion.
     *
     * @param comp nombre del componente que causa el error.
     */
    public ComponenteNuloExcepcion(String comp) {
        super(comp + " no registrado.");
    }
}
