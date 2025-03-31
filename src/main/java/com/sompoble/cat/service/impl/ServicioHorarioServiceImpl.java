package com.sompoble.cat.service.impl;

import com.sompoble.cat.service.ServicioHorarioService;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.repository.ServicioRepository;
import com.sompoble.cat.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioHorarioServiceImpl implements ServicioHorarioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    // --- Implementación de métodos ---

    @Override
    public List<ServicioHorarioDTO> obtenerServiciosConHorariosPorEmpresa(Long empresaId) {
        List<Servicio> servicios = servicioRepository.findAllByEmpresaId(empresaId);
        List<Horario> horarios = horarioRepository.findByEmpresa_IdEmpresa(empresaId);

        List<ServicioHorarioDTO> dtos = new ArrayList<>();

        // Combina cada servicio con cada horario de la empresa
        for (Servicio servicio : servicios) {
            for (Horario horario : horarios) {
                if (empresaId.equals(servicio.getEmpresa().getIdEmpresa()) &&
                    empresaId.equals(horario.getEmpresa().getIdEmpresa())) {
                    dtos.add(crearDTO(servicio, horario));
                }
            }
        }

        return dtos;
    }

    @Override
    public List<ServicioHorarioDTO> buscarPorNombreYServicio(
        String nombreServicio, String diaLaborable) {

        List<Servicio> servicios = servicioRepository.findByNombreContainingIgnoreCase(nombreServicio);
        List<Horario> horarios = horarioRepository.findByDiasLaborablesContainingIgnoreCase(diaLaborable);

        List<ServicioHorarioDTO> dtos = new ArrayList<>();

        for (Servicio servicio : servicios) {
            for (Horario horario : horarios) {
                if (servicio.getEmpresa().getIdEmpresa().equals(horario.getEmpresa().getIdEmpresa())) {
                    dtos.add(crearDTO(servicio, horario));
                }
            }
        }

        return dtos;
    }

    @Override
    public ServicioHorarioDTO crearDTO(Servicio servicio, Horario horario) {
        return new ServicioHorarioDTO(servicio, horario);
    }
}