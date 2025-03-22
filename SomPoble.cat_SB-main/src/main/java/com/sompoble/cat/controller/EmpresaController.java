package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.exception.BadRequestException;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.service.EmpresarioService;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private EmpresarioService empresarioService;

    // Obtener todas las empresas
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Empresa> empresas = empresaService.findAll();
        if (empresas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron empresas en la base de datos");
        }
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Empresa empresa : empresas) {
            Map<String, Object> response = new HashMap<>();
            response.put("empresa", empresa);
            response.put("dni", empresa.getEmpresario() != null ? empresa.getEmpresario().getDni() : "No disponible");
            responseList.add(response);
        }
        return ResponseEntity.ok(responseList);
    }

    // Consultar por identificador fiscal
    @GetMapping("/{identificadorFiscal}")
    public ResponseEntity<?> getByIdentificadorFiscal(@PathVariable String identificadorFiscal) {
        try {
            Empresa empresa = empresaService.findByIdentificadorFiscal(identificadorFiscal);
            Empresario empresario = empresa.getEmpresario();
            String dni = empresa.getEmpresario().getDni();

            Map<String, Object> response = new HashMap<>();
            response.put("empresa", empresa);
            response.put("dni", empresario != null ? empresario.getDni() : "No disponible");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Empresa o autónomo con " + identificadorFiscal + " no encontrada");
        }
    }

    // Crear una nueva empresa
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> empresaData = (Map<String, Object>) request.get("empresa");

        String identificadorFiscal = (String) empresaData.get("identificadorFiscal");
        if (empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
            throw new BadRequestException("Empresa con identificador fiscal " + identificadorFiscal + " ya existe");
        }

        Empresario empresario = null;
        String dni = null;
        boolean exists = false;

        if (request.containsKey("dni")) {
            dni = (String) request.get("dni");
            exists = empresarioService.existsByDni(dni);
        } else {
            throw new BadRequestException("No se ha informado el DNI del empresario");
        }

        if (!exists) {
            throw new BadRequestException("No existe un empresario con DNI " + dni);
        } else {
            empresario = empresarioService.findByDNI(dni);
        }

        // Crear la empresa con los datos recibidos
        Empresa empresa = new Empresa();
        empresa.setEmpresario(empresario);
        empresa.setIdentificadorFiscal(identificadorFiscal);
        empresa.setDireccion((String) empresaData.get("direccion"));
        empresa.setEmail((String) empresaData.get("email"));
        empresa.setTelefono((String) empresaData.get("telefono"));

        if (empresaData.containsKey("actividad") && empresaData.get("actividad") != null && !empresaData.get("actividad").toString().isEmpty()) {
            empresa.setActividad((String) empresaData.get("actividad"));
            //Autonomo
            empresa.setTipo(2);
        } else {
            empresa.setNombre((String) empresaData.get("nombre"));
            //Empresa
            empresa.setTipo(1);
        }

        empresaService.addEmpresario(empresa);
        return ResponseEntity.created(null).build();
    }

    // Actualizar una empresa
    @PutMapping("/{identificadorFiscal}")
    public ResponseEntity<?> update(@PathVariable String identificadorFiscal, @RequestBody Map<String, Object> updates) {
        try {
            Empresa existingEmpresa = empresaService.findByIdentificadorFiscal(identificadorFiscal);
            updates.forEach((key, value) -> {
                if (value != null) {
                    switch (key) {
                        case "identificadorFiscal" ->
                            existingEmpresa.setIdentificadorFiscal(value.toString());
                        case "nombre" ->
                            existingEmpresa.setNombre(value.toString());
                        case "actividad" ->
                            existingEmpresa.setActividad(value.toString());
                        case "direccion" ->
                            existingEmpresa.setDireccion(value.toString());
                        case "email" ->
                            existingEmpresa.setEmail(value.toString());
                        case "telefono" ->
                            existingEmpresa.setTelefono(value.toString());
                    }
                }
            });

            empresaService.updateEmpresa(existingEmpresa);
            return ResponseEntity.ok("Empresa o autónomo con identificador fiscal " + identificadorFiscal + " actualizada correctamente");
        } catch (Exception e) {
            throw new ResourceNotFoundException("No se encontró una empresa o autónomo con el identificador fiscal " + identificadorFiscal);
        }
    }

    // Eliminar una empresa por identificador fiscal
    @DeleteMapping("/{identificadorFiscal}")
    public ResponseEntity<?> delete(@PathVariable String identificadorFiscal) {
        if (!empresaService.existsByIdentificadorFiscal(identificadorFiscal)) {
            throw new ResourceNotFoundException("No se encontró una empresa con el identificador fiscal " + identificadorFiscal);
        }

        empresaService.deleteByIdentificadorFiscal(identificadorFiscal);

        return ResponseEntity.ok("Empresa o autónomo con identificador fiscal " + identificadorFiscal + " eliminada correctamente");
    }
}
