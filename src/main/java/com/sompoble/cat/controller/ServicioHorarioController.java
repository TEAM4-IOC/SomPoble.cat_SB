package com.sompoble.cat.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.service.ServicioHorarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/servicios-horarios")
public class ServicioHorarioController {
	private final ServicioHorarioService servicioService;

    /**
     * Inyección de dependencia del servicio.
     * 
     * @param servicioService Servicio que maneja la lógica de negocio.
     */
    @Autowired
    public ServicioHorarioController(ServicioHorarioService servicioService) {
        this.servicioService = servicioService;
    }

    // --- Endpoints ---

    /**
     * Obtiene servicios con horarios de una empresa por su ID.
     * 
     * @param empresaId Identificador de la empresa.
     * @return Lista de DTOs con los servicios y horarios.
     */
    @GetMapping("/empresa/{id}")
    public ResponseEntity<List<ServicioHorarioDTO>> obtenerServiciosPorEmpresa(
            @PathVariable Long empresaId) {

        List<ServicioHorarioDTO> dtos = servicioService.obtenerServiciosConHorariosPorEmpresa(empresaId);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Busca servicios con horarios por nombre de servicio y día laborable.
     * 
     * @param nombreServicio Nombre del servicio (soporta búsquedas parciales).
     * @param diaLaborable Día laborable (ej: "Lunes").
     * @return Lista de DTOs que cumplen los criterios.
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ServicioHorarioDTO>> buscarServicios(
            @RequestParam String nombreServicio,
            @RequestParam String diaLaborable) {

        List<ServicioHorarioDTO> dtos = servicioService.buscarPorNombreYServicio(
                nombreServicio, diaLaborable);
        return ResponseEntity.ok(dtos);
    }

    // --- Ejemplo de endpoint de creación (si lo necesitas) ---
    /**
     * Ejemplo de endpoint para crear un DTO (solo si es necesario).
     * 
     * @param servicio Servicio a combinar.
     * @param horario Horario a combinar.
     * @return DTO creado.
     */
    @PostMapping("/crear-dto")
    public ResponseEntity<ServicioHorarioDTO> crearDTO(
            @RequestBody Servicio servicio,
            @RequestBody Horario horario) {

        ServicioHorarioDTO dto = servicioService.crearDTO(servicio, horario);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
}