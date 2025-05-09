package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.repository.NotificacionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Implementación del repositorio {@link NotificacionRepository} utilizando
 * {@link EntityManager}. Realiza operaciones CRUD sobre la entidad
 * {@link Notificacion}.
 */
@Repository
@Transactional
public class NotificacionHibernate implements NotificacionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Guarda una nueva notificación o actualiza una existente en la base de
     * datos.
     *
     * @param notificacion la notificación a guardar o actualizar
     * @return notificacion
     */
    @Override
    public Notificacion save(Notificacion notificacion) {
        if (notificacion.getIdNotificacion() == null) {
            entityManager.persist(notificacion); // Inserta nueva entidad
        } else {
            notificacion = entityManager.merge(notificacion); // Actualiza entidad existente
        }
        return notificacion; // Devolvemos la notificación
    }

    /**
     * Recupera todas las notificaciones almacenadas en la base de datos.
     *
     * @return una lista de todas las notificaciones
     */
    @Override
    public List<Notificacion> findAll() {
        return entityManager.createQuery("FROM Notificacion", Notificacion.class).getResultList();
    }

    /**
     * Busca una notificación específica por su identificador.
     *
     * @param id el ID de la notificación
     * @return la notificación correspondiente al ID, o {@code null} si no se
     * encuentra
     */
    @Override
    public Notificacion findById(Long id) {
        return entityManager.find(Notificacion.class, id);
    }

    /**
     * Elimina una notificación de la base de datos usando su ID.
     *
     * @param id el ID de la notificación a eliminar
     */
    @Override
    public void deleteById(Long id) {
        Notificacion notificacion = entityManager.find(Notificacion.class, id);
        if (notificacion != null) {
            entityManager.remove(notificacion);
        }
    }

    /**
     * Recupera una lista de notificaciones asociadas a un cliente o empresa
     * dado su identificador.
     *
     * <p>
     * El método busca notificaciones donde:
     * </p>
     * <ul>
     * <li>El DNI del cliente sea igual al identificador proporcionado, o</li>
     * <li>El número fiscal de la empresa sea igual al identificador
     * proporcionado.</li>
     * </ul>
     * 
     *
     * @param identificador El DNI del cliente o el número fiscal de la empresa.
     * @return Una lista de {@link Notificacion} que corresponden al
     * identificador dado. Puede ser una lista vacía si no existen
     * coincidencias.
     */
    @Override
    public List<Notificacion> findByIdentificador(String identificador) {
        return entityManager.createQuery(
                "SELECT n FROM Notificacion n WHERE n.cliente.dni = :identificador OR n.empresa.numeroFiscal = :identificador",
                Notificacion.class
        )
                .setParameter("identificador", identificador)
                .getResultList();
    }

    /**
     * Busca y devuelve una lista de notificaciones asociadas a un cliente
     * específico identificado por su DNI.
     *
     * Este método utiliza la API Criteria de JPA para construir una consulta
     * dinámica que filtre las notificaciones en función del DNI del cliente.
     *
     * @param dni El DNI del cliente cuyas notificaciones se desean obtener.
     * @return Una lista de notificaciones asociadas al cliente con el DNI
     * proporcionado.
     */

    @Override
    public List<Notificacion> findByClienteDni(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Notificacion> cq = cb.createQuery(Notificacion.class);
        Root<Notificacion> root = cq.from(Notificacion.class);

        Predicate dniPredicate = cb.equal(root.get("cliente").get("dni"), dni);
        cq.where(dniPredicate);

        List<Notificacion> notificacion = entityManager.createQuery(cq).getResultList();

        return entityManager.createQuery(cq).getResultList();
    }

}
