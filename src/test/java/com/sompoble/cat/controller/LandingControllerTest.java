package com.sompoble.cat.controller;

import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.dto.LandingEmpresaDTO;
import com.sompoble.cat.exception.ResourceNotFoundException;
import com.sompoble.cat.repository.ServicioRepository;
import com.sompoble.cat.service.EmpresaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el controlador LandingController.
 * <p>
 * Esta clase contiene tests que verifican el comportamiento del controlador
 * que proporciona datos para la página de inicio, comprobando tanto escenarios
 * exitosos como posibles errores.
 * </p>
 *
 * @author SomPoble Testing Team
 * @version 1.0
 * @since 2025-05-01
 */
public class LandingControllerTest {

    /**
     * Mock del servicio de empresas para simular sus comportamientos.
     */
    @Mock
    private EmpresaService empresaService;

    /**
     * Mock del repositorio de servicios para simular sus comportamientos.
     */
    @Mock
    private ServicioRepository servicioRepository;

    /**
     * Instancia del controlador a probar, con sus dependencias mockeadas.
     */
    @InjectMocks
    private LandingController landingController;

    /**
     * Configura el entorno de pruebas antes de cada test.
     * <p>
     * Inicializa los mocks para simular comportamientos de servicios y repositorios.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba que el método getLandingData devuelve datos correctos cuando existen empresas.
     * <p>
     * Verifica que el controlador combine correctamente los datos de empresas y servicios
     * y que devuelva un ResponseEntity con estado OK (200) y los DTOs adecuados.
     * </p>
     */
    @Test
    public void testGetLandingData_Success() {
        EmpresaDTO empresa1 = new EmpresaDTO();
        empresa1.setIdEmpresa(1L);
        empresa1.setNombre("Empresa Test 1");
        empresa1.setDireccion("Dirección Test 1");
        empresa1.setTelefono("123456789");
        empresa1.setEmail("test1@example.com");
        empresa1.setImagenUrl("https://example.com/img1.jpg");
        empresa1.setIdentificadorFiscal("B12345678");

        EmpresaDTO empresa2 = new EmpresaDTO();
        empresa2.setIdEmpresa(2L);
        empresa2.setNombre("Empresa Test 2");
        empresa2.setDireccion("Dirección Test 2");
        empresa2.setTelefono("987654321");
        empresa2.setEmail("test2@example.com");
        empresa2.setImagenUrl("https://example.com/img2.jpg");
        empresa2.setIdentificadorFiscal("B87654321");

        List<EmpresaDTO> empresas = Arrays.asList(empresa1, empresa2);

        Servicio servicio1 = new Servicio();
        servicio1.setIdServicio(1L);
        servicio1.setNombre("Servicio Test 1");

        Servicio servicio2 = new Servicio();
        servicio2.setIdServicio(2L);
        servicio2.setNombre("Servicio Test 2");

        when(empresaService.findAll()).thenReturn(empresas);
        when(servicioRepository.findAllByEmpresaId(1L)).thenReturn(Arrays.asList(servicio1));
        when(servicioRepository.findAllByEmpresaId(2L)).thenReturn(Arrays.asList(servicio2));

        ResponseEntity<List<LandingEmpresaDTO>> response = landingController.getLandingData();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<LandingEmpresaDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        LandingEmpresaDTO landingEmpresa1 = responseBody.get(0);
        assertEquals("Empresa Test 1", landingEmpresa1.getNombre());
        assertEquals("Dirección Test 1", landingEmpresa1.getDireccion());
        assertEquals("123456789", landingEmpresa1.getTelefono());
        assertEquals("test1@example.com", landingEmpresa1.getEmail());
        assertEquals("https://example.com/img1.jpg", landingEmpresa1.getImagen());
        assertEquals("B12345678", landingEmpresa1.getIdentificadorFiscal());
        assertEquals(1, landingEmpresa1.getServicios().size());
        assertEquals("Servicio Test 1", landingEmpresa1.getServicios().get(0).getNombre());

        LandingEmpresaDTO landingEmpresa2 = responseBody.get(1);
        assertEquals("Empresa Test 2", landingEmpresa2.getNombre());
        assertEquals("Dirección Test 2", landingEmpresa2.getDireccion());
        assertEquals("987654321", landingEmpresa2.getTelefono());
        assertEquals("test2@example.com", landingEmpresa2.getEmail());
        assertEquals("https://example.com/img2.jpg", landingEmpresa2.getImagen());
        assertEquals("B87654321", landingEmpresa2.getIdentificadorFiscal());
        assertEquals(1, landingEmpresa2.getServicios().size());
        assertEquals("Servicio Test 2", landingEmpresa2.getServicios().get(0).getNombre());

        verify(empresaService, times(1)).findAll();
        verify(servicioRepository, times(1)).findAllByEmpresaId(1L);
        verify(servicioRepository, times(1)).findAllByEmpresaId(2L);
    }

    /**
     * Prueba que el método getLandingData maneja correctamente el caso de empresa sin servicios.
     * <p>
     * Verifica que cuando una empresa no tiene servicios asociados, se devuelve una lista vacía
     * de servicios para esa empresa en el DTO correspondiente.
     * </p>
     */
    @Test
    public void testGetLandingData_WithEmptyServices() {
        EmpresaDTO empresa = new EmpresaDTO();
        empresa.setIdEmpresa(1L);
        empresa.setNombre("Empresa Test");
        empresa.setDireccion("Dirección Test");
        empresa.setTelefono("123456789");
        empresa.setEmail("test@example.com");
        empresa.setImagenUrl("https://example.com/img.jpg");
        empresa.setIdentificadorFiscal("B12345678");

        List<EmpresaDTO> empresas = Collections.singletonList(empresa);

        when(empresaService.findAll()).thenReturn(empresas);
        when(servicioRepository.findAllByEmpresaId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<LandingEmpresaDTO>> response = landingController.getLandingData();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<LandingEmpresaDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.size());

        LandingEmpresaDTO landingEmpresa = responseBody.get(0);
        assertEquals("Empresa Test", landingEmpresa.getNombre());
        assertEquals("Dirección Test", landingEmpresa.getDireccion());
        assertEquals("123456789", landingEmpresa.getTelefono());
        assertEquals("test@example.com", landingEmpresa.getEmail());
        assertEquals("https://example.com/img.jpg", landingEmpresa.getImagen());
        assertEquals("B12345678", landingEmpresa.getIdentificadorFiscal());
        assertNotNull(landingEmpresa.getServicios());
        assertTrue(landingEmpresa.getServicios().isEmpty());

        verify(empresaService, times(1)).findAll();
        verify(servicioRepository, times(1)).findAllByEmpresaId(1L);
    }

    /**
     * Prueba que el método getLandingData lanza una excepción cuando no hay empresas.
     * <p>
     * Verifica que cuando el servicio devuelve una lista vacía de empresas,
     * el controlador lanza una ResourceNotFoundException con el mensaje adecuado.
     * </p>
     */
    @Test
    public void testGetLandingData_NoEmpresas() {
        // Configurar comportamiento del mock para devolver lista vacía
        when(empresaService.findAll()).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            landingController.getLandingData();
        });

        assertEquals("No se encontraron empresas en la base de datos", exception.getMessage());

        verify(empresaService, times(1)).findAll();
        verify(servicioRepository, never()).findAllByEmpresaId(anyLong());
    }

    /**
     * Prueba el manejo de excepciones del servicio en el método getLandingData.
     * <p>
     * Verifica que cuando el servicio de empresas lanza una excepción durante la ejecución,
     * esta excepción se propaga correctamente hacia arriba.
     * </p>
     */
    @Test
    public void testGetLandingData_ServiceException() {
        // Configurar comportamiento del mock para lanzar una excepción
        when(empresaService.findAll()).thenThrow(new RuntimeException("Error en el servicio"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            landingController.getLandingData();
        });

        assertEquals("Error en el servicio", exception.getMessage());

        verify(empresaService, times(1)).findAll();
        verify(servicioRepository, never()).findAllByEmpresaId(anyLong());
    }
}