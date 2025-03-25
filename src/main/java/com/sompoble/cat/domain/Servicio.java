package com.sompoble.cat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "SERVICIO")
public class Servicio implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_SERVICIO")
    private Long idServicio;
    
    @Column(name = "NOMBRE", nullable = false, length = 100) 
    @NotNull(message = "El nombre del servicio es obligatorio")
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String nombre;
    
    @Column(name = "DESCRIPCION", nullable = false) 
    @NotNull(message = "La descripción del servicio es obligatoria")
    private String descripcion;
    
    @Column(name = "DURACION", nullable = false) 
    @Size(max = 255, message = "El campo no puede exceder los 255 caracteres")
    @NotNull(message = "La duración del servicio es obligatoria")
    private String duracion;
    
    @Column(name = "PRECIO", nullable = false) 
    @Size(max = 255, message = "El campo no puede exceder los 255 caracteres")
    @NotNull(message = "El precio del servicio es obligatorio")
    private String precio;
    
    @Column(name="LIMITE_RESERVAS", nullable = false)
    @NotNull(message = "El limite de reservas del servicio es obligatorio")
    private int limiteReservas;
    
    @ManyToOne
    @JoinColumn(name="ID_EMPRESA", referencedColumnName = "ID_EMPRESA", nullable = false)
    @NotNull(message = "Debe asociarse a una empresa/autónomo")
    private Empresa empresa;
    
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    @NotNull
    private LocalDateTime fechaAlta;

    @Column(name = "FECHA_MODIFICACION", nullable = false)
    @UpdateTimestamp
    @NotNull
    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "servicio")
    private List<Reserva> reservas;
    
    public Servicio() {
    }

    public Servicio(String nombre, String descripcion, String duracion, String precio, int limiteReservas, Empresa empresa) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.precio = precio;
        this.limiteReservas = limiteReservas;
        this.empresa = empresa;
    }

    public Long getIdServicio() {
        return idServicio;
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

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
    
    public float getLimiteReservas() {
        return limiteReservas;
    }

    public void setLimiteReservas(int limiteReservas) {
        this.limiteReservas = limiteReservas;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
    
    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}