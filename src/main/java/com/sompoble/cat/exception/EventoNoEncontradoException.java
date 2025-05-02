package com.sompoble.cat.exception;

/**
 * Excepción lanzada cuando un evento no se encuentra en la base de datos. Esta
 * excepción se utiliza para indicar que se ha intentado acceder o manipular un
 * evento que no existe en el sistema.
 * <p>
 * Al extender de RuntimeException, esta excepción no requiere ser capturada o
 * declarada explícitamente en la firma del método.
 * </p>
 */
public class EventoNoEncontradoException extends RuntimeException {

    /**
     * Construye una nueva excepción de evento no encontrado con el ID
     * especificado.
     *
     * @param id el identificador único del evento que no pudo ser encontrado.
     * Este ID se incluye en el mensaje de error para facilitar la depuración.
     */
    public EventoNoEncontradoException(Long id) {
        super("Evento con ID " + id + " no encontrado.");
    }
}
