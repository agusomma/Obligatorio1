
import java.util.Scanner;

class MiHashTable {

    private static final int BASE = 31;
    private PlatoEntrada[] tabla;
    private int tamanio;

    public MiHashTable(int capacidadInicial) {
        int capacidad = obtenerPrimerPrimoDespuesDe(capacidadInicial);
        tabla = new PlatoEntrada[capacidad];
        tamanio = 0;
    }

    private static int obtenerPrimerPrimoDespuesDe(int numero) {
        while (true) {
            numero++;
            if (esPrimo(numero)) {
                return numero;
            }
        }
    }

    private static boolean esPrimo(int numero) {
        if (numero <= 1) {
            return false;
        }
        if (numero <= 3) {
            return true;
        }
        if (numero % 2 == 0 || numero % 3 == 0) {
            return false;
        }
        int divisor = 5;
        while (divisor * divisor <= numero) {
            if (numero % divisor == 0 || numero % (divisor + 2) == 0) {
                return false;
            }
            divisor += 6;
        }
        return true;
    }

    private int hashHorner(String nombre) {
        int hash = 0;
        for (int i = 0; i < nombre.length(); i++) {
            hash = (hash * BASE + nombre.charAt(i)) % tabla.length;
        }
        return hash;
    }

    public void agregar(String nombre) {
        int indice = hashHorner(nombre);

        if (tabla[indice] == null) {
            tabla[indice] = new PlatoEntrada(nombre);
            tamanio++;
        } else {
            PlatoEntrada actual = tabla[indice];
            while (actual != null) {
                if (actual.nombre.equals(nombre)) {
                    actual.incrementarFrecuencia();
                    return;
                }
                if (actual.siguiente == null) {
                    actual.siguiente = new PlatoEntrada(nombre);
                    tamanio++;
                    return;
                }
                actual = actual.siguiente;
            }
        }
    }

    public int obtenerFrecuencia(String nombre) {
        int indice = hashHorner(nombre);
        PlatoEntrada actual = tabla[indice];

        while (actual != null) {
            if (actual.nombre.equals(nombre)) {
                return actual.frecuencia;
            }
            actual = actual.siguiente;
        }
        return 0;
    }

    public MiListaDePlatos obtenerPlatos() {
        MiListaDePlatos listaPlatos = new MiListaDePlatos();

        for (int i = 0; i < tabla.length; i++) {
            PlatoEntrada actual = tabla[i];
            while (actual != null) {
                listaPlatos.agregarAlPrincipio(actual);
                actual = actual.siguiente;
            }
        }

        return listaPlatos;
    }
}

class minHeapOrdenaPlatoEntrada {
    private PlatoEntrada[] heap;
    private int capacidad;
    private int tamaño;

    public minHeapOrdenaPlatoEntrada(int capacidad) {
        this.capacidad = capacidad;
        this.tamaño = 0;
        this.heap = new PlatoEntrada[capacidad];
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public void agregar(PlatoEntrada plato) {
        if (tamaño == capacidad) {
            throw new IllegalStateException("Error: lleno");
        }

        tamaño++;
        int i = tamaño - 1;
        heap[i] = plato;

        while (i != 0 && heap[padre(i)].compareTo(heap[i]) > 0) {
            intercambiar(i, padre(i));
            i = padre(i);
        }
    }

    public PlatoEntrada extraerMinimo() {
        if (estaVacia()) {
            return null;
        }
        if (tamaño == 1) {
            tamaño--;
            return heap[0];
        }

        PlatoEntrada root = heap[0];
        heap[0] = heap[tamaño - 1];
        tamaño--;
        miHeapify(0);

        return root;
    }

    private void miHeapify(int i) {
        int izquierda = izq(i);
        int derecha = der(i);
        int menor = i;

        if (izquierda < tamaño && heap[izquierda].compareTo(heap[i]) < 0) {
            menor = izquierda;
        }
        if (derecha < tamaño && heap[derecha].compareTo(heap[menor]) < 0) {
            menor = derecha;
        }

        if (menor != i) {
            intercambiar(i, menor);
            miHeapify(menor);
        }
    }

    private int padre(int i) {
        return (i - 1) / 2;
    }

    private int izq(int i) {
        return 2 * i + 1;
    }

    private int der(int i) {
        return 2 * i + 2;
    }

    private void intercambiar(int i, int j) {
        PlatoEntrada temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}

class MiListaDePlatos {
    private NodoPlatoEntrada primero;

    public NodoPlatoEntrada getPrimero() {
        return primero;
    }

    public void agregarAlPrincipio(PlatoEntrada plato) {
        NodoPlatoEntrada nuevoNodo = new NodoPlatoEntrada(plato);
        nuevoNodo.siguiente = primero;
        primero = nuevoNodo;
    }

    public MiListaDePlatos ordenar(int capacidad) {
        MiListaDePlatos listaOrdenada = new MiListaDePlatos();

        if (primero != null) {
            minHeapOrdenaPlatoEntrada minHeap = new minHeapOrdenaPlatoEntrada(capacidad);

            NodoPlatoEntrada current = primero;
            while (current != null) {
                minHeap.agregar(current.plato);
                current = current.siguiente;
            }

            while (!minHeap.estaVacia()) {
                PlatoEntrada plato = minHeap.extraerMinimo();
                listaOrdenada.agregarAlPrincipio(plato);
            }
        }

        return listaOrdenada;
    }

    public void imprimirNombresDesdeInicio() {
        NodoPlatoEntrada actual = primero;
        while (actual != null) {
            System.out.println(actual.plato.nombre);
            actual = actual.siguiente;
        }
    }
}

class NodoPlatoEntrada {
    PlatoEntrada plato;
    NodoPlatoEntrada siguiente;

    NodoPlatoEntrada(PlatoEntrada plato) {
        this.plato = plato;
        this.siguiente = null;
    }
}

class PlatoEntrada implements Comparable<PlatoEntrada> {
    String nombre;
    int frecuencia;
    PlatoEntrada siguiente;

    PlatoEntrada(String nombre) {
        this.nombre = nombre;
        this.frecuencia = 1;
        this.siguiente = null;
    }

    void incrementarFrecuencia() {
        frecuencia++;
    }

    @Override
    public int compareTo(PlatoEntrada otroPlato) {
        if (this.frecuencia != otroPlato.frecuencia) {
            return Integer.compare(this.frecuencia,otroPlato.frecuencia); // Compara por frecuencia descendente
        } else {
            return otroPlato.nombre.compareTo(this.nombre); // Compara alfabéticamente por nombre
        }
    }

}

public class Ejercicio1 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int numeroN = Integer.parseInt(scanner.nextLine());

        MiHashTable tablaHash = new MiHashTable(numeroN * 2);

        for (int i = 0; i < numeroN; i++) {
            String nombrePlato = scanner.nextLine();
            tablaHash.agregar(nombrePlato);
        }

        MiListaDePlatos listaOrdenada = tablaHash.obtenerPlatos().ordenar(numeroN * 2);
        listaOrdenada.imprimirNombresDesdeInicio();
    }
}
