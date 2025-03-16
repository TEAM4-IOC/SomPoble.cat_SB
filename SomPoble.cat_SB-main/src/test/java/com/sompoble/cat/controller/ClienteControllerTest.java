package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.service.ClienteService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    /*@InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllClientes() throws Exception {
        // Aquí se pueden simular datos que devolverá el servicio
        Cliente cliente1 = new Cliente();
        cliente1.setDni("12345678A");
        cliente1.setNombre("Juan");
        cliente1.setApellidos("Perez Garcia");
        cliente1.setEmail("sergio@sergio.es");
        cliente1.setTelefono("650180800");
        cliente1.setContraseña("pass");

        Cliente cliente2 = new Cliente();
        cliente2.setDni("87654321B");
        cliente2.setNombre("Maria");
        cliente2.setApellidos("Lopez");
        cliente2.setEmail("maria@maria.es");
        cliente2.setTelefono("936772258");
        cliente2.setContraseña("pass");

        when(clienteService.findAll()).thenReturn(List.of(cliente1, cliente2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dni").value("12345678A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dni").value("87654321B"));

        verify(clienteService, times(1)).findAll();
    }

    @Test
    public void testGetClienteByDni() throws Exception {
        // Crear un cliente de prueba
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");

        when(clienteService.findByDni("12345678A")).thenReturn(cliente);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dni").value("12345678A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan"));

        verify(clienteService, times(1)).findByDni("12345678A");
    }

    @Test
    public void testGetClienteByDniNotFound() throws Exception {
        when(clienteService.findByDni("12345678A")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clienteService, times(1)).findByDni("12345678A");
    }

    @Test
    public void testCreateCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");

        when(clienteService.existsByDni(cliente.getDni())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(clienteService, times(1)).addCliente(cliente);
    }

    @Test
    public void testCreateClienteBadRequest() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");

        when(clienteService.existsByDni("12345678A")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());  // 400 Bad Request

        verify(clienteService, times(1)).existsByDni("12345678A");
    }

    @Test
    public void testUpdateCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez");
        cliente.setEmail("sergio@sergio.es");
        cliente.setTelefono("650180800");
        cliente.setContraseña("pass");

        Cliente updatedCliente = new Cliente();
        updatedCliente.setNombre("Juan Updated");
        updatedCliente.setApellidos("Perez Updated");
        updatedCliente.setEmail("juan@sergio.es");
        updatedCliente.setTelefono("916775589");
        updatedCliente.setContraseña("pass1");

        when(clienteService.findByDni("12345678A")).thenReturn(cliente);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clientes/12345678A")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedCliente)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Ajusta los jsonPath para que apunten a la estructura correcta
                .andExpect(MockMvcResultMatchers.jsonPath("$updatedCliente.nombre").value("Juan Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$updatedCliente.apellidos").value("Perez Updated"));

        verify(clienteService, times(1)).updateCliente(cliente);
    }

    @Test
    public void testUpdateClienteNotFound() throws Exception {
        Cliente updatedCliente = new Cliente();
        updatedCliente.setNombre("Juan Updated");
        updatedCliente.setApellidos("Perez Updated");
        updatedCliente.setEmail("sergio@sergio.es");
        updatedCliente.setTelefono("650180800");
        updatedCliente.setContraseña("pass");

        when(clienteService.findByDni("12345678A")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clientes/12345678A")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updatedCliente)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clienteService, times(1)).findByDni("12345678A");
    }

    @Test
    public void testDeleteCliente() throws Exception {
        when(clienteService.existsByDni("12345678A")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk());  // Cambiar isNoContent() a isOk()

        verify(clienteService, times(1)).deleteByDni("12345678A");
    }

    @Test
    public void testDeleteClienteNotFound() throws Exception {
        when(clienteService.existsByDni("12345678A")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clienteService, times(1)).existsByDni("12345678A");
    }*/
}
