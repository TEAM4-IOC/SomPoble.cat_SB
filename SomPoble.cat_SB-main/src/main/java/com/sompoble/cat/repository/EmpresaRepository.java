package com.sompoble.cat.repository;

import com.sompoble.cat.domain.Empresa;
import java.util.List;

public interface EmpresaRepository {
    Empresa findByCif(String cif);
    
    void updateEmpresa (Empresa empresa);
    
    void addEmpresario(Empresa empresa);

    List<Empresa> findAll();
    
    boolean existsById(Long id);

    void deleteById(Long id);
    
    boolean existsByCif(String cif);

    void deleteByCif(String cif);
}
