import java.util.Scanner;

class Plato {
    String nombre;
    Plato[] dependencias;
    int numDependencias;
    boolean visitado;
    boolean enStack;

    Plato(String nombre, int maxDependencias) {
        this.nombre = nombre;
        this.dependencias = new Plato[maxDependencias];
        this.numDependencias = 0;
        this.visitado = false;
        this.enStack = false;
    }

    void agregarDependencia(Plato plato) {
        this.dependencias[this.numDependencias++] = plato;
    }
}

class PlatoLista {
    Plato[] platos;
    int tamanio;

    PlatoLista(int capacidad) {
        this.platos = new Plato[capacidad];
        this.tamanio = 0;
    }

    void agregar(Plato plato) {
        this.platos[this.tamanio++] = plato;
    }

    Plato obtener(int indice) {
        return this.platos[indice];
    }

    int tamanio() {
        return this.tamanio;
    }

    Plato buscarPorNombre(String nombre) {
        for (int i = 0; i < tamanio; i++) {
            if (platos[i].nombre.equals(nombre)) {
                return platos[i];
            }
        }
        return null;
    }
}

class miStack {
    Plato[] stack;
    int top;

    miStack(int capacidad) {
        stack = new Plato[capacidad];
        top = -1;
    }

    boolean estaVacia() {
        return top == -1;
    }

    void push(Plato plato) {
        stack[++top] = plato;
    }

    Plato pop() {
        return stack[top--];
    }

    int tamanio() {
        return top + 1;
    }

    Plato obtener(int indice) {
        return stack[indice];
    }
}
class Restaurante {
    private PlatoLista platos;
    private miStack resultado;
    private Scanner sc;

    public Restaurante(Scanner sc) {
        this.platos = new PlatoLista(100);
        this.resultado = new miStack(100);
        this.sc = sc;
    }

    public void leerPlatos() {
        int P = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < P; i++) {
            String nombrePlato = sc.nextLine();
            platos.agregar(new Plato(nombrePlato, P));
        }
    }

    public void leerDependencias() {
        int D = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < D; i++) {
            String platoA_nombre = sc.next();
            String platoB_nombre = sc.next();
            sc.nextLine();

            Plato platoA = platos.buscarPorNombre(platoA_nombre);
            Plato platoB = platos.buscarPorNombre(platoB_nombre);

            platoA.agregarDependencia(platoB);
        }
    }

    public boolean verificarCiclos() {
        for (int i = 0; i < platos.tamanio(); i++) {
            if (!platos.obtener(i).visitado) {
                if (esCiclico(platos.obtener(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void imprimirResultado() {
        while (!resultado.estaVacia()) {
            System.out.println(resultado.pop().nombre);
        }
    }

    private boolean esCiclico(Plato plato) {
        if (plato.enStack) return true;
        if (plato.visitado) return false;

        plato.visitado = true;
        plato.enStack = true;

        // Ordenar alfabéticamente las dependencias
        for (int i = 0; i < plato.numDependencias - 1; i++) {
            for (int j = 0; j < plato.numDependencias - i - 1; j++) {
                if (plato.dependencias[j].nombre.compareTo(plato.dependencias[j + 1].nombre) > 0) {
                    Plato temp = plato.dependencias[j];
                    plato.dependencias[j] = plato.dependencias[j + 1];
                    plato.dependencias[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < plato.numDependencias; i++) {
            if (esCiclico(plato.dependencias[i])) return true;
        }

        plato.enStack = false;
        resultado.push(plato);
        return false;
    }
}

public class Ejercicio4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Restaurante restaurante = new Restaurante(sc);

        restaurante.leerPlatos();
        restaurante.leerDependencias();

        if (restaurante.verificarCiclos()) {
            System.out.println("CICLO DETECTADO");
        } else {
            restaurante.imprimirResultado();
        }
    }
}
