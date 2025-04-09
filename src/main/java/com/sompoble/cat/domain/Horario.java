package com.sompoble.cat.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

/**
 * Representa el horario laboral asociado a un servicio y una empresa.
 */
@Entity
@Table(name = "HORARIO")
public class Horario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long idHorario;

    @Column(name = "DIAS_LABORABLES")
    private String diasLaborables;

    @Column(name = "HORARIO_INICIO", nullable = false)
    @NotNull(message = "El horario de inicio es obligatorio")
    private LocalTime horarioInicio;

    @Column(name = "HORARIO_FIN", nullable = false)
    @NotNull(message = "El horario de fin es obligatorio")
    private LocalTime horarioFin;

    @CreationTimestamp
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    private LocalDateTime fechaAlta;

    @UpdateTimestamp
    @Column(name = "FECHA_MODIFICACION", nullable = false)
    private LocalDateTime fechaModificacion;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_EMPRESA", nullable = false)
    @NotNull(message = "Debe asociarse a una empresa/autónomo")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO", referencedColumnName = "ID_SERVICIO", nullable = false)
    private Servicio servicio;

    /**
     * Constructor vacío.
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

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public String getDiasLaborables() {
        return diasLaborables;
    }

    public void setDiasLaborables(String diasLaborables) {
        this.diasLaborables = diasLaborables;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(LocalTime horarioFin) {
        this.horarioFin = horarioFin;
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

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

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
