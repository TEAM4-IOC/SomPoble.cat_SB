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

/**
 * Implementación de {@link ClienteRepository} utilizando Hibernate como
 * proveedor JPA.
 * <p>
 * Esta clase proporciona la implementación de todos los métodos definidos en la
 * interfaz ClienteRepository utilizando Criteria API de JPA para realizar
 * operaciones sobre la entidad Cliente en la base de datos.
 * </p>
 */
@Repository
@Transactional
public class ClienteHibernate implements ClienteRepository {

    /**
     * EntityManager para gestionar las operaciones de persistencia.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCliente(Cliente cliente) {
        entityManager.persist(cliente);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCliente(Cliente cliente) {
        entityManager.merge(cliente);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClienteDTO> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);

        List<Cliente> clientes = entityManager.createQuery(cq).getResultList();
        return clientes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente != null) {
            entityManager.remove(cliente);
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Convierte una entidad Cliente a su representación DTO.
     * <p>
     * Este método extrae la información relevante de la entidad Cliente y crea
     * un objeto DTO que puede ser transferido a través de capas.
     * </p>
     *
     * @param cliente La entidad Cliente a convertir.
     * @return Un objeto ClienteDTO con la información del cliente.
     */
    private ClienteDTO convertToDTO(Cliente cliente) {
        List<Long> reservasIds = new ArrayList<>();
        List<Long> notificacionesIds = new ArrayList<>();

        if (cliente.getReservas() != null) {
            reservasIds = cliente.getReservas().stream()
                    .map(reserva -> reserva.getIdReserva())
                    .collect(Collectors.toList());
        }

        if (cliente.getNotificaciones() != null) {
            notificacionesIds = cliente.getNotificaciones().stream()
                    .map(notificacion -> notificacion.getIdNotificacion())
                    .collect(Collectors.toList());
        }

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

    /**
     * Convierte un objeto ClienteDTO a una entidad Cliente.
     * <p>
     * Este método crea una nueva entidad Cliente y la completa con la
     * información contenida en el DTO proporcionado.
     * </p>
     *
     * @param clienteDTO El DTO que contiene la información del cliente.
     * @return Una entidad Cliente con la información del DTO.
     */
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
