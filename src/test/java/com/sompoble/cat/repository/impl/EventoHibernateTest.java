package com.sompoble.cat.repository.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Evento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@Transactional
class EventoHibernateTest {

    @Autowired
    private EventoHibernate eventoHibernate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void saveTest() {
        // Crear un nuevo evento
        Evento evento = new Evento();
        evento.setNombre("Evento de prueba");
        evento.setDescripcion("Descripción del evento de prueba");
        evento.setFechaEvento(LocalDateTime.now().plusDays(1));
        evento.setUbicacion("Barcelona");

        // Guardar el evento
        Evento eventoPersistido = eventoHibernate.save(evento);

        // Verificar que se haya guardado correctamente
        assertNotNull(eventoPersistido.getIdEvento());
        Evento eventoRecuperado = entityManager.find(Evento.class, eventoPersistido.getIdEvento());
        assertNotNull(eventoRecuperado);
        assertEquals(evento.getNombre(), eventoRecuperado.getNombre());
    }

    @Test
    void updateTest() {
        // Crear y guardar un evento
        Evento evento = new Evento();
        evento.setNombre("Evento inicial");
        evento.setDescripcion("Descripción inicial");
        evento.setFechaEvento(LocalDateTime.now().plusDays(1));
        evento.setUbicacion("Barcelona");
        Evento eventoPersistido = eventoHibernate.save(evento);
        
        // Modificar el evento y guardarlo
        eventoPersistido.setNombre("Evento actualizado");
        Evento eventoActualizado = eventoHibernate.save(eventoPersistido);
        
        // Verificar que se haya actualizado correctamente
        assertEquals(eventoPersistido.getIdEvento(), eventoActualizado.getIdEvento());
        assertEquals("Evento actualizado", eventoActualizado.getNombre());
        
        // Verificar en la base de datos
        Evento eventoRecuperado = entityManager.find(Evento.class, eventoActualizado.getIdEvento());
        assertEquals("Evento actualizado", eventoRecuperado.getNombre());
    }

    @Test
    void deleteTest() {
        // Crear y guardar un evento
        Evento evento = new Evento();
        evento.setNombre("Evento a eliminar");
        evento.setDescripcion("Descripción del evento a eliminar");
        evento.setFechaEvento(LocalDateTime.now().plusDays(1));
        evento.setUbicacion("Barcelona");
        Evento eventoPersistido = eventoHibernate.save(evento);
        
        // Eliminar el evento
        eventoHibernate.delete(eventoPersistido.getIdEvento());
        
        // Verificar que se haya eliminado
        Evento eventoEliminado = entityManager.find(Evento.class, eventoPersistido.getIdEvento());
        assertNull(eventoEliminado);
    }
    
    @Test
    void deleteNonExistentTest() {
        // Intentar eliminar un evento que no existe
        assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            eventoHibernate.delete(-1L);
        });
    }

    @Test
    void findByIdTest() {
        // Crear y guardar un evento
        Evento evento = new Evento();
        evento.setNombre("Evento para buscar");
        evento.setDescripcion("Descripción del evento para buscar");
        evento.setFechaEvento(LocalDateTime.now().plusDays(1));
        evento.setUbicacion("Barcelona");
        Evento eventoPersistido = eventoHibernate.save(evento);
        
        // Buscar el evento por ID
        Evento eventoEncontrado = eventoHibernate.findById(eventoPersistido.getIdEvento());
        
        // Verificar que se haya encontrado correctamente
        assertNotNull(eventoEncontrado);
        assertEquals(eventoPersistido.getIdEvento(), eventoEncontrado.getIdEvento());
        
        // Buscar un ID que no existe
        Evento eventoNoExistente = eventoHibernate.findById(-1L);
        assertNull(eventoNoExistente);
    }

    @Test
    void findAllTest() {
        // Crear y guardar algunos eventos
        Evento evento1 = new Evento();
        evento1.setNombre("Evento 1");
        evento1.setDescripcion("Descripción del evento 1");
        evento1.setFechaEvento(LocalDateTime.now().plusDays(1));
        evento1.setUbicacion("Barcelona");
        eventoHibernate.save(evento1);
        
        Evento evento2 = new Evento();
        evento2.setNombre("Evento 2");
        evento2.setDescripcion("Descripción del evento 2");
        evento2.setFechaEvento(LocalDateTime.now().plusDays(2));
        evento2.setUbicacion("Madrid");
        eventoHibernate.save(evento2);
        
        // Obtener todos los eventos
        List<Evento> eventos = eventoHibernate.findAll();
        
        // Verificar que se hayan encontrado al menos los dos eventos creados
        assertNotNull(eventos);
        assertTrue(eventos.size() >= 2);
        
        // Verificar que los eventos creados estén en la lista
        boolean encontradoEvento1 = false;
        boolean encontradoEvento2 = false;
        
        for (Evento evento : eventos) {
            if (evento.getIdEvento().equals(evento1.getIdEvento())) {
                encontradoEvento1 = true;
            } else if (evento.getIdEvento().equals(evento2.getIdEvento())) {
                encontradoEvento2 = true;
            }
        }
        
        assertTrue(encontradoEvento1);
        assertTrue(encontradoEvento2);
    }

    @Test
    void findByFechaEventoBetweenTest() {
        // Limpiar eventos existentes que puedan interferir con este test
        entityManager.createQuery("DELETE FROM Evento").executeUpdate();
        entityManager.flush();
        
        // Crear y guardar eventos con diferentes fechas
        LocalDateTime ahora = LocalDateTime.now();
        
        Evento eventoPasado = new Evento();
        eventoPasado.setNombre("Evento pasado");
        eventoPasado.setDescripcion("Descripción del evento pasado");
        eventoPasado.setFechaEvento(ahora.minusDays(10));
        eventoPasado.setUbicacion("Barcelona");
        eventoHibernate.save(eventoPasado);
        
        Evento eventoFuturo1 = new Evento();
        eventoFuturo1.setNombre("Evento futuro 1");
        eventoFuturo1.setDescripcion("Descripción del evento futuro 1");
        eventoFuturo1.setFechaEvento(ahora.plusDays(5));
        eventoFuturo1.setUbicacion("Madrid");
        eventoHibernate.save(eventoFuturo1);
        
        Evento eventoFuturo2 = new Evento();
        eventoFuturo2.setNombre("Evento futuro 2");
        eventoFuturo2.setDescripcion("Descripción del evento futuro 2");
        eventoFuturo2.setFechaEvento(ahora.plusDays(15));
        eventoFuturo2.setUbicacion("Valencia");
        eventoHibernate.save(eventoFuturo2);
        
        // Buscar eventos en un rango de fechas específico
        LocalDateTime inicio = ahora.plusDays(1);
        LocalDateTime fin = ahora.plusDays(10);
        List<Evento> eventosEnRango = eventoHibernate.findByFechaEventoBetween(inicio, fin);
        
        // Verificar que solo se encuentre el evento que está dentro del rango
        assertNotNull(eventosEnRango);
        assertEquals(1, eventosEnRango.size());
        assertEquals(eventoFuturo1.getIdEvento(), eventosEnRango.get(0).getIdEvento());
    }

    @Test
    void findByUbicacionTest() {
        // Limpiar eventos existentes que puedan interferir con este test
        entityManager.createQuery("DELETE FROM Evento").executeUpdate();
        entityManager.flush();
        
        // Crear y guardar eventos con diferentes ubicaciones
        Evento evento1 = new Evento();
        evento1.setNombre("Evento Barcelona 1");
        evento1.setDescripcion("Descripción del evento Barcelona 1");
        evento1.setFechaEvento(LocalDateTime.now().plusDays(1));
        evento1.setUbicacion("Barcelona");
        eventoHibernate.save(evento1);
        
        Evento evento2 = new Evento();
        evento2.setNombre("Evento Barcelona 2");
        evento2.setDescripcion("Descripción del evento Barcelona 2");
        evento2.setFechaEvento(LocalDateTime.now().plusDays(2));
        evento2.setUbicacion("Barcelona");
        eventoHibernate.save(evento2);
        
        Evento evento3 = new Evento();
        evento3.setNombre("Evento Madrid");
        evento3.setDescripcion("Descripción del evento Madrid");
        evento3.setFechaEvento(LocalDateTime.now().plusDays(3));
        evento3.setUbicacion("Madrid");
        eventoHibernate.save(evento3);
        
        // Buscar eventos por ubicación
        List<Evento> eventosBarcelona = eventoHibernate.findByUbicacion("Barcelona");
        List<Evento> eventosMadrid = eventoHibernate.findByUbicacion("Madrid");
        List<Evento> eventosValencia = eventoHibernate.findByUbicacion("Valencia");
        
        // Verificar resultados
        assertNotNull(eventosBarcelona);
        assertEquals(2, eventosBarcelona.size());
        
        assertNotNull(eventosMadrid);
        assertEquals(1, eventosMadrid.size());
        
        assertNotNull(eventosValencia);
        assertEquals(0, eventosValencia.size());
    }

    @Test
    void findByUbicacionAndFechaEventoBetweenTest() {
        // Crear y guardar eventos con diferentes ubicaciones y fechas
        LocalDateTime ahora = LocalDateTime.now();
        
        Evento evento1 = new Evento();
        evento1.setNombre("Evento Barcelona 1");
        evento1.setDescripcion("Descripción del evento Barcelona 1");
        evento1.setFechaEvento(ahora.plusDays(5));
        evento1.setUbicacion("Barcelona");
        eventoHibernate.save(evento1);
        
        Evento evento2 = new Evento();
        evento2.setNombre("Evento Barcelona 2");
        evento2.setDescripcion("Descripción del evento Barcelona 2");
        evento2.setFechaEvento(ahora.plusDays(15));
        evento2.setUbicacion("Barcelona");
        eventoHibernate.save(evento2);
        
        Evento evento3 = new Evento();
        evento3.setNombre("Evento Madrid");
        evento3.setDescripcion("Descripción del evento Madrid");
        evento3.setFechaEvento(ahora.plusDays(5));
        evento3.setUbicacion("Madrid");
        eventoHibernate.save(evento3);
        
        // Buscar eventos por ubicación y rango de fechas
        LocalDateTime inicio = ahora.plusDays(1);
        LocalDateTime fin = ahora.plusDays(10);
        
        List<Evento> eventosBarcelonaEnRango = eventoHibernate.findByUbicacionAndFechaEventoBetween(
            "Barcelona", inicio, fin);
        
        // Verificar resultados
        assertNotNull(eventosBarcelonaEnRango);
        assertEquals(1, eventosBarcelonaEnRango.size());
        assertEquals(evento1.getIdEvento(), eventosBarcelonaEnRango.get(0).getIdEvento());
    }

    @Test
    void findByNombreContainingIgnoreCaseTest() {
        // Limpiar eventos existentes que puedan interferir con este test
        entityManager.createQuery("DELETE FROM Evento").executeUpdate();
        entityManager.flush();
        
        // Crear y guardar eventos con diferentes nombres
        Evento evento1 = new Evento();
        evento1.setNombre("Conferencia de Tecnología");
        evento1.setDescripcion("Descripción de la conferencia");
        evento1.setFechaEvento(LocalDateTime.now().plusDays(1));
        evento1.setUbicacion("Barcelona");
        eventoHibernate.save(evento1);
        
        Evento evento2 = new Evento();
        evento2.setNombre("Seminario de TECNOLOGÍA Avanzada");
        evento2.setDescripcion("Descripción del seminario");
        evento2.setFechaEvento(LocalDateTime.now().plusDays(2));
        evento2.setUbicacion("Madrid");
        eventoHibernate.save(evento2);
        
        Evento evento3 = new Evento();
        evento3.setNombre("Concierto de música");
        evento3.setDescripcion("Descripción del concierto");
        evento3.setFechaEvento(LocalDateTime.now().plusDays(3));
        evento3.setUbicacion("Valencia");
        eventoHibernate.save(evento3);
        
        // Buscar eventos por palabra clave en el nombre (ignorando mayúsculas/minúsculas)
        List<Evento> eventosTecnologia = eventoHibernate.findByNombreContainingIgnoreCase("tecnología");
        List<Evento> eventosConcierto = eventoHibernate.findByNombreContainingIgnoreCase("concierto");
        List<Evento> eventosNoExistentes = eventoHibernate.findByNombreContainingIgnoreCase("inexistente");
        
        // Verificar resultados
        assertNotNull(eventosTecnologia);
        assertEquals(2, eventosTecnologia.size());
        
        assertNotNull(eventosConcierto);
        assertEquals(1, eventosConcierto.size());
        
        assertNotNull(eventosNoExistentes);
        assertEquals(0, eventosNoExistentes.size());
    }

    @Test
    void findClosestEventTest() {
        // Limpiar eventos existentes que puedan interferir con este test
        entityManager.createQuery("DELETE FROM Evento").executeUpdate();
        entityManager.flush();
        
        // Crear y guardar eventos con diferentes fechas
        LocalDateTime ahora = LocalDateTime.now();
        
        Evento eventoPasado = new Evento();
        eventoPasado.setNombre("Evento pasado");
        eventoPasado.setDescripcion("Descripción del evento pasado");
        eventoPasado.setFechaEvento(ahora.minusDays(1));
        eventoPasado.setUbicacion("Barcelona");
        eventoHibernate.save(eventoPasado);
        
        Evento eventoFuturo1 = new Evento();
        eventoFuturo1.setNombre("Evento futuro cercano");
        eventoFuturo1.setDescripcion("Descripción del evento futuro cercano");
        eventoFuturo1.setFechaEvento(ahora.plusDays(2));
        eventoFuturo1.setUbicacion("Madrid");
        Evento savedEventoFuturo1 = eventoHibernate.save(eventoFuturo1);
        
        Evento eventoFuturo2 = new Evento();
        eventoFuturo2.setNombre("Evento futuro lejano");
        eventoFuturo2.setDescripcion("Descripción del evento futuro lejano");
        eventoFuturo2.setFechaEvento(ahora.plusDays(5));
        eventoFuturo2.setUbicacion("Valencia");
        eventoHibernate.save(eventoFuturo2);
        
        // Forzar flush para que todos los cambios se apliquen a la base de datos
        entityManager.flush();
        
        // Buscar el evento más cercano a la fecha actual
        Optional<Evento> eventoMasCercano = eventoHibernate.findClosestEvent();
        
        // Verificar que se haya encontrado el evento correcto
        assertTrue(eventoMasCercano.isPresent());
        
        // Verificar por nombre en lugar de por ID para evitar problemas con los IDs asignados
        assertEquals("Evento futuro cercano", eventoMasCercano.get().getNombre());
    }

    @Test
    void countTest() {
        // Limpiar eventos existentes que puedan interferir con este test
        entityManager.createQuery("DELETE FROM Evento").executeUpdate();
        entityManager.flush();
        
        // Verificar que inicialmente no hay eventos
        Long conteoInicial = eventoHibernate.count();
        assertEquals(0L, conteoInicial);
        
        // Crear y guardar algunos eventos
        Evento evento1 = new Evento();
        evento1.setNombre("Evento 1 para contar");
        evento1.setDescripcion("Descripción del evento 1");
        evento1.setFechaEvento(LocalDateTime.now().plusDays(1));
        evento1.setUbicacion("Barcelona");
        eventoHibernate.save(evento1);
        
        Evento evento2 = new Evento();
        evento2.setNombre("Evento 2 para contar");
        evento2.setDescripcion("Descripción del evento 2");
        evento2.setFechaEvento(LocalDateTime.now().plusDays(2));
        evento2.setUbicacion("Madrid");
        eventoHibernate.save(evento2);
        
        entityManager.flush();
        
        // Obtener el recuento de eventos
        Long cantidadEventos = eventoHibernate.count();
        
        // Verificar que el recuento sea exactamente 2
        assertEquals(2L, cantidadEventos);
        
        // Verificar que el recuento aumenta después de agregar otro evento
        Evento evento3 = new Evento();
        evento3.setNombre("Evento 3 para contar");
        evento3.setDescripcion("Descripción del evento 3");
        evento3.setFechaEvento(LocalDateTime.now().plusDays(3));
        evento3.setUbicacion("Valencia");
        eventoHibernate.save(evento3);
        
        entityManager.flush();
        
        Long nuevoConteo = eventoHibernate.count();
        assertEquals(3L, nuevoConteo);
    }
}