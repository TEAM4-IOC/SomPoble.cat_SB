package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.service.EmpresarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresarios")
@CrossOrigin(origins = "http://localhost:4200")
public class EmpresarioController {

    @Autowired
    private EmpresarioService empresarioService;

    // Obtener todos los empresarios
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Empresario> empresarios = empresarioService.findAll();
        if (empresarios.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontraron empresarios en la base de datos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(empresarios);
    }

    // Consultar por DNI
    @GetMapping("/{dni}")
    public ResponseEntity<?> getByDni(@PathVariable String dni) {
        Empresario empresario = empresarioService.findByDNI(dni);
        if (empresario == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "Empresario con DNI " + dni + " no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(empresario);
    }

    // Crear un nuevo empresario
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Empresario empresario) {
        if (empresarioService.existsByDni(empresario.getDni())) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("error", "Bad Request");
            response.put("message", "Empresario con DNI " + empresario.getDni() + " ya existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (empresarioService.existsByEmail(empresario.getEmail())) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("error", "Bad Request");
            response.put("message", "Email " + empresario.getEmail() + " ya está registrado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        empresarioService.addEmpresario(empresario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Actualizar un empresario
    @PutMapping("/{dni}")
    public ResponseEntity<?> update(@PathVariable String dni, @RequestBody Empresario empresario) {
        Empresario existingEmpresario = empresarioService.findByDNI(dni);

        if (existingEmpresario == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontró un empresario con el DNI " + dni);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Actualización de datos
        existingEmpresario.setNombre(empresario.getNombre());
        existingEmpresario.setApellidos(empresario.getApellidos());
        existingEmpresario.setTelefono(empresario.getTelefono());
        existingEmpresario.setContraseña(empresario.getContraseña());
        existingEmpresario.setEmail(empresario.getEmail());        

        empresarioService.updateEmpresario(existingEmpresario);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Empresario actualizado correctamente");
        response.put("empresario", existingEmpresario);

        return ResponseEntity.ok(response);
    }

    // Eliminar un empresario por DNI
    @DeleteMapping("/{dni}")
    public ResponseEntity<?> delete(@PathVariable String dni) {
        if (!empresarioService.existsByDni(dni)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontró un empresario con el DNI " + dni);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        empresarioService.deleteByDni(dni);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Empresario con DNI " + dni + " eliminado correctamente");
        return ResponseEntity.ok(response);
    }
}