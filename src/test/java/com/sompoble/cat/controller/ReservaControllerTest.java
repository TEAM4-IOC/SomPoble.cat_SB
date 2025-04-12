package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.dto.EmpresaDTO;
import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.exception.GlobalExceptionHandler;
import com.sompoble.cat.repository.impl.ReservaHibernate;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.EmpresaService;
import com.sompoble.cat.service.ReservaService;
import com.sompoble.cat.service.ServicioService;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaControllerTest {

    @InjectMocks
    private ReservaController reservaController;

    @Mock
    private ReservaService reservaService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private ServicioService servicioService;

    @Mock
    private ReservaHibernate reservaHibernate;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private ReservaDTO reservaDTO1;
    private ReservaDTO reservaDTO2;
    private Reserva reserva1;
    private Reserva reserva2;
    private Cliente cliente;
    private Empresa empresa;
    private Servicio servicio;
    private ClienteDTO clienteDTO;
    private EmpresaDTO empresaDTO;
    private List<Horario> horarios;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        clienteDTO = new ClienteDTO(
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

        empresaDTO = new EmpresaDTO(
                1L, 
                "87654321Z", 
                "B12345678", 
                "Empresa Test", 
                "Servicios Informáticos", 
                "Dirección Test", 
                "empresa@test.com",
                "915557777",
                1,
                new ArrayList<>(), 
                new ArrayList<>()
        );

        reservaDTO1 = new ReservaDTO();
        reservaDTO1.setFechaReserva("2025-04-15");
        reservaDTO1.setHora("10:00");
        reservaDTO1.setEstado("CONFIRMADA");
        reservaDTO1.setDniCliente("12345678A");
        reservaDTO1.setIdentificadorFiscalEmpresa("B12345678");
        reservaDTO1.setIdServicio(1L);

        reservaDTO2 = new ReservaDTO();
        reservaDTO2.setFechaReserva("2025-04-16");
        reservaDTO2.setHora("11:00");
        reservaDTO2.setEstado("PENDIENTE");
        reservaDTO2.setDniCliente("12345678A");
        reservaDTO2.setIdentificadorFiscalEmpresa("B12345678");
        reservaDTO2.setIdServicio(1L);

        cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");

        empresa = new Empresa();
        empresa.setIdentificadorFiscal("B12345678");
        empresa.setNombre("Empresa Test");
        empresa.setEmail("empresa@test.com");
        empresa.setTelefono("915557777");
        empresa.setDireccion("Dirección Test");

        horarios = new ArrayList<>();
        Horario horario = new Horario();
        horario.setHorarioInicio(LocalTime.of(9, 0));
        horario.setHorarioFin(LocalTime.of(18, 0));
        horarios.add(horario);

        servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setNombre("Servicio Test");
        servicio.setDescripcion("Descripción del servicio de prueba");
        servicio.setPrecio(50);
        servicio.setLimiteReservas(10);
        servicio.setHorarios(horarios);

        reserva1 = new Reserva();
        reserva1.setFechaReserva("2025-04-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("CONFIRMADA");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);

        reserva2 = new Reserva();
        reserva2.setFechaReserva("2025-04-16");
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);
    }

    @Test
    public void testGetReservasByClienteNotFound() throws Exception {
        when(clienteService.findByDni("12345678Z")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservas/clientes/12345678Z"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clienteService, times(1)).findByDni("12345678Z");
        verify(reservaService, never()).findByClienteDni(anyString());
    }

    @Test
    public void testGetReservasByClienteNoReservas() throws Exception {
        when(clienteService.findByDni("12345678A")).thenReturn(clienteDTO);
        when(reservaService.findByClienteDni("12345678A")).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservas/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clienteService, times(1)).findByDni("12345678A");
        verify(reservaService, times(1)).findByClienteDni("12345678A");
    }

    @Test
    public void testGetReservasByEmpresa() throws Exception {
        List<ReservaDTO> reservas = List.of(reservaDTO1, reservaDTO2);

        when(empresaService.findByIdentificadorFiscal("B12345678")).thenReturn(empresaDTO);
        when(reservaService.findByEmpresaIdentificadorFiscal("B12345678")).thenReturn(reservas);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservas/empresas/B12345678"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fechaReserva").value("2025-04-15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fechaReserva").value("2025-04-16"));

        verify(empresaService, times(1)).findByIdentificadorFiscal("B12345678");
        verify(reservaService, times(1)).findByEmpresaIdentificadorFiscal("B12345678");
    }

    @Test
    public void testGetReservasByEmpresaNotFound() throws Exception {
        when(empresaService.findByIdentificadorFiscal("Z87654321")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservas/empresas/Z87654321"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empresaService, times(1)).findByIdentificadorFiscal("Z87654321");
        verify(reservaService, never()).findByEmpresaIdentificadorFiscal(anyString());
    }

    @Test
    public void testGetReservaByIdNotFound() throws Exception {
        when(reservaService.findById(99L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/reservas/99"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(reservaService, times(1)).findById(99L);
    }

    @Test
    public void testCreateReserva() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "12345678A");

        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "B12345678");

        Map<String, Object> servicioData = new HashMap<>();
        servicioData.put("idServicio", 1);

        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("fechaReserva", "2025-04-15");
        reservaData.put("hora", "10:00");
        reservaData.put("estado", "CONFIRMADA");
        reservaData.put("cliente", clienteData);
        reservaData.put("empresa", empresaData);
        reservaData.put("servicio", servicioData);

        Map<String, Object> request = new HashMap<>();
        request.put("reserva", reservaData);

        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(clienteService.findByDniFull("12345678A")).thenReturn(cliente);
        when(empresaService.existsByIdentificadorFiscal("B12345678")).thenReturn(true);
        when(empresaService.findByIdentificadorFiscalFull("B12345678")).thenReturn(empresa);
        when(servicioService.existePorId(1L)).thenReturn(true);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(reservaService.countReservasByServicioIdAndFecha(1L, "2025-04-15")).thenReturn(5);
        doNothing().when(reservaService).addReserva(any(Reserva.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(empresaService, times(1)).existsByIdentificadorFiscal("B12345678");
        verify(servicioService, times(1)).existePorId(1L);
        verify(reservaService, times(1)).addReserva(any(Reserva.class));
    }

    @Test
    public void testCreateReservaClienteNoExiste() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "99999999Z");

        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "B12345678");

        Map<String, Object> servicioData = new HashMap<>();
        servicioData.put("idServicio", 1);

        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("fechaReserva", "2025-04-15");
        reservaData.put("hora", "10:00");
        reservaData.put("estado", "CONFIRMADA");
        reservaData.put("cliente", clienteData);
        reservaData.put("empresa", empresaData);
        reservaData.put("servicio", servicioData);

        Map<String, Object> request = new HashMap<>();
        request.put("reserva", reservaData);

        when(clienteService.existsByDni("99999999Z")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No existe un cliente con DNI 99999999Z."));

        verify(clienteService, times(1)).existsByDni("99999999Z");
        verify(reservaService, never()).addReserva(any(Reserva.class));
    }

    @Test
    public void testCreateReservaEmpresaNoExiste() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "12345678A");

        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "Z99999999");

        Map<String, Object> servicioData = new HashMap<>();
        servicioData.put("idServicio", 1);

        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("fechaReserva", "2025-04-15");
        reservaData.put("hora", "10:00");
        reservaData.put("estado", "CONFIRMADA");
        reservaData.put("cliente", clienteData);
        reservaData.put("empresa", empresaData);
        reservaData.put("servicio", servicioData);

        Map<String, Object> request = new HashMap<>();
        request.put("reserva", reservaData);

        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(empresaService.existsByIdentificadorFiscal("Z99999999")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No existe una empresa con identificador fiscal Z99999999"));

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(empresaService, times(1)).existsByIdentificadorFiscal("Z99999999");
        verify(reservaService, never()).addReserva(any(Reserva.class));
    }

    @Test
    public void testCreateReservaServicioNoExiste() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "12345678A");

        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "B12345678");

        Map<String, Object> servicioData = new HashMap<>();
        servicioData.put("idServicio", 99);

        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("fechaReserva", "2025-04-15");
        reservaData.put("hora", "10:00");
        reservaData.put("estado", "CONFIRMADA");
        reservaData.put("cliente", clienteData);
        reservaData.put("empresa", empresaData);
        reservaData.put("servicio", servicioData);

        Map<String, Object> request = new HashMap<>();
        request.put("reserva", reservaData);

        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(empresaService.existsByIdentificadorFiscal("B12345678")).thenReturn(true);
        when(servicioService.existePorId(99L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No existe un servicio con el ID 99"));

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(empresaService, times(1)).existsByIdentificadorFiscal("B12345678");
        verify(servicioService, times(1)).existePorId(99L);
        verify(reservaService, never()).addReserva(any(Reserva.class));
    }

    @Test
    public void testCreateReservaLimiteAlcanzado() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "12345678A");

        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "B12345678");

        Map<String, Object> servicioData = new HashMap<>();
        servicioData.put("idServicio", 1);

        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("fechaReserva", "2025-04-15");
        reservaData.put("hora", "10:00");
        reservaData.put("estado", "CONFIRMADA");
        reservaData.put("cliente", clienteData);
        reservaData.put("empresa", empresaData);
        reservaData.put("servicio", servicioData);

        Map<String, Object> request = new HashMap<>();
        request.put("reserva", reservaData);

        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(clienteService.findByDniFull("12345678A")).thenReturn(cliente);
        when(empresaService.existsByIdentificadorFiscal("B12345678")).thenReturn(true);
        when(empresaService.findByIdentificadorFiscalFull("B12345678")).thenReturn(empresa);
        when(servicioService.existePorId(1L)).thenReturn(true);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(reservaService.countReservasByServicioIdAndFecha(1L, "2025-04-15")).thenReturn(10);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Se ha alcanzado el límite de reservas para este servicio en la fecha indicada"));

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(empresaService, times(1)).existsByIdentificadorFiscal("B12345678");
        verify(servicioService, times(1)).existePorId(1L);
        verify(servicioService, times(1)).obtenerPorId(1L);
        verify(reservaService, times(1)).countReservasByServicioIdAndFecha(1L, "2025-04-15");
        verify(reservaService, never()).addReserva(any(Reserva.class));
    }

    @Test
    public void testUpdateReserva() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("fechaReserva", "2025-04-20");
        updates.put("hora", "15:00");
        updates.put("estado", "CONFIRMADA");

        when(reservaService.findByIdFull(1L)).thenReturn(reserva1);
        when(reservaService.findById(1L)).thenReturn(reservaDTO1);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(reservaService.countReservasByServicioIdAndFecha(1L, "2025-04-20")).thenReturn(5);
        when(reservaHibernate.convertToEntity(any(ReservaDTO.class))).thenReturn(reserva1);
        doNothing().when(reservaService).updateReserva(any(Reserva.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reservas/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Reserva con ID 1 actualizada correctamente"));

        verify(reservaService, times(1)).findByIdFull(1L);
        verify(reservaService, times(1)).findById(1L);
        verify(reservaService, times(1)).updateReserva(any(Reserva.class));
    }

    @Test
    public void testUpdateReservaNotFound() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("fechaReserva", "2025-04-20");
        updates.put("hora", "15:00");

        when(reservaService.findByIdFull(99L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reservas/99")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(reservaService, times(1)).findByIdFull(99L);
        verify(reservaService, never()).updateReserva(any(Reserva.class));
    }

    @Test
    public void testUpdateReservaEmpresaInvalida() throws Exception {
        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "Z99999999");

        Map<String, Object> updates = new HashMap<>();
        updates.put("empresa", empresaData);

        when(reservaService.findByIdFull(1L)).thenReturn(reserva1);
        when(reservaService.findById(1L)).thenReturn(reservaDTO1);
        when(empresaService.existsByIdentificadorFiscal("Z99999999")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reservas/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No existe una empresa con identificador fiscal Z99999999"));

        verify(reservaService, times(1)).findByIdFull(1L);
        verify(empresaService, times(1)).existsByIdentificadorFiscal("Z99999999");
        verify(reservaService, never()).updateReserva(any(Reserva.class));
    }

    @Test
    public void testDeleteReserva() throws Exception {
        when(reservaService.findById(1L)).thenReturn(reservaDTO1);
        doNothing().when(reservaService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservas/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Reserva con ID 1 eliminada correctamente"));

        verify(reservaService, times(1)).findById(1L);
        verify(reservaService, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteReservaNotFound() throws Exception {
        when(reservaService.findById(99L)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservas/99"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(reservaService, times(1)).findById(99L);
        verify(reservaService, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteReservasByCliente() throws Exception {
        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        doNothing().when(reservaService).deleteByClienteDni("12345678A");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservas/clientes/12345678A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Reservas para el cliente con DNI 12345678A eliminadas correctamente"));

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(reservaService, times(1)).deleteByClienteDni("12345678A");
    }

    @Test
    public void testDeleteReservasByClienteNotFound() throws Exception {
        when(clienteService.existsByDni("99999999Z")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservas/clientes/99999999Z"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(clienteService, times(1)).existsByDni("99999999Z");
        verify(reservaService, never()).deleteByClienteDni(anyString());
    }

    @Test
    public void testDeleteReservasByEmpresa() throws Exception {
        when(empresaService.existsByIdentificadorFiscal("B12345678")).thenReturn(true);
        doNothing().when(reservaService).deleteByEmpresaIdentificadorFiscal("B12345678");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservas/empresas/B12345678"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Reservas para la empresa con identificador fiscal B12345678 eliminadas correctamente"));

        verify(empresaService, times(1)).existsByIdentificadorFiscal("B12345678");
        verify(reservaService, times(1)).deleteByEmpresaIdentificadorFiscal("B12345678");
    }

    @Test
    public void testDeleteReservasByEmpresaNotFound() throws Exception {
        when(empresaService.existsByIdentificadorFiscal("Z99999999")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/reservas/empresas/Z99999999"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(empresaService, times(1)).existsByIdentificadorFiscal("Z99999999");
        verify(reservaService, never()).deleteByEmpresaIdentificadorFiscal(anyString());
    }

    @Test
    public void testUpdateReservaLimiteAlcanzado() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("fechaReserva", "2025-04-20");
        updates.put("hora", "15:00");

        when(reservaService.findByIdFull(1L)).thenReturn(reserva1);
        when(reservaService.findById(1L)).thenReturn(reservaDTO1);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(reservaService.countReservasByServicioIdAndFecha(1L, "2025-04-20")).thenReturn(10); 

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reservas/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Se ha alcanzado el límite de reservas para este servicio en la fecha indicada"));

        verify(reservaService, times(1)).findByIdFull(1L);
        verify(reservaService, times(1)).findById(1L);
        verify(servicioService, times(1)).obtenerPorId(1L);
        verify(reservaService, times(1)).countReservasByServicioIdAndFecha(1L, "2025-04-20");
        verify(reservaService, never()).updateReserva(any(Reserva.class));
    }

    @Test
    public void testUpdateReservaHoraInvalida() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("hora", "22:00"); 

        when(reservaService.findByIdFull(1L)).thenReturn(reserva1);
        when(reservaService.findById(1L)).thenReturn(reservaDTO1);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reservas/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("La hora de reserva no está dentro del horario disponible para este servicio"));

        verify(reservaService, times(1)).findByIdFull(1L);
        verify(reservaService, times(1)).findById(1L);
        verify(servicioService, times(1)).obtenerPorId(1L);
        verify(reservaService, never()).updateReserva(any(Reserva.class));
    }

    @Test
    public void testUpdateReservaClienteInvalido() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "99999999Z");

        Map<String, Object> updates = new HashMap<>();
        updates.put("cliente", clienteData);

        when(reservaService.findByIdFull(1L)).thenReturn(reserva1);
        when(reservaService.findById(1L)).thenReturn(reservaDTO1);
        when(clienteService.existsByDni("99999999Z")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reservas/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No existe un cliente con DNI 99999999Z"));

        verify(reservaService, times(1)).findByIdFull(1L);
        verify(clienteService, times(1)).existsByDni("99999999Z");
        verify(reservaService, never()).updateReserva(any(Reserva.class));
    }

    @Test
    public void testCreateReservaHoraInvalida() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "12345678A");

        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "B12345678");

        Map<String, Object> servicioData = new HashMap<>();
        servicioData.put("idServicio", 1);

        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("fechaReserva", "2025-04-15");
        reservaData.put("hora", "23:00"); 
        reservaData.put("estado", "CONFIRMADA");
        reservaData.put("cliente", clienteData);
        reservaData.put("empresa", empresaData);
        reservaData.put("servicio", servicioData);

        Map<String, Object> request = new HashMap<>();
        request.put("reserva", reservaData);

        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(clienteService.findByDniFull("12345678A")).thenReturn(cliente);
        when(empresaService.existsByIdentificadorFiscal("B12345678")).thenReturn(true);
        when(empresaService.findByIdentificadorFiscalFull("B12345678")).thenReturn(empresa);
        when(servicioService.existePorId(1L)).thenReturn(true);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(reservaService.countReservasByServicioIdAndFecha(1L, "2025-04-15")).thenReturn(5);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("La hora de reserva no está dentro del horario disponible para este servicio"));

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(empresaService, times(1)).existsByIdentificadorFiscal("B12345678");
        verify(servicioService, times(1)).existePorId(1L);
        verify(servicioService, times(1)).obtenerPorId(1L);
        verify(reservaService, times(1)).countReservasByServicioIdAndFecha(1L, "2025-04-15");
        verify(reservaService, never()).addReserva(any(Reserva.class));
    }

    @Test
    public void testCreateReservaCamposObligatoriosFaltantes() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "12345678A");

        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "B12345678");

        Map<String, Object> servicioData = new HashMap<>();
        servicioData.put("idServicio", 1);

        Map<String, Object> reservaData = new HashMap<>();

        reservaData.put("cliente", clienteData);
        reservaData.put("empresa", empresaData);
        reservaData.put("servicio", servicioData);

        Map<String, Object> request = new HashMap<>();
        request.put("reserva", reservaData);

        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(empresaService.existsByIdentificadorFiscal("B12345678")).thenReturn(true);
        when(servicioService.existePorId(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Fecha, hora y estado son obligatorios"));

        verify(reservaService, never()).addReserva(any(Reserva.class));
    }

    @Test
    public void testCreateReservaServicioSinHorarios() throws Exception {
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("dni", "12345678A");

        Map<String, Object> empresaData = new HashMap<>();
        empresaData.put("identificadorFiscal", "B12345678");

        Map<String, Object> servicioData = new HashMap<>();
        servicioData.put("idServicio", 1);

        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("fechaReserva", "2025-04-15");
        reservaData.put("hora", "10:00");
        reservaData.put("estado", "CONFIRMADA");
        reservaData.put("cliente", clienteData);
        reservaData.put("empresa", empresaData);
        reservaData.put("servicio", servicioData);

        Map<String, Object> request = new HashMap<>();
        request.put("reserva", reservaData);

        Servicio servicioSinHorarios = new Servicio();
        servicioSinHorarios.setIdServicio(1L);
        servicioSinHorarios.setNombre("Servicio Test");
        servicioSinHorarios.setLimiteReservas(10);
        servicioSinHorarios.setHorarios(new ArrayList<>()); 

        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(clienteService.findByDniFull("12345678A")).thenReturn(cliente);
        when(empresaService.existsByIdentificadorFiscal("B12345678")).thenReturn(true);
        when(empresaService.findByIdentificadorFiscalFull("B12345678")).thenReturn(empresa);
        when(servicioService.existePorId(1L)).thenReturn(true);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicioSinHorarios);
        when(reservaService.countReservasByServicioIdAndFecha(1L, "2025-04-15")).thenReturn(5);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/reservas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("El servicio no tiene horarios definidos"));

        verify(clienteService, times(1)).existsByDni("12345678A");
        verify(empresaService, times(1)).existsByIdentificadorFiscal("B12345678");
        verify(servicioService, times(1)).existePorId(1L);
        verify(servicioService, times(1)).obtenerPorId(1L);
        verify(reservaService, never()).addReserva(any(Reserva.class));
    }
}
