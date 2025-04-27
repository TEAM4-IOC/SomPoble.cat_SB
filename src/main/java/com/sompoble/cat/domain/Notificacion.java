package com.sompoble.cat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Entidad que representa una notificación en el sistema.
 * <p>
 * Almacena información sobre notificaciones dirigidas a clientes o empresarios,
 * con validaciones para garantizar consistencia en los datos. Requiere que cada
 * notificación esté asociada a al menos un destinatario (cliente o empresario).
 * </p>
 */
@Entity
@Table(name = "NOTIFICACION")
public class Notificacion implements Serializable {

    /**
     * Enumeración que define los tipos permitidos de notificación.
     */
    public enum TipoNotificacion {
        INFORMACION,
        ADVERTENCIA,
        ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NOTIFICACION")
    private Long idNotificacion;

    /**
     * Cliente asociado a la notificación (puede ser nulo si hay empresario).
     */
    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    /**
     * Empresario asociado a la notificación (puede ser nulo si hay cliente).
     */
    @ManyToOne
    @JoinColumn(name = "ID_EMPRESARIO")
    private Empresario empresario;

    /**
     * Contenido informativo de la notificación (obligatorio).
     */
    @Column(name = "MENSAJE", nullable = false)
    @NotNull(message = "El mensaje es obligatorio")
    private String mensaje;

    /**
     * Tipo de notificación (limitado a valores predefinidos).
     */
    @Column(name = "TIPO", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de notificación es obligatorio")
    private TipoNotificacion tipo;

    /**
     * Fecha de creación automática de la notificación.
     */
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime fechaAlta;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Notificacion() {}

    /**
     * Constructor con validación de destinatario.
     *
     * @param cliente    Cliente destinatario (puede ser nulo)
     * @param empresario Empresario destinatario (puede ser nulo)
     * @param mensaje    Contenido de la notificación
     * @param tipo       Tipo de notificación
     * @throws IllegalArgumentException Si ambos destinatarios son nulos
     */
    public Notificacion(Cliente cliente, Empresario empresario, String mensaje, TipoNotificacion tipo) {
        if (cliente == null && empresario == null) {
            throw new IllegalArgumentException("Debe especificar cliente o empresario");
        }
        this.cliente = cliente;
        this.empresario = empresario;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }


    public Long getIdNotificacion() {
        return idNotificacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empresario getEmpresario() {
        return empresario;
    }

    public void setEmpresario(Empresario empresario) {
        this.empresario = empresario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Verifica si la notificación está dirigida a un cliente.
     *
     * @return true si tiene cliente asociado, false en caso contrario
     */
    public boolean esParaCliente() {
        return cliente != null;
    }

    /**
     * Verifica si la notificación está dirigida a un empresario.
     *
     * @return true si tiene empresario asociado, false en caso contrario
     */
    public boolean esParaEmpresario() {
        return empresario != null;
    }

    /**
     * Valida que la notificación tenga al menos un destinatario.
     *
     * @throws IllegalStateException Si no hay destinatario válido
     */
    public void validarDestinatario() {
        if (cliente == null && empresario == null) {
            throw new IllegalStateException("Notificación sin destinatario válido");
        }
    }
}