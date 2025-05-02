package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.repository.ClienteRepository;
import com.sompoble.cat.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementación de la interfaz {@link ClienteService}.
 * <p>
 * Esta clase proporciona la implementación concreta de los métodos definidos en
 * la interfaz ClienteService, gestionando las operaciones relacionadas con los
 * clientes a través del repositorio correspondiente.
 * </p>
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    /**
     * Repositorio para acceder a los datos de los clientes.
     */
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Cliente findByDniFull(String dni) {
        return clienteRepository.findByDNIFull(dni);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClienteDTO findByDni(String dni) {
        return clienteRepository.findByDNI(dni);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCliente(Cliente cliente) {
        clienteRepository.updateCliente(cliente);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCliente(Cliente cliente) {
        clienteRepository.addCliente(cliente);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByDni(String dni) {
        clienteRepository.deleteByDni(dni);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClienteDTO findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
}
