package com.sompoble.cat.service;

import com.sompoble.cat.dto.PanelMetricasDTO;
import java.time.LocalDate;

/**
 * Interfaz que define los servicios para el cálculo y obtención de métricas de
 * negocio.
 * <p>
 * Esta interfaz proporciona métodos para generar informes estadísticos y
 * métricas relacionadas con el rendimiento de una empresa en un periodo
 * determinado, incluyendo datos sobre reservas, ingresos y comportamiento de
 * clientes.
 * </p>
 */
public interface MetricasService {

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
     * @throws IllegalArgumentException Si el identificador fiscal es nulo o
     * inválido.
     * @throws IllegalStateException Si ocurre un error al procesar las
     * métricas.
     */
    PanelMetricasDTO obtenerMetricas(String identificadorFiscal, LocalDate fechaInicio, LocalDate fechaFin);
}
