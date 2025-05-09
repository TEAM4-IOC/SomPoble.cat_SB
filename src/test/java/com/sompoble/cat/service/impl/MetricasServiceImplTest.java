package com.sompoble.cat.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.PanelMetricasDTO;
import com.sompoble.cat.dto.PanelMetricasDTO.MetricasMensualesDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.repository.ReservaRepository;
import com.sompoble.cat.repository.ServicioRepository;

@ExtendWith(MockitoExtension.class)
class MetricasServiceImplTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private MetricasServiceImpl metricasService;

    private Empresa empresa;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<MetricasMensualesDTO> metricasMensuales;

    @BeforeEach
    void setUp() {
        // Configurar una empresa de prueba
        empresa = new Empresa();
        empresa.setIdEmpresa(1L);
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa Test");

        // Fechas para las pruebas
        fechaInicio = LocalDate.of(2023, 1, 1);
        fechaFin = LocalDate.of(2023, 12, 31);

        // Crear datos de métricas mensuales de ejemplo
        MetricasMensualesDTO enero = new MetricasMensualesDTO("January", 10L, new BigDecimal("1000.00"));
        MetricasMensualesDTO febrero = new MetricasMensualesDTO("February", 15L, new BigDecimal("1500.00"));
        metricasMensuales = Arrays.asList(enero, febrero);
    }

    @Test
    void testObtenerMetricas_ConFechasProporcionadas() {
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        when(reservaRepository.contarReservasPorEmpresaYFechas(1L, fechaInicio, fechaFin)).thenReturn(25L);
        when(reservaRepository.sumarIngresosPorEmpresaYFechas(1L, fechaInicio, fechaFin)).thenReturn(2500.0);
        when(reservaRepository.contarClientesUnicos(1L, fechaInicio, fechaFin)).thenReturn(15);
        when(reservaRepository.obtenerMetricasMensuales(1L, fechaInicio, fechaFin)).thenReturn(metricasMensuales);

        // Ejecutar el método a probar
        PanelMetricasDTO resultado = metricasService.obtenerMetricas("A12345678", fechaInicio, fechaFin);

        // Verificar resultado
        assertNotNull(resultado);
        assertEquals("Empresa Test", resultado.getNombreEmpresa());
        assertEquals(25L, resultado.getTotalReservas());
        assertEquals(2500.0, resultado.getTotalIngresos());
        assertEquals(15, resultado.getClientesUnicos());
        assertEquals(metricasMensuales, resultado.getMensual());
        
        // Verificar que se llamaron los métodos correctos
        verify(empresaRepository).findByIdentificadorFiscalFull("A12345678");
        verify(reservaRepository).contarReservasPorEmpresaYFechas(1L, fechaInicio, fechaFin);
        verify(reservaRepository).sumarIngresosPorEmpresaYFechas(1L, fechaInicio, fechaFin);
        verify(reservaRepository).contarClientesUnicos(1L, fechaInicio, fechaFin);
        verify(reservaRepository).obtenerMetricasMensuales(1L, fechaInicio, fechaFin);
    }

    @Test
    void testObtenerMetricas_SinFechas() {
        // Capturar la fecha actual para las verificaciones
        LocalDate ahora = LocalDate.now();
        LocalDate seisMesesAtras = ahora.minusMonths(6);
        
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        when(reservaRepository.contarReservasPorEmpresaYFechas(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(25L);
        when(reservaRepository.sumarIngresosPorEmpresaYFechas(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(2500.0);
        when(reservaRepository.contarClientesUnicos(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(15);
        when(reservaRepository.obtenerMetricasMensuales(eq(1L), any(LocalDate.class), any(LocalDate.class))).thenReturn(metricasMensuales);

        // Ejecutar el método a probar
        PanelMetricasDTO resultado = metricasService.obtenerMetricas("A12345678", null, null);

        // Verificar resultado
        assertNotNull(resultado);
        assertEquals("Empresa Test", resultado.getNombreEmpresa());
        assertEquals(25L, resultado.getTotalReservas());
        assertEquals(2500.0, resultado.getTotalIngresos());
        assertEquals(15, resultado.getClientesUnicos());
        assertEquals(metricasMensuales, resultado.getMensual());
        
        // Verificar que se llamaron los métodos correctos con las fechas adecuadas
        verify(empresaRepository).findByIdentificadorFiscalFull("A12345678");
        verify(reservaRepository).contarReservasPorEmpresaYFechas(eq(1L), any(LocalDate.class), eq(ahora));
        verify(reservaRepository).sumarIngresosPorEmpresaYFechas(eq(1L), any(LocalDate.class), eq(ahora));
        verify(reservaRepository).contarClientesUnicos(eq(1L), any(LocalDate.class), eq(ahora));
        verify(reservaRepository).obtenerMetricasMensuales(eq(1L), any(LocalDate.class), eq(ahora));
    }

    @Test
    void testObtenerMetricas_EmpresaNoEncontrada() {
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("B87654321")).thenReturn(null);

        // Ejecutar el método a probar y verificar que lanza excepción
        Exception exception = assertThrows(RuntimeException.class, () -> {
            metricasService.obtenerMetricas("B87654321", fechaInicio, fechaFin);
        });

        // Verificar mensaje de excepción
        assertEquals("Empresa no encontrada", exception.getMessage());
        
        // Verificar que se llamó el método correcto
        verify(empresaRepository).findByIdentificadorFiscalFull("B87654321");
        
        // Verificar que no se llamaron los otros métodos
        verify(reservaRepository, never()).contarReservasPorEmpresaYFechas(anyLong(), any(LocalDate.class), any(LocalDate.class));
        verify(reservaRepository, never()).sumarIngresosPorEmpresaYFechas(anyLong(), any(LocalDate.class), any(LocalDate.class));
        verify(reservaRepository, never()).contarClientesUnicos(anyLong(), any(LocalDate.class), any(LocalDate.class));
        verify(reservaRepository, never()).obtenerMetricasMensuales(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testObtenerMetricas_SoloFechaInicio() {
        // Capturar la fecha actual para las verificaciones
        LocalDate ahora = LocalDate.now();
        
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        when(reservaRepository.contarReservasPorEmpresaYFechas(eq(1L), eq(fechaInicio), any(LocalDate.class))).thenReturn(25L);
        when(reservaRepository.sumarIngresosPorEmpresaYFechas(eq(1L), eq(fechaInicio), any(LocalDate.class))).thenReturn(2500.0);
        when(reservaRepository.contarClientesUnicos(eq(1L), eq(fechaInicio), any(LocalDate.class))).thenReturn(15);
        when(reservaRepository.obtenerMetricasMensuales(eq(1L), eq(fechaInicio), any(LocalDate.class))).thenReturn(metricasMensuales);

        // Ejecutar el método a probar
        PanelMetricasDTO resultado = metricasService.obtenerMetricas("A12345678", fechaInicio, null);

        // Verificar resultado
        assertNotNull(resultado);
        assertEquals("Empresa Test", resultado.getNombreEmpresa());
        assertEquals(25L, resultado.getTotalReservas());
        assertEquals(2500.0, resultado.getTotalIngresos());
        assertEquals(15, resultado.getClientesUnicos());
        assertEquals(metricasMensuales, resultado.getMensual());
        
        // Verificar que se llamaron los métodos correctos con las fechas adecuadas
        verify(empresaRepository).findByIdentificadorFiscalFull("A12345678");
        verify(reservaRepository).contarReservasPorEmpresaYFechas(eq(1L), eq(fechaInicio), eq(ahora));
        verify(reservaRepository).sumarIngresosPorEmpresaYFechas(eq(1L), eq(fechaInicio), eq(ahora));
        verify(reservaRepository).contarClientesUnicos(eq(1L), eq(fechaInicio), eq(ahora));
        verify(reservaRepository).obtenerMetricasMensuales(eq(1L), eq(fechaInicio), eq(ahora));
    }

    @Test
    void testObtenerMetricas_SoloFechaFin() {
        // Capturar la fecha actual para las verificaciones
        LocalDate ahora = LocalDate.now();
        LocalDate seisMesesAtras = ahora.minusMonths(6);
        
        // Configurar los mocks
        when(empresaRepository.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        when(reservaRepository.contarReservasPorEmpresaYFechas(eq(1L), any(LocalDate.class), eq(fechaFin))).thenReturn(25L);
        when(reservaRepository.sumarIngresosPorEmpresaYFechas(eq(1L), any(LocalDate.class), eq(fechaFin))).thenReturn(2500.0);
        when(reservaRepository.contarClientesUnicos(eq(1L), any(LocalDate.class), eq(fechaFin))).thenReturn(15);
        when(reservaRepository.obtenerMetricasMensuales(eq(1L), any(LocalDate.class), eq(fechaFin))).thenReturn(metricasMensuales);

        // Ejecutar el método a probar
        PanelMetricasDTO resultado = metricasService.obtenerMetricas("A12345678", null, fechaFin);

        // Verificar resultado
        assertNotNull(resultado);
        assertEquals("Empresa Test", resultado.getNombreEmpresa());
        assertEquals(25L, resultado.getTotalReservas());
        assertEquals(2500.0, resultado.getTotalIngresos());
        assertEquals(15, resultado.getClientesUnicos());
        assertEquals(metricasMensuales, resultado.getMensual());
        
        // Verificar que se llamaron los métodos correctos con las fechas adecuadas
        verify(empresaRepository).findByIdentificadorFiscalFull("A12345678");
        verify(reservaRepository).contarReservasPorEmpresaYFechas(eq(1L), any(LocalDate.class), eq(fechaFin));
        verify(reservaRepository).sumarIngresosPorEmpresaYFechas(eq(1L), any(LocalDate.class), eq(fechaFin));
        verify(reservaRepository).contarClientesUnicos(eq(1L), any(LocalDate.class), eq(fechaFin));
        verify(reservaRepository).obtenerMetricasMensuales(eq(1L), any(LocalDate.class), eq(fechaFin));
    }

    @Test
    void testObtenerMetricas_SinIngresos() {
        // Configurar los mocks con ingresos nulos
        when(empresaRepository.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        when(reservaRepository.contarReservasPorEmpresaYFechas(1L, fechaInicio, fechaFin)).thenReturn(25L);
        when(reservaRepository.sumarIngresosPorEmpresaYFechas(1L, fechaInicio, fechaFin)).thenReturn(null);
        when(reservaRepository.contarClientesUnicos(1L, fechaInicio, fechaFin)).thenReturn(15);
        when(reservaRepository.obtenerMetricasMensuales(1L, fechaInicio, fechaFin)).thenReturn(metricasMensuales);

        // Ejecutar el método a probar
        PanelMetricasDTO resultado = metricasService.obtenerMetricas("A12345678", fechaInicio, fechaFin);

        // Verificar resultado, especialmente que los ingresos son 0 cuando se devuelve null
        assertNotNull(resultado);
        assertEquals("Empresa Test", resultado.getNombreEmpresa());
        assertEquals(25L, resultado.getTotalReservas());
        assertEquals(0.0, resultado.getTotalIngresos());
        assertEquals(15, resultado.getClientesUnicos());
        assertEquals(metricasMensuales, resultado.getMensual());
        
        // Verificar que se llamaron los métodos correctos
        verify(empresaRepository).findByIdentificadorFiscalFull("A12345678");
        verify(reservaRepository).contarReservasPorEmpresaYFechas(1L, fechaInicio, fechaFin);
        verify(reservaRepository).sumarIngresosPorEmpresaYFechas(1L, fechaInicio, fechaFin);
        verify(reservaRepository).contarClientesUnicos(1L, fechaInicio, fechaFin);
        verify(reservaRepository).obtenerMetricasMensuales(1L, fechaInicio, fechaFin);
    }
}