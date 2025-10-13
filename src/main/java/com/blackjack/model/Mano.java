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

    /**
     * Constructor de copia. Soluciona el error de compilación en BlackjackService.
     * @param otra La mano a copiar.
     */
    public Mano(Mano otra) {
        this.cartas = new ArrayList<>(otra.getCartas());
        this.apuesta = otra.getApuesta();
        this.puntos = otra.getPuntos();
        this.completada = otra.isCompletada();
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
        if (carta != null) {
            this.cartas.add(carta);
            this.puntos = calcularPuntos();
        }
    }

    public boolean puedeDividir() {
        return cartas.size() == 2 &&
                cartas.get(0).getValor().equals(cartas.get(1).getValor());
    }

    public boolean puedeDoblar() {
        return cartas.size() == 2;
    }

    public int calcularPuntos() {
        int puntosCalculados = 0;
        int ases = 0;

        for (Carta carta : cartas) {
            puntosCalculados += carta.getValorNumerico();
            if (carta.getValor().equals("A")) {
                ases++;
            }
        }

        while (puntosCalculados > 21 && ases > 0) {
            puntosCalculados -= 10;
            ases--;
        }

        return puntosCalculados;
    }

    public boolean esBlackjack() {
        return this.cartas.size() == 2 && calcularPuntos() == 21;
    }

    public boolean sePaso() {
        return calcularPuntos() > 21;
    }
}

