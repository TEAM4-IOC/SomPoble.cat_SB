package com.sompoble.cat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sompoble.cat.domain.Notificacion;
import com.sompoble.cat.dto.ClienteDTO;
import com.sompoble.cat.service.ClienteService;
import com.sompoble.cat.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void testObtenerNotificacionesByClientes() throws Exception {
        String dni = "12345678A";
        ClienteDTO cliente = Mockito.mock(ClienteDTO.class);
        Notificacion notificacion = Mockito.mock(Notificacion.class);
        Mockito.when(notificacion.getIdNotificacion()).thenReturn(1L);

        Mockito.when(clienteService.findByDni(dni)).thenReturn(cliente);
        Mockito.when(notificationService.findByClienteDni(dni)).thenReturn(Arrays.asList(notificacion));

        mockMvc.perform(get("/api/notifications/clientes/{dni}", dni))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].idNotificacion").value(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void testObtenerNotificacionesPorIdentificador() throws Exception {
        String identificador = "12345678A";
        Notificacion notificacion = Mockito.mock(Notificacion.class);
        Mockito.when(notificacion.getIdNotificacion()).thenReturn(2L);

        Mockito.when(notificationService.findNotificationsByIdentificador(identificador))
                .thenReturn(Arrays.asList(notificacion));

        mockMvc.perform(get("/api/notifications/obtener")
                .param("identificador", identificador))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNotificacion").value(2L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void testSaveNotification() throws Exception {
        Notificacion notification = new Notificacion();

        Mockito.doNothing().when(notificationService).saveNotification(any(Notificacion.class));

        mockMvc.perform(post("/api/notifications/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificación guardada correctamente."));
    }

    @Test
    @WithMockUser
    public void testGetAllNotifications() throws Exception {
        Notificacion notificacion = Mockito.mock(Notificacion.class);
        Mockito.when(notificacion.getIdNotificacion()).thenReturn(4L);

        Mockito.when(notificationService.getAllNotifications())
                .thenReturn(Collections.singletonList(notificacion));

        mockMvc.perform(get("/api/notifications/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idNotificacion").value(4L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void testGetNotificationById() throws Exception {
        Long id = 5L;
        Notificacion notificacion = Mockito.mock(Notificacion.class);
        Mockito.when(notificacion.getIdNotificacion()).thenReturn(id);

        Mockito.when(notificationService.findNotificationById(id))
               .thenReturn(notificacion);

        mockMvc.perform(get("/api/notifications/{id}", id))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.idNotificacion").value(5L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void testDeleteNotification() throws Exception {
        Long id = 6L;
        Mockito.doNothing().when(notificationService).deleteNotificationById(id);

        mockMvc.perform(delete("/api/notifications/delete/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Notificación eliminada correctamente."));
    }
}
