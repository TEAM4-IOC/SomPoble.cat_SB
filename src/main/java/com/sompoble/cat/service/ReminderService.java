package com.sompoble.cat.service;

/**
 * Interfaz que define los servicios para la gestión de recordatorios
 * automáticos.
 * <p>
 * Esta interfaz proporciona métodos para procesar y enviar recordatorios
 * programados relacionados con las reservas y otros eventos del sistema,
 * ayudando a mejorar la comunicación con los clientes y reducir las
 * inasistencias.
 * </p>
 */
public interface ReminderService {

    /**
     * Envía recordatorios automáticos para reservas próximas. Este método se
     * ejecuta de forma programada (ej.: diariamente a las 8:00 AM).
     * <p>
     * Identifica las reservas que cumplen con los criterios para envío de
     * recordatorios y genera las notificaciones correspondientes.
     * </p>
     */
    void sendDailyReminders();

    /**
     * Procesa todos los recordatorios pendientes en el sistema.
     * <p>
     * Este método realiza la lógica de negocio para identificar qué
     * recordatorios deben enviarse, a quién deben enviarse y con qué contenido,
     * basándose en las reglas configuradas en el sistema.
     * </p>
     */
    void processReminders();
}
