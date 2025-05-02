package com.sompoble.cat.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Clase de configuración para el servicio de correo electrónico. Esta clase
 * proporciona la configuración necesaria para enviar correos electrónicos a
 * través de un servidor SMTP de Gmail.
 */
@Configuration
@PropertySource(value = {"classpath:email.properties"})
public class EmailConfig {

    /**
     * El nombre de usuario del correo electrónico desde las propiedades de la
     * aplicación.
     */
    @Value("${email.username}")
    private String email;

    /**
     * La contraseña del correo electrónico desde las propiedades de la
     * aplicación.
     */
    @Value("${email.password}")
    private String password;

    /**
     * Configura las propiedades necesarias para la conexión SMTP.
     *
     * @return Un objeto Properties con la configuración del servidor SMTP de
     * Gmail.
     */
    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        return properties;
    }

    /**
     * Crea y configura un bean JavaMailSender para el envío de correos
     * electrónicos.
     *
     * @return Una instancia configurada de JavaMailSender lista para enviar
     * correos.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(getMailProperties());
        mailSender.setUsername(email);
        mailSender.setPassword(password);
        return mailSender;
    }

    /**
     * Crea un bean ResourceLoader para cargar recursos.
     *
     * @return Una instancia de DefaultResourceLoader para acceder a recursos.
     */
    @Bean
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }
}
