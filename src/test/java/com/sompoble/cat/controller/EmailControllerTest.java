package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.dto.EmailDTO;
import com.sompoble.cat.exception.GlobalExceptionHandler;
import com.sompoble.cat.service.EmailService;
import com.sompoble.cat.service.NotificationService;
import com.sompoble.cat.service.ReminderService;
import com.sompoble.cat.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReminderService reminderService;

    @InjectMocks
    private EmailController emailController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private EmailDTO emailDTO;
    private Reserva reserva;
    private Cliente cliente;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(emailController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        // Configurar datos de prueba
        emailDTO = new EmailDTO();
        emailDTO.setDestinatario("test@example.com");
        emailDTO.setAsunto("Asunto de prueba");
        emailDTO.setAsunto("Contenido del correo de prueba");

        cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Pérez");
        cliente.setEmail("juan@example.com");
        cliente.setTelefono("678901234");
        cliente.setPass("password123");

        reserva = new Reserva();
        // Configurar datos básicos de la reserva
        reserva.setCliente(cliente);
        try {
            java.lang.reflect.Field field = reserva.getClass().getDeclaredField("idReserva");
            field.setAccessible(true);
            field.set(reserva, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendCustomEmailWithException() throws Exception {
        // Configurar comportamiento de los mocks para simular una excepción
        doThrow(new MessagingException("Error al enviar correo")).when(emailService).sendMail(any(EmailDTO.class));

        // Ejecutar y verificar
        mockMvc.perform(post("/api/email/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al enviar correo personalizado"));

        // Verificar que el método que causó la excepción fue llamado
        verify(emailService, times(1)).sendMail(any(EmailDTO.class));
        // Verificar que los métodos posteriores no fueron llamados
        verify(notificationService, never()).saveNotification(any(Notificacion.class));
        verify(emailService, never()).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    public void testNotifyReservationChange() throws Exception {
        // Configurar comportamiento de los mocks
        when(reservationService.getReservationById(1L)).thenReturn(reserva);
        doNothing().when(notificationService).saveNotification(any(Notificacion.class));
        doNothing().when(emailService).sendNotificationEmail(any(Notificacion.class));

        // Ejecutar y verificar
        mockMvc.perform(post("/api/email/notify-reservation-change/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificación de cambio de reserva enviada"));

        // Verificar que los métodos de los servicios fueron llamados correctamente
        verify(reservationService, times(1)).getReservationById(1L);
        verify(notificationService, times(1)).saveNotification(any(Notificacion.class));
        verify(emailService, times(1)).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    public void testNotifyReservationChangeReservaNoEncontrada() throws Exception {
        // Configurar comportamiento de los mocks para simular reserva no encontrada
        when(reservationService.getReservationById(999L)).thenReturn(null);

        // Ejecutar y verificar
        mockMvc.perform(post("/api/email/notify-reservation-change/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reserva no encontrada"));

        // Verificar que el método que busca la reserva fue llamado
        verify(reservationService, times(1)).getReservationById(999L);
        // Verificar que los métodos posteriores no fueron llamados
        verify(notificationService, never()).saveNotification(any(Notificacion.class));
        verify(emailService, never()).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    public void testNotifyReservationChangeWithException() throws Exception {
        // Configurar comportamiento de los mocks para simular una excepción
        when(reservationService.getReservationById(1L)).thenThrow(new RuntimeException("Error al obtener reserva"));

        // Ejecutar y verificar
        mockMvc.perform(post("/api/email/notify-reservation-change/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al notificar cambio de reserva"));

        // Verificar que el método que causó la excepción fue llamado
        verify(reservationService, times(1)).getReservationById(1L);
        // Verificar que los métodos posteriores no fueron llamados
        verify(notificationService, never()).saveNotification(any(Notificacion.class));
        verify(emailService, never()).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    public void testSendAutomaticReminders() throws Exception {
        // Configurar comportamiento de los mocks
        doNothing().when(reminderService).processReminders();

        // Ejecutar y verificar
        mockMvc.perform(post("/api/email/send-automatic-reminders"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recordatorios enviados exitosamente"));

        // Verificar que el método del servicio fue llamado
        verify(reminderService, times(1)).processReminders();
    }

    @Test
    public void testSendAutomaticRemindersWithException() throws Exception {
        // Configurar comportamiento de los mocks para simular una excepción
        doThrow(new RuntimeException("Error al procesar recordatorios")).when(reminderService).processReminders();

        // Ejecutar y verificar
        mockMvc.perform(post("/api/email/send-automatic-reminders"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al enviar recordatorios"));

        // Verificar que el método que causó la excepción fue llamado
        verify(reminderService, times(1)).processReminders();
    }

    @Test
    public void testTestPlainMail() throws Exception {
        // Configurar comportamiento de los mocks
        doNothing().when(emailService).sendPlainTextTestEmail();

        // Ejecutar y verificar
        mockMvc.perform(post("/api/email/test-plain"))
                .andExpect(status().isOk())
                .andExpect(content().string("Correo de prueba enviado"));

        // Verificar que el método del servicio fue llamado
        verify(emailService, times(1)).sendPlainTextTestEmail();
    }
}
