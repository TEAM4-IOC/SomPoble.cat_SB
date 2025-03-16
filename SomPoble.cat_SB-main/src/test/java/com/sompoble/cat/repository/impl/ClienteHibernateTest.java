package com.sompoble.cat.repository.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@Transactional
class ClienteHibernateTest {

    @Autowired
    private ClienteHibernate clienteHibernate; // El repositorio que estás probando

    @Autowired
    private EntityManager entityManager; // Para interactuar directamente con la base de datos

    @Test
    void addClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");

        clienteHibernate.addCliente(cliente);

        Cliente clientePersistido = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNotNull(clientePersistido);
        assertEquals(cliente.getDni(), clientePersistido.getDni());
    }

    @Test
    void updateClienteTest() {
        // Crear un cliente inicial
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");
        clienteHibernate.addCliente(cliente);

        // Actualizar el cliente
        cliente.setNombre("Pedro");
        clienteHibernate.updateCliente(cliente);

        // Verificar que el cliente ha sido actualizado
        Cliente clienteActualizado = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNotNull(clienteActualizado);
        assertEquals("Pedro", clienteActualizado.getNombre());
    }

    @Test
    void findByDniTest() {
        // Crear un cliente
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");
        clienteHibernate.addCliente(cliente);

        // Buscar cliente por DNI
        Cliente result = clienteHibernate.findByDNI("12345678A");
        assertNotNull(result);
        assertEquals(cliente.getDni(), result.getDni());
    }

    @Test
    void existsByDniTest() {
        // Crear un cliente
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");
        clienteHibernate.addCliente(cliente);

        // Verificar si el cliente existe por DNI
        boolean result = clienteHibernate.existsByDni("12345678A");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        // Crear un cliente
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");
        clienteHibernate.addCliente(cliente);

        // Eliminar el cliente por ID
        clienteHibernate.deleteById(cliente.getIdPersona());

        // Verificar que el cliente ha sido eliminado
        Cliente clienteEliminado = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNull(clienteEliminado);
    }

    @Test
    void findAllTest() {
        // Crear clientes
        Cliente cliente1 = new Cliente();
        cliente1.setDni("12345678A");
        cliente1.setNombre("Juan");
        cliente1.setApellidos("Perez Garcia");
        cliente1.setEmail("juan@juan.es");
        cliente1.setTelefono("650180800");
        cliente1.setContraseña("pass");
        clienteHibernate.addCliente(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setDni("87654321B");
        cliente2.setNombre("Maria");
        cliente2.setApellidos("Lopez Garcia");
        cliente2.setEmail("maria@maria.es");
        cliente2.setTelefono("650180801");
        cliente2.setContraseña("pass");
        clienteHibernate.addCliente(cliente2);

        // Verificar que ambos clientes están presentes
        List<Cliente> clientes = clienteHibernate.findAll();
        assertNotNull(clientes);
        assertEquals(2, clientes.size());
    }

    @Test
    void existsByIdTest() {
        // Crear un cliente
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");
        clienteHibernate.addCliente(cliente);

        // Verificar si el cliente existe por ID
        boolean result = clienteHibernate.existsById(cliente.getIdPersona());
        assertTrue(result);
    }

    @Test
    void deleteByDniTest() {
        // Crear un cliente
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");
        clienteHibernate.addCliente(cliente);

        // Eliminar el cliente por DNI
        clienteHibernate.deleteByDni(cliente.getDni());

        // Verificar que el cliente ha sido eliminado
        Cliente clienteEliminado = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNull(clienteEliminado);
    }

    @Test
    void existsByEmailTest() {
        // Crear un cliente
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");
        clienteHibernate.addCliente(cliente);

        // Verificar si el cliente existe por email
        boolean result = clienteHibernate.existsByEmail("sergio@sergio.es");
        assertTrue(result);

        // Verificar que un email no registrado no existe
        boolean resultNoExist = clienteHibernate.existsByEmail("noexistente@sergio.es");
        assertFalse(resultNoExist);
    }
}
