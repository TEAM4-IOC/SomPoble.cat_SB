package com.sompoble.cat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.sompoble.cat.domain.Evento;
import com.sompoble.cat.service.EventoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoService eventoService;
    @Autowired
    private ObjectMapper objectMapper;

    private Evento evento;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper(); 
        objectMapper.registerModule(new JavaTimeModule()); 

        evento = new Evento();
        evento.setIdEvento(1L);
        evento.setNombre("Concierto");
        evento.setUbicacion("Madrid");
        evento.setFechaEvento(LocalDateTime.now());
    }
    @Test
    void testCrearEvento() throws Exception {
        Evento eventoGuardado = new Evento();
        eventoGuardado.setIdEvento(1L);
        eventoGuardado.setNombre("Concierto Guardado");
        eventoGuardado.setUbicacion("Madrid");
        eventoGuardado.setFechaEvento(LocalDateTime.now());

        when(eventoService.guardarEvento(any(Evento.class))).thenReturn(eventoGuardado);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEvento").value(1L))
                .andExpect(jsonPath("$.nombre").value("Concierto Guardado"));
    }
    @Test
    void testListarEventos() throws Exception {
        List<Evento> eventos = Arrays.asList(evento);
        when(eventoService.listarTodosLosEventos()).thenReturn(eventos);

        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].idEvento").value(1L))
                .andExpect(jsonPath("$.[0].nombre").value("Concierto"));
    }
    @Test
    void testObtenerEventoExistente() throws Exception {
        when(eventoService.obtenerEventoPorId(1L)).thenReturn(evento);

        mockMvc.perform(get("/api/eventos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEvento").value(1L))
                .andExpect(jsonPath("$.nombre").value("Concierto"));
    }

    @Test
    void testObtenerEventoInexistente() throws Exception {
        when(eventoService.obtenerEventoPorId(999L)).thenReturn(null);
        mockMvc.perform(get("/api/eventos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarEventoExistente() throws Exception {
        when(eventoService.obtenerEventoPorId(1L)).thenReturn(evento);
        when(eventoService.eliminarEvento(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/eventos/1"))
                .andExpect(status().isNoContent()); 
    }

    @Test
    void testEliminarEventoInexistente() throws Exception {
        when(eventoService.obtenerEventoPorId(999L)).thenReturn(null);
        when(eventoService.eliminarEvento(999L)).thenReturn(false);
        mockMvc.perform(delete("/api/eventos/999"))
                .andExpect(status().isNotFound()); 
    }

    @Test
    void testActualizarEventoExistente() throws Exception {
        Evento eventoActualizado = new Evento();
        eventoActualizado.setIdEvento(1L);
        eventoActualizado.setNombre("Concierto Actualizado");
        eventoActualizado.setUbicacion("Barcelona");
        eventoActualizado.setFechaEvento(LocalDateTime.now());

        when(eventoService.actualizarEvento(eq(1L), any(Evento.class))).thenReturn(eventoActualizado);

        mockMvc.perform(put("/api/eventos/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Concierto Actualizado"));
    }

    @Test
    void testActualizarEventoInexistente() throws Exception {
        when(eventoService.actualizarEvento(eq(999L), any(Evento.class))).thenReturn(null);

        mockMvc.perform(put("/api/eventos/actualizar/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBuscarEventosPorFecha() throws Exception {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        List<Evento> eventos = Arrays.asList(evento);
        when(eventoService.buscarEventosPorFecha(start, end)).thenReturn(eventos);

        mockMvc.perform(get("/api/eventos/fecha")
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].idEvento").value(1L))
                .andExpect(jsonPath("$.[0].nombre").value("Concierto"));
    }

    @Test
    void testBuscarEventosPorUbicacion() throws Exception {
        List<Evento> eventos = Arrays.asList(evento);
        when(eventoService.buscarEventosPorUbicacion("Madrid")).thenReturn(eventos);

        mockMvc.perform(get("/api/eventos/ubicacion/Madrid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].idEvento").value(1L)) // Cambiar "id" por "idEvento"
                .andExpect(jsonPath("$.[0].nombre").value("Concierto"));
    }
}