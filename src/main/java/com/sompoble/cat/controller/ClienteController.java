package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Obtener todos los clientes
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Cliente> clientes = clienteService.findAll();
        if (clientes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron clientes en la base de datos");
        }
        return ResponseEntity.ok(clientes);
    }

    // Consulta por DNI
    @GetMapping("/{dni}")
    public ResponseEntity<?> getByDni(@PathVariable String dni) {
        try {
            Cliente cliente = clienteService.findByDni(dni);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Cliente con DNI " + dni + " no encontrado");
        }
    }

    // Crear un cliente
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Cliente cliente) {
        if (clienteService.existsByDni(cliente.getDni())) {
            throw new BadRequestException("Cliente con DNI " + cliente.getDni() + " ya existe");
        } else if (clienteService.existsByEmail(cliente.getEmail())) {
            throw new BadRequestException("Email " + cliente.getEmail() + " ya existe");
        }

        clienteService.addCliente(cliente);
        return ResponseEntity.created(null).build();
    }

    // Actualizar un cliente
    @PutMapping("/{dni}")
    public ResponseEntity<?> update(@PathVariable String dni, @RequestBody Map<String, Object> updates) { 
        try {
            Cliente existingCliente= clienteService.findByDni(dni);
            updates.forEach((key, value) -> {
            if (value != null) {
                switch (key) {
                    case "dni" ->
                        existingCliente.setDni(value.toString());
                    case "nombre" ->
                        existingCliente.setNombre(value.toString());
                    case "apellidos" ->
                        existingCliente.setApellidos(value.toString());
                    case "telefono" ->
                        existingCliente.setTelefono(value.toString());
                    case "pass" ->
                        existingCliente.setPass(value.toString());
                    case "email" ->
                        existingCliente.setEmail(value.toString());
                }
            }
        });

        clienteService.updateCliente(existingCliente);
        return ResponseEntity.ok("Cliente con DNI " + dni + " actualizado correctamente");
        
        } catch (Exception e) {
            throw new ResourceNotFoundException("Cliente con DNI " + dni + " no encontrado");
        }
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<?> delete(@PathVariable String dni) {
        if (!clienteService.existsByDni(dni)) {
            throw new ResourceNotFoundException("No se encontró un cliente con el DNI " + dni);
        }

        clienteService.deleteByDni(dni);
        return ResponseEntity.ok("Cliente con DNI " + dni + " eliminado correctamente");
    }
}
