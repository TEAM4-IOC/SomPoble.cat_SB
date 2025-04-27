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

@Service
public class ReminderServiceImpl implements ReminderService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;
    /**
     * Envía recordatorios automáticos para reservas próximas.
     * Este método se ejecuta de forma programada (ej.: diariamente a las 8:00 AM).
     */
    @Override
    @Scheduled(cron = "0 0 8 * * ?", zone = "Europe/Madrid")
    public void sendDailyReminders() {
        processReminders();
    }

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