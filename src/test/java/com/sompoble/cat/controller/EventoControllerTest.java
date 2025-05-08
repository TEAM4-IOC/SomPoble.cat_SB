package com.sompoble.cat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.sompoble.cat.domain.Evento;
import com.sompoble.cat.service.EventoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Clase de test para probar la funcionalidad del controlador EventoController.
 * 
 * Estos tests verifican que los endpoints del controlador manejen correctamente 
 * diferentes escenarios, incluyendo operaciones CRUD exitosas, manejo de errores,
 * y consultas personalizadas. Se utiliza MockMvc para simular peticiones HTTP
 * sin necesidad de un servidor web real y MockBean para simular el comportamiento
 * del servicio de eventos.
 */
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
    private LocalDateTime fechaEvento;

    /**
     * Configura el entorno de prueba antes de cada test.
     * Inicializa el ObjectMapper con soporte para fechas,
     * y crea un evento de ejemplo para usar en las pruebas.
     */
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper(); 
        objectMapper.registerModule(new JavaTimeModule()); 

        fechaEvento = LocalDateTime.now();
        
        evento = new Evento();
        evento.setIdEvento(1L);
        evento.setNombre("Concierto");
        evento.setUbicacion("Madrid");
        evento.setFechaEvento(fechaEvento);
    }
    
    /**
     * Prueba la creación exitosa de un evento.
     * Verifica que el endpoint POST /api/eventos procese correctamente
     * los datos y devuelva el evento creado con un código 201.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testCrearEvento() throws Exception {
        Evento eventoGuardado = new Evento();
        eventoGuardado.setIdEvento(1L);
        eventoGuardado.setNombre("Concierto Guardado");
        eventoGuardado.setUbicacion("Madrid");
        eventoGuardado.setFechaEvento(fechaEvento);

        when(eventoService.guardarEvento(any(Evento.class))).thenReturn(eventoGuardado);

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEvento").value(1L))
                .andExpect(jsonPath("$.nombre").value("Concierto Guardado"));
    }
    
    /**
     * Prueba la validación al intentar crear un evento nulo.
     * Verifica que se maneje correctamente la excepción cuando
     * se envía un evento nulo.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testCrearEventoNulo() throws Exception {
        when(eventoService.guardarEvento(null)).thenThrow(new NullPointerException("El evento no puede ser nulo"));

        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }
    
    /**
     * Prueba la obtención de la lista de todos los eventos.
     * Verifica que el endpoint GET /api/eventos devuelva correctamente
     * todos los eventos disponibles.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testListarEventos() throws Exception {
        List<Evento> eventos = Arrays.asList(evento);
        when(eventoService.listarTodosLosEventos()).thenReturn(eventos);

        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].idEvento").value(1L))
                .andExpect(jsonPath("$.[0].nombre").value("Concierto"));
    }
    
    /**
     * Prueba la obtención de la lista de eventos cuando no hay eventos.
     * Verifica que el endpoint GET /api/eventos devuelva una lista vacía
     * cuando no hay eventos disponibles.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testListarEventosVacio() throws Exception {
        when(eventoService.listarTodosLosEventos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
    
    /**
     * Prueba la obtención de un evento existente por su ID.
     * Verifica que el endpoint GET /api/eventos/{id} devuelva correctamente
     * el evento cuando existe.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testObtenerEventoExistente() throws Exception {
        when(eventoService.obtenerEventoPorId(1L)).thenReturn(evento);

        mockMvc.perform(get("/api/eventos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEvento").value(1L))
                .andExpect(jsonPath("$.nombre").value("Concierto"));
    }

    /**
     * Prueba la obtención de un evento inexistente por su ID.
     * Verifica que el endpoint GET /api/eventos/{id} devuelva un error 404
     * cuando el evento no existe.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testObtenerEventoInexistente() throws Exception {
        when(eventoService.obtenerEventoPorId(999L)).thenReturn(null);
        
        mockMvc.perform(get("/api/eventos/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Prueba la eliminación exitosa de un evento existente.
     * Verifica que el endpoint DELETE /api/eventos/{id} elimine correctamente
     * el evento y devuelva un código 204.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testEliminarEventoExistente() throws Exception {
        when(eventoService.obtenerEventoPorId(1L)).thenReturn(evento);
        when(eventoService.eliminarEvento(1L)).thenReturn(true);
        
        mockMvc.perform(delete("/api/eventos/1"))
                .andExpect(status().isNoContent()); 
    }

    /**
     * Prueba la eliminación de un evento inexistente.
     * Verifica que el endpoint DELETE /api/eventos/{id} devuelva un error 404
     * cuando el evento no existe.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testEliminarEventoInexistente() throws Exception {
        when(eventoService.obtenerEventoPorId(999L)).thenReturn(null);
        when(eventoService.eliminarEvento(999L)).thenReturn(false);
        
        mockMvc.perform(delete("/api/eventos/999"))
                .andExpect(status().isNotFound()); 
    }
    
    /**
     * Prueba el caso en que la eliminación falla por un error interno.
     * Verifica que el endpoint DELETE /api/eventos/{id} devuelva un error 500
     * cuando el evento existe pero no se puede eliminar.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testEliminarEventoFallo() throws Exception {
        when(eventoService.obtenerEventoPorId(1L)).thenReturn(evento);
        when(eventoService.eliminarEvento(1L)).thenReturn(false);
        
        mockMvc.perform(delete("/api/eventos/1"))
                .andExpect(status().isInternalServerError()); 
    }

    /**
     * Prueba la actualización exitosa de un evento existente.
     * Verifica que el endpoint PUT /api/eventos/actualizar/{id} actualice correctamente
     * los datos del evento y devuelva el evento actualizado.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testActualizarEventoExistente() throws Exception {
        Evento eventoActualizado = new Evento();
        eventoActualizado.setIdEvento(1L);
        eventoActualizado.setNombre("Concierto Actualizado");
        eventoActualizado.setUbicacion("Barcelona");
        eventoActualizado.setFechaEvento(fechaEvento);

        when(eventoService.actualizarEvento(eq(1L), any(Evento.class))).thenReturn(eventoActualizado);

        mockMvc.perform(put("/api/eventos/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Concierto Actualizado"));
    }

    /**
     * Prueba la actualización de un evento inexistente.
     * Verifica que el endpoint PUT /api/eventos/actualizar/{id} devuelva un error 404
     * cuando el evento no existe.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testActualizarEventoInexistente() throws Exception {
        when(eventoService.actualizarEvento(eq(999L), any(Evento.class))).thenReturn(null);

        mockMvc.perform(put("/api/eventos/actualizar/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isNotFound());
    }
    
    /**
     * Prueba la validación al intentar actualizar con un ID nulo.
     * Verifica que se maneje correctamente la excepción cuando
     * se intenta actualizar un evento con un ID nulo.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testActualizarEventoConIdNulo() throws Exception {
        // La implementación del controlador utiliza requireNonNull para validar el ID
        // lo que generará una excepción 500 en vez de 400
        mockMvc.perform(put("/api/eventos/actualizar/null")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isInternalServerError());
    }
    
    /**
     * Prueba la validación al intentar actualizar con un evento nulo.
     * Verifica que se maneje correctamente el envío de un cuerpo de solicitud vacío.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testActualizarEventoNulo() throws Exception {
        // Spring no convierte un cuerpo vacío en null, sino que arroja un error al intentar deserializar
        mockMvc.perform(put("/api/eventos/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
        
        // No necesitamos un when() aquí porque el error ocurre antes de llegar al servicio
    }

    /**
     * Prueba la búsqueda de eventos por rango de fechas.
     * Verifica que el endpoint GET /api/eventos/fecha devuelva correctamente
     * los eventos dentro del rango de fechas especificado.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testBuscarEventosPorFecha() throws Exception {
        LocalDateTime start = fechaEvento.minusDays(1);
        LocalDateTime end = fechaEvento.plusDays(1);

        List<Evento> eventos = Arrays.asList(evento);
        when(eventoService.buscarEventosPorFecha(start, end)).thenReturn(eventos);

        mockMvc.perform(get("/api/eventos/fecha")
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].idEvento").value(1L))
                .andExpect(jsonPath("$.[0].nombre").value("Concierto"));
    }
    
    /**
     * Prueba la validación al buscar eventos con fecha de inicio nula.
     * Verifica que se maneje correctamente la excepción cuando
     * se busca eventos con una fecha de inicio nula.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testBuscarEventosPorFechaInicioNula() throws Exception {
        LocalDateTime end = fechaEvento.plusDays(1);
        
        // La implementación del controlador utiliza requireNonNull para validar la fecha de inicio
        // lo que generará una excepción 500 en vez de 400
        mockMvc.perform(get("/api/eventos/fecha")
                .param("end", end.toString()))
                .andExpect(status().isInternalServerError());
    }
    
    /**
     * Prueba la validación al buscar eventos con fecha de fin nula.
     * Verifica que se maneje correctamente la excepción cuando
     * se busca eventos con una fecha de fin nula.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testBuscarEventosPorFechaFinNula() throws Exception {
        LocalDateTime start = fechaEvento.minusDays(1);
        
        // La implementación del controlador utiliza requireNonNull para validar la fecha de fin
        // lo que generará una excepción 500 en vez de 400
        mockMvc.perform(get("/api/eventos/fecha")
                .param("start", start.toString()))
                .andExpect(status().isInternalServerError());
    }
    
    /**
     * Prueba la búsqueda de eventos por fecha cuando no hay eventos.
     * Verifica que el endpoint GET /api/eventos/fecha devuelva una lista vacía
     * cuando no hay eventos en el rango de fechas especificado.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testBuscarEventosPorFechaSinResultados() throws Exception {
        LocalDateTime start = fechaEvento.minusDays(10);
        LocalDateTime end = fechaEvento.minusDays(5);
        
        when(eventoService.buscarEventosPorFecha(start, end)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/eventos/fecha")
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    /**
     * Prueba la búsqueda de eventos por ubicación.
     * Verifica que el endpoint GET /api/eventos/ubicacion/{ubicacion} devuelva correctamente
     * los eventos en la ubicación especificada.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testBuscarEventosPorUbicacion() throws Exception {
        List<Evento> eventos = Arrays.asList(evento);
        when(eventoService.buscarEventosPorUbicacion("Madrid")).thenReturn(eventos);

        mockMvc.perform(get("/api/eventos/ubicacion/Madrid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].idEvento").value(1L))
                .andExpect(jsonPath("$.[0].nombre").value("Concierto"));
    }
    
    /**
     * Prueba el comportamiento cuando se buscan eventos con la palabra "null" como ubicación.
     * La URL /api/eventos/ubicacion/null se interpreta como una cadena "null", no como un valor nulo.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testBuscarEventosPorUbicacionNula() throws Exception {
        // Cuando pasamos "null" como parte de la URL, Spring lo interpreta como una cadena "null",
        // no como un valor Java null. Por lo tanto, debemos esperar que funcione normalmente.
        when(eventoService.buscarEventosPorUbicacion("null")).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/eventos/ubicacion/null"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
    
    /**
     * Prueba la búsqueda de eventos por ubicación cuando no hay eventos.
     * Verifica que el endpoint GET /api/eventos/ubicacion/{ubicacion} devuelva una lista vacía
     * cuando no hay eventos en la ubicación especificada.
     * 
     * @throws Exception Si ocurre un error durante la ejecución de la prueba
     */
    @Test
    void testBuscarEventosPorUbicacionSinResultados() throws Exception {
        when(eventoService.buscarEventosPorUbicacion("Barcelona")).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/eventos/ubicacion/Barcelona"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}