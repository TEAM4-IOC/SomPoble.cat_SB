package com.sompoble.cat.service;

import java.util.List;

import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresarioDTO;

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

    Empresario findByEmailFull(String email);

    EmpresarioDTO findByEmail(String email);


}