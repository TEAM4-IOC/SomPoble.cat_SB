package com.sompoble.cat.service;

import com.sompoble.cat.dto.PanelMetricasDTO;

import java.time.LocalDate;

public interface MetricasService {

	 /**
     * Obtiene las métricas generales y detalladas de una empresa entre dos fechas.
     * Si no se proporciona un rango de fechas, se considera por defecto los últimos 6 meses.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @param fechaInicio Fecha de inicio del rango (opcional).
     * @param fechaFin Fecha de fin del rango (opcional).
     * @return Un DTO {@link PanelMetricasDTO} que contiene las métricas de reservas, ingresos y clientes.
     */
    PanelMetricasDTO obtenerMetricas(String identificadorFiscal, LocalDate fechaInicio, LocalDate fechaFin);
}
	
	

