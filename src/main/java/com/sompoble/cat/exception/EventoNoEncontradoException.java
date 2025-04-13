package com.sompoble.cat.exception;

/**
 * Excepción lanzada cuando un evento no se encuentra en la base de datos.
 */
public class EventoNoEncontradoException extends RuntimeException {

    public EventoNoEncontradoException(Long id) {
        super("Evento con ID " + id + " no encontrado.");
    }
}
