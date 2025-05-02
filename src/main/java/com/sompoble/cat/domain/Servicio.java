package com.sompoble.cat.domain;

import jakarta.persistence.CascadeType;
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
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
/**
 * Representa un servicio ofrecido por una empresa.
 * <p>
 * Un servicio tiene un nombre, descripción, duración, precio, límite de reservas, una empresa a la que pertenece,
 * y tiene una lista de reservas y horarios asociados.
 * </p>
 */
@Entity
@Table(name = "SERVICIO")
public class Servicio implements Serializable {
	/**
     * Identificador único del servicio.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_SERVICIO")
    private Long idServicio;
    /**
     * Nombre del servicio.
     * Este campo es obligatorio y no puede exceder los 100 caracteres.
     */
    @Column(name = "NOMBRE", nullable = false, length = 100) 
    @NotNull
    @Size(max = 100)
    private String nombre;
    /**
     * Descripción del servicio.
     * Este campo es obligatorio.
     */
    @Column(name = "DESCRIPCION", nullable = false) 
    @NotNull
    private String descripcion;
    /**
     * Duración del servicio en minutos.
     * Este campo es obligatorio.
     */
    @Column(name = "DURACION", nullable = false) 
    @NotNull
    private int duracion;
    /**
     * Precio del servicio.
     * Este campo es obligatorio.
     */
    @Column(name = "PRECIO", nullable = false) 
    @NotNull
    private float precio;
    /**
     * Límite de reservas permitidas para el servicio.
     * Este campo es obligatorio.
     */
    @Column(name="LIMITE_RESERVAS", nullable = false)
    @NotNull
    private int limiteReservas;
    /**
     * La empresa a la que pertenece este servicio.
     * Este campo es obligatorio.
     */
    @ManyToOne
    @JoinColumn(name="ID_EMPRESA", referencedColumnName = "ID_EMPRESA", nullable = false)
    @NotNull
    private Empresa empresa;
    /**
     * Fecha de alta del servicio. Se genera automáticamente al crear el servicio.
     */
    @CreationTimestamp
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    private LocalDateTime fechaAlta = LocalDateTime.now();
    /**
     * Fecha de última modificación del servicio. Se actualiza automáticamente al modificar el servicio.
     */
    @UpdateTimestamp
    @Column(name = "FECHA_MODIFICACION", nullable = false)
    private LocalDateTime fechaModificacion = LocalDateTime.now();
    /**
     * Lista de reservas asociadas a este servicio.
     * Este campo no puede ser nulo.
     */
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL)
    @NotNull
    private List<Reserva> reservas = new ArrayList<>();
    /**
     * Lista de horarios asociados a este servicio.
     */
    @OneToMany(mappedBy = "servicio")
    private List<Horario> horarios;
    
    /**
     * Obtiene la lista de horarios asociados a este servicio.
     * 
     * @return La lista de horarios asociados a este servicio.
     */
    public List<Horario> getHorarios() {
		return horarios;
	}
    /**
     * Establece la lista de horarios asociados a este servicio.
     * 
     * @param horarios La lista de horarios a establecer.
     */
	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}
	 /**
     * Establece el identificador único del servicio.
     * 
     * @param idServicio El identificador único del servicio.
     */
	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}
	/**
     * Establece la fecha de alta del servicio.
     * 
     * @param fechaAlta La fecha de alta del servicio.
     */
	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	/**
     * Establece la fecha de última modificación del servicio.
     * 
     * @param fechaModificacion La fecha de última modificación del servicio.
     */
	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	/**
     * Constructor vacío necesario para la persistencia.
     */
	public Servicio() {
    }
	 /**
     * Constructor con parámetros para inicializar un servicio con los datos proporcionados.
     * 
     * @param nombre El nombre del servicio.
     * @param descripcion La descripción del servicio.
     * @param duracion La duración del servicio en minutos.
     * @param precio El precio del servicio.
     * @param limiteReservas El límite de reservas para este servicio.
     * @param empresa La empresa a la que pertenece este servicio.
     */
    public Servicio(String nombre, String descripcion, int duracion, float precio, int limiteReservas, Empresa empresa) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.precio = precio;
        this.limiteReservas = limiteReservas;
        this.empresa = empresa;
    }
    /**
     * Obtiene el identificador único del servicio.
     * 
     * @return El identificador único del servicio.
     */
    public Long getIdServicio() {
        return idServicio;
    }
    /**
     * Obtiene el nombre del servicio.
     * 
     * @return El nombre del servicio.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Establece el nombre del servicio.
     * 
     * @param nombre El nombre del servicio.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Obtiene la descripción del servicio.
     * 
     * @return La descripción del servicio.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * Establece la descripción del servicio.
     * 
     * @param descripcion La descripción del servicio.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * Obtiene la duración del servicio en minutos.
     * 
     * @return La duración del servicio.
     */
    public int getDuracion() {
        return duracion;
    }
    /**
     * Establece la duración del servicio en minutos.
     * 
     * @param duracion La duración del servicio.
     */
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    /**
     * Obtiene el precio del servicio.
     * 
     * @return El precio del servicio.
     */
    public float getPrecio() {
        return precio;
    }
    /**
     * Establece el precio del servicio.
     * 
     * @param precio El precio del servicio.
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }
    /**
     * Obtiene el límite de reservas para este servicio.
     * 
     * @return El límite de reservas del servicio.
     */
    public int getLimiteReservas() {
        return limiteReservas;
    }
    /**
     * Establece el límite de reservas para este servicio.
     * 
     * @param limiteReservas El límite de reservas del servicio.
     */
    public void setLimiteReservas(int limiteReservas) {
        this.limiteReservas = limiteReservas;
    }
    /**
     * Obtiene la empresa a la que pertenece este servicio.
     * 
     * @return La empresa a la que pertenece el servicio.
     */
    public Empresa getEmpresa() {
        return empresa;
    }
    /**
     * Establece la empresa a la que pertenece este servicio.
     * 
     * @param empresa La empresa a la que pertenece el servicio.
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    /**
     * Obtiene la fecha de alta del servicio.
     * 
     * @return La fecha de alta del servicio.
     */
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }
    /**
     * Obtiene la fecha de última modificación del servicio.
     * 
     * @return La fecha de última modificación del servicio.
     */
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
    /**
     * Obtiene la lista de reservas asociadas a este servicio.
     * 
     * @return La lista de reservas asociadas al servicio.
     */
    public List<Reserva> getReservas() {
        return reservas;
    }
    /**
     * Establece la lista de reservas asociadas a este servicio.
     * 
     * @param reservas La lista de reservas a establecer.
     */
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}