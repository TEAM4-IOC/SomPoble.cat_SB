package com.sompoble.cat.domain;

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
import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
/**
 * Representa una notificación asociada a un cliente y/o empresario.
 * <p>
 * Esta clase se utiliza para almacenar las notificaciones que pueden ser enviadas
 * a clientes y empresarios. Cada notificación contiene un mensaje, un tipo
 * de notificación y la fecha en la que fue creada.
 * </p>
 */
@Entity
@Table(name = "NOTIFICACION")
public class Notificacion implements Serializable {
	/**
     * Identificador único de la notificación.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_NOTIFICACION")
    private Long idNotificacion;
    /**
     * Cliente al que se le asocia la notificación.
     * Relación muchos a uno con la entidad {@link Cliente}.
     */
    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", nullable = true)
    private Cliente cliente;
    /**
     * Empresario al que se le asocia la notificación.
     * Relación muchos a uno con la entidad {@link Empresario}.
     */
    @ManyToOne
    @JoinColumn(name = "ID_EMPRESARIO", nullable = true)
    private Empresario empresario;
    /**
     * El mensaje de la notificación.
     * Este campo es obligatorio.
     */
    @Column(name = "MENSAJE", nullable = false)
    @NotNull
    private String mensaje;
    /**
     * El tipo de la notificación (por ejemplo, "información", "advertencia").
     * Este campo es obligatorio y no puede exceder los 50 caracteres.
     */
    @Column(name = "TIPO", nullable = false, length = 50) 
    @NotNull
    @Size(max = 50)
    private String tipo;
    /**
     * Fecha en la que se creó la notificación.
     * Este campo se actualiza automáticamente al crear una nueva instancia.
     */
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    @NotNull
    private LocalDateTime fechaAlta;
    /**
     * Constructor vacío.
     * <p>
     * Este constructor es necesario para que JPA funcione correctamente.
     * </p>
     */
    public Notificacion() {
    }
    /**
     * Constructor con parámetros.
     * 
     * @param cliente Cliente al que se le asocia la notificación.
     * @param empresario Empresario al que se le asocia la notificación.
     * @param mensaje El mensaje de la notificación.
     * @param tipo El tipo de la notificación.
     */
    public Notificacion(Cliente cliente, Empresario empresario, String mensaje, String tipo){
        this.cliente = cliente;
        this.empresario = empresario;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }
    /**
     * Obtiene el identificador único de la notificación.
     * 
     * @return El identificador de la notificación.
     */
    public Long getIdNotificacion() {
        return idNotificacion;
    }
    /**
     * Obtiene el cliente al que se le asocia la notificación.
     * 
     * @return El cliente al que se le asocia la notificación.
     */
    public Cliente getCliente() {
        return cliente;
    }
    /**
     * Establece el cliente al que se le asociará la notificación.
     * 
     * @param cliente El cliente al que se le asociará la notificación.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    /**
     * Obtiene el empresario al que se le asocia la notificación.
     * 
     * @return El empresario al que se le asocia la notificación.
     */
    public Empresario getEmpresario() {
        return empresario;
    }
    /**
     * Establece el empresario al que se le asociará la notificación.
     * 
     * @param empresario El empresario al que se le asociará la notificación.
     */
    public void setEmpresario(Empresario empresario) {
        this.empresario = empresario;
    }
    /**
     * Obtiene el mensaje de la notificación.
     * 
     * @return El mensaje de la notificación.
     */
    public String getMensaje() {
        return mensaje;
    }
    /**
     * Establece el mensaje de la notificación.
     * 
     * @param mensaje El mensaje de la notificación.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    /**
     * Obtiene el tipo de la notificación.
     * 
     * @return El tipo de la notificación.
     */
    public String getTipo() {
        return tipo;
    }
    /**
     * Establece el tipo de la notificación.
     * 
     * @param tipo El tipo de la notificación.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /**
     * Obtiene la fecha en la que se creó la notificación.
     * 
     * @return La fecha en la que se creó la notificación.
     */
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }
}
