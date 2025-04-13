package com.sompoble.cat.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa una reserva en el sistema.
 * <p>
 * Contiene la información de la empresa, cliente y servicio asociados, además
 * de los datos de fecha, hora y estado de la reserva.
 * </p>
 *
 * @author SomPoble
 */
@Entity
@Table(name = "RESERVA")
public class Reserva implements Serializable {

    /**
     * Identificador único de la reserva.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long idReserva;

    /**
     * Empresa a la que pertenece la reserva.
     */
    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_EMPRESA", nullable = false)
    @NotNull(message = "La empresa es obligatoria")
    private Empresa empresa;

    /**
     * Cliente que ha realizado la reserva.
     */
    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_PERSONA", nullable = false)
    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    /**
     * Servicio reservado por el cliente.
     */
    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    @NotNull(message = "El servicio es obligatorio")
    private Servicio servicio;

    /**
     * Fecha en la que se ha realizado la reserva.
     */
    @Column(name = "FECHA", nullable = false)
    @NotNull(message = "La fecha de la reserva es obligatoria")
    @Size(max = 50, message = "El campo no puede exceder los 50 caracteres")
    private String fechaReserva;

    /**
     * Hora de la reserva.
     */
    @Column(name = "HORA", nullable = false)
    @NotNull(message = "La hora de la reserva es obligatoria")
    @Size(max = 50, message = "El campo no puede exceder los 50 caracteres")
    private String hora;

    /**
     * Estado actual de la reserva (por ejemplo: "Pendiente", "Confirmada",
     * "Cancelada").
     */
    @Column(name = "ESTADO", nullable = false, length = 50)
    @NotNull(message = "El estado de la reserva es obligatorio")
    @Size(max = 50, message = "El campo no puede exceder los 50 caracteres")
    private String estado;

    /**
     * Fecha de alta de la reserva en el sistema. Se establece automáticamente
     * al crearse la entidad.
     */
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime fechaAlta;

    /**
     * Fecha de la última modificación de la reserva. Se actualiza
     * automáticamente con cada cambio en la entidad.
     */
    @Column(name = "FECHA_MODIFICACION", nullable = false)
    @UpdateTimestamp
    private LocalDateTime fechaModificacion;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Reserva() {
    }

    /**
     * Constructor con parámetros para inicializar una reserva.
     *
     * @param empresa Empresa asociada a la reserva.
     * @param cliente Cliente que realiza la reserva.
     * @param servicio Servicio reservado.
     * @param fechaReserva Fecha de la reserva.
     * @param hora Hora de la reserva.
     * @param estado Estado actual de la reserva.
     */
    public Reserva(Empresa empresa, Cliente cliente, Servicio servicio, String fechaReserva, String hora, String estado) {
        this.empresa = empresa;
        this.cliente = cliente;
        this.servicio = servicio;
        this.fechaReserva = fechaReserva;
        this.hora = hora;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador único de la reserva.
     *
     * @return ID de la reserva.
     */
    public Long getIdReserva() {
        return idReserva;
    }

    /**
     * Establece el identificador único de la reserva.
     *
     * @param idReserva ID a asignar.
     */
    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    /**
     * Obtiene la empresa asociada a la reserva.
     *
     * @return Empresa de la reserva.
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * Establece la empresa asociada a la reserva.
     *
     * @param empresa Empresa a asignar.
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * Obtiene el cliente que realizó la reserva.
     *
     * @return Cliente de la reserva.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el cliente que realizó la reserva.
     *
     * @param cliente Cliente a asignar.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtiene el servicio reservado.
     *
     * @return Servicio de la reserva.
     */
    public Servicio getServicio() {
        return servicio;
    }

    /**
     * Establece el servicio reservado.
     *
     * @param servicio Servicio a asignar.
     */
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    /**
     * Obtiene la fecha de la reserva.
     *
     * @return Fecha de la reserva.
     */
    public String getFechaReserva() {
        return fechaReserva;
    }

    /**
     * Establece la fecha de la reserva.
     *
     * @param fechaReserva Fecha a asignar.
     */
    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    /**
     * Obtiene la hora de la reserva.
     *
     * @return Hora de la reserva.
     */
    public String getHora() {
        return hora;
    }

    /**
     * Establece la hora de la reserva.
     *
     * @param hora Hora a asignar.
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * Obtiene el estado de la reserva.
     *
     * @return Estado de la reserva.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la reserva.
     *
     * @param estado Estado a asignar.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha de alta de la reserva.
     *
     * @return Fecha de alta.
     */
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Obtiene la fecha de última modificación de la reserva.
     *
     * @return Fecha de modificación.
     */
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
}
