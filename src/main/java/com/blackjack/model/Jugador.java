package com.blackjack.model;

public class Jugador {
    private String nombre;
    private int dinero;
    private int apuestaActual;
    private Mano mano;
    private boolean plantado;
    private static final int DINERO_INICIAL = 1000;
    private static final int FICHA_MAXIMA = 500;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.dinero = DINERO_INICIAL;
        this.mano = new Mano();
        this.plantado = false;
        this.apuestaActual = 0;
    }

    public boolean apostar(int cantidad) {
        if (cantidad <= 0 || cantidad % 5 != 0) {
            return false;
        }

        if (cantidad > dinero) {
            return false;
        }

        this.apuestaActual = cantidad;
        return true;
    }

    public boolean doblarApuesta() {
        if (dinero >= apuestaActual) {
            dinero -= apuestaActual;
            apuestaActual *= 2;
            return true;
        }
        return false;
    }

    public void recibirPago(double multiplicador) {
        // ✅ CORREGIDO: Calcular solo las ganancias, no sumar la apuesta otra vez
        int ganancia = (int) (apuestaActual * (multiplicador - 1));
        dinero += apuestaActual + ganancia; // ✅ Devuelve apuesta + ganancias
        System.out.println("💰 " + nombre + " gana $" + ganancia + " | Total: $" + dinero);
        apuestaActual = 0;
    }

    public void devolverApuesta() {
        dinero += apuestaActual; // ✅ Solo devolver la apuesta (empate)
        System.out.println("🤝 " + nombre + " recupera su apuesta de $" + apuestaActual + " | Total: $" + dinero);
        apuestaActual = 0;
    }

    public void perderApuesta() {
        // ✅ El dinero ya fue descontado al hacer la apuesta
        System.out.println("❌ " + nombre + " pierde apuesta de $" + apuestaActual + " | Total: $" + dinero);
        apuestaActual = 0;
    }

    public boolean tieneBlackjack() {
        return mano.calcularValor() == 21 && mano.getCartas().size() == 2;
    }

    public boolean sePasó() {
        return mano.calcularValor() > 21;
    }

    public void plantarse() {
        this.plantado = true;
    }

    public boolean estaEnBancarrota() {
        return dinero < 5;
    }

    public void efectuarApuesta() {
        if (apuestaActual > 0 && apuestaActual <= dinero) {
            dinero -= apuestaActual;
            System.out.println("🎰 " + nombre + " apuesta $" + apuestaActual + " | Saldo restante: $" + dinero);
        }
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public int getDinero() { return dinero; }
    public int getApuestaActual() { return apuestaActual; }
    public Mano getMano() { return mano; }
    public boolean isPlantado() { return plantado; }
    public void setPlantado(boolean plantado) { this.plantado = plantado; }

    public static int getFichaMaxima() { return FICHA_MAXIMA; }
    public static int getDineroInicial() { return DINERO_INICIAL; }

    @Override
    public String toString() {
        return nombre + ": " + mano.toString() + " | Dinero: $" + dinero + " | Apuesta: $" + apuestaActual;
    }
}