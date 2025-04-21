package com.sompoble.cat.service;

import com.sompoble.cat.domain.Horario;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que define las operaciones para gestionar horarios.
 * <p>
 * Esta interfaz proporciona métodos para realizar operaciones CRUD sobre horarios,
 * así como búsquedas y filtros por diferentes criterios como empresa, días laborables,
 * rangos de horas, etc.
 * </p>
 */
public interface HorarioService {
   
    /**
     * Guarda un nuevo horario o actualiza uno existente.
     *
     * @param horario Objeto {@link Horario} a persistir.
     * @return El horario guardado con su ID asignado.
     */
    Horario save(Horario horario);
    
    /**
     * Elimina un horario por su ID.
     *
     * @param id Identificador del horario a eliminar.
     */
    void deleteById(Long id);
    
    /**
     * Busca un horario por su ID.
     *
     * @param id Identificador único del horario.
     * @return Un {@link Optional} que contiene el horario si existe, o vacío si no se encuentra.
     */
    Optional<Horario> findById(Long id);
    
    /**
     * Obtiene todos los horarios registrados.
     *
     * @return Lista de todos los horarios.
     */
    List<Horario> findAll();
   
    /**
     * Busca los horarios asociados a una empresa específica.
     *
     * @param idEmpresa Identificador de la empresa.
     * @return Lista de horarios de la empresa.
     */
    List<Horario> findByEmpresa_IdEmpresa(Long idEmpresa);
    
    /**
     * Busca horarios que contengan un día laboral específico, sin distinguir mayúsculas y minúsculas.
     *
     * @param dia Día de la semana a buscar.
     * @return Lista de horarios que incluyen el día especificado.
     */
    List<Horario> findByDiasLaborablesContainingIgnoreCase(String dia);
    
    /**
     * Busca horarios cuya hora de inicio esté dentro de un rango específico.
     *
     * @param inicio Hora de inicio del rango.
     * @param fin Hora de fin del rango.
     * @return Lista de horarios con hora de inicio dentro del rango especificado.
     */
    List<Horario> findByHorarioInicioBetween(LocalTime inicio, LocalTime fin);
    
    /**
     * Busca horarios cuya hora de fin esté dentro de un rango específico.
     *
     * @param inicio Hora de inicio del rango.
     * @param fin Hora de fin del rango.
     * @return Lista de horarios con hora de fin dentro del rango especificado.
     */
    List<Horario> findByHorarioFinBetween(LocalTime inicio, LocalTime fin);
    
    /**
     * Busca horarios de una empresa específica que incluyan un día laboral determinado.
     *
     * @param idEmpresa Identificador de la empresa.
     * @param dia Día de la semana a buscar.
     * @return Lista de horarios que cumplen ambas condiciones.
     */
    List<Horario> findByEmpresa_IdEmpresaAndDiasLaborablesContaining(Long idEmpresa, String dia);
    
    /**
     * Busca horarios que empiecen antes de una hora determinada.
     *
     * @param hora Hora límite.
     * @return Lista de horarios que comienzan antes de la hora especificada.
     */
    List<Horario> findByHorarioInicioBefore(LocalTime hora);
    
    /**
     * Busca horarios que terminen después de una hora determinada.
     *
     * @param hora Hora límite.
     * @return Lista de horarios que terminan después de la hora especificada.
     */
    List<Horario> findByHorarioFinAfter(LocalTime hora);
    
    /**
     * Obtiene todos los horarios ordenados por hora de inicio de forma ascendente.
     *
     * @return Lista de horarios ordenados.
     */
    List<Horario> findByOrderByHorarioInicioAsc();
    
    /**
     * Busca horarios de una empresa específica cuya hora de inicio esté en un rango determinado.
     *
     * @param idEmpresa Identificador de la empresa.
     * @param inicio Hora de inicio del rango.
     * @param fin Hora de fin del rango.
     * @return Lista de horarios que cumplen con las condiciones.
     */
    List<Horario> findByEmpresa_IdEmpresaAndHorarioInicioBetween(Long idEmpresa, LocalTime inicio, LocalTime fin);
    
    /**
     * Busca horarios de una empresa específica cuya hora de fin esté en un rango determinado.
     *
     * @param idEmpresa Identificador de la empresa.
     * @param inicio Hora de inicio del rango.
     * @param fin Hora de fin del rango.
     * @return Lista de horarios que cumplen con las condiciones.
     */
    List<Horario> findByEmpresa_IdEmpresaAndHorarioFinBetween(Long idEmpresa, LocalTime inicio, LocalTime fin);
    
    /**
     * Busca horarios que incluyan exactamente un día específico.
     *
     * @param dia Día de la semana a buscar.
     * @return Lista de horarios que incluyen el día especificado.
     */
    List<Horario> findByDiaExacto(String dia);
    
    /**
     * Busca horarios de una empresa específica que estén comprendidos entre dos horas determinadas.
     *
     * @param idEmpresa Identificador de la empresa.
     * @param inicio Hora de inicio.
     * @param fin Hora de fin.
     * @return Lista de horarios que cumplen con las condiciones.
     */
    List<Horario> findByEmpresaAndHorarioBetween(Long idEmpresa, LocalTime inicio, LocalTime fin);
    
    /**
     * Busca horarios que se solapen con un rango de horas específico.
     *
     * @param inicio Hora de inicio.
     * @param fin Hora de fin.
     * @return Lista de horarios que se solapan con el rango especificado.
     */
    List<Horario> findByHorarioOverlap(LocalTime inicio, LocalTime fin);
    
    /**
     * Busca un horario específico para una empresa, en un día y con horas de inicio y fin exactas.
     *
     * @param idEmpresa Identificador de la empresa.
     * @param dia Día de la semana.
     * @param inicio Hora exacta de inicio.
     * @param fin Hora exacta de fin.
     * @return El horario que cumple con todos los criterios, o null si no existe.
     */
    Horario findByEmpresaDiaYHorarioExacto(Long idEmpresa, String dia, LocalTime inicio, LocalTime fin);
    
    /**
     * Busca horarios que no estén asociados a ninguna empresa.
     *
     * @return Lista de horarios sin empresa asociada.
     */
    List<Horario> findOrphanHorarios();
    
    /**
     * Cuenta el número de horarios asociados a una empresa específica.
     *
     * @param idEmpresa Identificador de la empresa.
     * @return Número total de horarios de la empresa.
     */
    Long countByEmpresaId(Long idEmpresa);
    
    /**
     * Busca horarios que incluyan múltiples días específicos.
     *
     * @param dia1 Primer día a incluir.
     * @param dia2 Segundo día a incluir.
     * @return Lista de horarios que incluyen ambos días.
     */
    List<Horario> findByDiasMultiples(String dia1, String dia2);
    
    /**
     * Busca horarios cuya hora de inicio esté en un rango y cuya hora de fin esté en otro rango.
     *
     * @param inicio1 Límite inferior del rango para hora de inicio.
     * @param fin1 Límite superior del rango para hora de inicio.
     * @param inicio2 Límite inferior del rango para hora de fin.
     * @param fin2 Límite superior del rango para hora de fin.
     * @return Lista de horarios que cumplen con las condiciones.
     */
    List<Horario> findByHorarioRanges(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2);
    
    /**
     * Busca horarios que fueron dados de alta en un rango de fechas específico.
     *
     * @param fechaInicio Fecha de inicio del rango.
     * @param fechaFin Fecha de fin del rango.
     * @return Lista de horarios dados de alta en el período especificado.
     */
    List<Horario> findByFechaAltaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca horarios que fueron modificados después de una fecha determinada.
     *
     * @param fecha Fecha límite.
     * @return Lista de horarios modificados después de la fecha especificada.
     */
    List<Horario> findByFechaModificacionAfter(LocalDateTime fecha);
}