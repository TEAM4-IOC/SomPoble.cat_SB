package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.repository.EmpresaRepository;
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
 * Implementación del repositorio para la entidad {@code Empresa}.
 * <p>
 * Proporciona métodos para gestionar empresas en la base de datos.
 * </p>
 *
 * @author SomPoble
 */
@Repository
@Transactional
public class EmpresaHibernate implements EmpresaRepository {

    /**
     * EntityManager para gestionar las operaciones de persistencia.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Agrega una nueva empresa a la base de datos.
     *
     * @param empresa el objeto {@link Empresa} a guardar.
     */
    @Override
    public void addEmpresa(Empresa empresa) {
        entityManager.persist(empresa);
    }

    /**
     * Actualiza la información de una empresa en la base de datos.
     *
     * @param empresa el objeto {@link EmpresaDTO} con la información
     * actualizada.
     */
    @Override
    public void updateEmpresa(Empresa empresa) {
        entityManager.merge(empresa);
    }

    /**
     * Busca una empresa por su identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return un objeto {@code EmpresaDTO} si la empresa existe, o null si no
     * se encuentra.
     */
    @Override
    public EmpresaDTO findByIdentificadorFiscal(String identificadorFiscal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresa> cq = cb.createQuery(Empresa.class);
        Root<Empresa> root = cq.from(Empresa.class);

        Predicate identificadorFiscalPredicate = cb.equal(root.get("identificadorFiscal"), identificadorFiscal);
        cq.where(identificadorFiscalPredicate);

        List<Empresa> result = entityManager.createQuery(cq).getResultList();

        if (result.isEmpty()) {
            return null;
        } else {
            Empresa empresa = result.get(0);
            return convertToDTO(empresa);
        }
    }

    /**
     * Busca una empresa completa por su identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return un objeto {@code Empresa} completo si la empresa existe, o null
     * si no se encuentra.
     */
    @Override
    public Empresa findByIdentificadorFiscalFull(String identificadorFiscal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresa> cq = cb.createQuery(Empresa.class);
        Root<Empresa> root = cq.from(Empresa.class);

        Predicate identificadorFiscalPredicate = cb.equal(root.get("identificadorFiscal"), identificadorFiscal);
        cq.where(identificadorFiscalPredicate);

        List<Empresa> result = entityManager.createQuery(cq).getResultList();

        if (result.isEmpty()) {
            return null;
        } else {
            Empresa empresa = result.get(0);
            return empresa;
        }
    }

    /**
     * Obtiene todas las empresas registradas en la base de datos.
     *
     * @return una lista de objetos {@code EmpresaDTO} con la información de las
     * empresas.
     */
    @Override
    public List<EmpresaDTO> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresa> cq = cb.createQuery(Empresa.class);
        Root<Empresa> root = cq.from(Empresa.class);

        List<Empresa> empresas = entityManager.createQuery(cq).getResultList();
        return empresas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Elimina una empresa de la base de datos mediante su ID.
     *
     * @param id el identificador de la empresa a eliminar.
     */
    @Override
    public void deleteById(Long id) {
        Empresa empresa = entityManager.find(Empresa.class, id);
        if (empresa != null) {
            entityManager.remove(empresa);
        }
    }

    /**
     * Elimina una empresa de la base de datos mediante su identificador fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa a
     * eliminar.
     */
    @Override
    public void deleteByIdentificadorFiscal(String identificadorFiscal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresa> cq = cb.createQuery(Empresa.class);
        Root<Empresa> root = cq.from(Empresa.class);

        Predicate dniPredicate = cb.equal(root.get("identificadorFiscal"), identificadorFiscal);
        cq.where(dniPredicate);

        List<Empresa> result = entityManager.createQuery(cq).getResultList();

        if (!result.isEmpty()) {
            entityManager.remove(result.get(0));
        }
    }

    /**
     * Verifica si una empresa existe en la base de datos por su identificador
     * fiscal.
     *
     * @param identificadorFiscal el identificador fiscal de la empresa.
     * @return true si la empresa existe, false en caso contrario.
     */
    @Override
    public boolean existsByIdentificadorFiscal(String identificadorFiscal) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Empresa> root = cq.from(Empresa.class);

        Predicate identificadorFiscalPredicate = cb.equal(root.get("identificadorFiscal"), identificadorFiscal);
        cq.select(cb.count(root)).where(identificadorFiscalPredicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    /**
     * Verifica si una empresa existe en la base de datos por su ID.
     *
     * @param id el identificador de la empresa.
     * @return true si la empresa existe, false en caso contrario.
     */
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

    /**
     * Convierte un objeto {@link Empresa} a su correspondiente
     * {@link EmpresaDTO}.
     *
     * @param empresa el objeto {@link Empresa} a convertir.
     * @return el objeto {@link EmpresaDTO} correspondiente.
     */
    private EmpresaDTO convertToDTO(Empresa empresa) {
        System.out.println("Entrando a convertToDTO para la empresa: " + empresa);

        List<Long> reservasIds = new ArrayList<>();
        List<Long> serviciosIds = new ArrayList<>();

        if (empresa.getReservas() != null) {
            System.out.println("Reservas encontradas para la empresa: " + empresa.getIdEmpresa());
            for (Reserva reserva : empresa.getReservas()) {
                System.out.println("Reserva encontrada: " + reserva);
                reservasIds.add(reserva.getIdReserva());
            }
        } else {
            System.out.println("No se encontraron reservas para la empresa: " + empresa.getIdEmpresa());
        }

        if (empresa.getServicios() != null) {
            System.out.println("Servicios encontrados para la empresa: " + empresa.getIdEmpresa());
            for (Servicio servicio : empresa.getServicios()) {
                System.out.println("Servicio encontrado: " + servicio);
                serviciosIds.add(servicio.getIdServicio());
            }
        } else {
            System.out.println("No se encontraron servicios para la empresa: " + empresa.getIdEmpresa());
        }

        EmpresaDTO empresaDTO = new EmpresaDTO(
                empresa.getIdEmpresa(),
                empresa.getEmpresario() != null ? empresa.getEmpresario().getDni() : null,
                empresa.getIdentificadorFiscal(),
                empresa.getNombre(),
                empresa.getActividad(),
                empresa.getDireccion(),
                empresa.getEmail(),
                empresa.getTelefono(),
                empresa.getTipo(),
                reservasIds,
                serviciosIds
        );

        empresaDTO.setImagenUrl(empresa.getImagenUrl());
        empresaDTO.setImagenPublicId(empresa.getImagenPublicId());

        return empresaDTO;
    }

    /**
     * Convierte un objeto {@link EmpresaDTO} a una entidad {@link Empresa}.
     * <p>
     * Este método crea una nueva entidad Empresa y la completa con la
     * información contenida en el DTO proporcionado.
     * </p>
     *
     * @param empresaDTO El DTO que contiene la información de la empresa.
     * @return Una entidad Empresa con la información del DTO.
     */
    public Empresa convertToEntity(EmpresaDTO empresaDTO) {
        Empresa empresa = new Empresa();

        empresa.setIdentificadorFiscal(empresaDTO.getIdentificadorFiscal());
        empresa.setNombre(empresaDTO.getNombre());
        empresa.setActividad(empresaDTO.getActividad());
        empresa.setDireccion(empresaDTO.getDireccion());
        empresa.setEmail(empresaDTO.getEmail());
        empresa.setTelefono(empresaDTO.getTelefono());
        empresa.setTipo(empresaDTO.getTipo());

        empresa.setImagenUrl(empresaDTO.getImagenUrl());
        empresa.setImagenPublicId(empresaDTO.getImagenPublicId());

        return empresa;
    }
}
