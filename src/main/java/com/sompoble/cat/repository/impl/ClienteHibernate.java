package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.repository.ClienteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClienteHibernate implements ClienteRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void addCliente(Cliente cliente) {
        entityManager.persist(cliente);
    }

    @Override
    public void updateCliente(Cliente cliente) {
        entityManager.merge(cliente);
    }

    @Override
    public ClienteDTO findByDNI(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.where(dniPredicate);

        List<Cliente> result = entityManager.createQuery(cq).getResultList();

        if (result.isEmpty()) {
            return null;
        } else {
            Cliente cliente = result.get(0);
            return convertToDTO(cliente);
        }
    }
    
    @Override
    public Cliente findByDNIFull(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.where(dniPredicate);

        List<Cliente> result = entityManager.createQuery(cq).getResultList();

        if (result.isEmpty()) {
            return null;
        } else {
            Cliente cliente = result.get(0);
            return cliente;
        }
    }

    @Override
    public List<ClienteDTO> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);

        List<Cliente> clientes = entityManager.createQuery(cq).getResultList();
        return clientes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente != null) {
            entityManager.remove(cliente);
        }
    }

    @Override
    public void deleteByDni(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.where(dniPredicate);

        List<Cliente> result = entityManager.createQuery(cq).getResultList();
        
        if (!result.isEmpty()) {
            entityManager.remove(result.get(0));
        }
    }

    @Override
    public boolean existsByDni(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Cliente> root = cq.from(Cliente.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.select(cb.count(root)).where(dniPredicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Cliente> root = cq.from(Cliente.class);

        Predicate idPredicate = cb.equal(root.get("id"), id);
        cq.select(cb.count(root)).where(idPredicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Cliente> root = cq.from(Cliente.class);

        Predicate emailPredicate = cb.equal(root.get("email"), email);
        cq.select(cb.count(root)).where(emailPredicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    @Override
    public ClienteDTO findByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);

        Predicate emailPredicate = cb.equal(root.get("email"), email);
        cq.where(emailPredicate);

        List<Cliente> result = entityManager.createQuery(cq).getResultList();
        
        if (result.isEmpty()) {
            return null;
        } else {
            Cliente cliente = result.get(0);
            return convertToDTO(cliente);
        }
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        List<Long> reservasIds = new ArrayList<>();
        List<Long> notificacionesIds = new ArrayList<>();

        return new ClienteDTO(
                cliente.getIdPersona(),
                cliente.getDni(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getPass(),
                reservasIds,
                notificacionesIds
        );
    }
    
    public Cliente convertToEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();

        cliente.setDni(clienteDTO.getDni());
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setApellidos(clienteDTO.getApellidos());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setPass(clienteDTO.getPass());
        
        return cliente;
    }
    
}