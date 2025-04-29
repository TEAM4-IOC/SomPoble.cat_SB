package com.sompoble.cat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * Representa una persona en el sistema.
 * <p>
 * La clase Persona es la clase base de la cual pueden heredar otros tipos de personas como clientes o empresarios.
 * </p>
 */
@Entity
@Inheritance (strategy=InheritanceType.TABLE_PER_CLASS)
public class Persona implements Serializable {
    /**
     * Identificador único de la persona.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID_PERSONA")
    private Long idPersona;
    /**
     * Número de identificación de la persona (DNI).
     * Este campo es obligatorio y debe tener una longitud de exactamente 9 caracteres.
     */
    @Column(name = "DNI", nullable = false, length = 9) 
    @NotNull(message = "El DNI es obligatorio")
    @Size(min = 9, max = 9, message = "El campo no puede exceder los 9 caracteres")
    private String dni;
    /**
     * Nombre de la persona.
     * Este campo es obligatorio y no puede exceder los 100 caracteres.
     */
    @Column(name = "NOMBRE", nullable = false, length = 100) 
    @NotNull(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String nombre;
    /**
     * Apellidos de la persona.
     * Este campo es obligatorio y no puede exceder los 100 caracteres.
     */
    @Column(name = "APELLIDOS", nullable = false, length = 100) 
    @NotNull(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String apellidos;
    /**
     * Correo electrónico de la persona.
     * Este campo es obligatorio y no puede exceder los 100 caracteres.
     */
    @Column(name = "EMAIL", nullable = false, length = 100) 
    @NotNull(message = "El email es obligatorio")
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String email;
    /**
     * Teléfono de la persona.
     * Este campo es obligatorio y no puede exceder los 20 caracteres.
     */
    @Column(name = "TELEFONO", nullable = false, length = 20) 
    @NotNull(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El campo no puede exceder los 20 caracteres")
    private String telefono;   
    /**
     * Contraseña de la persona, la cual está cifrada.
     * Este campo es obligatorio y no puede exceder los 255 caracteres.
     */
    @Column(name = "PASSWRD", nullable = false, length = 255) 
    @NotNull(message = "El password es obligatorio")
    @Size(max = 255, message = "El campo no puede exceder los 255 caracteres")
    private String pass;
    /**
     * Fecha en la que se registró la persona.
     * Este campo es generado automáticamente al momento de crear la persona.
     */
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false,columnDefinition = "DATETIME")
    @CreationTimestamp
    private LocalDateTime fechaAlta;
    /**
     * Fecha en la que se modificó por última vez la persona.
     * Este campo es generado automáticamente cada vez que se actualiza la persona.
     */
    @Column(name = "FECHA_MODIFICACION", nullable = false,columnDefinition = "DATETIME")
    @UpdateTimestamp
    private LocalDateTime fechaModificacion;
    /**
     * Instancia del codificador de contraseñas (BCryptPasswordEncoder) para encriptar contraseñas.
     */
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    /**
     * Constructor vacío necesario para la persistencia.
     */
    public Persona() {
    }
    /**
     * Constructor con parámetros para inicializar una persona con los datos proporcionados.
     * 
     * @param dni El DNI de la persona.
     * @param nombre El nombre de la persona.
     * @param apellidos Los apellidos de la persona.
     * @param email El correo electrónico de la persona.
     * @param telefono El número de teléfono de la persona.
     * @param pass La contraseña de la persona.
     */
    public Persona(String dni, String nombre, String apellidos, String email, String telefono, String pass) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email.toLowerCase();
        this.telefono = telefono;
        this.pass = encoder.encode(pass);
    }
    /**
     * Obtiene el identificador único de la persona.
     * 
     * @return El identificador único de la persona.
     */
    public Long getIdPersona() {
        return idPersona;
    }
    /**
     * Obtiene el DNI de la persona.
     * 
     * @return El DNI de la persona.
     */
    public String getDni() {
        return dni;
    }
    /**
     * Establece el DNI de la persona.
     * 
     * @param dni El DNI de la persona.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }
    /**
     * Obtiene el nombre de la persona.
     * 
     * @return El nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Establece el nombre de la persona.
     * 
     * @param nombre El nombre de la persona.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * Obtiene los apellidos de la persona.
     * 
     * @return Los apellidos de la persona.
     */
    public String getApellidos() {
        return apellidos;
    }
    /**
     * Establece los apellidos de la persona.
     * 
     * @param apellidos Los apellidos de la persona.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    /**
     * Obtiene el correo electrónico de la persona.
     * 
     * @return El correo electrónico de la persona.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Establece el correo electrónico de la persona.
     * 
     * @param email El correo electrónico de la persona.
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
    /**
     * Obtiene el número de teléfono de la persona.
     * 
     * @return El número de teléfono de la persona.
     */
    public String getTelefono() {
        return telefono;
    }
    /**
     * Establece el número de teléfono de la persona.
     * 
     * @param telefono El número de teléfono de la persona.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    /**
     * Obtiene la contraseña cifrada de la persona.
     * 
     * @return La contraseña cifrada de la persona.
     */
    public String getPass() {
        return pass;
    }
    /**
     * Establece la contraseña de la persona.
     * La contraseña será cifrada antes de ser guardada.
     * 
     * @param pass La nueva contraseña de la persona.
     */
    public void setPass(String pass) {
        this.pass = encoder.encode(pass);
    }
    /**
     * Obtiene la fecha en la que se registró la persona.
     * 
     * @return La fecha en la que se registró la persona.
     */
    
    public LocalDateTime getFechaAlta() {
       return fechaAlta;
    }
    /**
     * Obtiene la fecha en la que se modificó por última vez la persona.
     * 
     * @return La fecha de la última modificación de la persona.
     */
    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
} 