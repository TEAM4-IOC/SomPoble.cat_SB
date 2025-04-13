package com.sompoble.cat.service.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.repository.ClienteRepository;
import com.sompoble.cat.service.ClienteService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@Transactional
class ClienteServiceImplTest {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void addClienteTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");

        clienteService.addCliente(cliente);

        ClienteDTO clientePersistido = clienteRepository.findByDNI(cliente.getDni());
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
        clienteService.addCliente(cliente);

        cliente.setNombre("Pedro");
        clienteService.updateCliente(cliente);

        ClienteDTO clienteActualizado = clienteRepository.findByDNI(cliente.getDni());
        assertNotNull(clienteActualizado);
        assertEquals("Pedro", clienteActualizado.getNombre());
    }

    @Test
    void findByDniTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteService.addCliente(cliente);

        ClienteDTO result = clienteService.findByDni("12345678A");
        assertNotNull(result);
        assertEquals(cliente.getDni(), result.getDni());
        
        ClienteDTO resultNoExiste = clienteService.findByDni("99999999Z");
        assertNull(resultNoExiste);
    }
    
    @Test
    void findByDniFullTest() {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");
        clienteService.addCliente(cliente);

        Cliente result = clienteService.findByDniFull("12345678A");
        assertNotNull(result);
        assertEquals(cliente.getDni(), result.getDni());
        
        Cliente resultNoExiste = clienteService.findByDniFull("99999999Z");
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
        clienteService.addCliente(cliente);

        boolean result = clienteService.existsByDni("12345678A");
        assertTrue(result);
        
        boolean resultNoExiste = clienteService.existsByDni("99999999Z");
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
        clienteService.addCliente(cliente);

        clienteService.deleteById(cliente.getIdPersona());

        ClienteDTO clienteEliminado = clienteRepository.findByDNI(cliente.getDni());
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
        clienteService.addCliente(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setDni("87654321B");
        cliente2.setNombre("Maria");
        cliente2.setApellidos("Lopez Garcia");
        cliente2.setEmail("maria@ejemplo.com");
        cliente2.setTelefono("650180801");
        cliente2.setPass("pass");
        clienteService.addCliente(cliente2);

        List<ClienteDTO> clientes = clienteService.findAll();
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
        clienteService.addCliente(cliente);

        boolean result = clienteService.existsById(cliente.getIdPersona());
        assertTrue(result);
        
        boolean resultNoExiste = clienteService.existsById(-1L);
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
        clienteService.addCliente(cliente);

        clienteService.deleteByDni(cliente.getDni());

        ClienteDTO clienteEliminado = clienteRepository.findByDNI(cliente.getDni());
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
        clienteService.addCliente(cliente);

        boolean result = clienteService.existsByEmail("juan@ejemplo.com");
        assertTrue(result);

        boolean resultNoExiste = clienteService.existsByEmail("noexistente@ejemplo.com");
        assertFalse(resultNoExiste);
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
        clienteService.addCliente(cliente);

        ClienteDTO result = clienteService.findByEmail("juan@ejemplo.com");
        assertNotNull(result);
        assertEquals(cliente.getEmail(), result.getEmail());
        
        ClienteDTO resultNoExiste = clienteService.findByEmail("noexistente@ejemplo.com");
        assertNull(resultNoExiste);
    }
}