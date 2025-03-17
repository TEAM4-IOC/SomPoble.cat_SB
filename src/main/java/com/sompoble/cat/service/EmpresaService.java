package com.sompoble.cat.service;

import com.sompoble.cat.domain.Empresa;
import java.util.List;

public interface EmpresaService {
    Empresa findByIdentificadorFiscal(String identificadorFiscal);
    
    void updateEmpresa(Empresa empresa);
    
    void addEmpresario(Empresa empresa);

    List<Empresa> findAll();
    
    boolean existsById(Long id);

    void deleteById(Long id);
    
    boolean existsByIdentificadorFiscal(String identificadorFiscal);

    void deleteByIdentificadorFiscal(String identificadorFiscal);
}
