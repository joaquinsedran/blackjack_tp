package com.blackjack.model;

public class Crupier {
    private Mano mano;
    private boolean jugando;

    // Constructor: crea un crupier con mano vacía
    public Crupier() {
        this.mano = new Mano();
        this.jugando = false;
    }

    // El crupier juega automáticamente según las reglas
    public void jugar(Mazo mazo) {
        jugando = true;
        System.out.println("\n--- Turno del Crupier ---");

        // El crupier debe pedir cartas hasta tener 17 o más
        while (mano.calcularValor() < 17) {
            Carta carta = mazo.repartirCarta();
            mano.agregarCarta(carta);
            System.out.println("Crupier recibe: " + carta + " (Total: " + mano.calcularValor() + ")");

            // Pequeña pausa para dramatismo
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Mostrar resultado final del crupier
        if (sePasó()) {
            System.out.println("¡El crupier se pasó de 21! (" + mano.calcularValor() + ")");
        } else {
            System.out.println("El crupier se planta con: " + mano.calcularValor());
        }

        jugando = false;
    }

    // Verifica si el crupier se pasó de 21
    public boolean sePasó() {
        return mano.calcularValor() > 21;
    }

    // Verifica si el crupier tiene blackjack
    public boolean tieneBlackjack() {
        return mano.calcularValor() == 21;
    }

    // GETTERS
    public Mano getMano() {
        return mano;
    }

    public boolean isJugando() {
        return jugando;
    }

    // Representación en String del crupier
    @Override
    public String toString() {
        return "Crupier: " + mano.toString();
    }
}
