import java.util.Scanner;

class Paciente implements Comparable<Paciente> {
    int id;
    int tiempoLlegada;
    int urgencia;
    int ordenLectura;

    // Contador estático global para seguir el orden de lectura
    private static int contadorOrdenLectura = 0;

    public Paciente(int id, int tiempoLlegada, int urgencia) {
        this.id = id;
        this.tiempoLlegada = tiempoLlegada;
        this.urgencia = urgencia;
        // Incrementar el contador y asignarlo a ordenLectura
        this.ordenLectura = ++contadorOrdenLectura;
    }

    @Override
    public int compareTo(Paciente otro) {
        if (this.urgencia != otro.urgencia) {
            return otro.urgencia - this.urgencia;  // Mayor urgencia primero
        }
        if (this.tiempoLlegada != otro.tiempoLlegada) {
            return this.tiempoLlegada - otro.tiempoLlegada;  // Menor tiempo de llegada primero
        }
        return this.ordenLectura - otro.ordenLectura;  // Orden de lectura primero
    }
}

class MiColaDePrioridad {
    private Paciente[] array;
    private int tamanio;
    public MiColaDePrioridad(int capacidadInicial) {
        array = new Paciente[capacidadInicial];
        tamanio = 0;
    }

    public boolean estaVacio() {
        return tamanio == 0;
    }

    public void agregar(Paciente valor) {
        if (tamanio == array.length) {
            duplicarArray();
        }

        array[tamanio] = valor;
        subir(tamanio);
        tamanio++;
    }


    public Paciente obtnerYEliminarMin() {
        if (estaVacio()) {
            throw new RuntimeException("CP vacío");
        }

        Paciente minimo = array[0];
        array[0] = array[tamanio - 1];
        tamanio--;
        bajar(0);

        return minimo;
    }

    public Paciente obtenerValorMin() {
        if (estaVacio()) {
            throw new RuntimeException("CP vacía");
        }
        return array[0];
    }

    private void subir(int i) {
        while (i > 0 && array[(i - 1) / 2].compareTo(array[i]) > 0) {
            intercambiar(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    private void bajar(int i) {
        while (2 * i + 1 < tamanio) {
            int hijoMenor = 2 * i + 1;
            if (hijoMenor + 1 < tamanio && array[hijoMenor + 1].compareTo(array[hijoMenor]) < 0) {
                hijoMenor++;
            }

            if (array[i].compareTo(array[hijoMenor]) <= 0) {
                break;
            }

            intercambiar(i, hijoMenor);
            i = hijoMenor;
        }
    }

    private void intercambiar(int i, int j) {
        Paciente aux = array[i];
        array[i] = array[j];
        array[j] = aux;
    }

    private void duplicarArray() {
        Paciente[] nuevoArray = new Paciente[array.length * 2];
        System.arraycopy(array, 0, nuevoArray, 0, tamanio);
        array = nuevoArray;
}

}
public class Ejercicio3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Leer el número de pacientes
        int N = scanner.nextInt();

        MiColaDePrioridad colaPacientes = new MiColaDePrioridad(N);
        
        // Leer cada paciente y agregarlo a la cola de prioridad
        for (int i = 0; i < N; i++) {
            int P = scanner.nextInt();  // ID del paciente
            int T = scanner.nextInt();  // Tiempo de llegada
            int U = scanner.nextInt();  // Nivel de urgencia
            colaPacientes.agregar(new Paciente(P, T, U));
        }

        // Mostrar y eliminar pacientes en orden de prioridad
        while (!colaPacientes.estaVacio()) {
            System.out.println(colaPacientes.obtnerYEliminarMin().id);
        }

        scanner.close();
    }
}

