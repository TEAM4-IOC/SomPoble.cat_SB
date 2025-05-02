package com.sompoble.cat.exception;

/**
 * Excepción personalizada que representa situaciones donde un usuario no está
 * autorizado para acceder a un recurso o realizar una operación específica.
 * <p>
 * Esta excepción se lanza cuando un usuario autenticado intenta realizar una
 * acción para la cual no tiene los permisos o privilegios necesarios dentro del
 * sistema.
 * </p>
 * <p>
 * Al extender de RuntimeException, esta excepción no requiere ser capturada o
 * declarada explícitamente en la firma del método.
 * </p>
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Construye una nueva excepción de no autorizado con el mensaje detallado
     * especificado.
     *
     * @param message el mensaje detallado que explica la razón por la cual el
     * usuario no está autorizado. Este mensaje se guarda para recuperación
     * posterior por el método {@link Throwable#getMessage()}.
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
