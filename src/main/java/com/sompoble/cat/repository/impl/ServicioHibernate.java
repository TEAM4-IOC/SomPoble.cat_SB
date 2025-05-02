package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.repository.ServicioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de {@link ServicioRepository} utilizando Hibernate con
 * Criteria API.
 * <p>
 * Esta clase proporciona la implementación concreta de las operaciones de
 * acceso a datos definidas en la interfaz ServicioRepository, utilizando
 * JPA/Hibernate y Criteria API para interactuar con la base de datos.
 * </p>
 */
@Repository
@Transactional
public class ServicioHibernate implements ServicioRepository {

    /**
     * EntityManager para gestionar las operaciones de persistencia.
     */
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
     * Obtiene todos los servicios asociados a una empresa específica por su
     * identificador fiscal.
     *
     * @param identificadorFiscal Identificador fiscal de la empresa.
     * @return Lista de servicios pertenecientes a la empresa con el
     * identificador fiscal dado.
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
     * @return {@code true} si el servicio existe, {@code false} en caso
     * contrario.
     */
    @Override
    public boolean existsById(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Servicio> root = cq.from(Servicio.class);

        Path<Object> idPath = root.get("idServicio");

        Predicate predicate = cb.equal(idPath, id);

        cq.select(cb.count(root)).where(predicate);

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

    /**
     * Busca servicios cuyo nombre contenga la cadena especificada, sin
     * distinguir entre mayúsculas y minúsculas.
     *
     * @param nombre Texto a buscar en el nombre del servicio.
     * @return Lista de servicios que coinciden con el criterio de búsqueda.
     */
    @Override
    public List<Servicio> findByNombreContainingIgnoreCase(String nombre) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Servicio> cq = cb.createQuery(Servicio.class);
        Root<Servicio> root = cq.from(Servicio.class);

        // Busca coincidencias parciales (ignorando mayúsculas/minúsculas)
        Predicate predicate = cb.like(
                cb.lower(root.get("nombre")), // Convierte el campo a minúsculas
                "%" + nombre.toLowerCase() + "%" // Convierte el valor a minúsculas y añade comodines
        );

        cq.where(predicate);
        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Obtiene todos los servicios de una empresa específica, incluyendo los
     * horarios asociados mediante un LEFT JOIN. Este método realiza una
     * consulta para recuperar todos los servicios de una empresa, junto con sus
     * horarios, sin necesidad de que exista un horario asignado a cada
     * servicio.
     *
     * @param empresaId El identificador de la empresa cuyos servicios se
     * quieren obtener.
     * @return Una lista de servicios asociados a la empresa indicada. Si no se
     * encuentran servicios, se devuelve una lista vacía.
     */
    @Override
    public List<Servicio> findAllHorariosByEmpresaId(Long empresaId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Servicio> cq = cb.createQuery(Servicio.class);
        Root<Servicio> root = cq.from(Servicio.class);

        Join<Servicio, Horario> horarioJoin = root.join("horarios", JoinType.LEFT);

        Predicate empresaPredicate = cb.equal(root.get("empresa").get("id"), empresaId);
        cq.where(empresaPredicate);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Busca un servicio por su ID y verifica que pertenezca a una empresa
     * específica.
     *
     * @param servicioId ID único del servicio.
     * @param empresaId ID de la empresa propietaria del servicio.
     * @return Un {@link Optional} con el servicio encontrado, o vacío si no
     * existe.
     */
    @Override
    public Optional<Servicio> findByIdAndEmpresaId(Long servicioId, Long empresaId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Servicio> cq = cb.createQuery(Servicio.class);
        Root<Servicio> root = cq.from(Servicio.class);

        // Filtro por ID del servicio
        Predicate idPredicate = cb.equal(root.get("id"), servicioId);
        // Filtro por ID de la empresa asociada
        Predicate empresaPredicate = cb.equal(root.get("empresa").get("id"), empresaId);

        // Combina ambos filtros
        cq.where(cb.and(idPredicate, empresaPredicate));

        try {
            Servicio servicio = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(servicio);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Busca un servicio por su ID y verifica que pertenezca a una empresa con
     * el identificador fiscal especificado.
     *
     * @param id ID del servicio a buscar
     * @param identificadorFiscal Identificador fiscal de la empresa
     * @return Optional con el servicio encontrado o vacío si no cumple las
     * condiciones
     */
    @Override
    public Optional<Servicio> findByIdAndEmpresaIdentificadorFiscal(Long id, String identificadorFiscal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Servicio> cq = cb.createQuery(Servicio.class);
        Root<Servicio> root = cq.from(Servicio.class);

        // Filtro por ID del servicio
        Predicate idPredicate = cb.equal(root.get("id"), id);
        // Filtro por identificador fiscal de la empresa
        Predicate fiscalPredicate = cb.equal(root.get("empresa").get("identificadorFiscal"), identificadorFiscal);

        // Combinar los predicados
        cq.where(cb.and(idPredicate, fiscalPredicate));

        try {
            Servicio servicio = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(servicio);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
