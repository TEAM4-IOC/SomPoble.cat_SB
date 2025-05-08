package com.sompoble.cat.conexion;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Clase de prueba para verificar la conexión a la base de datos.
 * 
 * Esta clase prueba que el DataSource proporcionado por Spring Boot esté
 * correctamente configurado y que sea posible establecer una conexión
 * a la base de datos. La prueba utiliza la anotación {@link SpringBootTest}
 * para cargar el contexto completo de la aplicación.
 * 
 */
@SpringBootTest
public class DatabaseConnectionTest {
    
    /**
     * Instancia del DataSource inyectada por Spring para acceder a la base de datos.
     * Este DataSource es configurado automáticamente basado en las propiedades
     * definidas en los archivos de configuración de la aplicación.
     */
    @Autowired
    private DataSource dataSource;
    
    /**
     * Prueba la conexión a la base de datos.
     * 
     * Este método verifica que:
     * <ul>
     *   <li>El DataSource haya sido inyectado correctamente y no sea nulo</li>
     *   <li>Se pueda obtener una conexión válida a la base de datos</li>
     * </ul>
     * 
     * La conexión se cierra automáticamente después de la prueba gracias al
     * bloque try-with-resources, asegurando la liberación adecuada de recursos.
     * 
     * @throws SQLException Si ocurre algún error al intentar conectarse a la base de datos.
     *                     Esto incluye problemas de credenciales, base de datos no disponible
     *                     o configuración incorrecta.
     */
    @Test
    public void testConnection() throws SQLException {
        assertNotNull(dataSource, "El DataSource no debe ser nulo");
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "La conexion a la base de datos no debe ser nula");
            System.out.println("Conexion exitosa a la base de datos");
        }
    }
}