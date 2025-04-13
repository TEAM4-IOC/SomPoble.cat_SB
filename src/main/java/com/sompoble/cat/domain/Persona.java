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

@Entity
@Inheritance (strategy=InheritanceType.TABLE_PER_CLASS)
public class Persona implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID_PERSONA")
    private Long idPersona;
    
    @Column(name = "DNI", nullable = false, length = 9) 
    @NotNull(message = "El DNI es obligatorio")
    @Size(min = 9, max = 9, message = "El campo no puede exceder los 9 caracteres")
    private String dni;
    
    @Column(name = "NOMBRE", nullable = false, length = 100) 
    @NotNull(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String nombre;
    
    @Column(name = "APELLIDOS", nullable = false, length = 100) 
    @NotNull(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String apellidos;
    
    @Column(name = "EMAIL", nullable = false, length = 100) 
    @NotNull(message = "El email es obligatorio")
    @Size(max = 100, message = "El campo no puede exceder los 100 caracteres")
    private String email;
    
    @Column(name = "TELEFONO", nullable = false, length = 20) 
    @NotNull(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El campo no puede exceder los 20 caracteres")
    private String telefono;   
    
    @Column(name = "PASSWRD", nullable = false, length = 255) 
    @NotNull(message = "El password es obligatorio")
    @Size(max = 255, message = "El campo no puede exceder los 255 caracteres")
    private String pass;
    
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false,columnDefinition = "DATETIME")
    @CreationTimestamp
    private LocalDateTime fechaAlta;

    @Column(name = "FECHA_MODIFICACION", nullable = false,columnDefinition = "DATETIME")
    @UpdateTimestamp
    private LocalDateTime fechaModificacion;
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public Persona() {
    }

    public Persona(String dni, String nombre, String apellidos, String email, String telefono, String pass) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email.toLowerCase();
        this.telefono = telefono;
        this.pass = encoder.encode(pass);
    }
    
    public Long getIdPersona() {
        return idPersona;
    }
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = encoder.encode(pass);
    }
    
    public LocalDateTime getFechaAlta() {
       return fechaAlta;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
} 