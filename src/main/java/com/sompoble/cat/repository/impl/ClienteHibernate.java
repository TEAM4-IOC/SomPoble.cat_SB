package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.repository.ClienteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
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
    public Cliente findByDNI(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.where(dniPredicate);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public List<Cliente> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
        Root<Cliente> root = cq.from(Cliente.class);

        return entityManager.createQuery(cq).getResultList();
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
        Cliente cliente = findByDNI(dni);
        if (cliente != null) {
            entityManager.remove(cliente);
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
}