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
            int apuesta = 0;

            while (!apuestaValida) {
                apuesta = consolaView.pedirApuesta(
                        blackjackService.getDineroJugador(),
                        blackjackService.getFichaMaxima()
                );

                if (apuesta > 0) {
                    consolaView.mostrarApuesta(apuesta);

                    apuestaValida = blackjackService.hacerApuesta(apuesta);
                    if (!apuestaValida) {
                        consolaView.mostrarMensaje("❌ Apuesta no válida. Verifica que:");
                        consolaView.mostrarMensaje("   - Sea múltiplo de 5");
                        consolaView.mostrarMensaje("   - No exceda tu dinero disponible ($" + blackjackService.getDineroJugador() + ")");
                    }
                } else {
                    consolaView.mostrarMensaje("❌ La apuesta debe ser mayor a 0.");
                }
            }

            consolaView.mostrarMensaje("✅ Apuesta aceptada: $" + apuesta);

            boolean partidaIniciada = blackjackService.iniciarPartida(apuesta);

            if (!partidaIniciada) {
                consolaView.mostrarMensaje("❌ No se pudo iniciar la partida.");
                continue;
            }

            // Bucle principal del juego
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

            // ✅ MOSTRAR DINERO ACTUALIZADO (se acumula entre partidas)
            consolaView.mostrarDinero(blackjackService.getDineroJugador());

            // Preguntar si quiere jugar otra partida
            jugarOtraVez = consolaView.preguntarJugarOtraVez();

            if (jugarOtraVez) {
                // ✅ NO reiniciar el servicio - mantener el dinero acumulado
                // Solo reiniciar el mazo si es necesario
                if (blackjackService.getMazoNecesitaReinicio()) {
                    blackjackService.reiniciarMazo();
                    consolaView.mostrarMensaje("🃏 Mazo reiniciado para nueva partida");
                }
                consolaView.mostrarMensaje("🎰 ¡Nueva partida iniciada!");
            }
        }

        if (!blackjackService.jugadorEnBancarrota()) {
            consolaView.mostrarDespedida();
        }

        consolaView.cerrar();
    }

    public static void main(String[] args) {
        BlackjackController controller = new BlackjackController();
        controller.iniciarJuego();
    }
}