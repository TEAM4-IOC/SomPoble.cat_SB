package com.sompoble.cat.conexion;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() throws SQLException {
        assertNotNull(dataSource, "El DataSource no debe ser nulo");

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "La conexion a la base de datos no debe ser nula");
            System.out.println("Conexion exitosa a la base de datos");
        }
    }
}