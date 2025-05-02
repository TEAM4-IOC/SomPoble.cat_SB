package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.dto.EmailDTO;
import com.sompoble.cat.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.jdbc.core.RowMapper;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Implementación del servicio de envío de correos electrónicos.
 * <p>
 * Utiliza Spring Mail y Thymeleaf para enviar emails basados en una plantilla
 * HTML.
 * </p>
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private ResourceLoader resourceLoader;
    private final JdbcTemplate jdbcTemplate;

    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine,
            ResourceLoader resourceLoader, JdbcTemplate jdbcTemplate) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.resourceLoader = resourceLoader;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Envía un email basado en una notificación utilizando una plantilla HTML.
     *
     * @param notificacion La notificación que contiene los detalles del
     * mensaje.
     */
    @Override
    public void sendNotificationEmail(Notificacion notificacion) {
        String subject = generateSubject(notificacion.getTipo());
        String recipient = getRecipientEmail(notificacion);

        // Procesar la plantilla HTML
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", notificacion.getMensaje());

        String htmlContent = templateEngine.process("email", context);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Indica que el contenido es HTML

            Resource logoResource = resourceLoader.getResource("classpath:templates/SomPoble.png");
            helper.addInline("logoImage", logoResource);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el email", e);
        }
    }

    /**
     * Genera el asunto del email según el tipo de notificación.
     *
     * @param tipo El tipo de notificación.
     * @return El asunto del email.
     */
    @Override
    public String generateSubject(Notificacion.TipoNotificacion tipo) {
        return switch (tipo) {
            case INFORMACION ->
                "Información sobre su reserva - SomPoble";
            case ADVERTENCIA ->
                "⚠️ Acción requerida - SomPoble";
            case ERROR ->
                "Error en actualizar reserva - SomPoble";
        };
    }

    /**
     * Obtiene el email del destinatario (cliente o empresario) asociado a la
     * notificación.
     *
     * @param notificacion La notificación que contiene los datos del
     * destinatario.
     * @return El email del destinatario.
     */
    @Override
    public String getRecipientEmail(Notificacion notificacion) {
        if (notificacion.esParaCliente()) {
            return notificacion.getCliente().getEmail();
        } else {
            return notificacion.getEmpresario().getEmail();
        }
    }

    /**
     * Envía un correo electrónico utilizando una plantilla HTML y un recurso
     * embebido.
     * <p>
     * Este método recibe un objeto {@link EmailDTO} que contiene el
     * destinatario, asunto y mensaje del correo. Se utiliza el motor de
     * plantillas {@code TemplateEngine} para procesar el contenido dinámico del
     * correo y se añade una imagen (logo) de forma embebida en el cuerpo del
     * correo.
     * </p>
     * <p>
     * Si ocurre algún error durante la creación o el envío del correo, se lanza
     * una {@link RuntimeException}.
     * </p>
     *
     * @param email el objeto {@link EmailDTO} que contiene los datos necesarios
     * para enviar el correo
     * @throws MessagingException si ocurre un error relacionado con el manejo
     * del mensaje
     */
    //Creamos el método para enviar el correo
    @Override
    public void sendMail(EmailDTO email) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email.getDestinatario());
            helper.setSubject(email.getAsunto());

            Context context = new Context();
            context.setVariable("message", email.getMensaje());
            String contentHtml = templateEngine.process("email", (IContext) context);

            helper.setText(contentHtml, true);

            Resource logoResource = resourceLoader.getResource("classpath:templates/SomPoble.png");
            helper.addInline("logoImage", logoResource);

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);

        }

    }

    /**
     * Envía recordatorios automáticos por correo electrónico para las reservas
     * próximas.
     * <p>
     * Este método consulta la base de datos en busca de reservas cuya fecha
     * esté entre el día actual y el siguiente día. Por cada reserva encontrada,
     * se genera una notificación de tipo "información" y se envía un correo
     * electrónico al cliente asociado a la reserva.
     * </p>
     * <p>
     * El envío de correos se realiza mediante el método
     * {@code sendNotificationEmail(Notificacion notificacion)}.
     * </p>
     */
    @Override
    public void sendAutomaticReminders() {
        // Consultar reservas próximas
        String sql = "SELECT ID_RESERVA, FECHA, HORA FROM RESERVA WHERE FECHA >= CURDATE() AND FECHA <= DATE_ADD(CURDATE(), INTERVAL 1 DAY)";
        List<Reserva> reservasProximas = jdbcTemplate.query(sql, new ReservaRowMapper());

        for (Reserva reserva : reservasProximas) {
            Notificacion notificacion = new Notificacion();
            notificacion.setTipo(Notificacion.TipoNotificacion.INFORMACION);
            notificacion.setMensaje("Recuerda que tienes una reserva programada para el " + reserva.getFechaReserva());
            notificacion.setCliente(reserva.getCliente());

            // Enviar notificación por email
            sendNotificationEmail(notificacion);
        }
    }

    /**
     * RowMapper para mapear los resultados de la consulta a objetos Reserva.
     */
    private static class ReservaRowMapper implements RowMapper {

        public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reserva reserva = new Reserva();

            // Mapear ID de reserva
            reserva.setIdReserva(rs.getLong("ID_RESERVA"));

            // Parsear fecha desde String a LocalDate
            String fechaStr = rs.getString("FECHA");
            if (fechaStr != null && !fechaStr.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate fecha = LocalDate.parse(fechaStr, formatter);
                    reserva.setFechaReserva(fecha.toString()); // Convertir de nuevo a String
                } catch (DateTimeParseException e) {
                    throw new RuntimeException("Error al parsear la fecha: " + fechaStr, e);
                }
            }

            // Parsear hora desde String a LocalTime
            String horaStr = rs.getString("HORA");
            if (horaStr != null && !horaStr.isEmpty()) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime hora = LocalTime.parse(horaStr, formatter);
                    reserva.setHora(hora.toString()); // Convertir de nuevo a String
                } catch (DateTimeParseException e) {
                    throw new RuntimeException("Error al parsear la hora: " + horaStr, e);
                }
            }

            // Mapear cliente
            Cliente cliente = new Cliente();
            cliente.setEmail(rs.getString("EMAIL_CLIENTE"));
            reserva.setCliente(cliente);

            return reserva;
        }

    }

    /**
     * Envía un correo electrónico de prueba en formato de texto plano.
     * <p>
     * El correo se envía a una dirección específica con un asunto y un cuerpo
     * de texto simples, sin utilizar plantillas ni contenido HTML. Se utiliza
     * para verificar que el servicio de envío de correos esté funcionando
     * correctamente.
     * </p>
     * <p>
     * En caso de error durante el proceso de envío, se captura la excepción y
     * se imprime la traza del error en la consola.
     * </p>
     */
    public void sendPlainTextTestEmail() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo("tu_correo@ejemplo.com");
            helper.setSubject("Test de envío plano");
            helper.setText("Este es un correo de prueba sin plantilla ni HTML.", false);

            javaMailSender.send(message);
            System.out.println("Correo enviado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
