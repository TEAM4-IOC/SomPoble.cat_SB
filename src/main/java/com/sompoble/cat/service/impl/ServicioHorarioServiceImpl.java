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

/**
 * Implementación del servicio para gestionar la relación entre servicios y horarios.
 * <p>
 * Esta clase proporciona la implementación concreta de los métodos definidos en la interfaz
 * ServicioHorarioService, facilitando la obtención de información combinada de servicios
 * y sus horarios asociados.
 * </p>
 */
@Service
public class ServicioHorarioServiceImpl implements ServicioHorarioService {

    /**
     * Repositorio para acceder a los datos de servicios.
     */
    @Autowired
    private ServicioRepository servicioRepository;

    /**
     * Repositorio para acceder a los datos de horarios.
     */
    @Autowired
    private HorarioRepository horarioRepository;
    
    /**
     * Repositorio para acceder a los datos de empresas.
     */
    @Autowired
    private EmpresaRepository empresaRepository;

    /**
     * {@inheritDoc}
     * <p>
     * Este método busca servicios cuyo nombre contenga la cadena especificada y
     * horarios que incluyan el día laborable indicado. Luego combina aquellos
     * que pertenezcan a la misma empresa.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public ServicioHorarioDTO crearDTO(Servicio servicio, Horario horario) {
        return new ServicioHorarioDTO(servicio, horario);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Este método recupera todos los servicios y horarios asociados a una empresa
     * identificada por su identificador fiscal, y crea DTOs combinando cada servicio
     * con cada horario de la misma empresa.
     * </p>
     */
    @Override
    public List<ServicioHorarioDTO> obtenerServiciosConHorariosPorEmpresa(String identificadorFiscal) {
        // 1. Buscar la empresa por identificador fiscal
        Optional<Empresa> optionalEmpresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal));
        
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

    /**
     * {@inheritDoc}
     * <p>
     * Este método busca un servicio y un horario por sus IDs respectivos, verifica que
     * ambos pertenezcan a la misma empresa, y crea un DTO con la información combinada.
     * </p>
     * 
     * @throws RuntimeException si el servicio o el horario no existen, o si no pertenecen
     *                           a la misma empresa
     */
    @Override
    public ServicioHorarioDTO crearDTO(Long servicioId, Long horarioId) {
        // 1. Buscar el servicio por ID
        Optional<Servicio> servicioOptional = Optional.ofNullable(servicioRepository.findById(servicioId));
        if (!servicioOptional.isPresent()) {
            throw new RuntimeException("Servicio no encontrado con ID: " + servicioId);
        }
        Servicio servicio = servicioOptional.get();

        // 2. Buscar el horario por ID
        Optional<Horario> horarioOptional = Optional.ofNullable(horarioRepository.findById(horarioId));
        if (!horarioOptional.isPresent()) {
            throw new RuntimeException("Horario no encontrado con ID: " + horarioId);
        }
        Horario horario = horarioOptional.get();

        // 3. Validar que pertenezcan a la misma empresa 
        if (!servicio.getEmpresa().getIdEmpresa().equals(horario.getEmpresa().getIdEmpresa())) {
            throw new RuntimeException("El servicio y el horario deben pertenecer a la misma empresa");
        }

        // 4. Crear el DTO
        return new ServicioHorarioDTO(servicio, horario);
    }
}