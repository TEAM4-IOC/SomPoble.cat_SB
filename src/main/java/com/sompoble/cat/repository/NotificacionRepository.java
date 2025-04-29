package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Notificacion;
import java.util.List;

/**
 * Repositorio para la gestión de entidades {@link Notificacion}.
 * Proporciona métodos para operaciones CRUD básicas.
 */
public interface NotificacionRepository {

    /**
     * Guarda una nueva notificación o actualiza una existente.
     *
     * @param notificacion la notificación a guardar
     */
	Notificacion save(Notificacion notificacion);

    /**
     * Recupera todas las notificaciones almacenadas.
     *
     * @return una lista de todas las notificaciones
     */
    List<Notificacion> findAll();

    /**
     * Busca una notificación por su identificador.
     *
     * @param id el ID de la notificación
     * @return la notificación correspondiente al ID, o {@code null} si no se encuentra
     */
    Notificacion findById(Long id);

    /**
     * Elimina una notificación por su identificador.
     *
     * @param id el ID de la notificación a eliminar
     */
    void deleteById(Long id);
    /**
     * Busca todas las notificaciones asociadas a un identificador dado.
     *
     * <p>El identificador puede ser:
     * <ul>
     *   <li>El DNI de un cliente.</li>
     *   <li>El número fiscal de una empresa o autónomo.</li>
     * </ul>
     * Se devuelven todas las notificaciones donde coincida alguno de estos campos.</p>
     *
     * @param identificador El DNI del cliente o el número fiscal de la empresa/autónomo.
     * @return Una lista de notificaciones asociadas al identificador. 
     *         La lista estará vacía si no se encuentran coincidencias.
     */
	List<Notificacion> findByIdentificador(String identificador);

	List<Notificacion> findByClienteDni(String dni);

}