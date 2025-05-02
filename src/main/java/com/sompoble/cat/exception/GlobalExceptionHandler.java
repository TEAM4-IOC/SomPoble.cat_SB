package com.sompoble.cat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * Manejador global de excepciones para la aplicación.
 * <p>
 * Esta clase centraliza el manejo de excepciones en toda la aplicación,
 * proporcionando respuestas coherentes y bien formateadas cuando ocurren
 * errores. Utiliza la anotación {@code @ControllerAdvice} de Spring para
 * interceptar excepciones lanzadas por los controladores.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo ResourceNotFoundException.
     *
     * @param ex la excepción ResourceNotFoundException capturada
     * @return un ResponseEntity con estado HTTP 404 (Not Found) y un cuerpo que
     * contiene detalles del error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja las excepciones de tipo BadRequestException.
     *
     * @param ex la excepción BadRequestException capturada
     * @return un ResponseEntity con estado HTTP 400 (Bad Request) y un cuerpo
     * que contiene detalles del error
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja cualquier excepción no capturada específicamente por otros
     * manejadores.
     *
     * @param ex la excepción general capturada
     * @return un ResponseEntity con estado HTTP 500 (Internal Server Error) y
     * un cuerpo que contiene detalles del error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja las excepciones relacionadas con problemas de deserialización de
     * mensajes HTTP. Se produce cuando el cuerpo de la solicitud no puede ser
     * convertido al objeto esperado.
     *
     * @param ex la excepción HttpMessageNotReadableException capturada
     * @return un ResponseEntity con estado HTTP 400 (Bad Request) y un cuerpo
     * que contiene detalles del error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "Error en la deserialización de los datos JSON. Verifique el formato");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja las excepciones de tipo UnauthorizedException.
     *
     * @param ex la excepción UnauthorizedException capturada
     * @return un ResponseEntity con estado HTTP 401 (Unauthorized) y un cuerpo
     * que contiene detalles del error
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("error", "Unauthorized");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja las excepciones de tipo EventoNoEncontradoException.
     *
     * @param ex la excepción EventoNoEncontradoException capturada
     * @return un ResponseEntity con estado HTTP 404 (Not Found) y un cuerpo que
     * contiene detalles del error
     */
    @ExceptionHandler(EventoNoEncontradoException.class)
    public ResponseEntity<?> manejarEventoNoEncontrado(EventoNoEncontradoException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
