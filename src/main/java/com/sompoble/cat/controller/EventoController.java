package com.sompoble.cat.controller;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sompoble.cat.domain.Evento;
import com.sompoble.cat.service.EventoService;

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
     * Este método maneja las solicitudes HTTP DELETE al endpoint `/api/eventos/{id}`.
     * Verifica si el evento con el ID proporcionado existe en el sistema. Si el evento no existe,
     * devuelve un código de estado HTTP 404 (Not Found). Si el evento existe, intenta eliminarlo
     * y devuelve un código de estado HTTP 204 (No Content) para indicar que la operación se realizó
     * exitosamente.
     *
     * Requisitos:
     *
     *     El ID proporcionado no puede ser nulo. Si es nulo, se lanzará una excepción.
     *     El evento debe existir en el sistema para que la eliminación sea exitosa.
     *
     *
     * Código de Estado HTTP:
     * 
     *    204 No Content: El evento se eliminó correctamente.
     *    404 Not Found: El evento con el ID proporcionado no existe.
     * 
     *
     * @param id ID del evento a eliminar.
     * @return ResponseEntity con el código de estado HTTP apropiado.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        requireNonNull(id, "El ID no puede ser nulo");
        Evento evento = eventoService.obtenerEventoPorId(id);
        if (evento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }

        boolean eliminado = eventoService.eliminarEvento(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }

        return ResponseEntity.noContent().build(); 
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