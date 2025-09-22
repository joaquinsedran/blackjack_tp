package com.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private List<Carta> cartas;
    private int indiceCartaActual;

    public Mazo() {
        cartas = new ArrayList<>();
        inicializarMazo();
        barajar();
    }

    private void inicializarMazo() {
        for (Carta.Palo palo : Carta.Palo.values()) {
            for (Carta.Valor valor : Carta.Valor.values()) {
                cartas.add(new Carta(palo, valor));
            }
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
        indiceCartaActual = 0;
    }

    public Carta repartirCarta() {
        if (indiceCartaActual >= cartas.size()) {
            barajar(); // Si se acaban las cartas, barajar de nuevo
        }
        return cartas.get(indiceCartaActual++);
    }

    // ✅ NUEVO MÉTODO AGREGADO
    public int getCartasRestantes() {
        return cartas.size() - indiceCartaActual;
    }

    // Método para reiniciar completamente el mazo
    public void reiniciar() {
        barajar();
    }
}