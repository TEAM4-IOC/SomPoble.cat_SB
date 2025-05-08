package com.sompoble.cat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
public class ApplicationTest {

    @Test
    public void testMain() {
        // Utilizamos MockedStatic para mockear la clase SpringApplication
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            // Preparamos el mock para verificar la llamada al método run
            mockedSpringApplication.when(() -> 
                SpringApplication.run(eq(Application.class), any(String[].class)))
                .thenReturn(null);
            
            // Ejecutamos el método main
            String[] args = new String[]{"arg1", "arg2"};
            Application.main(args);
            
            // Verificamos que el método run fue llamado con los argumentos correctos
            mockedSpringApplication.verify(() -> 
                SpringApplication.run(eq(Application.class), eq(args)), Mockito.times(1));
        }
    }
}