package com.sompoble.cat.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO (Data Transfer Object) para la entidad Empresario. Se utiliza para
 * transferir datos de Empresario sin exponer la entidad completa.
 */
public class EmpresarioDTO implements Serializable {

    private Long idPersona;
    private String dni;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String pass;
    private List<Long> notificacionesIds;
    private List<EmpresaDTO> empresas;

    /**
     * Constructor con parámetros.
     *
     * @param idPersona ID del empresario
     * @param dni Documento Nacional de Identidad
     * @param nombre Nombre del empresario
     * @param apellidos Apellidos del empresario
     * @param email Correo electrónico del empresario
     * @param telefono Número de teléfono del empresario
     * @param pass Contraseña del empresario
     * @param notificacionesIds Lista de IDs de notificaciones asociadas
     * @param empresas Lista de empresas
     */
    public EmpresarioDTO(Long idPersona, String dni, String nombre, String apellidos, String email, String telefono, String pass, List<Long> notificacionesIds, List<EmpresaDTO> empresas) {
        this.idPersona = idPersona;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.pass = pass;
        this.notificacionesIds = notificacionesIds;
        this.empresas = empresas != null ? empresas : new ArrayList<>();
    }

    public EmpresarioDTO() {

    }

    /**
     * Obtiene el ID del empresario.
     *
     * @return ID del empresario
     */
    public Long getIdPersona() {
        return idPersona;
    }

    /**
     * Establece el ID del empresario.
     *
     * @param idPersona ID del empresario
     */
    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Obtiene el DNI del empresario.
     *
     * @return DNI del empresario
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del empresario.
     *
     * @param dni DNI del empresario
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del empresario.
     *
     * @return Nombre del empresario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del empresario.
     *
     * @param nombre Nombre del empresario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del empresario.
     *
     * @return Apellidos del empresario
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del empresario.
     *
     * @param apellidos Apellidos del empresario
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el correo electrónico del empresario.
     *
     * @return Correo electrónico del empresario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del empresario.
     *
     * @param email Correo electrónico del empresario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el número de teléfono del empresario.
     *
     * @return Número de teléfono del empresario
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del empresario.
     *
     * @param telefono Número de teléfono del empresario
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la contraseña del empresario.
     *
     * @return Contraseña del empresario
     */
    public String getPass() {
        return pass;
    }

    /**
     * Establece la contraseña del empresario.
     *
     * @param pass Contraseña del empresario
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Obtiene la lista de IDs de notificaciones asociadas al empresario.
     *
     * @return Lista de IDs de notificaciones
     */
    public List<Long> getNotificacionesIds() {
        return notificacionesIds;
    }

    /**
     * Establece la lista de IDs de notificaciones asociadas al empresario.
     *
     * @param notificacionesIds Lista de IDs de notificaciones
     */
    public void setNotificacionesIds(List<Long> notificacionesIds) {
        this.notificacionesIds = notificacionesIds;
    }

    public List<EmpresaDTO> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<EmpresaDTO> empresas) {
        this.empresas = empresas;
    }
}
