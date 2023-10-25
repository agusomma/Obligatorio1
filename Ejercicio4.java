import java.util.Scanner;

class Plato {
    String nombre;
    PlatoLista dependencias;
    boolean visitado;
    boolean enCola;

    Plato(String nombre) {
        this.nombre = nombre;
        this.dependencias = new PlatoLista();
        this.visitado = false;
        this.enCola = false;
    }

    void agregarDependencia(Plato plato) {
        this.dependencias.agregar(plato);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Plato otroPlato = (Plato) obj;
        return nombre.equals(otroPlato.nombre);
    }

    @Override
    public String toString() {
        return "Plato: " + nombre;
    }

    public int compareTo(Plato otroPlato) {
        return this.nombre.compareTo(otroPlato.nombre);
    }
}

class PlatoLista {
    NodoPlato primero;
    int tamanio;

    PlatoLista() {
        this.primero = null;
        this.tamanio = 0;
    }

    void agregar(Plato plato) {
        NodoPlato nuevoNodo = new NodoPlato(plato);
        if (primero == null) {
            primero = nuevoNodo;
        } else {
            NodoPlato actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamanio++;
    }

    Plato obtener(int index) {
        NodoPlato actual = primero;
        for (int i = 0; i < index; i++) {
            actual = actual.siguiente;
        }
        return actual.plato;
    }

    int tamanio() {
        return this.tamanio;
    }

    void ordenarAlfabeticamente() {
        if (tamanio <= 1) {
            return; // No se necesita ordenar
        }

        boolean intercambiado;
        do {
            intercambiado = false;
            NodoPlato actual = primero;
            NodoPlato siguiente = primero.siguiente;

            while (siguiente != null) {
                if (actual.plato.nombre.compareTo(siguiente.plato.nombre) > 0) {
                    // Intercambiar los platos
                    Plato temp = actual.plato;
                    actual.plato = siguiente.plato;
                    siguiente.plato = temp;
                    intercambiado = true;
                }
                actual = siguiente;
                siguiente = siguiente.siguiente;
            }
        } while (intercambiado);
    }
    Plato buscarPorNombre(String nombre) {
    NodoPlato actual = primero;
    while (actual != null) {
        if (actual.plato.nombre.equals(nombre)) {
            return actual.plato;
        }
        actual = actual.siguiente;
    }
    return null; // Plato no encontrado
}

}

class NodoPlato {
    Plato plato;
    NodoPlato siguiente;

    NodoPlato(Plato plato) {
        this.plato = plato;
        this.siguiente = null;
    }
}

class miStack {
    PlatoLista stack;

    miStack() {
        stack = new PlatoLista();
    }

    public boolean estaVacia() {
        return stack.tamanio == 0;
    }

    public void push(Plato plato) {
        stack.agregar(plato);
    }

    public Plato pop() {
        if (!estaVacia()) {
            Plato plato = stack.obtener(stack.tamanio - 1);
            stack.tamanio--;
            return plato;
        }
        return null;
    }

    public int obtenerTamanioPila() {
        return stack.tamanio;
    }
}

class PlatoGrafo {
    PlatoLista platos;

    PlatoGrafo() {
        platos = new PlatoLista();
    }

    void agregarPlato(Plato plato) {
        platos.agregar(plato);
    }

    PlatoLista ordenTopologico() {
        PlatoLista orden = new PlatoLista();
        miStack stack = new miStack();

        for (int i = 0; i < platos.tamanio(); i++) {
            Plato plato = platos.obtener(i);
            if (!plato.visitado) {
                ordenTopologicoRecursivo(plato, orden, stack);
            }
        }

        return orden;
    }

   /* PlatoLista ordenTopologicoConDesempate() {
        PlatoLista orden = new PlatoLista();
        miStack stack = new miStack();

        for (int i = 0; i < platos.tamanio(); i++) {
            Plato plato = platos.obtener(i);
            System.out.println("Plato: "+ plato);
            if (!plato.visitado) {
                System.out.println("ordenTopologicoConDesempate: i " + i +"PlatoNoVisitadoAun: "+ plato.toString());
                ordenTopologicoRecursivo(plato, orden, stack);
            }
        }

        return orden;
    }*/

    void ordenTopologicoRecursivo(Plato plato, PlatoLista orden, miStack stack) {
        plato.visitado = true;
        System.out.println("ordenTopologicoRecursivo-plato:" + plato.toString());

        PlatoLista dependenciasNoVisitadas = new PlatoLista();
        for (int i = 0; i < plato.dependencias.tamanio(); i++) {
            Plato dependencia = plato.dependencias.obtener(i);
            System.out.println("Dependencia: " + dependencia);
            if (!dependencia.visitado) {
                dependenciasNoVisitadas.agregar(dependencia);
            }
        }

        dependenciasNoVisitadas.ordenarAlfabeticamente(); // Ordenar alfabéticamente las dependencias

        for (int i = 0; i < dependenciasNoVisitadas.tamanio(); i++) {
            Plato dependencia = dependenciasNoVisitadas.obtener(i);
            System.out.println("DependenciaNoVisitada i =" + i + "dependencia: " + dependencia);
            ordenTopologicoRecursivo(dependencia, orden, stack);
        }

        stack.push(plato);
        System.out.println("stack.push.plato:" + plato.toString());
        orden.agregar(plato);
    }
}

public class Ejercicio4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int P = scanner.nextInt(); // Número total de platos
        int D = scanner.nextInt(); // Número de dependencias

        PlatoGrafo grafo = new PlatoGrafo();

        // Leer nombres de platos y agregarlos al grafo
        for (int i = 0; i < P; i++) {
            String nombrePlato = scanner.next();
            Plato plato = new Plato(nombrePlato);
            grafo.agregarPlato(plato);
        }

        // Leer dependencias entre platos y establecerlas en el grafo
        for (int i = 0; i < D; i++) {
            String nombreA = scanner.next();
            String nombreB = scanner.next();

            Plato platoA = grafo.platos.buscarPorNombre(nombreA);
            Plato platoB = grafo.platos.buscarPorNombre(nombreB);

            if (platoA != null && platoB != null) {
                System.out.println("platoA: " + platoA.toString() + " platoB" + platoB.toString());
                platoB.agregarDependencia(platoA);
            }
        }

        // Realizar el orden topológico con desempate alfabético
        // PlatoLista ordenTopologico = grafo.ordenTopologicoConDesempate();

        PlatoLista ordenTopologico = grafo.ordenTopologico();

        if (ordenTopologico.tamanio() < P) {
            System.out.println("CICLO DETECTADO");
        } else {
            for (int i = 0; i < ordenTopologico.tamanio(); i++) {
                Plato plato = ordenTopologico.obtener(i);
                System.out.println("i = " + i);
                System.out.println(plato.nombre);
            }
        }

        scanner.close();
    }
}
