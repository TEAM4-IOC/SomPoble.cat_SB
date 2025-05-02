package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.dto.EmpresarioDTO;
import com.sompoble.cat.repository.EmpresarioRepository;
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
 * Implementación del repositorio para la entidad {@code Empresario}.
 * <p>
 * Proporciona métodos para gestionar empresarios en la base de datos utilizando
 * Hibernate como proveedor JPA.
 * </p>
 *
 * @author SomPoble
 */
@Repository
@Transactional
public class EmpresarioHibernate implements EmpresarioRepository {

    /**
     * EntityManager para gestionar las operaciones de persistencia.
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEmpresario(Empresario empresario) {
        entityManager.persist(empresario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEmpresario(EmpresarioDTO empresario) {
        entityManager.merge(empresario);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmpresarioDTO findByDNI(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.where(dniPredicate);

        List<Empresario> result = entityManager.createQuery(cq).getResultList();
        return result.isEmpty() ? null : convertToDTO(result.get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Empresario findEmpresarioByDNI(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.where(dniPredicate);

        return entityManager.createQuery(cq).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EmpresarioDTO> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        List<Empresario> empresarios = entityManager.createQuery(cq).getResultList();
        return empresarios.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        Empresario empresario = entityManager.find(Empresario.class, id);
        if (empresario != null) {
            entityManager.remove(empresario);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByDni(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.where(dniPredicate);

        List<Empresario> result = entityManager.createQuery(cq).getResultList();

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
        Root<Empresario> root = cq.from(Empresario.class);

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
        Root<Empresario> root = cq.from(Empresario.class);

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
        Root<Empresario> root = cq.from(Empresario.class);

        Predicate emailPredicate = cb.equal(root.get("email"), email);
        cq.select(cb.count(root)).where(emailPredicate);

        Long count = entityManager.createQuery(cq).getSingleResult();
        return count > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmpresarioDTO findByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        Predicate emailPredicate = cb.equal(root.get("email"), email);
        cq.where(emailPredicate);

        List<Empresario> result = entityManager.createQuery(cq).getResultList();
        return result.isEmpty() ? null : convertToDTO(result.get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Empresario findByEmailFull(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        Predicate emailPredicate = cb.equal(root.get("email"), email);
        cq.where(emailPredicate);

        List<Empresario> result = entityManager.createQuery(cq).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Convierte un objeto {@link Empresario} a su correspondiente
     * {@link EmpresarioDTO}.
     * <p>
     * Este método extrae la información relevante de la entidad Empresario y
     * crea un objeto DTO que puede ser transferido a través de capas. También
     * convierte las empresas asociadas al empresario a sus respectivos DTOs.
     * </p>
     *
     * @param empresario el objeto {@link Empresario} a convertir.
     * @return el objeto {@link EmpresarioDTO} correspondiente.
     */
    private EmpresarioDTO convertToDTO(Empresario empresario) {
        List<Long> notificacionesIds = new ArrayList<>();

        List<EmpresaDTO> empresasDTO = empresario.getEmpresas() != null
                ? empresario.getEmpresas().stream().map(empresa -> {

                    List<Long> reservasIds = empresa.getReservas() != null
                            ? empresa.getReservas().stream()
                                    .map(reserva -> reserva.getIdReserva())
                                    .collect(Collectors.toList())
                            : new ArrayList<>();

                    List<Long> serviciosIds = empresa.getServicios() != null
                            ? empresa.getServicios().stream()
                                    .map(servicio -> servicio.getIdServicio())
                                    .collect(Collectors.toList())
                            : new ArrayList<>();

                    return new EmpresaDTO(
                            empresa.getIdEmpresa(),
                            empresa.getEmpresario().getDni(),
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
                }).collect(Collectors.toList())
                : new ArrayList<>();

        return new EmpresarioDTO(
                empresario.getIdPersona(),
                empresario.getDni(),
                empresario.getNombre(),
                empresario.getApellidos(),
                empresario.getEmail(),
                empresario.getTelefono(),
                empresario.getPass(),
                notificacionesIds,
                empresasDTO
        );
    }

    /**
     * Convierte un objeto {@link EmpresarioDTO} a una entidad
     * {@link Empresario}.
     * <p>
     * Este método crea una nueva entidad Empresario y la completa con la
     * información contenida en el DTO proporcionado, incluyendo la conversión
     * de las empresas asociadas a sus respectivas entidades.
     * </p>
     *
     * @param empresarioDTO El DTO que contiene la información del empresario.
     * @return Una entidad Empresario con la información del DTO.
     */
    public Empresario convertToEntity(EmpresarioDTO empresarioDTO) {
        Empresario empresario = new Empresario();

        empresario.setDni(empresarioDTO.getDni());
        empresario.setNombre(empresarioDTO.getNombre());
        empresario.setApellidos(empresarioDTO.getApellidos());
        empresario.setEmail(empresarioDTO.getEmail());
        empresario.setTelefono(empresarioDTO.getTelefono());
        empresario.setPass(empresarioDTO.getPass());

        if (empresarioDTO.getEmpresas() != null) {
            List<Empresa> empresas = empresarioDTO.getEmpresas().stream().map(empresaDTO -> {
                Empresa empresa = new Empresa();
                empresa.setIdentificadorFiscal(empresaDTO.getIdentificadorFiscal());
                empresa.setNombre(empresaDTO.getNombre());
                empresa.setDireccion(empresaDTO.getDireccion());
                empresa.setEmail(empresaDTO.getEmail());
                empresa.setTelefono(empresaDTO.getTelefono());
                empresa.setActividad(empresaDTO.getActividad());
                return empresa;
            }).collect(Collectors.toList());

            empresario.setEmpresas(empresas);
        }

        return empresario;
    }
}
