import java.util.Random;
import java.util.Scanner;

public class Buscaminas {
    // Configuración del juego (puedes cambiar estos valores)
    private static final int FILAS = 8;
    private static final int COLUMNAS = 8;
    private static final int CANTIDAD_MINAS = 10;

    // Matrices para controlar el juego
    private static char[][] tableroVisible = new char[FILAS][COLUMNAS];
    private static boolean[][] matrizMinas = new boolean[FILAS][COLUMNAS];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean juegoTerminado = false;

        inicializarTableros();
        colocarMinas();

        System.out.println("¡Bienvenido al Buscaminas en Java!");

        // Bucle principal del juego
        while (!juegoTerminado) {
            imprimirTablero(false);
            
            System.out.print("Ingresa la FILA (0 a " + (FILAS - 1) + "): ");
            int fila = scanner.nextInt();
            System.out.print("Ingresa la COLUMNA (0 a " + (COLUMNAS - 1) + "): ");
            int columna = scanner.nextInt();

            // Validar entrada
            if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) {
                System.out.println("Coordenadas inválidas. Intenta de nuevo.\n");
                continue;
            }

            // Condición de derrota
            if (matrizMinas[fila][columna]) {
                juegoTerminado = true;
                System.out.println("\n¡BOOM! Has pisado una mina. Fin del juego.");
                imprimirTablero(true); // Muestra todas las minas
            } else {
                // Revelar casillas
                revelarCasilla(fila, columna);
                
                // Condición de victoria
                if (verificarVictoria()) {
                    juegoTerminado = true;
                    System.out.println("\n¡Felicidades! Has despejado el campo de minas.");
                    imprimirTablero(true);
                }
            }
        }
        scanner.close();
    }

    // Llena el tablero visible con guiones y la matriz de minas con 'false'
    private static void inicializarTableros() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tableroVisible[i][j] = '-';
                matrizMinas[i][j] = false;
            }
        }
    }

    // Coloca minas de forma aleatoria sin repetir posiciones
    private static void colocarMinas() {
        Random random = new Random();
        int minasColocadas = 0;

        while (minasColocadas < CANTIDAD_MINAS) {
            int filaAleatoria = random.nextInt(FILAS);
            int colAleatoria = random.nextInt(COLUMNAS);

            if (!matrizMinas[filaAleatoria][colAleatoria]) {
                matrizMinas[filaAleatoria][colAleatoria] = true;
                minasColocadas++;
            }
        }
    }

    // Imprime el estado actual del tablero
    private static void imprimirTablero(boolean mostrarMinas) {
        System.out.print("\n  ");
        for (int j = 0; j < COLUMNAS; j++) {
            System.out.print(j + " ");
        }
        System.out.println();

        for (int i = 0; i < FILAS; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < COLUMNAS; j++) {
                if (mostrarMinas && matrizMinas[i][j]) {
                    System.out.print("* "); // Imprime la mina
                } else {
                    System.out.print(tableroVisible[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Función recursiva que revela la casilla y las adyacentes si no hay minas cerca
    private static void revelarCasilla(int fila, int columna) {
        // Evitar desbordamientos y no procesar casillas ya reveladas
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS || tableroVisible[fila][columna] != '-') {
            return;
        }

        int minasCercanas = contarMinasAdyacentes(fila, columna);
        tableroVisible[fila][columna] = (char) (minasCercanas + '0');

        // Si no hay minas alrededor (es un 0), revelamos las casillas vecinas automáticamente
        if (minasCercanas == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    revelarCasilla(fila + i, columna + j);
                }
            }
        }
    }

    // Cuenta cuántas minas hay en las 8 direcciones alrededor de una casilla
    private static int contarMinasAdyacentes(int fila, int columna) {
        int contador = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int f = fila + i;
                int c = columna + j;
                if (f >= 0 && f < FILAS && c >= 0 && c < COLUMNAS) {
                    if (matrizMinas[f][c]) {
                        contador++;
                    }
                }
            }
        }
        return contador;
    }

    // El jugador gana si las únicas casillas ocultas ('-') que quedan son exactamente las minas
    private static boolean verificarVictoria() {
        int casillasOcultas = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (tableroVisible[i][j] == '-') {
                    casillasOcultas++;
                }
            }
        }
        return casillasOcultas == CANTIDAD_MINAS;
    }
}