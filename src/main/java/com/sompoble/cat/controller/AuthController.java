/**
 * Clase principal que maneja las solicitudes relacionadas con el inicio de sesión.
 * Este controlador procesa las peticiones de autenticación para usuarios (clientes y empresarios).
 */
package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.exception.BadRequestException;
import com.sompoble.cat.exception.UnauthorizedException;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmpresarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
/**
 * Controlador encargado de gestionar las rutas relacionadas con el inicio de sesión.
 */
@RestController
@RequestMapping("/api/login")
public class AuthController {
	/**
     * Servicio para interactuar con los datos de los clientes.
     */
    @Autowired
    private ClienteService clienteService;
    /**
     * Servicio para interactuar con los datos de los empresarios.
     */
    @Autowired
    private EmpresarioService empresarioService;
    /**
     * Codificador de contraseñas utilizado para comparar contraseñas cifradas.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Método que maneja las peticiones POST para iniciar sesión.
     *
     * @param loginRequest Mapa que contiene los campos "email" y "pass".
     * @return ResponseEntity con la respuesta del servidor.
     * @throws BadRequestException si faltan datos esenciales como email o contraseña.
     * @throws UnauthorizedException si las credenciales son incorrectas.
     */
    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String pass = loginRequest.get("pass");

        if (email == null || pass == null) {
            throw new BadRequestException("Email y contraseña son obligatorios");
        }

        email = email.toLowerCase();
        Map<String, Object> response = new HashMap<>();

        ClienteDTO cliente = clienteService.findByEmail(email);
        EmpresarioDTO empresario = empresarioService.findByEmail(email);

        if (cliente != null && empresario != null && passwordEncoder.matches(pass, cliente.getPass())) {
            return crearRespuestaExitosa(response, 3, cliente);
        }

        if (cliente != null && passwordEncoder.matches(pass, cliente.getPass())) {
            return crearRespuestaExitosa(response, 1, cliente);
        }

        if (empresario != null && passwordEncoder.matches(pass, empresario.getPass())) {
            return crearRespuestaExitosa(response, 2, empresario);
        }

        if (cliente == null && empresario == null) {
            throw new BadRequestException("El usuario introducido no se encuentra dado de alta en la plataforma");
        } else {
            throw new UnauthorizedException("Credenciales incorrectas");
        }
    }
    /**
     * Crea una respuesta exitosa para el inicio de sesión.
     *
     * @param response Mapa que contendrá los datos de la respuesta.
     * @param tipoUsuario Código que indica el tipo de usuario:
     *                    - 1: Cliente
     *                    - 2: Empresario
     *                    - 3: Ambos (caso especial, no se menciona en el código)
     * @param usuario Objeto DTO que representa al usuario autenticado.
     * @return ResponseEntity con un mensaje de éxito y los datos del usuario.
     */
    private ResponseEntity<?> crearRespuestaExitosa(Map<String, Object> response, int tipoUsuario, Object usuario) {
        response.put("status", 200);
        response.put("message", "Inicio de sesión exitoso");
        response.put("tipoUsuario", tipoUsuario);
        response.put("usuario", usuario);
        return ResponseEntity.ok(response);
    }
}
