import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("¿Cuántos jugadores (2-4)? ");
        int n = Integer.parseInt(sc.nextLine().trim());
        new Juego(n).jugar();
    }
}