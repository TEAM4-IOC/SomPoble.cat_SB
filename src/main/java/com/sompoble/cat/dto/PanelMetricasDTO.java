package com.sompoble.cat.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO que representa el conjunto completo de métricas para el panel de una
 * empresa.
 */
public class PanelMetricasDTO {

    /**
     * Nombre de la empresa
     */
    private String nombreEmpresa;

    /**
     * Total de reservas en el período indicado
     */
    private Long totalReservas;

    /**
     * Ingresos totales generados en el período indicado
     */
    private Double totalIngresos;

    /**
     * Número de clientes únicos en el período indicado
     */
    private Integer clientesUnicos;

    /**
     * Lista de métricas agrupadas por servicio
     */
    //IMPORNTANTE: no se termina de implementar. Inlcuido en TEA6 como mejora de la plataforma.
    //private List<ServicioResumenDTO> servicios;
    /**
     * Lista de métricas agrupadas por mes
     */
    private List<MetricasMensualesDTO> mensual;

    /**
     * Obtiene el nombre de la empresa.
     *
     * @return nombre de la empresa
     */
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    /**
     * Establece el nombre de la empresa.
     *
     * @param nombreEmpresa nombre de la empresa
     */
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * Obtiene el total de reservas.
     *
     * @return total de reservas
     */
    public Long getTotalReservas() {
        return totalReservas;
    }

    /**
     * Establece el total de reservas.
     *
     * @param totalReservas total de reservas
     */
    public void setTotalReservas(Long totalReservas) {
        this.totalReservas = totalReservas;
    }

    /**
     * Obtiene el total de ingresos.
     *
     * @return ingresos totales
     */
    public Double getTotalIngresos() {
        return totalIngresos;
    }

    /**
     * Establece el total de ingresos.
     *
     * @param totalIngresos ingresos totales
     */
    public void setTotalIngresos(Double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    /**
     * Obtiene el número de clientes únicos.
     *
     * @return número de clientes únicos
     */
    public Integer getClientesUnicos() {
        return clientesUnicos;
    }

    /**
     * Establece el número de clientes únicos.
     *
     * @param clientesUnicos número de clientes únicos
     */
    public void setClientesUnicos(Integer clientesUnicos) {
        this.clientesUnicos = clientesUnicos;
    }

    //IMPORNTANTE: no se termina de implementar. Inlcuido en TEA6 como mejora de la plataforma.
    /**
     * Obtiene la lista de métricas por servicio.
     *
     * @return lista de métricas por servicio
     */
    /*
    public List<ServicioResumenDTO> getServicios() {
        return servicios;
    }
     */
    /**
     * Establece la lista de métricas por servicio.
     *
     * @param servicios lista de métricas por servicio
     */
    /*
    public void setServicios(List<ServicioResumenDTO> servicios) {
        this.servicios = servicios;
    }
     */
    /**
     * Obtiene la lista de métricas mensuales.
     *
     * @return lista de métricas por mes
     */
    public List<MetricasMensualesDTO> getMensual() {
        return mensual;
    }

    /**
     * Establece la lista de métricas mensuales.
     *
     * @param mensual lista de métricas por mes
     */
    public void setMensual(List<MetricasMensualesDTO> mensual) {
        this.mensual = mensual;
    }

    /**
     * DTO interno que representa las métricas resumidas por servicio.
     * IMPORNTANTE: no se termina de implementar. Inlcuido en TEA6 como mejora
     * de la plataforma.
     */
    public static class ServicioResumenDTO {

        /**
         * Nombre del servicio
         */
        private String nombre;

        /**
         * Total de reservas del servicio
         */
        private Long reservas;

        /**
         * Ingresos generados por el servicio
         */
        private BigDecimal ingresos;

        /**
         * Constructor de ServicioResumenDTO.
         *
         * @param nombre nombre del servicio
         * @param reservas total de reservas
         * @param ingresos ingresos generados
         */
        public ServicioResumenDTO(String nombre, Long reservas, BigDecimal ingresos) {
            this.nombre = nombre;
            this.reservas = reservas != null ? reservas : 0L;
            this.ingresos = ingresos != null ? ingresos : BigDecimal.ZERO;
        }

        /**
         * Obtiene el nombre del servicio.
         *
         * @return nombre del servicio
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Establece el nombre del servicio.
         *
         * @param nombre nombre del servicio
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        /**
         * Obtiene el total de reservas del servicio.
         *
         * @return total de reservas
         */
        public Long getReservas() {
            return reservas;
        }

        /**
         * Establece el total de reservas del servicio.
         *
         * @param reservas total de reservas
         */
        public void setReservas(Long reservas) {
            this.reservas = reservas;
        }

        /**
         * Obtiene los ingresos generados por el servicio.
         *
         * @return ingresos
         */
        public BigDecimal getIngresos() {
            return ingresos;
        }

        /**
         * Establece los ingresos generados por el servicio.
         *
         * @param ingresos ingresos
         */
        public void setIngresos(BigDecimal ingresos) {
            this.ingresos = ingresos;
        }
    }

    /**
     * DTO interno que representa las métricas por mes.
     */
    public static class MetricasMensualesDTO {

        /**
         * Nombre del mes
         */
        private String mes;

        /**
         * Total de reservas en el mes
         */
        private Long reservas;

        /**
         * Ingresos generados en el mes
         */
        private BigDecimal ingresos;

        /**
         * Constructor de MetricasMensualesDTO.
         *
         * @param mes nombre del mes
         * @param reservas total de reservas
         * @param ingresos ingresos generados
         */
        public MetricasMensualesDTO(String mes, Long reservas, BigDecimal ingresos) {
            this.mes = mes;
            this.reservas = reservas != null ? reservas : 0L;
            this.ingresos = ingresos != null ? ingresos : BigDecimal.ZERO;
        }

        /**
         * Obtiene el nombre del mes.
         *
         * @return mes
         */
        public String getMes() {
            return mes;
        }

        /**
         * Establece el nombre del mes.
         *
         * @param mes mes
         */
        public void setMes(String mes) {
            this.mes = mes;
        }

        /**
         * Obtiene el total de reservas del mes.
         *
         * @return total de reservas
         */
        public Long getReservas() {
            return reservas;
        }

        /**
         * Establece el total de reservas del mes.
         *
         * @param reservas total de reservas
         */
        public void setReservas(Long reservas) {
            this.reservas = reservas;
        }

        /**
         * Obtiene los ingresos del mes.
         *
         * @return ingresos
         */
        public BigDecimal getIngresos() {
            return ingresos;
        }

        /**
         * Establece los ingresos del mes.
         *
         * @param ingresos ingresos
         */
        public void setIngresos(BigDecimal ingresos) {
            this.ingresos = ingresos;
        }
    }
}
