package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementación de la interfaz {@link EmpresaService}.
 * <p>
 * Esta clase proporciona la implementación concreta de los métodos
 * definidos en la interfaz EmpresaService, gestionando las operaciones
 * relacionadas con las empresas a través del repositorio correspondiente.
 * </p>
 */
@Service
public class EmpresaServiceImpl implements EmpresaService {
    
    /**
     * Repositorio para acceder a los datos de las empresas.
     */
    @Autowired
    private EmpresaRepository empresaRepository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EmpresaDTO findByIdentificadorFiscal(String identificadorFiscal) {
        return empresaRepository.findByIdentificadorFiscal(identificadorFiscal);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Empresa findByIdentificadorFiscalFull(String identificadorFiscal) {
        return empresaRepository.findByIdentificadorFiscalFull(identificadorFiscal);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEmpresa(Empresa empresa) {
        empresaRepository.updateEmpresa(empresa);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addEmpresa(Empresa empresa) {
        empresaRepository.addEmpresa(empresa);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmpresaDTO> findAll() {
        return empresaRepository.findAll();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return empresaRepository.existsById(id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByIdentificadorFiscal(String identificadorFiscal) {
        return empresaRepository.existsByIdentificadorFiscal(identificadorFiscal);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByIdentificadorFiscal(String identificadorFiscal) {
        empresaRepository.deleteByIdentificadorFiscal(identificadorFiscal);
    }
}