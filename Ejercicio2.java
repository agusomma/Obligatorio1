import java.util.Scanner;

class Pelicula {
    int id;
    String genero;
    int sumCalificaciones = 0;
    int numCalificaciones = 0;

    public Pelicula(int unId, String unGenero) {
        this.id = unId;
        this.genero = unGenero;
    }

    public void agregarCalificacion(int calificacion) {
        sumCalificaciones += calificacion;
        numCalificaciones++;
    }

    public double getPromedio() {
        double promedio;
        if(numCalificaciones == 0) {
            promedio =0;
        }
        else {
            promedio = (double)sumCalificaciones / numCalificaciones;        }
        return promedio;
    }
}
class Genero {
    String nombre;
    int mejorPeliculaID = -1;
    double mejorPromedio = -1;

    public Genero(String nombre) {
        this.nombre = nombre;
    }

    public void chequearMejorPelicula(Pelicula pelicula) {
        if (mejorPeliculaID == -1 || pelicula.getPromedio() > mejorPromedio || 
            (pelicula.getPromedio() == mejorPromedio && pelicula.id < mejorPeliculaID)) {
            mejorPromedio = pelicula.getPromedio();
            mejorPeliculaID = pelicula.id;
        }
    }
}

class NodoPelicula {
    Pelicula pelicula;
    NodoPelicula siguiente;

    public NodoPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }
}
class MiHashTable {

    private static final int CAPACIDAD_INICIAL = 103;
    private static final int BASE = 31;
    private NodoPelicula[] tabla;
    private int tamaño;
    private int umbralRehash;
    private boolean isRehashing = false;

    public MiHashTable() {
        int capacidad = obtenerPrimerPrimoDespuesDe(CAPACIDAD_INICIAL);
        tabla = new NodoPelicula[capacidad];
        tamaño = 0;
        this.umbralRehash = (int) (capacidad * 0.75);
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
            divisor = divisor + 6; // probamos solo los valores que tienen la posibilidad de ser primos. 
        }
        return true;
    }

    private int hashHorner(int unId) {
        return (BASE + unId) % tabla.length;
    }

    public void agregar(int unId, String unGenero) {
        int indice = hashHorner(unId);
        NodoPelicula nuevoNodo = new NodoPelicula(new Pelicula(unId, unGenero));
        if (tabla[indice] == null) {
            tabla[indice] = nuevoNodo;
        } else {
            NodoPelicula actual = tabla[indice];
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }

        if (!isRehashing) {
            tamaño++;

            if (tamaño > umbralRehash) {
                rehash();
            }
        }
    }

    private void rehash() {
        isRehashing = true;
        
        NodoPelicula[] tablaAntigua = tabla;
        tabla = new NodoPelicula[obtenerPrimerPrimoDespuesDe(tabla.length * 2)];

        for (NodoPelicula nodo : tablaAntigua) {
            while (nodo != null) {
                agregar(nodo.pelicula.id, nodo.pelicula.genero);
                nodo = nodo.siguiente;
            }
        }

        isRehashing = false;
        umbralRehash = (int) (tabla.length * 0.75);
    }


    public MiListaEnlazada obtenerPeliculas() {
        MiListaEnlazada listaPeliculas = new MiListaEnlazada();

        for (int i = 0; i < tabla.length; i++) {
            NodoPelicula actual = tabla[i];
            while (actual != null) {
                listaPeliculas.agregarAlPrincipio(actual.pelicula);
                actual = actual.siguiente;
            }
        }

        return listaPeliculas;
    }

    public Pelicula obtenerPeliculaPorID(int unId) {
        if (unId <= 0) {
            throw new IllegalArgumentException("El ID no es un entero positivo");
        }
        int indice = hashHorner(unId);
        NodoPelicula actual = tabla[indice];
        while (actual != null) {
            if (actual.pelicula.id == unId) {
                return actual.pelicula;
            }
            actual = actual.siguiente;
        }
        return null;  // Retorna null si no encuentra la película con ese ID
    }
}

class MiListaEnlazada {
    private NodoPelicula primero;
    private NodoPelicula ultimo;

    public NodoPelicula getPrimero() {
        return primero;
    }

    public void agregarAlPrincipio(Pelicula unaPelicula) {
        NodoPelicula nuevoNodo = new NodoPelicula(unaPelicula);
        if (primero == null) {
            primero = nuevoNodo;
            ultimo = nuevoNodo;
        } else {
            nuevoNodo.siguiente = primero;
            primero = nuevoNodo;
        }
    }
}

class Ejercicio2{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int G = scanner.nextInt();
        Genero[] generos = new Genero[G];
        for (int i = 0; i < G; i++) {
            generos[i] = new Genero(scanner.next());
        }

    MiHashTable peliculas = new MiHashTable();
        int P = scanner.nextInt();
        for (int i = 0; i < P; i++) {
            int id = scanner.nextInt();
            String genero = scanner.next();
            peliculas.agregar(id, genero);
        }

        int N = scanner.nextInt();
        for (int i = 0; i < N; i++) {
            int id = scanner.nextInt();
            int calificacion = scanner.nextInt();
    // Obtener pelicula por ID y actualizar calificacion
        Pelicula peliculaActual = peliculas.obtenerPeliculaPorID(id);
        if (peliculaActual != null) { 
        peliculaActual.agregarCalificacion(calificacion);
    }

    }

    MiListaEnlazada listaPeliculas = peliculas.obtenerPeliculas();

    // Por cada género, recorrer todas las películas y encontrar la de mejor calificación para ese género
    for (Genero genero : generos) {
        NodoPelicula nodoActual = listaPeliculas.getPrimero();
        while (nodoActual != null) {
            Pelicula peliculaActual = nodoActual.pelicula;
            if (peliculaActual.genero.equals(genero.nombre)) {
                genero.chequearMejorPelicula(peliculaActual);
            }
            nodoActual = nodoActual.siguiente;
        }
    }
    // Imprimir resultados
    for (Genero genero : generos) {
        System.out.println(genero.mejorPeliculaID);
    }
        scanner.close();
    }
}













