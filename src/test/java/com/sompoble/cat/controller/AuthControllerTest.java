package com.sompoble.cat.controller;

import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.dto.EmpresarioDTO;
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

import java.util.ArrayList;
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

    private ClienteDTO clienteDTO;
    private EmpresarioDTO empresarioDTO;
    private Map<String, String> loginRequest;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO(
                1L,
                "12345678A",
                "Juan",
                "Pérez",
                "cliente@example.com",
                "650180800",
                "hashedPassword1",
                new ArrayList<>(),
                new ArrayList<>()
        );

        empresarioDTO = new EmpresarioDTO(
                2L,
                "87654321B",
                "Carlos",
                "López",
                "empresario@example.com",
                "650180801",
                "hashedPassword2",
                new ArrayList<>(),
                new ArrayList<>()
        );

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
        when(clienteService.findByEmail("cliente@example.com")).thenReturn(clienteDTO);
        doReturn(true).when(passwordEncoder).matches(any(), any());

        ResponseEntity<?> response = authController.login(loginRequest);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, responseBody.get("status"));
        assertEquals("Inicio de sesión exitoso", responseBody.get("message"));
        assertEquals(1, responseBody.get("tipoUsuario"));
        assertEquals(clienteDTO, responseBody.get("usuario"));
        
        verify(clienteService).findByEmail("cliente@example.com");
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void loginValidEmpresario() {
        loginRequest.put("email", "empresario@example.com");
        when(clienteService.findByEmail("empresario@example.com")).thenReturn(null);
        when(empresarioService.findByEmail("empresario@example.com")).thenReturn(empresarioDTO);
        doReturn(true).when(passwordEncoder).matches(any(), any());

        ResponseEntity<?> response = authController.login(loginRequest);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, responseBody.get("status"));
        assertEquals("Inicio de sesión exitoso", responseBody.get("message"));
        assertEquals(2, responseBody.get("tipoUsuario"));
        assertEquals(empresarioDTO, responseBody.get("usuario"));
        
        verify(clienteService).findByEmail("empresario@example.com");
        verify(empresarioService).findByEmail("empresario@example.com");
        verify(passwordEncoder).matches(any(), any());
    }
    
    @Test
    void loginValidClienteAndEmpresario() {
        loginRequest.put("email", "both@example.com");
        when(clienteService.findByEmail("both@example.com")).thenReturn(clienteDTO);
        when(empresarioService.findByEmail("both@example.com")).thenReturn(empresarioDTO);
        doReturn(true).when(passwordEncoder).matches(any(), any());

        ResponseEntity<?> response = authController.login(loginRequest);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, responseBody.get("status"));
        assertEquals("Inicio de sesión exitoso", responseBody.get("message"));
        assertEquals(3, responseBody.get("tipoUsuario"));
        assertEquals(clienteDTO, responseBody.get("usuario"));
        
        verify(clienteService).findByEmail("both@example.com");
        verify(empresarioService).findByEmail("both@example.com");
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void loginNonExistingUser() {
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
        when(clienteService.findByEmail(anyString())).thenReturn(clienteDTO);
        doReturn(false).when(passwordEncoder).matches(any(), any());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            authController.login(loginRequest);
        });
        
        assertEquals("Credenciales incorrectas", exception.getMessage());
        
        verify(clienteService).findByEmail(anyString());
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void loginInvalidEmpresarioPassword() {
        when(clienteService.findByEmail(anyString())).thenReturn(null);
        when(empresarioService.findByEmail(anyString())).thenReturn(empresarioDTO);
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
        when(clienteService.findByEmail("cliente@example.com")).thenReturn(clienteDTO);
        doReturn(true).when(passwordEncoder).matches(any(), any());

        ResponseEntity<?> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(clienteService).findByEmail("cliente@example.com");
    }
}