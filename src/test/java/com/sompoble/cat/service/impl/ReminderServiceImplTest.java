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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReminderServiceImpl reminderService;

    private Reserva reserva1;
    private Reserva reserva2;

    @BeforeEach
    void setUp() {
        // Mockear dependencias necesarias
        Empresa empresaMock = mock(Empresa.class);
        Servicio servicioMock = mock(Servicio.class);
        Cliente cliente1 = mock(Cliente.class);
        Cliente cliente2 = mock(Cliente.class);

        // Crear reservas con datos válidos
        reserva1 = new Reserva(
                empresaMock,
                cliente1,
                servicioMock,
                "2024-05-01",
                "10:00",
                "Pendiente"
        );
        reserva1.setIdReserva(1L);

        reserva2 = new Reserva(
                empresaMock,
                cliente2,
                servicioMock,
                "2024-05-02",
                "15:30",
                "Confirmada"
        );
        reserva2.setIdReserva(2L);

        // Simular respuesta del repositorio
        when(reservaRepository.findUpcomingReservations())
                .thenReturn(Arrays.asList(reserva1, reserva2));
    }

    @Test
    void testProcessReminders_CreaNotificacionesYEnviaCorreos() {
        // Ejecutar el método
        reminderService.processReminders();

        // Verificar llamadas a los servicios
        verify(notificationService, times(2)).saveNotification(any());
        verify(emailService, times(2)).sendNotificationEmail(any());
    }

    @Test
    void testProcessReminders_SinReservas_NoHaceNada() {
        // Simular que no hay reservas
        when(reservaRepository.findUpcomingReservations()).thenReturn(List.of());

        // Ejecutar el método
        reminderService.processReminders();

        // Verificar que no se llamó a los servicios
        verify(notificationService, never()).saveNotification(any());
        verify(emailService, never()).sendNotificationEmail(any());
    }
}