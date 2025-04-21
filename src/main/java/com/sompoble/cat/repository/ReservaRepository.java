package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.dto.ReservaDTO;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad {@code Reserva}.
 * <p>
 * Proporciona métodos para gestionar reservas en la base de datos.
 * </p>
 */
@Repository
public interface ReservaRepository {

    /**
     * Obtiene todas las reservas asociadas a un cliente mediante su DNI.
     *
     * @param dni el documento nacional de identidad del cliente.
     * @return una lista de reservas realizadas por el cliente con el DNI
     * especificado.
     */
    List<ReservaDTO> findByClienteDni(String dni);

    /**
     * Obtiene todas las reservas asociadas a una empresa mediante su
     * identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return una lista de reservas asociadas a la empresa especificada.
     */
    List<ReservaDTO> findByEmpresaIdentificadorFiscal(String identificadorFiscal);

    /**
     * Busca una reserva por su identificador único.
     *
     * @param id el identificador de la reserva.
     * @return un {@code Optional} que contiene la reserva si existe, o vacío si
     * no se encuentra.
     */
    ReservaDTO findById(Long id);

    /**
     * Busca una reserva completa por su identificador único, devolviendo la
     * entidad completa.
     *
     * @param id el identificador de la reserva.
     * @return la entidad {@code Reserva} completa si existe, o null si no se
     * encuentra.
     */
    Reserva findByIdFull(Long id);

    /**
     * Guarda una nueva reserva en la base de datos. Si la reserva ya existe, la
     * actualiza.
     *
     * @param reserva la reserva a guardar.
     */
    void addReserva(Reserva reserva);

    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param reserva Objeto {@link Reserva} con la información actualizada.
     */
    void updateReserva(Reserva reserva);

    /**
     * Elimina una reserva de la base de datos mediante su identificador.
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
    int countByServicioIdAndFechaReserva(Long servicioId, String fechaReserva);
}
