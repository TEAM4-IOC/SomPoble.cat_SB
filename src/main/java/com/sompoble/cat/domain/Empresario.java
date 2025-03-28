package com.sompoble.cat.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "EMPRESARIO")
public class Empresario extends Persona{
    
    @OneToOne(mappedBy = "empresario")
    @JsonManagedReference 
    private Empresa empresas;
    
    @OneToMany(mappedBy = "empresario")
    private List<Notificacion> notificaciones;

    public Empresario() {
    }

    public Empresario(String dni, String nombre, String apellidos, String email, String telefono, String pass) {
        super(dni, nombre, apellidos, email, telefono, pass);
    }
    
    public Empresa getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Empresa empresas) {
        this.empresas = empresas;
    }
    
    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
}