package com.sompoble.cat.dto;

/**
 * Objeto de Transferencia de Datos (DTO) que representa la información básica
 * de un servicio para ser mostrado en la página de inicio (landing).
 * <p>
 * Este DTO contiene los datos esenciales de un servicio, optimizado para su uso
 * en la interfaz de usuario dentro del contexto de una empresa.
 * </p>
 */
public class LandingServicioDTO {

    /**
     * Nombre descriptivo del servicio ofrecido.
     */
    private String nombre;

    /**
     * Constructor que inicializa un DTO de servicio con su nombre.
     *
     * @param nombre Nombre descriptivo del servicio
     */
    public LandingServicioDTO(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Constructor vacío requerido para la serialización/deserialización.
     */
    public LandingServicioDTO() {
    }

    /**
     * Obtiene el nombre descriptivo del servicio.
     *
     * @return El nombre del servicio
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre descriptivo del servicio.
     *
     * @param nombre El nuevo nombre del servicio
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
