package com.sompoble.cat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "CLIENTE")
public class Cliente extends Persona {
   
    @OneToMany(mappedBy="cliente")
    private List<Reserva> reservas;
    
    @OneToMany(mappedBy = "cliente")
    private List<Notificacion> notificaciones;

    public Cliente() {
    }

    public Cliente(String dni, String nombre, String apellidos, String email, String telefono, String contraseña) {
        super(dni, nombre, apellidos, email, telefono, contraseña);
    }
    
    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    
    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    } 
}