package com.blackjack.controller;

import com.blackjack.model.Carta;
import com.blackjack.model.Mazo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"apuestaAcumulada", "fichasSeleccionadas"})
public class BlackjackController {

    private Mazo mazo;
    private List<Carta> manoJugador;
    private List<Carta> manoCrupier;
    private boolean juegoTerminado;
    private String mensaje;
    private int saldo;
    private int apuestaActual;
    private boolean apuestaRealizada;

    // Variables de sesión
    private int apuestaAcumulada;
    private List<Integer> fichasSeleccionadas;

    public BlackjackController() {
        this.apuestaAcumulada = 0;
        this.fichasSeleccionadas = new ArrayList<>();
        iniciarJuegoCompleto();
    }

    @ModelAttribute("apuestaAcumulada")
    public int getApuestaAcumulada() {
        return apuestaAcumulada;
    }

    @ModelAttribute("fichasSeleccionadas")
    public List<Integer> getFichasSeleccionadas() {
        return fichasSeleccionadas;
    }

    private void iniciarJuegoCompleto() {
        this.mazo = new Mazo();
        this.manoJugador = new ArrayList<>();
        this.manoCrupier = new ArrayList<>();
        this.saldo = 1000;
        this.apuestaActual = 0;
        this.juegoTerminado = false;
        this.apuestaRealizada = false;
        this.mensaje = "Bienvenido al Blackjack! Selecciona fichas para apostar.";

        repartirCartasIniciales();
    }

    private void repartirCartasIniciales() {
        manoJugador.clear();
        manoCrupier.clear();

        mazo.barajar();

        manoJugador.add(mazo.repartirCarta());
        manoJugador.add(mazo.repartirCarta());
        manoCrupier.add(mazo.repartirCarta());
        manoCrupier.add(mazo.repartirCarta());

        juegoTerminado = false;
        apuestaRealizada = false;
    }

    private int calcularPuntuacion(List<Carta> mano) {
        int puntuacion = 0;
        int ases = 0;

        for (Carta carta : mano) {
            int valor = carta.getValorNumerico();
            if (valor == 11) {
                ases++;
                puntuacion += 11;
            } else {
                puntuacion += valor;
            }
        }

        while (puntuacion > 21 && ases > 0) {
            puntuacion -= 10;
            ases--;
        }

        return puntuacion;
    }

    private void determinarGanador() {
        int puntuacionJugador = calcularPuntuacion(manoJugador);
        int puntuacionCrupier = calcularPuntuacion(manoCrupier);

        if (puntuacionJugador > 21) {
            mensaje = "Te pasaste de 21! Pierdes $" + apuestaActual;
            saldo -= apuestaActual;
        } else if (puntuacionCrupier > 21) {
            mensaje = "Crupier se paso! Ganas $" + apuestaActual;
            saldo += apuestaActual;
        } else if (puntuacionJugador > puntuacionCrupier) {
            mensaje = "Ganaste! +$" + apuestaActual;
            saldo += apuestaActual;
        } else if (puntuacionCrupier > puntuacionJugador) {
            mensaje = "Crupier gana! -$" + apuestaActual;
            saldo -= apuestaActual;
        } else {
            mensaje = "Empate! Recuperas tu apuesta";
        }
    }

    @GetMapping("/")
    public String mostrarMesa(Model model) {
        model.addAttribute("manoJugador", manoJugador);
        model.addAttribute("manoCrupier", manoCrupier);
        model.addAttribute("puntuacionJugador", calcularPuntuacion(manoJugador));
        model.addAttribute("puntuacionCrupier", juegoTerminado ? calcularPuntuacion(manoCrupier) : 0);
        model.addAttribute("juegoTerminado", juegoTerminado);
        model.addAttribute("mensaje", mensaje);
        model.addAttribute("saldo", saldo);
        model.addAttribute("apuestaActual", apuestaActual);
        model.addAttribute("apuestaAcumulada", apuestaAcumulada);
        model.addAttribute("fichasSeleccionadas", fichasSeleccionadas);
        model.addAttribute("apuestaRealizada", apuestaRealizada);

        return "mesa";
    }

    @PostMapping("/agregar-ficha")
    public String agregarFicha(@RequestParam int valorFicha, Model model) {
        if (!juegoTerminado && !apuestaRealizada) {
            int nuevaApuesta = apuestaAcumulada + valorFicha;

            if (nuevaApuesta <= saldo && nuevaApuesta <= 1000) {
                apuestaAcumulada = nuevaApuesta;
                fichasSeleccionadas.add(valorFicha);
                mensaje = "Apuesta acumulada: $" + apuestaAcumulada + " - Selecciona mas fichas o confirma";
            } else {
                mensaje = "No puedes apostar mas de $1000 o mas de tu saldo!";
            }
        }

        // Actualizar atributos de sesión
        model.addAttribute("apuestaAcumulada", apuestaAcumulada);
        model.addAttribute("fichasSeleccionadas", fichasSeleccionadas);

        return "redirect:/";
    }

    @PostMapping("/quitar-ficha")
    public String quitarFicha(@RequestParam int index, Model model) {
        if (!juegoTerminado && !apuestaRealizada && index >= 0 && index < fichasSeleccionadas.size()) {
            int ficha = fichasSeleccionadas.remove(index);
            apuestaAcumulada -= ficha;
            mensaje = "Apuesta acumulada: $" + apuestaAcumulada;
        }

        model.addAttribute("apuestaAcumulada", apuestaAcumulada);
        model.addAttribute("fichasSeleccionadas", fichasSeleccionadas);

        return "redirect:/";
    }

    @PostMapping("/confirmar-apuesta")
    public String confirmarApuesta(Model model) {
        if (!juegoTerminado && !apuestaRealizada && apuestaAcumulada >= 10) {
            apuestaActual = apuestaAcumulada;
            apuestaRealizada = true;
            mensaje = "Apuesta de $" + apuestaActual + " confirmada! Buena suerte!";

            if (calcularPuntuacion(manoJugador) == 21) {
                juegoTerminado = true;
                saldo += apuestaActual * 1.5;
                mensaje = "Blackjack Natural! Ganas $" + (int)(apuestaActual * 1.5);
            }
        } else if (apuestaAcumulada < 10) {
            mensaje = "Apuesta minima: $10";
        }

        return "redirect:/";
    }

    @PostMapping("/limpiar-apuesta")
    public String limpiarApuesta(Model model) {
        if (!juegoTerminado && !apuestaRealizada) {
            apuestaAcumulada = 0;
            fichasSeleccionadas.clear();
            mensaje = "Apuesta limpiada. Selecciona nuevas fichas.";
        }

        model.addAttribute("apuestaAcumulada", apuestaAcumulada);
        model.addAttribute("fichasSeleccionadas", fichasSeleccionadas);

        return "redirect:/";
    }

    @PostMapping("/pedir")
    public String pedirCarta() {
        if (!juegoTerminado && apuestaRealizada) {
            manoJugador.add(mazo.repartirCarta());
            int puntuacion = calcularPuntuacion(manoJugador);

            if (puntuacion > 21) {
                juegoTerminado = true;
                determinarGanador();
            } else if (puntuacion == 21) {
                return plantarse();
            } else {
                mensaje = "Puntuacion: " + puntuacion + " - Otra carta?";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/plantarse")
    public String plantarse() {
        if (!juegoTerminado && apuestaRealizada) {
            while (calcularPuntuacion(manoCrupier) < 17) {
                manoCrupier.add(mazo.repartirCarta());
            }

            juegoTerminado = true;
            determinarGanador();
        }
        return "redirect:/";
    }

    @PostMapping("/doblar")
    public String doblar() {
        if (!juegoTerminado && apuestaRealizada && apuestaActual * 2 <= saldo) {
            apuestaActual *= 2;
            manoJugador.add(mazo.repartirCarta());

            juegoTerminado = true;

            while (calcularPuntuacion(manoCrupier) < 17) {
                manoCrupier.add(mazo.repartirCarta());
            }

            determinarGanador();
        }
        return "redirect:/";
    }

    @PostMapping("/nueva-partida")
    public String nuevaPartida() {
        if (juegoTerminado) {
            if (saldo <= 0) {
                saldo = 1000;
                mensaje = "Credito renovado! $1000";
            }
            apuestaActual = 0;
            apuestaRealizada = false;
            apuestaAcumulada = 0;
            fichasSeleccionadas.clear();
            repartirCartasIniciales();
        }
        return "redirect:/";
    }
}