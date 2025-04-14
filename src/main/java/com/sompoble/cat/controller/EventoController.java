package com.sompoble.cat.controller;

import com.sompoble.cat.service.EventoService;
import com.sompoble.cat.domain.Evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Controlador REST para gestionar eventos.
 * Exponen endpoints para CRUD y consultas personalizadas.
 */
@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

  

    /**
     * Crea un nuevo evento.
     * 
     * @param evento Objeto {@link Evento} a crear.
     * @return Evento guardado con código 201 (CREATED).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Evento crearEvento(@RequestBody Evento evento) {
        requireNonNull(evento, "El evento no puede ser nulo");
        return eventoService.guardarEvento(evento);
    }

    /**
     * Endpoint para eliminar un evento por su ID.
     *
     * <p>Este método verifica si el evento existe antes de intentar eliminarlo.
     * Si el evento no existe, devuelve un estado HTTP 404 (Not Found). Si el evento
     * existe pero la eliminación falla, devuelve un estado HTTP 500 (Internal Server Error).</p>
     *
     * @param id ID del evento a eliminar.
     * @return Respuesta HTTP:
     *         - 204 No Content si el evento se elimina correctamente.
     *         - 404 Not Found si el evento no existe.
     *         - 500 Internal Server Error si ocurre un error interno.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        // Validar que el ID no sea nulo
        requireNonNull(id, "El ID no puede ser nulo");

        // Validar si el evento existe
        Evento evento = eventoService.obtenerEventoPorId(id);
        if (evento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(null); // Devolver 404 si el evento no existe
        }

        // Intentar eliminar el evento
        boolean eliminado = eventoService.eliminarEvento(id);
        if (eliminado) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                 .body(null); // Devolver 204 si la eliminación fue exitosa
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null); // Devolver 500 si ocurre un error interno
        }
    }

    /**
     * Obtiene un evento por su ID.
     * 
     * @param id ID del evento.
     * @return Evento encontrado o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evento> obtenerEvento(@PathVariable Long id) {
        Evento evento = eventoService.obtenerEventoPorId(id);
        return ResponseEntity
            .status(evento != null ? HttpStatus.OK : HttpStatus.NOT_FOUND)
            .body(evento);
    }

    /**
     * Lista todos los eventos almacenados.
     * 
     * @return Lista de eventos con código 200.
     */
    @GetMapping
    public ResponseEntity<List<Evento>> listarEventos() {
        List<Evento> eventos = eventoService.listarTodosLosEventos();
        return ResponseEntity.ok(eventos);
    }

    /**
     * Busca eventos dentro de un rango de fechas.
     * 
     * @param start Fecha de inicio del rango.
     * @param end   Fecha de fin del rango.
     * @return Lista de eventos en el rango con código 200.
     */
    @GetMapping("/fecha")
    public ResponseEntity<List<Evento>> buscarPorFecha(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        
        requireNonNull(start, "La fecha de inicio no puede ser nula");
        requireNonNull(end, "La fecha de fin no puede ser nula");
        
        return ResponseEntity.ok(
            eventoService.buscarEventosPorFecha(start, end)
        );
    }

    /**
     * Busca eventos por ubicación.
     * 
     * @param ubicacion Ubicación del evento.
     * @return Lista de eventos en la ubicación con código 200.
     */
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<Evento>> buscarPorUbicacion(
            @PathVariable String ubicacion) {
        
        requireNonNull(ubicacion, "La ubicación no puede ser nula");
        
        return ResponseEntity.ok(
            eventoService.buscarEventosPorUbicacion(ubicacion)
        );
    }
    
    /**
     * Actualiza un evento existente por su ID.
     *
     * @param id     ID del evento a actualizar.
     * @param evento Datos actualizados del evento.
     * @return Evento actualizado con código 200 o 404 si no se encuentra.
     */
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Evento> actualizarEvento(@PathVariable Long id, @RequestBody Evento evento) {
        requireNonNull(id, "El ID no puede ser nulo");
        requireNonNull(evento, "El evento no puede ser nulo");

        Evento actualizado = eventoService.actualizarEvento(id, evento);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }
    
}