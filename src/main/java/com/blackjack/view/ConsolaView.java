package com.blackjack.view;

import java.util.Scanner;

public class ConsolaView {
    private Scanner scanner;

    // Códigos de colores ANSI (mantener igual)
    public static final String RESET = "\u001B[0m";
    public static final String NEGRITA = "\u001B[1m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    public static final String MORADO = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BLANCO = "\u001B[37m";
    public static final String FONDO_ROJO = "\u001B[41m";
    public static final String FONDO_VERDE = "\u001B[42m";
    public static final String FONDO_AMARILLO = "\u001B[43m";
    public static final String FONDO_AZUL = "\u001B[44m";
    public static final String FONDO_MORADO = "\u001B[45m";

    public ConsolaView() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarBienvenida() {
        System.out.println(CYAN + NEGRITA + "╔════════════════════════════════╗" + RESET);
        System.out.println(CYAN + NEGRITA + "║    ¡BIENVENIDO AL BLACKJACK!    ║" + RESET);
        System.out.println(CYAN + NEGRITA + "╚════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public int pedirApuesta(int dineroDisponible, int fichaMaxima) {
        System.out.println(AZUL + NEGRITA + "--- HACER APUESTA ---" + RESET);
        System.out.println("Dinero disponible: " + VERDE + "$" + dineroDisponible + RESET);
        System.out.println("Ficha individual máxima: " + AMARILLO + "$" + fichaMaxima + RESET); // Mensaje actualizado
        System.out.println("Apuesta máxima permitida: " + VERDE + "$" + dineroDisponible + RESET); // Nuevo mensaje

        // Mostrar fichas disponibles con colores
        System.out.print("Fichas disponibles: ");
        mostrarFichasConColores(dineroDisponible, fichaMaxima);

        System.out.print("¿Cuánto quieres apostar? (múltiplo de 5, min $5, max $" + dineroDisponible + "): ");

        try {
            int apuesta = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            return apuesta;
        } catch (Exception e) {
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }

    private void mostrarFichasConColores(int dineroDisponible, int fichaMaxima) {
        int[] fichas = {500, 100, 25, 5};
        String[] coloresFondo = {FONDO_ROJO, FONDO_AZUL, FONDO_VERDE, FONDO_AMARILLO};
        String[] coloresTexto = {BLANCO, BLANCO, BLANCO, BLANCO};

        for (int i = 0; i < fichas.length; i++) {
            if (fichas[i] <= fichaMaxima && fichas[i] <= dineroDisponible) {
                System.out.print(coloresFondo[i] + coloresTexto[i] + " $" + fichas[i] + " " + RESET + " ");
            }
        }
        System.out.println();
    }

    public void mostrarApuesta(int apuesta) {
        int[] fichas = {500, 100, 25, 5};
        String[] coloresFondo = {FONDO_ROJO, FONDO_AZUL, FONDO_VERDE, FONDO_AMARILLO};
        String[] coloresTexto = {BLANCO, BLANCO, BLANCO, BLANCO};
        String[] nombres = {"ROJA", "AZUL", "VERDE", "AMARILLA"};

        System.out.print("Fichas apostadas: ");
        int tempApuesta = apuesta;
        boolean primeraFicha = true;

        for (int i = 0; i < fichas.length; i++) {
            int cantidad = tempApuesta / fichas[i];
            if (cantidad > 0) {
                if (!primeraFicha) {
                    System.out.print(" + ");
                }
                System.out.print(coloresFondo[i] + coloresTexto[i] + "×" + cantidad + " " + nombres[i] + RESET);
                tempApuesta %= fichas[i];
                primeraFicha = false;
            }
        }
        System.out.println(" | Total: " + VERDE + "$" + apuesta + RESET);
    }

    public void mostrarMensaje(String mensaje) {
        if (mensaje.contains("❌") || mensaje.toLowerCase().contains("insuficiente") ||
                mensaje.toLowerCase().contains("inválida") || mensaje.toLowerCase().contains("error")) {
            System.out.println(ROJO + mensaje + RESET);
        } else if (mensaje.contains("✅") || mensaje.toLowerCase().contains("gana") ||
                mensaje.toLowerCase().contains("éxito") || mensaje.toLowerCase().contains("aceptada")) {
            System.out.println(VERDE + mensaje + RESET);
        } else if (mensaje.contains("🎰") || mensaje.contains("🤝")) {
            System.out.println(AMARILLO + mensaje + RESET);
        } else {
            System.out.println(mensaje);
        }
    }

    public void mostrarOpciones(boolean puedeDoblar, boolean puedeDividir) {
        System.out.println(MORADO + NEGRITA + "\n--- OPCIONES DISPONIBLES ---" + RESET);
        System.out.println("1. " + CYAN + "Pedir carta" + RESET);
        System.out.println("2. " + AMARILLO + "Plantarse" + RESET);

        if (puedeDoblar) {
            System.out.println("3. " + VERDE + "Doblar apuesta" + RESET);
        } else {
            System.out.println("3. " + ROJO + "Doblar apuesta (no disponible)" + RESET);
        }

        if (puedeDividir) {
            System.out.println("4. " + AZUL + "Dividir" + RESET);
        } else {
            System.out.println("4. " + ROJO + "Dividir (no disponible)" + RESET);
        }

        System.out.println("5. " + MORADO + "Ver reglas" + RESET);
        System.out.print("Elige una opción: ");
    }

    public int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            return opcion;
        } catch (Exception e) {
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }

    public void mostrarDinero(int dinero) {
        System.out.println(VERDE + NEGRITA + "💰 Dinero actual: $" + dinero + RESET);
    }

    public void mostrarBancarrota() {
        System.out.println(ROJO + NEGRITA + "╔════════════════════════════════╗" + RESET);
        System.out.println(ROJO + NEGRITA + "║         ¡BANCARROTA!           ║" + RESET);
        System.out.println(ROJO + NEGRITA + "║   Te has quedado sin dinero    ║" + RESET);
        System.out.println(ROJO + NEGRITA + "╚════════════════════════════════╝" + RESET);
    }

    public void mostrarDespedida() {
        System.out.println(CYAN + NEGRITA + "╔════════════════════════════════╗" + RESET);
        System.out.println(CYAN + NEGRITA + "║     ¡GRACIAS POR JUGAR!        ║" + RESET);
        System.out.println(CYAN + NEGRITA + "║     ¡VUELVE PRONTO!            ║" + RESET);
        System.out.println(CYAN + NEGRITA + "╚════════════════════════════════╝" + RESET);
    }

    public boolean preguntarJugarOtraVez() {
        System.out.print(AMARILLO + "¿Quieres jugar otra partida? (s/n): " + RESET);
        String respuesta = scanner.nextLine().toLowerCase();
        return respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí");
    }

    public void mostrarReglas() {
        System.out.println(AZUL + NEGRITA + "\n--- REGLAS DEL BLACKJACK ---" + RESET);
        System.out.println("• Objetivo: Llegar a 21 o tener más puntos que el crupier");
        System.out.println("• Blackjack: 21 con 2 cartas (paga 3:2)");
        System.out.println("• El crupier debe pedir hasta 16 y plantarse en 17");
        System.out.println("• Puedes doblar solo con 2 cartas");
        System.out.println("• Puedes dividir con dos cartas del mismo valor");
        System.out.println("• Apuesta mínima: $5, máxima: TODO tu dinero"); // Mensaje actualizado
        System.out.println("• Las fichas son múltiplos de 5\n");
    }

    public void cerrar() {
        scanner.close();
    }
}