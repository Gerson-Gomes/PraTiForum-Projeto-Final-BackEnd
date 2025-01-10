package com.maisprati.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // Importação do ComponentScan

@SpringBootApplication
@ComponentScan("com.maisprati.forum") // A anotação ComponentScan precisa estar aqui
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
