package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.service.EmpresarioService;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de empresarios.
 */
@RestController
@RequestMapping("/api/empresarios")
public class EmpresarioController {

    /**
     * Servicio que maneja la lógica de negocio para empresarios.
     */
    @Autowired
    private EmpresarioService empresarioService;

    /**
     * Obtiene todos los empresarios registrados.
     *
     * @return lista de {@link EmpresarioDTO} si existen, de lo contrario lanza
     * excepción.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<EmpresarioDTO> empresarios = empresarioService.findAll();
        if (empresarios.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron empresarios en la base de datos");
        }
        return ResponseEntity.ok(empresarios);
    }

    /**
     * Obtiene un empresario específico por su DNI.
     *
     * @param dni DNI del empresario.
     * @return el {@link EmpresarioDTO} correspondiente.
     * @throws ResourceNotFoundException si no se encuentra el empresario.
     */
    @GetMapping("/{dni}")
    public ResponseEntity<?> getByDni(@PathVariable String dni) {
        try {
            EmpresarioDTO empresario = empresarioService.findByDni(dni);
            return ResponseEntity.ok(empresario);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Empresario con DNI " + dni + " no encontrado");
        }
    }

    /**
     * Crea un nuevo empresario si no existe previamente por DNI o email.
     *
     * @param empresario Objeto {@link Empresario} a crear.
     * @return respuesta con estado 201 si se crea correctamente.
     * @throws BadRequestException si el DNI o el email ya están registrados.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Empresario empresario) {
        if (empresarioService.existsByDni(empresario.getDni())) {
            throw new BadRequestException("Empresario con DNI " + empresario.getDni() + " ya existe");
        } else if (empresarioService.existsByEmail(empresario.getEmail())) {
            throw new BadRequestException("Email " + empresario.getEmail() + " ya está registrado");
        }

        empresarioService.addEmpresario(empresario);
        return ResponseEntity.created(null).build();
    }

    /**
     * Actualiza parcialmente un empresario existente por DNI.
     *
     * @param dni DNI del empresario a actualizar.
     * @param updates Mapa con los campos a actualizar y sus nuevos valores.
     * @return mensaje de éxito si se actualiza correctamente.
     * @throws ResourceNotFoundException si no se encuentra el empresario.
     */
    @PutMapping("/{dni}")
    public ResponseEntity<?> update(@PathVariable String dni, @RequestBody Map<String, Object> updates) {
        try {
            Empresario existingEmpresario = empresarioService.findEmpresarioByDNI(dni);
            updates.forEach((key, value) -> {
                if (value != null) {
                    switch (key) {
                        case "dni" ->
                            existingEmpresario.setDni(value.toString());
                        case "nombre" ->
                            existingEmpresario.setNombre(value.toString());
                        case "apellidos" ->
                            existingEmpresario.setApellidos(value.toString());
                        case "telefono" ->
                            existingEmpresario.setTelefono(value.toString());
                        case "pass" ->
                            existingEmpresario.setPass(value.toString());
                        case "email" ->
                            existingEmpresario.setEmail(value.toString());
                    }
                }
            });

            empresarioService.updateEmpresario(existingEmpresario);
            return ResponseEntity.ok("Empresario con DNI " + dni + " actualizado correctamente");
        } catch (Exception e) {
            throw new ResourceNotFoundException("No se encontró un empresario con el DNI " + dni);
        }
    }

    /**
     * Elimina un empresario por su DNI.
     *
     * @param dni DNI del empresario a eliminar.
     * @return mensaje de confirmación si se elimina correctamente.
     * @throws ResourceNotFoundException si no se encuentra el empresario.
     */
    @DeleteMapping("/{dni}")
    public ResponseEntity<?> delete(@PathVariable String dni) {
        if (!empresarioService.existsByDni(dni)) {
            throw new ResourceNotFoundException("No se encontró un empresario con el DNI " + dni);
        }

        empresarioService.deleteByDni(dni);
        return ResponseEntity.ok("Empresario con DNI " + dni + " eliminado correctamente");
    }
}
