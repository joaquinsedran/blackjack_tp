package com.blackjack.service;

import com.blackjack.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlackjackService {
    private Mazo mazo;
    private Mano manoCrupier;
    private List<Mano> manosJugador;
    private int manoActivaIndex;
    private boolean juegoActivo;
    private int saldoJugador;

    public BlackjackService() {
        this.saldoJugador = 1000;
        reiniciarPartida();
    }

    private void reiniciarPartida() {
        this.mazo = new Mazo();
        this.manoCrupier = new Mano();
        this.manosJugador = new ArrayList<>();
        this.manosJugador.add(new Mano());
        this.manoActivaIndex = 0;
        this.juegoActivo = false;
    }

    public JuegoEstado iniciarNuevaPartida(int apuesta) {
        if (apuesta <= 0 || apuesta > saldoJugador) {
            JuegoEstado estadoError = obtenerEstadoActual();
            estadoError.setMensaje("Apuesta inválida o saldo insuficiente.");
            return estadoError;
        }

        reiniciarPartida();
        saldoJugador -= apuesta;
        manosJugador.get(0).setApuesta(apuesta);

        manosJugador.get(0).agregarCarta(mazo.sacarCarta());
        manoCrupier.agregarCarta(mazo.sacarCarta());
        manosJugador.get(0).agregarCarta(mazo.sacarCarta());
        manoCrupier.agregarCarta(mazo.sacarCarta());

        juegoActivo = true;

        if (manosJugador.get(0).esBlackjack()) {
            return finalizarPartidaPorBlackjack();
        }

        JuegoEstado estado = obtenerEstadoActual();
        estado.setMensaje("Es tu turno. ¿Pedir o plantarse?");
        return estado;
    }

    public JuegoEstado jugadorPideCarta() {
        if (!juegoActivo) return obtenerEstadoActual();

        Mano manoActual = manosJugador.get(manoActivaIndex);
        manoActual.agregarCarta(mazo.sacarCarta());

        if (manoActual.sePaso()) {
            manoActual.setCompletada(true);
            juegoActivo = false;
            return turnoCrupier();
        }

        return obtenerEstadoActual();
    }

    public JuegoEstado jugadorSePlanta() {
        if (!juegoActivo) return obtenerEstadoActual();

        Mano manoActual = manosJugador.get(manoActivaIndex);
        manoActual.setCompletada(true);
        manoActivaIndex++;

        if (manoActivaIndex >= manosJugador.size()) {
            return turnoCrupier();
        }

        return obtenerEstadoActual();
    }

    public JuegoEstado jugadorDobla() {
        if (!juegoActivo) return obtenerEstadoActual();

        Mano manoActual = manosJugador.get(manoActivaIndex);

        if (!manoActual.puedeDoblar() || saldoJugador < manoActual.getApuesta()) {
            JuegoEstado estadoError = obtenerEstadoActual();
            estadoError.setMensaje("No puedes doblar en este momento.");
            return estadoError;
        }

        saldoJugador -= manoActual.getApuesta();
        manoActual.setApuesta(manoActual.getApuesta() * 2);

        manoActual.agregarCarta(mazo.sacarCarta());

        manoActual.setCompletada(true);
        juegoActivo = false;

        return turnoCrupier();
    }

    private JuegoEstado turnoCrupier() {
        juegoActivo = false;

        while (manoCrupier.getPuntos() < 17) {
            manoCrupier.agregarCarta(mazo.sacarCarta());
        }

        return determinarGanador();
    }

    private JuegoEstado determinarGanador() {
        for (Mano manoJugador : manosJugador) {
            if (manoJugador.sePaso()) {
            } else if (manoCrupier.sePaso() || manoJugador.getPuntos() > manoCrupier.getPuntos()) {
                saldoJugador += manoJugador.getApuesta() * 2;
            } else if (manoJugador.getPuntos() == manoCrupier.getPuntos()) {
                saldoJugador += manoJugador.getApuesta();
            }
        }

        JuegoEstado estadoFinal = obtenerEstadoActual();
        estadoFinal.setMensaje(generarMensajeFinal());
        return estadoFinal;
    }

    private JuegoEstado finalizarPartidaPorBlackjack() {
        juegoActivo = false;
        Mano manoJugador = manosJugador.get(0);
        saldoJugador += (int)(manoJugador.getApuesta() * 2.5);

        JuegoEstado estado = obtenerEstadoActual();
        estado.setMensaje("¡Blackjack! ¡Ganaste!");
        return estado;
    }

    public JuegoEstado obtenerEstadoActual() {
        JuegoEstado estado = new JuegoEstado();
        estado.setJuegoActivo(juegoActivo);
        estado.setSaldoJugador(saldoJugador);

        estado.setManosJugador(new ArrayList<>());
        for(Mano mano : manosJugador) {
            estado.getManosJugador().add(new Mano(mano));
        }

        estado.setManoActivaIndex(manoActivaIndex);
        estado.setCartasCrupier(new ArrayList<>(manoCrupier.getCartas()));

        if (juegoActivo && !manoCrupier.getCartas().isEmpty()) {
            estado.setPuntosCrupier(manoCrupier.getCartas().get(0).getValorNumerico());
        } else {
            estado.setPuntosCrupier(manoCrupier.getPuntos());
        }

        if (juegoActivo && !manosJugador.isEmpty()) {
            Mano manoActiva = manosJugador.get(manoActivaIndex);
            boolean puedePagar = saldoJugador >= manoActiva.getApuesta();
            estado.setPuedeDoblar(manoActiva.puedeDoblar() && puedePagar);
        } else {
            estado.setPuedeDoblar(false);
        }

        return estado;
    }

    private String generarMensajeFinal() {
        Mano manoJugador = manosJugador.get(0);
        if (manoJugador.sePaso()) {
            return "¡Te pasaste de 21! Gana el crupier.";
        }
        if (manoCrupier.sePaso()) {
            return "¡Crupier se pasó de 21! ¡Ganaste!";
        }
        if (manoJugador.getPuntos() > manoCrupier.getPuntos()) {
            return "¡Felicidades! ¡Ganaste!";
        }
        if (manoCrupier.getPuntos() > manoJugador.getPuntos()) {
            return "Gana el crupier.";
        }
        return "¡Empate!";
    }
}

