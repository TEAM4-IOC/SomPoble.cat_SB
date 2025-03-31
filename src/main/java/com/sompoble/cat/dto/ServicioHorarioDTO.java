package com.sompoble.cat.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;

public class ServicioHorarioDTO {
    // Campos de Servicio
    private Long idServicio;
    private String nombre;
    private String descripcion;
    private String duracion;
    private String precio;
    private int limiteReservas;
    private LocalDateTime fechaAltaServicio;
    private LocalDateTime fechaModificacionServicio;
    private Long empresaId;

    // Campos de Horario
    private Long idHorario;
    private String diasLaborables;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private LocalDateTime fechaAltaHorario;
    private LocalDateTime fechaModificacionHorario;

    // Constructor vacío
    public ServicioHorarioDTO() {}

    // Constructor para inicializar desde objetos Servicio y Horario
    public ServicioHorarioDTO(Servicio servicio, Horario horario) {
        this.idServicio = servicio.getIdServicio();
        this.nombre = servicio.getNombre();
        this.descripcion = servicio.getDescripcion();
        this.duracion = servicio.getDuracion();
        this.precio = servicio.getPrecio();
        this.limiteReservas = (int) servicio.getLimiteReservas();
        this.fechaAltaServicio = servicio.getFechaAlta();
        this.fechaModificacionServicio = servicio.getFechaModificacion();
        this.empresaId = servicio.getEmpresa().getIdEmpresa(); 

        this.idHorario = horario.getIdHorario();
        this.diasLaborables = horario.getDiasLaborables();
        this.horarioInicio = horario.getHorarioInicio();
        this.horarioFin = horario.getHorarioFin();
        this.fechaAltaHorario = horario.getFechaAlta();
        this.fechaModificacionHorario = horario.getFechaModificacion();
    }

    // Getters y setters
    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }

    public int getLimiteReservas() { return limiteReservas; }
    public void setLimiteReservas(int limiteReservas) { this.limiteReservas = limiteReservas; }

    public LocalDateTime getFechaAltaServicio() { return fechaAltaServicio; }
    public void setFechaAltaServicio(LocalDateTime fechaAltaServicio) { this.fechaAltaServicio = fechaAltaServicio; }

    public LocalDateTime getFechaModificacionServicio() { return fechaModificacionServicio; }
    public void setFechaModificacionServicio(LocalDateTime fechaModificacionServicio) { this.fechaModificacionServicio = fechaModificacionServicio; }

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }

    public Long getIdHorario() { return idHorario; }
    public void setIdHorario(Long idHorario) { this.idHorario = idHorario; }

    public String getDiasLaborables() { return diasLaborables; }
    public void setDiasLaborables(String diasLaborables) { this.diasLaborables = diasLaborables; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFin() { return horarioFin; }
    public void setHorarioFin(LocalTime horarioFin) { this.horarioFin = horarioFin; }

    public LocalDateTime getFechaAltaHorario() { return fechaAltaHorario; }
    public void setFechaAltaHorario(LocalDateTime fechaAltaHorario) { this.fechaAltaHorario = fechaAltaHorario; }

    public LocalDateTime getFechaModificacionHorario() { return fechaModificacionHorario; }
    public void setFechaModificacionHorario(LocalDateTime fechaModificacionHorario) { this.fechaModificacionHorario = fechaModificacionHorario; }

    @Override
    public String toString() {
        return "ServicioHorarioDTO{" +
                "idServicio=" + idServicio +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracion='" + duracion + '\'' +
                ", precio='" + precio + '\'' +
                ", limiteReservas=" + limiteReservas +
                ", fechaAltaServicio=" + fechaAltaServicio +
                ", fechaModificacionServicio=" + fechaModificacionServicio +
                ", empresaId=" + empresaId +
                ", idHorario=" + idHorario +
                ", diasLaborables='" + diasLaborables + '\'' +
                ", horarioInicio=" + horarioInicio +
                ", horarioFin=" + horarioFin +
                ", fechaAltaHorario=" + fechaAltaHorario +
                ", fechaModificacionHorario=" + fechaModificacionHorario +
                '}';
    }
}