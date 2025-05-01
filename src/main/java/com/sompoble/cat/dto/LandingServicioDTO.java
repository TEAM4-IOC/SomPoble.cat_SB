package com.sompoble.cat.dto;

public class LandingServicioDTO {

    private String nombre;

    public LandingServicioDTO(String nombre) {
        this.nombre = nombre;
    }

    public LandingServicioDTO() {
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
}