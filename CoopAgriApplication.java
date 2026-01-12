package com.coopagri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoopAgriApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoopAgriApplication.class, args);
        System.out.println("""
            🚀 CoopAgri Backend démarré !
            📊 API disponible sur: http://localhost:8080
            📚 Documentation: http://localhost:8080/swagger-ui.html
            """);
    }
}
