package com.sompoble.cat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entidad que representa un evento en el sistema. Almacena información como
 * nombre, descripción, ubicación y fechas.
 */
@Entity
@Table(name = "EVENTO")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del evento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EVENTO")
    private Long idEvento;

    /**
     * Nombre del evento (máximo 100 caracteres).
     */
    @Column(name = "NOMBRE", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    private String nombre;

    /**
     * Descripción detallada del evento.
     */
    @Column(name = "DESCRIPCION", nullable = false)
    @NotNull
    private String descripcion;

    /**
     * Ubicación donde se realizará el evento (máximo 255 caracteres).
     */
    @Column(name = "UBICACION", nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    private String ubicacion;

    /**
     * Fecha y hora programada para el evento.
     */
    @Column(name = "FECHA_EVENTO", nullable = false)
    @NotNull
    private LocalDateTime fechaEvento;

    /**
     * URL de la imagen de la empresa almacenada en Cloudinary.
     */
    @Column(name = "IMAGEN_URL", nullable = true, length = 255)
    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres")
    private String imagenUrl;

    /**
     * ID público de la imagen en Cloudinary (necesario para eliminación).
     */
    @Column(name = "IMAGEN_PUBLIC_ID", nullable = true, length = 255)
    @Size(max = 255, message = "El ID público de la imagen no puede exceder los 255 caracteres")
    private String imagenPublicId;

    /**
     * Fecha de creación del evento (autogenerada).
     */
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    @NotNull(message = "La fecha de alta no debe ser nula")
    private LocalDateTime fechaAlta;

    /**
     * Fecha de última modificación del evento (autogenerada).
     */
    @Column(name = "FECHA_MODIFICACION", nullable = false)
    @UpdateTimestamp
    @NotNull(message = "La fecha de modificación no debe ser nula")
    private LocalDateTime fechaModificacion;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Evento() {
    }

    /**
     * Constructor con parámetros para inicializar un evento.
     *
     * @param nombre Nombre del evento.
     * @param descripcion Descripción del evento.
     * @param ubicacion Ubicación del evento.
     * @param fechaEvento Fecha y hora del evento.
     */
    public Evento(String nombre, String descripcion, String ubicacion, LocalDateTime fechaEvento) {
        setNombre(nombre);
        setDescripcion(descripcion);
        setUbicacion(ubicacion);
        this.fechaEvento = fechaEvento;
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaAlta == null) {
            this.fechaAlta = LocalDateTime.now();
        }
        if (this.fechaModificacion == null) {
            this.fechaModificacion = LocalDateTime.now();
        }
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    /**
     * Obtiene el ID único del evento.
     *
     * @return ID del evento.
     */
    public Long getIdEvento() {
        return idEvento;
    }

    /**
     * Establece el ID único del evento.
     *
     * @param idEvento ID del evento.
     */
    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    /**
     * Obtiene el nombre del evento.
     *
     * @return Nombre del evento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del evento.
     *
     * @param nombre Nombre del evento.
     */
    public void setNombre(String nombre) {
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede tener más de 100 caracteres");
        }
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del evento.
     *
     * @return Descripción del evento.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del evento.
     *
     * @param descripcion Descripción del evento.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la ubicación del evento.
     *
     * @return Ubicación del evento.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Establece la ubicación del evento.
     *
     * @param ubicacion Ubicación del evento.
     */
    public void setUbicacion(String ubicacion) {
        if (ubicacion.length() > 255) {
            throw new IllegalArgumentException("La ubicación no puede tener más de 255 caracteres");
        }
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene la fecha y hora programada para el evento.
     *
     * @return Fecha y hora del evento.
     */
    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    /**
     * Establece la fecha y hora del evento.
     *
     * @param fechaEvento Fecha y hora del evento.
     */
    public void setFechaEvento(LocalDateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    /**
     * Obtiene la URL de la imagen de la empresa.
     *
     * @return URL de la imagen.
     */
    public String getImagenUrl() {
        return imagenUrl;
    }

    /**
     * Establece la URL de la imagen de la empresa.
     *
     * @param imagenUrl URL de la imagen a establecer.
     */
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    /**
     * Obtiene el ID público de la imagen en Cloudinary.
     *
     * @return ID público de la imagen.
     */
    public String getImagenPublicId() {
        return imagenPublicId;
    }

    /**
     * Establece el ID público de la imagen en Cloudinary.
     *
     * @param imagenPublicId ID público a establecer.
     */
    public void setImagenPublicId(String imagenPublicId) {
        this.imagenPublicId = imagenPublicId;
    }

    /**
     * Obtiene la fecha de creación del evento.
     *
     * @return Fecha de creación (autogenerada).
     */
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Obtiene la fecha de última modificación del evento.
     *
     * @return Fecha de última modificación (autogenerada).
     */
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
}
