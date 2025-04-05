package controlador.excepciones;

/**
 * Excepcion en caso de que el archivo seleccionado no sea de texto.
 */
public class ArchivoNoTextoExcepcion extends Exception{

    /**
     * Constructor de la Excepcion.
     *
     */
    public ArchivoNoTextoExcepcion() {
        super("El archivo no es de texto.");
    }

}
