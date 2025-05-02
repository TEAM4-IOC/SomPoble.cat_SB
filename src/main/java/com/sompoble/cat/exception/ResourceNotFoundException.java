package com.sompoble.cat.exception;

/**
 * Excepción personalizada que representa situaciones donde un recurso
 * solicitado no puede ser encontrado en el sistema.
 * <p>
 * Esta excepción se lanza cuando se intenta acceder a recursos como entidades,
 * archivos o datos que no existen o no están disponibles en el momento de la
 * solicitud.
 * </p>
 * <p>
 * Al extender de RuntimeException, esta excepción no requiere ser capturada o
 * declarada explícitamente en la firma del método.
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construye una nueva excepción de recurso no encontrado con el mensaje
     * detallado especificado.
     *
     * @param message el mensaje detallado que explica qué recurso no se
     * encontró. Este mensaje se guarda para recuperación posterior por el
     * método {@link Throwable#getMessage()}.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
