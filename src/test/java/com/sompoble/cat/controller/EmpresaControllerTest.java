package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.exception.GlobalExceptionHandler;
import com.sompoble.cat.service.EmpresaService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpresaControllerTest {

    @InjectMocks
    private EmpresaController empresaController;

    @Mock
    private EmpresaService empresaService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(empresaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllEmpresas() throws Exception {
        Empresa empresa1 = new Empresa();
        empresa1.setIdentificadorFiscal("A12345678");
        empresa1.setNombre("Empresa 1");
        empresa1.setDireccion("Dirección 1");
        empresa1.setEmail("empresa1@empresa.com");
        empresa1.setTelefono("650180800");

        Empresa empresa2 = new Empresa();
        empresa2.setIdentificadorFiscal("B12345678");
        empresa2.setActividad("Peluqueria");
        empresa2.setDireccion("Dirección 2");
        empresa2.setEmail("empresa2@empresa.com");
        empresa2.setTelefono("650180801");

        when(empresaService.findAll()).thenReturn(List.of(empresa1, empresa2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].identificadorFiscal").value("A12345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].identificadorFiscal").value("B12345678"));

        verify(empresaService, times(1)).findAll();
    }

    @Test
    public void testGetEmpresaByIdentificadorFiscal() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa 1");
        empresa.setDireccion("Dirección 1");
        empresa.setEmail("empresa1@empresa.com");
        empresa.setTelefono("650180800");

        when(empresaService.findByIdentificadorFiscal("A12345678")).thenReturn(empresa);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresas/A12345678"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.identificadorFiscal").value("A12345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Empresa 1"));

        verify(empresaService, times(1)).findByIdentificadorFiscal("A12345678");
    }
    
    /*
    * Testeado en Postman
    *
    @Test
    public void testGetEmpresaByIdentificadorFiscalNotFound() throws Exception {
        when(empresaService.findByIdentificadorFiscal("A12345678")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresas/A12345678"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresaService, times(1)).findByIdentificadorFiscal("A12345678");
    }
    */
    /*
    * Testeado en Postman
    *
    @Test
    public void testCreateEmpresa() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa 1");
        empresa.setDireccion("Dirección 1");
        empresa.setEmail("empresa1@empresa.com");
        empresa.setTelefono("650180800");

        when(empresaService.existsByIdentificadorFiscal(empresa.getIdentificadorFiscal())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(empresa)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(empresaService, times(1)).addEmpresario(empresa);
    }
     */
    @Test
    public void testCreateEmpresaBadRequest() throws Exception {
        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "A12345678");
        empresaData.put("nombre", "Empresa 1");
        empresaData.put("direccion", "Dirección 1");
        empresaData.put("email", "empresa1@empresa.com");
        empresaData.put("telefono", "650180800");

        Map<String, Object> request = new HashMap<>();
        request.put("empresa", empresaData);
        request.put("dniEmpresario", "12345678X");

        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/empresas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Empresa con identificador fiscal A12345678 ya existe"));

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
    }

    @Test
    public void testUpdateEmpresa() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa 1");
        empresa.setDireccion("Dirección 1");
        empresa.setEmail("empresa1@empresa.com");
        empresa.setTelefono("650180800");

        Empresa updatedEmpresa = new Empresa();
        updatedEmpresa.setNombre("Empresa 1 Updated");
        updatedEmpresa.setDireccion("Dirección 1 Updated");
        updatedEmpresa.setEmail("empresa1_updated@empresa.com");
        updatedEmpresa.setTelefono("650180801");

        when(empresaService.findByIdentificadorFiscal("A12345678")).thenReturn(empresa);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/empresas/A12345678")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedEmpresa)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Empresa o autónomo con identificador fiscal A12345678 actualizada correctamente"));

        verify(empresaService, times(1)).updateEmpresa(empresa);
    }

    @Test
    public void testUpdateEmpresaNotFound() throws Exception {
        Empresa updatedEmpresa = new Empresa();
        updatedEmpresa.setNombre("Empresa 1 Updated");
        updatedEmpresa.setDireccion("Dirección 1 Updated");
        updatedEmpresa.setEmail("empresa1_updated@empresa.com");
        updatedEmpresa.setTelefono("650180801");

        when(empresaService.findByIdentificadorFiscal("A12345678")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/empresas/A12345678")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedEmpresa)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresaService, times(1)).findByIdentificadorFiscal("A12345678");
    }

    @Test
    public void testDeleteEmpresa() throws Exception {
        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/empresas/A12345678"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
    }

    @Test
    public void testDeleteEmpresaNotFound() throws Exception {
        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/empresas/A12345678"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
    }
}