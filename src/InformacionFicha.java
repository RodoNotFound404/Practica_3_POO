import java.util.Random;


public class InformacionFicha {
    private FichaDomino[] mazo = new FichaDomino[28];
    private int restantes;

    public InformacionFicha() {
        int k = 0;
        for (int a = 0; a <= 6; a++) {
            for (int b = a; b <= 6; b++) {
                mazo[k++] = new FichaDomino(a, b);
            }
        }
        restantes = k;
        barajar();
    }

    public void barajar() {
        Random r = new Random();
        for (int i = restantes - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            FichaDomino tmp = mazo[i];
            mazo[i] = mazo[j];
            mazo[j] = tmp;
        }
    }

    public FichaDomino sacarFicha() {
        if (restantes == 0) return null;
        FichaDomino f = mazo[--restantes];
        mazo[restantes] = null;
        return f;
    }

    public int fichasRestantes() {
        return restantes;
    }

    public void mostrarMazo() {
        System.out.print("Mazo (" + restantes + "): ");
        for (int i = 0; i < restantes; i++) {
            System.out.print(mazo[i] + " ");
        }
        System.out.println();
    }
}