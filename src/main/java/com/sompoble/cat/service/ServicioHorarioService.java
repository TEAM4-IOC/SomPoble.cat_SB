package com.sompoble.cat.service;

import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.domain.Horario;
import java.util.List;

/**
 * Servicio que gestiona la relación entre servicios y horarios.
 * <p>
 * Esta interfaz proporciona métodos para obtener información combinada de
 * servicios y sus horarios asociados, facilitando la consulta y manipulación de
 * estas relaciones.
 * </p>
 */
public interface ServicioHorarioService {

    /**
     * Obtiene todos los servicios con sus horarios asociados para una empresa
     * específica.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return Lista de DTOs que contienen información combinada de servicios y
     * horarios.
     */
    List<ServicioHorarioDTO> obtenerServiciosConHorariosPorEmpresa(String identificadorFiscal);

    /**
     * Busca servicios por nombre y día laborable.
     *
     * @param nombreServicio Nombre o parte del nombre del servicio a buscar.
     * @param diaLaborable Día de la semana en que el servicio está disponible.
     * @return Lista de DTOs que contienen información de los servicios que
     * coinciden con los criterios de búsqueda y sus horarios.
     */
    List<ServicioHorarioDTO> buscarPorNombreYServicio(
            String nombreServicio, String diaLaborable);

    /**
     * Crea un DTO combinado a partir de los identificadores de un servicio y un
     * horario.
     *
     * @param servicio Identificador del servicio.
     * @param horario Identificador del horario.
     * @return DTO con la información combinada del servicio y horario.
     */
    ServicioHorarioDTO crearDTO(Long servicio, Long horario);

    /**
     * Crea un DTO combinado a partir de objetos de servicio y horario.
     *
     * @param servicio Objeto {@link Servicio} con la información del servicio.
     * @param horario Objeto {@link Horario} con la información del horario.
     * @return DTO con la información combinada del servicio y horario.
     */
    ServicioHorarioDTO crearDTO(Servicio servicio, Horario horario);
}
