package com.blackjack.model;

import java.util.ArrayList;
import java.util.List;

public class JuegoEstado {
    private boolean juegoActivo;
    private int saldoJugador;
    private int apuestaActual;
    private List<Carta> cartasCrupier;
    private List<Mano> manosJugador;
    private int manoActivaIndex;
    private boolean enModoSplit;
    private String mensaje;
    private boolean puedeDividir;
    private boolean puedeDoblar;
    private int puntosCrupier;

    public JuegoEstado() {
        this.cartasCrupier = new ArrayList<>();
        this.manosJugador = new ArrayList<>();
        this.saldoJugador = 1000;
        this.apuestaActual = 0;
        this.juegoActivo = false;
        this.manoActivaIndex = 0;
        this.enModoSplit = false;
        this.mensaje = "¡Bienvenido al Blackjack!";
        this.puedeDividir = false;
        this.puedeDoblar = false;
        this.puntosCrupier = 0;
    }


    public boolean isJuegoActivo() { return juegoActivo; }
    public void setJuegoActivo(boolean juegoActivo) { this.juegoActivo = juegoActivo; }

    public int getSaldoJugador() { return saldoJugador; }
    public void setSaldoJugador(int saldoJugador) { this.saldoJugador = saldoJugador; }

    public int getApuestaActual() { return apuestaActual; }
    public void setApuestaActual(int apuestaActual) { this.apuestaActual = apuestaActual; }

    public List<Carta> getCartasCrupier() { return cartasCrupier; }
    public void setCartasCrupier(List<Carta> cartasCrupier) { this.cartasCrupier = cartasCrupier; }

    public List<Mano> getManosJugador() { return manosJugador; }
    public void setManosJugador(List<Mano> manosJugador) { this.manosJugador = manosJugador; }

    public int getManoActivaIndex() { return manoActivaIndex; }
    public void setManoActivaIndex(int manoActivaIndex) { this.manoActivaIndex = manoActivaIndex; }

    public boolean isEnModoSplit() { return enModoSplit; }
    public void setEnModoSplit(boolean enModoSplit) { this.enModoSplit = enModoSplit; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public boolean isPuedeDividir() { return puedeDividir; }
    public void setPuedeDividir(boolean puedeDividir) { this.puedeDividir = puedeDividir; }

    public int getPuntosCrupier() { return puntosCrupier; }
    public void setPuntosCrupier(int puntosCrupier) { this.puntosCrupier = puntosCrupier; }

    public boolean isPuedeDoblar() { return puedeDoblar; }
    public void setPuedeDoblar(boolean puedeDoblar) { this.puedeDoblar = puedeDoblar; }
}
