package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.repository.ReservaRepository;
import com.sompoble.cat.service.EmailService;
import com.sompoble.cat.service.NotificationService;
import com.sompoble.cat.service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación del servicio de gestión de reservas.
 * <p>
 * Este servicio proporciona métodos para crear, actualizar, cancelar y
 * consultar reservas, así como para generar notificaciones y enviar emails
 * relacionados con las reservas.
 * </p>
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    /**
     * Crea o actualiza una reserva en el sistema.
     * <p>
     * Si la reserva no tiene un ID asignado, se considera nueva y se guarda en
     * la base de datos. Si la reserva ya tiene un ID asignado, se actualiza en
     * la base de datos. Además, genera una notificación y envía un email al
     * cliente afectado.
     * </p>
     *
     * @param reserva La reserva a crear o actualizar.
     */
    @Override
    public void createOrUpdateReservation(Reserva reserva) {
        // Determinar si la reserva es nueva o existente
        if (reserva.getIdReserva() == null) {
            // La reserva es nueva
            reservaRepository.addReserva(reserva);
        } else {
            // La reserva ya existe
            reservaRepository.updateReserva(reserva);
        }

        // Generar notificación
        Notificacion notificacion = new Notificacion(
                reserva.getCliente(),
                null,
                "Su reserva ha sido creada/actualizada",
                Notificacion.TipoNotificacion.INFORMACION
        );

        // Guardar notificación
        notificationService.saveNotification(notificacion);

        // Enviar email
        emailService.sendNotificationEmail(notificacion);
    }

    /**
     * Cancela una reserva existente.
     * <p>
     * Elimina la reserva de la base de datos, genera una notificación
     * informando al cliente sobre la cancelación y envía un email al cliente
     * afectado.
     * </p>
     *
     * @param reservaId El ID de la reserva a cancelar.
     */
    @Override
    public void cancelReservation(Long reservaId) {
        Optional<Reserva> optionalReserva = Optional.ofNullable(reservaRepository.findByIdFull(reservaId));
        if (optionalReserva.isPresent()) {
            Reserva reserva = optionalReserva.get();

            // Eliminar la reserva
            reservaRepository.deleteById(reservaId);

            // Generar notificación
            Notificacion notificacion = new Notificacion(
                    reserva.getCliente(),
                    null,
                    "Su reserva ha sido cancelada",
                    Notificacion.TipoNotificacion.ADVERTENCIA
            );

            // Guardar notificación
            notificationService.saveNotification(notificacion);

            // Enviar email
            emailService.sendNotificationEmail(notificacion);
        }
    }

    public void notifyReservationChange(Long reservaId) {
        Optional<Reserva> optionalReserva = Optional.ofNullable(reservaRepository.findByIdFull(reservaId));
        if (optionalReserva.isPresent()) {
            Reserva reserva = optionalReserva.get();

            Notificacion notificacion = new Notificacion(
                    reserva.getCliente(),
                    null,
                    "Su reserva ha sido actualizada",
                    Notificacion.TipoNotificacion.INFORMACION
            );

            notificationService.saveNotification(notificacion);
            emailService.sendNotificationEmail(notificacion);
        }
    }

    /**
     * Obtiene una reserva por su ID.
     * <p>
     * Busca una reserva en la base de datos utilizando su identificador único.
     * Si no se encuentra ninguna reserva con el ID proporcionado, se devuelve
     * {@code null}.
     * </p>
     *
     * @param reservaId El ID de la reserva a buscar.
     * @return La reserva encontrada, o {@code null} si no existe ninguna
     * reserva con el ID especificado.
     * @throws IllegalArgumentException Si el ID proporcionado es nulo.
     */
    @Override
    public Reserva getReservationById(Long reservaId) {
        // Validar que el ID no sea nulo
        if (reservaId == null) {
            throw new IllegalArgumentException("El ID de la reserva no puede ser nulo.");
        }

        // Buscar la reserva completa por su ID usando el repositorio
        return reservaRepository.findByIdFull(reservaId);
    }
}