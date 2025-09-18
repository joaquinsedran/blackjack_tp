package com.blackjack.controller;

import com.blackjack.service.BlackjackService;
import com.blackjack.view.ConsolaView;

public class BlackjackController {
    private BlackjackService blackjackService;
    private ConsolaView consolaView;

    public BlackjackController() {
        this.blackjackService = new BlackjackService();
        this.consolaView = new ConsolaView();
    }

    public void iniciarJuego() {
        consolaView.mostrarBienvenida();

        boolean jugarOtraVez = true;

        while (jugarOtraVez && !blackjackService.jugadorEnBancarrota()) {
            // FASE 1: APUESTA
            boolean apuestaValida = false;
            while (!apuestaValida) {
                int apuesta = consolaView.pedirApuesta(
                        blackjackService.getDineroJugador(),
                        blackjackService.getFichaMaxima()
                );

                if (apuesta > 0) {
                    apuestaValida = blackjackService.hacerApuesta(apuesta);
                    if (!apuestaValida) {
                        consolaView.mostrarMensaje("❌ Apuesta no válida. Intenta nuevamente.");
                    }
                }
            }

            // FASE 2: JUEGO
            blackjackService.iniciarPartida();

            while (!blackjackService.isJuegoTerminado()) {
                consolaView.mostrarOpciones();
                int opcion = consolaView.leerOpcion();

                switch (opcion) {
                    case 1: // Pedir carta
                        blackjackService.jugadorPideCarta();
                        break;
                    case 2: // Plantarse
                        blackjackService.jugadorSePlanta();
                        break;
                    case 3: // Ver reglas
                        consolaView.mostrarReglas();
                        break;
                    default:
                        consolaView.mostrarMensaje("❌ Opción inválida. Intenta nuevamente.");
                }
            }

            // Verificar si el jugador quedó en bancarrota
            if (blackjackService.jugadorEnBancarrota()) {
                consolaView.mostrarBancarrota();
                break;
            }

            // Preguntar si quiere jugar otra vez
            consolaView.mostrarDinero(blackjackService.getDineroJugador());
            jugarOtraVez = consolaView.preguntarJugarOtraVez();

            if (jugarOtraVez) {
                // Reiniciar el servicio para nueva partida
                blackjackService = new BlackjackService();
            }
        }

        // Mensaje final
        if (blackjackService.jugadorEnBancarrota()) {
            consolaView.mostrarBancarrota();
        } else {
            consolaView.mostrarDespedida();
        }

        consolaView.cerrar();
    }
}