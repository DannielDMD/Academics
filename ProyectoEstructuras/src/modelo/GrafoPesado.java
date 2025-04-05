package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

/**
 * Implementacion de Grafo Pesado mediante lista de adyacencia.
 *
 * @author Santiago
 */
public class GrafoPesado {

    /**
     * Numero de vertices.
     */
    private int n;
    /**
     * Lista de adyacencia.
     */
    private List<VerticePesado> adj[];
    /**
     * Lista para recuperar los nombres de los vertices representados con
     * numeros (indices).
     */
    List<String> nombresParadas;
    /**
     * Mapa para representar el nombre de cada vertice con un numero (indice).
     */
    Map<String, Integer> valoresParadas = new HashMap<String, Integer>();

    /**
     * Constructor del Grafo Pesado.
     *
     * @param n int numero de vertices.
     */
    public GrafoPesado(int n) {
        this.n = n;
        nombresParadas = new ArrayList<String>();
        valoresParadas = new HashMap<String, Integer>();
        adj = new List[n + 2]; //Se asigna el size como n+2 para evitar problemas con indices y porque los numeros de las paradas empiezan desde 1.
        for (int i = 1; i < n + 2; i++) {
            adj[i] = new LinkedList<VerticePesado>();
        }
    }

    /**
     * Agrega una arista.
     *
     * @param a int vertice origen.
     * @param b int vertice destino.
     * @param w int peso de la arista.
     * @param r int nombre de la ruta.
     */
    public void addEdge(int a, int b, int w, int r) {
        adj[a].add(new VerticePesado(b, w, r));
    }

    /**
     * Agrega una arista con vertices de tipo String.
     *
     * @param a String vertice origen.
     * @param b String vertice destino.
     * @param w int peso de la arista.
     * @param r int nombre de la ruta.
     */
    public void addEdge(String a, String b, int w, int r) {
        if (!valoresParadas.containsKey(a)) {
            valoresParadas.put(a, valoresParadas.size() + 1);
            nombresParadas.add(a);
        }
        if (!valoresParadas.containsKey(b)) {
            valoresParadas.put(b, valoresParadas.size() + 1);
            nombresParadas.add(b);
        }
        adj[valoresParadas.get(a)].add(new VerticePesado(valoresParadas.get(b), w, r));
    }

    /**
     * Imprime la lista de adyacencia del grafo.
     *
     * @return String lista de adyacencia.
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < n; i++) {
            s += adj[i].toString() + "\n";
        }
        return s;
    }

    /**
     * Retorna la lista de adyacencia del grafo pesado.
     *
     * @return List[] lista de adyacencia del grafo pesado.
     */
    public List<VerticePesado>[] getAdj() {
        return adj;
    }

    /**
     * Retorna el numero de vertices del grafo.
     *
     * @return int numero de vertices del grafo.
     */
    public int getN() {
        return n;
    }

    /**
     * Implementacion de un Vertice Pesado.
     */
    public static class VerticePesado {

        /**
         * Representa el vertice adyacente.
         */
        int v;
        /**
         * Representa el peso de la arista incidente.
         */
        int w;
        /**
         * Representa el nombre de la ruta de la arista incidente.
         */
        int r;

        /**
         * Constructor del Vertice Pesado.
         *
         * @param v int vertice adyacente.
         * @param w int peso de la arista incidente.
         * @param r int nombre de la ruta de la arista incidente.
         */
        VerticePesado(int v, int w, int r) {
            this.v = v;
            this.w = w;
            this.r = r;
        }

        /**
         * Metodo para imprimir los atributos de la instancia.
         *
         * @return String (v,w,r).
         */
        public String toString() {
            return "(" + v + ", " + w + ", " + r + ")";
        }
    }

}
