package com.blackjack.service;

import org.springframework.stereotype.Service;

@Service
public class BlackjackService {
    private int contadorCartas = 0;
    private boolean juegoTerminado = false;
    private String mensaje = "¡Blackjack funcionando!";

    public String getMensajeResultado() {
        return mensaje;
    }

    public String getEstadoJugador() {
        return "Jugador: " + (2 + contadorCartas) + " cartas - " + (17 + contadorCartas) + " puntos";
    }

    public String getEstadoCrupier() {
        return "Crupier: 1 carta - ? puntos";
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void jugadorPideCarta() {
        if (!juegoTerminado) {
            contadorCartas++;
            mensaje = "Pediste carta #" + contadorCartas;

            if (17 + contadorCartas > 21) {
                juegoTerminado = true;
                mensaje = "¡Te pasaste de 21! Pierdes.";
            }
        }
    }

    public void jugadorSePlanta() {
        if (!juegoTerminado) {
            juegoTerminado = true;
            int puntosCrupier = 16 + (int)(Math.random() * 10);
            int puntosJugador = 17 + contadorCartas;

            if (puntosCrupier > 21) {
                mensaje = "¡Ganas! Crupier se pasó de 21.";
            } else if (puntosJugador > puntosCrupier) {
                mensaje = "¡Ganas! " + puntosJugador + " vs " + puntosCrupier;
            } else if (puntosCrupier > puntosJugador) {
                mensaje = "Pierdes. " + puntosJugador + " vs " + puntosCrupier;
            } else {
                mensaje = "Empate. " + puntosJugador + " vs " + puntosCrupier;
            }
        }
    }

    public void reiniciarJuego() {
        contadorCartas = 0;
        juegoTerminado = false;
        mensaje = "¡Nueva partida!";
    }
}