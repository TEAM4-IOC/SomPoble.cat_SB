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
 * <p>
 * Esta clase proporciona la implementación concreta de los métodos
 * definidos en la interfaz EventoService, gestionando las operaciones
 * relacionadas con eventos a través del repositorio correspondiente.
 * </p>
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {
    
    /**
     * Repositorio para acceder a los datos de eventos.
     */
    @Autowired
    private EventoRepository eventoRepository;
    
    /**
     * Guarda un nuevo evento o actualiza uno existente.
     *
     * @param evento Objeto {@link Evento} a persistir.
     * @return El evento guardado con su ID asignado.
     * @throws NullPointerException si el evento es nulo
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
     * @return true si el evento se eliminó correctamente, false si no existe o hubo un error
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
     * @throws NullPointerException si el ID es nulo
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
     * @throws NullPointerException si alguna de las fechas es nula
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
     * @throws NullPointerException si la ubicación es nula
     */
    @Override
    public List<Evento> buscarEventosPorUbicacion(String ubicacion) {
        requireNonNull(ubicacion, "La ubicación no puede ser nula");
        return eventoRepository.findByUbicacion(ubicacion);
    }
   
    /**
     * Actualiza un evento existente con la información proporcionada.
     *
     * @param id ID del evento a actualizar.
     * @param evento Objeto {@link Evento} con los datos actualizados.
     * @return El evento actualizado.
     * @throws EventoNoEncontradoException si no se encuentra el evento con el ID especificado.
     */
    @Override
    @Transactional
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