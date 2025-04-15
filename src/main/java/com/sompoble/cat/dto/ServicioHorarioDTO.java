package com.sompoble.cat.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
/**
 * DTO que agrupa información de un {@link Servicio} junto con su {@link Horario} asociado.
 * Facilita la transferencia de datos combinados entre capas del sistema.
 */
public class ServicioHorarioDTO {
	
	//--------------------------------------------------------------------------//
	// Campos de Servicio
	
	  /** ID del servicio */
    private Long idServicio;
    /** Nombre del servicio */
    private String nombre;
    /** Descripción del servicio */
    private String descripcion;
    /** Duración del servicio en minutos */
    private int duracion;
    /** Precio del servicio */
    private float precio;
    /** Límite máximo de reservas permitidas para el servicio */
    private int limiteReservas;
    /** Fecha de creación del servicio */
    private LocalDateTime fechaAltaServicio;
    /** Fecha de la última modificación del servicio */
    private LocalDateTime fechaModificacionServicio;
    /** ID de la empresa que ofrece el servicio */
    private Long empresaId;
    /** Identificador fiscal de la empresa (NIF/CIF) */
    @JsonProperty("identificadorFiscal")
    private String identificadorFiscal;

    //-------------------------------------------------------------------------//
    // Campos de Horario
    
    /** ID del horario asociado */
    private Long idHorario;
    /** Días laborables en que se ofrece el servicio (ej. "Lunes,Martes") */
    private String diasLaborables; 
    /** Hora de inicio del servicio */
    private LocalTime horarioInicio;
    /** Hora de finalización del servicio */
    private LocalTime horarioFin;
    /** Fecha de alta del horario */
    private LocalDateTime fechaAltaHorario;
    /** Fecha de modificación del horario */
    private LocalDateTime fechaModificacionHorario;

    //-------------------------------------------------------------------------//
    /**
     * Constructor vacío requerido para serialización/deserialización.
     */
    public ServicioHorarioDTO() {}

    /**
     * Constructor que inicializa este DTO a partir de entidades {@link Servicio} y {@link Horario}.
     *
     * @param servicio Objeto de tipo Servicio.
     * @param horario Objeto de tipo Horario.
     */
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
        this.identificadorFiscal = servicio.getEmpresa().getIdentificadorFiscal();

        this.idHorario = horario.getIdHorario();
        this.diasLaborables = String.join(",", horario.getDiasLaborables());  
        this.horarioInicio = horario.getHorarioInicio();
        this.horarioFin = horario.getHorarioFin();
        this.fechaAltaHorario = horario.getFechaAlta();
        this.fechaModificacionHorario = horario.getFechaModificacion();
    }

    /**
     * Obtiene el ID del servicio.
     * 
     * @return ID del servicio.
     */
    public Long getIdServicio() { return idServicio; }
    /**
     * Establece el ID del servicio.
     * 
     * @param idServicio ID del servicio.
     */
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }
    /**
     * Obtiene el nombre del servicio.
     * 
     * @return Nombre del servicio.
     */
    public String getNombre() { return nombre; }
    /**
     * Establece el nombre del servicio.
     * 
     * @param nombre Nombre del servicio.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    /**
     * Obtiene la descripción del servicio.
     * 
     * @return Descripción del servicio.
     */
    public String getDescripcion() { return descripcion; }
    /**
     * Establece la descripción del servicio.
     * 
     * @param descripcion Descripción del servicio.
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    /**
     * Obtiene la duración del servicio en minutos.
     * 
     * @return Duración del servicio.
     */
    public int getDuracion() { return duracion; }
    /**
     * Establece la duración del servicio en minutos.
     * 
     * @param duracion Duración en minutos.
     */
    public void setDuracion(int duracion) { this.duracion = duracion; }
    /**
     * Obtiene el precio del servicio.
     * 
     * @return Precio del servicio.
     */
    public float getPrecio() { return precio; }
    /**
     * Establece el precio del servicio.
     * 
     * @param precio Precio del servicio.
     */
    public void setPrecio(float precio) { this.precio = precio; }
    /**
     * Obtiene el límite de reservas permitidas para el servicio.
     * 
     * @return Límite de reservas.
     */
    public int getLimiteReservas() { return limiteReservas; }
    /**
     * Establece el límite de reservas permitidas para el servicio.
     * 
     * @param limiteReservas Número máximo de reservas.
     */
    public void setLimiteReservas(int limiteReservas) { this.limiteReservas = limiteReservas; }
    /**
     * Obtiene la fecha de alta del servicio.
     * 
     * @return Fecha de alta.
     */
    public LocalDateTime getFechaAltaServicio() { return fechaAltaServicio; }
    /**
     * Establece la fecha de alta del servicio.
     * 
     * @param fechaAltaServicio Fecha de creación del servicio.
     */
    public void setFechaAltaServicio(LocalDateTime fechaAltaServicio) { this.fechaAltaServicio = fechaAltaServicio; }
    /**
     * Obtiene la fecha de la última modificación del servicio.
     * 
     * @return Fecha de modificación.
     */
    public LocalDateTime getFechaModificacionServicio() { return fechaModificacionServicio; }
    /**
     * Establece la fecha de la última modificación del servicio.
     * 
     * @param fechaModificacionServicio Fecha de modificación.
     */
    public void setFechaModificacionServicio(LocalDateTime fechaModificacionServicio) { this.fechaModificacionServicio = fechaModificacionServicio; }
    /**
     * Obtiene el ID de la empresa que ofrece el servicio.
     * 
     * @return ID de la empresa.
     */
    public Long getEmpresaId() { return empresaId; }
    /**
     * Establece el ID de la empresa que ofrece el servicio.
     * 
     * @param empresaId ID de la empresa.
     */
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }
    /**
     * Obtiene el identificador fiscal de la empresa.
     * 
     * @return Identificador fiscal.
     */
    public String getIdentificadorFiscal() { return identificadorFiscal; }
    /**
     * Establece el identificador fiscal de la empresa.
     * 
     * @param identificadorFiscal NIF o CIF de la empresa.
     */
    public void setIdentificadorFiscal(String identificadorFiscal) { this.identificadorFiscal = identificadorFiscal; }
    /**
     * Obtiene el ID del horario asociado.
     * 
     * @return ID del horario.
     */
    public Long getIdHorario() { return idHorario; }
    /**
     * Establece el ID del horario asociado.
     * 
     * @param idHorario ID del horario.
     */
    public void setIdHorario(Long idHorario) { this.idHorario = idHorario; }
    /**
     * Obtiene los días laborables en los que se ofrece el servicio.
     * 
     * @return Días laborables como cadena separada por comas.
     */
    public String getDiasLaborables() { return (String) diasLaborables; } 
    /**
     * Establece los días laborables en los que se ofrece el servicio.
     * 
     * @param diasLaborables Cadena separada por comas de los días laborables.
     */
    public void setDiasLaborables(String diasLaborables) { this.diasLaborables = diasLaborables; }
    /**
     * Obtiene la hora de inicio del servicio.
     * 
     * @return Hora de inicio.
     */
    public LocalTime getHorarioInicio() { return horarioInicio; }
    /**
     * Establece la hora de inicio del servicio.
     * 
     * @param horarioInicio Hora de inicio.
     */
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }
    /**
     * Obtiene la hora de fin del servicio.
     * 
     * @return Hora de fin.
     */
    public LocalTime getHorarioFin() { return horarioFin; }
    /**
     * Establece la hora de fin del servicio.
     * 
     * @param horarioFin Hora de fin.
     */
    public void setHorarioFin(LocalTime horarioFin) { this.horarioFin = horarioFin; }
    /**
     * Obtiene la fecha de alta del horario.
     * 
     * @return Fecha de alta del horario.
     */
    public LocalDateTime getFechaAltaHorario() { return fechaAltaHorario; }
    /**
     * Establece la fecha de alta del horario.
     * 
     * @param fechaAltaHorario Fecha de alta del horario.
     */
    public void setFechaAltaHorario(LocalDateTime fechaAltaHorario) { this.fechaAltaHorario = fechaAltaHorario; }
    /**
     * Obtiene la fecha de modificación del horario.
     * 
     * @return Fecha de última modificación del horario.
     */
    public LocalDateTime getFechaModificacionHorario() { return fechaModificacionHorario; }
    /**
     * Establece la fecha de modificación del horario.
     * 
     * @param fechaModificacionHorario Fecha de modificación del horario.
     */
    public void setFechaModificacionHorario(LocalDateTime fechaModificacionHorario) { this.fechaModificacionHorario = fechaModificacionHorario; }
    /**
     * Representación en forma de texto del objeto {@code ServicioHorarioDTO}.
     * 
     * @return Cadena con los valores de todos los atributos del objeto.
     */
    @Override
    public String toString() {
        return "ServicioHorarioDTO{" +
                "idServicio=" + idServicio +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracion=" + duracion +
                ", precio=" + precio +
                ", limiteReservas=" + limiteReservas +
                ", fechaAltaServicio=" + fechaAltaServicio +
                ", fechaModificacionServicio=" + fechaModificacionServicio +
                ", empresaId=" + empresaId +
                ", identificadorFiscal='" + identificadorFiscal + '\'' +
                ", idHorario=" + idHorario +
                ", diasLaborables=" + diasLaborables +  
                ", horarioInicio=" + horarioInicio +
                ", horarioFin=" + horarioFin +
                ", fechaAltaHorario=" + fechaAltaHorario +
                ", fechaModificacionHorario=" + fechaModificacionHorario +
                '}';
    }
}
