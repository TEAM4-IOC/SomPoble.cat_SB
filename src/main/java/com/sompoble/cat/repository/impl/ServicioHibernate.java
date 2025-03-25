package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.repository.ServicioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de {@link ServicioRepository} utilizando Hibernate con Criteria API.
 */
@Repository
@Transactional
public class ServicioHibernate implements ServicioRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Busca un servicio por su identificador único.
     *
     * @param id Identificador del servicio.
     * @return El servicio encontrado o {@code null} si no existe.
     */
    @Override
    public Servicio findById(Long id) {
        return entityManager.find(Servicio.class, id);
    }

    /**
     * Actualiza un servicio existente en la base de datos.
     *
     * @param servicio Objeto {@link Servicio} con la información actualizada.
     */
    @Override
    public void updateServicio(Servicio servicio) {
        entityManager.merge(servicio);
    }

    /**
     * Agrega un nuevo servicio a la base de datos.
     *
     * @param servicio Objeto {@link Servicio} que se va a persistir.
     */
    @Override
    public void addServicio(Servicio servicio) {
        entityManager.persist(servicio);
    }

    /**
     * Obtiene todos los servicios asociados a una empresa específica por su ID.
     *
     * @param empresaId Identificador de la empresa.
     * @return Lista de servicios pertenecientes a la empresa indicada.
     */
    @Override
    public List<Servicio> findAllByEmpresaId(Long empresaId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Servicio> cq = cb.createQuery(Servicio.class);
        Root<Servicio> root = cq.from(Servicio.class);

        Predicate empresaPredicate = cb.equal(root.get("empresa").get("id"), empresaId);
        cq.where(empresaPredicate);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Obtiene todos los servicios asociados a una empresa específica por su identificador fiscal.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return Lista de servicios pertenecientes a la empresa con el identificador fiscal dado.
     */
    @Override
    public List<Servicio> findAllByEmpresaIdentificador(String identificadorFiscal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Servicio> cq = cb.createQuery(Servicio.class);
        Root<Servicio> root = cq.from(Servicio.class);

        Predicate identificadorPredicate = cb.equal(root.get("empresa").get("identificadorFiscal"), identificadorFiscal);
        cq.where(identificadorPredicate);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Verifica si existe un servicio con el identificador especificado.
     *
     * @param id Identificador del servicio.
     * @return {@code true} si el servicio existe, {@code false} en caso contrario.
     */
    @Override
    public boolean existsById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Servicio> root = cq.from(Servicio.class);

        Predicate idPredicate = cb.equal(root.get("id"), id);
        cq.select(cb.count(root)).where(idPredicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    /**
     * Elimina un servicio por su identificador.
     *
     * @param id Identificador del servicio a eliminar.
     */
    @Override
    public void deleteById(Long id) {
        Servicio servicio = entityManager.find(Servicio.class, id);
        if (servicio != null) {
            entityManager.remove(servicio);
        }
    }
}