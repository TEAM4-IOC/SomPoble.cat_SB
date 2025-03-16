package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class EmpresaHibernate implements EmpresaRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void addEmpresario(Empresa empresa) {
        entityManager.persist(empresa);
    }

    @Override
    public void updateEmpresa(Empresa empresa) {
        entityManager.merge(empresa);
    }

    @Override
    public Empresa findByCif(String cif) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresa> cq = cb.createQuery(Empresa.class);
        Root<Empresa> root = cq.from(Empresa.class);

        Predicate cifPredicate = cb.equal(root.get("cif"), cif);
        cq.where(cifPredicate);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public List<Empresa> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresa> cq = cb.createQuery(Empresa.class);
        Root<Empresa> root = cq.from(Empresa.class);
        
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Empresa empresa = entityManager.find(Empresa.class, id);
        if (empresa != null) {
            entityManager.remove(empresa);
        }
    }

    @Override
    public void deleteByCif(String cif) {
        Empresa empresa = findByCif(cif);  // Puedes reutilizar la consulta findByCif
        if (empresa != null) {
            entityManager.remove(empresa);
        }
    }

    @Override
    public boolean existsByCif(String cif) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Empresa> root = cq.from(Empresa.class);

        Predicate cifPredicate = cb.equal(root.get("cif"), cif);
        cq.select(cb.count(root)).where(cifPredicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Empresa> root = cq.from(Empresa.class);

        Predicate idPredicate = cb.equal(root.get("id"), id);
        cq.select(cb.count(root)).where(idPredicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }
}
