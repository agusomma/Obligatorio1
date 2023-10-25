import java.util.*;
import java.util.stream.Collectors;

class Portal {
    int destination;
    int cost;

    public Portal(int destination, int cost) {
        this.destination = destination;
        this.cost = cost;
    }
}

public class MiListaPersonalizada<T> {
    private Object[] elementos;
    private int tamaño;
    private static final int CAPACIDAD_INICIAL = 10;

    public MiListaPersonalizada() {
        elementos = new Object[CAPACIDAD_INICIAL];
        tamaño = 0;
    }

    public void add(T elemento) {
        if (tamaño == elementos.length) {
            ampliarCapacidad();
        }
        elementos[tamaño] = elemento;
        tamaño++;
    }

    public T get(int indice) {
        if (indice < 0 || indice >= tamaño) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        return (T) elementos[indice];
    }

    public int tamaño() {
        return tamaño;
    }

    private void ampliarCapacidad() {
        int nuevaCapacidad = elementos.length * 2;
        elementos = Arrays.copyOf(elementos, nuevaCapacidad);
    }
}



public class Ejercicio5 {
    public static void main(String[] args) {
        
        /*
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();

        List<List<Portal>> portals = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            portals.add(new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            int A = scanner.nextInt() - 1;
            int B = scanner.nextInt() - 1;
            int C = scanner.nextInt();
            portals.get(A).add(new Portal(B, C));
        }

        */

        int N = 5; // Número de nodos planetas
        int M = 19; // Número de aristas portales

        System.out.println("planetas:" + N);
        System.out.println("portales:" + M);
        
        List<List<Portal>> portales = new ArrayList<>();
        

       
        
        for (int i = 0; i < N; i++) {
            portales.add(new ArrayList<>());
        }



        portales.get(0).add(new Portal(1, 16));
        portales.get(0).add(new Portal(3, 93));
        portales.get(0).add(new Portal(4, 63));
        portales.get(1).add(new Portal(2, 27));
        portales.get(1).add(new Portal(3, 12));
        portales.get(1).add(new Portal(4, 30));
        portales.get(2).add(new Portal(0, 63));
        portales.get(2).add(new Portal(1, 36));
        portales.get(2).add(new Portal(3, 59));
        portales.get(2).add(new Portal(4, 94));
        portales.get(3).add(new Portal(0, 43));
        portales.get(3).add(new Portal(1, 22));
        portales.get(3).add(new Portal(2, 38));
        portales.get(3).add(new Portal(4, 71));
        portales.get(4).add(new Portal(0, 92));
        portales.get(4).add(new Portal(1, 74));
        portales.get(4).add(new Portal(2, 97));
        portales.get(4).add(new Portal(3, 26));
    


        int planetaOrigen = 4; //5 -1
        int planetaDestino = 0;// 1-1

        int result = calcularCostoMinimo(N, portales, planetaOrigen, planetaDestino);

        if (result == -1) {
            System.out.println(-1);
        } else {
            System.out.println(result);
        }
    }

    

    public static int calcularCostoMinimo(int N, List<List<Portal>> portales, int planetaOrigen, int planetaDestino) {
        int[] distance = new int[N];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[planetaOrigen] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{planetaOrigen, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentPlanet = current[0];
            int currentDistance = current[1];

            if (currentPlanet == planetaDestino) {
                return currentDistance;
            }

            if (currentDistance > distance[currentPlanet]) {
                continue;
            }

            for (Portal portal : portales.get(currentPlanet)) {
                int neighbor = portal.destination;
                int cost = portal.cost;

                if (currentDistance + cost < distance[neighbor]) {
                    distance[neighbor] = currentDistance + cost;
                    pq.add(new int[]{neighbor, distance[neighbor]});
                }
            }
        }

        return -1;
    }
}