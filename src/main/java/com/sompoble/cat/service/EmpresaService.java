package com.sompoble.cat.service;

import java.util.List;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.EmpresaDTO;

public interface EmpresaService {
    EmpresaDTO findByIdentificadorFiscal(String identificadorFiscal);

    Empresa findByIdentificadorFiscalFull(String identificadorFiscal);

    void updateEmpresa(Empresa empresa);

    void addEmpresa(Empresa empresa);

    List<EmpresaDTO> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);

    boolean existsByIdentificadorFiscal(String identificadorFiscal);

    void deleteByIdentificadorFiscal(String identificadorFiscal);


}
