package com.blackjack.controller;

import com.blackjack.model.JuegoEstado;
import com.blackjack.service.BlackjackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/blackjack")
@CrossOrigin(origins = "*")
public class BlackjackController {

    private final BlackjackService blackjackService;

    public BlackjackController(BlackjackService blackjackService) {
        this.blackjackService = blackjackService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<JuegoEstado> iniciarNuevaPartida(@RequestBody Map<String, Integer> payload) {
        int apuesta = payload.get("apuesta");
        JuegoEstado estado = blackjackService.iniciarNuevaPartida(apuesta);
        return ResponseEntity.ok(estado);
    }

    @PostMapping("/pedir")
    public ResponseEntity<JuegoEstado> jugadorPideCarta() {
        return ResponseEntity.ok(blackjackService.jugadorPideCarta());
    }

    @PostMapping("/plantarse")
    public ResponseEntity<JuegoEstado> jugadorSePlanta() {
        return ResponseEntity.ok(blackjackService.jugadorSePlanta());
    }

    @PostMapping("/doblar")
    public ResponseEntity<JuegoEstado> jugadorDobla() {
        return ResponseEntity.ok(blackjackService.jugadorDobla());
    }
}

