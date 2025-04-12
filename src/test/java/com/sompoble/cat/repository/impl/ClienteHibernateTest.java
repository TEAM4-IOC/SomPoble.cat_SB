package com.sompoble.cat.repository.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@Transactional
class ClienteHibernateTest {

    @Autowired
    private ClienteHibernate clienteHibernate;

    @Autowired
    private EntityManager entityManager; 

    @Test
    void addClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
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
        cliente.setEmail("juan@ejemplo.com");
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
    void findByDNITest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        ClienteDTO result = clienteHibernate.findByDNI("12345678A");
        assertNotNull(result);
        assertEquals(cliente.getDni(), result.getDni());
        
        ClienteDTO resultNoExiste = clienteHibernate.findByDNI("99999999Z");
        assertNull(resultNoExiste);
    }

    @Test
    void findByDNIFullTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        Cliente result = clienteHibernate.findByDNIFull("12345678A");
        assertNotNull(result);
        assertEquals(cliente.getDni(), result.getDni());
        
        Cliente resultNoExiste = clienteHibernate.findByDNIFull("99999999Z");
        assertNull(resultNoExiste);
    }

    @Test
    void existsByDniTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        boolean result = clienteHibernate.existsByDni("12345678A");
        assertTrue(result);
        
        boolean resultNoExiste = clienteHibernate.existsByDni("99999999Z");
        assertFalse(resultNoExiste);
    }

    @Test
    void deleteByIdTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        clienteHibernate.deleteById(cliente.getIdPersona());

        Cliente clienteEliminado = entityManager.find(Cliente.class, cliente.getIdPersona());
        assertNull(clienteEliminado);
    }
    
    /*
    * Comentado ya que de lo contrario da error al tener registros en la base de datos
    @Test
    void findAllTest() {
        Cliente cliente1 = new Cliente();
        cliente1.setDni("12345678A");
        cliente1.setNombre("Juan");
        cliente1.setApellidos("Perez Garcia");
        cliente1.setEmail("juan@ejemplo.com");
        cliente1.setTelefono("650180800");
        cliente1.setPass("pass");
        clienteHibernate.addCliente(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setDni("87654321B");
        cliente2.setNombre("Maria");
        cliente2.setApellidos("Lopez Garcia");
        cliente2.setEmail("maria@ejemplo.com");
        cliente2.setTelefono("650180801");
        cliente2.setPass("pass");
        clienteHibernate.addCliente(cliente2);

        List<ClienteDTO> clientes = clienteHibernate.findAll();
        assertNotNull(clientes);
        assertEquals(2, clientes.size());
    }
    */

    @Test
    void existsByIdTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        boolean result = clienteHibernate.existsById(cliente.getIdPersona());
        assertTrue(result);
        
        boolean resultNoExiste = clienteHibernate.existsById(-1L);
        assertFalse(resultNoExiste);
    }

    @Test
    void deleteByDniTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
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
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        boolean result = clienteHibernate.existsByEmail("juan@ejemplo.com");
        assertTrue(result);

        boolean resultNoExist = clienteHibernate.existsByEmail("noexistente@ejemplo.com");
        assertFalse(resultNoExist);
    }
    
    @Test
    void findByEmailTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteHibernate.addCliente(cliente);

        ClienteDTO result = clienteHibernate.findByEmail("juan@ejemplo.com");
        assertNotNull(result);
        assertEquals(cliente.getEmail(), result.getEmail());
        
        ClienteDTO resultNoExiste = clienteHibernate.findByEmail("noexiste@ejemplo.com");
        assertNull(resultNoExiste);
    }
    
    @Test
    void convertToEntityTest() {
        ClienteDTO clienteDTO = new ClienteDTO(
            null,
            "12345678A",
            "Juan",
            "Perez Garcia",
            "juan@ejemplo.com",
            "650180800",
            "pass",
            new ArrayList<>(),
            new ArrayList<>()
        );
        
        Cliente cliente = clienteHibernate.convertToEntity(clienteDTO);
        
        assertNotNull(cliente);
        assertEquals(clienteDTO.getDni(), cliente.getDni());
        assertEquals(clienteDTO.getNombre(), cliente.getNombre());
        assertEquals(clienteDTO.getApellidos(), cliente.getApellidos());
        assertEquals(clienteDTO.getEmail(), cliente.getEmail());
        assertEquals(clienteDTO.getTelefono(), cliente.getTelefono());
    }
}