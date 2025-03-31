package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.repository.HorarioRepository;
import com.sompoble.cat.repository.ServicioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar la relación entre Servicio y Horario.
 */
@RestController
@RequestMapping("/api/servicio-horario")
public class SHController {

    @Autowired
    private ServicioRepository servicioRepository;
    
    @Autowired
    private HorarioRepository horarioRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;

    /**
     * Crea un nuevo servicio y su horario asociado utilizando el identificador fiscal de la empresa.
     * 
     * @param dto Objeto DTO con los datos del servicio y horario.
     * @return ResponseEntity con el DTO del servicio y horario creados.
     */
    @PostMapping("/crear")
    public ResponseEntity<ServicioHorarioDTO> crearServicioConHorario(@RequestBody ServicioHorarioDTO dto) {
        Empresa empresa = empresaRepository.findByIdentificadorFiscal(dto.getIdentificadorFiscal());
        if (empresa == null) {
            throw new RuntimeException("Empresa no encontrada con Identificador Fiscal: " + dto.getIdentificadorFiscal());
        }
        
        Servicio servicio = new Servicio(
                dto.getNombre(), dto.getDescripcion(), dto.getDuracion(),
                dto.getPrecio(), dto.getLimiteReservas(), empresa
        );
        servicioRepository.addServicio(servicio);
        
        Horario horario = new Horario(
                dto.getDiasLaborables(), dto.getHorarioInicio(), dto.getHorarioFin(), empresa
        );
        horario.setServicio(servicio);
        horarioRepository.save(horario);
        
        ServicioHorarioDTO responseDTO = new ServicioHorarioDTO(servicio, horario);
        return ResponseEntity.created(URI.create("/api/servicio-horario/" + servicio.getIdServicio())).body(responseDTO);
    }

    /**
     * Obtiene los servicios y horarios de una empresa según su identificador fiscal.
     * 
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return ResponseEntity con la lista de servicios y horarios asociados.
     */
    @GetMapping("/obtener")
    public ResponseEntity<List<ServicioHorarioDTO>> obtenerServicioConHorario(@RequestParam String identificadorFiscal) {
        Empresa empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscal(identificadorFiscal))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        
        List<Servicio> servicios = servicioRepository.findAllByEmpresaIdentificador(identificadorFiscal);
        if (servicios.isEmpty()) {
            throw new RuntimeException("No hay servicios registrados para esta empresa");
        }
        
        List<ServicioHorarioDTO> respuesta = servicios.stream().map(servicio -> {
            Horario horario = horarioRepository.findByServicio_IdServicio(servicio.getIdServicio())
                    .orElseThrow(() -> new RuntimeException("Horario no encontrado para el servicio con ID: " + servicio.getIdServicio()));
            return new ServicioHorarioDTO(servicio, horario);
        }).toList();
        
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Actualiza un servicio y su horario asociado según el identificador fiscal de la empresa.
     * 
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @param dto Objeto DTO con los datos actualizados.
     * @return ResponseEntity con el DTO del servicio y horario actualizados.
     */
    @PutMapping("/actualizar")
    public ResponseEntity<ServicioHorarioDTO> actualizarServicioConHorario(@RequestParam String identificadorFiscal, @RequestBody ServicioHorarioDTO dto) {
        Empresa empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscal(identificadorFiscal))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        
        List<Servicio> servicios = servicioRepository.findAllByEmpresaIdentificador(identificadorFiscal);
        if (servicios.isEmpty()) {
            throw new RuntimeException("No se encontraron servicios para la empresa con identificador fiscal: " + identificadorFiscal);
        }
        
        Servicio servicio = servicios.get(0);
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setDuracion(dto.getDuracion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setLimiteReservas(dto.getLimiteReservas());
        servicio.setFechaModificacion(LocalDateTime.now());
        servicioRepository.addServicio(servicio);
        
        Horario horario = horarioRepository.findByServicio_IdServicio(servicio.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Horario no encontrado para el servicio"));
        
        horario.setDiasLaborables(dto.getDiasLaborables());
        horario.setHorarioInicio(dto.getHorarioInicio());
        horario.setHorarioFin(dto.getHorarioFin());
        horario.setFechaModificacion(LocalDateTime.now());
        horarioRepository.save(horario);
        
        return ResponseEntity.ok(new ServicioHorarioDTO(servicio, horario));
    }

    /**
     * Elimina un servicio y su horario asociado según el identificador fiscal de la empresa.
     * 
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return ResponseEntity con código de respuesta 204 si la eliminación fue exitosa.
     */
    @DeleteMapping("/anular")
    public ResponseEntity<Void> anularServicioConHorario(@RequestParam String identificadorFiscal) {
        Empresa empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscal(identificadorFiscal))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        
        List<Servicio> servicios = servicioRepository.findAllByEmpresaIdentificador(identificadorFiscal);
        if (servicios.isEmpty()) {
            throw new RuntimeException("No se encontraron servicios para la empresa con identificador fiscal: " + identificadorFiscal);
        }
        
        Servicio servicio = servicios.get(0);
        Horario horario = horarioRepository.findByServicio_IdServicio(servicio.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Horario no encontrado para el servicio"));
        
        horarioRepository.delete(horario.getIdHorario());
        servicioRepository.deleteById(servicio.getIdServicio());
        
        return ResponseEntity.noContent().build();
    }
}
