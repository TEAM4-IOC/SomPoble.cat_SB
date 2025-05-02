package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Evento;
import com.sompoble.cat.repository.EventoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de eventos usando JPA y Criteria API. Maneja
 * operaciones CRUD y consultas personalizadas.
 */
@Repository
@Transactional
public class EventoHibernate implements EventoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Guarda o actualiza un evento en la base de datos.
     *
     * @param evento Objeto {@link Evento} a persistir.
     * @return El evento guardado con su ID asignado.
     */
    @Override
    public Evento save(Evento evento) {
        if (evento.getIdEvento() == null) {
            entityManager.persist(evento);
        } else {
            evento = entityManager.merge(evento);
        }
        return evento;
    }

    /**
     * Elimina un evento por su ID.
     *
     * @param id ID del evento a eliminar.
     * @throws EntityNotFoundException si el evento no existe.
     */
    @Override
    public void delete(Long id) {
        Evento evento = findById(id);
        if (evento == null) {
            throw new EntityNotFoundException("No se encontró el evento con ID: " + id);
        }
        entityManager.remove(evento);
    }

    /**
     * Obtiene un evento por su ID.
     *
     * @param id ID del evento.
     * @return El evento encontrado o {@code null} si no existe.
     */
    @Override
    public Evento findById(Long id) {
        return entityManager.find(Evento.class, id);
    }

    /**
     * Lista todos los eventos almacenados.
     *
     * @return Lista de eventos.
     */
    @Override
    public List<Evento> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Evento> cq = cb.createQuery(Evento.class);
        Root<Evento> root = cq.from(Evento.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Busca eventos dentro de un rango de fechas.
     *
     * @param start Fecha de inicio del rango.
     * @param end Fecha de fin del rango.
     * @return Lista de eventos dentro del rango.
     */
    @Override
    public List<Evento> findByFechaEventoBetween(LocalDateTime start, LocalDateTime end) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Evento> cq = cb.createQuery(Evento.class);
        Root<Evento> root = cq.from(Evento.class);
        Predicate fechaPredicate = cb.between(root.get("fechaEvento"), start, end);
        cq.where(fechaPredicate);
        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Busca eventos por ubicación.
     *
     * @param ubicacion Ubicación del evento.
     * @return Lista de eventos en la ubicación especificada.
     */
    @Override
    public List<Evento> findByUbicacion(String ubicacion) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Evento> cq = cb.createQuery(Evento.class);
        Root<Evento> root = cq.from(Evento.class);
        Predicate ubicacionPredicate = cb.equal(root.get("ubicacion"), ubicacion);
        cq.where(ubicacionPredicate);
        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Busca eventos por ubicación y rango de fechas.
     *
     * @param ubicacion Ubicación del evento.
     * @param start Fecha de inicio del rango.
     * @param end Fecha de fin del rango.
     * @return Lista de eventos que cumplen con las condiciones.
     */
    @Override
    public List<Evento> findByUbicacionAndFechaEventoBetween(String ubicacion, LocalDateTime start, LocalDateTime end) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Evento> cq = cb.createQuery(Evento.class);
        Root<Evento> root = cq.from(Evento.class);
        Predicate ubicacionPredicate = cb.equal(root.get("ubicacion"), ubicacion);
        Predicate fechaPredicate = cb.between(root.get("fechaEvento"), start, end);
        cq.where(cb.and(ubicacionPredicate, fechaPredicate));
        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Busca eventos por una palabra clave en el nombre del evento.
     *
     * @param keyword Palabra clave a buscar en el nombre del evento.
     * @return Lista de eventos que contienen la palabra clave.
     */
    @Override
    public List<Evento> findByNombreContainingIgnoreCase(String keyword) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Evento> cq = cb.createQuery(Evento.class);
        Root<Evento> root = cq.from(Evento.class);
        Predicate keywordPredicate = cb.like(cb.lower(root.get("nombre")), "%" + keyword.toLowerCase() + "%");
        cq.where(keywordPredicate);
        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Encuentra el evento más cercano a la fecha actual.
     *
     * @return El evento más cercano a la fecha actual.
     */
    @Override
    public Optional<Evento> findClosestEvent() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Evento> cq = cb.createQuery(Evento.class);
        Root<Evento> root = cq.from(Evento.class);
        cq.select(root).where(cb.greaterThanOrEqualTo(root.get("fechaEvento"), LocalDateTime.now()));
        List<Evento> resultList = entityManager.createQuery(cq).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    /**
     * Cuenta la cantidad total de eventos en la base de datos.
     *
     * @return El número total de eventos.
     */
    @Override
    public Long count() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Evento> root = cq.from(Evento.class);
        cq.select(cb.count(root));
        return entityManager.createQuery(cq).getSingleResult();
    }

}
