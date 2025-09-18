package com.blackjack.service;

import com.blackjack.model.*;

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

    // ===== MÉTODOS MODIFICADOS CON PAGOS DE CASINO =====
    public void iniciarPartida() {
        System.out.println("=== NUEVA PARTIDA ===");
        System.out.println("Dinero inicial: $" + getDineroInicial());
        System.out.println("Ficha máxima: $" + getFichaMaxima());

        jugador.resetearApuesta();
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
            // Pierde la apuesta
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
            // Pierde la apuesta
        }
        else {
            System.out.println("🤝 Empate. Ambos tienen " + valorJugador);
            jugador.devolverApuesta();
        }

        juegoTerminado = true;
        apuestaHecha = false; // Resetear para próxima ronda
    }

    // ===== MÉTODOS EXISTENTES CORREGIDOS =====
    private void repartirCartasIniciales() {
        // Limpiar manos anteriores
        jugador.getMano().getCartas().clear();
        crupier.getMano().getCartas().clear();

        // Repartir 2 cartas al jugador
        jugador.getMano().agregarCarta(mazo.repartirCarta());
        jugador.getMano().agregarCarta(mazo.repartirCarta());

        // Repartir 2 cartas al crupier
        crupier.getMano().agregarCarta(mazo.repartirCarta());
        crupier.getMano().agregarCarta(mazo.repartirCarta());

        System.out.println("Cartas repartidas. Crupier muestra: " + crupier.getMano().getCartas().get(0));
    }

    private void mostrarEstadoJuego() {
        System.out.println("\n--- Estado Actual ---");
        System.out.println(jugador.toString());

        // VERIFICAR que el crupier tenga cartas antes de mostrarlas
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
            jugador.pedirCarta(mazo);
            mostrarEstadoJuego();

            if (jugador.sePasó()) {
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
        crupier.jugar(mazo);
        determinarGanador();
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