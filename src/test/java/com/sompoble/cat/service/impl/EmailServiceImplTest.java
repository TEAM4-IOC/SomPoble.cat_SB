package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.domain.Notificacion.TipoNotificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource logoResource;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        emailService = new EmailServiceImpl(javaMailSender, templateEngine, resourceLoader, null); // jdbcTemplate null si no lo usas
    }

    @Test
    void testSendNotificationEmail_success() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setEmail("cliente@correo.com");

        Notificacion notificacion = new Notificacion();
        notificacion.setCliente(cliente);
        notificacion.setTipo(TipoNotificacion.INFORMACION);
        notificacion.setMensaje("Mensaje de prueba");

        when(templateEngine.process(eq("email"), any(Context.class))).thenReturn("<html>Email content</html>");
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(resourceLoader.getResource("classpath:templates/SomPoble.png")).thenReturn(logoResource);
        when(logoResource.exists()).thenReturn(true);

        // Act
        emailService.sendNotificationEmail(notificacion);

        // Assert
        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void testGetRecipientEmail_clienteSinEmail_lanzaExcepcion() {
        Cliente cliente = new Cliente(); // sin email
        Notificacion notificacion = new Notificacion();
        notificacion.setCliente(cliente);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            emailService.getRecipientEmail(notificacion)
        );

        assertTrue(exception.getMessage().contains("no tiene un email configurado"));
    }

    @Test
    void testGenerateSubject() {
        assertEquals("Información sobre su reserva - SomPoble", 
            emailService.generateSubject(TipoNotificacion.INFORMACION));

        assertEquals("⚠️ Acción requerida - SomPoble", 
            emailService.generateSubject(TipoNotificacion.ADVERTENCIA));

        assertEquals("Error en actualizar reserva - SomPoble", 
            emailService.generateSubject(TipoNotificacion.ERROR));
    }
}
