package com.blackjack.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Mazo {
    private List<Carta> cartas;

    public Mazo() {
        crearYBarajar();
    }

    private void crearYBarajar() {
        String[] palos = {"Corazones", "Diamantes", "Tréboles", "Picas"};
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        this.cartas = Arrays.stream(palos)
                .flatMap(palo -> Arrays.stream(valores)
                        .map(valor -> new Carta(palo, valor)) // Operación intermedia: mapea cada valor a una nueva Carta
                )
                .collect(Collectors.toList()); // Operación terminal: recolecta todas las Cartas en una lista

        barajar();
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta sacarCarta() {
        if (cartas.isEmpty()) {
            System.out.println("--- El mazo se ha quedado sin cartas. Creando y barajando uno nuevo. ---");
            crearYBarajar();
        }
        return cartas.remove(0);
    }

    public int cartasRestantes() {
        return cartas.size();
    }
}