package com.sompoble.cat.repository.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.PanelMetricasDTO.MetricasMensualesDTO;
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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Test
    void deleteByServicioIdTest() {
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

        Servicio servicio2 = new Servicio();
        servicio2.setNombre("Otro servicio");
        servicio2.setDescripcion("Otra descripción");
        servicio2.setPrecio(75);
        servicio2.setDuracion(45);
        servicio2.setEmpresa(empresa);
        entityManager.persist(servicio2);

        Reserva reserva3 = new Reserva();
        reserva3.setFechaReserva("2023-05-17");
        reserva3.setHora("12:00");
        reserva3.setEstado("PENDIENTE");
        reserva3.setCliente(cliente);
        reserva3.setEmpresa(empresa);
        reserva3.setServicio(servicio2);
        reservaHibernate.addReserva(reserva3);

        reservaHibernate.deleteByServicioId(servicio.getIdServicio());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> cq = cb.createQuery(Reserva.class);
        Root<Reserva> root = cq.from(Reserva.class);
        Predicate servicioIdPredicate = cb.equal(root.get("servicio").get("idServicio"), servicio.getIdServicio());
        cq.where(servicioIdPredicate);
        List<Reserva> reservas = entityManager.createQuery(cq).getResultList();
        assertTrue(reservas.isEmpty(), "Deberían haberse eliminado todas las reservas del servicio");

        CriteriaQuery<Reserva> cq2 = cb.createQuery(Reserva.class);
        Root<Reserva> root2 = cq2.from(Reserva.class);
        Predicate servicio2IdPredicate = cb.equal(root2.get("servicio").get("idServicio"), servicio2.getIdServicio());
        cq2.where(servicio2IdPredicate);
        List<Reserva> reservasServicio2 = entityManager.createQuery(cq2).getResultList();
        assertEquals(1, reservasServicio2.size(), "La reserva del segundo servicio debe seguir existiendo");
    }

    @Test
    void contarClientesUnicosTest() {
        Cliente cliente2 = new Cliente();
        cliente2.setDni("22222222B");
        cliente2.setNombre("Ana");
        cliente2.setApellidos("Gomez Lopez");
        cliente2.setEmail("ana@ejemplo.com");
        cliente2.setTelefono("650180802");
        cliente2.setPass("pass");
        entityManager.persist(cliente2);

        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva("2023-05-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);
        reservaHibernate.addReserva(reserva1);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva("2023-05-16");
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);
        reservaHibernate.addReserva(reserva2);

        Reserva reserva3 = new Reserva();
        reserva3.setFechaReserva("2023-05-17");
        reserva3.setHora("12:00");
        reserva3.setEstado("PENDIENTE");
        reserva3.setCliente(cliente2);
        reserva3.setEmpresa(empresa);
        reserva3.setServicio(servicio);
        reservaHibernate.addReserva(reserva3);

        Reserva reserva4 = new Reserva();
        reserva4.setFechaReserva("2023-06-01");
        reserva4.setHora("09:00");
        reserva4.setEstado("PENDIENTE");
        reserva4.setCliente(cliente);
        reserva4.setEmpresa(empresa);
        reserva4.setServicio(servicio);
        reservaHibernate.addReserva(reserva4);

        Integer clientesUnicos = reservaHibernate.contarClientesUnicos(
                empresa.getIdEmpresa(),
                LocalDate.of(2023, 5, 1),
                LocalDate.of(2023, 5, 31)
        );

        assertEquals(2, clientesUnicos, "Debe haber exactamente 2 clientes únicos en el período");
    }

    @Test
    void contarReservasPorEmpresaYFechasTest() {
        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva("2023-05-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);
        reservaHibernate.addReserva(reserva1);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva("2023-05-16"); 
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);
        reservaHibernate.addReserva(reserva2);

        Reserva reserva3 = new Reserva();
        reserva3.setFechaReserva("2023-06-01");
        reserva3.setHora("09:00");
        reserva3.setEstado("PENDIENTE");
        reserva3.setCliente(cliente);
        reserva3.setEmpresa(empresa);
        reserva3.setServicio(servicio);
        reservaHibernate.addReserva(reserva3);

        Long reservasMayo = reservaHibernate.contarReservasPorEmpresaYFechas(
                empresa.getIdEmpresa(),
                LocalDate.of(2023, 5, 1),
                LocalDate.of(2023, 5, 31)
        );

        assertEquals(2L, reservasMayo, "Debe haber exactamente 2 reservas en mayo");

        Long reservasJunio = reservaHibernate.contarReservasPorEmpresaYFechas(
                empresa.getIdEmpresa(),
                LocalDate.of(2023, 6, 1),
                LocalDate.of(2023, 6, 30)
        );

        assertEquals(1L, reservasJunio, "Debe haber exactamente 1 reserva en junio");
    }

    @Test
    void sumarIngresosPorEmpresaYFechasTest() {
        Servicio servicio1 = servicio;

        Servicio servicio2 = new Servicio();
        servicio2.setNombre("Servicio premium");
        servicio2.setDescripcion("Descripción del servicio premium");
        servicio2.setPrecio(100);
        servicio2.setDuracion(90);
        servicio2.setEmpresa(empresa);
        entityManager.persist(servicio2);

        Reserva reserva1 = new Reserva();
        reserva1.setFechaReserva("2023-05-15"); 
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio1); 
        reservaHibernate.addReserva(reserva1);

        Reserva reserva2 = new Reserva();
        reserva2.setFechaReserva("2023-05-16"); 
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio2);
        reservaHibernate.addReserva(reserva2);

        Reserva reserva3 = new Reserva();
        reserva3.setFechaReserva("2023-06-01"); 
        reserva3.setHora("09:00");
        reserva3.setEstado("PENDIENTE");
        reserva3.setCliente(cliente);
        reserva3.setEmpresa(empresa);
        reserva3.setServicio(servicio1); 
        reservaHibernate.addReserva(reserva3);

        Double ingresosMayo = reservaHibernate.sumarIngresosPorEmpresaYFechas(
                empresa.getIdEmpresa(),
                LocalDate.of(2023, 5, 1),
                LocalDate.of(2023, 5, 31)
        );

        assertEquals(150.0, ingresosMayo, 0.001, "Los ingresos en mayo deben ser 150€");

        Double ingresosJunio = reservaHibernate.sumarIngresosPorEmpresaYFechas(
                empresa.getIdEmpresa(),
                LocalDate.of(2023, 6, 1),
                LocalDate.of(2023, 6, 30)
        );

        assertEquals(50.0, ingresosJunio, 0.001, "Los ingresos en junio deben ser 50€");
    }
}
