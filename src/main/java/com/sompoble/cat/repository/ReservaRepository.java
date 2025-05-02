package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.dto.PanelMetricasDTO.MetricasMensualesDTO;
import com.sompoble.cat.dto.PanelMetricasDTO.ServicioResumenDTO;
import com.sompoble.cat.dto.ReservaDTO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    
    /**
     * Obtiene las reservas dadas de alta en las últimas 24 horas.
     */
    @Query("SELECT r FROM Reserva r WHERE r.fechaAlta BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + 1")
    List<Reserva> findUpcomingReservations();
    /**
     * Cuenta el número total de reservas realizadas para una empresa en un rango de fechas determinado.
     *
     * @param empresaId ID de la empresa.
     * @param inicio Fecha de inicio del rango.
     * @param fin Fecha de fin del rango.
     * @return Total de reservas realizadas.
     */
    
    @Query("""
    	    SELECT COUNT(r) FROM Reserva r
    	    WHERE r.servicio.empresa.idEmpresa = :empresaId
    	      AND r.fechaReserva BETWEEN :inicio AND :fin
    	""")
    	Long contarReservasPorEmpresaYFechas(@Param("empresaId") Long empresaId,
    	                                     @Param("inicio") LocalDate inicio,
    	                                     @Param("fin") LocalDate fin);
    /**
     * Suma el total de ingresos generados por las reservas de una empresa en un rango de fechas determinado.
     *
     * @param empresaId ID de la empresa.
     * @param inicio Fecha de inicio del rango.
     * @param fin Fecha de fin del rango.
     * @return Suma total de ingresos.
     */
    @Query("""
    	    SELECT SUM(r.servicio.precio) FROM Reserva r
    	    WHERE r.servicio.empresa.idEmpresa = :empresaId
    	      AND r.fechaReserva BETWEEN :inicio AND :fin
    	""")
    	BigDecimal sumarIngresosPorEmpresaYFechas(@Param("empresaId") Long empresaId,
    	                                           @Param("inicio") LocalDate inicio,
    	                                           @Param("fin") LocalDate fin);
        /**
         * Cuenta el número de clientes únicos que han realizado reservas para una empresa en un rango de fechas.
         *
         * @param empresaId ID de la empresa.
         * @param inicio Fecha de inicio del rango.
         * @param fin Fecha de fin del rango.
         * @return Total de clientes únicos.
         */
    @Query("""
    	    SELECT COUNT(DISTINCT r.cliente.idCliente) FROM Reserva r
    	    WHERE r.servicio.empresa.idEmpresa = :empresaId
    	      AND r.fechaReserva BETWEEN :inicio AND :fin
    	""")
    	Integer contarClientesUnicos(@Param("empresaId") Long empresaId,
    	                             @Param("inicio") LocalDate inicio,
    	                             @Param("fin") LocalDate fin);
        /**
         * Obtiene un resumen de métricas agrupadas por servicio, incluyendo el número de reservas
         * y el ingreso total por servicio para una empresa y rango de fechas.
         *
         * @param empresaId ID de la empresa.
         * @param inicio Fecha de inicio del rango.
         * @param fin Fecha de fin del rango.
         * @return Lista de {@link com.sompoble.cat.dto.PanelMetricasDTO.ServicioResumenDTO}.
         */
    @Query("""
    	    SELECT new com.sompoble.cat.dto.PanelMetricasDTO$ServicioResumenDTO(
    	        r.servicio.nombre, COUNT(r), SUM(r.servicio.precio))
    	    FROM Reserva r
    	    WHERE r.servicio.empresa.idEmpresa = :empresaId
    	      AND r.fechaReserva BETWEEN :inicio AND :fin
    	    GROUP BY r.servicio.nombre
    	""")
    	List<ServicioResumenDTO> obtenerMetricasPorServicio(@Param("empresaId") Long empresaId,
    	                                                     @Param("inicio") LocalDate inicio,
    	                                                     @Param("fin") LocalDate fin);
        /**
         * Obtiene las métricas mensuales de reservas e ingresos para una empresa,
         * agrupadas por nombre del mes.
         *
         * @param empresaId ID de la empresa.
         * @param inicio Fecha de inicio del rango.
         * @param fin Fecha de fin del rango.
         * @return Lista de {@link com.sompoble.cat.dto.PanelMetricasDTO.MetricasMensualesDTO}.
         */
    @Query("""
    	    SELECT new com.sompoble.cat.dto.PanelMetricasDTO$MetricasMensualesDTO(
    	        FUNCTION('to_char', r.fechaReserva, 'Month'),
    	        COUNT(r),
    	        SUM(r.servicio.precio))
    	    FROM Reserva r
    	    WHERE r.servicio.empresa.idEmpresa = :empresaId
    	      AND r.fechaReserva BETWEEN :inicio AND :fin
    	    GROUP BY FUNCTION('to_char', r.fechaReserva, 'Month')
    	    ORDER BY FUNCTION('to_char', r.fechaReserva, 'Month')
    	""")
    	List<MetricasMensualesDTO> obtenerMetricasMensuales(@Param("empresaId") Long empresaId,
    	                                                     @Param("inicio") LocalDate inicio,
    	                                                     @Param("fin") LocalDate fin);
    }
    