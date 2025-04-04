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
import java.util.ArrayList;
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
        Empresa empresa = empresaRepository.findByIdentificadorFiscalFull(dto.getIdentificadorFiscal());
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
     * Obtiene los horarios asociados a una empresa específica a partir de su identificador fiscal.
     *
     * Este método consulta los horarios relacionados con los servicios de una empresa,
     * utilizando el identificador fiscal de la empresa para filtrar los resultados.
     * 
     * @param identificadorFiscal El identificador fiscal de la empresa cuyo horario se desea consultar.
     * @return Una lista de objetos {@link Horario} asociados a la empresa con el identificador fiscal dado.
     * Si no se encuentran horarios, se devuelve una lista vacía.
     */
    @GetMapping("/obtener")
    public ResponseEntity<List<ServicioHorarioDTO>> obtenerServicioConHorario(@RequestParam String identificadorFiscal) {
       
        Empresa empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        List<Horario> horarios = horarioRepository.findByServicio_Empresa_IdentificadorFiscal(identificadorFiscal);
        if (horarios.isEmpty()) {
            throw new RuntimeException("No hay horarios registrados para esta empresa");
        }

        List<Servicio> servicios = new ArrayList<>();
        for (Horario horario : horarios) {
            Servicio servicio = horario.getServicio();
            if (servicio != null && !servicios.contains(servicio)) {
                servicios.add(servicio);
            }
        }
        if (servicios.isEmpty()) {
            throw new RuntimeException("No hay servicios relacionados con los horarios para esta empresa");
        }
  
        List<ServicioHorarioDTO> respuesta = servicios.stream().map(servicio -> {
           
            Horario horario = horarios.stream()
                    .filter(h -> h.getServicio().equals(servicio)) 
                    .findFirst()
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
        Empresa empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal))
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
     * Elimina un servicio y su horario asociado por su ID y el identificador fiscal de la empresa.
     * 
     * <p>
     * Este método busca el servicio por su ID y verifica que pertenezca a la empresa especificada
     * mediante el identificador fiscal. Si ambos existen, se eliminan el horario y el servicio de la base de datos.
     * </p>
     * 
     * @param idServicio     ID único del servicio a eliminar.
     * @param identificadorFiscal Identificador fiscal de la empresa propietaria del servicio.
     * @return {@link ResponseEntity#NO_CONTENT()} (status 204) si la operación se realiza con éxito.
     * @throws RuntimeException Si:
     *   - La empresa no existe (identificador fiscal inválido).
     *   - El servicio no existe o no pertenece a la empresa.
     *   - El horario asociado al servicio no existe.
     */
    @DeleteMapping("/anular/{idServicio}")
    public ResponseEntity<Void> anularServicioConHorario(
        @PathVariable Long idServicio,
        @RequestParam String identificadorFiscal) {

        // Verificar existencia de la empresa
        Empresa empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        // Buscar el servicio específico por ID y verificar que pertenezca a la empresa
        Servicio servicio = servicioRepository.findByIdAndEmpresaId(idServicio, empresa.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado para la empresa"));

        // Buscar el horario asociado al servicio
        Horario horario = horarioRepository.findByServicio_IdServicio(idServicio)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado para el servicio"));

        // Eliminar horario y servicio
        horarioRepository.delete(horario.getIdHorario());
        servicioRepository.deleteById(servicio.getIdServicio());

        return ResponseEntity.noContent().build();
    }
}
