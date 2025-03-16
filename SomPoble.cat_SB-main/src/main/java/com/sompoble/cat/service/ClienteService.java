package com.sompoble.cat.service;

import com.sompoble.cat.domain.Cliente;
import java.util.List;

public interface ClienteService {
    Cliente findByDni(String dni);
    
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