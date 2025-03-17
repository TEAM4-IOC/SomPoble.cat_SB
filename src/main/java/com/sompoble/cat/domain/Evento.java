package com.sompoble.cat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "EVENTO")
public class Evento implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_EVENTO")
    private Long idEvento;
    
    @Column(name = "NOMBRE", nullable = false, length = 100) 
    @NotNull
    @Size(max = 100)
    private String nombre;
    
    @Column(name = "DESCRIPCION", nullable = false) 
    @NotNull
    private String descripcion;
    
    @Column(name = "UBICACION", nullable = false, length = 255) 
    @NotNull
    @Size(max = 255)
    private String ubicacion;
    
    @Column(name = "FECHA_EVENTO", nullable = false)
    @NotNull
    private LocalDateTime fechaEvento;
    
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    @NotNull
    private LocalDateTime fechaAlta;

    @Column(name = "FECHA_MODIFICACION", nullable = false)
    @UpdateTimestamp
    @NotNull
    private LocalDateTime fechaModificacion;

    public Evento() {
    }

    public Evento(String nombre, String descripcion, String ubicacion, LocalDateTime fechaEvento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fechaEvento = fechaEvento;
    }
    
    public Long getIdEvento() {
        return idEvento;
    }   

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
}