package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Servicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicioHibernateTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Servicio> criteriaQuery;

    @Mock
    private CriteriaQuery<Long> countCriteriaQuery;

    @Mock
    private Root<Servicio> root;

    @Mock
    private Path<Object> empresaPath;

    @Mock
    private Path<Object> idPath;

    @Mock
    private Path<Object> identificadorPath;

    @Mock
    private TypedQuery<Servicio> typedQuery;

    @Mock
    private TypedQuery<Long> countTypedQuery;

    @InjectMocks
    private ServicioHibernate servicioHibernate;

    private Servicio servicio;
    private Empresa empresa;

    @BeforeEach
    public void setup() {
        empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setIdEmpresa(1L);
        servicio = new Servicio(
                "Servicio Test",
                "Descripción del servicio test",
                2,
                100.0f,
                10,
                empresa
        );
        servicio.setIdServicio(1L);
    }

    @Test
    public void testFindById() {
        when(entityManager.find(Servicio.class, 1L)).thenReturn(servicio);

        Servicio result = servicioHibernate.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdServicio());
        assertEquals("Servicio Test", result.getNombre());
        verify(entityManager).find(Servicio.class, 1L);
    }

    @Test
    public void testUpdateServicio() {
        when(entityManager.merge(servicio)).thenReturn(servicio);
        servicioHibernate.updateServicio(servicio);
        verify(entityManager).merge(servicio);
    }

    @Test
    public void testAddServicio() {
        servicioHibernate.addServicio(servicio);
        verify(entityManager).persist(servicio);
    }

    @Test
    public void testFindAllByEmpresaId() {
        List<Servicio> expectedServicios = Arrays.asList(servicio);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Servicio.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Servicio.class)).thenReturn(root);
        when(root.get("empresa")).thenReturn(empresaPath);
        when(empresaPath.get("id")).thenReturn(idPath);
        when(criteriaBuilder.equal(idPath, 1L)).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedServicios);
        List<Servicio> result = servicioHibernate.findAllByEmpresaId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdServicio());
        verify(criteriaQuery).where(any(Predicate.class));
    }

    @Test
    public void testExistsById() {
        // Simular el EntityManager y CriteriaBuilder
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countCriteriaQuery);
        when(countCriteriaQuery.from(Servicio.class)).thenReturn(root);
        when(root.get("idServicio")).thenReturn(idPath);

        // Simular el Predicate
        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.equal(idPath, 1L)).thenReturn(predicate);

        // Simular el encadenamiento de métodos de CriteriaQuery
        when(countCriteriaQuery.select(any())).thenReturn(countCriteriaQuery); // << importante
        when(countCriteriaQuery.where(any(Predicate.class))).thenReturn(countCriteriaQuery); // << importante

        // Simular la consulta final
        when(entityManager.createQuery(countCriteriaQuery)).thenReturn(countTypedQuery);
        when(countTypedQuery.getSingleResult()).thenReturn(1L);

        // Ejecutar el método
        boolean exists = servicioHibernate.existsById(1L);

        // Verificar
        assertTrue(exists);
    }

    @Test
    public void testDeleteById_ExistingServicio() {
        when(entityManager.find(Servicio.class, 1L)).thenReturn(servicio);
        servicioHibernate.deleteById(1L);
        verify(entityManager).find(Servicio.class, 1L);
        verify(entityManager).remove(servicio);
    }

    @Test
    public void testDeleteById_NonExistingServicio() {
        when(entityManager.find(Servicio.class, 1L)).thenReturn(null);
        servicioHibernate.deleteById(1L);
        verify(entityManager).find(Servicio.class, 1L);
        verify(entityManager, never()).remove(any(Servicio.class));
    }

    @Test
    public void testFindAllByEmpresaIdentificador() {
        List<Servicio> expectedServicios = Arrays.asList(servicio);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Servicio.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Servicio.class)).thenReturn(root);
        when(root.get("empresa")).thenReturn(empresaPath);
        when(empresaPath.get("identificadorFiscal")).thenReturn(identificadorPath);
        when(criteriaBuilder.equal(identificadorPath, "A12345678")).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedServicios);

        List<Servicio> result = servicioHibernate.findAllByEmpresaIdentificador("A12345678");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdServicio());
        verify(criteriaQuery).where(any(Predicate.class));
    }
    
    @Test
    public void testFindAllHorariosByEmpresaId() {
        List<Servicio> expectedServicios = Arrays.asList(servicio);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Servicio.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Servicio.class)).thenReturn(root);

        // Mocking the join operation
        when(root.join("horarios", JoinType.LEFT)).thenReturn(mock(Join.class));

        when(root.get("empresa")).thenReturn(empresaPath);
        when(empresaPath.get("id")).thenReturn(idPath);
        when(criteriaBuilder.equal(idPath, 1L)).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedServicios);

        List<Servicio> result = servicioHibernate.findAllHorariosByEmpresaId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdServicio());
        verify(criteriaQuery).where(any(Predicate.class));
    }

    @Test
    public void testFindByIdAndEmpresaId_Found() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Servicio.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Servicio.class)).thenReturn(root);

        Path<Object> servicioIdPath = mock(Path.class);
        when(root.get("id")).thenReturn(servicioIdPath);
        when(criteriaBuilder.equal(servicioIdPath, 1L)).thenReturn(mock(Predicate.class));

        when(root.get("empresa")).thenReturn(empresaPath);
        when(empresaPath.get("id")).thenReturn(idPath);
        when(criteriaBuilder.equal(idPath, 1L)).thenReturn(mock(Predicate.class));

        when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class))).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(servicio);

        Optional<Servicio> result = servicioHibernate.findByIdAndEmpresaId(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdServicio());
        verify(criteriaQuery).where(any(Predicate.class));
    }

    @Test
    public void testFindByIdAndEmpresaId_NotFound() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Servicio.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Servicio.class)).thenReturn(root);

        Path<Object> servicioIdPath = mock(Path.class);
        when(root.get("id")).thenReturn(servicioIdPath);
        when(criteriaBuilder.equal(servicioIdPath, 1L)).thenReturn(mock(Predicate.class));

        when(root.get("empresa")).thenReturn(empresaPath);
        when(empresaPath.get("id")).thenReturn(idPath);
        when(criteriaBuilder.equal(idPath, 2L)).thenReturn(mock(Predicate.class));

        when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class))).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new NoResultException());

        Optional<Servicio> result = servicioHibernate.findByIdAndEmpresaId(1L, 2L);

        assertFalse(result.isPresent());
        verify(criteriaQuery).where(any(Predicate.class));
    }

    @Test
    public void testFindByIdAndEmpresaIdentificadorFiscal_Found() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Servicio.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Servicio.class)).thenReturn(root);

        Path<Object> servicioIdPath = mock(Path.class);
        when(root.get("id")).thenReturn(servicioIdPath);
        when(criteriaBuilder.equal(servicioIdPath, 1L)).thenReturn(mock(Predicate.class));

        when(root.get("empresa")).thenReturn(empresaPath);
        when(empresaPath.get("identificadorFiscal")).thenReturn(identificadorPath);
        when(criteriaBuilder.equal(identificadorPath, "A12345678")).thenReturn(mock(Predicate.class));

        when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class))).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(servicio);

        Optional<Servicio> result = servicioHibernate.findByIdAndEmpresaIdentificadorFiscal(1L, "A12345678");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdServicio());
        verify(criteriaQuery).where(any(Predicate.class));
    }

    @Test
    public void testFindByIdAndEmpresaIdentificadorFiscal_NotFound() {
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Servicio.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Servicio.class)).thenReturn(root);

        Path<Object> servicioIdPath = mock(Path.class);
        when(root.get("id")).thenReturn(servicioIdPath);
        when(criteriaBuilder.equal(servicioIdPath, 1L)).thenReturn(mock(Predicate.class));

        when(root.get("empresa")).thenReturn(empresaPath);
        when(empresaPath.get("identificadorFiscal")).thenReturn(identificadorPath);
        when(criteriaBuilder.equal(identificadorPath, "B87654321")).thenReturn(mock(Predicate.class));

        when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class))).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new NoResultException());

        Optional<Servicio> result = servicioHibernate.findByIdAndEmpresaIdentificadorFiscal(1L, "B87654321");

        assertFalse(result.isPresent());
        verify(criteriaQuery).where(any(Predicate.class));
    }
}
