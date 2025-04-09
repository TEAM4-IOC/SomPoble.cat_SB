package com.sompoble.cat.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

@Repository
@Transactional
public class EmpresarioHibernate implements EmpresarioRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void addEmpresario(Empresario empresario) {
        entityManager.persist(empresario);
    }

    @Override //merge directamente sobre un DTO, pero entityManager.merge() solo trabaja con entidades, no con DTOs--se cambia-Gemma
    public void updateEmpresario(EmpresarioDTO empresarioDTO) {
        Empresario empresario = convertToEntity(empresarioDTO);
        entityManager.merge(empresario);
    }

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

    @Override
    public Empresario findEmpresarioByDNI(String dni) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        Predicate dniPredicate = cb.equal(root.get("dni"), dni);
        cq.where(dniPredicate);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public List<EmpresarioDTO> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        List<Empresario> empresarios = entityManager.createQuery(cq).getResultList();
        return empresarios.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Empresario empresario = entityManager.find(Empresario.class, id);
        if (empresario != null) {
            entityManager.remove(empresario);
        }
    }

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

    @Override
    public Empresario findByEmailFull(String email){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Empresario> cq = cb.createQuery(Empresario.class);
        Root<Empresario> root = cq.from(Empresario.class);

        Predicate emailPredicate = cb.equal(root.get("email"), email);
        cq.where(emailPredicate);

        List<Empresario> result = entityManager.createQuery(cq).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

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

    public Empresario convertToEntity(Empresario empresarioDTO) {
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
