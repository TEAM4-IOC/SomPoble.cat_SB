package com.sompoble.cat.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sompoble.cat.domain.Horario;


@Repository
public interface HorarioRepository {


	void delete(Long id);

	Horario findById(Long id);

	List<Horario> findAll();

	Horario save(Horario horario);


    List<Horario> findByEmpresa_IdEmpresa(Long idEmpresa);


    List<Horario> findByDiasLaborablesContainingIgnoreCase(String dia);


    List<Horario> findByHorarioInicioBetween(LocalTime inicio, LocalTime fin);


    List<Horario> findByHorarioFinBetween(LocalTime inicio, LocalTime fin);


    List<Horario> findByEmpresa_IdEmpresaAndDiasLaborablesContaining(
        Long idEmpresa, String dia);


    List<Horario> findByHorarioInicioBefore(LocalTime hora);


    List<Horario> findByHorarioFinAfter(LocalTime hora);


    List<Horario> findByOrderByHorarioInicioAsc();


    List<Horario> findByEmpresa_IdEmpresaAndHorarioInicioBetween(
        Long idEmpresa, LocalTime inicio, LocalTime fin);


    List<Horario> findByEmpresa_IdEmpresaAndHorarioFinBetween(
        Long idEmpresa, LocalTime inicio, LocalTime fin);



    // Buscar horarios que incluyan un día específico
    @Query("SELECT h FROM Horario h WHERE h.diasLaborables LIKE %:dia%")
    List<Horario> findByDiaExacto(@Param("dia") String dia);

    //  Buscar horarios de una Empresa con horario entre horarioInicio y horarioFin
    @Query("SELECT h FROM Horario h " +
           "WHERE h.empresa.idEmpresa = :idEmpresa " +
           "AND h.horarioInicio >= :inicio " +
           "AND h.horarioFin <= :fin")
    List<Horario> findByEmpresaAndHorarioBetween(
        @Param("idEmpresa") Long idEmpresa,
        @Param("inicio") LocalTime inicio,
        @Param("fin") LocalTime fin);

    //  Buscar horarios que empiecen antes de una hora y terminen después
    @Query("SELECT h FROM Horario h " +
           "WHERE h.horarioInicio < :inicio " +
           "AND h.horarioFin > :fin")
    List<Horario> findByHorarioOverlap(
        @Param("inicio") LocalTime inicio,
        @Param("fin") LocalTime fin);

    //  Buscar horarios de una Empresa con días laborables y horario específico
    @Query("SELECT h FROM Horario h " +
           "WHERE h.empresa.idEmpresa = :idEmpresa " +
           "AND h.diasLaborables LIKE %:dia% " +
           "AND h.horarioInicio = :inicio " +
           "AND h.horarioFin = :fin")
    Horario findByEmpresaDiaYHorarioExacto(
        @Param("idEmpresa") Long idEmpresa,
        @Param("dia") String dia,
        @Param("inicio") LocalTime inicio,
        @Param("fin") LocalTime fin);

    // Buscar horarios que no estén asociados a una Empresa
    @Query("SELECT h FROM Horario h WHERE h.empresa IS NULL")
    List<Horario> findOrphanHorarios();

    // Contar horarios por Empresa
    @Query("SELECT COUNT(h) FROM Horario h WHERE h.empresa.idEmpresa = :idEmpresa")
    Long countByEmpresaId(@Param("idEmpresa") Long idEmpresa);

    //  Buscar horarios que contengan múltiples días (ej: "Lunes,Martes")
    @Query("SELECT h FROM Horario h " +
           "WHERE h.diasLaborables LIKE %:dia1% " +
           "AND h.diasLaborables LIKE %:dia2%")
    List<Horario> findByDiasMultiples(
        @Param("dia1") String dia1,
        @Param("dia2") String dia2);

    //  Buscar horarios en intervalos de fechas
    @Query("SELECT h FROM Horario h " +
           "WHERE h.horarioInicio BETWEEN :inicio1 AND :fin1 " +
           "AND h.horarioFin BETWEEN :inicio2 AND :fin2")
    List<Horario> findByHorarioRanges(
        @Param("inicio1") LocalTime inicio1,
        @Param("fin1") LocalTime fin1,
        @Param("inicio2") LocalTime inicio2,
        @Param("fin2") LocalTime fin2);

    //  Buscar horarios con fecha de alta en un rango
    @Query("SELECT h FROM Horario h " +
           "WHERE h.fechaAlta BETWEEN :fechaInicio AND :fechaFin")
    List<Horario> findByFechaAltaBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin);

    //  Buscar horarios con fecha de modificación más reciente
    @Query("SELECT h FROM Horario h " +
           "WHERE h.fechaModificacion >= :fecha")
    List<Horario> findByFechaModificacionAfter(
        @Param("fecha") LocalDateTime fecha);


 // Buscar horarios por idServicio
    @Query("SELECT h FROM Horario h WHERE h.servicio.idServicio = :idServicio")
    Optional<Horario> findByServicio_IdServicio(@Param("idServicio") Long idServicio);

    // Buscar horarios por identificadorFiscal de la empresa
    @Query("SELECT h FROM Horario h " +
           "JOIN h.servicio s " +
           "JOIN s.empresa e " +
           "WHERE e.identificadorFiscal = :identificadorFiscal")
    List<Horario> findByServicio_Empresa_IdentificadorFiscal(
        @Param("identificadorFiscal") String identificadorFiscal);

    Optional<Horario> findByEmpresaIdAndServicioId(Long idEmpresa, Long idServicio);












}