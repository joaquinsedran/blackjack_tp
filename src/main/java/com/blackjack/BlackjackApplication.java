package com.blackjack;

import com.blackjack.service.BlackjackService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class BlackjackApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlackjackApplication.class, args);
    }

    @Bean
    @Scope("prototype")
    public BlackjackService blackjackService() {
        System.out.println("=== CREANDO NUEVO BLACKJACK SERVICE ===");
        return new BlackjackService();
    }
}