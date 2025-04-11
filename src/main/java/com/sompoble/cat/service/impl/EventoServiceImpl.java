package com.sompoble.cat.service.impl;

import static java.util.Objects.requireNonNull;
import com.sompoble.cat.exception.EventoNoEncontradoException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sompoble.cat.domain.Evento;
import com.sompoble.cat.repository.EventoRepository;
import com.sompoble.cat.service.EventoService;

/**
 * Implementación del servicio para gestionar eventos.
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;


    /**
     * Guarda un nuevo evento o actualiza uno existente.
     *
     * @param evento Objeto {@link Evento} a persistir.
     * @return El evento guardado con su ID asignado.
     */
    @Override
    @Transactional
    public Evento guardarEvento(Evento evento) {
        requireNonNull(evento, "El evento no puede ser nulo");
        return eventoRepository.save(evento);
    }

    /**
     * Elimina un evento por su ID.
     *
     * @param id ID del evento a eliminar.
     */
    @Override
    @Transactional
    public boolean eliminarEvento(Long id) {
        Evento evento = obtenerEventoPorId(id);
        if (evento == null) {
            return false; // Evento no existe
        }

        // Intentar eliminar el evento
        try {
            eventoRepository.delete(id);
            return true; // Evento eliminado correctamente
        } catch (Exception e) {
            return false; // Error al eliminar el evento
        }
    }

    /**
     * Obtiene un evento por su ID.
     *
     * @param id ID del evento.
     * @return El evento encontrado o {@code null} si no existe.
     */
    @Override
    public Evento obtenerEventoPorId(Long id) {
        requireNonNull(id, "El ID no puede ser nulo");
        return eventoRepository.findById(id);
    }

    /**
     * Lista todos los eventos almacenados.
     *
     * @return Lista de eventos.
     */
    @Override
    public List<Evento> listarTodosLosEventos() {
        return eventoRepository.findAll();
    }

    /**
     * Busca eventos dentro de un rango de fechas.
     *
     * @param start Fecha de inicio del rango.
     * @param end   Fecha de fin del rango.
     * @return Lista de eventos dentro del rango.
     */
    @Override
    public List<Evento> buscarEventosPorFecha(LocalDateTime start, LocalDateTime end) {
        requireNonNull(start, "La fecha de inicio no puede ser nula");
        requireNonNull(end, "La fecha de fin no puede ser nula");
        return eventoRepository.findByFechaEventoBetween(start, end);
    }

    /**
     * Busca eventos por ubicación.
     *
     * @param ubicacion Ubicación del evento.
     * @return Lista de eventos en la ubicación especificada.
     */
    @Override
    public List<Evento> buscarEventosPorUbicacion(String ubicacion) {
        requireNonNull(ubicacion, "La ubicación no puede ser nula");
        return eventoRepository.findByUbicacion(ubicacion);
    }

   
    @Override
    public Evento actualizarEvento(Long id, Evento evento) {
        Evento existente = eventoRepository.findById(id);

        if (existente == null) {
            throw new EventoNoEncontradoException(id);
        }

        existente.setNombre(evento.getNombre());
        existente.setDescripcion(evento.getDescripcion());
        existente.setFechaEvento(evento.getFechaEvento());
        existente.setUbicacion(evento.getUbicacion());

        return eventoRepository.save(existente);
    
    
    }   
}