package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Servicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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

        servicio = new Servicio("Servicio Test", "Descripción del servicio test", "2 horas", "100.0", 10, empresa);
    }
    /*
    @Test
    public void testFindById() {
        when(entityManager.find(Servicio.class, 1L)).thenReturn(servicio);
        Servicio result = servicioHibernate.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdServicio());
        assertEquals("Servicio Test", result.getNombre());
        verify(entityManager).find(Servicio.class, 1L);
    }
    */
    
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
    
    /*
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
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countCriteriaQuery);
        when(countCriteriaQuery.from(Servicio.class)).thenReturn(root);
        when(root.get("idServicio")).thenReturn(idPath);
        when(criteriaBuilder.equal(idPath, 1L)).thenReturn(mock(Predicate.class));
        when(entityManager.createQuery(countCriteriaQuery)).thenReturn(countTypedQuery);
        when(countTypedQuery.getSingleResult()).thenReturn(1L);
        boolean exists = servicioHibernate.existsById(1L);
        assertTrue(exists);
        verify(countCriteriaQuery).select(any());
        verify(countCriteriaQuery).where(any(Predicate.class));
    }
    */

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
}
