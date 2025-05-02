package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Horario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@code Horario}.
 * <p>
 * Proporciona métodos para gestionar horarios en la base de datos, incluyendo
 * operaciones de búsqueda personalizadas mediante queries JPQL.
 * </p>
 */
@Repository
public interface HorarioRepository {

    /**
     * Elimina un horario por su ID.
     *
     * @param id ID del horario a eliminar.
     */
    void delete(Long id);

    /**
     * Obtiene un horario por su ID.
     *
     * @param id ID del horario.
     * @return El horario encontrado o {@code null} si no existe.
     */
    Horario findById(Long id);

    /**
     * Lista todos los horarios almacenados.
     *
     * @return Lista de horarios.
     */
    List<Horario> findAll();

    /**
     * Guarda un nuevo horario o actualiza uno existente.
     *
     * @param horario Objeto {@link Horario} a persistir.
     * @return El horario guardado con su ID asignado.
     */
    Horario save(Horario horario);

    /**
     * Busca horarios por el ID de la empresa asociada.
     *
     * @param idEmpresa ID de la empresa.
     * @return Lista de horarios de la empresa especificada.
     */
    List<Horario> findByEmpresa_IdEmpresa(Long idEmpresa);

    /**
     * Busca horarios que incluyan un día específico en sus días laborables.
     *
     * @param dia Día de la semana a buscar.
     * @return Lista de horarios que incluyen el día especificado.
     */
    List<Horario> findByDiasLaborablesContainingIgnoreCase(String dia);

    /**
     * Busca horarios cuya hora de inicio esté dentro de un rango específico.
     *
     * @param inicio Límite inferior del rango de tiempo.
     * @param fin Límite superior del rango de tiempo.
     * @return Lista de horarios que cumplen con la condición.
     */
    List<Horario> findByHorarioInicioBetween(LocalTime inicio, LocalTime fin);

    /**
     * Busca horarios cuya hora de fin esté dentro de un rango específico.
     *
     * @param inicio Límite inferior del rango de tiempo.
     * @param fin Límite superior del rango de tiempo.
     * @return Lista de horarios que cumplen con la condición.
     */
    List<Horario> findByHorarioFinBetween(LocalTime inicio, LocalTime fin);

    /**
     * Busca horarios por ID de empresa y día laborable específico.
     *
     * @param idEmpresa ID de la empresa.
     * @param dia Día de la semana a buscar.
     * @return Lista de horarios que cumplen con ambas condiciones.
     */
    List<Horario> findByEmpresa_IdEmpresaAndDiasLaborablesContaining(
            Long idEmpresa, String dia);

    /**
     * Busca horarios que comienzan antes de una hora específica.
     *
     * @param hora Hora límite.
     * @return Lista de horarios que comienzan antes de la hora especificada.
     */
    List<Horario> findByHorarioInicioBefore(LocalTime hora);

    /**
     * Busca horarios que terminan después de una hora específica.
     *
     * @param hora Hora límite.
     * @return Lista de horarios que terminan después de la hora especificada.
     */
    List<Horario> findByHorarioFinAfter(LocalTime hora);

    /**
     * Busca todos los horarios ordenados por hora de inicio ascendente.
     *
     * @return Lista de horarios ordenados.
     */
    List<Horario> findByOrderByHorarioInicioAsc();

    /**
     * Busca horarios por ID de empresa y rango de hora de inicio.
     *
     * @param idEmpresa ID de la empresa.
     * @param inicio Límite inferior del rango de tiempo.
     * @param fin Límite superior del rango de tiempo.
     * @return Lista de horarios que cumplen con las condiciones.
     */
    List<Horario> findByEmpresa_IdEmpresaAndHorarioInicioBetween(
            Long idEmpresa, LocalTime inicio, LocalTime fin);

    /**
     * Busca horarios por ID de empresa y rango de hora de fin.
     *
     * @param idEmpresa ID de la empresa.
     * @param inicio Límite inferior del rango de tiempo.
     * @param fin Límite superior del rango de tiempo.
     * @return Lista de horarios que cumplen con las condiciones.
     */
    List<Horario> findByEmpresa_IdEmpresaAndHorarioFinBetween(
            Long idEmpresa, LocalTime inicio, LocalTime fin);

    /**
     * Busca horarios que incluyan un día específico.
     *
     * @param dia Día de la semana a buscar.
     * @return Lista de horarios que incluyen el día especificado.
     */
    @Query("SELECT h FROM Horario h WHERE h.diasLaborables LIKE %:dia%")
    List<Horario> findByDiaExacto(@Param("dia") String dia);

    /**
     * Busca horarios de una empresa con horario entre horarioInicio y
     * horarioFin.
     *
     * @param idEmpresa ID de la empresa.
     * @param inicio Hora de inicio del rango.
     * @param fin Hora de fin del rango.
     * @return Lista de horarios que cumplen con las condiciones.
     */
    @Query("SELECT h FROM Horario h "
            + "WHERE h.empresa.idEmpresa = :idEmpresa "
            + "AND h.horarioInicio >= :inicio "
            + "AND h.horarioFin <= :fin")
    List<Horario> findByEmpresaAndHorarioBetween(
            @Param("idEmpresa") Long idEmpresa,
            @Param("inicio") LocalTime inicio,
            @Param("fin") LocalTime fin);

    /**
     * Busca horarios que empiecen antes de una hora y terminen después de otra.
     *
     * @param inicio Hora de inicio para la comparación.
     * @param fin Hora de fin para la comparación.
     * @return Lista de horarios que se solapan con el rango proporcionado.
     */
    @Query("SELECT h FROM Horario h "
            + "WHERE h.horarioInicio < :inicio "
            + "AND h.horarioFin > :fin")
    List<Horario> findByHorarioOverlap(
            @Param("inicio") LocalTime inicio,
            @Param("fin") LocalTime fin);

    /**
     * Busca un horario específico por empresa, día y rango de horas exacto.
     *
     * @param idEmpresa ID de la empresa.
     * @param dia Día específico.
     * @param inicio Hora de inicio exacta.
     * @param fin Hora de fin exacta.
     * @return El horario que cumple con todas las condiciones.
     */
    @Query("SELECT h FROM Horario h "
            + "WHERE h.empresa.idEmpresa = :idEmpresa "
            + "AND h.diasLaborables LIKE %:dia% "
            + "AND h.horarioInicio = :inicio "
            + "AND h.horarioFin = :fin")
    Horario findByEmpresaDiaYHorarioExacto(
            @Param("idEmpresa") Long idEmpresa,
            @Param("dia") String dia,
            @Param("inicio") LocalTime inicio,
            @Param("fin") LocalTime fin);

    /**
     * Busca horarios que no estén asociados a una empresa.
     *
     * @return Lista de horarios sin empresa asociada.
     */
    @Query("SELECT h FROM Horario h WHERE h.empresa IS NULL")
    List<Horario> findOrphanHorarios();

    /**
     * Cuenta el número de horarios asociados a una empresa específica.
     *
     * @param idEmpresa ID de la empresa.
     * @return Número de horarios de la empresa.
     */
    @Query("SELECT COUNT(h) FROM Horario h WHERE h.empresa.idEmpresa = :idEmpresa")
    Long countByEmpresaId(@Param("idEmpresa") Long idEmpresa);

    /**
     * Busca horarios que contengan múltiples días específicos.
     *
     * @param dia1 Primer día a incluir.
     * @param dia2 Segundo día a incluir.
     * @return Lista de horarios que incluyen ambos días.
     */
    @Query("SELECT h FROM Horario h "
            + "WHERE h.diasLaborables LIKE %:dia1% "
            + "AND h.diasLaborables LIKE %:dia2%")
    List<Horario> findByDiasMultiples(
            @Param("dia1") String dia1,
            @Param("dia2") String dia2);

    /**
     * Busca horarios en intervalos de fechas específicos.
     *
     * @param inicio1 Límite inferior del rango para la hora de inicio.
     * @param fin1 Límite superior del rango para la hora de inicio.
     * @param inicio2 Límite inferior del rango para la hora de fin.
     * @param fin2 Límite superior del rango para la hora de fin.
     * @return Lista de horarios que cumplen con las condiciones de rango.
     */
    @Query("SELECT h FROM Horario h "
            + "WHERE h.horarioInicio BETWEEN :inicio1 AND :fin1 "
            + "AND h.horarioFin BETWEEN :inicio2 AND :fin2")
    List<Horario> findByHorarioRanges(
            @Param("inicio1") LocalTime inicio1,
            @Param("fin1") LocalTime fin1,
            @Param("inicio2") LocalTime inicio2,
            @Param("fin2") LocalTime fin2);

    /**
     * Busca horarios con fecha de alta en un rango específico.
     *
     * @param fechaInicio Fecha inicial del rango.
     * @param fechaFin Fecha final del rango.
     * @return Lista de horarios creados dentro del rango de fechas.
     */
    @Query("SELECT h FROM Horario h "
            + "WHERE h.fechaAlta BETWEEN :fechaInicio AND :fechaFin")
    List<Horario> findByFechaAltaBetween(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Busca horarios con fecha de modificación posterior a una fecha
     * específica.
     *
     * @param fecha Fecha límite.
     * @return Lista de horarios modificados después de la fecha especificada.
     */
    @Query("SELECT h FROM Horario h "
            + "WHERE h.fechaModificacion >= :fecha")
    List<Horario> findByFechaModificacionAfter(
            @Param("fecha") LocalDateTime fecha);

    /**
     * Busca un horario por el ID del servicio asociado.
     *
     * @param idServicio ID del servicio.
     * @return Un Optional que contiene el horario si existe.
     */
    @Query("SELECT h FROM Horario h WHERE h.servicio.idServicio = :idServicio")
    Optional<Horario> findByServicio_IdServicio(@Param("idServicio") Long idServicio);

    /**
     * Busca horarios por el identificador fiscal de la empresa asociada al
     * servicio.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return Lista de horarios asociados a la empresa con el identificador
     * fiscal especificado.
     */
    @Query("SELECT h FROM Horario h "
            + "JOIN h.servicio s "
            + "JOIN s.empresa e "
            + "WHERE e.identificadorFiscal = :identificadorFiscal")
    List<Horario> findByServicio_Empresa_IdentificadorFiscal(
            @Param("identificadorFiscal") String identificadorFiscal);

    /**
     * Busca un horario por el ID de la empresa y el ID del servicio.
     *
     * @param idEmpresa ID de la empresa.
     * @param idServicio ID del servicio.
     * @return Un Optional que contiene el horario si existe.
     */
    Optional<Horario> findByEmpresaIdAndServicioId(Long idEmpresa, Long idServicio);

    /**
     * Busca el primer horario asociado a un servicio específico.
     *
     * @param servicioId ID del servicio.
     * @return Un Optional que contiene el primer horario encontrado si existe.
     */
    Optional<Horario> findFirstByServicioId(Long servicioId);
}
