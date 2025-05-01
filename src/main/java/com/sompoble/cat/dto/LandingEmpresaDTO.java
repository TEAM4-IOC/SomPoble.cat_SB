package com.sompoble.cat.dto;

import java.util.List;

public class LandingEmpresaDTO {

    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String imagen;
    private String identificadorFiscal; // Necesario para relacionar con servicios
    private List<LandingServicioDTO> servicios;

    // Constructor completo
    public LandingEmpresaDTO(String nombre, String direccion, String telefono,
            String email, String imagen, String identificadorFiscal,
            List<LandingServicioDTO> servicios) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.imagen = imagen;
        this.identificadorFiscal = identificadorFiscal;
        this.servicios = servicios;
    }

    // Constructor vacío requerido para la serialización/deserialización
    public LandingEmpresaDTO() {
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getIdentificadorFiscal() {
        return identificadorFiscal;
    }

    public void setIdentificadorFiscal(String identificadorFiscal) {
        this.identificadorFiscal = identificadorFiscal;
    }

    public List<LandingServicioDTO> getServicios() {
        return servicios;
    }

    public void setServicios(List<LandingServicioDTO> servicios) {
        this.servicios = servicios;
    }
}
