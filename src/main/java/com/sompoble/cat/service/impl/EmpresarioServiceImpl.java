package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.repository.EmpresarioRepository;
import com.sompoble.cat.service.EmpresarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementación de la interfaz {@link EmpresarioService}.
 * <p>
 * Esta clase proporciona la implementación concreta de los métodos
 * definidos en la interfaz EmpresarioService, gestionando las operaciones
 * relacionadas con los empresarios a través del repositorio correspondiente.
 * </p>
 */
@Service
public class EmpresarioServiceImpl implements EmpresarioService {
    
    /**
     * Repositorio para acceder a los datos de los empresarios.
     */
    @Autowired
    private EmpresarioRepository empresarioRepository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EmpresarioDTO findByDni(String dni) {
        return empresarioRepository.findByDNI(dni);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Empresario findEmpresarioByDNI(String dni) {
        return empresarioRepository.findEmpresarioByDNI(dni);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEmpresario(EmpresarioDTO empresario) {
        empresarioRepository.updateEmpresario(empresario);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addEmpresario(Empresario empresario) {
        empresarioRepository.addEmpresario(empresario);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmpresarioDTO> findAll() {
        return empresarioRepository.findAll();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return empresarioRepository.existsById(id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        empresarioRepository.deleteById(id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByDni(String dni) {
        return empresarioRepository.existsByDni(dni);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByDni(String dni) {
        empresarioRepository.deleteByDni(dni);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByEmail(String email) {
        return empresarioRepository.existsByEmail(email);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public EmpresarioDTO findByEmail(String email) {
        return empresarioRepository.findByEmail(email);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Empresario findByEmailFull(String email) {
        return empresarioRepository.findByEmailFull(email);
    }
}