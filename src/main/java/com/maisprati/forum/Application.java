package com.maisprati.forum;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // Importação do ComponentScan

@SpringBootApplication
@ComponentScan("com.maisprati.forum") // A anotação ComponentScan precisa estar aqui
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
