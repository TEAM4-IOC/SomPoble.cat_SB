package com.sompoble.cat.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.repository.ServicioRepository;

@ExtendWith(MockitoExtension.class)
public class ServicioServiceImplTest {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioServiceImpl servicioService;

    private Servicio servicio;
    private Empresa empresa;

    @BeforeEach
    public void setup() {
        empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa Test");

        servicio = new Servicio();
        servicio.setNombre("Servicio Test");
        servicio.setDescripcion("Descripción del servicio test");
        servicio.setPrecio(Float.parseFloat("100"));
        servicio.setEmpresa(empresa);
        servicio.setIdServicio(1L);
    }


    @Test
    public void testObtenerPorId() {
        // Arrange
        when(servicioRepository.findById(1L)).thenReturn(servicio);

        // Act
        Servicio result = servicioService.obtenerPorId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdServicio());
        assertEquals("Servicio Test", result.getNombre());
        verify(servicioRepository).findById(1L);
    }
    @Test
    public void testActualizarServicio() {
        // Act
        servicioService.actualizarServicio(servicio);

        // Assert
        verify(servicioRepository).updateServicio(servicio);
    }

    @Test
    public void testAgregarServicio() {
        // Act
        servicioService.agregarServicio(servicio);

        // Assert
        verify(servicioRepository).addServicio(servicio);
    }


    @Test
    public void testObtenerPorEmpresaId() {
        // Arrange
        List<Servicio> expectedServicios = Collections.singletonList(servicio);
        when(servicioRepository.findAllByEmpresaId(1L)).thenReturn(expectedServicios);

        // Act
        List<Servicio> result = servicioService.obtenerPorEmpresaId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdServicio());
        assertEquals("Servicio Test", result.get(0).getNombre());
        verify(servicioRepository).findAllByEmpresaId(1L);
    }

    @Test
    public void testObtenerPorEmpresaIdentificador() {
        // Arrange
        List<Servicio> expectedServicios = Collections.singletonList(servicio);
        when(servicioRepository.findAllByEmpresaIdentificador("A12345678")).thenReturn(expectedServicios);

        // Act
        List<Servicio> result = servicioService.obtenerPorEmpresaIdentificador("A12345678");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdServicio());
        assertEquals("Servicio Test", result.get(0).getNombre());
        verify(servicioRepository).findAllByEmpresaIdentificador("A12345678");
    }


    @Test
    public void testExistePorId_True() {
        // Arrange
        when(servicioRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean exists = servicioService.existePorId(1L);

        // Assert
        assertTrue(exists);
        verify(servicioRepository).existsById(1L);
    }

    @Test
    public void testExistePorId_False() {
        // Arrange
        when(servicioRepository.existsById(1L)).thenReturn(false);

        // Act
        boolean exists = servicioService.existePorId(1L);

        // Assert
        assertFalse(exists);
        verify(servicioRepository).existsById(1L);
    }

    @Test
    public void testEliminarPorId() {
        // Act
        servicioService.eliminarPorId(1L);

        // Assert
        verify(servicioRepository).deleteById(1L);
    }

    @Test
    public void testObtenerPorId_NotFound() {
        // Arrange
        when(servicioRepository.findById(99L)).thenReturn(null);

        // Act
        Servicio result = servicioService.obtenerPorId(99L);

        // Assert
        assertNull(result);
        verify(servicioRepository).findById(99L);
    }

    @Test
    public void testObtenerPorEmpresaId_EmptyList() {
        // Arrange
        when(servicioRepository.findAllByEmpresaId(99L)).thenReturn(List.of());

        // Act
        List<Servicio> result = servicioService.obtenerPorEmpresaId(99L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(servicioRepository).findAllByEmpresaId(99L);
    }

    @Test
    public void testObtenerPorEmpresaIdentificador_EmptyList() {
        // Arrange
        when(servicioRepository.findAllByEmpresaIdentificador("NONEXISTENT")).thenReturn(List.of());

        // Act
        List<Servicio> result = servicioService.obtenerPorEmpresaIdentificador("NONEXISTENT");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(servicioRepository).findAllByEmpresaIdentificador("NONEXISTENT");
    }
}