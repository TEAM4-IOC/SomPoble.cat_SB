package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.exception.GlobalExceptionHandler;
import com.sompoble.cat.repository.impl.EmpresarioHibernate;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.service.EmpresarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Clase de test para probar la funcionalidad del controlador EmpresaController.
 * 
 * Estos tests verifican que los endpoints del controlador manejen correctamente 
 * diferentes escenarios, incluyendo operaciones CRUD exitosas y manejo de errores.
 * La configuración usa MockMvc para simular peticiones HTTP sin necesidad de un 
 * servidor web real.
 */
@ExtendWith(MockitoExtension.class)
public class EmpresaControllerTest {

    @InjectMocks
    private EmpresaController empresaController;

    @Mock
    private EmpresaService empresaService;
    
    @Mock
    private EmpresarioService empresarioService;
    
    @Mock
    private EmpresarioHibernate empresarioHibernate;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    
    private EmpresaDTO empresaDTO1;
    private EmpresaDTO empresaDTO2;
    private Empresa empresa1;
    private Empresa empresa2;
    private Empresario empresario1;
    private Empresario empresario2;
    private EmpresarioDTO empresarioDTO1;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(empresaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        
        empresario1 = new Empresario();
        empresario1.setDni("12345678X");
        empresario1.setNombre("Carlos");
        empresario1.setApellidos("López");
        empresario1.setEmail("carlos@ejemplo.com");
        empresario1.setTelefono("650180800");
        
        empresario2 = new Empresario();
        empresario2.setDni("87654321Y");
        empresario2.setNombre("María");
        empresario2.setApellidos("García");
        empresario2.setEmail("maria@ejemplo.com");
        empresario2.setTelefono("650180801");
        
        empresarioDTO1 = new EmpresarioDTO(
            1L,
            "12345678X",
            "Carlos",
            "López",
            "carlos@ejemplo.com",
            "650180800",
            "pass",
            new ArrayList<>(),
            new ArrayList<>()
        );
        
        empresa1 = new Empresa();
        empresa1.setIdentificadorFiscal("A12345678");
        empresa1.setNombre("Empresa 1");
        empresa1.setDireccion("Dirección 1");
        empresa1.setEmail("empresa1@empresa.com");
        empresa1.setTelefono("650180800");
        empresa1.setTipo(1);
        empresa1.setEmpresario(empresario1);
        
        empresa2 = new Empresa();
        empresa2.setIdentificadorFiscal("B12345678");
        empresa2.setActividad("Peluquería");
        empresa2.setDireccion("Dirección 2");
        empresa2.setEmail("empresa2@empresa.com");
        empresa2.setTelefono("650180801");
        empresa2.setTipo(2);
        empresa2.setEmpresario(empresario2);
        
        empresaDTO1 = new EmpresaDTO(
            1L,
            "12345678X",
            "A12345678",
            "Empresa 1",
            null,
            "Dirección 1",
            "empresa1@empresa.com",
            "650180800",
            1,
            new ArrayList<>(),
            new ArrayList<>()
        );
        
        empresaDTO2 = new EmpresaDTO(
            2L,
            "87654321Y",
            "B12345678",
            null,
            "Peluquería",
            "Dirección 2",
            "empresa2@empresa.com",
            "650180801",
            2,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    /**
     * Prueba para verificar que se obtienen correctamente todas las empresas.
     * Verifica que el endpoint GET /api/empresas devuelva una lista de empresas
     * con sus respectivos DNIs de empresarios.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testGetAllEmpresas() throws Exception {
        List<EmpresaDTO> empresasDTO = List.of(empresaDTO1, empresaDTO2);
        
        when(empresaService.findAll()).thenReturn(empresasDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].empresa.identificadorFiscal").value("A12345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dni").value("12345678X"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].empresa.identificadorFiscal").value("B12345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dni").value("87654321Y"));

        verify(empresaService, times(1)).findAll();
    }

    /**
     * Prueba para verificar que se obtiene correctamente una empresa por su identificador fiscal.
     * Verifica que el endpoint GET /api/empresas/{identificadorFiscal} devuelva los datos de la empresa
     * y el DNI del empresario asociado.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testGetEmpresaByIdentificadorFiscal() throws Exception {
        when(empresaService.findByIdentificadorFiscal("A12345678")).thenReturn(empresaDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresas/A12345678"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.empresa.identificadorFiscal").value("A12345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.empresa.nombre").value("Empresa 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dni").value("12345678X"));

        verify(empresaService, times(1)).findByIdentificadorFiscal("A12345678");
    }
    
    /**
     * Prueba para verificar el comportamiento cuando se intenta obtener una empresa que no existe.
     * Verifica que se devuelva un código de estado 404 (Not Found) cuando el identificador fiscal
     * no corresponde a ninguna empresa registrada.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testGetEmpresaByIdentificadorFiscalNotFound() throws Exception {
        when(empresaService.findByIdentificadorFiscal("A12345678")).thenThrow(new RuntimeException("Empresa no encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/empresas/A12345678"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresaService, times(1)).findByIdentificadorFiscal("A12345678");
    }
    
    /**
     * Prueba para verificar la creación exitosa de una empresa.
     * Verifica que el endpoint POST /api/empresas procese correctamente los datos
     * en formato multipart/form-data y cree una nueva empresa asociada a un empresario existente.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testCreateEmpresa() throws Exception {
        String dni = "12345678X";
        
        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "A12345678");
        empresaData.put("nombre", "Empresa 1");
        empresaData.put("direccion", "Dirección 1");
        empresaData.put("email", "empresa1@empresa.com");
        empresaData.put("telefono", "650180800");
        
        Map<String, Object> dniMap = new HashMap<>();
        dniMap.put("dni", dni);

        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(false);
        when(empresarioService.existsByDni(dni)).thenReturn(true);
        when(empresarioService.findEmpresarioByDNI(dni)).thenReturn(empresario1);
        when(empresarioService.findByDni(dni)).thenReturn(empresarioDTO1);

        MockMultipartFile empresaJson = new MockMultipartFile(
                "empresa", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(empresaData).getBytes());
        
        MockMultipartFile dniJson = new MockMultipartFile(
                "dni", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(dniMap).getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/empresas")
                .file(empresaJson)
                .file(dniJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
        verify(empresarioService, times(1)).existsByDni(dni);
        verify(empresarioService, times(1)).findEmpresarioByDNI(dni);
        verify(empresarioService, times(1)).findByDni(dni);
        verify(empresaService, times(1)).addEmpresa(any(Empresa.class));
    }
    
    /**
     * Prueba para verificar el comportamiento cuando se intenta crear una empresa para un 
     * empresario que ya tiene una empresa asignada.
     * Verifica que se devuelva un código de estado 400 (Bad Request) con un mensaje de error apropiado.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testCreateEmpresaWithExistingEmpresa() throws Exception {
        String dni = "12345678X";
        
        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "A12345678");
        empresaData.put("nombre", "Empresa 1");
        empresaData.put("direccion", "Dirección 1");
        empresaData.put("email", "empresa1@empresa.com");
        empresaData.put("telefono", "650180800");
        
        Map<String, Object> dniMap = new HashMap<>();
        dniMap.put("dni", dni);

        List<EmpresaDTO> empresasAsignadas = new ArrayList<>();
        empresasAsignadas.add(empresaDTO1);
        
        EmpresarioDTO empresarioConEmpresa = new EmpresarioDTO(
            1L,
            "12345678X",
            "Carlos",
            "López",
            "carlos@ejemplo.com",
            "650180800",
            "pass",
            new ArrayList<>(),
            empresasAsignadas
        );

        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(false);
        when(empresarioService.existsByDni(dni)).thenReturn(true);
        when(empresarioService.findEmpresarioByDNI(dni)).thenReturn(empresario1);
        when(empresarioService.findByDni(dni)).thenReturn(empresarioConEmpresa);

        MockMultipartFile empresaJson = new MockMultipartFile(
                "empresa", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(empresaData).getBytes());
        
        MockMultipartFile dniJson = new MockMultipartFile(
                "dni", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(dniMap).getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/empresas")
                .file(empresaJson)
                .file(dniJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("El empresario con DNI 12345678X ya tiene una empresa/autónomo asignada"));

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
        verify(empresarioService, times(1)).existsByDni(dni);
        verify(empresarioService, times(1)).findEmpresarioByDNI(dni);
        verify(empresarioService, times(1)).findByDni(dni);
        verify(empresaService, never()).addEmpresa(any(Empresa.class));
    }
    
    /**
     * Prueba para verificar el comportamiento cuando se intenta crear una empresa con un 
     * identificador fiscal que ya está registrado.
     * Verifica que se devuelva un código de estado 400 (Bad Request) con un mensaje de error apropiado.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testCreateEmpresaWithExistingIdentificadorFiscal() throws Exception {
        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "A12345678");
        empresaData.put("nombre", "Empresa 1");
        empresaData.put("direccion", "Dirección 1");
        empresaData.put("email", "empresa1@empresa.com");
        empresaData.put("telefono", "650180800");
        
        Map<String, Object> dniMap = new HashMap<>();
        dniMap.put("dni", "12345678X");

        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(true);

        MockMultipartFile empresaJson = new MockMultipartFile(
                "empresa", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(empresaData).getBytes());
        
        MockMultipartFile dniJson = new MockMultipartFile(
                "dni", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(dniMap).getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/empresas")
                .file(empresaJson)
                .file(dniJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Empresa con identificador fiscal A12345678 ya existe"));

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
        verify(empresarioService, never()).existsByDni(anyString());
    }
    
    /**
     * Prueba para verificar el comportamiento cuando se intenta crear una empresa sin proporcionar 
     * el DNI del empresario.
     * Verifica que se devuelva un código de estado 400 (Bad Request) con un mensaje de error apropiado.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testCreateEmpresaWithoutDni() throws Exception {
        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "A12345678");
        empresaData.put("nombre", "Empresa 1");
        empresaData.put("direccion", "Dirección 1");
        empresaData.put("email", "empresa1@empresa.com");
        empresaData.put("telefono", "650180800");
        
        Map<String, Object> dniMap = new HashMap<>();
        // No ponemos el DNI en el mapa

        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(false);

        MockMultipartFile empresaJson = new MockMultipartFile(
                "empresa", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(empresaData).getBytes());
        
        MockMultipartFile dniJson = new MockMultipartFile(
                "dni", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(dniMap).getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/empresas")
                .file(empresaJson)
                .file(dniJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No se ha informado el DNI del empresario"));

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
    }
    
    /**
     * Prueba para verificar el comportamiento cuando se intenta crear una empresa asociada a un 
     * empresario que no existe en el sistema.
     * Verifica que se devuelva un código de estado 400 (Bad Request) con un mensaje de error apropiado.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testCreateEmpresaWithNonExistentEmpresario() throws Exception {
        String dni = "12345678X";
        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "A12345678");
        empresaData.put("nombre", "Empresa 1");
        empresaData.put("direccion", "Dirección 1");
        empresaData.put("email", "empresa1@empresa.com");
        empresaData.put("telefono", "650180800");
        
        Map<String, Object> dniMap = new HashMap<>();
        dniMap.put("dni", dni);

        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(false);
        when(empresarioService.existsByDni(dni)).thenReturn(false);

        MockMultipartFile empresaJson = new MockMultipartFile(
                "empresa", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(empresaData).getBytes());
        
        MockMultipartFile dniJson = new MockMultipartFile(
                "dni", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(dniMap).getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/empresas")
                .file(empresaJson)
                .file(dniJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No existe un empresario con DNI " + dni));

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
        verify(empresarioService, times(1)).existsByDni(dni);
    }

    /**
     * Prueba para verificar la actualización exitosa de una empresa existente.
     * Verifica que el endpoint PUT /api/empresas/{identificadorFiscal} actualice correctamente 
     * los datos de la empresa y devuelva un mensaje de confirmación.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testUpdateEmpresa() throws Exception {
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("nombre", "Empresa 1 Updated");
        updatedFields.put("direccion", "Dirección 1 Updated");
        updatedFields.put("email", "empresa1_updated@empresa.com");
        updatedFields.put("telefono", "650180801");

        when(empresaService.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa1);

        MockMultipartFile empresaJson = new MockMultipartFile(
                "empresa", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(updatedFields).getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/empresas/A12345678")
                .file(empresaJson)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Empresa o autónomo con identificador fiscal A12345678 actualizada correctamente"));

        verify(empresaService, times(1)).findByIdentificadorFiscalFull("A12345678");
        verify(empresaService, times(1)).updateEmpresa(empresa1);
    }

    /**
     * Prueba para verificar el comportamiento cuando se intenta actualizar una empresa que no existe.
     * Verifica que se devuelva un código de estado 404 (Not Found) cuando el identificador fiscal
     * no corresponde a ninguna empresa registrada.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testUpdateEmpresaNotFound() throws Exception {
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("nombre", "Empresa 1 Updated");
        updatedFields.put("direccion", "Dirección 1 Updated");
        updatedFields.put("email", "empresa1_updated@empresa.com");
        updatedFields.put("telefono", "650180801");

        when(empresaService.findByIdentificadorFiscalFull("A12345678")).thenThrow(new RuntimeException("Empresa no encontrada"));

        MockMultipartFile empresaJson = new MockMultipartFile(
                "empresa", 
                "", 
                "application/json", 
                objectMapper.writeValueAsString(updatedFields).getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/empresas/A12345678")
                .file(empresaJson)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresaService, times(1)).findByIdentificadorFiscalFull("A12345678");
        verify(empresaService, never()).updateEmpresa(any(Empresa.class));
    }

    /**
     * Prueba para verificar la eliminación exitosa de una empresa.
     * Verifica que el endpoint DELETE /api/empresas/{identificadorFiscal} elimine correctamente 
     * la empresa y devuelva un mensaje de confirmación.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testDeleteEmpresa() throws Exception {
        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(true);
        doNothing().when(empresaService).deleteByIdentificadorFiscal("A12345678");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/empresas/A12345678"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Empresa o autónomo con identificador fiscal A12345678 eliminada correctamente"));

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
        verify(empresaService, times(1)).deleteByIdentificadorFiscal("A12345678");
    }

    /**
     * Prueba para verificar el comportamiento cuando se intenta eliminar una empresa que no existe.
     * Verifica que se devuelva un código de estado 404 (Not Found) cuando el identificador fiscal
     * no corresponde a ninguna empresa registrada.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    public void testDeleteEmpresaNotFound() throws Exception {
        when(empresaService.existsByIdentificadorFiscal("A12345678")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/empresas/A12345678"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresaService, times(1)).existsByIdentificadorFiscal("A12345678");
        verify(empresaService, never()).deleteByIdentificadorFiscal(anyString());
    }
}