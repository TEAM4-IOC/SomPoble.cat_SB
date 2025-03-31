package com.sompoble.cat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "HORARIO")
public class Horario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long idHorario;

   
    @Pattern(
        regexp = "^(Lunes|Martes|Miercoles|Jueves|Viernes|Sabado|Domingo)(,(Lunes|Martes|Miercoles|Jueves|Viernes|Sabado|Domingo))*$",
        message = "Días laborables deben ser una lista válida de días separados por comas (ej: 'Lunes,Martes')"
    )
    @Size(max = 255, message = "El campo no puede exceder los 255 caracteres")
    @Column(name = "DIAS_LABORALES", nullable = false, length = 255)
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
    @JoinColumn(name = "ID_EMPRESA",referencedColumnName = "ID_EMPRESA", nullable = false)
    @NotNull(message = "Debe asociarse a una empresa/autónomo")
    private Empresa empresa;

    
    public Horario() {}

    public Horario(String diasLaborables, LocalTime horarioInicio, LocalTime horarioFin, Empresa empresa) {
        this.diasLaborables = diasLaborables;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.empresa = empresa;
    }

  
    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }
    
    public Long getIdHorario() {
        return idHorario;
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

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "idHorario=" + idHorario +
                ", diasLaborables='" + diasLaborables + '\'' +
                ", horarioInicio=" + horarioInicio +
                ", horarioFin=" + horarioFin +
                ", empresa=" + empresa +
                '}';
    }
}