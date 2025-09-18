package com.blackjack.model;

public class Jugador {
    private String nombre;
    private Mano mano;
    private boolean plantado;
    private int dinero;
    private int apuestaActual;
    private static final int FICHA_MAXIMA = 500;
    private static final int DINERO_INICIAL = 5000;

    // Constructor - Ahora con dinero inicial de casino
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new Mano();
        this.plantado = false;
        this.dinero = DINERO_INICIAL;
        this.apuestaActual = 0;
    }

    // ===== MÉTODOS DE DINERO Y APUESTAS =====
    public int getDinero() {
        return dinero;
    }

    public int getApuestaActual() {
        return apuestaActual;
    }

    public static int getFichaMaxima() {
        return FICHA_MAXIMA;
    }

    public static int getDineroInicial() {
        return DINERO_INICIAL;
    }

    // Apostar con validación de fichas de casino
    public boolean apostar(int cantidad) {
        if (esApuestaValida(cantidad)) {
            apuestaActual = cantidad;  // ← ¡DEBE ser 'cantidad', no 500!
            dinero -= cantidad;        // ← ¡DEBE restar 'cantidad', no 500!
            System.out.println(nombre + " apuesta $" + cantidad + " | Fichas: " + convertirAFichas(cantidad));
            return true;
        }
        return false;
    }

    // Verificar si la apuesta es válida (múltiplo de 5, entre 5-500)
    public boolean esApuestaValida(int cantidad) {
        return cantidad >= 5 &&
                cantidad <= FICHA_MAXIMA &&
                cantidad % 5 == 0 &&
                cantidad <= dinero;
    }

    // Recibir pago según reglas de casino
    public void recibirPago(double multiplicador) {
        int ganancia = (int) (apuestaActual * multiplicador);
        dinero += ganancia;
        System.out.println(nombre + " gana $" + ganancia + " | Total: $" + dinero);
    }

    // Devolver apuesta (en caso de empate)
    public void devolverApuesta() {
        dinero += apuestaActual;
        System.out.println(nombre + " recupera su apuesta de $" + apuestaActual);
    }

    // Convertir cantidad a símbolos de fichas
    private String convertirAFichas(int cantidad) {
        StringBuilder fichas = new StringBuilder();
        int temp = cantidad;

        if (temp >= 500) {
            fichas.append("🔵×").append(temp / 500).append(" ");
            temp %= 500;
        }
        if (temp >= 100) {
            fichas.append("🔴×").append(temp / 100).append(" ");
            temp %= 100;
        }
        if (temp >= 25) {
            fichas.append("🟢×").append(temp / 25).append(" ");
            temp %= 25;
        }
        if (temp >= 5) {
            fichas.append("⚫×").append(temp / 5).append(" ");
        }

        return fichas.toString().trim();
    }

    public void resetearApuesta() {
        apuestaActual = 0;
    }

    public boolean estaEnBancarrota() {
        return dinero < 5; // No puede hacer la apuesta mínima
    }

    // ===== MÉTODOS ORIGINALES DEL JUEGO =====
    public void pedirCarta(Mazo mazo) {
        if (!plantado) {
            Carta carta = mazo.repartirCarta();
            mano.agregarCarta(carta);
            System.out.println(nombre + " recibe: " + carta);
        }
    }

    public void plantarse() {
        this.plantado = true;
        System.out.println(nombre + " se planta.");
    }

    public boolean sePasó() {
        return mano.calcularValor() > 21;
    }

    public boolean tieneBlackjack() {
        return mano.calcularValor() == 21 && mano.getCartas().size() == 2;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public Mano getMano() {
        return mano;
    }

    public boolean isPlantado() {
        return plantado;
    }

    public void setPlantado(boolean plantado) {
        this.plantado = plantado;
    }

    @Override
    public String toString() {
        return nombre + " ($" + dinero + ") - Mano: " + mano.toString();
    }
}
