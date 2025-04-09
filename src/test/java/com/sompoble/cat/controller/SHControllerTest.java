package com.sompoble.cat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Horario;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ServicioHorarioDTO;
import com.sompoble.cat.repository.EmpresaRepository;
import com.sompoble.cat.repository.HorarioRepository;
import com.sompoble.cat.repository.ServicioRepository;

@WebMvcTest(SHController.class)
public class SHControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicioRepository servicioRepository;

    @MockBean
    private HorarioRepository horarioRepository;

    @MockBean
    private EmpresaRepository empresaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Método auxiliar para crear un DTO de prueba
    private ServicioHorarioDTO crearDtoPrueba() {
        ServicioHorarioDTO dto = new ServicioHorarioDTO();
        dto.setNombre("Test Servicio");
        dto.setDescripcion("Descripción");
        dto.setDuracion(60);
        dto.setPrecio(20.0f);
        dto.setLimiteReservas(5);
        dto.setIdentificadorFiscal("ABC123");
        dto.setDiasLaborables("Lunes,Martes");
        dto.setHorarioInicio(LocalTime.of(10, 0));
        dto.setHorarioFin(LocalTime.of(18, 0));
        return dto;
    }

    // Método auxiliar para crear entidades de prueba
    private Empresa crearEmpresaPrueba() {
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(1L);
        empresa.setIdentificadorFiscal("ABC123");
        return empresa;
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCrearServicioConHorario() throws Exception {
        ServicioHorarioDTO dto = crearDtoPrueba();
        Empresa empresa = crearEmpresaPrueba();

        Servicio servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setEmpresa(empresa);

        Horario horario = new Horario();
        horario.setIdHorario(1L);
        horario.setServicio(servicio);

        Mockito.when(empresaRepository.findByIdentificadorFiscalFull(anyString())).thenReturn(empresa);
        Mockito.doAnswer(invocation -> {
            Servicio arg = invocation.getArgument(0);
            arg.setIdServicio(1L);
            return null;
        }).when(servicioRepository).addServicio(any(Servicio.class));
        Mockito.when(horarioRepository.save(any(Horario.class))).thenReturn(horario);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/servicio-horario/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Test Servicio"))
                .andExpect(jsonPath("$.descripcion").value("Descripción"));
    }

    @Test
    @WithMockUser
    void testObtenerServicioConHorario() throws Exception {
        Empresa empresa = crearEmpresaPrueba();

        Servicio servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setEmpresa(empresa);

        Horario horario = new Horario();
        horario.setIdHorario(1L);
        horario.setServicio(servicio);

        List<Horario> horarios = Arrays.asList(horario);

        Mockito.when(empresaRepository.findByIdentificadorFiscalFull(anyString())).thenReturn(empresa);
        Mockito.when(horarioRepository.findByServicio_Empresa_IdentificadorFiscal(anyString())).thenReturn(horarios);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/servicio-horario/obtener")
                        .param("identificadorFiscal", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idServicio").value(1))
                .andExpect(jsonPath("$[0].idHorario").value(1));
    }



    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAnularServicioConHorario() throws Exception {
        Empresa empresa = crearEmpresaPrueba();

        Servicio servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setEmpresa(empresa);

        Horario horario = new Horario();
        horario.setIdHorario(1L);
        horario.setServicio(servicio);

        Mockito.when(empresaRepository.findByIdentificadorFiscalFull(anyString())).thenReturn(empresa);
        Mockito.when(servicioRepository.findByIdAndEmpresaId(anyLong(), anyLong())).thenReturn(Optional.of(servicio));
        Mockito.when(horarioRepository.findByServicio_IdServicio(anyLong())).thenReturn(Optional.of(horario));
        Mockito.doNothing().when(horarioRepository).delete(anyLong());
        Mockito.doNothing().when(servicioRepository).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/servicio-horario/anular/1")
                        .with(csrf())
                        .param("identificadorFiscal", "ABC123"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testObtenerTodosServiciosConHorario() throws Exception {
        Empresa empresa = crearEmpresaPrueba();

        Servicio servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setEmpresa(empresa);

        Horario horario = new Horario();
        horario.setIdHorario(1L);
        horario.setServicio(servicio);

        List<Horario> horarios = Arrays.asList(horario);

        Mockito.when(horarioRepository.findAll()).thenReturn(horarios);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/servicio-horario/obtener-todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idServicio").value(1))
                .andExpect(jsonPath("$[0].idHorario").value(1));
    }

    @Test
    @WithMockUser
    void testObtenerServicioConHorario_EmpresaNoEncontrada() throws Exception {
        Mockito.when(empresaRepository.findByIdentificadorFiscalFull(anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/servicio-horario/obtener")
                        .param("identificadorFiscal", "NO_EXISTE"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testActualizarServicioConHorario_ServicioNoEncontrado() throws Exception {
        ServicioHorarioDTO dto = crearDtoPrueba();
        Empresa empresa = crearEmpresaPrueba();

        Mockito.when(empresaRepository.findByIdentificadorFiscalFull(anyString())).thenReturn(empresa);
        Mockito.when(servicioRepository.findAllByEmpresaIdentificador(anyString())).thenReturn(Arrays.asList());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/servicio-horario/actualizar")
                        .with(csrf())
                        .param("identificadorFiscal", "ABC123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is5xxServerError());
    }
}