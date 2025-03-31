package com.sompoble.cat.service;

import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.domain.Horario;
import java.util.List;

public interface ServicioHorarioService {

    // Obtener todos los servicios con sus horarios por empresa
    List<ServicioHorarioDTO> obtenerServiciosConHorariosPorEmpresa(Long empresaId);

    // Buscar por nombre de servicio y día laborable
    List<ServicioHorarioDTO> buscarPorNombreYServicio(
        String nombreServicio, String diaLaborable);

    // Obtener DTO a partir de objetos individuales
    ServicioHorarioDTO crearDTO(Servicio servicio, Horario horario);
}