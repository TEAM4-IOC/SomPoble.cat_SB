package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.exception.GlobalExceptionHandler;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Notificacion notificacion1;
    private Notificacion notificacion2;
    private Cliente cliente;
    private Empresario empresario;
    private ClienteDTO clienteDTO;
    private List<Notificacion> notificaciones;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        // Configuración de datos de prueba
        cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Pérez");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("678901234");
        cliente.setPass("password123");

        empresario = new Empresario();
        empresario.setDni("87654321B");
        empresario.setNombre("Maria");
        empresario.setApellidos("López");
        empresario.setEmail("maria@empresa.com");
        empresario.setTelefono("654321987");
        empresario.setPass("password456");

        clienteDTO = new ClienteDTO(
                1L, // idPersona
                "12345678A", // dni
                "Juan", // nombre
                "Pérez", // apellidos
                "juan@ejemplo.com", // email
                "678901234", // telefono
                "password123", // pass
                new ArrayList<>(), // reservasIds
                Arrays.asList(1L, 2L) // notificacionesIds
        );

        notificacion1 = new Notificacion(cliente, null, "Notificación para cliente", Notificacion.TipoNotificacion.INFORMACION);
        // Simular ID generado
        try {
            java.lang.reflect.Field field = notificacion1.getClass().getDeclaredField("idNotificacion");
            field.setAccessible(true);
            field.set(notificacion1, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        notificacion2 = new Notificacion(cliente, null, "Otra notificación", Notificacion.TipoNotificacion.ADVERTENCIA);
        // Simular ID generado
        try {
            java.lang.reflect.Field field = notificacion2.getClass().getDeclaredField("idNotificacion");
            field.setAccessible(true);
            field.set(notificacion2, 2L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        notificaciones = new ArrayList<>();
        notificaciones.add(notificacion1);
        notificaciones.add(notificacion2);
    }

    @Test
    public void testObtenerNotificacionesPorIdentificador() throws Exception {
        // Configurar comportamiento del mock
        when(notificationService.findNotificationsByIdentificador("12345678A")).thenReturn(notificaciones);

        // Ejecutar y verificar
        mockMvc.perform(get("/api/notifications/obtener")
                .param("identificador", "12345678A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].mensaje", is("Notificación para cliente")))
                .andExpect(jsonPath("$[1].mensaje", is("Otra notificación")));

        verify(notificationService, times(1)).findNotificationsByIdentificador("12345678A");
    }

    @Test
    public void testObtenerNotificacionesPorIdentificadorNoEncontradas() throws Exception {
        // Configurar comportamiento del mock
        when(notificationService.findNotificationsByIdentificador("00000000X")).thenReturn(Collections.emptyList());

        // Ejecutar y verificar
        mockMvc.perform(get("/api/notifications/obtener")
                .param("identificador", "00000000X"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(containsString("No hay notificaciones para este identificador")));

        verify(notificationService, times(1)).findNotificationsByIdentificador("00000000X");
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        // Configurar comportamiento del mock
        when(notificationService.getAllNotifications()).thenReturn(notificaciones);

        // Ejecutar y verificar
        mockMvc.perform(get("/api/notifications/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].mensaje", is("Notificación para cliente")))
                .andExpect(jsonPath("$[1].mensaje", is("Otra notificación")));

        verify(notificationService, times(1)).getAllNotifications();
    }

    @Test
    public void testGetNotificationById() throws Exception {
        // Configurar comportamiento del mock
        when(notificationService.findNotificationById(1L)).thenReturn(notificacion1);

        // Ejecutar y verificar
        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Notificación para cliente")))
                .andExpect(jsonPath("$.tipo", is("INFORMACION")));

        verify(notificationService, times(1)).findNotificationById(1L);
    }

    @Test
    public void testGetNotificationByIdNotFound() throws Exception {
        // Configurar comportamiento del mock
        when(notificationService.findNotificationById(999L)).thenThrow(new ResourceNotFoundException("Notificación con ID 999 no encontrada"));

        // Ejecutar y verificar
        mockMvc.perform(get("/api/notifications/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Notificación con ID 999 no encontrada")));

        verify(notificationService, times(1)).findNotificationById(999L);
    }

    @Test
    public void testDeleteNotification() throws Exception {
        // Configurar comportamiento del mock
        doNothing().when(notificationService).deleteNotificationById(1L);

        // Ejecutar y verificar
        mockMvc.perform(delete("/api/notifications/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificación eliminada correctamente."));

        verify(notificationService, times(1)).deleteNotificationById(1L);
    }

    @Test
    public void testDeleteNotificationNotFound() throws Exception {
        // Configurar comportamiento del mock
        doThrow(new ResourceNotFoundException("Notificación con ID 999 no encontrada"))
                .when(notificationService).deleteNotificationById(999L);

        // Ejecutar y verificar
        mockMvc.perform(delete("/api/notifications/delete/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Notificación con ID 999 no encontrada")));

        verify(notificationService, times(1)).deleteNotificationById(999L);
    }
}
