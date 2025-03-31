package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Horario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


      @ExtendWith(MockitoExtension.class)
      class HorarioHibernateTest {

          @Mock
          private EntityManager entityManager;

          @Mock
          private TypedQuery<Horario> typedQuery;

          @InjectMocks
          private HorarioHibernate horarioRepository;

          private Horario horario;

          @BeforeEach
          void setUp() {
              horario = new Horario();
              horario.setIdHorario(1L);
              horario.setHorarioInicio(LocalTime.of(9, 0));
              horario.setHorarioFin(LocalTime.of(18, 0));
              horario.setDiasLaborables("Lunes,Martes");
              
              MockitoAnnotations.openMocks(this);
            
          }
          
          
          @Test
          void findByDiaExacto() {
              when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
              when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
              when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

              List<Horario> result = horarioRepository.findByDiaExacto("Lunes");

              assertNotNull(result);
              assertEquals(1, result.size());
              assertEquals(horario, result.get(0));
              verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
          }
          @Test
          void findByEmpresaAndHorarioBetween() {
              when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
              when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
              when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

              List<Horario> result = horarioRepository.findByEmpresaAndHorarioBetween(1L, LocalTime.of(8, 0), LocalTime.of(10, 0));

              assertNotNull(result);
              assertEquals(1, result.size());
              assertEquals(horario, result.get(0));
              verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
          }

          @Test
          void save() {
              when(entityManager.merge(horario)).thenReturn(horario);

              Horario result = horarioRepository.save(horario);

              assertNotNull(result);
              assertEquals(horario, result);
              verify(entityManager, times(1)).merge(horario);
          }

          @Test
          void delete() {
              when(entityManager.find(Horario.class, 1L)).thenReturn(horario);
              doNothing().when(entityManager).remove(horario);

              horarioRepository.delete(1L);

              verify(entityManager, times(1)).find(Horario.class, 1L);
              verify(entityManager, times(1)).remove(horario);
          }
          
     
/*
    @Test
    void findByEmpresa_IdEmpresa() {
    	 // Mock CriteriaBuilder and CriteriaQuery
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Horario> cq = mock(CriteriaQuery.class);
        Root<Horario> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);

        // Set up the mocks
        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Horario.class)).thenReturn(cq);
        when(cq.from(Horario.class)).thenReturn(root);
        when(cb.equal(root.get("empresa").get("idEmpresa"), 1L)).thenReturn(predicate);
        when(cq.where(predicate)).thenReturn(cq);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        // Call the method being tested
        List<Horario> result = horarioRepository.findByEmpresa_IdEmpresa(1L);

        // Verify the results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(cq);
    }

          
   /*       
    @Test
    void findByDiasLaborablesContainingIgnoreCase() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByDiasLaborablesContainingIgnoreCase("Lunes");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }

    @Test
    void findByHorarioInicioBetween() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByHorarioInicioBetween(LocalTime.of(8, 0), LocalTime.of(10, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }

    @Test
    void findByHorarioFinBetween() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByHorarioFinBetween(LocalTime.of(17, 0), LocalTime.of(19, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }

    @Test
    void findByEmpresa_IdEmpresaAndDiasLaborablesContaining() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByEmpresa_IdEmpresaAndDiasLaborablesContaining(1L, "Lunes");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }

    @Test
    void findByHorarioInicioBefore() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByHorarioInicioBefore(LocalTime.of(10, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }

    @Test
    void findByHorarioFinAfter() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByHorarioFinAfter(LocalTime.of(17, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }

    @Test
    void findByOrderByHorarioInicioAsc() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByOrderByHorarioInicioAsc();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }

    @Test
    void findByEmpresa_IdEmpresaAndHorarioInicioBetween() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByEmpresa_IdEmpresaAndHorarioInicioBetween(1L, LocalTime.of(8, 0), LocalTime.of(10, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }

    @Test
    void findByEmpresa_IdEmpresaAndHorarioFinBetween() {
        when(entityManager.createQuery(anyString(), eq(Horario.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

        List<Horario> result = horarioRepository.findByEmpresa_IdEmpresaAndHorarioFinBetween(1L, LocalTime.of(17, 0), LocalTime.of(19, 0));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(horario, result.get(0));
        verify(entityManager, times(1)).createQuery(anyString(), eq(Horario.class));
    }
    */

 
}
