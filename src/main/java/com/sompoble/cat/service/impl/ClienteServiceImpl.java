package com.sompoble.cat.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.repository.ClienteRepository;
import com.sompoble.cat.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente findByDniFull(String dni) {
        return clienteRepository.findByDNIFull(dni);
    }

    @Override
    public ClienteDTO findByDni(String dni) {
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
    public List<ClienteDTO> findAll() {
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
    public ClienteDTO findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
}