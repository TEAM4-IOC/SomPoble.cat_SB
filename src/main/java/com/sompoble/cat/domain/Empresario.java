package com.sompoble.cat.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
/**
 * Entidad que representa a un Empresario en el sistema.
 * Hereda de {@link Persona} y puede tener asociadas múltiples empresas y notificaciones.
 */
@Entity
@Table(name = "EMPRESARIO")
public class Empresario extends Persona {
	/**
     * Lista de empresas asociadas al empresario.
     * Relación uno a muchos con la entidad {@link Empresa}.
     */
    @OneToMany(mappedBy = "empresario")
    @JsonManagedReference
    private List<Empresa> empresas;
    /**
     * Lista de notificaciones recibidas por el empresario.
     * Relación uno a muchos con la entidad {@link Notificacion}.
     */
    @OneToMany(mappedBy = "empresario")
    @JsonManagedReference("notificacion-empresario")
    private List<Notificacion> notificaciones;
    /**
     * Constructor por defecto.
     */
    public Empresario() {
    }
    /**
     * Constructor con todos los campos heredados de {@link Persona}.
     * 
     * @param dni DNI del empresario.
     * @param nombre Nombre del empresario.
     * @param apellidos Apellidos del empresario.
     * @param email Correo electrónico del empresario.
     * @param telefono Número de teléfono del empresario.
     * @param pass Contraseña del empresario.
     */
    public Empresario(String dni, String nombre, String apellidos, String email, String telefono, String pass) {
        super(dni, nombre, apellidos, email, telefono, pass);
    }
    /**
     * Obtiene la lista de empresas asociadas al empresario.
     * 
     * @return Lista de {@link Empresa}.
     */
    public List<Empresa> getEmpresas() {
        return empresas;
    }
    /**
     * Establece la lista de empresas asociadas al empresario.
     * 
     * @param empresas Lista de empresas a asociar.
     */
    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }
    /**
     * Obtiene la lista de notificaciones del empresario.
     * 
     * @return Lista de {@link Notificacion}.
     */
    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }
    /**
     * Establece la lista de notificaciones del empresario.
     * 
     * @param notificaciones Lista de notificaciones a establecer.
     */
    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
}
