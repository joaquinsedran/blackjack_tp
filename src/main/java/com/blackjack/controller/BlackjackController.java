package com.blackjack.controller;

import com.blackjack.model.JuegoEstado;
import com.blackjack.service.BlackjackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
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
    public ResponseEntity<JuegoEstado> iniciarPartida(@RequestBody Object payload) {

        // Pattern matching
        if (payload instanceof Map payloadMap) {
            Object apuestaObj = payloadMap.get("apuesta");
            if (apuestaObj instanceof Integer apuesta) {
                JuegoEstado estado = blackjackService.iniciarNuevaPartida(apuesta);
                return ResponseEntity.ok(estado);
            }
        }

        return ResponseEntity.badRequest().body(blackjackService.obtenerEstadoActual());
    }

    @PostMapping("/pedir")
    public ResponseEntity<JuegoEstado> jugadorPideCarta() {
        JuegoEstado estado = blackjackService.jugadorPideCarta();
        return ResponseEntity.ok(estado);
    }

    @PostMapping("/plantarse")
    public ResponseEntity<JuegoEstado> jugadorSePlanta() {
        JuegoEstado estado = blackjackService.jugadorSePlanta();
        return ResponseEntity.ok(estado);
    }

    @PostMapping("/doblar")
    public ResponseEntity<JuegoEstado> jugadorDobla() {
        JuegoEstado estado = blackjackService.jugadorDobla();
        return ResponseEntity.ok(estado);
    }

    @GetMapping("/estado")
    public ResponseEntity<JuegoEstado> obtenerEstado() {
        JuegoEstado estado = blackjackService.obtenerEstadoActual();
        return ResponseEntity.ok(estado);
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardarPartida() {
        try {
            blackjackService.guardarPartida();
            return ResponseEntity.ok("Partida guardada con éxito.");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar la partida: " + e.getMessage());
        }
    }

    @GetMapping("/cargar")
    public ResponseEntity<JuegoEstado> cargarPartida() {
        JuegoEstado estado = blackjackService.cargarPartida();
        return ResponseEntity.ok(estado);
    }
}