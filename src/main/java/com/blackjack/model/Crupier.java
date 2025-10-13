package com.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class Crupier {
    private String nombre;
    private List<Carta> mano;
    private int puntuacion;

    public Crupier() {
        this.nombre = "Crupier";
        this.mano = new ArrayList<>();
        this.puntuacion = 0;
    }

    public void recibirCarta(Carta carta) {
        if (carta != null) {
            mano.add(carta);
            actualizarPuntuacion();
        }
    }

    public void actualizarPuntuacion() {
        int valor = 0;
        int ases = 0;

        for (Carta carta : mano) {
            valor += carta.getValorNumerico();
            if (carta.getValor().equals("A")) {
                ases++;
            }
        }

        while (valor > 21 && ases > 0) {
            valor -= 10;
            ases--;
        }

        this.puntuacion = valor;
    }

    public void reiniciarMano() {
        this.mano.clear();
        this.puntuacion = 0;
    }

    public boolean debePedirCarta() {
        return puntuacion < 17;
    }

    public boolean sePasó() {
        return puntuacion > 21;
    }

    public boolean tieneBlackjack() {
        return mano.size() == 2 && puntuacion == 21;
    }


    public String getNombre() {
        return nombre;
    }

    public List<Carta> getMano() {
        return new ArrayList<>(mano);
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    @Override
    public String toString() {
        return nombre + " - Mano: " + mano + " - Puntuación: " + puntuacion;
    }
}