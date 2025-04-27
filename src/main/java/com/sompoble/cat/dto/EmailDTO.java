package com.sompoble.cat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO que representa los datos necesarios para enviar un correo electrónico.
 * Incluye el destinatario, el asunto y el mensaje del correo.
 * 
 * Se utiliza en el controlador {@link com.sompoble.cat.controller.EmailController}
 * para recibir datos desde el cliente de forma segura y validada.
 */
public class EmailDTO {

    /**
     * Dirección de correo electrónico del destinatario.
     * Debe tener un formato válido y no puede estar vacío.
     */
    @NotBlank(message = "El destinatario no puede estar vacío")
    @Email(message = "El formato del correo electrónico no es válido")
    private String destinatario;

    /**
     * Asunto del correo electrónico.
     * No puede estar vacío.
     */
    @NotBlank(message = "El asunto no puede estar vacío")
    private String asunto;

    /**
     * Cuerpo del mensaje del correo electrónico.
     * No puede estar vacío.
     */
    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    /**
     * Obtiene la dirección de correo electrónico del destinatario.
     * @return destinatario
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Establece la dirección de correo electrónico del destinatario.
     * @param destinatario Dirección de correo válida
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Obtiene el asunto del correo electrónico.
     * @return asunto del correo
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Establece el asunto del correo electrónico.
     * @param asunto Texto breve que describe el propósito del correo
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Obtiene el mensaje del correo electrónico.
     * @return contenido del mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el mensaje del correo electrónico.
     * @param mensaje Contenido del correo en texto plano o HTML
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
