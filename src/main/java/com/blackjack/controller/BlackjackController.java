package com.blackjack.controller;

import com.blackjack.model.JuegoEstado;
import com.blackjack.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/blackjack")
@CrossOrigin(origins = "*")
public class BlackjackController {

    private final BlackjackService blackjackService;

    @Autowired
    public BlackjackController(BlackjackService blackjackService) {
        this.blackjackService = blackjackService;
    }

    @GetMapping("/estado")
    public JuegoEstado obtenerEstado() {
        return blackjackService.obtenerEstadoActual();
    }

    @PostMapping("/iniciar")
    public JuegoEstado iniciarPartida(@RequestBody Map<String, Integer> payload) {
        int apuesta = payload.get("apuesta");
        return blackjackService.iniciarNuevaPartida(apuesta);
    }

    @PostMapping("/pedir")
    public JuegoEstado pedirCarta() {
        return blackjackService.jugadorPideCarta();
    }

    @PostMapping("/plantarse")
    public JuegoEstado plantarse() {
        return blackjackService.jugadorSePlanta();
    }
}

