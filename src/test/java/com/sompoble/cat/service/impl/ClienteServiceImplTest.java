package com.sompoble.cat.service.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.repository.ClienteRepository;
import com.sompoble.cat.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@Transactional
class ClienteServiceImplTest {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

  

    /**
     * Convierte un ClienteDTO a Cliente usando el constructor correcto.
     */
    private Cliente mapToCliente(ClienteDTO dto) {
        return new Cliente(
                dto.getDni(),        
                dto.getNombre(),     
                dto.getApellidos(),  
                dto.getEmail(),      
                dto.getTelefono(),   
                dto.getPass()        
        );
    }

    /**
     * Crea un ClienteDTO con valores de prueba.
     */
    private ClienteDTO buildClienteDTO() {
        return new ClienteDTO(
                null,                
                "12345678A",        
                "Juan",             
                "Perez Garcia",      
                "sergio@sergio.es",  
                "650180800",        
                "pass",             
                Collections.emptyList(), 
                Collections.emptyList()  
        );
    }



    @Test
    void addClienteTest() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteService.addCliente(mapToCliente(clienteDTO));

        // Verifica que el cliente se guardó en la base de datos
        ClienteDTO clientePersistido = clienteRepository.findByDNI(clienteDTO.getDni());
        assertNotNull(clientePersistido);
        assertEquals(clienteDTO.getDni(), clientePersistido.getDni());
    }

    @Test
    void updateClienteTest() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteService.addCliente(mapToCliente(clienteDTO));

        // Actualiza el nombre y llama al servicio
        clienteDTO.setNombre("Pedro");
        clienteService.updateCliente(mapToCliente(clienteDTO));

        // Verifica que el nombre se actualizó en la base de datos
        ClienteDTO clienteActualizado = clienteRepository.findByDNI(clienteDTO.getDni());
        assertNotNull(clienteActualizado);
        assertEquals("Pedro", clienteActualizado.getNombre());
    }

    @Test
    void findByDniTest() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteService.addCliente(mapToCliente(clienteDTO));

        // Busca por DNI y verifica el resultado
        ClienteDTO result = clienteService.findByDni(clienteDTO.getDni());
        assertNotNull(result);
        assertEquals(clienteDTO.getDni(), result.getDni());
    }

    @Test
    void existsByDniTest() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteService.addCliente(mapToCliente(clienteDTO));

        // Verifica que el DNI existe en la base de datos
        assertTrue(clienteService.existsByDni(clienteDTO.getDni()));
    }

    @Test
    void deleteByIdTest() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteService.addCliente(mapToCliente(clienteDTO));

        // Obtiene el ID del cliente y lo elimina
        ClienteDTO cliente = clienteRepository.findByDNI(clienteDTO.getDni());
        clienteService.deleteById(cliente.getIdPersona());

        // Verifica que el cliente fue eliminado
        ClienteDTO clienteEliminado = clienteRepository.findByDNI(clienteDTO.getDni());
        assertNull(clienteEliminado);
    }

    @Test
    void findAllTest() {
        // Crea dos clientes para probar
        ClienteDTO cliente1 = new ClienteDTO(
                null,
                "12345678A",
                "Juan",
                "Perez",
                "juan@example.com",
                "650123456",
                "pass",
                Collections.emptyList(),
                Collections.emptyList()
        );

        ClienteDTO cliente2 = new ClienteDTO(
                null,
                "87654321B",
                "Ana",
                "López",
                "ana@example.com",
                "650987654",
                "pass",
                Collections.emptyList(),
                Collections.emptyList()
        );

        // Guarda ambos clientes
        clienteService.addCliente(mapToCliente(cliente1));
        clienteService.addCliente(mapToCliente(cliente2));

        // Verifica que ambos estén en la lista
        List<ClienteDTO> clientes = clienteService.findAll();
        assertNotNull(clientes);
        assertEquals(2, clientes.size());
    }

    @Test
    void existsByIdTest() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteService.addCliente(mapToCliente(clienteDTO));

        // Obtiene el ID del cliente y verifica su existencia
        ClienteDTO cliente = clienteRepository.findByDNI(clienteDTO.getDni());
        assertTrue(clienteService.existsById(cliente.getIdPersona()));
    }

    @Test
    void deleteByDniTest() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteService.addCliente(mapToCliente(clienteDTO));

        // Elimina por DNI y verifica que ya no existe
        clienteService.deleteByDni(clienteDTO.getDni());
        assertNull(clienteRepository.findByDNI(clienteDTO.getDni()));
    }

    @Test
    void existsByEmailTest() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteService.addCliente(mapToCliente(clienteDTO));

        // Verifica existencia por email
        assertTrue(clienteService.existsByEmail(clienteDTO.getEmail()));
        assertFalse(clienteService.existsByEmail("noexiste@example.com"));
    }
}
