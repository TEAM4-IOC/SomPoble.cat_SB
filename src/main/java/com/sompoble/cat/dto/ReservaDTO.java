package com.sompoble.cat.dto;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) para la entidad Reserva.
 * <p>
 * Clase utilizada para transferir datos de una reserva sin exponer la entidad
 * completa. Contiene información esencial para el cliente y la empresa.
 * </p>
 *
 * @author SomPoble
 */
public class ReservaDTO implements Serializable {

    private Long idReserva;
    private String fechaReserva;
    private String hora;
    private String estado;
    private String dniCliente;
    private String identificadorFiscalEmpresa;
    private Long idServicio;
    private String nombreServicio;

    public ReservaDTO() {
    }

    /**
     * Constructor con parámetros para inicializar una reserva.
     *
     * @param idReserva Identificador único de la reserva.
     * @param fechaReserva Fecha de la reserva.
     * @param hora Hora de la reserva.
     * @param estado Estado actual de la reserva.
     * @param dniCliente DNI del cliente que realizó la reserva.
     * @param identificadorFiscalEmpresa Identificador fiscal de la empresa.
     * @param idServicio Identificador del servicio reservado.
     */
    public ReservaDTO(Long idReserva, String fechaReserva, String hora, String estado,
            String dniCliente, String identificadorFiscalEmpresa, Long idServicio) {
        this.idReserva = idReserva;
        this.fechaReserva = fechaReserva;
        this.hora = hora;
        this.estado = estado;
        this.dniCliente = dniCliente;
        this.identificadorFiscalEmpresa = identificadorFiscalEmpresa;
        this.idServicio = idServicio;
    }

    /**
     * Obtiene el identificador único de la reserva.
     *
     * @return ID de la reserva.
     */
    public Long getIdReserva() {
        return idReserva;
    }

    /**
     * Establece el identificador único de la reserva.
     *
     * @param idReserva ID de la reserva.
     */
    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    /**
     * Obtiene la fecha de la reserva.
     *
     * @return Fecha de la reserva.
     */
    public String getFechaReserva() {
        return fechaReserva;
    }

    /**
     * Establece la fecha de la reserva.
     *
     * @param fechaReserva Fecha de la reserva.
     */
    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    /**
     * Obtiene la hora de la reserva.
     *
     * @return Hora de la reserva.
     */
    public String getHora() {
        return hora;
    }

    /**
     * Establece la hora de la reserva.
     *
     * @param hora Hora de la reserva.
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * Obtiene el estado actual de la reserva.
     *
     * @return Estado de la reserva.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la reserva.
     *
     * @param estado Estado de la reserva.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el DNI del cliente asociado a la reserva.
     *
     * @return DNI del cliente.
     */
    public String getDniCliente() {
        return dniCliente;
    }

    /**
     * Establece el DNI del cliente asociado a la reserva.
     *
     * @param dniCliente DNI del cliente.
     */
    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    /**
     * Obtiene el identificador fiscal de la empresa asociada a la reserva.
     *
     * @return Identificador fiscal de la empresa.
     */
    public String getIdentificadorFiscalEmpresa() {
        return identificadorFiscalEmpresa;
    }

    /**
     * Establece el identificador fiscal de la empresa asociada a la reserva.
     *
     * @param identificadorFiscalEmpresa Identificador fiscal de la empresa o
     * autónomo.
     */
    public void setIdentificadorFiscalEmpresa(String identificadorFiscalEmpresa) {
        this.identificadorFiscalEmpresa = identificadorFiscalEmpresa;
    }

    /**
     * Obtiene el identificador del servicio reservado.
     *
     * @return ID del servicio.
     */
    public Long getIdServicio() {
        return idServicio;
    }

    /**
     * Establece el identificador del servicio reservado.
     *
     * @param idServicio ID del servicio.
     */
    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    /**
     * Obtiene el nombre del servicio reservado.
     *
     * @return Nombre del servicio.
     */
    public String getNombreServicio() {
        return nombreServicio;
    }

    /**
     * Establece el nombre del servicio reservado.
     *
     * @param nombreServicio Nombre del servicio.
     */
    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }
}
