package com.blackjack.view;

import java.util.Scanner;

public class ConsolaView {
    private Scanner scanner;

    public ConsolaView() {
        this.scanner = new Scanner(System.in);
    }

    // ===== NUEVOS MÉTODOS PARA APUESTAS =====
    public int pedirApuesta(int dineroDisponible, int fichaMaxima) {
        System.out.println("\n💎 --- HACER APUESTA --- 💎");
        System.out.println("💰 Dinero disponible: $" + dineroDisponible);
        System.out.println("🎯 Ficha máxima: $" + fichaMaxima);
        System.out.println("🎲 Fichas disponibles: 🔵$500 🔴$100 🟢$25 ⚫$5");
        System.out.print("¿Cuánto quieres apostar? (múltiplo de 5, min $5, max $" + fichaMaxima + "): ");

        try {
            int apuesta = Integer.parseInt(scanner.nextLine());

            // Validar apuesta
            if (apuesta < 5) {
                System.out.println("❌ Apuesta mínima: $5");
                return -1;
            }
            if (apuesta > fichaMaxima) {
                System.out.println("❌ Apuesta máxima: $" + fichaMaxima);
                return -1;
            }
            if (apuesta % 5 != 0) {
                System.out.println("❌ La apuesta debe ser múltiplo de 5");
                return -1;
            }
            if (apuesta > dineroDisponible) {
                System.out.println("❌ No tienes suficiente dinero");
                return -1;
            }

            return apuesta;

        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor ingresa un número válido");
            return -1;
        }
    }

    public void mostrarDinero(int dinero) {
        System.out.println("💵 Dinero actual: $" + dinero);
    }

    public void mostrarApuesta(int apuesta) {
        System.out.println("🎯 Apuesta actual: $" + apuesta);
    }

    public void mostrarBancarrota() {
        System.out.println("\n💸 ¡BANCARROTA! No tienes más dinero para apostar.");
        System.out.println("😞 El juego ha terminado para ti.");
    }

    public void mostrarFichas(int cantidad) {
        System.out.print("Fichas apostadas: ");
        int temp = cantidad;

        if (temp >= 500) {
            System.out.print("🔵×" + (temp / 500) + " ");
            temp %= 500;
        }
        if (temp >= 100) {
            System.out.print("🔴×" + (temp / 100) + " ");
            temp %= 100;
        }
        if (temp >= 25) {
            System.out.print("🟢×" + (temp / 25) + " ");
            temp %= 25;
        }
        if (temp >= 5) {
            System.out.print("⚫×" + (temp / 5) + " ");
        }
        System.out.println();
    }

    // ===== MÉTODOS EXISTENTES (se mantienen igual) =====
    public void mostrarBienvenida() {
        System.out.println("🎰 ¡BIENVENIDO AL BLACKJACK DE CASINO! 🎰");
        System.out.println("=========================================");
    }

    public void mostrarOpciones() {
        System.out.println("\n--- ¿Qué quieres hacer? ---");
        System.out.println("1. Pedir carta");
        System.out.println("2. Plantarse");
        System.out.println("3. Ver reglas");
        System.out.print("Elige una opción (1-3): ");
    }

    public int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void mostrarReglas() {
        System.out.println("\n--- REGLAS DEL CASINO ---");
        System.out.println("• Apuesta mínima: $5, máxima: $500");
        System.out.println("• Blackjack paga 3:2 (apuesta $100 → ganas $150)");
        System.out.println("• Victoria normal paga 1:1");
        System.out.println("• Empate: devuelve la apuesta");
        System.out.println("• Si te pasas de 21, pierdes la apuesta");
        System.out.println("---------------------------");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public boolean preguntarJugarOtraVez() {
        System.out.print("\n¿Quieres jugar otra vez? (s/n): ");
        String respuesta = scanner.nextLine().toLowerCase();
        return respuesta.equals("s") || respuesta.equals("si");
    }

    public void mostrarDespedida() {
        System.out.println("\n¡Gracias por jugar! ¡Hasta pronto! 👋");
    }

    public void cerrar() {
        scanner.close();
    }
}