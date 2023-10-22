import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

class MiMinHeap {
    private Par<Integer, Integer>[] heap;
    private int size;
    private Map<Integer, Integer> indicePorVertice;

    public MiMinHeap(int capacidadInicial) {
        this.heap = new Par[capacidadInicial];
        this.size = 0;
        this.indicePorVertice = new HashMap<>();
    }

    public void insertar(Par<Integer, Integer> par) {
        if (size == heap.length) {
            redimensionar();
        }

        heap[size] = par;
        indicePorVertice.put(par.getClave(), size);
        flotar(size);
        size++;
    }

    public Par<Integer, Integer> eliminar() {
        if (size == 0) return null;

        Par<Integer, Integer> min = heap[0];
        size--;
        heap[0] = heap[size];
        heap[size] = null;
        indicePorVertice.remove(min.getClave());
        hundir(0);

        return min;
    }

    public void disminuirClave(int vertice, int nuevaDistancia) {
        Integer idx = indicePorVertice.get(vertice);
        if (idx == null) return;

        //Par<Integer, Integer> par = heap[idx];
        //heap[idx].valor = nuevaDistancia;
        heap[idx].setValor(nuevaDistancia);
        flotar(idx);


    }

    public boolean estaVacio() {
        return size == 0;
    }

    private void redimensionar() {
        Par<Integer, Integer>[] nuevoArray = new Par[heap.length * 2];
        System.arraycopy(heap, 0, nuevoArray, 0, size);
        heap = nuevoArray;
    }
    private void flotar(int indice) {
        while (indice > 0) {
            int indicePadre = (indice - 1) / 2;
            if (heap[indice].getValor().compareTo(heap[indicePadre].getValor()) < 0) {
                intercambiar(indice, indicePadre);
                indice = indicePadre;
            } else {
                break;
            }
        }
    }
    private void hundir(int indice) {
        int indiceHijoIzquierdo = 2 * indice + 1;
        int indiceHijoDerecho = 2 * indice + 2;
        
        while (indiceHijoIzquierdo < size) {
            int menor = indiceHijoIzquierdo;
            if (indiceHijoDerecho < size && heap[indiceHijoDerecho].getValor().compareTo(heap[indiceHijoIzquierdo].getValor()) < 0) {
                menor = indiceHijoDerecho;
            }

            if (heap[indice].getValor().compareTo(heap[menor].getValor()) <= 0) {
                break;
            }

            intercambiar(indice, menor);
            indice = menor;
            indiceHijoIzquierdo = 2 * indice + 1;
            indiceHijoDerecho = 2 * indice + 2;
        }
    }
    private void intercambiar(int i, int j) {
        Par<Integer, Integer> temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        // Actualizamos el mapa de índices después del intercambio
        indicePorVertice.put(heap[i].getClave(), i);
        indicePorVertice.put(heap[j].getClave(), j);
    }

}

class Par<K, V> {
    private K clave;
    private V valor;

    public Par(K clave, V valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public K getClave() {
        return clave;
    }

    public V getValor() {
        return valor;
    }

    public void setValor(V valor) {
        this.valor = valor;
    }
}

class NodoAdyacente {
    int vertice;
    int peso;
    NodoAdyacente siguiente;

    public NodoAdyacente(int vertice, int peso, NodoAdyacente siguiente) {
        this.vertice = vertice;
        this.peso = peso;
        this.siguiente = siguiente;
    }
}

class Grafo {
    NodoAdyacente[] adyacencias;

    public Grafo(int numeroVertices) {
        adyacencias = new NodoAdyacente[numeroVertices];
    }

    public void agregarArista(int origen, int destino, int peso) {
        NodoAdyacente nuevoNodo = new NodoAdyacente(destino, peso, adyacencias[origen]);
        adyacencias[origen] = nuevoNodo;
    }

    public NodoAdyacente obtenerAdyacentes(int vertice) {
        return adyacencias[vertice];
    }
}

public class Ejercicio5 {

    public static int calcularCostoMinimo(Grafo grafo, int origen, int destino) {
        MiMinHeap distanciasPendientes = new MiMinHeap(grafo.adyacencias.length);
        distanciasPendientes.insertar(new Par<>(origen, 0));

        int[] distancias = new int[grafo.adyacencias.length];
        Arrays.fill(distancias, Integer.MAX_VALUE);
        distancias[origen] = 0;

        boolean[] visitado = new boolean[grafo.adyacencias.length];

        while (!distanciasPendientes.estaVacio()) {
            Par<Integer, Integer> actual = distanciasPendientes.eliminar();
            int verticeActual = actual.getClave();
        
            if (visitado[verticeActual]) {
                continue;
            }
            visitado[verticeActual] = true;
        
            System.out.println("Explorando nodo: " + verticeActual + ", distancia: " + distancias[verticeActual]);
        
            NodoAdyacente vecino = grafo.obtenerAdyacentes(verticeActual);

            while (vecino != null) {
                if (!visitado[vecino.vertice] && distancias[verticeActual] + vecino.peso < distancias[vecino.vertice]) {
                    System.out.println("VecinoVertice: " + vecino.vertice);
                    System.out.println("vecino.peso: " + vecino.peso);
                    System.out.println("VerticeActual: " + verticeActual);
                    System.out.println("distancias[vecino.vertice]: " + distancias[vecino.vertice]);
                    
                    distancias[vecino.vertice] = distancias[verticeActual] + vecino.peso;
                    distanciasPendientes.disminuirClave(vecino.vertice, distancias[vecino.vertice]);
                    System.out.println("Actualizado nodo " + vecino.vertice + " con distancia " + distancias[vecino.vertice]);
                    System.out.println("DistanciaMinim: " + distancias[vecino.vertice]);
                }
                vecino = vecino.siguiente;
            }
        }
    
        /*if (!visitado[destino]) {
            return -1;
        }*/
        return distancias[destino];
    }

    public static void main(String[] args) {


        /* 
        Scanner escaner = new Scanner(System.in);
        int planetas = escaner.nextInt(); // Número de nodos
        int portales = escaner.nextInt(); // Número de aristas

        System.out.println("planetas:" + planetas);
        System.out.println("portales:" + portales);
        Grafo grafo = new Grafo(planetas);

        for (int i = 0; i < portales; i++) {
            int planetaOrigen = escaner.nextInt(); 
            int planetaDestino = escaner.nextInt();
            int costo = escaner.nextInt();
            System.out.println("planetaOrigen " + planetaOrigen);
            System.out.println("planetaDestino " + planetaDestino);
            System.out.println("costo " + costo);
            grafo.agregarArista(planetaOrigen-1, planetaDestino-1, costo);  // -1 porque asumimos que la entrada es indice 1
            grafo.agregarArista(planetaDestino-1, planetaOrigen-1, costo);  // y convertimos a indice 0 para nuestro grafo.
        }

        int planetaOrigen = escaner.nextInt(); // Convertimos a indice 0
        int planetaDestino = escaner.nextInt(); // Convertimos a indice 0
        
        
        System.out.println("planetaOrigen:" + planetaOrigen);
        System.out.println("planetaDestino:" + planetaDestino);

        int costoMinimo = calcularCostoMinimo(grafo, planetaOrigen-1, planetaDestino-1);
        System.out.println(costoMinimo);
        escaner.close();

        */


        int planetas = 5; // Número de nodos
        int portales = 19; // Número de aristas

        System.out.println("planetas:" + planetas);
        System.out.println("portales:" + portales);
        Grafo grafo = new Grafo(planetas+1);
         
        grafo.agregarArista(1, 2, 16);
        grafo.agregarArista(2, 1, 16);
        grafo.agregarArista(1, 4, 93);
        grafo.agregarArista(4, 1, 93);
        grafo.agregarArista(5, 1, 63);
        grafo.agregarArista(1, 5, 63);
        grafo.agregarArista(2, 3, 27);
        grafo.agregarArista(3, 2, 27);
        grafo.agregarArista(2, 4, 12);
        grafo.agregarArista(4, 2, 12);
        grafo.agregarArista(2, 5, 30);
        grafo.agregarArista(5, 2, 30);
        grafo.agregarArista(3, 1, 63);
        grafo.agregarArista(1, 3, 63);
        grafo.agregarArista(3, 2, 36);
        grafo.agregarArista(2, 3, 36);
        grafo.agregarArista(3, 4, 59);
        grafo.agregarArista(4, 3, 59);
        grafo.agregarArista(3, 5, 94);
        grafo.agregarArista(5, 3, 94);
        grafo.agregarArista(4, 1, 43);
        grafo.agregarArista(1, 4, 43);
        grafo.agregarArista(4, 2, 22);
        grafo.agregarArista(2, 4, 22);
        grafo.agregarArista(4, 3, 38);
        grafo.agregarArista(3, 4, 38);
        grafo.agregarArista(4, 5, 71);
        grafo.agregarArista(5, 4, 71);
        grafo.agregarArista(5, 1, 92);
        grafo.agregarArista(1, 5, 92);
        grafo.agregarArista(5, 2, 74);
        grafo.agregarArista(2, 5, 74);
        grafo.agregarArista(5, 3, 97);
        grafo.agregarArista(3, 5, 97);
        grafo.agregarArista(5, 4, 26);
        grafo.agregarArista(4, 5, 26);
    



        int planetaOrigen = 5; //5; // Convertimos a indice 0
        int planetaDestino = 1;//1; // Convertimos a indice 0
        
        
        System.out.println("planetaOrigen:" + planetaOrigen);
        System.out.println("planetaDestino:" + planetaDestino);

        int costoMinimo = calcularCostoMinimo(grafo, planetaOrigen, planetaDestino);
        System.out.println(costoMinimo);
    //escaner.close();




    }






}
