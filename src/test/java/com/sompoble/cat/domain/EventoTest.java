package com.sompoble.cat.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventoTest {

    private Evento evento;

    @BeforeEach
    void setUp() {
        evento = new Evento("Concierto de Rock", "Concierto en el estadio", "Estadio Central", LocalDateTime.now().plusDays(1));
    }

    @Test
    void testCrearEvento() {
        assertEquals("Concierto de Rock", evento.getNombre());
        assertEquals("Concierto en el estadio", evento.getDescripcion());
        assertEquals("Estadio Central", evento.getUbicacion());
    }

    @Test
    void testFechasDeCreacionYModificacion() {
        Evento evento = new Evento("Nombre", "Descripción", "Ubicación", LocalDateTime.now());
        evento.prePersist();
        assertNotNull(evento.getFechaAlta());
        assertNotNull(evento.getFechaModificacion());
        assertTrue(LocalDateTime.now().isAfter(evento.getFechaAlta()));
        assertTrue(LocalDateTime.now().isAfter(evento.getFechaModificacion()));
    }

    @Test
    void testFechaEvento() {
        LocalDateTime fechaEventoEsperada = LocalDateTime.now().plusDays(1);
        assertEquals(fechaEventoEsperada.toLocalDate(), evento.getFechaEvento().toLocalDate());
    }

    @Test
    void testNombreMaximoDeLongitud() {
        String nombreValido = "A".repeat(100);
        Evento eventoValido = new Evento(nombreValido, "Descripción", "Ubicación", LocalDateTime.now());
        assertEquals(nombreValido, eventoValido.getNombre());

        
        String nombreExcedente = "B".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> {
            new Evento(nombreExcedente, "Descripción", "Ubicación", LocalDateTime.now());
        });
    }

    @Test
    void testUbicacionMaximoDeLongitud() {
        Evento eventoValido = new Evento("Nombre", "Descripción", "A".repeat(255), LocalDateTime.now());
        eventoValido.setUbicacion("B".repeat(255)); 
        assertEquals("B".repeat(255), eventoValido.getUbicacion());

        Evento evento = new Evento("Nombre", "Descripción", "A".repeat(255), LocalDateTime.now());
        String ubicacionExcedente = "B".repeat(256);

        assertThrows(IllegalArgumentException.class, () -> {
            evento.setUbicacion(ubicacionExcedente);
        });
    }
}
