package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.exception.GlobalExceptionHandler;
import com.sompoble.cat.service.ClienteService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper;
    
    private ClienteDTO clienteDTO1;
    private ClienteDTO clienteDTO2;
    private Cliente cliente1;
    private Cliente cliente2;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
        objectMapper = new ObjectMapper();
        
        clienteDTO1 = new ClienteDTO(
            1L,
            "12345678A",
            "Juan",
            "Perez Garcia",
            "juan@ejemplo.com",
            "650180800",
            "pass",
            new ArrayList<>(),
            new ArrayList<>()
        );
        
        clienteDTO2 = new ClienteDTO(
            2L,
            "87654321B",
            "Maria",
            "Lopez",
            "maria@ejemplo.com",
            "936772258",
            "pass",
            new ArrayList<>(),
            new ArrayList<>()
        );
        
        cliente1 = new Cliente();
        cliente1.setDni("12345678A");
        cliente1.setNombre("Juan");
        cliente1.setApellidos("Perez Garcia");
        cliente1.setEmail("juan@ejemplo.com");
        cliente1.setTelefono("650180800");
        cliente1.setPass("pass");

        cliente2 = new Cliente();
        cliente2.setDni("87654321B");
        cliente2.setNombre("Maria");
        cliente2.setApellidos("Lopez");
        cliente2.setEmail("maria@ejemplo.com");
        cliente2.setTelefono("936772258");
        cliente2.setPass("pass");
    }

    @Test
    public void testGetAllClientes() throws Exception {
        List<ClienteDTO> clientes = List.of(clienteDTO1, clienteDTO2);
        
        when(clienteService.findAll()).thenReturn(clientes);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dni").value("12345678A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dni").value("87654321B"));

        verify(clienteService, times(1)).findAll();
    }

    @Test
    public void testGetClienteByDni() throws Exception {
        when(clienteService.findByDni("12345678A")).thenReturn(clienteDTO1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dni").value("12345678A"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan"));

        verify(clienteService, times(1)).findByDni("12345678A");
    }

    @Test
    public void testGetClienteByDniNotFound() throws Exception {
        when(clienteService.findByDni("12345678A")).thenThrow(new RuntimeException("Cliente no encontrado"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clienteService, times(1)).findByDni("12345678A");
    }

    @Test
    public void testCreateCliente() throws Exception {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDni("12345678A");
        nuevoCliente.setNombre("Juan");
        nuevoCliente.setApellidos("Perez");
        nuevoCliente.setEmail("juan@ejemplo.com");
        nuevoCliente.setTelefono("650180800");
        nuevoCliente.setPass("pass");

        when(clienteService.existsByDni(nuevoCliente.getDni())).thenReturn(false);
        when(clienteService.existsByEmail(nuevoCliente.getEmail())).thenReturn(false);
        doNothing().when(clienteService).addCliente(any(Cliente.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoCliente)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(clienteService, times(1)).addCliente(any(Cliente.class));
    }
    
    @Test
    public void testCreateClienteDniExistente() throws Exception {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDni("12345678A");
        nuevoCliente.setNombre("Juan");
        nuevoCliente.setApellidos("Perez");
        nuevoCliente.setEmail("juan@ejemplo.com");
        nuevoCliente.setTelefono("650180800");
        nuevoCliente.setPass("pass");

        when(clienteService.existsByDni("12345678A")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoCliente)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cliente con DNI 12345678A ya existe")); 

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(clienteService, never()).addCliente(any(Cliente.class));
    }
    
    @Test
    public void testCreateClienteEmailExistente() throws Exception {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDni("12345678A");
        nuevoCliente.setNombre("Juan");
        nuevoCliente.setApellidos("Perez");
        nuevoCliente.setEmail("juan@ejemplo.com");
        nuevoCliente.setTelefono("650180800");
        nuevoCliente.setPass("pass");

        when(clienteService.existsByDni("12345678A")).thenReturn(false);
        when(clienteService.existsByEmail("juan@ejemplo.com")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(nuevoCliente)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email juan@ejemplo.com ya existe")); 

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(clienteService, times(1)).existsByEmail("juan@ejemplo.com");
        verify(clienteService, never()).addCliente(any(Cliente.class));
    }

    @Test
    public void testUpdateCliente() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", "Juan Updated");
        updates.put("apellidos", "Perez Updated");
        updates.put("email", "juan.updated@ejemplo.com");
        updates.put("telefono", "916775589");
        updates.put("pass", "pass1");

        when(clienteService.findByDniFull("12345678A")).thenReturn(cliente1);
        doNothing().when(clienteService).updateCliente(any(Cliente.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clientes/12345678A")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Cliente con DNI 12345678A actualizado correctamente"));

        verify(clienteService, times(1)).updateCliente(any(Cliente.class));
    }

    @Test
    public void testUpdateClienteNotFound() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nombre", "Juan Updated");
        updates.put("apellidos", "Perez Updated");

        when(clienteService.findByDniFull("12345678A")).thenThrow(new RuntimeException("Cliente no encontrado"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/clientes/12345678A")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clienteService, times(1)).findByDniFull("12345678A");
        verify(clienteService, never()).updateCliente(any(Cliente.class));
    }

    @Test
    public void testDeleteCliente() throws Exception {
        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        doNothing().when(clienteService).deleteByDni("12345678A");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Cliente con DNI 12345678A eliminado correctamente"));

        verify(clienteService, times(1)).deleteByDni("12345678A");
    }

    @Test
    public void testDeleteClienteNotFound() throws Exception {
        when(clienteService.existsByDni("12345678A")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(clienteService, never()).deleteByDni(anyString());
    }
}