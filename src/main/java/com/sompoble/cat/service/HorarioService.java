package com.sompoble.cat.service;

import com.sompoble.cat.domain.Horario;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HorarioService {

   
    Horario save(Horario horario);
    
    void deleteById(Long id);
    
    Optional<Horario> findById(Long id);
    
    List<Horario> findAll();

   
    List<Horario> findByEmpresa_IdEmpresa(Long idEmpresa);

    // Búsqueda por días laborables
    List<Horario> findByDiasLaborablesContainingIgnoreCase(String dia);

    // Búsqueda por horario de inicio entre dos horas
    List<Horario> findByHorarioInicioBetween(LocalTime inicio, LocalTime fin);

    // Búsqueda por horario de fin entre dos horas
    List<Horario> findByHorarioFinBetween(LocalTime inicio, LocalTime fin);

    // Búsqueda por Empresa y días laborables
    List<Horario> findByEmpresa_IdEmpresaAndDiasLaborablesContaining(Long idEmpresa, String dia);

    // Horarios que empiecen antes de una hora
    List<Horario> findByHorarioInicioBefore(LocalTime hora);

    // Horarios que terminen después de una hora
    List<Horario> findByHorarioFinAfter(LocalTime hora);

    // Ordenar por horario de inicio ascendente
    List<Horario> findByOrderByHorarioInicioAsc();

    // Horarios de una Empresa con horario de inicio en intervalo
    List<Horario> findByEmpresa_IdEmpresaAndHorarioInicioBetween(Long idEmpresa, LocalTime inicio, LocalTime fin);

    // Horarios de una Empresa con horario de fin en intervalo
    List<Horario> findByEmpresa_IdEmpresaAndHorarioFinBetween(Long idEmpresa, LocalTime inicio, LocalTime fin);

    // Horarios que incluyan un día específico
    List<Horario> findByDiaExacto(String dia);

    // Horarios de una Empresa con horario entre X e Y
    List<Horario> findByEmpresaAndHorarioBetween(Long idEmpresa, LocalTime inicio, LocalTime fin);

    // Horarios que se superpongan entre dos horas
    List<Horario> findByHorarioOverlap(LocalTime inicio, LocalTime fin);

    // Horarios de Empresa con día, inicio y fin exactos
    Horario findByEmpresaDiaYHorarioExacto(Long idEmpresa, String dia, LocalTime inicio, LocalTime fin);

    // Horarios huérfanos (sin Empresa asociada)
    List<Horario> findOrphanHorarios();

    // Contar horarios por Empresa
    Long countByEmpresaId(Long idEmpresa);

    // Horarios que incluyan múltiples días
    List<Horario> findByDiasMultiples(String dia1, String dia2);

    // Horarios con rangos de inicio y fin en dos intervalos
    List<Horario> findByHorarioRanges(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2);

    // Búsqueda por fecha de alta entre dos fechas
    List<Horario> findByFechaAltaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Horarios modificados después de una fecha
    List<Horario> findByFechaModificacionAfter(LocalDateTime fecha);
}