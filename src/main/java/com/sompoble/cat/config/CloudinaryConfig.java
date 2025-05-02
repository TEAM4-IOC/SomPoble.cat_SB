package com.sompoble.cat.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase de configuración para la integración con Cloudinary. Esta clase
 * proporciona la configuración para el cliente de Cloudinary, que se utiliza
 * para servicios de gestión de imagenes en la nube.
 */
@Configuration
public class CloudinaryConfig {

    /**
     * El nombre de la nube de Cloudinary desde las propiedades de la
     * aplicación.
     */
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    /**
     * La clave API de Cloudinary desde las propiedades de la aplicación.
     */
    @Value("${cloudinary.api-key}")
    private String apiKey;

    /**
     * El secreto API de Cloudinary desde las propiedades de la aplicación.
     */
    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    /**
     * Crea y configura un bean de Cloudinary.
     *
     * @return Una instancia configurada de Cloudinary lista para su uso en la
     * aplicación.
     */
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }
}
