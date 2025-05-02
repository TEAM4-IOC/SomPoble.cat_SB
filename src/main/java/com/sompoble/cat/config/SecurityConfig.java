package com.sompoble.cat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para la aplicación.
 * <p>
 * Esta clase define cómo se gestionan las solicitudes HTTP en términos de
 * seguridad, incluyendo la desactivación del CSRF y la codificación de
 * contraseñas.
 * </p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad para las peticiones HTTP.
     * <p>
     * Actualmente se permite el acceso a todas las rutas y se desactiva la
     * protección CSRF. Esta configuración es útil durante el desarrollo, pero
     * debe ser revisada para producción.
     * </p>
     *
     * @param http el objeto {@link HttpSecurity} proporcionado por Spring
     * Security.
     * @return una instancia de {@link SecurityFilterChain} con la configuración
     * aplicada.
     * @throws Exception en caso de error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //TODO: valorar
                .authorizeHttpRequests(auth -> auth //TODO: valorar
                .requestMatchers("/**").permitAll() //TODO: valorar
                );
        return http.build();
    }

    /**
     * Proveedor de codificación de contraseñas que utiliza el algoritmo BCrypt.
     * <p>
     * Este bean se utiliza para cifrar las contraseñas de los usuarios antes de
     * almacenarlas en la base de datos.
     * </p>
     *
     * @return una instancia de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
