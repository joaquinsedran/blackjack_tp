package com.blackjack.model;

public class Carta {
    public enum Palo { PICAS, CORAZONES, DIAMANTES, TREBOLES }
    public enum Valor { DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE, DIEZ, J, Q, K, A }

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
            case DIEZ:
            case J:
            case Q:
            case K: return 10;
            case A: return 11;
            default: return 0;
        }
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }

    // Getters
    public Palo getPalo() { return palo; }
    public Valor getValor() { return valor; }
}