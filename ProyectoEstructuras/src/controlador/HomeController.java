package controlador;

import controlador.excepciones.ArchivoNoTextoExcepcion;
import controlador.excepciones.ComponenteNuloExcepcion;
import controlador.excepciones.FormatoIncorrectoExcepcion;
import controlador.excepciones.SinCaminoExcepcion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import modelo.Dijkstra;
import modelo.GrafoPesado;

/**
 *
 * @author David Leal
 */
public class HomeController {

    /**
     * Boton para seleccion archivo.
     */
    @FXML
    private Button btnFile;
    /**
     * Etiqueta para mostrar la ruta del archivo.
     */
    @FXML
    private Label labelFile;
    /**
     * TextArea para imprimir los mensajes de consola.
     */
    @FXML
    private TextArea txtConsola;

    /**
     * Grafo del archivo.
     */
    static GrafoPesado gp;

    /**
     * Lista con nombres de los vertices del grafo.
     */
    List<String> nombresParadas;

    /**
     * Mapa con los numeros que representan cada vertice.
     */
    Map<String, Integer> valoresParadas;
    /**
     * Boton para calcular el algoritmo de Dijkstra.
     */
    @FXML
    private Button btnCacularDj;
    /**
     * Campo para mostrar el tiempo de la ruta.
     */
    @FXML
    private TextField tfTiempo;
    /**
     * Campo para mostrar el numero de rutas.
     */
    @FXML
    private TextField tfNumRutas;
    /**
     * TextArea para imprimir las instrucciones de que rutas y en que paradas
     * hacer los transbordos para la mejor ruta.
     */
    @FXML
    private TextArea txtTransbordos;
    /**
     * Campo para digitar el nombre de la parada origen.
     */
    @FXML
    private TextField tfOrigen;
    /**
     * Campo para digitar el nombre de la parada destino.
     */
    @FXML
    private TextField tfDestino;

    /**
     * Evento del boton para subir un archivo y crear el grafo en memoria.
     *
     * @param event click en el boton.
     */
    @FXML
    private void subirArchivo(ActionEvent event) {
        try {
            FileChooser d = new FileChooser();
            File file = d.showOpenDialog(null);
            System.out.println(file);
            this.labelFile.setText(file.toString());
            String s = file.toString();
            if (!(s.toCharArray()[s.length() - 1] == 't'
                    && s.toCharArray()[s.length() - 2] == 'x'
                    && s.toCharArray()[s.length() - 3] == 't')) {
                throw new ArchivoNoTextoExcepcion();
            }
            crearGrafo(file);
        } catch (ArchivoNoTextoExcepcion e) {
            System.out.println("Error " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error en el archivo");
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace(System.out);
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace(System.out);
        }

    }

    /**
     * Imprime los mensajes de consola en el TextArea correspondiente.
     *
     * @param s String a imprimir.
     */
    public void consolear(String s) {
        String s2 = this.txtConsola.getText();
        this.txtConsola.setText(s + "\n" + s2);
    }

    /**
     * Creacion del grafo con el archivo subido.
     *
     * @param file File archivo subido.
     * @throws FormatoIncorrectoExcepcion excepcion en caso de que el formato
     * del archivo no permite generar correctamente el grafo.
     */
    public void crearGrafo(File file) throws FormatoIncorrectoExcepcion {
        try {
            List<int[]> lista = new LinkedList<int[]>();
            nombresParadas = new ArrayList<String>();
            valoresParadas = new TreeMap<String, Integer>();
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            int n = Integer.parseInt(in.readLine());
            for (int j = 0; j < n; j++) {
                line = in.readLine();
                String[] palabras = line.split(" ");
                String p1, p2;
                int ruta = Integer.parseInt(palabras[0]);
                int peso;
                consolear("Creando ruta " + j + 1);
                System.out.println("Creando ruta " + j + 1);
                for (int i = 1; i < palabras.length - 2; i += 2) {
                    p1 = palabras[i];
                    peso = Integer.parseInt(palabras[i + 1]);
                    p2 = palabras[i + 2];
                    if (!valoresParadas.containsKey(p1)) {
                        valoresParadas.put(p1, valoresParadas.size() + 1);
                        nombresParadas.add(p1);
                    }
                    if (!valoresParadas.containsKey(p2)) {
                        valoresParadas.put(p2, valoresParadas.size() + 1);
                        nombresParadas.add(p2);
                    }
                    lista.add(new int[]{valoresParadas.get(p1), valoresParadas.get(p2), peso, ruta});
                }
            }
            consolear("Numero de nodos: " + valoresParadas.size());
            System.out.println("Numero de nodos: " + valoresParadas.size());
            gp = new GrafoPesado(valoresParadas.size());
            consolear("Creando Grafo... Espere");
            System.out.println("Creando Grafo... Espere");
            for (int[] e : lista) {
                gp.addEdge(e[0], e[1], e[2], e[3]);
            }
            consolear("Grafo Creado!");
            System.out.println("Grafo Creado");
            consolear("Llenando opciones de origen y destino...");
            consolear("Opciones Creadas!");
            this.tfOrigen.setEditable(true);
            this.tfDestino.setEditable(true);
        } catch (Exception e) {
            throw new FormatoIncorrectoExcepcion();
        }
    }

    /**
     * Inicializador de los componentes de la vista.
     */
    @FXML
    void initialize() {
        this.labelFile.setText("");
        this.tfOrigen.setEditable(false);
        this.tfDestino.setEditable(false);
    }

    /**
     * Evento del boton para calcular la ruta mas rapida entre dos paradas.
     * @param event click en el boton correspondiente.
     */
    @FXML
    private void calcularDj(ActionEvent event) {
        try {
            String origen = this.tfOrigen.getText();
            String destino = this.tfDestino.getText();
            if (origen.length() == 0) {
                throw new ComponenteNuloExcepcion("Campo Origen");
            }
            if (destino.length() == 0) {
                throw new ComponenteNuloExcepcion("Campo Destino");
            }
            origen = origen.toUpperCase();
            destino = destino.toUpperCase();
            if (!valoresParadas.containsKey(origen)) {
                throw new ComponenteNuloExcepcion("Campo Origen");
            }
            if (!valoresParadas.containsKey(destino)) {
                throw new ComponenteNuloExcepcion("Campo Destino");
            }
            //Hacer excepcion si no ingresa datos
            consolear("Creando Dijkstra...");
            System.out.println("Creando Dijkstra...");
            Dijkstra dj = new Dijkstra(gp, valoresParadas.get(origen));
            consolear("Dijkstra creado!");
            System.out.println("Dijkstra creado!");
            if (dj.getUltimaRuta()[valoresParadas.get(destino)] == -1) {
                throw new SinCaminoExcepcion(origen, destino);
            }
            List<int[]> transbordos = dj.transbordos(valoresParadas.get(destino));//valoresParadas.get(destino));
            Iterator<int[]> it = transbordos.iterator();
            int[] itArr = it.next();
            String s = "";
            consolear("Imprimiendo rutas...");
            int numRutas = 0;
            while (it.hasNext()) {
                s += "Desde " + nombresParadas.get(itArr[0] - 1);
                System.out.print("Desde " + nombresParadas.get(itArr[0] - 1));
                itArr = it.next();
                numRutas++;
                s += " tomas la ruta " + itArr[1] + " hasta " + nombresParadas.get(itArr[0] - 1) + "\n";
                System.out.println(" tomas la ruta " + (itArr[1] + 1) + " hasta " + nombresParadas.get(itArr[0] - 1));
            }
            this.tfTiempo.setText((dj.getDistance()[valoresParadas.get(destino)] / 60) + " minutos o " + (dj.getDistance()[valoresParadas.get(destino)]) + " segundos.");
            this.tfNumRutas.setText(numRutas + "");
            this.txtTransbordos.setText(s);
        } catch (ComponenteNuloExcepcion e) {
            System.out.println("Error " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error en el campo " + e.getMessage().split("\\s+")[1]);
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace(System.out);
        } catch (SinCaminoExcepcion e) {
            System.out.println("Error " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error buscando la ruta.");
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace(System.out);
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.show();
            e.printStackTrace(System.out);
        }
    }
}
