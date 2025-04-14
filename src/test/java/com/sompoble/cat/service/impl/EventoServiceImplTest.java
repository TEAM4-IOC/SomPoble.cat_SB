package com.sompoble.cat.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sompoble.cat.domain.Evento;
import com.sompoble.cat.exception.EventoNoEncontradoException;
import com.sompoble.cat.repository.EventoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class EventoServiceImplTest {

    @InjectMocks
    private EventoServiceImpl eventoService;

    @Mock
    private EventoRepository eventoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarEvento_deberiaGuardarYRetornarEvento() {
        Evento evento = new Evento();
        evento.setNombre("Concierto");

        when(eventoRepository.save(evento)).thenReturn(evento);

        Evento resultado = eventoService.guardarEvento(evento);

        assertNotNull(resultado);
        assertEquals("Concierto", resultado.getNombre());
    }

    @Test
    void eliminarEvento_deberiaEliminarEventoExistente() {
        Long id = 1L;
        Evento evento = new Evento();
        evento.setIdEvento(id);

        when(eventoRepository.findById(id)).thenReturn(evento);
        doNothing().when(eventoRepository).delete(id);

        boolean eliminado = eventoService.eliminarEvento(id);

        assertTrue(eliminado);
        verify(eventoRepository).delete(id);
    }

    @Test
    void eliminarEvento_deberiaRetornarFalseSiNoExiste() {
        Long id = 2L;

        when(eventoRepository.findById(id)).thenReturn(null);

        boolean eliminado = eventoService.eliminarEvento(id);

        assertFalse(eliminado);
        verify(eventoRepository, never()).delete(id);
    }

    @Test
    void obtenerEventoPorId_deberiaRetornarEventoSiExiste() {
        Long id = 3L;
        Evento evento = new Evento();
        evento.setIdEvento(id);

        when(eventoRepository.findById(id)).thenReturn(evento);

        Evento resultado = eventoService.obtenerEventoPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdEvento());
    }

    @Test
    void listarTodosLosEventos_deberiaRetornarListaEventos() {
        Evento evento1 = new Evento();
        Evento evento2 = new Evento();

        when(eventoRepository.findAll()).thenReturn(Arrays.asList(evento1, evento2));

        List<Evento> eventos = eventoService.listarTodosLosEventos();

        assertEquals(2, eventos.size());
    }

    @Test
    void buscarEventosPorFecha_deberiaRetornarEventosDentroDelRango() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);

        Evento evento = new Evento();

        when(eventoRepository.findByFechaEventoBetween(inicio, fin)).thenReturn(List.of(evento));

        List<Evento> eventos = eventoService.buscarEventosPorFecha(inicio, fin);

        assertFalse(eventos.isEmpty());
    }

    @Test
    void buscarEventosPorUbicacion_deberiaRetornarEventosDeUbicacion() {
        String ubicacion = "Barcelona";

        Evento evento = new Evento();
        evento.setUbicacion(ubicacion);

        when(eventoRepository.findByUbicacion(ubicacion)).thenReturn(List.of(evento));

        List<Evento> eventos = eventoService.buscarEventosPorUbicacion(ubicacion);

        assertEquals(1, eventos.size());
        assertEquals(ubicacion, eventos.get(0).getUbicacion());
    }

    @Test
    void actualizarEvento_deberiaActualizarYRetornarEvento() {
        Long id = 1L;
        Evento existente = new Evento();
        existente.setIdEvento(id);
        existente.setNombre("Viejo Nombre");

        Evento nuevo = new Evento();
        nuevo.setNombre("Nuevo Nombre");
        nuevo.setDescripcion("Descripción actualizada");
        nuevo.setFechaEvento(LocalDateTime.now());
        nuevo.setUbicacion("Madrid");

        when(eventoRepository.findById(id)).thenReturn(existente);
        when(eventoRepository.save(any(Evento.class))).thenAnswer(i -> i.getArguments()[0]);

        Evento actualizado = eventoService.actualizarEvento(id, nuevo);

        assertEquals("Nuevo Nombre", actualizado.getNombre());
        assertEquals("Madrid", actualizado.getUbicacion());
    }

    @Test
    void actualizarEvento_lanzaExcepcionSiNoExiste() {
        Long id = 99L;
        Evento nuevo = new Evento();

        when(eventoRepository.findById(id)).thenReturn(null);

        assertThrows(EventoNoEncontradoException.class, () -> {
            eventoService.actualizarEvento(id, nuevo);
        });
    }
}
