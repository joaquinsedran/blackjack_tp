package com.blackjack;

import com.blackjack.controller.BlackjackController;

public class Main {
    public static void main(String[] args) {
        BlackjackController controller = new BlackjackController();
        controller.iniciarJuego();
    }
}