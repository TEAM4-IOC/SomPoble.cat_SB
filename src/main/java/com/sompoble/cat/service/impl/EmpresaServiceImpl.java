package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmpresaServiceImpl implements EmpresaService {

     @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public EmpresaDTO findByIdentificadorFiscal(String identificadorFiscal) {
        return empresaRepository.findByIdentificadorFiscal(identificadorFiscal);
    }
    
    @Override
    public Empresa findByIdentificadorFiscalFull(String identificadorFiscal) {
        return empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal);
    }
    
    @Override
    public void updateEmpresa(Empresa empresa) {
        empresaRepository.updateEmpresa(empresa);
    }

    @Override
    public void addEmpresa(Empresa empresa) {
        empresaRepository.addEmpresa(empresa);
    }

    @Override
    public List<EmpresaDTO> findAll() {
        return empresaRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return empresaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }

    @Override
    public boolean existsByIdentificadorFiscal(String identificadorFiscal) {
        return empresaRepository.existsByIdentificadorFiscal(identificadorFiscal);
    }

    @Override
    public void deleteByIdentificadorFiscal(String identificadorFiscal) {
        empresaRepository.deleteByIdentificadorFiscal(identificadorFiscal);
    }
}