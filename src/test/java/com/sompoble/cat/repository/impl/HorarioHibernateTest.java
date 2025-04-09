package com.sompoble.cat.repository.impl;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Expression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


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

      @Test
      void findByEmpresa_IdEmpresa() {
            CriteriaBuilder cb = mock(CriteriaBuilder.class);
            CriteriaQuery<Horario> cq = mock(CriteriaQuery.class);
            Root<Horario> root = mock(Root.class);
            Predicate predicate = mock(Predicate.class);
            Path<Object> empresaPath = mock(Path.class);
            Path<Object> idEmpresaPath = mock(Path.class);

            when(entityManager.getCriteriaBuilder()).thenReturn(cb);
            when(cb.createQuery(Horario.class)).thenReturn(cq);
            when(cq.from(Horario.class)).thenReturn(root);

            when(root.get("empresa")).thenReturn(empresaPath);
            when(empresaPath.get("idEmpresa")).thenReturn(idEmpresaPath);
            when(cb.equal(idEmpresaPath, 1L)).thenReturn(predicate);

            when(cq.where(predicate)).thenReturn(cq);
            when(entityManager.createQuery(cq)).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

            List<Horario> result = horarioRepository.findByEmpresa_IdEmpresa(1L);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(horario, result.get(0));
            verify(entityManager, times(1)).createQuery(cq);
      }



      @Test
      void findByOrderByHorarioInicioAsc() {
            // Mocking the CriteriaBuilder and CriteriaQuery
            CriteriaBuilder cb = mock(CriteriaBuilder.class);
            CriteriaQuery<Horario> query = mock(CriteriaQuery.class);
            Root<Horario> root = mock(Root.class);

            // Mocking the EntityManager to return the CriteriaBuilder and query
            when(entityManager.getCriteriaBuilder()).thenReturn(cb);
            when(cb.createQuery(Horario.class)).thenReturn(query);
            when(query.from(Horario.class)).thenReturn(root);

            // Mocking the TypedQuery behavior
            when(entityManager.createQuery(query)).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(Collections.singletonList(horario));

            // Call the method being tested
            List<Horario> result = horarioRepository.findByOrderByHorarioInicioAsc();

            // Assert the results
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(horario, result.get(0));

            // Verify the mock interactions
            verify(entityManager, times(1)).getCriteriaBuilder();
            verify(cb, times(1)).createQuery(Horario.class);
            verify(entityManager, times(1)).createQuery(query);
      }

}