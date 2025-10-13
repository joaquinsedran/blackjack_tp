package com.blackjack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.blackjack.controller", "com.blackjack.service"})
public class BlackjackApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlackjackApplication.class, args);
        System.out.println(">>> Servidor de Blackjack iniciado! Accede en http://localhost:8080/mesa.html <<<");
    }
}

