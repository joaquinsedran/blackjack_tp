package com.blackjack.controller;

import com.blackjack.service.BlackjackService;
import com.blackjack.model.JuegoEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blackjack")
@CrossOrigin(origins = "*")
public class BlackjackController {

    @Autowired
    private BlackjackService blackjackService;

    @PostMapping("/nueva-partida")
    public ResponseEntity<JuegoEstado> nuevaPartida(@RequestBody ApuestaRequest request) {
        try {
            JuegoEstado estado = blackjackService.iniciarNuevaPartida(request.getApuesta());
            return ResponseEntity.ok(estado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/pedir-carta")
    public ResponseEntity<JuegoEstado> pedirCarta() {
        try {
            JuegoEstado estado = blackjackService.jugadorPideCarta();
            return ResponseEntity.ok(estado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/plantarse")
    public ResponseEntity<JuegoEstado> plantarse() {
        try {
            JuegoEstado estado = blackjackService.jugadorSePlanta();
            return ResponseEntity.ok(estado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/estado")
    public ResponseEntity<JuegoEstado> obtenerEstado() {
        try {
            JuegoEstado estado = blackjackService.obtenerEstadoActual();
            return ResponseEntity.ok(estado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class ApuestaRequest {
        private int apuesta;
        public int getApuesta() { return apuesta; }
        public void setApuesta(int apuesta) { this.apuesta = apuesta; }
    }
}