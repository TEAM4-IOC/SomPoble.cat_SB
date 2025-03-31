package com.sompoble.cat.repository;

import java.util.List;

import com.sompoble.cat.domain.Empresario;

public interface EmpresarioRepository {
    Empresario findByDNI(String dni);
    
    void updateEmpresario (Empresario empresario);
    
    void addEmpresario(Empresario empresario);

    List<Empresario> findAll();
    
    boolean existsById(Long id);

    void deleteById(Long id);
    
    boolean existsByDni(String dni);

    void deleteByDni(String dni);
    
    boolean existsByEmail(String email);
    
    Empresario findByEmail(String email);

	
}