package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.*;
import com.sompoble.cat.dto.EmailDTO;
import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.exception.BadRequestException;
import com.sompoble.cat.service.*;
import com.sompoble.cat.repository.impl.ReservaHibernate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ReservaController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class ReservaControllerTest2 {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private EmpresaService empresaService;

    @MockBean
    private ServicioService servicioService;

    @MockBean
    private ReservaHibernate reservaHibernate;

    @MockBean
    private EmailService emailService;

    @MockBean
    private NotificationService notificationService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateReserva_Success() throws Exception {
        // Mock data
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setEmail("cliente@test.com");
        cliente.setNombre("Test Cliente");

        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("ABC123");
        empresa.setNombre("Empresa Test");

        Servicio servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setNombre("Servicio Test");
        servicio.setEmpresa(empresa);
        servicio.setLimiteReservas(5);

        Horario horario = new Horario();
        horario.setHorarioInicio(LocalTime.of(9, 0));
        horario.setHorarioFin(LocalTime.of(18, 0));
        servicio.setHorarios(List.of(horario));

        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(clienteService.findByDniFull("12345678A")).thenReturn(cliente);
        when(empresaService.existsByIdentificadorFiscal("ABC123")).thenReturn(true);
        when(empresaService.findByIdentificadorFiscalFull("ABC123")).thenReturn(empresa);
        when(servicioService.existePorId(1L)).thenReturn(true);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(reservaService.countReservasByServicioIdAndFecha(1L, "2025-05-10")).thenReturn(0);

        Map<String, Object> requestBody = Map.of(
            "reserva", Map.of(
                "cliente", Map.of("dni", "12345678A"),
                "empresa", Map.of("identificadorFiscal", "ABC123"),
                "servicio", Map.of("idServicio", 1),
                "fechaReserva", "2025-05-10",
                "hora", "10:00",
                "estado", "PENDIENTE"
            )
        );

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("creada correctamente")));

        // Verificar envío de correo
        ArgumentCaptor<EmailDTO> emailCaptor = ArgumentCaptor.forClass(EmailDTO.class);
        verify(emailService, times(1)).sendMail(emailCaptor.capture());

        EmailDTO sentEmail = emailCaptor.getValue();
        System.out.println("Asunto: " + sentEmail.getAsunto());
        System.out.println("Mensaje: " + sentEmail.getMensaje());

        assertEquals("cliente@test.com", sentEmail.getDestinatario());
        assertNotNull(sentEmail.getAsunto());
        assertNotNull(sentEmail.getMensaje());
        assertFalse(sentEmail.getAsunto().isEmpty());
        assertFalse(sentEmail.getMensaje().isEmpty());

        // Verificar registro de notificación
        ArgumentCaptor<Notificacion> notificationCaptor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificationService, times(1)).saveNotification(notificationCaptor.capture());

        Notificacion notificacion = notificationCaptor.getValue();
        assertTrue(notificacion.getMensaje().toLowerCase().contains("reserva"));
        assertEquals("12345678A", notificacion.getCliente().getDni());
    }

    
    @Test
    void testUpdateReserva_Success() throws Exception {
        // Datos originales
        Reserva reservaOriginal = new Reserva();
        reservaOriginal.setIdReserva(1L);
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setEmail("cliente@test.com");
        cliente.setNombre("Test Cliente");
        Empresa empresa = new Empresa();
        empresa.setIdentificadorFiscal("ABC123");
        Servicio servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setEmpresa(empresa);
        servicio.setNombre("Servicio Test");
        servicio.setLimiteReservas(5);
        Horario horario = new Horario();
        horario.setHorarioInicio(LocalTime.of(9, 0));
        horario.setHorarioFin(LocalTime.of(18, 0));
        servicio.setHorarios(List.of(horario));

        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setIdReserva(1L);
        reservaDTO.setDniCliente("12345678A");
        reservaDTO.setIdentificadorFiscalEmpresa("ABC123");
        reservaDTO.setIdServicio(1L);
        reservaDTO.setFechaReserva("2025-05-10");
        reservaDTO.setHora("10:00");

        when(reservaService.findByIdFull(1L)).thenReturn(reservaOriginal);
        when(reservaService.findById(1L)).thenReturn(reservaDTO);
        when(clienteService.existsByDni("12345678A")).thenReturn(true);
        when(clienteService.findByDniFull("12345678A")).thenReturn(cliente);
        when(empresaService.existsByIdentificadorFiscal("ABC123")).thenReturn(true);
        when(servicioService.existePorId(1L)).thenReturn(true);
        when(servicioService.obtenerPorId(1L)).thenReturn(servicio);
        when(reservaService.countReservasByServicioIdAndFecha(1L, "2025-05-10")).thenReturn(1);
        when(reservaHibernate.convertToEntity(any(ReservaDTO.class))).thenReturn(reservaOriginal);

        Map<String, Object> updates = Map.of(
            "fechaReserva", "2025-05-10",
            "hora", "10:00",
            "estado", "CONFIRMADA"
        );

        mockMvc.perform(put("/api/reservas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("actualizada correctamente")));

        // Verificar que se envió un correo
        ArgumentCaptor<EmailDTO> emailCaptor = ArgumentCaptor.forClass(EmailDTO.class);
        verify(emailService, times(1)).sendMail(emailCaptor.capture());

        EmailDTO email = emailCaptor.getValue();
        assertEquals("cliente@test.com", email.getDestinatario());
        assertTrue(email.getAsunto().contains("Actualización"));
        assertTrue(email.getMensaje().contains("2025-05-10"));
        assertTrue(email.getMensaje().contains("10:00"));

        // Verificar que se registró una notificación
        ArgumentCaptor<Notificacion> notificationCaptor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificationService, times(1)).saveNotification(notificationCaptor.capture());

        Notificacion notificacion = notificationCaptor.getValue();
        assertEquals(cliente, notificacion.getCliente());
        assertEquals(Notificacion.TipoNotificacion.INFORMACION, notificacion.getTipo());
        assertTrue(notificacion.getMensaje().contains("actualizado"));
        assertTrue(notificacion.getMensaje().contains("2025-05-10"));
    }
}
