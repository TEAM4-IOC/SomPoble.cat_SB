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

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReminderService reminderService;

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
        doThrow(new MessagingException("Error al enviar correo")).when(emailService).sendMail(any(EmailDTO.class));

        mockMvc.perform(post("/api/email/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al enviar correo personalizado"));

        verify(emailService, times(1)).sendMail(any(EmailDTO.class));
        verify(notificationService, never()).saveNotification(any(Notificacion.class));
        verify(emailService, never()).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    public void testNotifyReservationChange() throws Exception {
        when(reservationService.getReservationById(1L)).thenReturn(reserva);
        doNothing().when(notificationService).saveNotification(any(Notificacion.class));
        doNothing().when(emailService).sendNotificationEmail(any(Notificacion.class));

        mockMvc.perform(post("/api/email/notify-reservation-change/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificación de cambio de reserva enviada"));

        verify(reservationService, times(1)).getReservationById(1L);
        verify(notificationService, times(1)).saveNotification(any(Notificacion.class));
        verify(emailService, times(1)).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    public void testNotifyReservationChangeReservaNoEncontrada() throws Exception {
        when(reservationService.getReservationById(999L)).thenReturn(null);

        mockMvc.perform(post("/api/email/notify-reservation-change/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reserva no encontrada"));

        verify(reservationService, times(1)).getReservationById(999L);
        verify(notificationService, never()).saveNotification(any(Notificacion.class));
        verify(emailService, never()).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    public void testNotifyReservationChangeWithException() throws Exception {
        when(reservationService.getReservationById(1L)).thenThrow(new RuntimeException("Error al obtener reserva"));

        mockMvc.perform(post("/api/email/notify-reservation-change/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al notificar cambio de reserva"));

        verify(reservationService, times(1)).getReservationById(1L);
        verify(notificationService, never()).saveNotification(any(Notificacion.class));
        verify(emailService, never()).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    public void testSendAutomaticReminders() throws Exception {
        doNothing().when(reminderService).processReminders();

        mockMvc.perform(post("/api/email/send-automatic-reminders"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recordatorios enviados exitosamente"));

        verify(reminderService, times(1)).processReminders();
    }

    @Test
    public void testSendAutomaticRemindersWithException() throws Exception {
        doThrow(new RuntimeException("Error al procesar recordatorios")).when(reminderService).processReminders();

        mockMvc.perform(post("/api/email/send-automatic-reminders"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al enviar recordatorios"));

        verify(reminderService, times(1)).processReminders();
    }

    @Test
    public void testTestPlainMail() throws Exception {
        doNothing().when(emailService).sendPlainTextTestEmail();

        mockMvc.perform(post("/api/email/test-plain"))
                .andExpect(status().isOk())
                .andExpect(content().string("Correo de prueba enviado"));

        verify(emailService, times(1)).sendPlainTextTestEmail();
    }
}
