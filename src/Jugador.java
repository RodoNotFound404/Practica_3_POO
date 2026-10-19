import java.util.ArrayList;

public class Jugador {
    private ArrayList<FichaDomino> jugadorMazo = new ArrayList<>();
    private String nombre;
    private int posicion;
    private int puntajeTotal;

    public Jugador() {
    }

    public Jugador(String nombre, int posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.puntajeTotal = 0;
    }

    public ArrayList<FichaDomino> getJugadorMazo() { return jugadorMazo; }
    public void setJugadorMazo(ArrayList<FichaDomino> jugadorMazo) { this.jugadorMazo = jugadorMazo; }
    public String getStringNombre() { return nombre; }
    public void setStringNombre(String nombre) { this.nombre = nombre; }
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }
    public int getPuntaje() { return puntajeTotal; }
    public void setPuntaje(int puntaje) { this.puntajeTotal = puntaje; }

    public void agregarFicha(FichaDomino f) {
        if (f != null) jugadorMazo.add(f);
    }

    public int puntosEnMano() {
        int s = 0;
        for (FichaDomino f : jugadorMazo) s += f.totalPuntos();
        return s;
    }
}