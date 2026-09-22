import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

// Clase que representa una carta individual
class Carta {
    String valor;
    String palo;
    int puntos;

    public Carta(String valor, String palo, int puntos) {
        this.valor = valor;
        this.palo = palo;
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
}

// Clase que administra la baraja
class Mazo {
    private List<Carta> cartas;

    public Mazo() {
        cartas = new ArrayList<>();
        String[] palos = {"Corazones", "Diamantes", "Tréboles", "Picas"};
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String palo : palos) {
            for (int i = 0; i < valores.length; i++) {
                // Las cartas numéricas valen su número. J, Q, K valen 10. El As vale 11 por defecto.
                int puntos = (i < 9) ? Integer.parseInt(valores[i]) : (valores[i].equals("A") ? 11 : 10);
                cartas.add(new Carta(valores[i], palo, puntos));
            }
        }
        Collections.shuffle(cartas);
    }

    public Carta robar() {
        return cartas.remove(cartas.size() - 1);
    }
}

// Clase que administra las cartas en la mano de un jugador o del crupier
class Mano {
    List<Carta> cartas = new ArrayList<>();

    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    public int obtenerValor() {
        int valorTotal = 0;
        int cantidadAses = 0;

        for (Carta c : cartas) {
            valorTotal += c.puntos;
            if (c.valor.equals("A")) {
                cantidadAses++;
            }
        }

        // Lógica clave del As: Si nos pasamos de 21 y tenemos un As, su valor pasa de 11 a 1.
        while (valorTotal > 21 && cantidadAses > 0) {
            valorTotal -= 10;
            cantidadAses--;
        }

        return valorTotal;
    }

    public String mostrarMano() {
        StringBuilder sb = new StringBuilder();
        for (Carta c : cartas) {
            sb.append("[").append(c.toString()).append("] ");
        }
        return sb.toString();
    }
}

// Clase principal con la lógica del juego
public class juego21 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean jugarDeNuevo = true;

        System.out.println("♠️ ♥️ ¡Bienvenido al Juego de 21 (Blackjack)! ♦️ ♣️");

        while (jugarDeNuevo) {
            Mazo mazo = new Mazo();
            Mano manoJugador = new Mano();
            Mano manoCrupier = new Mano();

            // Repartir cartas iniciales
            manoJugador.agregarCarta(mazo.robar());
            manoJugador.agregarCarta(mazo.robar());
            manoCrupier.agregarCarta(mazo.robar());
            manoCrupier.agregarCarta(mazo.robar());

            boolean turnoJugador = true;

            // Turno del Jugador
            while (turnoJugador) {
                System.out.println("\n--- TU TURNO ---");
                System.out.println("Tu mano: " + manoJugador.mostrarMano() + " (Valor: " + manoJugador.obtenerValor() + ")");
                System.out.println("Mano del crupier: [" + manoCrupier.cartas.get(0).toString() + "] [Carta Oculta]");

                if (manoJugador.obtenerValor() == 21) {
                    System.out.println("¡Tienes 21!");
                    break;
                } else if (manoJugador.obtenerValor() > 21) {
                    System.out.println("Te has pasado de 21. ¡Pierdes!");
                    turnoJugador = false;
                    break;
                }

                System.out.print("¿Deseas (1) Pedir carta o (2) Plantarte?: ");
                String opcion = scanner.nextLine();

                if (opcion.equals("1")) {
                    Carta nuevaCarta = mazo.robar();
                    manoJugador.agregarCarta(nuevaCarta);
                    System.out.println("Robaste: " + nuevaCarta);
                } else if (opcion.equals("2")) {
                    turnoJugador = false;
                } else {
                    System.out.println("Opción no válida.");
                }
            }

            // Turno del Crupier (solo si el jugador no perdió antes)
            if (manoJugador.obtenerValor() <= 21) {
                System.out.println("\n--- TURNO DEL CRUPIER ---");
                System.out.println("Mano del crupier: " + manoCrupier.mostrarMano() + " (Valor: " + manoCrupier.obtenerValor() + ")");

                // Regla clásica del Blackjack: El crupier pide carta hasta tener 17 o más.
                while (manoCrupier.obtenerValor() < 17) {
                    Carta nuevaCarta = mazo.robar();
                    manoCrupier.agregarCarta(nuevaCarta);
                    System.out.println("El crupier roba: " + nuevaCarta);
                    System.out.println("Mano del crupier: " + manoCrupier.mostrarMano() + " (Valor: " + manoCrupier.obtenerValor() + ")");
                }

                // Determinar el ganador
                int puntosJugador = manoJugador.obtenerValor();
                int puntosCrupier = manoCrupier.obtenerValor();

                System.out.println("\n--- RESULTADO FINAL ---");
                if (puntosCrupier > 21) {
                    System.out.println("El crupier se pasa de 21. ¡GANASTE!");
                } else if (puntosJugador > puntosCrupier) {
                    System.out.println("Tienes más puntos que el crupier. ¡GANASTE!");
                } else if (puntosJugador < puntosCrupier) {
                    System.out.println("El crupier tiene más puntos. ¡PIERDES!");
                } else {
                    System.out.println("Empate.");
                }
            }

            System.out.print("\n¿Quieres jugar otra ronda? (s/n): ");
            String respuesta = scanner.nextLine();
            if (!respuesta.equalsIgnoreCase("s")) {
                jugarDeNuevo = false;
                System.out.println("¡Gracias por jugar!");
            }
        }
        scanner.close();
    }
}