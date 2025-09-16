package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private List<Carta> cartas;
    private int siguienteCartaIndex;

    // Constructor: crea un mazo nuevo y barajado
    public Mazo() {
        cartas = new ArrayList<>();
        siguienteCartaIndex = 0;
        inicializarMazo();
        barajar();
    }

    // Llena el mazo con las 52 cartas
    private void inicializarMazo() {
        for (Carta.Palo palo : Carta.Palo.values()) {
            for (Carta.Valor valor : Carta.Valor.values()) {
                cartas.add(new Carta(palo, valor));
            }
        }
    }

    // Mezcla las cartas
    public void barajar() {
        Collections.shuffle(cartas);
        siguienteCartaIndex = 0; // Reinicia el índice después de barajar
    }

    // Reparte la siguiente carta del mazo
    public Carta repartirCarta() {
        if (siguienteCartaIndex >= cartas.size()) {
            throw new IllegalStateException("¡No hay más cartas en el mazo!");
        }
        return cartas.get(siguienteCartaIndex++);
    }

    // Método útil para debugging
    @Override
    public String toString() {
        return "Mazo con " + cartas.size() + " cartas";
    }
}