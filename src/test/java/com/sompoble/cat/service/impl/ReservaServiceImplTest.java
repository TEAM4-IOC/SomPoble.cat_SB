package com.sompoble.cat.service.impl;

import com.sompoble.cat.domain.Cliente;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.domain.Reserva;
import com.sompoble.cat.domain.Servicio;
import com.sompoble.cat.dto.ReservaDTO;
import com.sompoble.cat.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Cliente cliente;
    private Empresa empresa;
    private Servicio servicio;
    private Reserva reserva1;
    private Reserva reserva2;
    private ReservaDTO reservaDTO1;
    private ReservaDTO reservaDTO2;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellidos("Perez Garcia");
        cliente.setEmail("juan@ejemplo.com");
        cliente.setTelefono("650180800");
        cliente.setPass("pass");

        Empresario empresario = new Empresario();
        empresario.setDni("87654321B");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180801");
        empresario.setPass("pass");

        empresa = new Empresa();
        empresa.setIdentificadorFiscal("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setActividad("Desarrollo software");
        empresa.setTipo(1);
        empresa.setEmpresario(empresario);

        servicio = new Servicio();
        servicio.setIdServicio(1L);
        servicio.setNombre("Servicio de prueba");
        servicio.setDescripcion("Descripción del servicio");
        servicio.setPrecio(50);
        servicio.setDuracion(60);
        servicio.setEmpresa(empresa);

        reserva1 = new Reserva();
        reserva1.setIdReserva(1L);
        reserva1.setFechaReserva("2023-05-15");
        reserva1.setHora("10:00");
        reserva1.setEstado("PENDIENTE");
        reserva1.setCliente(cliente);
        reserva1.setEmpresa(empresa);
        reserva1.setServicio(servicio);

        reserva2 = new Reserva();
        reserva2.setIdReserva(2L);
        reserva2.setFechaReserva("2023-05-16");
        reserva2.setHora("11:00");
        reserva2.setEstado("PENDIENTE");
        reserva2.setCliente(cliente);
        reserva2.setEmpresa(empresa);
        reserva2.setServicio(servicio);

        reservaDTO1 = new ReservaDTO(
            1L,
            "2023-05-15",
            "10:00",
            "PENDIENTE",
            cliente.getDni(),
            empresa.getIdentificadorFiscal(),
            servicio.getIdServicio()
        );

        reservaDTO2 = new ReservaDTO(
            2L,
            "2023-05-16",
            "11:00",
            "PENDIENTE",
            cliente.getDni(),
            empresa.getIdentificadorFiscal(),
            servicio.getIdServicio()
        );
    }

    @Test
    void findByClienteDniTest() {
        List<ReservaDTO> expectedReservas = Arrays.asList(reservaDTO1, reservaDTO2);
        when(reservaRepository.findByClienteDni("12345678A")).thenReturn(expectedReservas);

        List<ReservaDTO> result = reservaService.findByClienteDni("12345678A");
        
        assertEquals(expectedReservas.size(), result.size());
        verify(reservaRepository).findByClienteDni("12345678A");
    }

    @Test
    void findByEmpresaIdentificadorFiscalTest() {
        List<ReservaDTO> expectedReservas = Arrays.asList(reservaDTO1, reservaDTO2);
        when(reservaRepository.findByEmpresaIdentificadorFiscal("A12345678")).thenReturn(expectedReservas);

        List<ReservaDTO> result = reservaService.findByEmpresaIdentificadorFiscal("A12345678");
        
        assertEquals(expectedReservas.size(), result.size());
        verify(reservaRepository).findByEmpresaIdentificadorFiscal("A12345678");
    }

    @Test
    void findByIdTest() {
        when(reservaRepository.findById(1L)).thenReturn(reservaDTO1);

        ReservaDTO result = reservaService.findById(1L);
        
        assertNotNull(result);
        assertEquals(reservaDTO1.getIdReserva(), result.getIdReserva());
        verify(reservaRepository).findById(1L);

        when(reservaRepository.findById(-1L)).thenReturn(null);
        ReservaDTO resultNoExiste = reservaService.findById(-1L);
        assertNull(resultNoExiste);
    }

    @Test
    void findByIdFullTest() {
        when(reservaRepository.findByIdFull(1L)).thenReturn(reserva1);

        Reserva result = reservaService.findByIdFull(1L);
        
        assertNotNull(result);
        assertEquals(reserva1.getIdReserva(), result.getIdReserva());
        verify(reservaRepository).findByIdFull(1L);

        when(reservaRepository.findByIdFull(-1L)).thenReturn(null);
        Reserva resultNoExiste = reservaService.findByIdFull(-1L);
        assertNull(resultNoExiste);
    }

    @Test
    void addReservaTest() {
        doNothing().when(reservaRepository).addReserva(reserva1);

        reservaService.addReserva(reserva1);
        
        verify(reservaRepository).addReserva(reserva1);
    }

    @Test
    void updateReservaTest() {
        doNothing().when(reservaRepository).updateReserva(reserva1);

        reservaService.updateReserva(reserva1);
        
        verify(reservaRepository).updateReserva(reserva1);
    }

    @Test
    void deleteByIdTest() {
        doNothing().when(reservaRepository).deleteById(1L);

        reservaService.deleteById(1L);
        
        verify(reservaRepository).deleteById(1L);
    }

    @Test
    void deleteByClienteDniTest() {
        doNothing().when(reservaRepository).deleteByClienteDni("12345678A");

        reservaService.deleteByClienteDni("12345678A");
        
        verify(reservaRepository).deleteByClienteDni("12345678A");
    }

    @Test
    void deleteByEmpresaIdentificadorFiscalTest() {
        doNothing().when(reservaRepository).deleteByEmpresaIdentificadorFiscal("A12345678");

        reservaService.deleteByEmpresaIdentificadorFiscal("A12345678");
        
        verify(reservaRepository).deleteByEmpresaIdentificadorFiscal("A12345678");
    }

    @Test
    void countReservasByServicioIdAndFechaTest() {
        when(reservaRepository.countByServicioIdAndFechaReserva(1L, "2023-05-15")).thenReturn(2);

        int count = reservaService.countReservasByServicioIdAndFecha(1L, "2023-05-15");
        
        assertEquals(2, count);
        verify(reservaRepository).countByServicioIdAndFechaReserva(1L, "2023-05-15");
    }
}