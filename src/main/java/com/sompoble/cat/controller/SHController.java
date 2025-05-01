package com.sompoble.cat.controller;
import com.sompoble.cat.domain.*;
import com.sompoble.cat.dto.EmpresaDTO;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar la relación entre Servicio y Horario.
 */
@RestController
@RequestMapping("/api/servicio-horario")
public class SHController {
	/**
	 * Repositorio para acceder y gestionar los datos de los servicios.
	 */
    @Autowired
    private ServicioRepository servicioRepository;
    /**
     * Repositorio que permite la gestión de los horarios asociados a los servicios.
     */
    @Autowired
    private HorarioRepository horarioRepository;
    /**
     * Repositorio encargado de operaciones CRUD sobre las empresas o autónomos.
     */
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
       
        Empresa empresa = empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal);
        if (empresa == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
                
        List<Horario> horarios = horarioRepository.findByServicio_Empresa_IdentificadorFiscal(identificadorFiscal);
        if (horarios.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Servicio> servicios = new ArrayList<>();
        for (Horario horario : horarios) {
            Servicio servicio = horario.getServicio();
            if (servicio != null && !servicios.contains(servicio)) {
                servicios.add(servicio);
            }
        }
                
        if (servicios.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
                
        List<ServicioHorarioDTO> respuesta = servicios.stream().map(servicio -> {
           
            Horario horario = horarios.stream()
                    .filter(h -> h.getServicio().equals(servicio)) 
                    .findFirst()
                    .orElse(null);

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
    /**
     * Actualiza un servicio y su horario asociado para una empresa específica.
     *
     * @param idServicio ID del servicio a actualizar.
     * @param identificadorFiscal Identificador fiscal de la empresa dueña del servicio.
     * @param dto Objeto que contiene los datos actualizados del servicio y su horario.
     * @return ResponseEntity con el objeto actualizado {@link ServicioHorarioDTO}.
     * @throws RuntimeException si la empresa, el servicio o el horario no se encuentran.
     */
    @PutMapping("/actualizar/{idServicio}")
    public ResponseEntity<ServicioHorarioDTO> actualizarServicioConHorario(
            @PathVariable Long idServicio,
            @RequestParam String identificadorFiscal,
            @RequestBody ServicioHorarioDTO dto) {

        // Verificar existencia de la empresa
        EmpresaDTO empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscal(identificadorFiscal))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        // Buscar el servicio específico por ID y verificar que pertenezca a la empresa
        Servicio servicio = servicioRepository.findByIdAndEmpresaId(idServicio, empresa.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado para la empresa"));

        // Actualizar datos del servicio
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setDuracion(dto.getDuracion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setLimiteReservas(dto.getLimiteReservas());
        servicio.setFechaModificacion(LocalDateTime.now());
        servicioRepository.addServicio(servicio);

        // Buscar y actualizar el horario asociado
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
     * @return (status 204) si la operación se realiza con éxito.
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
    /*
     * 
     * obtenerTodosServiciosConHorario()
    Obtiene todos los servicios disponibles junto con sus horarios asociados.

    Este método consulta la base de datos para recuperar todos los registros de servicios y sus horarios correspondientes,
    devolviendo una lista de objetos ServicioHorarioDTO que contienen la información combinada.

    Returns:
        ResponseEntity<List<ServicioHorarioDTO>> Respuesta HTTP con código 200 (OK) que incluye una lista de servicios con sus horarios. Si no hay servicios registrados, la lista se devuelve vacía.
     * 
     * */
    @GetMapping("/obtener-todos")
    public ResponseEntity<List<ServicioHorarioDTO>> obtenerTodosServiciosConHorario() {
        List<Horario> horarios = horarioRepository.findAll();
        List<ServicioHorarioDTO> respuesta = new ArrayList<>();

        for (Horario horario : horarios) {
            Servicio servicio = horario.getServicio();
            if (servicio != null) {
                respuesta.add(new ServicioHorarioDTO(servicio, horario));
            }
        }

        return ResponseEntity.ok(respuesta);
    }
    
    /**
     * Obtiene un servicio específico con su horario validando que pertenezca a la empresa indicada
     * 
     * @param identificadorFiscal Identificador fiscal de la empresa (para validación)
     * @param idServicio ID del servicio a consultar
     * @return Servicio con su horario si cumple las validaciones
     */
    @GetMapping("/obtener-empresa-idservicio")
    public ResponseEntity<ServicioHorarioDTO> obtenerServicioEspecifico(
            @RequestParam String identificadorFiscal,
            @RequestParam Long idServicio) {

        // Validar que existe la empresa
        EmpresaDTO empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscal(identificadorFiscal))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con identificador: " + identificadorFiscal));

        // Obtener el servicio validando pertenencia
        Servicio servicio = servicioRepository.findByIdAndEmpresaIdentificadorFiscal(idServicio, identificadorFiscal)
                .orElseThrow(() -> new RuntimeException(
                    "Servicio no encontrado o no pertenece a la empresa (ID: " + idServicio + ", IF: " + identificadorFiscal + ")"));

        // Obtener el horario asociado
        Horario horario = horarioRepository.findFirstByServicioId(idServicio)
                .orElseThrow(() -> new RuntimeException("No existe horario registrado para este servicio"));

        return ResponseEntity.ok(new ServicioHorarioDTO(servicio, horario));
    }
}