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
        
        when(servicioRepository.findById(1L)).thenReturn(servicio);

      
        Servicio result = servicioService.obtenerPorId(1L);

       
        assertNotNull(result);
        assertEquals(1L, result.getIdServicio());
        assertEquals("Servicio Test", result.getNombre());
        verify(servicioRepository).findById(1L);
    }
    @Test
    public void testActualizarServicio() {
      
        servicioService.actualizarServicio(servicio);

        
        verify(servicioRepository).updateServicio(servicio);
    }

    @Test
    public void testAgregarServicio() {
       
        servicioService.agregarServicio(servicio);

    
        verify(servicioRepository).addServicio(servicio);
    }


    @Test
    public void testObtenerPorEmpresaId() {
      
        List<Servicio> expectedServicios = Collections.singletonList(servicio);
        when(servicioRepository.findAllByEmpresaId(1L)).thenReturn(expectedServicios);

        List<Servicio> result = servicioService.obtenerPorEmpresaId(1L);

       
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdServicio());
        assertEquals("Servicio Test", result.get(0).getNombre());
        verify(servicioRepository).findAllByEmpresaId(1L);
    }

    @Test
    public void testObtenerPorEmpresaIdentificador() {
        
        List<Servicio> expectedServicios = Collections.singletonList(servicio);
        when(servicioRepository.findAllByEmpresaIdentificador("A12345678")).thenReturn(expectedServicios);

        
        List<Servicio> result = servicioService.obtenerPorEmpresaIdentificador("A12345678");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdServicio());
        assertEquals("Servicio Test", result.get(0).getNombre());
        verify(servicioRepository).findAllByEmpresaIdentificador("A12345678");
    }


    @Test
    public void testExistePorId_True() {
      
        when(servicioRepository.existsById(1L)).thenReturn(true);

        boolean exists = servicioService.existePorId(1L);

        assertTrue(exists);
        verify(servicioRepository).existsById(1L);
    }

    @Test
    public void testExistePorId_False() {
      
        when(servicioRepository.existsById(1L)).thenReturn(false);

      
        boolean exists = servicioService.existePorId(1L);

        assertFalse(exists);
        verify(servicioRepository).existsById(1L);
    }

    @Test
    public void testEliminarPorId() {
     
        servicioService.eliminarPorId(1L);

        
        verify(servicioRepository).deleteById(1L);
    }

    @Test
    public void testObtenerPorId_NotFound() {
        
        when(servicioRepository.findById(99L)).thenReturn(null);

       
        Servicio result = servicioService.obtenerPorId(99L);

        
        assertNull(result);
        verify(servicioRepository).findById(99L);
    }

    @Test
    public void testObtenerPorEmpresaId_EmptyList() {
      
        when(servicioRepository.findAllByEmpresaId(99L)).thenReturn(List.of());

        
        List<Servicio> result = servicioService.obtenerPorEmpresaId(99L);

     
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(servicioRepository).findAllByEmpresaId(99L);
    }

    @Test
    public void testObtenerPorEmpresaIdentificador_EmptyList() {
      
        when(servicioRepository.findAllByEmpresaIdentificador("NONEXISTENT")).thenReturn(List.of());

      
        List<Servicio> result = servicioService.obtenerPorEmpresaIdentificador("NONEXISTENT");

   
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(servicioRepository).findAllByEmpresaIdentificador("NONEXISTENT");
    }
}