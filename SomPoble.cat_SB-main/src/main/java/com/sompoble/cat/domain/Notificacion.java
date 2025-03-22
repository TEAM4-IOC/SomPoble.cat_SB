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

@Entity
@Table(name = "NOTIFICACION")
public class Notificacion implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_NOTIFICACION")
    private Long idNotificacion;
    
    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE", nullable = true)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESARIO", nullable = true)
    private Empresario empresario;

    @Column(name = "MENSAJE", nullable = false)
    @NotNull
    private String mensaje;
    
    @Column(name = "TIPO", nullable = false, length = 50) 
    @NotNull
    @Size(max = 50)
    private String tipo;
    
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    @NotNull
    private LocalDateTime fechaAlta;

    public Notificacion() {
    }
    
    public Notificacion(Cliente cliente, Empresario empresario, String mensaje, String tipo){
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }
}
