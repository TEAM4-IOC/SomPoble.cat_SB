package com.sompoble.cat.service;
import java.util.List;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;

public interface ClienteService {
    Cliente findByDniFull(String dni);

    ClienteDTO findByDni(String dni);

    void updateCliente(Cliente cliente);

    void addCliente(Cliente cliente);

    List<ClienteDTO> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);

    boolean existsByDni(String dni);

    void deleteByDni(String dni);

    boolean existsByEmail(String email);

    ClienteDTO findByEmail(String email);
}