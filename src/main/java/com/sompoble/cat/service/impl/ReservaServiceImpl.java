package com.sompoble.cat.service.impl;

import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.repository.ReservaRepository;
import com.sompoble.cat.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación de {@link ReservaService}.
 * <p>
 * Proporciona la lógica de negocio para gestionar las reservas, delegando las
 * operaciones al repositorio.
 * </p>
 *
 * @author SomPoble
 */
@Service
public class ReservaServiceImpl implements ReservaService {

    /**
     * Repositorio para acceder a los datos de las reservas.
     */
    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Obtiene todas las reservas asociadas a un cliente mediante su DNI.
     *
     * @param dni el documento nacional de identidad del cliente.
     * @return una lista de DTOs de reservas realizadas por el cliente con el
     * DNI especificado.
     */
    @Override
    public List<ReservaDTO> findByClienteDni(String dni) {
        return reservaRepository.findByClienteDni(dni);
    }

    /**
     * Obtiene todas las reservas asociadas a una empresa o autónomo mediante su
     * identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa o
     * autónomo.
     * @return una lista de DTOs de reservas asociadas a la empresa o autónomo
     * especificada.
     */
    @Override
    public List<ReservaDTO> findByEmpresaIdentificadorFiscal(String identificadorFiscal) {
        return reservaRepository.findByEmpresaIdentificadorFiscal(identificadorFiscal);
    }

    /**
     * Busca una reserva por su identificador único.
     *
     * @param id el identificador de la reserva.
     * @return un DTO con la reserva correspondiente, o {@code null} si no se
     * encuentra.
     */
    @Override
    public ReservaDTO findById(Long id) {
        return reservaRepository.findById(id);
    }

    /**
     * Busca una reserva completa por su identificador único.
     *
     * @param id el identificador de la reserva.
     * @return la entidad {@code Reserva} completa si existe, o null si no se
     * encuentra.
     */
    @Override
    public Reserva findByIdFull(Long id) {
        return reservaRepository.findByIdFull(id);
    }

    /**
     * Guarda una nueva reserva. Si la reserva ya existe, la actualiza.
     *
     * @param reserva la reserva a guardar o actualizar.
     */
    @Override
    public void addReserva(Reserva reserva) {
        reservaRepository.addReserva(reserva);
    }

    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param reserva la reserva con la información actualizada.
     */
    @Override
    public void updateReserva(Reserva reserva) {
        reservaRepository.updateReserva(reserva);
    }

    /**
     * Elimina una reserva mediante su identificador.
     *
     * @param id el identificador de la reserva a eliminar.
     */
    @Override
    public void deleteById(Long id) {
        reservaRepository.deleteById(id);
    }

    /**
     * Elimina todas las reservas asociadas a un cliente mediante su DNI.
     *
     * @param dni el documento nacional de identidad del cliente.
     */
    @Override
    public void deleteByClienteDni(String dni) {
        reservaRepository.deleteByClienteDni(dni);
    }

    /**
     * Elimina todas las reservas asociadas a una empresa o autónomo mediante su
     * identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa o
     * autónomo.
     */
    @Override
    public void deleteByEmpresaIdentificadorFiscal(String identificadorFiscal) {
        reservaRepository.deleteByEmpresaIdentificadorFiscal(identificadorFiscal);
    }

    /**
     * Cuenta el número de reservas para un servicio específico en una fecha
     * determinada.
     *
     * @param servicioId el identificador del servicio.
     * @param fechaReserva la fecha de la reserva en formato String.
     * @return el número de reservas existentes para ese servicio en esa fecha.
     */
    @Override
    public int countReservasByServicioIdAndFecha(Long servicioId, String fechaReserva) {
        return reservaRepository.countByServicioIdAndFechaReserva(servicioId, fechaReserva);
    }
}
