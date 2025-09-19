package com.blackjack.service;

import com.blackjack.model.*;
import java.util.concurrent.TimeUnit;

public class BlackjackService {
    private Mazo mazo;
    private Jugador jugador;
    private Crupier crupier;
    private boolean juegoTerminado;
    private boolean apuestaHecha;

    public BlackjackService() {
        this.mazo = new Mazo();
        this.jugador = new Jugador("Jugador 1");
        this.crupier = new Crupier();
        this.juegoTerminado = false;
        this.apuestaHecha = false;
    }

    // ===== MÉTODOS DE APUESTAS =====
    public boolean hacerApuesta(int cantidad) {
        if (jugador.apostar(cantidad)) {
            apuestaHecha = true;
            return true;
        }
        return false;
    }

    public int getDineroJugador() {
        return jugador.getDinero();
    }

    public int getApuestaActual() {
        return jugador.getApuestaActual();
    }

    public boolean jugadorEnBancarrota() {
        return jugador.estaEnBancarrota();
    }

    public int getFichaMaxima() {
        return Jugador.getFichaMaxima();
    }

    public int getDineroInicial() {
        return Jugador.getDineroInicial();
    }

    // ===== MÉTODOS PARA PAUSAS DRAMÁTICAS =====
    private void pausa(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void pausaCorta() {
        pausa(1000); // 1 segundo
    }

    private void pausaLarga() {
        pausa(2000); // 2 segundos
    }

    // ===== MÉTODOS PRINCIPALES DEL JUEGO =====
    public void iniciarPartida() {
        System.out.println("=== NUEVA PARTIDA ===");
        System.out.println("Dinero disponible: $" + jugador.getDinero());
        System.out.println("Ficha máxima: $" + getFichaMaxima());

        // Limpiar manos y resetear estado
        jugador.getMano().limpiar();
        crupier.getMano().limpiar();
        jugador.setPlantado(false);
        juegoTerminado = false;

        // Solo reparte cartas si se hizo apuesta
        if (apuestaHecha) {
            repartirCartasIniciales();
            mostrarEstadoJuego();

            if (verificarBlackjackInicial()) {
                juegoTerminado = true;
                determinarGanador();
            }
        }
    }

    private void repartirCartasIniciales() {
        System.out.print("Repartiendo cartas");
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            pausaCorta();
        }
        System.out.println();

        // Repartir al jugador con pausas entre cartas
        Carta carta1 = mazo.repartirCarta();
        jugador.getMano().agregarCarta(carta1);
        System.out.println(jugador.getNombre() + " recibe: " + carta1);
        pausaCorta();

        Carta carta2 = mazo.repartirCarta();
        jugador.getMano().agregarCarta(carta2);
        System.out.println(jugador.getNombre() + " recibe: " + carta2);
        pausaCorta();

        // Repartir al crupier con pausas entre cartas
        Carta cartaCrupier1 = mazo.repartirCarta();
        crupier.getMano().agregarCarta(cartaCrupier1);
        pausaCorta();

        Carta cartaCrupier2 = mazo.repartirCarta();
        crupier.getMano().agregarCarta(cartaCrupier2);
        pausaCorta();

        System.out.print("Crupier muestra: ");
        pausaLarga();
        System.out.println(crupier.getMano().getCartas().get(0));
    }

    private void mostrarEstadoJuego() {
        System.out.println("\n--- Estado Actual ---");
        System.out.println(jugador.toString());

        if (crupier.getMano().getCartas().size() > 0) {
            System.out.println("Crupier muestra: " + crupier.getMano().getCartas().get(0));
        } else {
            System.out.println("Crupier muestra: [Carta oculta]");
        }
        System.out.println("---------------------");
    }

    private boolean verificarBlackjackInicial() {
        if (jugador.tieneBlackjack()) {
            System.out.println("¡BLACKJACK! " + jugador.getNombre() + " gana automáticamente.");
            return true;
        }
        if (crupier.tieneBlackjack()) {
            System.out.println("¡BLACKJACK del Crupier! La casa gana.");
            return true;
        }
        return false;
    }

    public void jugadorPideCarta() {
        if (!juegoTerminado && !jugador.isPlantado()) {
            System.out.print("Repartiendo carta");
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                pausaCorta();
            }
            System.out.println();

            Carta nuevaCarta = mazo.repartirCarta();
            jugador.getMano().agregarCarta(nuevaCarta);
            System.out.println(jugador.getNombre() + " recibe: " + nuevaCarta);

            mostrarEstadoJuego();

            if (jugador.sePasó()) {
                pausaLarga();
                System.out.println("¡Te pasaste de 21! Pierdes.");
                juegoTerminado = true;
                determinarGanador();
            }
        }
    }

    public void jugadorSePlanta() {
        if (!juegoTerminado) {
            jugador.plantarse();
            turnoCrupier();
        }
    }

    private void turnoCrupier() {
        System.out.println("\n--- Turno del Crupier ---");
        pausaLarga();

        crupier.jugar(mazo);
        determinarGanador();
    }

    // Modificado para pagos de casino
    private void determinarGanador() {
        System.out.println("\n=== RESULTADO FINAL ===");
        System.out.println(jugador.toString());
        System.out.println(crupier.toString());

        int valorJugador = jugador.getMano().calcularValor();
        int valorCrupier = crupier.getMano().calcularValor();

        // Sistema de pagos de casino
        if (jugador.sePasó()) {
            System.out.println("❌ " + jugador.getNombre() + " pierde por pasarse de 21.");
            // Pierde la apuesta (ya se descontó)
        }
        else if (crupier.sePasó()) {
            System.out.println("✅ " + jugador.getNombre() + " gana! El crupier se pasó de 21.");
            jugador.recibirPago(2.0);
        }
        else if (jugador.tieneBlackjack() && !crupier.tieneBlackjack()) {
            System.out.println("🎰 ¡BLACKJACK! " + jugador.getNombre() + " gana 3:2");
            jugador.recibirPago(2.5);
        }
        else if (valorJugador > valorCrupier) {
            System.out.println("✅ " + jugador.getNombre() + " gana con " + valorJugador + " contra " + valorCrupier);
            jugador.recibirPago(2.0);
        }
        else if (valorJugador < valorCrupier) {
            System.out.println("❌ " + jugador.getNombre() + " pierde con " + valorJugador + " contra " + valorCrupier);
            // Pierde la apuesta (ya se descontó)
        }
        else {
            System.out.println("🤝 Empate. Ambos tienen " + valorJugador);
            jugador.devolverApuesta();
        }

        juegoTerminado = true;
        apuestaHecha = false; // Resetear para próxima ronda
    }

    // ===== NUEVOS MÉTODOS PARA DOBLAR Y DIVIDIR =====
    public boolean puedeDoblar() {
        return jugador.getMano().getCartas().size() == 2 &&
                jugador.getDinero() >= jugador.getApuestaActual();
    }

    public boolean puedeDividir() {
        if (jugador.getMano().getCartas().size() != 2) return false;

        Carta primera = jugador.getMano().getCartas().get(0);
        Carta segunda = jugador.getMano().getCartas().get(1);

        return primera.getValor().equals(segunda.getValor()) &&
                jugador.getDinero() >= jugador.getApuestaActual();
    }

    public void doblarApuesta() {
        if (puedeDoblar()) {
            // Doblar la apuesta
            int apuestaActual = jugador.getApuestaActual();
            if (jugador.doblarApuesta()) {
                System.out.println("Apuesta doblada a $" + jugador.getApuestaActual());

                // El jugador recibe una sola carta adicional
                System.out.print("Repartiendo carta");
                for (int i = 0; i < 3; i++) {
                    System.out.print(".");
                    pausaCorta();
                }
                System.out.println();

                Carta nuevaCarta = mazo.repartirCarta();
                jugador.getMano().agregarCarta(nuevaCarta);
                System.out.println(jugador.getNombre() + " recibe: " + nuevaCarta);

                mostrarEstadoJuego();

                // Después de doblar, el jugador se planta automáticamente
                jugadorSePlanta();
            }
        }
    }

    public void dividir() {
        // Implementación básica para dividir
        System.out.println("Función de dividir próximamente disponible");
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Crupier getCrupier() {
        return crupier;
    }
}
