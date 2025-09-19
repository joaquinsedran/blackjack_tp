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

            blackjackService.iniciarPartida();

            while (!blackjackService.isJuegoTerminado()) {
                consolaView.mostrarOpciones(
                        blackjackService.puedeDoblar(),
                        blackjackService.puedeDividir()
                );

                int opcion = consolaView.leerOpcion();

                switch (opcion) {
                    case 1:
                        blackjackService.jugadorPideCarta();
                        break;
                    case 2:
                        blackjackService.jugadorSePlanta();
                        break;
                    case 3:
                        if (blackjackService.puedeDoblar()) {
                            blackjackService.doblarApuesta();
                        } else {
                            consolaView.mostrarMensaje("❌ No puedes doblar en este momento.");
                        }
                        break;
                    case 4:
                        if (blackjackService.puedeDividir()) {
                            blackjackService.dividir();
                        } else {
                            consolaView.mostrarMensaje("❌ No puedes dividir en este momento.");
                        }
                        break;
                    case 5:
                        consolaView.mostrarReglas();
                        break;
                    default:
                        consolaView.mostrarMensaje("❌ Opción inválida. Intenta nuevamente.");
                }
            }

            if (blackjackService.jugadorEnBancarrota()) {
                consolaView.mostrarBancarrota();
                break;
            }

            consolaView.mostrarDinero(blackjackService.getDineroJugador());
            jugarOtraVez = consolaView.preguntarJugarOtraVez();

            if (jugarOtraVez) {
                blackjackService = new BlackjackService();
            }
        }

        if (blackjackService.jugadorEnBancarrota()) {
            consolaView.mostrarBancarrota();
        } else {
            consolaView.mostrarDespedida();
        }

        consolaView.cerrar();
    }
}