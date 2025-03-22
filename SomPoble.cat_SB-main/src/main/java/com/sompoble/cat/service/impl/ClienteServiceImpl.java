package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.repository.ClienteRepository;
import com.sompoble.cat.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente findByDni(String dni) {
        return clienteRepository.findByDNI(dni);
    }

    @Override
    public void updateCliente(Cliente cliente) {
        clienteRepository.updateCliente(cliente);
    }

    @Override
    public void addCliente(Cliente cliente) {
        clienteRepository.addCliente(cliente);
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public boolean existsByDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }

    @Override
    public void deleteByDni(String dni) {
        clienteRepository.deleteByDni(dni);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
    
    @Override
    public Cliente findByEmail(String email) {
            return clienteRepository.findByEmail(email);
    }
}