package com.sompoble.cat.service;

import java.util.List;

import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ServicioHorarioDTO;

public interface ServicioHorarioService {

    // Obtener todos los servicios con sus horarios por empresa
    List<ServicioHorarioDTO> obtenerServiciosConHorariosPorEmpresa(String identificadorFiscal);

    // Buscar por nombre de servicio y día laborable
    List<ServicioHorarioDTO> buscarPorNombreYServicio(
        String nombreServicio, String diaLaborable);

    // Obtener DTO a partir de objetos individuales
    ServicioHorarioDTO crearDTO(Long servicio, Long horario);

	ServicioHorarioDTO crearDTO(Servicio servicio, Horario horario);


}