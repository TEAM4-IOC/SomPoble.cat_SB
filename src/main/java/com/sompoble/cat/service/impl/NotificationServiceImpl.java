package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.repository.NotificacionRepository;
import com.sompoble.cat.service.NotificationService;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación del servicio {@link NotificationService} que proporciona operaciones
 * para gestionar las notificaciones.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificacionRepository notificationRepository;
    private Boolean configEnabled;
    private String  configFrequency;
    private String  configSendTime;
    /**
     * Constructor con inyección de dependencias para el repositorio de notificaciones.
     *
     * @param notificationRepository el repositorio para gestionar notificaciones
     */
    public NotificationServiceImpl(NotificacionRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Guarda una nueva notificación en la base de datos.
     *
     * @param notification la notificación a guardar
     */
    @Override
    @Transactional
    public void saveNotification(Notificacion notification) {
        Notificacion saved = notificationRepository.save(notification);
        System.out.println("Guardada notificación con ID: " + saved.getIdNotificacion());
    }

    /**
     * Recupera todas las notificaciones almacenadas.
     *
     * @return una lista de notificaciones
     */
    @Override
    public List<Notificacion> getAllNotifications() {
        return notificationRepository.findAll();
    }

    /**
     * Busca una notificación por su identificador único.
     *
     * @param id el ID de la notificación
     * @return la notificación correspondiente, o {@code null} si no se encuentra
     */
    @Override
    public Notificacion findNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    /**
     * Elimina una notificación según su ID.
     *
     * @param id el ID de la notificación a eliminar
     */
    @Override
    public void deleteNotificationById(Long id) {
        notificationRepository.deleteById(id);
    }
    /**
     * Establece la configuración de envío de notificaciones.
     *
     * @param enabled Indica si las notificaciones están habilitadas o no.
     * @param freq La frecuencia con la que se deben enviar las notificaciones (por ejemplo, diaria, semanal).
     * @param time La hora del día a la que se deben enviar las notificaciones.
     */
    public void setConfig(Boolean enabled, String freq, String time) {
        this.configEnabled   = enabled;
        this.configFrequency = freq;
        this.configSendTime  = time;
    }
    /**
     * Obtiene el estado de habilitación de la configuración de notificaciones.
     *
     * @return {@code true} si las notificaciones están habilitadas, {@code false} en caso contrario.
     */
    public Boolean getConfigEnabled()   { return configEnabled; }
    
    
    /**
     * Obtiene la frecuencia configurada para el envío de notificaciones.
     *
     * @return La frecuencia como cadena de texto (por ejemplo, "diaria", "semanal").
     */
    public String  getConfigFrequency() { return configFrequency; }
    
    
    /**
     * Obtiene la hora configurada para el envío de notificaciones.
     *
     * @return La hora de envío configurada, como una cadena de texto en formato HH:mm.
     */
    public String  getConfigSendTime()  { return configSendTime;  }
    
    /**
     * Recupera todas las notificaciones asociadas a un identificador específico.
     *
     * <p>El identificador puede ser:
     * <ul>
     *   <li>El DNI de un cliente.</li>
     *   <li>El número fiscal de una empresa o autónomo.</li>
     * </ul>
     * Se devuelven todas las notificaciones relacionadas.</p>
     *
     * @param identificador El DNI del cliente o el número fiscal de la empresa/autónomo.
     * @return Una lista de notificaciones encontradas; puede ser una lista vacía si no hay resultados.
     */
    @Override
    public List<Notificacion> findNotificationsByIdentificador(String identificador) {
        return notificationRepository.findByIdentificador(identificador);
    }

	@Override
	public List<Notificacion> findByClienteDni(String dni) {
		
		return notificationRepository.findByClienteDni(dni);
	}
}
