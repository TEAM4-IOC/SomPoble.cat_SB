package com.sompoble.cat.service.impl;

import com.sompoble.cat.service.ServicioHorarioService;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.repository.ServicioRepository;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioHorarioServiceImpl implements ServicioHorarioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private HorarioRepository horarioRepository;
    

    @Autowired
    private EmpresaRepository empresaRepository;

    // --- Implementación de métodos ---

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

    @Override
    public List<ServicioHorarioDTO> obtenerServiciosConHorariosPorEmpresa(String identificadorFiscal) {
        // 1. Buscar la empresa por identificador fiscal
        Optional<Empresa> optionalEmpresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscal(identificadorFiscal));
        
        if (optionalEmpresa.isEmpty()) {
            return Collections.emptyList(); // Empresa no encontrada
        }

        Empresa empresa = optionalEmpresa.get();
        Long empresaId = empresa.getIdEmpresa();

        // 2. Obtener servicios y horarios de la empresa
        List<Servicio> servicios = servicioRepository.findAllByEmpresaId(empresaId);
        List<Horario> horarios = horarioRepository.findByEmpresa_IdEmpresa(empresaId);

        List<ServicioHorarioDTO> dtos = new ArrayList<>();

        // 3. Combinar cada servicio con cada horario (producto cartesiano)
        for (Servicio servicio : servicios) {
            for (Horario horario : horarios) {
                // Verificar que pertenezcan a la misma empresa (aunque ya están filtrados)
                if (empresaId.equals(servicio.getEmpresa().getIdEmpresa()) &&
                    empresaId.equals(horario.getEmpresa().getIdEmpresa())) {
                    dtos.add(crearDTO(servicio, horario));
                }
            }
        }

        return dtos;
    }

    @Override
    public ServicioHorarioDTO crearDTO(Long servicioId, Long horarioId) {
        // 1. Buscar el servicio por ID
        Optional<Servicio> servicioOptional = Optional.ofNullable(servicioRepository.findById(servicioId));
        if (!servicioOptional.isPresent()) {
            throw new RuntimeException("Servicio no encontrado con ID: " + servicioId);
        }
        Servicio servicio = servicioOptional.get();

        // 2. Buscar el horario por ID
        Optional<Horario> horarioOptional = horarioRepository.findById(horarioId);
        if (!horarioOptional.isPresent()) {
            throw new RuntimeException("Horario no encontrado con ID: " + horarioId);
        }
        Horario horario = horarioOptional.get();

        // 3. Validar que pertenezcan a la misma empresa (opcional)
        if (!servicio.getEmpresa().getIdEmpresa().equals(horario.getEmpresa().getIdEmpresa())) {
            throw new RuntimeException("El servicio y el horario deben pertenecer a la misma empresa");
        }

        // 4. Crear el DTO
        return new ServicioHorarioDTO(servicio, horario);
    }

	
	}