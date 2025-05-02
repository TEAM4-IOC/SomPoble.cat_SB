package com.sompoble.cat.controller;

import com.sompoble.cat.dto.PanelMetricasDTO;
import com.sompoble.cat.service.MetricasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controlador REST que expone las métricas para el panel de control
 * empresarial.
 */
@RestController
@RequestMapping("/api/metricas")
public class MetricasController {

    @Autowired
    private MetricasService metricasService;

    /**
     * Obtiene las métricas generales, por servicio y mensuales de una empresa
     * en un rango de fechas.
     *
     * @param empresaIdFiscal Identificador fiscal de la empresa.
     * @param fechaInicio Fecha de inicio del rango (formato yyyy-MM-dd).
     * @param fechaFin Fecha de fin del rango (formato yyyy-MM-dd).
     * @return DTO con las métricas agregadas.
     */
    @GetMapping
    public PanelMetricasDTO obtenerMetricas(
            @RequestParam("empresaIdFiscal") String empresaIdFiscal,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        return metricasService.obtenerMetricas(empresaIdFiscal, fechaInicio, fechaFin);
    }
}
