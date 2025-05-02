package com.sompoble.cat.service;

import com.sompoble.cat.domain.Reserva;

/**
 * Interfaz para el servicio de gestión de reservas.
 * <p>
 * Define los métodos necesarios para crear, actualizar y gestionar reservas.
 * </p>
 */
public interface ReservationService {

    /**
     * Crea o actualiza una reserva en el sistema.
     * <p>
     * Este método guarda la reserva en la base de datos, genera una
     * notificación relacionada con la reserva y envía un email al cliente
     * afectado.
     * </p>
     *
     * @param reserva La reserva a crear o actualizar.
     */
    void createOrUpdateReservation(Reserva reserva);

    /**
     * Cancela una reserva existente.
     * <p>
     * Este método elimina la reserva de la base de datos y genera una
     * notificación informando al cliente sobre la cancelación.
     * </p>
     *
     * @param reservaId El ID de la reserva a cancelar.
     */
    void cancelReservation(Long reservaId);

    /**
     * Obtiene una reserva por su ID.
     *
     * @param reservaId El ID de la reserva a buscar.
     * @return La reserva encontrada, o {@code null} si no existe.
     */
    /**
     * Notifica cambios en una reserva específica.
     *
     * @param reservaId El ID de la reserva que ha cambiado.
     */
    void notifyReservationChange(Long reservaId);

    /**
     * Obtiene una reserva por su ID.
     * <p>
     * Busca una reserva en la base de datos utilizando su identificador único.
     * Si no se encuentra ninguna reserva con el ID proporcionado, se devuelve
     * {@code null}.
     * </p>
     *
     * @param reservaId El ID de la reserva a buscar. Debe ser un valor no nulo.
     * @return La reserva encontrada, o {@code null} si no existe ninguna
     * reserva con el ID especificado.
     */
    Reserva getReservationById(Long reservaId);

}
