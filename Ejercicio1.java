\import java.util.Scanner;

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
            divisor += 6; // probamos solo los valores que tienen la posibilidad de ser primos. 
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
        } 
        else {
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
        return 0; // Plato no encontrado
    }

    public void eliminar(String nombre) {// no se usa la elimino?
        int indice = hashHorner(nombre);
        PlatoEntrada actual = tabla[indice];
        PlatoEntrada previo = null;
        boolean eliminado = false;

        while (actual != null && !eliminado) {
            if (actual.nombre.equals(nombre)) {
                if (previo == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    previo.siguiente = actual.siguiente;
                }
                tamanio--;
                actual.siguiente = null;// para Garbage Collector
                eliminado = true;
            } else {
                previo = actual;
                actual = actual.siguiente;
            }
        }
    }

    public MiListaEnlazada obtenerPlatos() {
        MiListaEnlazada listaPlatos = new MiListaEnlazada();

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

class MiListaEnlazada {
    private NodoPlatoEntrada primero;

    public NodoPlatoEntrada getPrimero() {
    return primero;
    }

    // para este caso es mas eficiente agregar al principio
    public void agregarAlPrincipio(PlatoEntrada plato) {
        NodoPlatoEntrada nuevoNodo = new NodoPlatoEntrada(plato);
        nuevoNodo.siguiente = primero;
        primero = nuevoNodo;
    }

    public MiListaEnlazada ordenar() {
    MiListaEnlazada listaOrdenada = new MiListaEnlazada();

    if (primero != null) {
        NodoPlatoEntrada nuevoPrimero = miMergeSort(primero);
        listaOrdenada.primero = nuevoPrimero;
    }

    return listaOrdenada;
}

private NodoPlatoEntrada miMergeSort(NodoPlatoEntrada nodo) {
    if (nodo == null || nodo.siguiente == null) {
        return nodo;
    }

    NodoPlatoEntrada medio = encontrarMedio(nodo);
    NodoPlatoEntrada mitadDerecha = medio.siguiente;
    medio.siguiente = null;

    NodoPlatoEntrada izquierda = miMergeSort(nodo);
    NodoPlatoEntrada derecha = miMergeSort(mitadDerecha);

    return miMerge(izquierda, derecha);
}

private NodoPlatoEntrada encontrarMedio(NodoPlatoEntrada nodo) {
    NodoPlatoEntrada rapido = nodo;
    NodoPlatoEntrada lento = nodo;
    while (rapido != null && rapido.siguiente != null && rapido.siguiente.siguiente != null) {
        rapido = rapido.siguiente.siguiente;
        lento = lento.siguiente;
    }
    return lento;
}

private NodoPlatoEntrada miMerge(NodoPlatoEntrada izquierda, NodoPlatoEntrada derecha) {
    if (izquierda == null) {
        return derecha;
    }
    if (derecha == null) {
        return izquierda;
    }

    if (izquierda.plato.compareTo(derecha.plato) <= 0) {
        izquierda.siguiente = miMerge(izquierda.siguiente, derecha);
        return izquierda;
    } else {
        derecha.siguiente = miMerge(izquierda, derecha.siguiente);
        return derecha;
    }
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

class PlatoEntrada implements Comparable<PlatoEntrada>{
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlatoEntrada otroPlato = (PlatoEntrada) o;
        if (nombre != null) {
            return nombre.equals(otroPlato.nombre);
        } else {
            return otroPlato.nombre == null;
        }
    }
    @Override
    public int compareTo(PlatoEntrada otroPlato) {
        int comparacionPorFrecuencia = Integer.compare(otroPlato.frecuencia, this.frecuencia);
        if (comparacionPorFrecuencia != 0) {
            return comparacionPorFrecuencia;
        }
        // Si las frecuencias son iguales, comparar alfabéticamente por nombre
        return this.nombre.compareTo(otroPlato.nombre);
    }
}
public class Ejercicio1 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int numeroN = Integer.parseInt(scanner.nextLine());

        MiHashTable tablaHash = new MiHashTable(numeroN*2);

        for (int i = 0; i < numeroN; i++) {
            String nombrePlato = scanner.nextLine();
            tablaHash.agregar(nombrePlato);
        }

        MiListaEnlazada listaOrdenada = tablaHash.obtenerPlatos().ordenar();
        listaOrdenada.imprimirNombresDesdeInicio();
        scanner.close();
    }
}
