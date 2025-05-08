package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.dto.EmailDTO;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmailService;
import com.sompoble.cat.service.NotificationService;
import com.sompoble.cat.service.ReminderService;
import com.sompoble.cat.service.ReservationService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    
    @Mock
    private ClienteService clienteService;
    
    

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

  

    @Test
    void testSendCustomEmail_messagingException() throws MessagingException {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setDestinatario("error@example.com");

        doThrow(MessagingException.class).when(emailService).sendMail(emailDTO);

        ResponseEntity<String> response = emailController.sendCustomEmail(emailDTO);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error al enviar correo personalizado", response.getBody());
    }

    
    @Test
    void testNotifyReservationChange_notFound() {
        when(reservationService.getReservationById(999L)).thenReturn(null);

        ResponseEntity<String> response = emailController.notifyReservationChange(999L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Reserva no encontrada", response.getBody());
    }

    @Test
    void testSendAutomaticReminders_success() {
        ResponseEntity<String> response = emailController.sendAutomaticReminders();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Recordatorios enviados exitosamente", response.getBody());
        verify(reminderService).processReminders();
    }

  

    @Test
    void testTestPlainMail_success() {
        ResponseEntity<String> response = emailController.testPlainMail();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Correo de prueba enviado", response.getBody());
        verify(emailService).sendPlainTextTestEmail();
    }
}
