package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.PanelMetricasDTO.MetricasMensualesDTO;
import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.repository.ReservaRepository;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.service.ServicioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de {@link ReservaRepository} utilizando Hibernate con Criteria
 * API.
 * <p>
 * Proporciona operaciones CRUD para gestionar reservas en la base de datos.
 * </p>
 *
 * @author SomPoble
 */
@Repository
@Transactional
public class ReservaHibernate implements ReservaRepository {

    /**
     * EntityManager para gestionar las operaciones de persistencia.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Servicio para gestionar clientes.
     */
    @Autowired
    private ClienteService clienteService;

    /**
     * Servicio para gestionar empresas.
     */
    @Autowired
    private EmpresaService empresaService;

    /**
     * Servicio para gestionar servicios.
     */
    @Autowired
    private ServicioService servicioService;

    /**
     * Obtiene todas las reservas asociadas a un cliente mediante su DNI y
     * devuelve una lista de DTOs.
     *
     * @param dni el documento nacional de identidad del cliente.
     * @return una lista de DTOs de reservas realizadas por el cliente con el
     * DNI especificado.
     */
    @Override
    public List<ReservaDTO> findByClienteDni(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> cq = cb.createQuery(Reserva.class);
        Root<Reserva> root = cq.from(Reserva.class);

        Predicate dniPredicate = cb.equal(root.get("cliente").get("dni"), dni);
        cq.where(dniPredicate);

        List<Reserva> reservas = entityManager.createQuery(cq).getResultList();

        return reservas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Obtiene todas las reservas asociadas a una empresa o autónomo mediante su
     * identificador fiscal y devuelve una lista de DTOs.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa o
     * autónomo.
     * @return una lista de DTOs de reservas asociadas a la empresa
     * especificada.
     */
    @Override
    public List<ReservaDTO> findByEmpresaIdentificadorFiscal(String identificadorFiscal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> cq = cb.createQuery(Reserva.class);
        Root<Reserva> root = cq.from(Reserva.class);

        Predicate identificadorFiscalPredicate = cb.equal(root.get("empresa").get("identificadorFiscal"), identificadorFiscal);
        cq.where(identificadorFiscalPredicate);

        List<Reserva> reservas = entityManager.createQuery(cq).getResultList();

        return reservas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Busca una reserva por su identificador único y devuelve un DTO.
     *
     * @param id el identificador de la reserva.
     * @return un DTO con la reserva correspondiente, o {@code null} si no se
     * encuentra.
     */
    @Override
    public ReservaDTO findById(Long id) {
        Reserva reserva = entityManager.find(Reserva.class, id);
        return reserva != null ? convertToDTO(reserva) : null;
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
        Reserva reserva = entityManager.find(Reserva.class, id);
        return reserva;
    }

    /**
     * Guarda una nueva reserva en la base de datos.
     *
     * @param reserva la reserva a guardar.
     */
    @Override
    public void addReserva(Reserva reserva) {
        entityManager.persist(reserva); // Si la reserva no tiene id, la persiste.
    }

    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param reserva Objeto {@link Reserva} con la información actualizada.
     */
    @Override
    public void updateReserva(Reserva reserva) {
        entityManager.merge(reserva);
    }

    /**
     * Elimina una reserva de la base de datos mediante su identificador.
     *
     * @param id el id de la reserva a eliminar.
     */
    @Override
    public void deleteById(Long id) {
        Reserva reserva = entityManager.find(Reserva.class, id);
        if (reserva != null) {
            entityManager.remove(reserva);
        }
    }

    /**
     * Elimina todas las reservas asociadas a un cliente mediante su DNI.
     *
     * @param dni el documento nacional de identidad del cliente.
     */
    @Override
    public void deleteByClienteDni(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> cq = cb.createQuery(Reserva.class);
        Root<Reserva> root = cq.from(Reserva.class);

        Predicate dniPredicate = cb.equal(root.get("cliente").get("dni"), dni);
        cq.where(dniPredicate);

        List<Reserva> reservas = entityManager.createQuery(cq).getResultList();
        for (Reserva reserva : reservas) {
            entityManager.remove(reserva);
        }
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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> cq = cb.createQuery(Reserva.class);
        Root<Reserva> root = cq.from(Reserva.class);

        Predicate identificadorFiscalPredicate = cb.equal(root.get("empresa").get("identificadorFiscal"), identificadorFiscal);
        cq.where(identificadorFiscalPredicate);

        List<Reserva> reservas = entityManager.createQuery(cq).getResultList();
        for (Reserva reserva : reservas) {
            entityManager.remove(reserva);
        }
    }

    /**
     * Elimina todas las reservas asociadas a un servicio mediante su ID.
     *
     * @param servicioId el identificador del servicio.
     */
    @Override
    public void deleteByServicioId(Long servicioId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> cq = cb.createQuery(Reserva.class);
        Root<Reserva> root = cq.from(Reserva.class);

        Predicate servicioIdPredicate = cb.equal(root.get("servicio").get("idServicio"), servicioId);
        cq.where(servicioIdPredicate);

        List<Reserva> reservas = entityManager.createQuery(cq).getResultList();
        for (Reserva reserva : reservas) {
            entityManager.remove(reserva);
        }
    }

    /**
     * Convierte una entidad Reserva a un DTO ReservaDTO.
     *
     * @param reserva la entidad Reserva.
     * @return el DTO ReservaDTO.
     */
    private ReservaDTO convertToDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO(
                reserva.getIdReserva(),
                reserva.getFechaReserva(),
                reserva.getHora(),
                reserva.getEstado(),
                reserva.getCliente().getDni(),
                reserva.getEmpresa().getIdentificadorFiscal(),
                reserva.getServicio().getIdServicio()
        );

        dto.setNombreServicio(reserva.getServicio().getNombre());

        return dto;
    }

    /**
     * Convierte un objeto {@link ReservaDTO} a una entidad {@link Reserva}.
     * <p>
     * Este método crea una nueva entidad Reserva y la completa con la
     * información contenida en el DTO proporcionado, incluyendo las relaciones
     * con Cliente, Empresa y Servicio.
     * </p>
     *
     * @param reservaDTO El DTO que contiene la información de la reserva.
     * @return Una entidad Reserva con la información del DTO.
     */
    public Reserva convertToEntity(ReservaDTO reservaDTO) {
        Reserva reserva = new Reserva();

        reserva.setIdReserva(reservaDTO.getIdReserva());
        reserva.setFechaReserva(reservaDTO.getFechaReserva());
        reserva.setHora(reservaDTO.getHora());
        reserva.setEstado(reservaDTO.getEstado());

        String dniCliente = reservaDTO.getDniCliente();
        String identificadorFiscal = reservaDTO.getIdentificadorFiscalEmpresa();
        Long idServicio = reservaDTO.getIdServicio();

        Cliente cliente = clienteService.findByDniFull(dniCliente);
        reserva.setCliente(cliente);

        Empresa empresa = empresaService.findByIdentificadorFiscalFull(identificadorFiscal);
        reserva.setEmpresa(empresa);

        Servicio servicio = servicioService.obtenerPorId(idServicio);
        reserva.setServicio(servicio);

        return reserva;
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
    public int countByServicioIdAndFechaReserva(Long servicioId, String fechaReserva) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Reserva> root = cq.from(Reserva.class);

        Predicate servicioIdPredicate = cb.equal(root.get("servicio").get("idServicio"), servicioId);
        Predicate fechaPredicate = cb.equal(root.get("fechaReserva"), fechaReserva);

        cq.select(cb.count(root)).where(cb.and(servicioIdPredicate, fechaPredicate));

        return entityManager.createQuery(cq).getSingleResult().intValue();
    }

    /**
     * Obtiene todas las reservas próximas a ocurrir dentro de un rango de
     * tiempo específico.
     * <p>
     * Este método busca reservas cuya fecha y hora de inicio estén entre la
     * fecha actual y las próximas 24 horas. Se utiliza para identificar
     * reservas que requieren recordatorios automáticos.
     * </p>
     *
     * @return una lista de objetos {@link Reserva} que representan las reservas
     * próximas. Si no hay reservas próximas, la lista estará vacía.
     */
    @Override
    public List<Reserva> findUpcomingReservations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> cq = cb.createQuery(Reserva.class);
        Root<Reserva> root = cq.from(Reserva.class);

        // Predicado sobre fechaAlta
        Predicate recent = cb.between(
                root.get("fechaAlta"),
                cb.literal(now),
                cb.literal(tomorrow)
        );

        cq.where(recent);
        return entityManager.createQuery(cq).getResultList();
    }

    //-------------------------------------------------------------------------------------------//
    //--------------------PARTE DE MÉTRICAS------------------------------------------------------//
    /**
     * Cuenta la cantidad de clientes únicos que realizaron reservas en una
     * empresa durante un período determinado.
     *
     * @param empresaId ID único de la empresa.
     * @param inicio Fecha de inicio del período (inclusive).
     * @param fin Fecha de fin del período (inclusive).
     * @return Número de clientes únicos como {@code Integer}.
     */
    @Override
    public Integer contarClientesUnicos(Long empresaId, LocalDate inicio, LocalDate fin) {
        return entityManager.createQuery("""
                    SELECT COUNT(DISTINCT r.cliente.idPersona) FROM Reserva r
                    WHERE r.servicio.empresa.idEmpresa = :empresaId
                      AND r.fechaReserva BETWEEN :inicio AND :fin
                """, Long.class)
                .setParameter("empresaId", empresaId)
                .setParameter("inicio", inicio.toString())
                .setParameter("fin", fin.toString())
                .getSingleResult()
                .intValue();
    }

    /**
     * Cuenta el número total de reservas realizadas para una empresa dentro de
     * un período de fechas.
     *
     * @param empresaId ID único de la empresa.
     * @param inicio Fecha de inicio del período (inclusive).
     * @param fin Fecha de fin del período (inclusive).
     * @return Número total de reservas realizadas.
     */
    @Override
    public Long contarReservasPorEmpresaYFechas(Long empresaId, LocalDate inicio, LocalDate fin) {
        return entityManager.createQuery("""
                    SELECT COUNT(r) FROM Reserva r
                    WHERE r.servicio.empresa.idEmpresa = :empresaId
                      AND r.fechaReserva BETWEEN :inicio AND :fin
                """, Long.class)
                .setParameter("empresaId", empresaId)
                .setParameter("inicio", inicio.toString())
                .setParameter("fin", fin.toString())
                .getSingleResult();
    }

    /**
     * Suma los ingresos generados por las reservas de una empresa en un período
     * determinado.
     *
     * @param empresaId ID único de la empresa.
     * @param inicio Fecha de inicio del período (inclusive).
     * @param fin Fecha de fin del período (inclusive).
     * @return Total de ingresos como {@link BigDecimal}.
     */
    @Override
    public Double sumarIngresosPorEmpresaYFechas(Long empresaId, LocalDate inicio, LocalDate fin) {
        return entityManager.createQuery("""
                    SELECT SUM(r.servicio.precio) FROM Reserva r
                    WHERE r.servicio.empresa.idEmpresa = :empresaId
                      AND r.fechaReserva BETWEEN :inicio AND :fin
                """, Double.class)
                .setParameter("empresaId", empresaId)
                .setParameter("inicio", inicio.toString())
                .setParameter("fin", fin.toString())
                .getSingleResult();
    }

    //IMPORTANTE: metodo comentado para futura implementacion. Inlcuido en TEA6 como mejora de la plataforma.
    /**
     * Obtiene un resumen de métricas por servicio, agrupado por nombre del
     * servicio. Incluye la cantidad de reservas y el total de ingresos por cada
     * servicio.
     *
     * @param empresaId ID único de la empresa.
     * @param inicio Fecha de inicio del período (inclusive).
     * @param fin Fecha de fin del período (inclusive).
     * @return Lista de {@link ServicioResumenDTO} con métricas por servicio.
     */
    /*
    @Override
    public List<ServicioResumenDTO> obtenerMetricasPorServicio(Long empresaId, LocalDate inicio, LocalDate fin) {
        return entityManager.createQuery("""
                    SELECT new com.sompoble.cat.dto.PanelMetricasDTO$ServicioResumenDTO(
                        r.servicio.nombre, COUNT(r), SUM(r.servicio.precio))
                    FROM Reserva r
                    WHERE r.servicio.empresa.idEmpresa = :empresaId
                      AND r.fechaReserva BETWEEN :inicio AND :fin
                    GROUP BY r.servicio.nombre
                """, ServicioResumenDTO.class)
                .setParameter("empresaId", empresaId)
                .setParameter("inicio", inicio.toString())
                .setParameter("fin", fin.toString())
                .getResultList();
    }
     */
    /**
     * Obtiene métricas agregadas por mes (nombre del mes), incluyendo número
     * total de reservas y suma de ingresos por mes para una empresa.
     *
     * Nota: El campo {@code fechaReserva} debe estar en formato ISO
     * (yyyy-MM-dd) para que la función {@code to_char} lo procese
     * correctamente.
     *
     * @param empresaId ID único de la empresa.
     * @param inicio Fecha de inicio del período (inclusive).
     * @param fin Fecha de fin del período (inclusive).
     * @return Lista de {@link MetricasMensualesDTO} con las métricas mensuales.
     */
    @Override
    public List<MetricasMensualesDTO> obtenerMetricasMensuales(Long empresaId, LocalDate inicio, LocalDate fin) {
        List<Object[]> resultados = entityManager.createQuery("""
            SELECT
                FUNCTION('DATE_FORMAT', FUNCTION('STR_TO_DATE', r.fechaReserva, '%Y-%m-%d'), '%M'),
                COUNT(r),
                SUM(r.servicio.precio)
            FROM Reserva r
            WHERE r.servicio.empresa.idEmpresa = :empresaId
              AND r.fechaReserva BETWEEN :inicio AND :fin
            GROUP BY FUNCTION('DATE_FORMAT', FUNCTION('STR_TO_DATE', r.fechaReserva, '%Y-%m-%d'), '%M'), 
                     FUNCTION('MONTH', FUNCTION('STR_TO_DATE', r.fechaReserva, '%Y-%m-%d'))
            ORDER BY FUNCTION('MONTH', FUNCTION('STR_TO_DATE', r.fechaReserva, '%Y-%m-%d'))
        """, Object[].class)
                .setParameter("empresaId", empresaId)
                .setParameter("inicio", inicio.toString())
                .setParameter("fin", fin.toString())
                .getResultList();

        List<MetricasMensualesDTO> dtos = new ArrayList<>();
        for (Object[] resultado : resultados) {
            String mes = (String) resultado[0];
            Long reservas = ((Number) resultado[1]).longValue();
            BigDecimal ingresos = resultado[2] != null
                    ? new BigDecimal(resultado[2].toString())
                    : BigDecimal.ZERO;

            dtos.add(new MetricasMensualesDTO(mes, reservas, ingresos));
        }

        return dtos;
    }
}
