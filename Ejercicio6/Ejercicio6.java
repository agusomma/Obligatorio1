import java.util.Scanner;

class Conexion {
    int ubicacionOrigen;
    int ubicacionDestino;
    int costo;

    public Conexion(int ubicacionOrigen, int ubicacionDestino, int costo) {
        this.ubicacionOrigen = ubicacionOrigen;
        this.ubicacionDestino = ubicacionDestino;
        this.costo = costo;
    }
}

class MiColaDePrioridad {
    private Conexion[] conexiones;
    private int tamanio;
    private int capacidad;

    public MiColaDePrioridad(int capacidad) {
        this.capacidad = capacidad;
        conexiones = new Conexion[capacidad];
        tamanio = 0;
    }

    public boolean esVacia() {
        return tamanio == 0;
    }

    public void encolar(Conexion conexion) {
        if (tamanio == capacidad) {
            System.out.println("La cola de prioridad está llena.");
            return;
        }

        int posicionActual = tamanio;
        conexiones[posicionActual] = conexion;

        // flotar
        while (posicionActual > 0) {
            int posicionPadre = (posicionActual - 1) / 2;
            if (conexiones[posicionActual].costo < conexiones[posicionPadre].costo) {
                actualizarPosicion(posicionActual, posicionPadre);
                posicionActual = posicionPadre;
            } else {
                break;
            }
        }
        tamanio++;
    }

    public Conexion obtenerYeliminarMinimo() {
        if (esVacia()) {
            System.out.println("La cola de prioridad está vacía.");
            return null;
        }

        Conexion minimaConexion = conexiones[0];
        conexiones[0] = conexiones[tamanio - 1];
        tamanio--;

        int posicionActual = 0;

        // hundir
        while (true) {
            int hijoIzquierdo = 2 * posicionActual + 1;
            int hijoDerecho = 2 * posicionActual + 2;
            int minimaPosicion = posicionActual;

            if (hijoIzquierdo < tamanio && conexiones[hijoIzquierdo].costo < conexiones[minimaPosicion].costo) {
                minimaPosicion = hijoIzquierdo;
            }
            if (hijoDerecho < tamanio && conexiones[hijoDerecho].costo < conexiones[minimaPosicion].costo) {
                minimaPosicion = hijoDerecho;
            }

            if (minimaPosicion != posicionActual) {
                actualizarPosicion(posicionActual, minimaPosicion);
                posicionActual = minimaPosicion;
            } else {
                break;
            }
        }

        return minimaConexion;

    }

    private void actualizarPosicion(int posicion, int nuevaPosicion) {
        Conexion con = conexiones[posicion];
        conexiones[posicion] = conexiones[nuevaPosicion];
        conexiones[nuevaPosicion] = con;
    }
}


public class Ejercicio6 {
    public static void main(String[] args) {
            

            
            /*
            //Scanner scanner = new Scanner(System.in);
            int cantUbicaciones = 4;//scanner.nextInt(); // Número de ubicaciones
            int cantConexiones = 5;//scanner.nextInt(); // Número de conexiones

            Conexion[] conexiones = new Conexion[cantConexiones];
            Conexion con1 = new Conexion(1, 2, 3);
            Conexion con2 = new Conexion(1, 3, 2);
            Conexion con3 = new Conexion(1, 4, 5);
            Conexion con4 = new Conexion(2, 3, 1);
            Conexion con5 = new Conexion(3, 4, 4);

            conexiones[0] = con1;
            conexiones[1] = con2;
            conexiones[2] = con3;
            conexiones[3] = con4;
            conexiones[4] = con5;
        
           */


        Scanner scanner = new Scanner(System.in);

        int cantUbicaciones = scanner.nextInt(); // Número de ubicaciones
        int cantConexiones = scanner.nextInt(); // Número de conexiones
        scanner.nextLine(); // Lee la línea en blanco

        System.out.println("ubis: " + cantUbicaciones);
        System.out.println("cons: " + cantConexiones);

        System.out.println("Ingrese las conexiones en el formato 'source target weight':");

        Conexion[] conexiones = new Conexion[cantConexiones];

        // Leer las conexiones
        for (int i = 0; i < cantConexiones; i++) {
            int conexionOrigen = scanner.nextInt();
            int conexionDestino = scanner.nextInt();
            int conexionPeso = scanner.nextInt();
            conexiones[i] = new Conexion(conexionOrigen, conexionDestino, conexionPeso);
            System.out.println("Conexión " + (i + 1) + ": conexionOrigen=" + conexionOrigen + " conexionDestino=" + conexionDestino + " conexionPeso=" + conexionPeso);
        }

           

            int minCost = miArbolkruskal(conexiones, cantUbicaciones);
            System.out.println("El costo mínimo para conectar todas las ubicaciones es: " + minCost);


        scanner.close();

        }
        



    private static int miArbolkruskal(Conexion[] conexiones, int cantUbicaciones) {
        MiColaDePrioridad conexionesOrdenadas = new MiColaDePrioridad(conexiones.length);

        for (int i = 0; i < conexiones.length; i++) {
            Conexion con = conexiones[i];
            conexionesOrdenadas.encolar(con);
        }

        int[] representante = new int[cantUbicaciones];
        int costoMinimo = 0;

        for (int i = 0; i < cantUbicaciones; i++) {
            representante[i] = i;
        }

        int contadorConexiones = 0;

        while (contadorConexiones < cantUbicaciones - 1) {
            Conexion conexion = conexionesOrdenadas.obtenerYeliminarMinimo();
            int ubicacionOrigenTemp = find(representante, conexion.ubicacionOrigen - 1);
            int ubicacionDestinoTemp = find(representante, conexion.ubicacionDestino - 1);

            if (ubicacionOrigenTemp != ubicacionDestinoTemp) {
                costoMinimo += conexion.costo;
                union(representante, ubicacionOrigenTemp, ubicacionDestinoTemp);
                contadorConexiones++;
            }
        }

        return costoMinimo;
    }

    private static int find(int[] representante, int ubicacion) {
        while (ubicacion != representante[ubicacion]) {
            ubicacion = representante[ubicacion];
        }
        return ubicacion;
    }

    private static void union(int[] representante, int ubicacionOrigen, int ubicacionDestino) {
        int ubiacionOrigenTemp = find(representante, ubicacionOrigen);
        int ubiacionDestinoTemp = find(representante, ubicacionDestino);
        representante[ubiacionOrigenTemp] = ubiacionDestinoTemp;
    }
}
