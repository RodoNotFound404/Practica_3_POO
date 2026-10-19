public class FichaDomino {
    private int ladoA;
    private int ladoB;
    private int puntosA;
    private int puntosB;

    public FichaDomino() {
    }

    public FichaDomino(int ladoA, int ladoB) {
        this.ladoA = ladoA;
        this.ladoB = ladoB;
        this.puntosA = ladoA;
        this.puntosB = ladoB;
    }

    public int getLadoA() { return ladoA; }
    public void setLadoA(int ladoA) { this.ladoA = ladoA; }
    public int getLadoB() { return ladoB; }
    public void setLadoB(int ladoB) { this.ladoB = ladoB; }
    public int getPuntosA() { return puntosA; }
    public void setPuntosA(int puntosA) { this.puntosA = puntosA; }
    public int getPuntosB() { return puntosB; }
    public void setPuntosB(int puntosB) { this.puntosB = puntosB; }

    /** Intercambia los lados (y sus puntos) de la ficha. */
    public void girarFicha() {
        int tmp = ladoA;
        ladoA = ladoB;
        ladoB = tmp;
        tmp = puntosA;
        puntosA = puntosB;
        puntosB = tmp;
    }

    public boolean esDoble() {
        return ladoA == ladoB;
    }

    public int totalPuntos() {
        return puntosA + puntosB;
    }

    @Override
    public String toString() {
        return "[" + ladoA + "|" + ladoB + "]";
    }
}