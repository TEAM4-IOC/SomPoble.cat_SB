package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.PanelMetricasDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.repository.ReservaRepository;
import com.sompoble.cat.repository.ServicioRepository;
import com.sompoble.cat.service.MetricasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Servicio que procesa y agrega las métricas para el panel de control de
 * empresas. Se encarga de consultar los datos de reservas, ingresos y clientes,
 * tanto totales como desglosados, y empaquetarlos en un objeto
 * {@link PanelMetricasDTO}.
 */
@Service
public class MetricasServiceImpl implements MetricasService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    /**
     * Obtiene las métricas generales y detalladas de una empresa entre dos
     * fechas. Si no se proporciona un rango de fechas, se considera por defecto
     * los últimos 6 meses.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @param fechaInicio Fecha de inicio del rango (opcional).
     * @param fechaFin Fecha de fin del rango (opcional).
     * @return Un DTO {@link PanelMetricasDTO} que contiene las métricas de
     * reservas, ingresos y clientes.
     * @throws RuntimeException si la empresa no es encontrada por su
     * identificador fiscal.
     */
    @Override
    public PanelMetricasDTO obtenerMetricas(String identificadorFiscal, LocalDate fechaInicio, LocalDate fechaFin) {
        Empresa empresa = Optional.ofNullable(empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal))
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        // Si no se proporcionan fechas, se usa el rango de los últimos 6 meses
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now().minusMonths(6);
        }
        if (fechaFin == null) {
            fechaFin = LocalDate.now();
        }

        // Métricas totales
        Long totalReservas = reservaRepository.contarReservasPorEmpresaYFechas(
                empresa.getIdEmpresa(), fechaInicio, fechaFin);
        Double totalIngresos = Optional.ofNullable(reservaRepository.sumarIngresosPorEmpresaYFechas(
                empresa.getIdEmpresa(), fechaInicio, fechaFin)).orElse(0.0);
        Integer clientesUnicos = reservaRepository.contarClientesUnicos(
                empresa.getIdEmpresa(), fechaInicio, fechaFin);

        // Métricas mensuales
        List<PanelMetricasDTO.MetricasMensualesDTO> mensual = reservaRepository.obtenerMetricasMensuales(
                empresa.getIdEmpresa(), fechaInicio, fechaFin);

        // Construcción del DTO
        PanelMetricasDTO panel = new PanelMetricasDTO();
        panel.setNombreEmpresa(empresa.getNombre());
        panel.setTotalReservas(totalReservas);
        panel.setTotalIngresos(totalIngresos);
        panel.setClientesUnicos(clientesUnicos);
        panel.setMensual(mensual);

        return panel;
    }
}
