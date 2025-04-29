package com.sompoble.cat.service;

import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.dto.ClienteDTO;

import java.util.List;

/**
 * Servicio para gestionar notificaciones en el sistema.
 * Proporciona métodos para CRUD de Notificacion y
 * para configurar la lógica de notificaciones.
 */
public interface NotificationService {
    /**
     * Guarda una notificación en la base de datos.
     * @param notification la notificación a guardar
     */
    void saveNotification(Notificacion notification);

    /**
     * Obtiene todas las notificaciones almacenadas.
     * @return lista de notificaciones
     */
    List<Notificacion> getAllNotifications();

    /**
     * Busca una notificación por su ID.
     * @param id ID de la notificación
     * @return la notificación encontrada o null
     */
    Notificacion findNotificationById(Long id);

    /**
     * Elimina una notificación por su ID.
     * @param id ID de la notificación a eliminar
     */
    void deleteNotificationById(Long id);

    /**
     * Establece la configuración de notificaciones.
     * @param enabled si las notificaciones están habilitadas
     * @param frequency frecuencia de envío (e.g., "daily")
     * @param sendTime hora de envío (e.g., "08:00")
     */
    void setConfig(Boolean enabled, String frequency, String sendTime);

    /**
     * Comprueba si las notificaciones están habilitadas.
     * @return true si están habilitadas
     */
    Boolean getConfigEnabled();

    /**
     * Obtiene la frecuencia configurada.
     * @return la frecuencia de envío
     */
    String getConfigFrequency();

    /**
     * Obtiene la hora configurada para el envío.
     * @return la hora de envío
     */
    String getConfigSendTime();
    
    List<Notificacion> findNotificationsByIdentificador(String identificador);

	List<Notificacion> findByClienteDni(String dni);

	
    
}
