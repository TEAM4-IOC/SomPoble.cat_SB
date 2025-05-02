package com.sompoble.cat.service;

import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.domain.Reserva;
import java.util.List;

/**
 * Servicio para gestionar las reservas.
 * <p>
 * Proporciona métodos para realizar operaciones sobre las reservas.
 * </p>
 */
public interface ReservaService {

    /**
     * Obtiene todas las reservas asociadas a un cliente mediante su DNI.
     *
     * @param dni el documento nacional de identidad del cliente.
     * @return una lista de DTOs de reservas realizadas por el cliente con el
     * DNI especificado.
     */
    List<ReservaDTO> findByClienteDni(String dni);

    /**
     * Obtiene todas las reservas asociadas a una empresa o autónomo mediante su
     * identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa o
     * autónomo.
     * @return una lista de DTOs de reservas asociadas a la empresa o autónomo
     * especificado.
     */
    List<ReservaDTO> findByEmpresaIdentificadorFiscal(String identificadorFiscal);

    /**
     * Busca una reserva por su identificador único.
     *
     * @param id el identificador de la reserva.
     * @return el DTO con la reserva correspondiente.
     */
    ReservaDTO findById(Long id);

    /**
     * Busca una reserva completa por su identificador único.
     *
     * @param id el identificador de la reserva.
     * @return la entidad {@code Reserva} completa si existe, o null si no se
     * encuentra.
     */
    Reserva findByIdFull(Long id);

    /**
     * Guarda una nueva reserva. Si la reserva ya existe, la actualiza.
     *
     * @param reserva la reserva a guardar.
     */
    void addReserva(Reserva reserva);

    /**
     * Actualiza una reserva existente.
     *
     * @param reserva la reserva con la información actualizada.
     */
    void updateReserva(Reserva reserva);

    /**
     * Elimina una reserva mediante su identificador.
     *
     * @param id el identificador de la reserva a eliminar.
     */
    void deleteById(Long id);

    /**
     * Elimina todas las reservas asociadas a un cliente mediante su DNI.
     *
     * @param dni el documento nacional de identidad del cliente.
     */
    void deleteByClienteDni(String dni);

    /**
     * Elimina todas las reservas asociadas a una empresa o autónomo mediante su
     * identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa o
     * autónomo.
     */
    void deleteByEmpresaIdentificadorFiscal(String identificadorFiscal);

    /**
     * Cuenta el número de reservas para un servicio específico en una fecha
     * determinada.
     *
     * @param servicioId el identificador del servicio.
     * @param fechaReserva la fecha de la reserva en formato String.
     * @return el número de reservas existentes para ese servicio en esa fecha.
     */
    int countReservasByServicioIdAndFecha(Long servicioId, String fechaReserva);
}
