package com.blackjack.service;

import com.blackjack.model.*;
import java.util.ArrayList;
import java.util.List;

public class BlackjackService {
    private Mazo mazo;
    private Jugador jugador;
    private Crupier crupier;
    private boolean juegoActivo;
    private int saldoJugador;
    private int apuestaActual;

    public BlackjackService() {
        System.out.println("=== INICIANDO BLACKJACK SERVICE ===");
        reiniciarJuego();
    }

    public void reiniciarJuego() {
        this.mazo = new Mazo();
        this.jugador = new Jugador("Jugador");
        this.crupier = new Crupier();
        this.juegoActivo = false;
        this.saldoJugador = 1000;
        this.apuestaActual = 0;
        System.out.println("=== JUEGO REINICIADO ===");
    }

    public JuegoEstado iniciarNuevaPartida(int apuesta) {
        reiniciarJuego();

        if (apuesta > saldoJugador || apuesta <= 0) {
            JuegoEstado estadoError = obtenerEstadoActual();
            estadoError.setMensaje("Apuesta inválida");
            return estadoError;
        }

        mazo.barajar();

        Carta carta1Jugador = mazo.sacarCarta();
        Carta carta1Crupier = mazo.sacarCarta();
        Carta carta2Jugador = mazo.sacarCarta();
        Carta carta2Crupier = mazo.sacarCarta();

        jugador.recibirCarta(carta1Jugador);
        crupier.recibirCarta(carta1Crupier);
        jugador.recibirCarta(carta2Jugador);
        crupier.recibirCarta(carta2Crupier);

        juegoActivo = true;
        apuestaActual = apuesta;
        saldoJugador -= apuesta;

        JuegoEstado estado = obtenerEstadoActual();
        estado.setMensaje("Nueva partida iniciada. ¿Pedir carta o plantarse?");

        if (jugador.tieneBlackjack()) {
            estado.setMensaje("¡BLACKJACK! ¡Ganaste!");
            saldoJugador += apuestaActual * 3;
            juegoActivo = false;
            estado.setJuegoActivo(false);
            estado.setSaldoJugador(saldoJugador);
        }

        return estado;
    }

    public JuegoEstado obtenerEstadoActual() {
        JuegoEstado estado = new JuegoEstado();
        estado.setMensaje(juegoActivo ? "Tu turno - ¿Pedir carta o plantarse?" : "¡Bienvenido al Blackjack!");
        estado.setSaldoJugador(saldoJugador);
        estado.setApuestaActual(apuestaActual);
        estado.setJuegoActivo(juegoActivo);

        estado.setCartasCrupier(new ArrayList<>(crupier.getMano()));

        if (juegoActivo && crupier.getMano().size() >= 2) {
            estado.setPuntosCrupier(crupier.getMano().get(0).getValorNumerico());
        } else {
            estado.setPuntosCrupier(crupier.getPuntuacion());
        }

        ArrayList<Mano> manos = new ArrayList<>();
        Mano manoPrincipal = new Mano();

        for (Carta carta : jugador.getMano()) {
            manoPrincipal.agregarCarta(carta);
        }

        manos.add(manoPrincipal);
        estado.setManosJugador(manos);

        return estado;
    }

    public JuegoEstado jugadorPideCarta() {
        if (!juegoActivo) {
            return obtenerEstadoActual();
        }

        Carta nuevaCarta = mazo.sacarCarta();
        jugador.recibirCarta(nuevaCarta);

        JuegoEstado estado = obtenerEstadoActual();

        if (jugador.sePasó()) {
            estado.setMensaje("¡Te pasaste de 21! Has perdido.");
            juegoActivo = false;
            estado.setJuegoActivo(false);
        } else if (jugador.getPuntuacion() == 21) {
            estado.setMensaje("¡Tienes 21! ¿Te plantas?");
        } else {
            estado.setMensaje("Carta recibida. ¿Otra carta o te plantas?");
        }

        return estado;
    }

    public JuegoEstado jugadorSePlanta() {
        if (!juegoActivo) {
            return obtenerEstadoActual();
        }

        juegoActivo = false;

        turnoCrupier();
        String resultado = determinarGanador();

        JuegoEstado estado = obtenerEstadoActual();
        estado.setJuegoActivo(false);
        estado.setMensaje(resultado);

        return estado;
    }

    private void turnoCrupier() {
        System.out.println("=== INICIO TURNO CRUPIER ===");
        System.out.println("Cartas crupier: " + crupier.getMano());
        System.out.println("Puntos crupier: " + crupier.getPuntuacion());
        System.out.println("Debe pedir carta: " + crupier.debePedirCarta());

        int contadorCartas = 0;
        while (crupier.debePedirCarta() && !crupier.sePasó()) {
            Carta nuevaCarta = mazo.sacarCarta();
            if (nuevaCarta != null) {
                System.out.println("Crupier recibe carta " + (++contadorCartas) + ": " + nuevaCarta.getValor());
                crupier.recibirCarta(nuevaCarta);
                System.out.println("Nuevos puntos: " + crupier.getPuntuacion());
            } else {
                System.out.println("¡No hay más cartas en el mazo!");
                break;
            }
        }

        System.out.println("=== FIN TURNO CRUPIER ===");
        System.out.println("Puntos finales: " + crupier.getPuntuacion());
        System.out.println("Cartas finales: " + crupier.getMano());
    }

    private String determinarGanador() {
        int puntosJugador = jugador.getPuntuacion();
        int puntosCrupier = crupier.getPuntuacion();

        if (jugador.sePasó()) {
            return "¡Te pasaste de 21! Gana el crupier.";
        } else if (crupier.sePasó()) {
            saldoJugador += apuestaActual * 2;
            return "¡Crupier se pasó de 21! ¡Ganaste!";
        } else if (puntosJugador > puntosCrupier) {
            saldoJugador += apuestaActual * 2;
            return "¡Felicidades! ¡Ganaste!";
        } else if (puntosCrupier > puntosJugador) {
            return "Gana el crupier.";
        } else {
            saldoJugador += apuestaActual;
            return "¡Empate! Se devuelve tu apuesta.";
        }
    }

    public JuegoEstado seleccionarFicha(int valor) {
        JuegoEstado estado = obtenerEstadoActual();
        estado.setMensaje("Ficha de $" + valor + " seleccionada");
        estado.setApuestaActual(valor);
        return estado;
    }
}