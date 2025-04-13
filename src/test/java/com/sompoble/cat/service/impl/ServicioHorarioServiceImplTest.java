package com.sompoble.cat.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.repository.HorarioRepository;
import com.sompoble.cat.repository.ServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ServicioHorarioServiceImplTest {

    @InjectMocks
    private ServicioHorarioServiceImpl servicioHorarioService;

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerServiciosConHorariosPorEmpresa() {
        
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(1L);

        Servicio servicio = new Servicio();
        servicio.setIdServicio(10L);
        servicio.setNombre("Servicio de Prueba");
        servicio.setDescripcion("Descripción del servicio");
        servicio.setDuracion(60);
        servicio.setPrecio(50.0f);
        servicio.setLimiteReservas(10);
        servicio.setFechaAlta(LocalDateTime.now());
        servicio.setFechaModificacion(LocalDateTime.now());
        servicio.setEmpresa(empresa);

        Horario horario = new Horario();
        horario.setIdHorario(100L);
        horario.setDiasLaborables(String.join(",", Arrays.asList("Lunes", "Martes"))); 
        horario.setHorarioInicio(LocalTime.of(9, 0));
        horario.setHorarioFin(LocalTime.of(18, 0));
        horario.setFechaAlta(LocalDateTime.now());
        horario.setFechaModificacion(LocalDateTime.now());
        horario.setEmpresa(empresa);

        when(empresaRepository.findByIdentificadorFiscalFull("X1234567Z"))
                .thenReturn(empresa);

        when(servicioRepository.findAllByEmpresaId(1L))
                .thenReturn(Collections.singletonList(servicio));

        when(horarioRepository.findByEmpresa_IdEmpresa(1L))
                .thenReturn(Collections.singletonList(horario));

        List<ServicioHorarioDTO> result = servicioHorarioService
                .obtenerServiciosConHorariosPorEmpresa("X1234567Z");

        assertEquals(1, result.size());

        ServicioHorarioDTO dto = result.get(0);
        assertEquals(servicio.getIdServicio(), dto.getIdServicio());
        assertEquals(servicio.getNombre(), dto.getNombre());
        assertEquals(servicio.getDescripcion(), dto.getDescripcion());
        assertEquals(servicio.getDuracion(), dto.getDuracion());
        assertEquals(servicio.getPrecio(), dto.getPrecio(), 0.01); 
        assertEquals(servicio.getLimiteReservas(), dto.getLimiteReservas());
        assertEquals(servicio.getEmpresa().getIdEmpresa(), dto.getEmpresaId());
        assertEquals(servicio.getEmpresa().getIdentificadorFiscal(), dto.getIdentificadorFiscal());

        assertEquals(horario.getIdHorario(), dto.getIdHorario());
        assertEquals(horario.getDiasLaborables(), dto.getDiasLaborables()); 
        assertEquals(horario.getHorarioInicio(), dto.getHorarioInicio());
        assertEquals(horario.getHorarioFin(), dto.getHorarioFin());
    }
    @Test
    void testObtenerServiciosConHorariosPorEmpresa_empresaNoExiste() {
        when(empresaRepository.findByIdentificadorFiscalFull("ZZZZZZZZZ"))
                .thenReturn(null);

        List<ServicioHorarioDTO> result = servicioHorarioService
                .obtenerServiciosConHorariosPorEmpresa("ZZZZZZZZZ");

        assertTrue(result.isEmpty());
    }
}
