package com.sompoble.cat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase principal de la aplicación Spring Boot. Esta clase contiene el punto de
 * entrada principal de la aplicación y configura el contexto de Spring Boot con
 * sus anotaciones.
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    /**
     * Método principal que inicia la aplicación Spring Boot. Este método
     * arranca el contexto de Spring y pone en marcha la aplicación.
     *
     * @param args Argumentos de línea de comandos pasados a la aplicación
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
