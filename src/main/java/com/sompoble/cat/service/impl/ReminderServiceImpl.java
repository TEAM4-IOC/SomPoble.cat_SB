package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.repository.ReservaRepository;
import com.sompoble.cat.service.EmailService;
import com.sompoble.cat.service.NotificationService;
import com.sompoble.cat.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementación del servicio de recordatorios automáticos.
 * <p>
 * Esta clase proporciona la implementación concreta de la interfaz
 * {@link ReminderService}, gestionando el envío de recordatorios a los clientes
 * sobre sus reservas próximas mediante notificaciones y correos electrónicos
 * programados.
 * </p>
 */
@Service
public class ReminderServiceImpl implements ReminderService {

    /**
     * Repositorio para acceder a los datos de reservas.
     */
    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Servicio para la gestión de notificaciones.
     */
    @Autowired
    private NotificationService notificationService;

    /**
     * Servicio para el envío de correos electrónicos.
     */
    @Autowired
    private EmailService emailService;

    /**
     * Envía recordatorios automáticos para reservas próximas. Este método se
     * ejecuta de forma programada diariamente a las 8:00 AM en la zona horaria
     * de Europa/Madrid.
     */
    @Override
    @Scheduled(cron = "0 0 8 * * ?", zone = "Europe/Madrid")
    public void sendDailyReminders() {
        processReminders();
    }

    /**
     * Procesa todos los recordatorios pendientes en el sistema.
     * <p>
     * Identifica las reservas próximas a través del repositorio, crea una
     * notificación para cada una de ellas y envía correos electrónicos a los
     * clientes correspondientes.
     * </p>
     */
    @Override
    public void processReminders() {
        List<Reserva> recent = reservaRepository.findUpcomingReservations();
        for (Reserva r : recent) {
            Notificacion n = new Notificacion(
                    r.getCliente(),
                    null,
                    "Recordatorio: Su reserva está próxima",
                    Notificacion.TipoNotificacion.ADVERTENCIA
            );
            notificationService.saveNotification(n);
            emailService.sendNotificationEmail(n);
        }
    }
}
