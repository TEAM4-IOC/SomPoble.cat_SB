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
    @NotNull
    @Size(min = 9, max = 9)
    private String dni;
    
    @Column(name = "NOMBRE", nullable = false, length = 100) 
    @NotNull
    @Size(max = 100)
    private String nombre;
    
    @Column(name = "APELLIDOS", nullable = false, length = 100) 
    @NotNull
    @Size(max = 100)
    private String apellidos;
    
    @Column(name = "EMAIL", nullable = false, length = 100) 
    @NotNull
    @Size(max = 100)
    private String email;
    
    @Column(name = "TELEFONO", nullable = false, length = 20) 
    @NotNull
    @Size(max = 20)
    private String telefono;   
    
    @Column(name = "CONTRASEÑA", nullable = false, length = 255) 
    @NotNull
    @Size(max = 255)
    private String contraseña;
    
    @Column(name = "FECHA_ALTA", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime fechaAlta;

    @Column(name = "FECHA_MODIFICACION", nullable = false)
    @UpdateTimestamp
    private LocalDateTime fechaModificacion;

    public Persona() {
    }

    public Persona(String dni, String nombre, String apellidos, String email, String telefono, String contraseña) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.contraseña = contraseña;
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
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.contraseña = encoder.encode(contraseña);
    }
    
    public LocalDateTime getFechaAlta() {
       return fechaAlta;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
} 