package com.sompoble.cat.repository;

import java.util.List;

import com.sompoble.cat.domain.Cliente;

public interface ClienteRepository {
    Cliente findByDNI(String dni);
    
    void updateCliente(Cliente cliente);
    
    void addCliente(Cliente cliente);

    List<Cliente> findAll();
    
    boolean existsById(Long id);

    void deleteById(Long id);
    
    boolean existsByDni(String dni);

    void deleteByDni(String dni);
    
    boolean existsByEmail(String email);

	Cliente findByEmail(String email);
}