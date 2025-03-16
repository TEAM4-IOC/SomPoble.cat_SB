package com.sompoble.cat.service.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.repository.ClienteRepository;
import com.sompoble.cat.service.ClienteService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.dao.EmptyResultDataAccessException;

@SpringBootTest(classes = Application.class)
@Transactional
class ClienteServiceImplTest {

    @Autowired
    private ClienteService clienteService; // El servicio que estás probando

    @Autowired
    private ClienteRepository clienteRepository; // Para interactuar con el repositorio directamente

    @Test
    void addClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");

        clienteService.addCliente(cliente);

        Cliente clientePersistido = clienteRepository.findByDNI(cliente.getDni());
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
        clienteService.addCliente(cliente);

        // Actualizar el cliente
        cliente.setNombre("Pedro");
        clienteService.updateCliente(cliente);

        // Verificar que el cliente ha sido actualizado
        Cliente clienteActualizado = clienteRepository.findByDNI(cliente.getDni());
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
        clienteService.addCliente(cliente);

        // Buscar cliente por DNI
        Cliente result = clienteService.findByDni("12345678A");
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
        clienteService.addCliente(cliente);

        // Verificar si el cliente existe por DNI
        boolean result = clienteService.existsByDni("12345678A");
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
        clienteService.addCliente(cliente);

        // Eliminar el cliente por ID
        clienteService.deleteById(cliente.getIdPersona());

        // Verificar que el cliente ha sido eliminado
        try {
            Cliente clienteEliminado = clienteRepository.findByDNI(cliente.getDni());
            assertNull(clienteEliminado);
        } catch (EmptyResultDataAccessException e) {
            // Exception is expected because the client has been deleted
            assertTrue(true);
        }
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
        clienteService.addCliente(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setDni("87654321B");
        cliente2.setNombre("Maria");
        cliente2.setApellidos("Lopez Garcia");
        cliente2.setEmail("maria@maria.es");
        cliente2.setTelefono("650180801");
        cliente2.setContraseña("pass");
        clienteService.addCliente(cliente2);

        // Verificar que ambos clientes están presentes
        List<Cliente> clientes = clienteService.findAll();
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
        clienteService.addCliente(cliente);

        // Verificar si el cliente existe por ID
        boolean result = clienteService.existsById(cliente.getIdPersona());
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
        clienteService.addCliente(cliente);

        // Eliminar el cliente por DNI
        clienteService.deleteByDni(cliente.getDni());

        // Verificar que el cliente ha sido eliminado
        try {
            Cliente clienteEliminado = clienteRepository.findByDNI(cliente.getDni());
            assertNull(clienteEliminado);
        } catch (EmptyResultDataAccessException e) {
            // Exception is expected because the client has been deleted
            assertTrue(true);
        }
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
        clienteService.addCliente(cliente);

        // Verificar si el cliente existe por email
        boolean result = clienteService.existsByEmail("sergio@sergio.es");
        assertTrue(result);

        // Verificar que un email no existente no existe
        boolean resultFalse = clienteService.existsByEmail("noexistent@domain.com");
        assertFalse(resultFalse);
    }
}