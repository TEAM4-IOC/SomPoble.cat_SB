package com.sompoble.cat.controller;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmpresarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpresarioService empresarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String contraseña = loginRequest.get("contraseña");
        int tipoUsuario = Integer.parseInt(loginRequest.get("tipoUsuario"));

        if (email == null || contraseña == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", "Email y contraseña son obligatorios"
            ));
        }

        Map<String, Object> response = new HashMap<>();

        if (tipoUsuario == 1) { // Cliente
            Cliente cliente = clienteService.findByEmail(email);
            if (cliente != null && passwordEncoder.matches(contraseña, cliente.getContraseña())) {
                response.put("status", 200);
                response.put("message", "Inicio de sesión exitoso");
                response.put("usuario", cliente);
                return ResponseEntity.ok(response);
            }
        } else if (tipoUsuario == 2) { // Empresario
            Empresario empresario = empresarioService.findByEmail(email);
            if (empresario != null && passwordEncoder.matches(contraseña, empresario.getContraseña())) {
                response.put("status", 200);
                response.put("message", "Inicio de sesión exitoso");
                response.put("usuario", empresario);
                return ResponseEntity.ok(response);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", "Tipo de usuario inválido"
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
            "status", 401,
            "error", "Unauthorized",
            "message", "Credenciales incorrectas"
        ));
    }
}
