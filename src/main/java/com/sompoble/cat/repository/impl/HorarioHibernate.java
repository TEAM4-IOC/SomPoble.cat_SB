package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class HorarioHibernate implements HorarioRepository {

    @Autowired
    private EntityManager entityManager;

    
    
    @Override
    public List<Horario> findByEmpresa_IdEmpresa(Long idEmpresa) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate empresaIdPredicate = cb.equal(root.get("empresa").get("idEmpresa"), idEmpresa);
        cq.where(empresaIdPredicate);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByDiasLaborablesContainingIgnoreCase(String dia) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate diaPredicate = cb.like(
            cb.lower(root.get("diasLaborables")),
            "%" + dia.toLowerCase() + "%"
        );
        cq.where(diaPredicate);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByHorarioInicioBetween(LocalTime inicio, LocalTime fin) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate horarioBetween = cb.between(root.get("horarioInicio"), inicio, fin);
        cq.where(horarioBetween);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByHorarioFinBetween(LocalTime inicio, LocalTime fin) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate horarioBetween = cb.between(root.get("horarioFin"), inicio, fin);
        cq.where(horarioBetween);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByEmpresa_IdEmpresaAndDiasLaborablesContaining(
        Long idEmpresa, String dia) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate empresaIdPredicate = cb.equal(root.get("empresa").get("idEmpresa"), idEmpresa);
        Predicate diaPredicate = cb.like(
            cb.lower(root.get("diasLaborables")),
            "%" + dia.toLowerCase() + "%"
        );
        cq.where(cb.and(empresaIdPredicate, diaPredicate));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByHorarioInicioBefore(LocalTime hora) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate predicate = cb.lessThan(root.get("horarioInicio"), hora);
        cq.where(predicate);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByHorarioFinAfter(LocalTime hora) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate predicate = cb.greaterThan(root.get("horarioFin"), hora);
        cq.where(predicate);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByOrderByHorarioInicioAsc() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        cq.orderBy(cb.asc(root.get("horarioInicio")));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByEmpresa_IdEmpresaAndHorarioInicioBetween(
        Long idEmpresa, LocalTime inicio, LocalTime fin) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate empresaIdPredicate = cb.equal(root.get("empresa").get("idEmpresa"), idEmpresa);
        Predicate horarioBetween = cb.between(root.get("horarioInicio"), inicio, fin);
        cq.where(cb.and(empresaIdPredicate, horarioBetween));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Horario> findByEmpresa_IdEmpresaAndHorarioFinBetween(
        Long idEmpresa, LocalTime inicio, LocalTime fin) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        Root<Horario> root = cq.from(Horario.class);
        Predicate empresaIdPredicate = cb.equal(root.get("empresa").get("idEmpresa"), idEmpresa);
        Predicate horarioBetween = cb.between(root.get("horarioFin"), inicio, fin);
        cq.where(cb.and(empresaIdPredicate, horarioBetween));
        return entityManager.createQuery(cq).getResultList();
    }

    // --- Métodos con @Query (usando JPQL) ---

    @Override
    public List<Horario> findByDiaExacto(String dia) {
        return entityManager.createQuery(
            "SELECT h FROM Horario h " +
            "WHERE h.diasLaborables LIKE CONCAT('%', :dia, '%')", Horario.class)
            .setParameter("dia", dia)
            .getResultList();
    }

    @Override
    public List<Horario> findByEmpresaAndHorarioBetween(
        Long idEmpresa, LocalTime inicio, LocalTime fin) {
        return entityManager.createQuery(
            "SELECT h FROM Horario h " +
            "WHERE h.empresa.idEmpresa = :idEmpresa " +
            "AND h.horarioInicio >= :inicio " +
            "AND h.horarioFin <= :fin", Horario.class)
            .setParameter("idEmpresa", idEmpresa)
            .setParameter("inicio", inicio)
            .setParameter("fin", fin)
            .getResultList();
    }

    @Override
    public List<Horario> findByHorarioOverlap(
        LocalTime inicio, LocalTime fin) {
        return entityManager.createQuery(
            "SELECT h FROM Horario h " +
            "WHERE h.horarioInicio < :inicio " +
            "AND h.horarioFin > :fin", Horario.class)
            .setParameter("inicio", inicio)
            .setParameter("fin", fin)
            .getResultList();
    }

    @Override
    public Horario findByEmpresaDiaYHorarioExacto(
        Long idEmpresa, String dia, LocalTime inicio, LocalTime fin) {
        return entityManager.createQuery(
            "SELECT h FROM Horario h " +
            "WHERE h.empresa.idEmpresa = :idEmpresa " +
            "AND h.diasLaborables LIKE CONCAT('%', :dia, '%') " +
            "AND h.horarioInicio = :inicio " +
            "AND h.horarioFin = :fin", Horario.class)
            .setParameter("idEmpresa", idEmpresa)
            .setParameter("dia", dia)
            .setParameter("inicio", inicio)
            .setParameter("fin", fin)
            .getSingleResult();
    }

    @Override
    public List<Horario> findOrphanHorarios() {
        return entityManager.createQuery(
            "SELECT h FROM Horario h WHERE h.empresa IS NULL", Horario.class)
            .getResultList();
    }

    @Override
    public Long countByEmpresaId(Long idEmpresa) {
        return entityManager.createQuery(
            "SELECT COUNT(h) FROM Horario h WHERE h.empresa.idEmpresa = :idEmpresa", Long.class)
            .setParameter("idEmpresa", idEmpresa)
            .getSingleResult();
    }

    @Override
    public List<Horario> findByDiasMultiples(
        String dia1, String dia2) {
        return entityManager.createQuery(
            "SELECT h FROM Horario h " +
            "WHERE h.diasLaborables LIKE CONCAT('%', :dia1, '%') " +
            "AND h.diasLaborables LIKE CONCAT('%', :dia2, '%')", Horario.class)
            .setParameter("dia1", dia1)
            .setParameter("dia2", dia2)
            .getResultList();
    }

    @Override
    public List<Horario> findByHorarioRanges(
        LocalTime inicio1, LocalTime fin1, 
        LocalTime inicio2, LocalTime fin2) {
        return entityManager.createQuery(
            "SELECT h FROM Horario h " +
            "WHERE h.horarioInicio BETWEEN :inicio1 AND :fin1 " +
            "AND h.horarioFin BETWEEN :inicio2 AND :fin2", Horario.class)
            .setParameter("inicio1", inicio1)
            .setParameter("fin1", fin1)
            .setParameter("inicio2", inicio2)
            .setParameter("fin2", fin2)
            .getResultList();
    }

    @Override
    public List<Horario> findByFechaAltaBetween(
        LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return entityManager.createQuery(
            "SELECT h FROM Horario h " +
            "WHERE h.fechaAlta BETWEEN :fechaInicio AND :fechaFin", Horario.class)
            .setParameter("fechaInicio", fechaInicio)
            .setParameter("fechaFin", fechaFin)
            .getResultList();
    }

    @Override
    public List<Horario> findByFechaModificacionAfter(
        LocalDateTime fecha) {
        return entityManager.createQuery(
            "SELECT h FROM Horario h " +
            "WHERE h.fechaModificacion >= :fecha", Horario.class)
            .setParameter("fecha", fecha)
            .getResultList();
    }

    @Override
    public void delete(Long id) {
        Horario horario = entityManager.find(Horario.class, id);
        if (horario != null) {
            entityManager.remove(horario);
        }
    }

    @Override
    public Optional<Horario> findById(Long id) {
        Horario horario = entityManager.find(Horario.class, id);
        return Optional.ofNullable(horario);
    }

    @Override
    public List<Horario> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Horario> cq = cb.createQuery(Horario.class);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Horario save(Horario horario) {
        if (horario.getIdHorario() == null) {
            entityManager.persist(horario);
        } else {
            horario = entityManager.merge(horario);
        }
        return horario;
    }
}