package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.exception.BadRequestException;
import com.sompoble.cat.exception.UnauthorizedException;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmpresarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private EmpresarioService empresarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private Cliente cliente;
    private Empresario empresario;
    private Map<String, String> loginRequest;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setEmail("cliente@example.com");
        cliente.setPass("hashedPassword1");

        empresario = new Empresario();
        empresario.setEmail("empresario@example.com");
        empresario.setPass("hashedPassword2");

        loginRequest = new HashMap<>();
        loginRequest.put("email", "test@example.com");
        loginRequest.put("pass", "password123");
    }

    @Test
    void loginNullEmail() {
        loginRequest.put("email", null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authController.login(loginRequest);
        });
        
        assertEquals("Email y contraseña son obligatorios", exception.getMessage());
    }

    @Test
    void loginNullPass() {
        loginRequest.put("pass", null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authController.login(loginRequest);
        });
        
        assertEquals("Email y contraseña son obligatorios", exception.getMessage());
    }

    @Test
    void loginValidCliente() {
        loginRequest.put("email", "cliente@example.com");
        when(clienteService.findByEmail("cliente@example.com")).thenReturn(cliente);
        doReturn(true).when(passwordEncoder).matches(any(), any());

        ResponseEntity<?> response = authController.login(loginRequest);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, responseBody.get("status"));
        assertEquals("Inicio de sesión exitoso", responseBody.get("message"));
        assertEquals(1, responseBody.get("tipoUsuario"));
        assertEquals(cliente, responseBody.get("usuario"));
        
        verify(clienteService).findByEmail("cliente@example.com");
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void loginValidEmpresario() {
        loginRequest.put("email", "empresario@example.com");
        when(clienteService.findByEmail("empresario@example.com")).thenReturn(null);
        when(empresarioService.findByEmail("empresario@example.com")).thenReturn(empresario);
        doReturn(true).when(passwordEncoder).matches(any(), any());

        ResponseEntity<?> response = authController.login(loginRequest);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, responseBody.get("status"));
        assertEquals("Inicio de sesión exitoso", responseBody.get("message"));
        assertEquals(2, responseBody.get("tipoUsuario"));
        assertEquals(empresario, responseBody.get("usuario"));
        
        verify(clienteService).findByEmail("empresario@example.com");
        verify(empresarioService).findByEmail("empresario@example.com");
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void loginhNonExistingUsern() {
        when(clienteService.findByEmail(anyString())).thenReturn(null);
        when(empresarioService.findByEmail(anyString())).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authController.login(loginRequest);
        });
        
        assertEquals("El usuario introducido no se encuentra dado de alta en la plataforma", exception.getMessage());
        
        verify(clienteService).findByEmail(anyString());
        verify(empresarioService).findByEmail(anyString());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void loginInvalidClientePassword() {
        when(clienteService.findByEmail(anyString())).thenReturn(cliente);
        doReturn(false).when(passwordEncoder).matches(any(), any());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            authController.login(loginRequest);
        });
        
        assertEquals("Credenciales incorrectas", exception.getMessage());
        
        verify(clienteService).findByEmail(anyString());
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void loginWithInvalidEmpresarioPasswordShouldThrowUnauthorizedException() {
        when(clienteService.findByEmail(anyString())).thenReturn(null);
        when(empresarioService.findByEmail(anyString())).thenReturn(empresario);
        doReturn(false).when(passwordEncoder).matches(any(), any());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            authController.login(loginRequest);
        });
        
        assertEquals("Credenciales incorrectas", exception.getMessage());
        
        verify(clienteService).findByEmail(anyString());
        verify(empresarioService).findByEmail(anyString());
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void loginConvertEmailToLowerCase() {
        loginRequest.put("email", "CLIENTE@EXAMPLE.COM");
        when(clienteService.findByEmail("cliente@example.com")).thenReturn(cliente);
        doReturn(true).when(passwordEncoder).matches(any(), any());

        ResponseEntity<?> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(clienteService).findByEmail("cliente@example.com");
    }
}