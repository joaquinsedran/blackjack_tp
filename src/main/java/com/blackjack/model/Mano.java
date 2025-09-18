package com.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Mano {
    private List<Carta> cartas;

    // Constructor: crea una mano vacía
    public Mano() {
        cartas = new ArrayList<>();
    }

    // Agrega una carta a la mano
    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    // Calcula el valor total de la mano
    public int calcularValor() {
        int valor = 0;
        int contadorAses = 0;

        // Sumar el valor de todas las cartas
        for (Carta carta : cartas) {
            valor += carta.getValorNumerico();
            if (carta.getValor() == Carta.Valor.A) {
                contadorAses++;
            }
        }

        // Ajustar el valor de los Ases si nos pasamos de 21
        while (valor > 21 && contadorAses > 0) {
            valor -= 10; // Cambiamos un As de 11 a 1 (restamos 10)
            contadorAses--;
        }

        return valor;
    }

    // Muestra las cartas de la mano
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Carta carta : cartas) {
            sb.append(carta.toString()).append(", ");
        }
        // Elimina la última coma y espacio
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString() + " (Total: " + calcularValor() + ")";
    }

    // Getter para acceder a las cartas
    public List<Carta> getCartas() {
        return cartas;
    }
}