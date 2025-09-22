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

    // ===== MÉTODO PRINCIPAL CORREGIDO =====
    public boolean iniciarPartida(int apuesta) {
        // Primero validar la apuesta
        if (!hacerApuesta(apuesta)) {
            System.out.println("❌ No se pudo hacer la apuesta de $" + apuesta);
            return false;
        }

        System.out.println("=== NUEVA PARTIDA ===");
        System.out.println("Apuesta: $" + jugador.getApuestaActual());

        // Limpiar manos y resetear estado
        jugador.getMano().limpiar();
        crupier.getMano().limpiar();
        jugador.setPlantado(false);
        juegoTerminado = false;
        apuestaHecha = true;

        // ✅ EFECTUAR LA APUESTA (descontar el dinero)
        jugador.efectuarApuesta();

        repartirCartasIniciales();
        mostrarEstadoJuego();

        if (verificarBlackjackInicial()) {
            juegoTerminado = true;
            determinarGanador();
        }

        return true;
    }

    // ===== MÉTODOS DE APUESTAS CORREGIDOS =====
    public boolean hacerApuesta(int cantidad) {
        // ✅ Solo validar la apuesta, no descontar dinero
        return jugador.apostar(cantidad);
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

    public Crupier getCrupier() {
        return crupier;
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
        pausa(1000);
    }

    private void pausaLarga() {
        pausa(2000);
    }

    // ===== MÉTODOS DE JUEGO =====
    private void repartirCartasIniciales() {
        System.out.print("Repartiendo cartas");
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            pausaCorta();
        }
        System.out.println();

        // Repartir al jugador
        Carta carta1 = mazo.repartirCarta();
        jugador.getMano().agregarCarta(carta1);
        System.out.println(jugador.getNombre() + " recibe: " + carta1);
        pausaCorta();

        Carta carta2 = mazo.repartirCarta();
        jugador.getMano().agregarCarta(carta2);
        System.out.println(jugador.getNombre() + " recibe: " + carta2);
        pausaCorta();

        // Repartir al crupier
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
        System.out.println("Crupier muestra: " + crupier.getMano().getCartas().get(0));
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

        // Mostrar la carta oculta del crupier
        System.out.println("Crupier muestra su carta oculta: " + crupier.getMano().getCartas().get(1));
        System.out.println("Crupier: " + crupier.toString());
        pausaLarga();

        crupier.jugar(mazo);
        determinarGanador();
    }

    private void determinarGanador() {
        System.out.println("\n=== RESULTADO FINAL ===");
        System.out.println(jugador.toString());
        System.out.println(crupier.toString());

        int valorJugador = jugador.getMano().calcularValor();
        int valorCrupier = crupier.getMano().calcularValor();

        // ✅ SISTEMA DE PAGOS CORREGIDO - CASINO REAL
        if (jugador.sePasó()) {
            System.out.println("❌ " + jugador.getNombre() + " pierde por pasarse de 21.");
            jugador.perderApuesta();
        }
        else if (crupier.sePasó()) {
            System.out.println("✅ " + jugador.getNombre() + " gana! El crupier se pasó de 21.");
            jugador.recibirPago(2.0); // ✅ Gana 1:1 (apuesta + igual cantidad)
        }
        else if (jugador.tieneBlackjack() && !crupier.tieneBlackjack()) {
            System.out.println("🎰 ¡BLACKJACK! " + jugador.getNombre() + " gana 3:2");
            jugador.recibirPago(2.5); // ✅ Blackjack paga 3:2 (apuesta + 1.5 veces)
        }
        else if (valorJugador > valorCrupier) {
            System.out.println("✅ " + jugador.getNombre() + " gana con " + valorJugador + " contra " + valorCrupier);
            jugador.recibirPago(2.0); // ✅ Gana 1:1 (apuesta + igual cantidad)
        }
        else if (valorJugador < valorCrupier) {
            System.out.println("❌ " + jugador.getNombre() + " pierde con " + valorJugador + " contra " + valorCrupier);
            jugador.perderApuesta();
        }
        else {
            System.out.println("🤝 Empate. Ambos tienen " + valorJugador);
            jugador.devolverApuesta(); // ✅ Recupera su apuesta
        }

        juegoTerminado = true;
        apuestaHecha = false;

        // ✅ Mostrar saldo final actualizado
        System.out.println("💰 Saldo final: $" + jugador.getDinero());
    }

    // ===== MÉTODOS PARA DOBLAR Y DIVIDIR =====
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
            if (jugador.doblarApuesta()) {
                System.out.println("🎰 Apuesta doblada a $" + jugador.getApuestaActual());

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
        if (puedeDividir()) {
            System.out.println("🎴 Función de dividir activada");
            // Implementación básica de división
            Carta primeraCarta = jugador.getMano().getCartas().get(0);
            Carta segundaCarta = jugador.getMano().getCartas().get(1);

            System.out.println("Dividiendo " + primeraCarta + " y " + segundaCarta);
            System.out.println("⚠️  Función de dividir completa próximamente disponible");
            System.out.println("Por ahora, continuamos con la mano actual...");
        } else {
            System.out.println("❌ No puedes dividir en este momento");
        }
    }

    // ===== GETTERS Y MÉTODOS ADICIONALES =====
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public boolean getMazoNecesitaReinicio() {
        return mazo.getCartasRestantes() < 20;
    }

    public void reiniciarMazo() {
        this.mazo = new Mazo();
        System.out.println("🃏 Mazo reiniciado - ¡Cartas nuevas!");
    }

    // ===== MÉTODO PARA REINICIAR JUEGO (OPCIONAL) =====
    public void reiniciarJuego() {
        this.mazo = new Mazo();
        this.jugador = new Jugador("Jugador 1");
        this.crupier = new Crupier();
        this.juegoTerminado = false;
        this.apuestaHecha = false;
        System.out.println("🔄 Juego reiniciado - ¡Buena suerte!");
    }
}