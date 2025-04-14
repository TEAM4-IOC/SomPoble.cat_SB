package com.sompoble.cat.repository.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sompoble.cat.domain.Evento;
import com.sompoble.cat.repository.EventoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EventoHibernateTest {
  
	 @Mock
	private EntityManager entityManager;
	
	 @Mock
	 private EventoRepository eventoRepository;
	 
	 @InjectMocks
	 private EventoHibernate eventoHibernate;

    private Evento evento;

    @BeforeEach
    void setUp() {
        evento = new Evento();
        evento.setNombre("Concierto de Primavera");
        evento.setFechaEvento(LocalDateTime.of(2025, 4, 10, 19, 30));
        evento.setUbicacion("Madrid");
    }

    @Test
    void testSaveAndFindById() {
        Evento savedEvento = new Evento();
        savedEvento.setIdEvento(1L);
        savedEvento.setNombre("Concierto de Primavera");
        savedEvento.setDescripcion("Concierto al aire libre.");
        savedEvento.setUbicacion("Madrid");
        savedEvento.setFechaEvento(LocalDateTime.of(2025, 4, 10, 19, 30));

        when(eventoRepository.save(evento)).thenReturn(savedEvento);
        when(eventoRepository.findById(1L)).thenReturn(savedEvento);

        Evento saved = eventoRepository.save(evento);
        Evento found = eventoRepository.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getIdEvento());
        assertEquals("Concierto de Primavera", found.getNombre());
    }
    @Test
    void testDelete() {
        Evento savedEvento = new Evento();
        savedEvento.setIdEvento(1L);
        savedEvento.setNombre("Concierto de Primavera");
        savedEvento.setDescripcion("Concierto al aire libre.");
        savedEvento.setUbicacion("Madrid");
        savedEvento.setFechaEvento(LocalDateTime.of(2025, 4, 10, 19, 30));

        lenient().when(eventoRepository.findById(1L)).thenReturn(savedEvento);
        lenient().doNothing().when(eventoRepository).delete(1L);

        eventoRepository.delete(1L);
    }
    @Test
    void testDeleteNonExistentEvent() {
        when(entityManager.find(Evento.class, 1L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> eventoHibernate.delete(1L));
        verify(entityManager).find(Evento.class, 1L);
    }
    
    
    @Test
    void testFindAll() {
        Evento evento1 = new Evento();
        evento1.setIdEvento(2L);
        evento1.setNombre("Feria de Otoño");
        List<Evento> eventos = List.of(evento, evento1);
        when(eventoRepository.findAll()).thenReturn(eventos);

        List<Evento> result = eventoRepository.findAll();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void testFindByFechaEventoBetween() {
        Evento eventoA = new Evento();
        eventoA.setFechaEvento(LocalDateTime.of(2025, 5, 1, 15, 0));
        List<Evento> eventos = List.of(evento); 
        when(eventoRepository.findByFechaEventoBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(eventos);

        List<Evento> result = eventoRepository.findByFechaEventoBetween(
                LocalDateTime.of(2025, 4, 1, 0, 0),
                LocalDateTime.of(2025, 5, 31, 23, 59)
        );

        assertEquals(1, result.size());
        assertEquals("Concierto de Primavera", result.get(0).getNombre());
    }

    @Test
    void testFindByUbicacion() {
        List<Evento> eventos = Collections.singletonList(evento);
        when(eventoRepository.findByUbicacion("Madrid")).thenReturn(eventos);

        List<Evento> result = eventoRepository.findByUbicacion("Madrid");

        assertEquals(1, result.size());
        assertEquals("Madrid", result.get(0).getUbicacion());
    }

    @Test
    void testFindByUbicacionAndFechaEventoBetween() {
        Evento eventoX = new Evento();
        eventoX.setNombre("Evento X");
        List<Evento> eventos = List.of(evento, eventoX);
        when(eventoRepository.findByUbicacionAndFechaEventoBetween(
                eq("Madrid"),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(eventos);

        List<Evento> result = eventoRepository.findByUbicacionAndFechaEventoBetween(
                "Madrid",
                LocalDateTime.of(2025, 4, 1, 0, 0),
                LocalDateTime.of(2025, 5, 31, 23, 59)
        );

        assertEquals(2, result.size());
    }

    @Test
    void testFindByNombreContainingIgnoreCase() {
        List<Evento> eventos = Collections.singletonList(evento);
        when(eventoRepository.findByNombreContainingIgnoreCase("primavera"))
                .thenReturn(eventos);

        List<Evento> result = eventoRepository.findByNombreContainingIgnoreCase("primavera");

        assertFalse(result.isEmpty());
        assertEquals("Concierto de Primavera", result.get(0).getNombre());
    }

    @Test
    void testFindClosestEvent() {
        when(eventoRepository.findClosestEvent()).thenReturn(Optional.of(evento));

        Optional<Evento> closestEvent = eventoRepository.findClosestEvent();

        assertTrue(closestEvent.isPresent());
        assertEquals("Concierto de Primavera", closestEvent.get().getNombre());
    }

    @Test
    void testCount() {
        when(eventoRepository.count()).thenReturn(2L);

        Long count = eventoRepository.count();

        assertEquals(2, count);
    }
}