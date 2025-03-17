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
import java.time.LocalTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "HORARIO")
public class Horario implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long idHorario;
    
    @Column(name = "DIAS_LABORABLES", nullable = false, length = 255) 
    @NotNull
    @Size(max = 255)
    private String diasLaborables;    
    
    @Column(name = "HORARIO_INICIO", nullable = false)
    @NotNull
    private LocalTime  horarioInicio;
    
    @Column(name = "HORARIO_FIN", nullable = false)
    @NotNull
    private LocalTime  horarioFin;
    
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    @NotNull
    private LocalDateTime fechaAlta;

    @Column(name = "FECHA_MODIFICACION", nullable = false)
    @UpdateTimestamp
    @NotNull
    private LocalDateTime fechaModificacion;
    
    @ManyToOne
    @JoinColumn(name="ID_EMPRESA", referencedColumnName = "ID_EMPRESA", nullable = false)
    @NotNull
    private Empresa empresa;
    
    public Horario() {
    }

    public Horario(String diasLaborables, LocalTime horarioInicio, LocalTime horarioFin, Empresa empresa) {
        this.diasLaborables = diasLaborables;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.empresa = empresa;
    }

    public String getDiasLaborables() {
        return diasLaborables;
    }

    public void setDiasLaborables(String diasLaborables) {
        this.diasLaborables = diasLaborables;
    }
    
    public Long getIdHorario() {
        return idHorario;
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
}
