package com.sompoble.cat.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
/**
 * Entidad que representa a un cliente dentro del sistema.
 * <p>
 * Hereda de la clase {@link Persona} e incluye listas de reservas y notificaciones
 * asociadas al cliente.
 * </p>
 */
@Entity
@Table(name = "CLIENTE")
public class Cliente extends Persona {
	/**
     * Lista de reservas realizadas por el cliente.
     * <p>
     * Relación uno a muchos con la entidad {@link Reserva}.
     * </p>
     */
    @OneToMany(mappedBy="cliente")
    private List<Reserva> reservas;
    /**
     * Lista de notificaciones enviadas al cliente.
     * <p>
     * Relación uno a muchos con la entidad {@link Notificacion}.
     * </p>
     */
    @OneToMany(mappedBy = "cliente")
    @JsonManagedReference("notificacion-cliente")
    private List<Notificacion> notificaciones;
    /**
     * Constructor vacío requerido por JPA.
     */
    public Cliente() {
    }
    /**
     * Constructor que inicializa los campos heredados de {@link Persona}.
     *
     * @param dni       Documento Nacional de Identidad del cliente.
     * @param nombre    Nombre del cliente.
     * @param apellidos Apellidos del cliente.
     * @param email     Correo electrónico del cliente.
     * @param telefono  Número de teléfono del cliente.
     * @param pass      Contraseña del cliente.
     */
    public Cliente(String dni, String nombre, String apellidos, String email, String telefono, String pass) {
        super(dni, nombre, apellidos, email, telefono, pass);
    }
    /**
     * Obtiene la lista de reservas asociadas al cliente.
     *
     * @return lista de {@link Reserva}.
     */
    public List<Reserva> getReservas() {
        return reservas;
    }
    /**
     * Establece la lista de reservas del cliente.
     *
     * @param reservas lista de {@link Reserva}.
     */
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    /**
     * Obtiene la lista de notificaciones asociadas al cliente.
     *
     * @return lista de {@link Notificacion}.
     */
    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }
    /**
     * Establece la lista de notificaciones del cliente.
     *
     * @param notificaciones lista de {@link Notificacion}.
     */
    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    } 
}