package com.sompoble.cat.service.impl;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.repository.ReservaRepository;
import com.sompoble.cat.service.EmailService;
import com.sompoble.cat.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.sompoble.cat.domain.Notificacion; 
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reserva reserva;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Crear un cliente mock con datos de Gemma
        cliente = mock(Cliente.class);
      

        // Crear una reserva válida con Gemma como cliente
        Empresa empresaMock = mock(Empresa.class);
        Servicio servicioMock = mock(Servicio.class);
        reserva = new Reserva(
            empresaMock,
            cliente,
            servicioMock,
            "2024-05-01",
            "10:00",
            "Pendiente"
        );
        reserva.setIdReserva(null); // Simular nueva reserva

        // Crear una notificación válida con Gemma como cliente
        Notificacion notificacion = new Notificacion(
            cliente, // Cliente no nulo (Gemma)
            null, // Empresario nulo
            "Mensaje de prueba",
            Notificacion.TipoNotificacion.INFORMACION
        );

       
    }

    @Test
    void testCreateOrUpdateReservation_NuevaReserva() {
        // Ejecutar método
        reservationService.createOrUpdateReservation(reserva);

        // Verificar que se llamó a addReserva
        verify(reservaRepository, times(1)).addReserva(reserva);

        // Verificar que se creó una notificación y se envió correo
        verify(notificationService, times(1)).saveNotification(any(Notificacion.class));
        verify(emailService, times(1)).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    void testCreateOrUpdateReservation_ExistenteReserva() {
        // Simular que la reserva ya tiene ID
        reserva.setIdReserva(1L);

        // Ejecutar método
        reservationService.createOrUpdateReservation(reserva);

        // Verificar que se llamó a updateReserva
        verify(reservaRepository, times(1)).updateReserva(reserva);

        // Verificar que se creó una notificación y se envió correo
        verify(notificationService, times(1)).saveNotification(any(Notificacion.class));
        verify(emailService, times(1)).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    void testCancelReservation_ReservaExistente() {
        // Simular que la reserva existe
        when(reservaRepository.findByIdFull(1L)).thenReturn(reserva);

        // Ejecutar método
        reservationService.cancelReservation(1L);

        // Verificar que se llamó a deleteById
        verify(reservaRepository, times(1)).deleteById(1L);

        // Verificar que se creó una notificación y se envió correo
        verify(notificationService, times(1)).saveNotification(any(Notificacion.class));
        verify(emailService, times(1)).sendNotificationEmail(any(Notificacion.class));
    }

    @Test
    void testCancelReservation_ReservaNoExistente() {
        // Simular que la reserva no existe
        when(reservaRepository.findByIdFull(1L)).thenReturn(null);

        // Ejecutar método
        reservationService.cancelReservation(1L);

        // Verificar que no se llamó a deleteById
        verify(reservaRepository, never()).deleteById(1L);

        // Verificar que no se creó notificación ni se envió correo
        verify(notificationService, never()).saveNotification(any());
        verify(emailService, never()).sendNotificationEmail(any());
    }

    @Test
    void testNotifyReservationChange_ReservaExistente() {
        // Simular que la reserva existe
        when(reservaRepository.findByIdFull(1L)).thenReturn(reserva);

        // Crear una notificación válida con datos específicos
        Notificacion notificacion = new Notificacion(
            cliente, // Cliente mockeado
            null, // Empresario nulo
            "Su reserva ha sido actualizada", // Mensaje específico
            Notificacion.TipoNotificacion.INFORMACION // Tipo de notificación
        );

      
        // Ejecutar método
        reservationService.notifyReservationChange(1L);

        // Verificar que se creó una notificación con los datos esperados
        verify(notificationService, times(1))
            .saveNotification(argThat(n -> 
                n.getMensaje().equals("Su reserva ha sido actualizada") && 
                n.getTipo().equals(Notificacion.TipoNotificacion.INFORMACION)
            ));

        // Verificar que se envió un correo electrónico con los datos esperados
        verify(emailService, times(1))
            .sendNotificationEmail(argThat(n -> 
                n.getMensaje().equals("Su reserva ha sido actualizada") && 
                n.getTipo().equals(Notificacion.TipoNotificacion.INFORMACION)
            ));
    }
    
    
    @Test
    void testNotifyReservationChange_ReservaNoExistente() {
        // Simular que la reserva no existe
        when(reservaRepository.findByIdFull(1L)).thenReturn(null);

        // Ejecutar método
        reservationService.notifyReservationChange(1L);

        // Verificar que no se creó notificación ni se envió correo
        verify(notificationService, never()).saveNotification(any());
        verify(emailService, never()).sendNotificationEmail(any());
    }

    @Test
    void testGetReservationById_ValidId() {
        // Simular que la reserva existe
        when(reservaRepository.findByIdFull(1L)).thenReturn(reserva);

        // Ejecutar método
        Reserva result = reservationService.getReservationById(1L);

        // Verificar que se devolvió la reserva correcta
        verify(reservaRepository, times(1)).findByIdFull(1L);
        assert result == reserva;
    }

    @Test
    void testGetReservationById_NullId() {
        // Ejecutar método
        // Debería lanzar IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getReservationById(null);
        });

        // Verificar que no se llamó a findByIdFull
        verify(reservaRepository, never()).findByIdFull(null);
    }
}