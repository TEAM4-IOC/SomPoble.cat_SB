package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.dto.EmailDTO;
import com.sompoble.cat.service.EmailService;
import com.sompoble.cat.service.NotificationService;
import com.sompoble.cat.service.ReminderService;
import com.sompoble.cat.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private ReminderService reminderService;

    /**
     * Envía un correo electrónico personalizado basado en un DTO,
     * luego guarda la notificación y reenvía por email la notificación.
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendCustomEmail(@RequestBody EmailDTO emailDTO) {
        try {
            // 1) Envío principal
            emailService.sendMail(emailDTO);

            // 2) Registro en Notificacion
            Notificacion not1 = new Notificacion(
                /* cliente: */ null,
                /* reserva: */ null,
                /* mensaje: */ "Se ha enviado correo personalizado a " + emailDTO.getDestinatario(),
                Notificacion.TipoNotificacion.INFORMACION
            );
            notificationService.saveNotification(not1);

            // 3) Reenvío de la notificación por email
            emailService.sendNotificationEmail(not1);

            return ResponseEntity.ok("Correo enviado y notificación registrada");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al enviar correo personalizado");
        }
    }

    /**
     * Notifica cambios en una reserva.
     *
     * @param reservaId El ID de la reserva que ha cambiado.
     * @return Una respuesta HTTP indicando el éxito de la operación.
     */
    @PostMapping("/notify-reservation-change/{reservaId}")
    public ResponseEntity<String> notifyReservationChange(@PathVariable Long reservaId) {
        try {
            Reserva reserva = reservationService.getReservationById(reservaId);
            if (reserva == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
            }

            Notificacion not2 = new Notificacion(
                reserva.getCliente(),
                null,
                "Se ha modificado su reserva",
                Notificacion.TipoNotificacion.INFORMACION
            );
            notificationService.saveNotification(not2);
            emailService.sendNotificationEmail(not2);

            return ResponseEntity.ok("Notificación de cambio de reserva enviada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al notificar cambio de reserva");
        }
    }

    /**
     * Configura y envía recordatorios automáticos para reservas próximas.
     *
     * @return Una respuesta HTTP indicando el éxito de la operación.
     */
    @PostMapping("/send-automatic-reminders")
    public ResponseEntity<String> sendAutomaticReminders() {
        try {
            reminderService.processReminders(); 

            return new ResponseEntity<>("Recordatorios enviados exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al enviar recordatorios", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Configura notificaciones: guarda un registro en Notificacion
     * y envía un email resumen de la nueva configuración.
     */
    @PostMapping("/configure-notifications")
    public ResponseEntity<String> configureNotifications(@RequestBody Map<String, Object> cfg) {
        try {
            // 1) Extraer parámetros de configuración
            Boolean enabled = cfg.containsKey("enabled") ? (Boolean) cfg.get("enabled") : null;
            String frequency = cfg.containsKey("frequency") ? cfg.get("frequency").toString() : null;
            String sendTime = cfg.containsKey("sendTime") ? cfg.get("sendTime").toString() : null;

            // 2) Registrar la notificación de configuración
            String msg = String.format(
                "Configuración actualizada: enabled=%s, frequency=%s, sendTime=%s",
                enabled, frequency, sendTime
            );
            Notificacion notConfig = new Notificacion(
                null,
                null,
                msg,
                Notificacion.TipoNotificacion.INFORMACION
            );
            notificationService.saveNotification(notConfig);

            // 3) Enviar el email de notificación
            emailService.sendNotificationEmail(notConfig);

            return ResponseEntity.ok("Configuración aplicada y notificación enviada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al configurar notificaciones");
        }
    }
    	//PARA PRUEBAS INTERNAS DE BACK
    @PostMapping("/test-plain")
    public ResponseEntity<String> testPlainMail() {
        emailService.sendPlainTextTestEmail();
        return new ResponseEntity<>("Correo de prueba enviado", HttpStatus.OK);
    }
    
    
}