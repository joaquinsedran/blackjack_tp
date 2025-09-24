package com.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Mano {
    private List<Carta> cartas;

    public Mano() {
        this.cartas = new ArrayList<>();
    }

    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    public int calcularValor() {
        int valor = 0;
        int ases = 0;

        for (Carta carta : cartas) {
            // ✅ CORRECTO: Comparar enums directamente
            switch (carta.getValorEnum()) {
                case A:
                    ases++;
                    valor += 11;
                    break;
                case J:
                case Q:
                case K:
                    valor += 10;
                    break;
                default:
                    // Para DOS, TRES, ..., DIEZ
                    valor += carta.getValorEnum().ordinal() + 2;
                    break;
            }
        }

        // Ajustar Ases
        while (valor > 21 && ases > 0) {
            valor -= 10;
            ases--;
        }

        return valor;
    }

    public List<Carta> getCartas() { return cartas; }
    public void limpiar() { cartas.clear(); }
    public int tamaño() { return cartas.size(); }

    @Override
    public String toString() {
        return cartas.toString();
    }
}