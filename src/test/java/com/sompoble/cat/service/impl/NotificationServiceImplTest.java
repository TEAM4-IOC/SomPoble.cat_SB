package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.domain.Notificacion.TipoNotificacion;
import com.sompoble.cat.repository.NotificacionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNotification() {
        Notificacion notificacion = new Notificacion(new Cliente(), null, "Mensaje", TipoNotificacion.INFORMACION);
        try {
            var field = Notificacion.class.getDeclaredField("idNotificacion");
            field.setAccessible(true);
            field.set(notificacion, 1L);
        } catch (Exception e) {
            fail("Error al fijar ID por reflexión");
        }

        when(notificacionRepository.save(notificacion)).thenReturn(notificacion);

        notificationService.saveNotification(notificacion);

        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void testGetAllNotifications() {
        List<Notificacion> lista = List.of(new Notificacion(), new Notificacion());
        when(notificacionRepository.findAll()).thenReturn(lista);

        List<Notificacion> result = notificationService.getAllNotifications();

        assertEquals(2, result.size());
    }

    @Test
    void testFindNotificationById() {
        Notificacion noti = new Notificacion();
        when(notificacionRepository.findById(5L)).thenReturn(noti);

        Notificacion result = notificationService.findNotificationById(5L);

        assertEquals(noti, result);
    }

    @Test
    void testDeleteNotificationById() {
        notificationService.deleteNotificationById(10L);

        verify(notificacionRepository).deleteById(10L);
    }

    @Test
    void testFindNotificationsByIdentificador() {
        List<Notificacion> lista = List.of(new Notificacion());
        when(notificacionRepository.findByIdentificador("1234A")).thenReturn(lista);

        List<Notificacion> result = notificationService.findNotificationsByIdentificador("1234A");

        assertEquals(1, result.size());
        verify(notificacionRepository).findByIdentificador("1234A");
    }

    @Test
    void testFindByClienteDni() {
        List<Notificacion> lista = List.of(new Notificacion(), new Notificacion());
        when(notificacionRepository.findByClienteDni("87654321B")).thenReturn(lista);

        List<Notificacion> result = notificationService.findByClienteDni("87654321B");

        assertEquals(2, result.size());
    }

    @Test
    void testConfigSetAndGet() {
        notificationService.setConfig(true, "diaria", "08:00");

        assertTrue(notificationService.getConfigEnabled());
        assertEquals("diaria", notificationService.getConfigFrequency());
        assertEquals("08:00", notificationService.getConfigSendTime());
    }
}
