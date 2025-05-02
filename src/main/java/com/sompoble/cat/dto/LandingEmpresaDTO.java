package com.sompoble.cat.dto;

import java.util.List;

/**
 * Objeto de Transferencia de Datos (DTO) que representa la información de una
 * empresa para ser mostrada en la página de inicio (landing).
 * <p>
 * Este DTO contiene los datos esenciales de una empresa junto con sus servicios
 * asociados, optimizado para su uso en la interfaz de usuario.
 * </p>
 */
public class LandingEmpresaDTO {

    /**
     * Nombre comercial de la empresa.
     */
    private String nombre;

    /**
     * Dirección física de la empresa.
     */
    private String direccion;

    /**
     * Número de teléfono de contacto de la empresa.
     */
    private String telefono;

    /**
     * Dirección de correo electrónico de contacto de la empresa.
     */
    private String email;

    /**
     * URL o ruta de la imagen representativa de la empresa.
     */
    private String imagen;

    /**
     * Identificador fiscal (CIF/NIF) necesario para relacionar con servicios.
     */
    private String identificadorFiscal;

    /**
     * Lista de servicios ofrecidos por la empresa.
     */
    private List<LandingServicioDTO> servicios;

    /**
     * Constructor completo para crear una instancia con todos sus atributos.
     *
     * @param nombre Nombre comercial de la empresa
     * @param direccion Dirección física de la empresa
     * @param telefono Número de teléfono de contacto
     * @param email Dirección de correo electrónico
     * @param imagen URL o ruta de la imagen representativa
     * @param identificadorFiscal Identificador fiscal (CIF/NIF)
     * @param servicios Lista de servicios ofrecidos por la empresa
     */
    public LandingEmpresaDTO(String nombre, String direccion, String telefono,
            String email, String imagen, String identificadorFiscal,
            List<LandingServicioDTO> servicios) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.imagen = imagen;
        this.identificadorFiscal = identificadorFiscal;
        this.servicios = servicios;
    }

    /**
     * Constructor vacío requerido para la serialización/deserialización.
     */
    public LandingEmpresaDTO() {
    }

    /**
     * Obtiene el nombre comercial de la empresa.
     *
     * @return El nombre de la empresa
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre comercial de la empresa.
     *
     * @param nombre El nuevo nombre de la empresa
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la dirección física de la empresa.
     *
     * @return La dirección de la empresa
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección física de la empresa.
     *
     * @param direccion La nueva dirección de la empresa
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el número de teléfono de contacto de la empresa.
     *
     * @return El teléfono de la empresa
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono de contacto de la empresa.
     *
     * @param telefono El nuevo teléfono de la empresa
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección de correo electrónico de contacto.
     *
     * @return El email de la empresa
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece la dirección de correo electrónico de contacto.
     *
     * @param email El nuevo email de la empresa
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la URL o ruta de la imagen representativa de la empresa.
     *
     * @return La ruta de la imagen de la empresa
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece la URL o ruta de la imagen representativa de la empresa.
     *
     * @param imagen La nueva ruta de imagen de la empresa
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene el identificador fiscal de la empresa.
     *
     * @return El identificador fiscal (CIF/NIF) de la empresa
     */
    public String getIdentificadorFiscal() {
        return identificadorFiscal;
    }

    /**
     * Establece el identificador fiscal de la empresa.
     *
     * @param identificadorFiscal El nuevo identificador fiscal de la empresa
     */
    public void setIdentificadorFiscal(String identificadorFiscal) {
        this.identificadorFiscal = identificadorFiscal;
    }

    /**
     * Obtiene la lista de servicios ofrecidos por la empresa.
     *
     * @return Lista de DTOs representando los servicios de la empresa
     */
    public List<LandingServicioDTO> getServicios() {
        return servicios;
    }

    /**
     * Establece la lista de servicios ofrecidos por la empresa.
     *
     * @param servicios Nueva lista de DTOs de servicios
     */
    public void setServicios(List<LandingServicioDTO> servicios) {
        this.servicios = servicios;
    }
}
