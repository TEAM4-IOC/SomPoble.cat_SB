package com.sompoble.cat.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.sompoble.cat.domain.Evento;

/**
 * Servicio para gestionar operaciones CRUD y consultas personalizadas sobre
 * eventos.
 */
@Service
public interface EventoService {

    /**
     * Guarda un nuevo evento o actualiza uno existente.
     *
     * @param evento Objeto {@link Evento} a persistir.
     * @return El evento guardado con su ID asignado.
     */
    Evento guardarEvento(Evento evento);

    /**
     * Obtiene un evento por su ID.
     *
     * @param id ID del evento.
     * @return El evento encontrado o {@code null} si no existe.
     */
    Evento obtenerEventoPorId(Long id);

    /**
     * Lista todos los eventos almacenados.
     *
     * @return Lista de eventos.
     */
    List<Evento> listarTodosLosEventos();

    /**
     * Busca eventos dentro de un rango de fechas.
     *
     * @param start Fecha de inicio del rango.
     * @param end Fecha de fin del rango.
     * @return Lista de eventos dentro del rango.
     */
    List<Evento> buscarEventosPorFecha(LocalDateTime start, LocalDateTime end);

    /**
     * Busca eventos por ubicación.
     *
     * @param ubicacion Ubicación del evento.
     * @return Lista de eventos en la ubicación especificada.
     */
    List<Evento> buscarEventosPorUbicacion(String ubicacion);

    /**
     * Actualiza un evento existente con la información proporcionada.
     *
     * @param id ID del evento a actualizar.
     * @param evento Objeto {@link Evento} con los datos actualizados.
     * @return El evento actualizado, o {@code null} si no se encuentra el
     * evento con el ID especificado.
     */
    Evento actualizarEvento(Long id, Evento evento);

    /**
     * Elimina un evento por su ID.
     *
     * @param id ID del evento a eliminar.
     * @return true si el evento se eliminó correctamente, false si no existe.
     */
    boolean eliminarEvento(Long id);
}
