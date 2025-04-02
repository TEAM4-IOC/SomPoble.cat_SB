package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Evento;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository {

    /**
     * Guarda un nuevo evento o actualiza uno existente.
     * 
     * @param evento Objeto {@link Evento} a persistir.
     * @return El evento guardado con su ID asignado.
     */
    Evento save(Evento evento);

    /**
     * Elimina un evento por su ID.
     * 
     * @param id ID del evento a eliminar.
     */
    void delete(Long id);

    /**
     * Obtiene un evento por su ID.
     * 
     * @param id ID del evento.
     * @return El evento encontrado o {@code null} si no existe.
     */
    Evento findById(Long id);

    /**
     * Lista todos los eventos almacenados.
     * 
     * @return Lista de eventos.
     */
    List<Evento> findAll();

    /**
     * Busca eventos dentro de un rango de fechas.
     * 
     * @param start Fecha de inicio del rango.
     * @param end   Fecha de fin del rango.
     * @return Lista de eventos dentro del rango.
     */
    List<Evento> findByFechaEventoBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Busca eventos por ubicación.
     * 
     * @param ubicacion Ubicación del evento.
     * @return Lista de eventos en la ubicación especificada.
     */
    List<Evento> findByUbicacion(String ubicacion);

	/**
	 * Busca eventos por ubicación y rango de fechas.
	 * 
	 * @param ubicacion Ubicación del evento.
	 * @param start     Fecha de inicio del rango.
	 * @param end       Fecha de fin del rango.
	 * @return Lista de eventos que cumplen con las condiciones.
	 */
	List<Evento> findByUbicacionAndFechaEventoBetween(String ubicacion, LocalDateTime start, LocalDateTime end);

	/**
	 * Busca eventos por una palabra clave en el nombre del evento.
	 * 
	 * @param keyword Palabra clave a buscar en el nombre del evento.
	 * @return Lista de eventos que contienen la palabra clave.
	 */
	List<Evento> findByNombreContainingIgnoreCase(String keyword);

	/**
	 * Encuentra el evento más cercano a la fecha actual.
	 * 
	 * @return El evento más cercano a la fecha actual.
	 */
	Optional<Evento> findClosestEvent();

	/**
	 * Cuenta la cantidad total de eventos en la base de datos.
	 * 
	 * @return El número total de eventos.
	 */
	Long count();
}