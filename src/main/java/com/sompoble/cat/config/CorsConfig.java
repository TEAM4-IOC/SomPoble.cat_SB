package com.sompoble.cat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing) para la
 * aplicación.
 * <p>
 * Esta clase define los orígenes, métodos y cabeceras permitidas para las
 * peticiones HTTP que se realizan desde dominios distintos al backend.
 * </p>
 */
@Configuration
public class CorsConfig {

    /**
     * Define y registra un filtro de CORS para permitir el acceso controlado
     * desde aplicaciones frontend.
     *
     * @return una instancia de {@link CorsFilter} con la configuración
     * personalizada.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("http://localhost:4200")); //TODO: cambiar cuando lo tengamos definido
        config.setAllowedHeaders(List.of("*")); // TODO: ver si lo tenemos que cambiar o que hacemos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
