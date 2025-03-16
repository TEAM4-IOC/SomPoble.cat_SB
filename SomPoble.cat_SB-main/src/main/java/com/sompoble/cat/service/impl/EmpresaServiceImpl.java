package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Empresa;
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
    public Empresa findByCif(String cif) {
        return empresaRepository.findByCif(cif);
    }

    @Override
    public void updateEmpresa(Empresa empresa) {
        empresaRepository.updateEmpresa(empresa);
    }

    @Override
    public void addEmpresario(Empresa empresa) {
        empresaRepository.addEmpresario(empresa);
    }

    @Override
    public List<Empresa> findAll() {
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
    public boolean existsByCif(String cif) {
        return empresaRepository.existsByCif(cif);
    }

    @Override
    public void deleteByCif(String cif) {
        empresaRepository.deleteByCif(cif);
    }
}