package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Empresario;
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
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpresarioControllerTest {

    @InjectMocks
    private EmpresarioController empresarioController;

    @Mock
    private EmpresarioService empresarioService;

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(empresarioController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllEmpresarios() throws Exception {
        Empresario empresario1 = new Empresario();
        empresario1.setDni("12345678A");
        empresario1.setNombre("Juan");
        empresario1.setApellidos("Perez Garcia");
        empresario1.setEmail("sergio@sergio.es");
        empresario1.setTelefono("650180800");
        empresario1.setPass("pass");

        Empresario empresario2 = new Empresario();
        empresario2.setDni("87654321B");
        empresario2.setNombre("Maria");
        empresario2.setApellidos("Lopez");
        empresario2.setEmail("maria@maria.es");
        empresario2.setTelefono("936772258");
        empresario2.setPass("pass");

        when(empresarioService.findAll()).thenReturn(List.of(empresario1, empresario2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresarios"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dni").value("12345678A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dni").value("87654321B"));

        verify(empresarioService, times(1)).findAll();
    }
    
    @Test
    public void testGetEmpresarioByDni() throws Exception {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Juan");
        empresario.setApellidos("Perez Garcia");
        empresario.setEmail("sergio@sergio.es");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        when(empresarioService.findByDNI("12345678A")).thenReturn(empresario);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresarios/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dni").value("12345678A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan"));

        verify(empresarioService, times(1)).findByDNI("12345678A");
    }

    /*
    * Testeado en Postman
    *
    @Test
    public void testGetEmpresarioByDniNotFound() throws Exception {
        when(empresarioService.findByDNI("12345678A")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresarios/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresarioService, times(1)).findByDNI("12345678A");
    }
    */
    /*
    * Testeado en Postman
    *
    @Test
    public void testCreateEmpresario() throws Exception {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Juan");
        empresario.setApellidos("Perez Garcia");
        empresario.setEmail("sergio@sergio.es");
        empresario.setTelefono("650180800");
        empresario.setpass("pass");
        when(empresarioService.existsByDni(empresario.getDni())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empresario)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(empresarioService, times(1)).addEmpresario(empresario);
    }
    */

    @Test
    public void testCreateEmpresarioBadRequest() throws Exception {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Juan");
        empresario.setApellidos("Perez Garcia");
        empresario.setEmail("sergio@sergio.es");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        when(empresarioService.existsByDni("12345678A")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empresario)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(empresarioService, times(1)).existsByDni("12345678A");
    }

    @Test
    public void testUpdateEmpresario() throws Exception {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Juan");
        empresario.setApellidos("Perez");
        empresario.setEmail("sergio@sergio.es");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");

        Empresario updatedEmpresario = new Empresario();
        updatedEmpresario.setNombre("Juan Updated");
        updatedEmpresario.setApellidos("Perez Updated");
        updatedEmpresario.setEmail("juan@sergio.es");
        updatedEmpresario.setTelefono("916775589");
        updatedEmpresario.setPass("pass1");

        when(empresarioService.findByDNI("12345678A")).thenReturn(empresario);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/empresarios/12345678A")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedEmpresario)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Empresario con DNI 12345678A actualizado correctamente"));

        verify(empresarioService, times(1)).updateEmpresario(empresario);
    }

    @Test
    public void testUpdateClienteNotFound() throws Exception {
        Empresario updatedEmpresario = new Empresario();
        updatedEmpresario.setNombre("Juan Updated");
        updatedEmpresario.setApellidos("Perez Updated");
        updatedEmpresario.setEmail("sergio@sergio.es");
        updatedEmpresario.setTelefono("650180800");
        updatedEmpresario.setPass("pass");

        when(empresarioService.findByDNI("12345678A")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/empresarios/12345678A")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedEmpresario)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresarioService, times(1)).findByDNI("12345678A");
    }

    @Test
    public void testDeleteEmpresario() throws Exception {
        when(empresarioService.existsByDni("12345678A")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/empresarios/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(empresarioService, times(1)).deleteByDni("12345678A");
    }

    @Test
    public void testDeleteEmpresarioNotFound() throws Exception {
        when(empresarioService.existsByDni("12345678A")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/empresarios/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresarioService, times(1)).existsByDni("12345678A");
    }
}