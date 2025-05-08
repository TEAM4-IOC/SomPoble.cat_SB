package com.sompoble.cat.repository.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.domain.Notificacion.TipoNotificacion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacionHibernateTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private NotificacionHibernate notificacionHibernate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNuevaNotificacion() {
        Notificacion notificacion = new Notificacion(new Cliente(), null, "Mensaje", TipoNotificacion.INFORMACION);

        notificacionHibernate.save(notificacion);

        verify(entityManager).persist(notificacion);
    }

    @Test
    void testSaveActualizarNotificacion() {
        Notificacion notificacion = new Notificacion(new Cliente(), null, "Mensaje", TipoNotificacion.ERROR);

        try {
            Field idField = Notificacion.class.getDeclaredField("idNotificacion");
            idField.setAccessible(true);
            idField.set(notificacion, 1L);
        } catch (Exception e) {
            fail("No se pudo establecer el ID por reflexión: " + e.getMessage());
        }

        when(entityManager.merge(notificacion)).thenReturn(notificacion);

        Notificacion result = notificacionHibernate.save(notificacion);

        verify(entityManager).merge(notificacion);
        assertEquals("Mensaje", result.getMensaje());
    }

    @Test
    void testFindById() {
        Notificacion expected = new Notificacion(new Cliente(), null, "Buscar por ID", TipoNotificacion.ADVERTENCIA);

        when(entityManager.find(Notificacion.class, 1L)).thenReturn(expected);

        Notificacion result = notificacionHibernate.findById(1L);

        verify(entityManager).find(Notificacion.class, 1L);
        assertEquals("Buscar por ID", result.getMensaje());
    }

    @Test
    void testDeleteById() {
        Notificacion notificacion = new Notificacion();
        when(entityManager.find(Notificacion.class, 1L)).thenReturn(notificacion);

        notificacionHibernate.deleteById(1L);

        verify(entityManager).remove(notificacion);
    }

    @Test
    void testFindAll() {
        TypedQuery<Notificacion> mockQuery = mock(TypedQuery.class);
        List<Notificacion> fakeList = List.of(new Notificacion(), new Notificacion());

        when(entityManager.createQuery("FROM Notificacion", Notificacion.class)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(fakeList);

        List<Notificacion> result = notificacionHibernate.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testFindByIdentificador() {
        TypedQuery<Notificacion> mockQuery = mock(TypedQuery.class);
        List<Notificacion> resultList = List.of(new Notificacion());

        when(entityManager.createQuery(anyString(), eq(Notificacion.class))).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq("identificador"), eq("12345678A"))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(resultList);

        List<Notificacion> result = notificacionHibernate.findByIdentificador("12345678A");

        assertEquals(1, result.size());
        verify(mockQuery).setParameter("identificador", "12345678A");
    }
}
