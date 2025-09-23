package com.blackjack.controller;

import com.blackjack.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlackjackController {

    @Autowired
    private BlackjackService blackjackService;

    @GetMapping("/")
    public String mostrarMesa(Model model) {
        model.addAttribute("mensaje", blackjackService.getMensajeResultado());
        model.addAttribute("estadoJugador", blackjackService.getEstadoJugador());
        model.addAttribute("estadoCrupier", blackjackService.getEstadoCrupier());
        model.addAttribute("juegoTerminado", blackjackService.isJuegoTerminado());
        return "mesa";
    }

    @PostMapping("/pedir")
    public String pedirCarta() {
        blackjackService.jugadorPideCarta();
        return "redirect:/";
    }

    @PostMapping("/plantarse")
    public String plantarse() {
        blackjackService.jugadorSePlanta();
        return "redirect:/";
    }

    @PostMapping("/reiniciar")
    public String reiniciar() {
        blackjackService.reiniciarJuego();
        return "redirect:/";
    }
}