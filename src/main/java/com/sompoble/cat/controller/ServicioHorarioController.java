package com.sompoble.cat.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.service.ServicioHorarioService;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/servicios-horarios")
public class ServicioHorarioController {

    @Autowired
    private ServicioHorarioService servicioService;
    
    @GetMapping("/{identificadorFiscal}")
    public ResponseEntity<?> obtenerServiciosPorEmpresa(
            @PathVariable String identificadorFiscal) {
        try {
            List<ServicioHorarioDTO> servicios = servicioService
                .obtenerServiciosConHorariosPorEmpresa(identificadorFiscal);
            
            if(servicios.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron servicios para el identificador fiscal proporcionado");
            }
            
            return ResponseEntity.ok(servicios);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno al procesar la solicitud: " + e.getMessage());
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearDTO(@RequestBody ServicioHorarioDTO dtoRequest) {
        try {
            if(dtoRequest.getIdServicio() == null || dtoRequest.getIdHorario() == null) {
                return ResponseEntity.badRequest()
                    .body("Los campos idServicio e idHorario son obligatorios");
            }
            
            ServicioHorarioDTO dto = servicioService.crearDTO(
                dtoRequest.getIdServicio(), 
                dtoRequest.getIdHorario());
                
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Error al crear el servicio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno al procesar la solicitud: " + e.getMessage());
        }
    }
}