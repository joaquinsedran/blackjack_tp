package com.blackjack.model;

public class Carta {
    public enum Palo {
        PICAS,
        CORAZONES,
        DIAMANTES,
        TREBOLES
    }

    public enum Valor {
        DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE, DIEZ, J, Q, K, A
    }

    private final Palo palo;
    private final Valor valor;

    public Carta(Palo palo, Valor valor) {
        this.palo = palo;
        this.valor = valor;
    }

    public int getValorNumerico() {
        switch (valor) {
            case DOS: return 2;
            case TRES: return 3;
            case CUATRO: return 4;
            case CINCO: return 5;
            case SEIS: return 6;
            case SIETE: return 7;
            case OCHO: return 8;
            case NUEVE: return 9;
            case DIEZ: case J: case Q: case K: return 10;
            case A: return 11;
            default: return 0;
        }
    }

    // Getters para Thymeleaf - CORREGIDOS
    public String getPalo() {
        return palo.name();
    }

    public String getValor() {
        switch (valor) {
            case J: return "J";
            case Q: return "Q";
            case K: return "K";
            case A: return "A";
            default: return String.valueOf(getValorNumerico());
        }
    }

    // Nuevo método para obtener el símbolo del palo
    public String getSimboloPalo() {
        switch (palo) {
            case CORAZONES: return "♥";
            case DIAMANTES: return "♦";
            case TREBOLES: return "♣";
            case PICAS: return "♠";
            default: return "";
        }
    }

    public Palo getPaloEnum() {
        return palo;
    }

    public Valor getValorEnum() {
        return valor;
    }

    @Override
    public String toString() {
        return getValor() + " de " + palo;
    }
}