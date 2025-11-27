package com.blackjack.model;

import java.io.Serializable;

public class Carta implements Serializable {
    private static final long serialVersionUID = 1L;

    private String palo;
    private String valor;

    public Carta(String palo, String valor) {
        this.palo = palo;
        this.valor = valor;
    }

    public int getValorNumerico() {
        return switch (valor) {
            case "A" -> 11;
            case "K", "Q", "J", "10" -> 10;
            case "9" -> 9;
            case "8" -> 8;
            case "7" -> 7;
            case "6" -> 6;
            case "5" -> 5;
            case "4" -> 4;
            case "3" -> 3;
            case "2" -> 2;
            default -> 0;
        };
    }

    public String getValor() {
        return valor;
    }

    public String getPalo() {
        return palo;
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
}