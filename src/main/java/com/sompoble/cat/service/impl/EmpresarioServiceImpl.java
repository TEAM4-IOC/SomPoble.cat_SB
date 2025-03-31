package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.repository.EmpresarioRepository;
import com.sompoble.cat.service.EmpresarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmpresarioServiceImpl implements EmpresarioService {

    @Autowired
    private EmpresarioRepository empresarioRepository;

    @Override
    public Empresario findByDNI(String dni) {
        return empresarioRepository.findByDNI(dni);
    }

    @Override
    public void updateEmpresario(Empresario empresario) {
        empresarioRepository.updateEmpresario(empresario);
    }

    @Override
    public void addEmpresario(Empresario empresario) {
        empresarioRepository.addEmpresario(empresario);
    }

    @Override
    public List<Empresario> findAll() {
        return empresarioRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return empresarioRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        empresarioRepository.deleteById(id);
    }

    @Override
    public boolean existsByDni(String dni) {
        return empresarioRepository.existsByDni(dni);
    }

    @Override
    public void deleteByDni(String dni) {
        empresarioRepository.deleteByDni(dni);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return empresarioRepository.existsByEmail(email);
    }
    
    @Override
    public Empresario findByEmail(String email) {
            return empresarioRepository.findByEmail(email);
    }

	
}