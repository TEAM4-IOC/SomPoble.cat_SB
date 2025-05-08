package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.exception.GlobalExceptionHandler;
import com.sompoble.cat.service.EmpresarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpresarioControllerTest {

    @InjectMocks
    private EmpresarioController empresarioController;

    @Mock
    private EmpresarioService empresarioService;

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;
    
    private EmpresarioDTO empresarioDTO1;
    private EmpresarioDTO empresarioDTO2;
    private Empresario empresario1;
    private Empresario empresario2;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(empresarioController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
        objectMapper = new ObjectMapper();
        
        empresarioDTO1 = new EmpresarioDTO(
            1L,
            "12345678A",
            "Juan",
            "Perez Garcia",
            "juan@ejemplo.com",
            "650180800",
            "pass",
            new ArrayList<>(),
            new ArrayList<>()
        );
        
        empresarioDTO2 = new EmpresarioDTO(
            2L,
            "87654321B",
            "Maria",
            "Lopez",
            "maria@ejemplo.com",
            "936772258",
            "pass",
            new ArrayList<>(),
            new ArrayList<>()
        );
        
        empresario1 = new Empresario();
        empresario1.setDni("12345678A");
        empresario1.setNombre("Juan");
        empresario1.setApellidos("Perez Garcia");
        empresario1.setEmail("juan@ejemplo.com");
        empresario1.setTelefono("650180800");
        empresario1.setPass("pass");

        empresario2 = new Empresario();
        empresario2.setDni("87654321B");
        empresario2.setNombre("Maria");
        empresario2.setApellidos("Lopez");
        empresario2.setEmail("maria@ejemplo.com");
        empresario2.setTelefono("936772258");
        empresario2.setPass("pass");
    }

    @Test
    public void testGetAllEmpresarios() throws Exception {
        List<EmpresarioDTO> empresarios = List.of(empresarioDTO1, empresarioDTO2);
        when(empresarioService.findAll()).thenReturn(empresarios);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresarios"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dni").value("12345678A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dni").value("87654321B"));

        verify(empresarioService, times(1)).findAll();
    }
    
    @Test
    public void testGetAllEmpresariosEmpty() throws Exception {
        // Test para el caso cuando no hay empresarios (lista vacía)
        when(empresarioService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresarios"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No se encontraron empresarios en la base de datos"));

        verify(empresarioService, times(1)).findAll();
    }
    
    @Test
    public void testGetEmpresarioByDni() throws Exception {
        when(empresarioService.findByDni("12345678A")).thenReturn(empresarioDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresarios/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dni").value("12345678A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan"));

        verify(empresarioService, times(1)).findByDni("12345678A");
    }

    @Test
    public void testGetEmpresarioByDniNotFound() throws Exception {
        when(empresarioService.findByDni("12345678A")).thenThrow(new RuntimeException("Empresario no encontrado"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresarios/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresarioService, times(1)).findByDni("12345678A");
    }

    @Test
    public void testCreateEmpresario() throws Exception {
        Empresario nuevoEmpresario = new Empresario();
        nuevoEmpresario.setDni("12345678A");
        nuevoEmpresario.setNombre("Juan");
        nuevoEmpresario.setApellidos("Perez Garcia");
        nuevoEmpresario.setEmail("juan@ejemplo.com");
        nuevoEmpresario.setTelefono("650180800");
        nuevoEmpresario.setPass("pass");
        
        when(empresarioService.existsByDni(nuevoEmpresario.getDni())).thenReturn(false);
        when(empresarioService.existsByEmail(nuevoEmpresario.getEmail())).thenReturn(false);
        doNothing().when(empresarioService).addEmpresario(any(Empresario.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEmpresario)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(empresarioService, times(1)).addEmpresario(any(Empresario.class));
    }

    @Test
    public void testCreateEmpresarioDniExistente() throws Exception {
        Empresario nuevoEmpresario = new Empresario();
        nuevoEmpresario.setDni("12345678A");
        nuevoEmpresario.setNombre("Juan");
        nuevoEmpresario.setApellidos("Perez Garcia");
        nuevoEmpresario.setEmail("juan@ejemplo.com");
        nuevoEmpresario.setTelefono("650180800");
        nuevoEmpresario.setPass("pass");
        
        when(empresarioService.existsByDni("12345678A")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEmpresario)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Empresario con DNI 12345678A ya existe"));

        verify(empresarioService, times(1)).existsByDni("12345678A");
        verify(empresarioService, never()).addEmpresario(any(Empresario.class));
    }
    
    @Test
    public void testCreateEmpresarioEmailExistente() throws Exception {
        Empresario nuevoEmpresario = new Empresario();
        nuevoEmpresario.setDni("12345678A");
        nuevoEmpresario.setNombre("Juan");
        nuevoEmpresario.setApellidos("Perez Garcia");
        nuevoEmpresario.setEmail("juan@ejemplo.com");
        nuevoEmpresario.setTelefono("650180800");
        nuevoEmpresario.setPass("pass");
        
        when(empresarioService.existsByDni("12345678A")).thenReturn(false);
        when(empresarioService.existsByEmail("juan@ejemplo.com")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEmpresario)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email juan@ejemplo.com ya está registrado"));

        verify(empresarioService, times(1)).existsByDni("12345678A");
        verify(empresarioService, times(1)).existsByEmail("juan@ejemplo.com");
        verify(empresarioService, never()).addEmpresario(any(Empresario.class));
    }

    @Test
    public void testUpdateEmpresario() throws Exception {
        // Test para el método update utilizando diferentes tipos de campos
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", "Juan Updated");
        updates.put("apellidos", "Perez Updated");
        updates.put("email", "juan.updated@ejemplo.com");
        updates.put("telefono", "916775589");
        updates.put("pass", "pass1");
        updates.put("dni", "12345678Z"); // Probar actualización del DNI también

        when(empresarioService.findEmpresarioByDNI("12345678A")).thenReturn(empresario1);
        doNothing().when(empresarioService).updateEmpresario(any(Empresario.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/empresarios/12345678A")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Empresario con DNI 12345678A actualizado correctamente"));

        // Verificar que el método updateEmpresario fue llamado con el empresario actualizado
        verify(empresarioService, times(1)).findEmpresarioByDNI("12345678A");
        verify(empresarioService, times(1)).updateEmpresario(any(Empresario.class));
            }

    @Test
    public void testUpdateEmpresarioWithNullValues() throws Exception {
        // Test para verificar que los valores nulos no actualizan los campos
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", "Juan Updated");
        updates.put("apellidos", null); // Valor nulo, no debería actualizar
        updates.put("email", "juan.updated@ejemplo.com");

        when(empresarioService.findEmpresarioByDNI("12345678A")).thenReturn(empresario1);
        doNothing().when(empresarioService).updateEmpresario(any(Empresario.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/empresarios/12345678A")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verificar que los apellidos no fueron actualizados porque el valor era nulo
        verify(empresarioService).updateEmpresario(argThat(empresario -> 
            empresario.getNombre().equals("Juan Updated") &&
            empresario.getApellidos().equals("Perez Garcia") && // Mantiene el valor original
            empresario.getEmail().equals("juan.updated@ejemplo.com")
        ));
    }

    @Test
    public void testUpdateEmpresarioWithInvalidField() throws Exception {
        // Test para verificar que los campos no reconocidos son ignorados
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", "Juan Updated");
        updates.put("campo_invalido", "Este campo debe ser ignorado");

        when(empresarioService.findEmpresarioByDNI("12345678A")).thenReturn(empresario1);
        doNothing().when(empresarioService).updateEmpresario(any(Empresario.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/empresarios/12345678A")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verificar que solo se actualizó el nombre y se ignoró el campo inválido
        verify(empresarioService).updateEmpresario(argThat(empresario -> 
            empresario.getNombre().equals("Juan Updated") &&
            empresario.getApellidos().equals("Perez Garcia") // Los demás campos mantienen sus valores originales
        ));
    }

    @Test
    public void testUpdateEmpresarioNotFound() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", "Juan Updated");
        updates.put("apellidos", "Perez Updated");

        when(empresarioService.findEmpresarioByDNI("12345678A")).thenThrow(new RuntimeException("Empresario no encontrado"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/empresarios/12345678A")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No se encontró un empresario con el DNI 12345678A"));

        verify(empresarioService, times(1)).findEmpresarioByDNI("12345678A");
        verify(empresarioService, never()).updateEmpresario(any(Empresario.class));
    }

    @Test
    public void testDeleteEmpresario() throws Exception {
        when(empresarioService.existsByDni("12345678A")).thenReturn(true);
        doNothing().when(empresarioService).deleteByDni("12345678A");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/empresarios/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Empresario con DNI 12345678A eliminado correctamente"));

        verify(empresarioService, times(1)).deleteByDni("12345678A");
    }

    @Test
    public void testDeleteEmpresarioNotFound() throws Exception {
        when(empresarioService.existsByDni("12345678A")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/empresarios/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresarioService, times(1)).existsByDni("12345678A");
        verify(empresarioService, never()).deleteByDni(anyString());
    }
}