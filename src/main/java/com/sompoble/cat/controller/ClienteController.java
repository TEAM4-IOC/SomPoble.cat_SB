/**
 * Controlador encargado de gestionar las operaciones relacionadas con los clientes.
 */
package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
/**
 * Controlador REST que expone endpoints para administrar clientes.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	/**
     * Servicio que maneja las operaciones relacionadas con los clientes.
     */
    @Autowired
    private ClienteService clienteService;
    /**
     * Obtiene todos los clientes registrados en la base de datos.
     *
     * @return ResponseEntity con una lista de clientes si existen, o una excepción si no hay clientes.
     * @throws ResourceNotFoundException si no se encuentran clientes en la base de datos.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ClienteDTO> clientes = clienteService.findAll();
        if (clientes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron clientes en la base de datos");
        }
        return ResponseEntity.ok(clientes);
    }
    /**
     * Obtiene un cliente por su DNI.
     *
     * @param dni Identificador único del cliente.
     * @return ResponseEntity con el cliente si se encuentra, o una excepción si no se encuentra.
     * @throws ResourceNotFoundException si el cliente con el DNI especificado no existe.
     */
    @GetMapping("/{dni}")
    public ResponseEntity<?> getByDni(@PathVariable String dni) {
        try {
            ClienteDTO cliente = clienteService.findByDni(dni);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Cliente con DNI " + dni + " no encontrado");
        }
    }
    /**
     * Crea un nuevo cliente.
     *
     * @param cliente Objeto cliente que contiene los datos necesarios para crear el cliente.
     * @return ResponseEntity indicando que el cliente ha sido creado.
     * @throws BadRequestException si el DNI o el email ya existen en la base de datos.
     */
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
    /**
     * Actualiza un cliente existente.
     *
     * @param dni Identificador único del cliente a actualizar.
     * @param updates Mapa con los nuevos valores para los campos del cliente.
     * @return ResponseEntity indicando que el cliente ha sido actualizado correctamente.
     * @throws ResourceNotFoundException si el cliente con el DNI especificado no existe.
     */
    @PutMapping("/{dni}")
    public ResponseEntity<?> update(@PathVariable String dni, @RequestBody Map<String, Object> updates) { 
        try {
            Cliente existingCliente= clienteService.findByDniFull(dni);
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
    /**
     * Elimina un cliente existente por su DNI.
     *
     * @param dni Identificador único del cliente a eliminar.
     * @return ResponseEntity indicando que el cliente ha sido eliminado correctamente.
     * @throws ResourceNotFoundException si el cliente con el DNI especificado no existe.
     */
    @DeleteMapping("/{dni}")
    public ResponseEntity<?> delete(@PathVariable String dni) {
        if (!clienteService.existsByDni(dni)) {
            throw new ResourceNotFoundException("No se encontró un cliente con el DNI " + dni);
        }

        clienteService.deleteByDni(dni);
        return ResponseEntity.ok("Cliente con DNI " + dni + " eliminado correctamente");
    }
}
