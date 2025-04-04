package com.sompoble.cat.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DomainTest {

    /*@Test
    public void testConstructorGetAndRelations() {
        String dniEmpresario = "12345678A";
        String nombreEmpresario = "Juan";
        String apellidoEmpresario = "Perez";
        String email = "juan@empresa.com";
        String telefono = "650180800";
        String pass = "pass";
        Empresario empresario = new Empresario(dniEmpresario, nombreEmpresario, apellidoEmpresario, email, telefono, pass);

        String identificadorFiscal = "B53567814";
        String nombre = "Empresa XYZ";
        String actividad = null;
        String direccion = "Calle Ficticia 123";
        String telefonoEmpresa = "123456789";
        String emailEmp = "juan@xyz.com";
        int tipo = 1;
        Empresa empresa = new Empresa(empresario, identificadorFiscal, nombre, actividad, direccion, emailEmp, telefonoEmpresa, tipo);

        Cliente cliente = new Cliente("12345678A", "Juan", "Perez", "juan.perez@example.com", "654321987", "password123");
        Notificacion notificacion = new Notificacion(cliente, empresario, "Mensaje de prueba", "Informativa");
        cliente.setNotificaciones(List.of(notificacion));

        Notificacion notificacion2 = new Notificacion(cliente, empresario, "Mensaje de prueba", "Informativa");
        empresario.setNotificaciones(List.of(notificacion2));

        Servicio servicio = new Servicio("Servicio A", "Descripción del servicio A", "60", "150.0", 10, empresa);
        Reserva reserva = new Reserva(empresa, cliente, servicio, "2025-03-15", "10:30", "Confirmada");

        Empresario result = empresa.getEmpresario();
        assertEquals(empresario, result, "El empresario no coincide con el valor esperado");
        assertEquals(nombreEmpresario, result.getNombre(), "El nombre del empresario no es correcto");
        assertEquals(apellidoEmpresario, result.getApellidos(), "El apellido del empresario no es correcto");
        assertEquals(email, result.getEmail(), "El email del empresario no es correcto");
        assertEquals(telefono, result.getTelefono(), "El teléfono del empresario no es correcto");

        assertEquals(identificadorFiscal, empresa.getIdentificadorFiscal(), "El identificador fiscal de la empresa no coincide");
        assertEquals(nombre, empresa.getNombre(), "El nombre de la empresa no coincide");
        assertEquals(direccion, empresa.getDireccion(), "La dirección de la empresa no coincide");
        assertEquals(telefonoEmpresa, empresa.getTelefono(), "El teléfono de la empresa no coincide");
        assertEquals(emailEmp, empresa.getEmail(), "El email de la empresa no coincide");

        empresa.setTelefono("987654321");
        empresa.setEmail("empresa@xyz.com");
        assertEquals("987654321", empresa.getTelefono(), "El teléfono de la empresa no se ha actualizado correctamente");
        assertEquals("empresa@xyz.com", empresa.getEmail(), "El email de la empresa no se ha actualizado correctamente");

        assertNotNull(cliente.getNotificaciones(), "El cliente no tiene notificaciones");
        assertEquals(1, cliente.getNotificaciones().size(), "El cliente debería tener una notificación");
        assertEquals("Mensaje de prueba", cliente.getNotificaciones().get(0).getMensaje(), "El mensaje de la notificación no es correcto");
        assertNotNull(empresario.getNotificaciones(), "El empresario no tiene notificaciones");
        assertEquals(1, empresario.getNotificaciones().size(), "El empresario debería tener una notificación");
        assertEquals("Mensaje de prueba", empresario.getNotificaciones().get(0).getMensaje(), "El mensaje de la notificación no es correcto");

        List<Notificacion> nuevasNotificaciones = List.of(new Notificacion(cliente, empresario, "Otro mensaje", "Urgente"));
        cliente.setNotificaciones(nuevasNotificaciones);
        assertEquals(1, cliente.getNotificaciones().size(), "El cliente debería tener una notificación");
        assertEquals("Otro mensaje", cliente.getNotificaciones().get(0).getMensaje(), "El mensaje de la nueva notificación no es correcto");

        assertEquals("Servicio A", reserva.getServicio().getNombre(), "El nombre del servicio no es correcto");
        assertEquals("Juan", reserva.getCliente().getNombre(), "El nombre del cliente asociado con la reserva no es correcto");
        assertEquals("Empresa XYZ", reserva.getEmpresa().getNombre(), "El nombre de la empresa asociada con la reserva no es correcto");
        // Verificar y actualizar el tipo de empresa
        empresa.setTipo(2);
        assertEquals(2, empresa.getTipo(), "El tipo de empresa no se ha actualizado correctamente");

        List<Reserva> nuevasReservas = List.of(reserva);
        empresa.setReservas(nuevasReservas);
        assertEquals(1, empresa.getReservas().size(), "La empresa debería tener una reserva asociada");
        assertEquals(reserva, empresa.getReservas().get(0), "La reserva asociada a la empresa no es la correcta");

        List<Servicio> nuevosServicios = List.of(servicio);
        empresa.setServicios(nuevosServicios);
        assertEquals(1, empresa.getServicios().size(), "La empresa debería tener un servicio asociado");
        assertEquals(servicio, empresa.getServicios().get(0), "El servicio asociado a la empresa no es el correcto");

        List<Horario> horarios = new ArrayList<>();
        horarios.add(new Horario("Lunes-Viernes", LocalTime.of(8, 0), LocalTime.of(12, 0), empresa));
        horarios.add(new Horario("Lunes-Viernes", LocalTime.of(14, 0), LocalTime.of(18, 0), empresa));
        empresa.setHorarios(horarios);

        assertEquals(2, empresa.getHorarios().size(), "La empresa debería tener dos horarios configurados");
        assertEquals("08:00", empresa.getHorarios().get(0).getHorarioInicio().toString(), "El horario de inicio del primer horario no es correcto");
        assertEquals("12:00", empresa.getHorarios().get(0).getHorarioFin().toString(), "El horario de fin del primer horario no es correcto");
        assertEquals("14:00", empresa.getHorarios().get(1).getHorarioInicio().toString(), "El horario de inicio del segundo horario no es correcto");
        assertEquals("18:00", empresa.getHorarios().get(1).getHorarioFin().toString(), "El horario de fin del segundo horario no es correcto");
    }*/
}
