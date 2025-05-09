package com.sompoble.cat.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.repository.HorarioRepository;
import com.sompoble.cat.repository.ServicioRepository;

@ExtendWith(MockitoExtension.class)
class ServicioHorarioServiceImplTest {

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private ServicioHorarioServiceImpl servicioHorarioService;

    private Empresa empresa;
    private Servicio servicio;
    private Horario horario;
    private ServicioHorarioDTO servicioHorarioDTO;

    @BeforeEach
    void setUp() {
        // Configurar una empresa de prueba
        empresa = new Empresa();
        empresa.setIdEmpresa(1L);
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa Test");

        // Configurar un servicio de prueba
        servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setNombre("Servicio Test");
        servicio.setDescripcion("Descripción de prueba");
        servicio.setPrecio(100.0f);
        servicio.setEmpresa(empresa);

        // Configurar un horario de prueba
        horario = new Horario();
        horario.setIdHorario(1L);
        horario.setDiasLaborables("Lunes,Martes");
        horario.setHorarioInicio(LocalTime.of(9, 0));
        horario.setHorarioFin(LocalTime.of(18, 0));
        horario.setEmpresa(empresa);

        // Configurar un DTO para comparaciones
        servicioHorarioDTO = new ServicioHorarioDTO(servicio, horario);
    }

    @Test
    void testBuscarPorNombreYServicio_NoMatches() {
        // Configurar una empresa diferente
        Empresa empresa2 = new Empresa();
        empresa2.setIdEmpresa(2L);
        
        // Configurar un horario con otra empresa
        Horario horario2 = new Horario();
        horario2.setIdHorario(2L);
        horario2.setEmpresa(empresa2);
        
        // Configurar los mocks
        when(servicioRepository.findByNombreContainingIgnoreCase("Servicio"))
                .thenReturn(Collections.singletonList(servicio));
        when(horarioRepository.findByDiasLaborablesContainingIgnoreCase("Lunes"))
                .thenReturn(Collections.singletonList(horario2));

        // Ejecutar el método a probar
        List<ServicioHorarioDTO> result = servicioHorarioService.buscarPorNombreYServicio("Servicio", "Lunes");

        // Verificar que no hay coincidencias (empresas diferentes)
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se llamaron los métodos correctos
        verify(servicioRepository).findByNombreContainingIgnoreCase("Servicio");
        verify(horarioRepository).findByDiasLaborablesContainingIgnoreCase("Lunes");
    }

    @Test
    void testBuscarPorNombreYServicio_EmptyResults() {
        // Configurar los mocks para devolver listas vacías
        when(servicioRepository.findByNombreContainingIgnoreCase("Inexistente"))
                .thenReturn(Collections.emptyList());
        when(horarioRepository.findByDiasLaborablesContainingIgnoreCase("Domingo"))
                .thenReturn(Collections.emptyList());

        // Ejecutar el método a probar
        List<ServicioHorarioDTO> result = servicioHorarioService.buscarPorNombreYServicio("Inexistente", "Domingo");

        // Verificar resultado vacío
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se llamaron los métodos correctos
        verify(servicioRepository).findByNombreContainingIgnoreCase("Inexistente");
        verify(horarioRepository).findByDiasLaborablesContainingIgnoreCase("Domingo");
    }

    @Test
    void testObtenerServiciosConHorariosPorEmpresa_EmpresaNoEncontrada() {
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("B87654321")).thenReturn(null);

        // Ejecutar el método a probar
        List<ServicioHorarioDTO> result = servicioHorarioService.obtenerServiciosConHorariosPorEmpresa("B87654321");

        // Verificar resultado
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se llamó el método correcto
        verify(empresaRepository).findByIdentificadorFiscalFull("B87654321");
        
        // Verificar que no se llamaron los otros métodos
        verify(servicioRepository, never()).findAllByEmpresaId(anyLong());
        verify(horarioRepository, never()).findByEmpresa_IdEmpresa(anyLong());
    }

    @Test
    void testObtenerServiciosConHorariosPorEmpresa_SinServiciosOHorarios() {
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        when(servicioRepository.findAllByEmpresaId(1L)).thenReturn(Collections.emptyList());
        when(horarioRepository.findByEmpresa_IdEmpresa(1L)).thenReturn(Collections.emptyList());

        // Ejecutar el método a probar
        List<ServicioHorarioDTO> result = servicioHorarioService.obtenerServiciosConHorariosPorEmpresa("A12345678");

        // Verificar resultado
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se llamaron los métodos correctos
        verify(empresaRepository).findByIdentificadorFiscalFull("A12345678");
        verify(servicioRepository).findAllByEmpresaId(1L);
        verify(horarioRepository).findByEmpresa_IdEmpresa(1L);
    }

    @Test
    void testObtenerServiciosConHorariosPorEmpresa_SinServicios() {
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        when(servicioRepository.findAllByEmpresaId(1L)).thenReturn(Collections.emptyList());
        when(horarioRepository.findByEmpresa_IdEmpresa(1L)).thenReturn(Collections.singletonList(horario));

        // Ejecutar el método a probar
        List<ServicioHorarioDTO> result = servicioHorarioService.obtenerServiciosConHorariosPorEmpresa("A12345678");

        // Verificar resultado
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se llamaron los métodos correctos
        verify(empresaRepository).findByIdentificadorFiscalFull("A12345678");
        verify(servicioRepository).findAllByEmpresaId(1L);
        verify(horarioRepository).findByEmpresa_IdEmpresa(1L);
    }

    @Test
    void testObtenerServiciosConHorariosPorEmpresa_SinHorarios() {
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        when(servicioRepository.findAllByEmpresaId(1L)).thenReturn(Collections.singletonList(servicio));
        when(horarioRepository.findByEmpresa_IdEmpresa(1L)).thenReturn(Collections.emptyList());

        // Ejecutar el método a probar
        List<ServicioHorarioDTO> result = servicioHorarioService.obtenerServiciosConHorariosPorEmpresa("A12345678");

        // Verificar resultado
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verificar que se llamaron los métodos correctos
        verify(empresaRepository).findByIdentificadorFiscalFull("A12345678");
        verify(servicioRepository).findAllByEmpresaId(1L);
        verify(horarioRepository).findByEmpresa_IdEmpresa(1L);
    }

    @Test
    void testCrearDTO_ServicioNoEncontrado() {
        // Configurar los mocks
        when(servicioRepository.findById(99L)).thenReturn(null);

        // Ejecutar el método a probar y verificar que lanza excepción
        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioHorarioService.crearDTO(99L, 1L);
        });

        // Verificar mensaje de excepción
        assertEquals("Servicio no encontrado con ID: 99", exception.getMessage());
        
        // Verificar que se llamó el método correcto
        verify(servicioRepository).findById(99L);
        
        // Verificar que no se llamó el método de horario
        verify(horarioRepository, never()).findById(anyLong());
    }

    @Test
    void testCrearDTO_HorarioNoEncontrado() {
        // Configurar los mocks
        when(servicioRepository.findById(1L)).thenReturn(servicio);
        when(horarioRepository.findById(99L)).thenReturn(null);

        // Ejecutar el método a probar y verificar que lanza excepción
        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioHorarioService.crearDTO(1L, 99L);
        });

        // Verificar mensaje de excepción
        assertEquals("Horario no encontrado con ID: 99", exception.getMessage());
        
        // Verificar que se llamaron los métodos correctos
        verify(servicioRepository).findById(1L);
        verify(horarioRepository).findById(99L);
    }

    @Test
    void testCrearDTO_EmpresasNoCoinciden() {
        // Configurar una empresa diferente
        Empresa empresa2 = new Empresa();
        empresa2.setIdEmpresa(2L);
        
        // Configurar un horario con otra empresa
        Horario horario2 = new Horario();
        horario2.setIdHorario(2L);
        horario2.setEmpresa(empresa2);
        
        // Configurar los mocks
        when(servicioRepository.findById(1L)).thenReturn(servicio);
        when(horarioRepository.findById(2L)).thenReturn(horario2);

        // Ejecutar el método a probar y verificar que lanza excepción
        Exception exception = assertThrows(RuntimeException.class, () -> {
            servicioHorarioService.crearDTO(1L, 2L);
        });

        // Verificar mensaje de excepción
        assertEquals("El servicio y el horario deben pertenecer a la misma empresa", exception.getMessage());
        
        // Verificar que se llamaron los métodos correctos
        verify(servicioRepository).findById(1L);
        verify(horarioRepository).findById(2L);
    }
}