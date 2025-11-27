package com.blackjack.service;

import com.blackjack.model.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlackjackService {

    private static final String NOMBRE_ARCHIVO_ESTADO = "blackjack_estado.dat";

    private Mazo mazo;
    private Mano manoCrupier;
    private List<Mano> manosJugador;
    private int manoActivaIndex;
    private boolean juegoActivo;
    private int saldoJugador;

    public BlackjackService() {
        JuegoEstado estadoInicial = cargarPartida();
        if (estadoInicial.getSaldoJugador() == 1000 && !estadoInicial.isJuegoActivo()) {

            this.saldoJugador = 1000;
            reiniciarPartida();
        } else {
            actualizarEstadoInterno(estadoInicial);
        }
    }

    private void reiniciarPartida() {
        this.mazo = new Mazo();
        this.manoCrupier = new Mano();
        this.manosJugador = new ArrayList<>();
        this.manosJugador.add(new Mano());
        this.manoActivaIndex = 0;
        this.juegoActivo = false;
    }

    public JuegoEstado iniciarNuevaPartida(int apuesta) {
        if (apuesta <= 0 || apuesta > saldoJugador) {
            JuegoEstado estadoError = obtenerEstadoActual();
            estadoError.setMensaje("Apuesta inválida o saldo insuficiente.");
            return estadoError;
        }

        reiniciarPartida();
        saldoJugador -= apuesta;
        manosJugador.get(0).setApuesta(apuesta);

        manosJugador.get(0).agregarCarta(mazo.sacarCarta());
        manoCrupier.agregarCarta(mazo.sacarCarta());
        manosJugador.get(0).agregarCarta(mazo.sacarCarta());
        manoCrupier.agregarCarta(mazo.sacarCarta());

        juegoActivo = true;

        if (manosJugador.get(0).esBlackjack()) {
            return finalizarPartidaPorBlackjack();
        }

        JuegoEstado estado = obtenerEstadoActual();
        estado.setMensaje("Es tu turno. ¿Pedir o plantarse?");
        return estado;
    }

    public JuegoEstado jugadorPideCarta() {
        if (!juegoActivo) return obtenerEstadoActual();

        Mano manoActual = manosJugador.get(manoActivaIndex);
        manoActual.agregarCarta(mazo.sacarCarta());

        if (manoActual.sePaso()) {
            manoActual.setCompletada(true);
            manoActivaIndex++;
            if (manoActivaIndex >= manosJugador.size()) {
                juegoActivo = false;
                return turnoCrupier();
            }
        }

        return obtenerEstadoActual();
    }

    public JuegoEstado jugadorSePlanta() {
        if (!juegoActivo) return obtenerEstadoActual();

        Mano manoActual = manosJugador.get(manoActivaIndex);
        manoActual.setCompletada(true);
        manoActivaIndex++;

        if (manoActivaIndex >= manosJugador.size()) {
            return turnoCrupier();
        }

        return obtenerEstadoActual();
    }

    public JuegoEstado jugadorDobla() {
        if (!juegoActivo) return obtenerEstadoActual();

        Mano manoActual = manosJugador.get(manoActivaIndex);

        if (!manoActual.puedeDoblar() || saldoJugador < manoActual.getApuesta()) {
            JuegoEstado estadoError = obtenerEstadoActual();
            estadoError.setMensaje("No puedes doblar en este momento.");
            return estadoError;
        }

        saldoJugador -= manoActual.getApuesta();
        manoActual.setApuesta(manoActual.getApuesta() * 2);

        manoActual.agregarCarta(mazo.sacarCarta());

        manoActual.setCompletada(true);
        manoActivaIndex++;

        return turnoCrupier();
    }

    private JuegoEstado turnoCrupier() {
        juegoActivo = false;
        manoActivaIndex = manosJugador.size();

        boolean algunaManoNoSePaso = manosJugador.stream().anyMatch(mano -> !mano.sePaso());

        if (algunaManoNoSePaso) {

            while (manoCrupier.getPuntos() < 17) {
                manoCrupier.agregarCarta(mazo.sacarCarta());
            }
        }

        return determinarGanador();
    }

    private JuegoEstado determinarGanador() {
        for (Mano manoJugador : manosJugador) {
            if (manoJugador.sePaso()) {

            } else if (manoCrupier.sePaso() || manoJugador.getPuntos() > manoCrupier.getPuntos()) {

                saldoJugador += manoJugador.getApuesta() * 2;
            } else if (manoJugador.getPuntos() == manoCrupier.getPuntos()) {

                saldoJugador += manoJugador.getApuesta();
            }
        }

        JuegoEstado estadoFinal = obtenerEstadoActual();
        estadoFinal.setMensaje(generarMensajeFinal());
        return estadoFinal;
    }

    private JuegoEstado finalizarPartidaPorBlackjack() {
        juegoActivo = false;
        Mano manoJugador = manosJugador.get(0);
        saldoJugador += (int)(manoJugador.getApuesta() * 2.5);

        JuegoEstado estado = obtenerEstadoActual();
        estado.setMensaje("¡Blackjack! ¡Ganaste!");
        return estado;
    }

    public JuegoEstado obtenerEstadoActual() {
        JuegoEstado estado = new JuegoEstado();
        estado.setJuegoActivo(juegoActivo);
        estado.setSaldoJugador(saldoJugador);

        estado.setManosJugador(new ArrayList<>());
        for(Mano mano : manosJugador) {
            estado.getManosJugador().add(new Mano(mano));
        }

        estado.setManoActivaIndex(manoActivaIndex);
        estado.setCartasCrupier(new ArrayList<>(manoCrupier.getCartas()));
        estado.setPuntosCrupier(manoCrupier.getPuntos());

        if (juegoActivo && !manoCrupier.getCartas().isEmpty()) {
            estado.setPuntosCrupier(manoCrupier.getCartas().get(0).getValorNumerico());
        }

        if (juegoActivo && manoActivaIndex < manosJugador.size()) {
            Mano manoActiva = manosJugador.get(manoActivaIndex);
            boolean puedePagar = saldoJugador >= manoActiva.getApuesta();
            estado.setPuedeDoblar(manoActiva.puedeDoblar() && puedePagar);
        } else {
            estado.setPuedeDoblar(false);
        }

        return estado;
    }

    private String generarMensajeFinal() {
        if (manosJugador.isEmpty()) return "Partida finalizada.";

        Mano manoJugador = manosJugador.get(0);

        if (manoJugador.sePaso()) {
            return "¡Te pasaste de 21! Gana el crupier.";
        }
        if (manoCrupier.sePaso()) {
            return "¡Crupier se pasó de 21! ¡Ganaste!";
        }
        if (manoJugador.getPuntos() > manoCrupier.getPuntos()) {
            return "¡Felicidades! ¡Ganaste!";
        }
        if (manoCrupier.getPuntos() > manoJugador.getPuntos()) {
            return "Gana el crupier.";
        }
        return "¡Empate!";
    }
    // serializacion
    public void guardarPartida() throws IOException {
        try (FileOutputStream archivoSalida = new FileOutputStream(NOMBRE_ARCHIVO_ESTADO);
             ObjectOutputStream salidaObjeto = new ObjectOutputStream(archivoSalida))
        {
            JuegoEstado estadoAGuardar = obtenerEstadoActual();
            salidaObjeto.writeObject(estadoAGuardar);
            System.out.println("Partida guardada exitosamente en " + NOMBRE_ARCHIVO_ESTADO);
        }
    }
     // deserializacion
    public JuegoEstado cargarPartida() {
        try (FileInputStream archivoEntrada = new FileInputStream(NOMBRE_ARCHIVO_ESTADO);
             ObjectInputStream entradaObjeto = new ObjectInputStream(archivoEntrada))
        {
            JuegoEstado estadoCargado = (JuegoEstado) entradaObjeto.readObject();
            System.out.println("Partida cargada exitosamente.");
            return estadoCargado;

        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo de partida. Iniciando nuevo juego.");
            return new JuegoEstado();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la partida. Se inicia un nuevo juego.");
            return new JuegoEstado();
        }
    }

    private void actualizarEstadoInterno(JuegoEstado estado) {

        this.saldoJugador = estado.getSaldoJugador();
        this.juegoActivo = estado.isJuegoActivo();
        this.manoActivaIndex = estado.getManoActivaIndex();

        this.mazo = new Mazo();
        this.manoCrupier = new Mano();
        this.manoCrupier.setCartas(estado.getCartasCrupier());
        this.manoCrupier.calcularPuntos();

        this.manosJugador = new ArrayList<>();
        for (Mano manoCargada : estado.getManosJugador()) {
            Mano nuevaMano = new Mano();
            nuevaMano.setCartas(manoCargada.getCartas());
            nuevaMano.setApuesta(manoCargada.getApuesta());
            nuevaMano.setCompletada(manoCargada.isCompletada());
            nuevaMano.calcularPuntos();
            this.manosJugador.add(nuevaMano);
        }
    }
}