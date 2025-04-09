package com.sompoble.cat.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO (Data Transfer Object) para la entidad Cliente. Se utiliza para
 * transferir datos de Cliente sin exponer la entidad completa.
 */
public class ClienteDTO implements Serializable {

    private Long idPersona;
    private String dni;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String pass;
    private List<Long> reservasIds;
    private List<Long> notificacionesIds;

    /**
     * Constructor con parámetros.
     *
     * @param idPersona ID del cliente
     * @param dni Documento Nacional de Identidad
     * @param nombre Nombre del cliente
     * @param apellidos Apellidos del cliente
     * @param email Correo electrónico del cliente
     * @param telefono Número de teléfono del cliente
     * @param pass password del cliente
     * @param reservasIds Lista de IDs de reservas asociadas
     * @param notificacionesIds Lista de IDs de notificaciones asociadas
     */
    public ClienteDTO(Long idPersona, String dni, String nombre, String apellidos, String email, String telefono, String pass, List<Long> reservasIds, List<Long> notificacionesIds) {
        this.idPersona = idPersona;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.pass = pass;
        this.reservasIds = reservasIds;
        this.notificacionesIds = notificacionesIds;
    }



    public ClienteDTO() {

	}



	/**
     * Obtiene el ID del cliente.
     *
     * @return ID del cliente
     */
    public Long getIdPersona() {
        return idPersona;
    }

    /**
     * Establece el ID del cliente.
     *
     * @param idPersona ID del cliente
     */
    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Obtiene el DNI del cliente.
     *
     * @return DNI del cliente
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del cliente.
     *
     * @param dni DNI del cliente
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del cliente.
     *
     * @return Nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     *
     * @param nombre Nombre del cliente
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del cliente.
     *
     * @return Apellidos del cliente
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del cliente.
     *
     * @param apellidos Apellidos del cliente
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     *
     * @return Correo electrónico del cliente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del cliente.
     *
     * @param email Correo electrónico del cliente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     *
     * @return Número de teléfono del cliente
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     *
     * @param telefono Número de teléfono del cliente
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la contraseña del cliente.
     *
     * @return Contraseña del cliente
     */
    public String getPass() {
        return pass;
    }

    /**
     * Establece la contraseña del cliente.
     *
     * @param pass Contraseña del cliente
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Obtiene la lista de IDs de reservas asociadas al cliente.
     *
     * @return Lista de IDs de reservas
     */
    public List<Long> getReservasIds() {
        return reservasIds;
    }

    /**
     * Establece la lista de IDs de reservas asociadas al cliente.
     *
     * @param reservasIds Lista de IDs de reservas
     */
    public void setReservasIds(List<Long> reservasIds) {
        this.reservasIds = reservasIds;
    }

    /**
     * Obtiene la lista de IDs de notificaciones asociadas al cliente.
     *
     * @return Lista de IDs de notificaciones
     */
    public List<Long> getNotificacionesIds() {
        return notificacionesIds;
    }

    /**
     * Establece la lista de IDs de notificaciones asociadas al cliente.
     *
     * @param notificacionesIds Lista de IDs de notificaciones
     */
    public void setNotificacionesIds(List<Long> notificacionesIds) {
        this.notificacionesIds = notificacionesIds;
    }
}