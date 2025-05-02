package com.sompoble.cat.service;

import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.dto.EmailDTO;
import jakarta.mail.MessagingException;

/**
 * Interfaz que define los servicios relacionados con el envío de correos
 * electrónicos.
 * <p>
 * Esta interfaz proporciona métodos para gestionar diferentes tipos de correos
 * electrónicos en la aplicación, incluyendo notificaciones, recordatorios
 * automáticos y correos personalizados. Utiliza la API de Jakarta Mail para el
 * envío de mensajes.
 * </p>
 */
public interface EmailService {

    /**
     * Envía un email basado en una notificación.
     *
     * @param notificacion La notificación que contiene los detalles del
     * mensaje.
     */
    void sendNotificationEmail(Notificacion notificacion);

    /**
     * Genera el asunto del email según el tipo de notificación.
     *
     * @param tipo El tipo de notificación.
     * @return El asunto del email.
     */
    String generateSubject(Notificacion.TipoNotificacion tipo);

    /**
     * Obtiene el email del destinatario (cliente o empresario) asociado a la
     * notificación.
     *
     * @param notificacion La notificación que contiene los datos del
     * destinatario.
     * @return El email del destinatario.
     */
    String getRecipientEmail(Notificacion notificacion);

    /**
     * Envía un correo electrónico basado en la información proporcionada en el
     * DTO.
     *
     * @param email el objeto {@link EmailDTO} que contiene los detalles del
     * correo a enviar
     * @throws MessagingException si ocurre un error al enviar el correo
     */
    void sendMail(EmailDTO email) throws MessagingException;

    /**
     * Envía recordatorios automáticos por correo electrónico según la
     * programación definida.
     * <p>
     * Este método es invocado por el planificador de tareas para enviar
     * recordatorios a los usuarios según las reglas de negocio establecidas.
     * </p>
     */
    void sendAutomaticReminders();

    /**
     * Envía un correo electrónico de prueba en texto plano para verificar el
     * servicio de correo.
     * <p>
     * Método utilizado principalmente para propósitos de prueba y diagnóstico
     * del sistema de correo electrónico.
     * </p>
     */
    void sendPlainTextTestEmail();
}
