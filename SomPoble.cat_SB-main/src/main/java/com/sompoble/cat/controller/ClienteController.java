package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.service.ClienteService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

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
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontraron clientes en la base de datos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(clientes); //Devuelve 200
    }

    // Consulta por DNI
    @GetMapping("/{dni}")
    public ResponseEntity<?> getByDni(@PathVariable String dni) {
        Cliente cliente = clienteService.findByDni(dni);
        if (cliente == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "Cliente con DNI " + dni + " no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(cliente); //Devuelve 200
    }

    // Crear un cliente
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Cliente cliente) {
        if (clienteService.existsByDni(cliente.getDni())) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("error", "Bad Request");
            response.put("message", "Cliente con DNI " + cliente.getDni() + " ya existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else if (clienteService.existsByEmail(cliente.getEmail())){
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("error", "Bad Request");
            response.put("message", "Email" + cliente.getEmail()+ " ya existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        clienteService.addCliente(cliente);
        return ResponseEntity.created(null).build();  //Creación exitosa con código 201
    }

    // Actualizar un cliente
    @PutMapping("/{dni}")
    public ResponseEntity<?> update(@PathVariable String dni, @RequestBody Cliente cliente) {
        Cliente existingCliente = clienteService.findByDni(dni);

        if (existingCliente == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontró un cliente con el DNI " + dni);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Actualización de los datos del cliente
        existingCliente.setNombre(cliente.getNombre());
        existingCliente.setApellidos(cliente.getApellidos());
        existingCliente.setTelefono(cliente.getTelefono());
        existingCliente.setContraseña(cliente.getContraseña());
        existingCliente.setEmail(cliente.getEmail());

        clienteService.updateCliente(existingCliente);

        // Respuesta de éxito
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Cliente actualizado correctamente");
        response.put("cliente", existingCliente);

        return ResponseEntity.ok(response); //Devuelve 200
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<?> delete(@PathVariable String dni) {
        if (!clienteService.existsByDni(dni)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", "No se encontró un cliente con el DNI " + dni);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        clienteService.deleteByDni(dni);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Cliente con DNI " + dni + " eliminado correctamente");

        return ResponseEntity.ok(response); //Devuelve 200
    }
}
