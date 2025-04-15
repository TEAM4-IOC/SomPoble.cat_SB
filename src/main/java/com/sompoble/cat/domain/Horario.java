package com.sompoble.cat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Representa el horario laboral asociado a un servicio y una empresa.
 * <p>
 * Esta clase contiene la información de los horarios de trabajo de una empresa para un servicio específico.
 * Además, registra los días laborables, las horas de inicio y fin, y las fechas de alta y modificación.
 * </p>
 */
@Entity
@Table(name = "HORARIO")
public class Horario implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Identificador único del horario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long idHorario;
    /**
     * Días de la semana en los que se realiza el servicio (por ejemplo, "Lunes a Viernes").
     */
    @Column(name = "DIAS_LABORABLES")
    private String diasLaborables;
    /**
     * Hora de inicio del servicio.
     * <p>
     * Este campo es obligatorio y no puede ser nulo.
     * </p>
     */
    @Column(name = "HORARIO_INICIO", nullable = false)
    @NotNull(message = "El horario de inicio es obligatorio")
    private LocalTime horarioInicio;
    /**
     * Hora de finalización del servicio.
     * <p>
     * Este campo es obligatorio y no puede ser nulo.
     * </p>
     */
    @Column(name = "HORARIO_FIN", nullable = false)
    @NotNull(message = "El horario de fin es obligatorio")
    private LocalTime horarioFin;
    /**
     * Fecha en la que se creó el horario.
     * Este campo se actualiza automáticamente al crear una nueva instancia.
     */
    @CreationTimestamp
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    private LocalDateTime fechaAlta;
    /**
     * Fecha en la que se realizó la última modificación del horario.
     * Este campo se actualiza automáticamente cada vez que se actualiza el horario.
     */
    @UpdateTimestamp
    @Column(name = "FECHA_MODIFICACION", nullable = false)
    private LocalDateTime fechaModificacion;
    /**
     * Empresa asociada a este horario.
     * Relación muchos a uno con la entidad {@link Empresa}.
     */
    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_EMPRESA", nullable = false)
    @NotNull(message = "Debe asociarse a una empresa/autónomo")
    private Empresa empresa;
    /**
     * Servicio asociado a este horario.
     * Relación muchos a uno con la entidad {@link Servicio}.
     */
    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    private Servicio servicio;

    /**
     * Constructor vacío.
     * <p>
     * Este constructor es necesario para que JPA funcione correctamente.
     * </p>
     */
    public Horario() {}

    /**
     * Constructor con parámetros.
     *
     * @param diasLaborables Días laborables en formato String.
     * @param horarioInicio Hora de inicio del servicio.
     * @param horarioFin Hora de fin del servicio.
     * @param empresa Empresa asociada al horario.
     */
    public Horario(String diasLaborables, LocalTime horarioInicio, LocalTime horarioFin, Empresa empresa) {
        this.diasLaborables = diasLaborables;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.empresa = empresa;
    }
    /**
     * Obtiene el identificador único del horario.
     * 
     * @return El identificador del horario.
     */
    public Long getIdHorario() {
        return idHorario;
    }
    /**
     * Establece el identificador único del horario.
     * 
     * @param idHorario El identificador del horario.
     */
    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }
    /**
     * Obtiene los días laborables en los que se ofrece el servicio.
     * 
     * @return Días laborables del servicio.
     */
    public String getDiasLaborables() {
        return diasLaborables;
    }
    /**
     * Establece los días laborables en los que se ofrece el servicio.
     * 
     * @param diasLaborables Días laborables del servicio.
     */
    public void setDiasLaborables(String diasLaborables) {
        this.diasLaborables = diasLaborables;
    }
    /**
     * Obtiene la hora de inicio del servicio.
     * 
     * @return La hora de inicio del servicio.
     */
    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }
    /**
     * Establece la hora de inicio del servicio.
     * 
     * @param horarioInicio Hora de inicio del servicio.
     */
    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }
    /**
     * Obtiene la hora de fin del servicio.
     * 
     * @return La hora de fin del servicio.
     */
    public LocalTime getHorarioFin() {
        return horarioFin;
    }
    /**
     * Establece la hora de fin del servicio.
     * 
     * @param horarioFin Hora de fin del servicio.
     */
    public void setHorarioFin(LocalTime horarioFin) {
        this.horarioFin = horarioFin;
    }
    /**
     * Obtiene la empresa asociada al horario.
     * 
     * @return La empresa asociada al horario.
     */
    public Empresa getEmpresa() {
        return empresa;
    }
    /**
     * Establece la empresa asociada al horario.
     * 
     * @param empresa La empresa asociada al horario.
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    /**
     * Obtiene la fecha en que se creó el horario.
     * 
     * @return La fecha de alta del horario.
     */
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }
    /**
     * Establece la fecha de alta del horario.
     * 
     * @param fechaAlta La fecha de alta del horario.
     */
    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    /**
     * Obtiene la fecha de la última modificación del horario.
     * 
     * @return La fecha de modificación del horario.
     */
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
    /**
     * Establece la fecha de la última modificación del horario.
     * 
     * @param fechaModificacion La fecha de modificación del horario.
     */
    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    /**
     * Obtiene el servicio asociado al horario.
     * 
     * @return El servicio asociado al horario.
     */
    public Servicio getServicio() {
        return servicio;
    }
    /**
     * Establece el servicio asociado al horario.
     * 
     * @param servicio El servicio asociado al horario.
     */
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
    /**
     * Devuelve una representación en cadena de caracteres del horario.
     * 
     * @return Una cadena que representa el horario.
     */
    @Override
    public String toString() {
        return "Horario{" +
                "idHorario=" + idHorario +
                ", diasLaborables=" + diasLaborables +
                ", horarioInicio=" + horarioInicio +
                ", horarioFin=" + horarioFin +
                ", empresa=" + empresa +
                '}';
    }
}
