package com.sompoble.cat.service;

import com.sompoble.cat.domain.Empresario;
import java.util.List;

public interface EmpresarioService {
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