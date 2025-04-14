package com.sompoble.cat.repository.impl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.sompoble.cat.Application;

@SpringBootTest(classes = Application.class)
@Transactional
class ClienteHibernateTest {

    /*@Autowired
    private ClienteHibernate clienteHibernate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void addClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");

        clienteHibernate.addCliente(cliente);

        Cliente clientePersistido = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNotNull(clientePersistido);
        assertEquals(cliente.getDni(), clientePersistido.getDni());
    }

    @Test
    void updateClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        cliente.setNombre("Pedro");
        clienteHibernate.updateCliente(cliente);

        Cliente clienteActualizado = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNotNull(clienteActualizado);
        assertEquals("Pedro", clienteActualizado.getNombre());
    }

    @Test
    void findByDniTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        Cliente result = clienteHibernate.findByDNI("12345678A");
        assertNotNull(result);
        assertEquals(cliente.getDni(), result.getDni());
    }

    @Test
    void existsByDniTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        boolean result = clienteHibernate.existsByDni("12345678A");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        clienteHibernate.deleteById(cliente.getIdPersona());

        Cliente clienteEliminado = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNull(clienteEliminado);
    }

    /*
    @Test
    void findAllTest() {
        Cliente cliente1 = new Cliente();
        cliente1.setDni("12345678A");
        cliente1.setNombre("Juan");
        cliente1.setApellidos("Perez Garcia");
        cliente1.setEmail("juan@juan.es");
        cliente1.setTelefono("650180800");
        cliente1.setPass("pass");
        clienteHibernate.addCliente(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setDni("87654321B");
        cliente2.setNombre("Maria");
        cliente2.setApellidos("Lopez Garcia");
        cliente2.setEmail("maria@maria.es");
        cliente2.setTelefono("650180801");
        cliente2.setPass("pass");
        clienteHibernate.addCliente(cliente2);

        List<Cliente> clientes = clienteHibernate.findAll();
        assertNotNull(clientes);
        assertEquals(2, clientes.size());
    }
    */
/*
    @Test
    void existsByIdTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        boolean result = clienteHibernate.existsById(cliente.getIdPersona());
        assertTrue(result);
    }

    @Test
    void deleteByDniTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        clienteHibernate.deleteByDni(cliente.getDni());

        Cliente clienteEliminado = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNull(clienteEliminado);
    }

    @Test
    void existsByEmailTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        boolean result = clienteHibernate.existsByEmail("sergio@sergio.es");
        assertTrue(result);

        boolean resultNoExist = clienteHibernate.existsByEmail("noexistente@sergio.es");
        assertFalse(resultNoExist);
    }*/
}
