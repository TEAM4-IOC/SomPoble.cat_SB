package com.sompoble.cat.repository.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.service.ServicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
@Transactional
@ExtendWith(MockitoExtension.class)
class ReservaHibernateTest {

    @Autowired
    private ReservaHibernate reservaHibernate;

    @Autowired
    private EntityManager entityManager;

    @Mock
    private ClienteService clienteService;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private ServicioService servicioService;

    private Cliente cliente;
    private Empresario empresario;
    private Empresa empresa;
    private Servicio servicio;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        entityManager.persist(cliente);

        empresario = new Empresario();
        empresario.setDni("87654321B");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180801");
        empresario.setPass("pass");
        entityManager.persist(empresario);

        empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);
        entityManager.persist(empresa);

        servicio = new Servicio();
        servicio.setNombre("Servicio de prueba");
        servicio.setDescripcion("Descripción del servicio");
        servicio.setPrecio(50);
        servicio.setDuracion(60);
        servicio.setEmpresa(empresa);
        entityManager.persist(servicio);

        // Eliminamos los stubs innecesarios:
        // when(clienteService.findByDniFull("12345678A")).thenReturn(cliente);
        // when(empresaService.findByIdentificadorFiscalFull("A12345678")).thenReturn(empresa);
        // when(servicioService.obtenerPorId(servicio.getIdServicio())).thenReturn(servicio);
        entityManager.flush();
    }

    @Test
    void addReservaTest() {
        Reserva reserva = new Reserva();
        reserva.setFechaReserva("2023-05-15");
        reserva.setHora("10:00");
        reserva.setEstado("PENDIENTE");
        reserva.setCliente(cliente);
        reserva.setEmpresa(empresa);
        reserva.setServicio(servicio);

        reservaHibernate.addReserva(reserva);

        Reserva reservaPersistida = entityManager.find(Reserva.class, reserva.getIdReserva());
        assertNotNull(reservaPersistida);
        assertEquals(reserva.getFechaReserva(), reservaPersistida.getFechaReserva());
        assertEquals(reserva.getHora(), reservaPersistida.getHora());
    }

    @Test
    void updateReservaTest() {
        Reserva reserva = new Reserva();
        reserva.setFechaReserva("2023-05-15");
        reserva.setHora("10:00");
        reserva.setEstado("PENDIENTE");
        reserva.setCliente(cliente);
        reserva.setEmpresa(empresa);
        reserva.setServicio(servicio);

        reservaHibernate.addReserva(reserva);

        reserva.setEstado("CONFIRMADA");
        reservaHibernate.updateReserva(reserva);

        Reserva reservaActualizada = entityManager.find(Reserva.class, reserva.getIdReserva());
        assertNotNull(reservaActualizada);
        assertEquals("CONFIRMADA", reservaActualizada.getEstado());
    }

    @Test
    void findByIdTest() {
        Reserva reserva = new Reserva();
        reserva.setFechaReserva("2023-05-15");
        reserva.setHora("10:00");
        reserva.setEstado("PENDIENTE");
        reserva.setCliente(cliente);
        reserva.setEmpresa(empresa);
        reserva.setServicio(servicio);

        reservaHibernate.addReserva(reserva);

        ReservaDTO result = reservaHibernate.findById(reserva.getIdReserva());
        assertNotNull(result);
        assertEquals(reserva.getFechaReserva(), result.getFechaReserva());
        assertEquals(reserva.getHora(), result.getHora());
        assertEquals(cliente.getDni(), result.getDniCliente());
        assertEquals(empresa.getIdentificadorFiscal(), result.getIdentificadorFiscalEmpresa());

        ReservaDTO resultNoExiste = reservaHibernate.findById(-1L);
        assertNull(resultNoExiste);
    }

    @Test
    void findByIdFullTest() {
        Reserva reserva = new Reserva();
        reserva.setFechaReserva("2023-05-15");
        reserva.setHora("10:00");
        reserva.setEstado("PENDIENTE");
        reserva.setCliente(cliente);
        reserva.setEmpresa(empresa);
        reserva.setServicio(servicio);

        reservaHibernate.addReserva(reserva);

        Reserva result = reservaHibernate.findByIdFull(reserva.getIdReserva());
        assertNotNull(result);
        assertEquals(reserva.getFechaReserva(), result.getFechaReserva());
        assertEquals(reserva.getHora(), result.getHora());
        assertEquals(cliente.getDni(), result.getCliente().getDni());
        assertEquals(empresa.getIdentificadorFiscal(), result.getEmpresa().getIdentificadorFiscal());

        Reserva resultNoExiste = reservaHibernate.findByIdFull(-1L);
        assertNull(resultNoExiste);
    }

    @Test
    void findByClienteDniTest() {
        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva("2023-05-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva("2023-05-16");
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);

        reservaHibernate.addReserva(reserva1);
        reservaHibernate.addReserva(reserva2);

        List<ReservaDTO> result = reservaHibernate.findByClienteDni(cliente.getDni());
        assertNotNull(result);
        assertEquals(2, result.size());

        List<ReservaDTO> resultNoExiste = reservaHibernate.findByClienteDni("99999999Z");
        assertNotNull(resultNoExiste);
        assertTrue(resultNoExiste.isEmpty());
    }

    @Test
    void findByEmpresaIdentificadorFiscalTest() {
        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva("2023-05-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva("2023-05-16");
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);

        reservaHibernate.addReserva(reserva1);
        reservaHibernate.addReserva(reserva2);

        List<ReservaDTO> result = reservaHibernate.findByEmpresaIdentificadorFiscal(empresa.getIdentificadorFiscal());
        assertNotNull(result);
        assertEquals(2, result.size());

        List<ReservaDTO> resultNoExiste = reservaHibernate.findByEmpresaIdentificadorFiscal("Z98765432");
        assertNotNull(resultNoExiste);
        assertTrue(resultNoExiste.isEmpty());
    }

    @Test
    void deleteByIdTest() {
        Reserva reserva = new Reserva();
        reserva.setFechaReserva("2023-05-15");
        reserva.setHora("10:00");
        reserva.setEstado("PENDIENTE");
        reserva.setCliente(cliente);
        reserva.setEmpresa(empresa);
        reserva.setServicio(servicio);

        reservaHibernate.addReserva(reserva);

        reservaHibernate.deleteById(reserva.getIdReserva());

        Reserva reservaEliminada = entityManager.find(Reserva.class, reserva.getIdReserva());
        assertNull(reservaEliminada);
    }

    @Test
    void deleteByClienteDniTest() {
        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva("2023-05-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva("2023-05-16");
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);

        reservaHibernate.addReserva(reserva1);
        reservaHibernate.addReserva(reserva2);

        reservaHibernate.deleteByClienteDni(cliente.getDni());

        List<ReservaDTO> reservas = reservaHibernate.findByClienteDni(cliente.getDni());
        assertTrue(reservas.isEmpty());
    }

    @Test
    void deleteByEmpresaIdentificadorFiscalTest() {
        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva("2023-05-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva("2023-05-16");
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);

        reservaHibernate.addReserva(reserva1);
        reservaHibernate.addReserva(reserva2);

        reservaHibernate.deleteByEmpresaIdentificadorFiscal(empresa.getIdentificadorFiscal());

        List<ReservaDTO> reservas = reservaHibernate.findByEmpresaIdentificadorFiscal(empresa.getIdentificadorFiscal());
        assertTrue(reservas.isEmpty());
    }

    @Test
    void convertToEntityTest() {
        ReservaDTO reservaDTO = new ReservaDTO(
                null,
                "2023-05-15",
                "10:00",
                "PENDIENTE",
                cliente.getDni(),
                empresa.getIdentificadorFiscal(),
                servicio.getIdServicio()
        );

        Reserva reserva = reservaHibernate.convertToEntity(reservaDTO);

        assertNotNull(reserva);
        assertEquals(reservaDTO.getFechaReserva(), reserva.getFechaReserva());
        assertEquals(reservaDTO.getHora(), reserva.getHora());
        assertEquals(reservaDTO.getEstado(), reserva.getEstado());
        assertEquals(cliente, reserva.getCliente());
        assertEquals(empresa, reserva.getEmpresa());
        assertEquals(servicio, reserva.getServicio());
    }

    @Test
    void countByServicioIdAndFechaReservaTest() {
        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva("2023-05-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva("2023-05-15");
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);

        Reserva reserva3 = new Reserva();
        reserva3.setFechaReserva("2023-05-16");
        reserva3.setHora("10:00");
        reserva3.setEstado("PENDIENTE");
        reserva3.setCliente(cliente);
        reserva3.setEmpresa(empresa);
        reserva3.setServicio(servicio);

        reservaHibernate.addReserva(reserva1);
        reservaHibernate.addReserva(reserva2);
        reservaHibernate.addReserva(reserva3);

        int count = reservaHibernate.countByServicioIdAndFechaReserva(servicio.getIdServicio(), "2023-05-15");
        assertEquals(2, count);

        int countOtraFecha = reservaHibernate.countByServicioIdAndFechaReserva(servicio.getIdServicio(), "2023-05-16");
        assertEquals(1, countOtraFecha);

        int countFechaInexistente = reservaHibernate.countByServicioIdAndFechaReserva(servicio.getIdServicio(), "2023-05-17");
        assertEquals(0, countFechaInexistente);
    }
}
