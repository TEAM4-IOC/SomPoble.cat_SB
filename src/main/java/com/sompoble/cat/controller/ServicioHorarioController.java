package com.sompoble.cat.controller;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.service.ServicioHorarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/servicios-horarios")
public class ServicioHorarioController {
	

	    @Autowired
	    private ServicioHorarioService servicioService;
	    
	    @Autowired
	    private EmpresaService empresaService;
	    
	 
	    // --- Servicios y Horarios ---
	    @GetMapping("{identificadorFiscal}")
	    public ResponseEntity<List<ServicioHorarioDTO>> obtenerServiciosPorEmpresa(
	            @PathVariable String identificadorFiscal) {
	        return ResponseEntity.ok(
	            servicioService.obtenerServiciosConHorariosPorEmpresa(identificadorFiscal)
	        );
	    }


	    @PostMapping("/crear")
	    public ResponseEntity<ServicioHorarioDTO> crearDTO(
	            @RequestBody ServicioHorarioDTO dtoRequest) { // Usa el DTO de solicitud

	        Long servicio = dtoRequest.getIdServicio();
	        Long horario = dtoRequest.getIdHorario();

	        // Llamar al servicio para crear el DTO final
	        ServicioHorarioDTO dto = servicioService.crearDTO(servicio, horario);

	        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	    }

	   
	 

/*
	    // --- Reservas (por DNI del cliente) ---
	    @PostMapping("/reservas")
	    public ResponseEntity<Reserva> crearReserva(
	            @RequestParam String dniCliente,
	            @RequestBody Reserva reserva) {
	        Reserva reservaGuardada = reservaService.crearReserva(dniCliente, reserva);
	        return ResponseEntity.status(HttpStatus.CREATED).body(reservaGuardada);
	    }

	    @DeleteMapping("/reservas/{idReserva}/cliente/{dni}")
	    public ResponseEntity<?> eliminarReserva(
	            @PathVariable String dni,
	            @PathVariable Long idReserva) {
	        if (!reservaService.eliminarReserva(dni, idReserva)) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	        return ResponseEntity.noContent().build();
	    }*/
	}
	   
	    