package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "http://localhost:4200")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    // Obtener todas las empresas
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Empresa> empresas = empresaService.findAll();
        if (empresas.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontraron empresas en la base de datos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(empresas);
    }

    // Consultar por CIF
    @GetMapping("/{cif}")
    public ResponseEntity<?> getByCif(@PathVariable String cif) {
        Empresa empresa = empresaService.findByCif(cif);
        if (empresa == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "Empresa con CIF " + cif + " no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(empresa);
    }

    // Crear una nueva empresa
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Empresa empresa) {
        if (empresaService.existsByCif(empresa.getCif())) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("error", "Bad Request");
            response.put("message", "Empresa con CIF " + empresa.getCif() + " ya existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        empresaService.addEmpresario(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Actualizar una empresa
    @PutMapping("/{cif}")
    public ResponseEntity<?> update(@PathVariable String cif, @RequestBody Empresa empresa) {
        Empresa existingEmpresa = empresaService.findByCif(cif);

        if (existingEmpresa == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontró una empresa con el CIF " + cif);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Actualización de datos
        existingEmpresa.setNombre(empresa.getNombre());
        existingEmpresa.setDireccion(empresa.getDireccion());
        existingEmpresa.setEmail(empresa.getEmail());
        existingEmpresa.setTelefono(empresa.getTelefono());

        empresaService.updateEmpresa(existingEmpresa);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Empresa actualizada correctamente");
        response.put("empresa", existingEmpresa);

        return ResponseEntity.ok(response);
    }

    // Eliminar una empresa por CIF
    @DeleteMapping("/{cif}")
    public ResponseEntity<?> delete(@PathVariable String cif) {
        if (!empresaService.existsByCif(cif)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontró una empresa con el CIF " + cif);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        empresaService.deleteByCif(cif);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Empresa con CIF " + cif + " eliminada correctamente");
        return ResponseEntity.ok(response);
    }
}