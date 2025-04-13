package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.repository.EmpresarioRepository;
import com.sompoble.cat.service.EmpresarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpresarioServiceImpl implements EmpresarioService {

    @Autowired
    private EmpresarioRepository empresarioRepository;

    @Override
    public EmpresarioDTO findByDni(String dni) {
        return empresarioRepository.findByDNI(dni);
    }
    
    @Override
    public Empresario findEmpresarioByDNI(String dni) {
        return empresarioRepository.findEmpresarioByDNI(dni);
    }

    @Override
    public void updateEmpresario(EmpresarioDTO empresario) {
        empresarioRepository.updateEmpresario(empresario);
    }

    @Override
    public void addEmpresario(Empresario empresario) {
        empresarioRepository.addEmpresario(empresario);
    }

    @Override
    public List<EmpresarioDTO> findAll() {
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
    public EmpresarioDTO findByEmail(String email) {
        return empresarioRepository.findByEmail(email);
    }
    
    @Override
    public Empresario findByEmailFull(String email) {
            return empresarioRepository.findByEmailFull(email);
    }

	
}