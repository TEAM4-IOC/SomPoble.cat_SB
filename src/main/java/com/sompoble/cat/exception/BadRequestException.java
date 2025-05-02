package com.sompoble.cat.exception;

/**
 * Excepción personalizada que representa condiciones de solicitud incorrecta.
 * Esta excepción se lanza cuando una solicitud contiene parámetros inválidos,
 * datos malformados o incumple los requisitos del negocio.
 * <p>
 * Al extender de RuntimeException, esta excepción no requiere ser capturada o
 * declarada explícitamente en la firma del método.
 * </p>
 */
public class BadRequestException extends RuntimeException {

    /**
     * Construye una nueva excepción de solicitud incorrecta con el mensaje
     * detallado especificado.
     *
     * @param message el mensaje detallado que explica la razón de la excepción.
     * Este mensaje se guarda para recuperación posterior por el método
     * {@link Throwable#getMessage()}.
     */
    public BadRequestException(String message) {
        super(message);
    }
}
