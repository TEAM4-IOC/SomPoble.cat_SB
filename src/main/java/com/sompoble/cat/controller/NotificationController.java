package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador que gestiona las notificaciones. Este controlador incluye endpoints
 * tanto para la administración interna como para el acceso desde el front-end.
 * Proporciona funciones como listar, guardar, obtener, y borrar notificaciones.
 * 
 * API para administrar notificaciones de forma manual: consultar, borrar, listar, etc.
 * 
 * Para ser utilizado principalmente en un panel de administración interno.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Constructor de la clase NotificationController.
     * 
     * @param notificationService Servicio que maneja las operaciones sobre las notificaciones.
     */
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //------------------------------------------------------------------------//
    // PARA LISTAR DESDE EL FRONT:
    
    /**
     * Obtiene las notificaciones filtradas por identificador (DNI para clientes
     * o Número Fiscal para empresas/autónomos).
     * 
     * @param identificador El DNI si es cliente o el Número Fiscal si es empresa/autónomo.
     * @return Lista de notificaciones asociadas al identificador proporcionado.
     * @throws RuntimeException Si no se encuentran notificaciones asociadas al identificador.
     */
    @GetMapping("/obtener")
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorIdentificador(@RequestParam String identificador) {
        List<Notificacion> notificaciones = notificationService.findNotificationsByIdentificador(identificador);
        
        if (notificaciones.isEmpty()) {
            throw new RuntimeException("No hay notificaciones para este identificador");
        }

        return ResponseEntity.ok(notificaciones);
    }

    //------------------------------------------------------------------------//
    // PARA ADMINISTRACION INTERNA:
    
    /**
     * Guarda una nueva notificación en el sistema.
     * 
     * @param notification La notificación a guardar.
     * @return Mensaje confirmando que la notificación fue guardada correctamente.
     */
    @PostMapping("/save")
    public String saveNotification(@RequestBody Notificacion notification) {
        notificationService.saveNotification(notification);
        return "Notificación guardada correctamente.";
    }

    /**
     * Obtiene todas las notificaciones almacenadas en el sistema.
     * 
     * @return Lista de todas las notificaciones.
     */
    @GetMapping("/all")
    public List<Notificacion> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    /**
     * Obtiene una notificación por su identificador único.
     * 
     * @param id El identificador único de la notificación.
     * @return La notificación asociada al identificador proporcionado.
     */
    @GetMapping("/{id}")
    public Notificacion getNotificationById(@PathVariable Long id) {
        return notificationService.findNotificationById(id);
    }

    /**
     * Elimina una notificación por su identificador único.
     * 
     * @param id El identificador único de la notificación a eliminar.
     * @return Mensaje confirmando que la notificación fue eliminada correctamente.
     */
    @DeleteMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotificationById(id);
        return "Notificación eliminada correctamente.";
    }
}