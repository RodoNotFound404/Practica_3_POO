import java.util.ArrayList;
import java.util.Scanner;


public class Juego {
    private static final String[] BRAZO = {"Izquierda", "Derecha", "Arriba", "Abajo"};

    private int cantidadJugador;
    private Jugador[] jugadores;
    private InformacionFicha pozo;
    private ArrayList<ArrayList<FichaDomino>> brazos;
    private FichaDomino espinner;
    private int turnoActual;
    private int pasesSeguidos;
    private boolean terminado;
    private Scanner sc = new Scanner(System.in);

    public Juego() {
        this(2);
    }

    public Juego(int cantidadJugador) {
        if (cantidadJugador < 2 || cantidadJugador > 4) {
            throw new IllegalArgumentException("El juego admite de 2 a 4 jugadores");
        }
        this.cantidadJugador = cantidadJugador;
        jugadores = new Jugador[cantidadJugador];
        for (int i = 0; i < cantidadJugador; i++) {
            jugadores[i] = new Jugador("Jugador " + (i + 1), i + 1);
        }
    }

    // ---------- Preparación ----------


    public void repartirFichasJugador() {
        int fichas = (cantidadJugador == 2) ? 7 : 5;
        boolean hayDoble;
        do {
            pozo = new InformacionFicha();
            hayDoble = false;
            for (Jugador j : jugadores) {
                j.getJugadorMazo().clear();
                for (int k = 0; k < fichas; k++) j.agregarFicha(pozo.sacarFicha());
                if (indiceMayorDoble(j) >= 0) hayDoble = true;
            }
        } while (!hayDoble);

        int mejor = -1;
        for (int i = 0; i < cantidadJugador; i++) {
            int idx = indiceMayorDoble(jugadores[i]);
            if (idx >= 0) {
                int valor = jugadores[i].getJugadorMazo().get(idx).getLadoA();
                if (valor > mejor) {
                    mejor = valor;
                    turnoActual = i;
                }
            }
        }
    }

    private int indiceMayorDoble(Jugador j) {
        int idx = -1, max = -1;
        ArrayList<FichaDomino> m = j.getJugadorMazo();
        for (int i = 0; i < m.size(); i++) {
            if (m.get(i).esDoble() && m.get(i).getLadoA() > max) {
                max = m.get(i).getLadoA();
                idx = i;
            }
        }
        return idx;
    }

    // ---------- Tablero y puntos ----------

    private int valorExtremo(int brazo) {
        ArrayList<FichaDomino> b = brazos.get(brazo);
        return b.isEmpty() ? espinner.getLadoA() : b.get(b.size() - 1).getLadoB();
    }

    private boolean brazoDisponible(int brazo) {
        return brazo < 2 || (!brazos.get(0).isEmpty() && !brazos.get(1).isEmpty());
    }

    private boolean puedeColocar(FichaDomino f, int brazo) {
        if (!brazoDisponible(brazo)) return false;
        int v = valorExtremo(brazo);
        return f.getLadoA() == v || f.getLadoB() == v;
    }

    private boolean fichaJugable(FichaDomino f) {
        for (int i = 0; i < 4; i++) if (puedeColocar(f, i)) return true;
        return false;
    }

    private boolean tieneJugada(Jugador j) {
        for (FichaDomino f : j.getJugadorMazo()) if (fichaJugable(f)) return true;
        return false;
    }

    private void colocar(FichaDomino f, int brazo) {
        if (f.getLadoA() != valorExtremo(brazo)) f.girarFicha();
        brazos.get(brazo).add(f); // ladoA toca el brazo, ladoB queda expuesto
    }

    private int sumaExtremos() {
        int v = espinner.getLadoA();
        boolean solo = true;
        for (ArrayList<FichaDomino> b : brazos) if (!b.isEmpty()) solo = false;
        if (solo) return 2 * v;

        int suma = 0;
        for (int i = 0; i < 4; i++) {
            ArrayList<FichaDomino> b = brazos.get(i);
            if (!b.isEmpty()) {
                FichaDomino ult = b.get(b.size() - 1);
                suma += ult.esDoble() ? 2 * ult.getLadoB() : ult.getLadoB();
            } else if (i < 2) {
                suma += 2 * v;
            } else if (brazoDisponible(i)) {
                suma += v;           // brazo vertical abierto pero vacío
            }
        }
        return suma;
    }

    public int combosPuntos() {
        int s = sumaExtremos();
        return (s > 0 && s % 5 == 0) ? s : 0;
    }

    public void tablero() {
        StringBuilder linea = new StringBuilder();
        ArrayList<FichaDomino> izq = brazos.get(0);
        for (int i = izq.size() - 1; i >= 0; i--) {
            linea.append("[").append(izq.get(i).getLadoB()).append("|").append(izq.get(i).getLadoA()).append("] ");
        }
        linea.append("<").append(espinner).append("> ");
        for (FichaDomino f : brazos.get(1)) linea.append(f).append(" ");

        System.out.println("\n----------- TABLERO -----------");
        if (brazoDisponible(2)) System.out.println("Arriba : " + brazos.get(2));
        System.out.println(linea);
        if (brazoDisponible(3)) System.out.println("Abajo  : " + brazos.get(3));
        System.out.println("Suma de extremos: " + sumaExtremos());
        System.out.println("-------------------------------");
    }

    // ---------- Consultas ----------

    public int fichasRestantesJugador(int i) { return jugadores[i].getJugadorMazo().size(); }
    public int puntajeTotalJugador(int i) { return jugadores[i].getPuntaje(); }
    public int fichasRestantesMazo() { return pozo.fichasRestantes(); }

    // ---------- Turnos ----------

    private int leerEntero(String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n >= min && n <= max) return n;
            } catch (NumberFormatException e) {
                // se vuelve a pedir
            }
            System.out.println("Valor inválido.");
        }
    }

    private void mostrarMano(Jugador j) {
        System.out.println("Pozo: " + fichasRestantesMazo() + " fichas | Puntaje: " + j.getPuntaje());
        System.out.print("Tu mano: ");
        ArrayList<FichaDomino> m = j.getJugadorMazo();
        for (int i = 0; i < m.size(); i++) System.out.print((i + 1) + ")" + m.get(i) + " ");
        System.out.println();
    }

    public void turnoJugador() {
        Jugador j = jugadores[turnoActual];
        ArrayList<FichaDomino> mano = j.getJugadorMazo();
        System.out.println("\n=== Turno de " + j.getStringNombre() + " ===");

        while (true) {
            tablero();
            mostrarMano(j);

            if (!tieneJugada(j)) {
                if (fichasRestantesMazo() > 0) {
                    FichaDomino robada = pozo.sacarFicha();
                    j.agregarFicha(robada);
                    System.out.println("No tienes jugada, robas " + robada);
                    continue;
                }
                System.out.println("Sin jugadas y pozo vacío: pasas turno.");
                pasesSeguidos++;
                if (pasesSeguidos >= cantidadJugador) terminado = true;
                break;
            }

            int idx = leerEntero("Elige ficha (1-" + mano.size() + "): ", 1, mano.size()) - 1;
            FichaDomino f = mano.get(idx);
            if (!fichaJugable(f)) {
                System.out.println("Esa ficha no encaja en ningún brazo.");
                continue;
            }
            int brazo;
            do {
                brazo = leerEntero("Brazo (1 Izq, 2 Der, 3 Arriba, 4 Abajo): ", 1, 4) - 1;
                if (!puedeColocar(f, brazo)) System.out.println("No se puede colocar ahí.");
            } while (!puedeColocar(f, brazo));

            colocar(f, brazo);
            mano.remove(idx);
            pasesSeguidos = 0;

            int pts = combosPuntos();
            if (pts > 0) {
                j.setPuntaje(j.getPuntaje() + pts);
                System.out.println("¡Combo! " + j.getStringNombre() + " anota " + pts + " puntos.");
            }
            if (mano.isEmpty()) terminado = true;
            break;
        }
        if (!terminado) turnoActual = (turnoActual + 1) % cantidadJugador;
    }

    // ---------- Flujo general ----------

    public void jugar() {
        repartirFichasJugador();
        brazos = new ArrayList<>();
        for (int i = 0; i < 4; i++) brazos.add(new ArrayList<>());
        pasesSeguidos = 0;
        terminado = false;

        Jugador inicial = jugadores[turnoActual];
        int idx = indiceMayorDoble(inicial);
        espinner = inicial.getJugadorMazo().remove(idx);
        System.out.println(inicial.getStringNombre() + " inicia con el sniff " + espinner);

        int pts = combosPuntos();
        if (pts > 0) {
            inicial.setPuntaje(inicial.getPuntaje() + pts);
            System.out.println("¡Combo! " + inicial.getStringNombre() + " anota " + pts + " puntos.");
        }
        turnoActual = (turnoActual + 1) % cantidadJugador;

        while (!terminado) turnoJugador();
        acabarJuego();
    }

    public void acabarJuego() {
        System.out.println("\n########## FIN DE LA MANO ##########");
        Jugador quien = jugadores[turnoActual];
        if (quien.getJugadorMazo().isEmpty()) {
            int otros = 0;
            for (Jugador j : jugadores) if (j != quien) otros += j.puntosEnMano();
            int bono = (int) (Math.round(otros / 5.0) * 5);
            quien.setPuntaje(quien.getPuntaje() + bono);
            System.out.println(quien.getStringNombre() + " se quedó sin fichas y gana un bono de " + bono);
        } else {
            System.out.println("Juego bloqueado.");
        }

        Jugador ganador = jugadores[0];
        for (int i = 0; i < cantidadJugador; i++) {
            System.out.println(jugadores[i].getStringNombre() + ": " + puntajeTotalJugador(i)
                    + " pts (fichas restantes: " + fichasRestantesJugador(i) + ")");
            if (jugadores[i].getPuntaje() > ganador.getPuntaje()) ganador = jugadores[i];
        }
        System.out.println("\nGanador: " + ganador.getStringNombre() + " con " + ganador.getPuntaje() + " puntos");
    }
}