package com.sompoble.cat.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

@Entity
@Table(name = "SERVICIO")
public class Servicio implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    private String nombre;

    @Column(name = "DESCRIPCION", nullable = false)
    @NotNull
    private String descripcion;

    @Column(name = "DURACION", nullable = false)
    @NotNull
    private int duracion;

    @Column(name = "PRECIO", nullable = false)
    @NotNull
    private float precio;

    @Column(name="LIMITE_RESERVAS", nullable = false)
    @NotNull
    private int limiteReservas;

    @ManyToOne
    @JoinColumn(name="ID_EMPRESA", referencedColumnName = "ID_EMPRESA", nullable = false)
    @NotNull
    private Empresa empresa;

    @CreationTimestamp
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    private LocalDateTime fechaAlta = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "FECHA_MODIFICACION", nullable = false)
    private LocalDateTime fechaModificacion = LocalDateTime.now();

    @OneToMany(mappedBy = "servicio")
    @NotNull
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "servicio")
    private List<Horario> horarios;


    public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}

	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Servicio() {
    }

    public Servicio(String nombre, String descripcion, int duracion, float precio, int limiteReservas, Empresa empresa) {
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

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getLimiteReservas() {
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