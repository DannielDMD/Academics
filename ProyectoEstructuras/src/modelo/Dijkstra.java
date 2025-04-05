package modelo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author Santiago
 */
public class Dijkstra {

    /**
     * Constante para representar el infinito.
     */
    private static final int INF = Integer.MAX_VALUE;
    /**
     * Grafo del cual tomar las rutas.
     */
    private GrafoPesado gp;
    /**
     * Cola prioritaria que contiene registros de la forma (d, v, nR), significa
     * que la distancia actual al vertice x es d con nR rutas.
     */
    private PriorityQueue<int[]> q;
    /**
     * Arreglo que contiene la distancia a cada vertice desde el vertice origen.
     */
    private int distance[];
    /**
     * Arreglo que indica si un vertice ya ha sido totalmente procesado.
     */
    private boolean processed[];
    /**
     * Arreglo que contiene la ultima ruta que hay que tomar para cada vertice.
     */
    private int ultimaRuta[];
    /**
     * Arreglo que contiene la ultima parada antes de cada vertice.
     */
    private int ultimaParada[];

    /**
     * Ejecucion del algoritmo sobre un grafo pesado gp desde el vertice x.
     *
     * @param gp GrafoPesado grafo a revisar.
     * @param x int vertice origen.
     */
    public Dijkstra(GrafoPesado gp, int x) {
        this.gp = gp;
        int n = gp.getN() + 1; //Numero de Vertices en el grafo +1 porque los nombres de los vertices empiezan desde 1.
        this.q = new PriorityQueue<int[]>(n, new ComparatorDj()); //Se crea la cola de prioridad de capacidad n y con un comparador especial para arreglo de entero de manera que la prioridad sean los indices 0 menores y los indices 2 menores.
        this.distance = new int[n]; //Se crea el arreglo de distancias
        this.processed = new boolean[n]; //Se crea el arreglo de procesados
        this.ultimaRuta = new int[n]; //Se crea el arreglo de rutas
        this.ultimaParada = new int[n]; //Se crea el arreglo de ultimas paradas
        Arrays.fill(this.ultimaRuta, -1); //Se llena las rutas de cada parada con -1.
        Arrays.fill(this.ultimaParada, -1); //Se llena la parada anterior a cada parada con -1.
        Arrays.fill(this.distance, INF); //Se llena las distancias con el valor de INF
        distance[x] = 0; //La distancia del vertice origen es 0.
        ultimaRuta[x] = 0; //La ultima ruta del vertice origen es 0.
        ultimaParada[x] = 0; //La parada anterior del vertice origen es 0.
        q.offer(new int[]{0, x, 0}); //El primer registro de la cola es el vertice origen. Tiene la forma [peso, vertice, numero de rutas]
        while (!q.isEmpty()) { //Mientras la cola tenga elementos
            int a = q.peek()[1]; //a es el vertice mas alto de la cola.
            int r_a = q.peek()[2]; //r_a es el numero de rutas del vertice mas alto de la cola.
            q.poll(); // se elimina el registro mas alto de la cola
            if (processed[a]) {
                continue; //si el vertice ya esta procesado, va al sigueinte elemento de la cola.
            }
            processed[a] = true; //se marca el vertice como procesado
            for (var u : gp.getAdj()[a]) { //Itera cada vertice vecino
                int b = u.v, w = u.w, r_b = u.r; //b es el vertice vecino, w es el peso de su arista, y r es la ruta del veertice vecino.
                int numRutas = r_b == ultimaRuta[a] ? r_a : r_a + 1; //Si la ruta del vecino es la misma de la ruta de la cola, entonces el numero de rutas del vecino es igual al numero de rutas de la cola. Si no, entonces es el numero de rutas de la cola + 1.
                if (distance[a] + w < distance[b]) { //Si el peso de la ruta de la cola mas el peso de la ruta vecina es menor a la ruta actual desde el origen hasta el vecino.
                    ultimaParada[b] = a; //La parada anterior al vertice vecino es el vertice de la cola.
                    distance[b] = distance[a] + w; //Se actualiza el valor de la distancia al vecino.
                    ultimaRuta[b] = r_b; //Se actualiza el nombre de la ruta anterior al vecino.
                    q.offer(new int[]{distance[b], b, numRutas}); //Se agrega el vecino a la cola.
                }
            }
        }
        System.out.println("Mejores rutas encontradas!"); //Luz verde del algoritmo.
    }

    /**
     * Enlista el orden de rutas a tomar y en que paradas transbordar para
     * llegar a la parada y.
     *
     * @param y int vertice (parada) a calcular transbordos.
     * @return List lista de la forma [parada, ruta] en orden desde la
     * parada p_i toma la ruta r_i hasta la parada p_i+1. Retorna null en caso
     * de que no exista una ruta.
     */
    public List<int[]> transbordos(int y) {
        if (ultimaRuta[y] == -1) { //Si no hay ruta hasta y.
            return null;
        }
        List<int[]> output = new LinkedList<int[]>(); //Lista de rutas.
        int[] tr = {y, ultimaRuta[y]}; //ultimo transbordo de la forma [y, ruta para llegar a y]
        output.add(new int[]{y, ultimaRuta[y]}); //agrega el ultimo transbordo a la lista de rutas
        while (ultimaRuta[y] != 0) { //mientras la ultima ruta no sea 0 (implica que y es la parada origen),
            y = ultimaParada[y]; //y ahora es la parada anterior.
            if (ultimaRuta[y] != tr[1] || ultimaRuta[y] == 0) { //Si la ultima ruta del nuevo y es igual a la ruta del antiguo y, o si la ultima ruta es 0 (y es la parada origen).
                tr[0] = y; //el primer transbordo debe ir al nuevo y
                tr[1] = ultimaRuta[y]; //la primer ruta debe ser la ultima ruta de y.
                output.add(0, new int[]{y, ultimaRuta[y]}); //agrega ese transbordo como el primero a realizar.
            }
        }
        return output; //retorna la lista de rutas.
    }

    /**
     * Imprime las indicaciones de que transbordos hacer para determinada parada
     * y.
     *
     * @param y int vertice destino.
     * @return String indicaciones de que transbordos hacer para determinada
     * parada y.
     */
    public String transbordosToString(int y) {
        String s = "";
        Iterator<int[]> it = this.transbordos(y).iterator();
        int[] itArr = it.next();
        while (it.hasNext()) {
            s += "Desde " + gp.nombresParadas.get(itArr[0] - 1);
            itArr = it.next();
            s += " tomas la ruta " + itArr[1] + " hasta " + gp.nombresParadas.get(itArr[0] - 1) + "\n";
        }
        return s;
    }

    /**
     * Imprime la distancia, ultima parada, y ultima ruta de cada parada
     * respecto a la parada origen.
     *
     * @return String informacion de cada parada respecto a la parada origen.
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < gp.getN(); i++) {
            s += i + " " + distance[i] + " " + ultimaParada[i] + " " + ultimaRuta[i] + "\n";
        }
        return s;
    }

    /**
     * Comparador para registros de cola para el algortimo Dijkstra. Los
     * registros para la cola de prioridad son de la forma [peso, vertice,
     * numero de rutas]. Un registro tiene mas prioridad si tiene menor peso; en
     * caso de que dos registros tengan el mismo peso, tiene mas prioridad el
     * que tenga menor numero de rutas.
     *
     */
    private class ComparatorDj implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            int[] t1 = (int[]) o1;
            int[] t2 = (int[]) o2;
            return t1[0] > t2[0] ? 1 : (t1[0] < t2[0] ? -1 : (t1[2] > t2[2] ? 1 : -1));
        }
    }

    /**
     * Getter del arreglo distance.
     *
     * @return int[] arreglo distance.
     */
    public int[] getDistance() {
        return distance;
    }

    /**
     * Getter del arreglo de la ruta que hay que tomar para llegar a cada
     * parada.
     *
     * @return int[] arreglo de la ruta que hay que tomar para llegar a cada
     * parada.
     */
    public int[] getUltimaRuta() {
        return ultimaRuta;
    }

}
