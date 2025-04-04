package com.sompoble.cat.service;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresarioDTO;
import java.util.List;

public interface EmpresarioService {
    EmpresarioDTO findByDni(String dni);
    
    Empresario findEmpresarioByDNI(String dni);
    
    void updateEmpresario(EmpresarioDTO empresario);
    
    void addEmpresario(Empresario empresario);

    List<EmpresarioDTO> findAll();
    
    boolean existsById(Long id);

    void deleteById(Long id);
    
    boolean existsByDni(String dni);

    void deleteByDni(String dni);
    
    boolean existsByEmail(String email);
    
    Empresario findByEmail(String email);
  
    EmpresarioDTO findByEmail(String email);
}