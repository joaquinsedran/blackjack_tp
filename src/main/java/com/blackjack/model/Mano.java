package com.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Mano {
    private List<Carta> cartas;
    private int apuesta;
    private int puntos;
    private boolean completada;

    public Mano() {
        this.cartas = new ArrayList<>();
        this.apuesta = 0;
        this.puntos = 0;
        this.completada = false;
    }

    public Mano(int apuesta) {
        this.cartas = new ArrayList<>();
        this.apuesta = apuesta;
        this.puntos = 0;
        this.completada = false;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    public int getApuesta() {
        return apuesta;
    }

    public void setApuesta(int apuesta) {
        this.apuesta = apuesta;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public void agregarCarta(Carta carta) {
        this.cartas.add(carta);
        this.puntos = calcularPuntos();
    }

    public boolean puedeDividir() {
        return cartas.size() == 2 &&
                cartas.get(0).getValor().equals(cartas.get(1).getValor());
    }

    public boolean puedeDoblar() {
        return cartas.size() == 2;
    }

    public int calcularPuntos() {
        int puntos = 0;
        int ases = 0;

        for (Carta carta : cartas) {
            String valor = carta.getValor();

            if (valor.equals("A")) {
                ases++;
                puntos += 11;
            } else if (valor.equals("K") || valor.equals("Q") || valor.equals("J") || valor.equals("10")) {
                puntos += 10;
            } else {
                puntos += Integer.parseInt(valor);
            }
        }

        while (puntos > 21 && ases > 0) {
            puntos -= 10;
            ases--;
        }

        return puntos;
    }

    public boolean esBlackjack() {
        return cartas.size() == 2 && puntos == 21;
    }

    public boolean sePaso() {
        return puntos > 21;
    }
}