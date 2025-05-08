package com.sompoble.cat.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para validar las relaciones y funcionalidades de las
 * entidades del dominio. Verifica la correcta creación de objetos, sus
 * relaciones y la funcionalidad de los métodos getter y setter.
 */
public class DomainTest {

    /**
     * Prueba los constructores, getters y las relaciones entre las entidades
     * del dominio.
     *
     * Este test verifica: - La creación correcta de entidades (Empresario,
     * Empresa, Cliente, Servicio, Reserva, Horario) - El funcionamiento de
     * getters y setters - Las relaciones entre entidades - La consistencia de
     * los datos al modificar las entidades
     */
    @Test
    public void testConstructorGetAndRelations() {
        // Creación del empresario
        String dniEmpresario = "12345678A";
        String nombreEmpresario = "Juan";
        String apellidoEmpresario = "Perez";
        String email = "juan@empresa.com";
        String telefono = "650180800";
        String pass = "pass";
        Empresario empresario = new Empresario(dniEmpresario, nombreEmpresario, apellidoEmpresario, email, telefono, pass);

        // Creación de la empresa
        String identificadorFiscal = "B53567814";
        String nombre = "Empresa XYZ";
        String actividad = null;
        String direccion = "Calle Ficticia 123";
        String telefonoEmpresa = "123456789";
        String emailEmp = "juan@xyz.com";
        int tipo = 1;
        Empresa empresa = new Empresa(empresario, identificadorFiscal, nombre, actividad, direccion, emailEmp, telefonoEmpresa, tipo);

        // Creación del cliente
        Cliente cliente = new Cliente("12345678A", "Juan", "Perez", "juan.perez@example.com", "654321987", "password123");

        // AMPLIACIÓN DE PRUEBAS PARA NOTIFICACIONES
        
        // -- Prueba 1: Creación de notificación para ambos destinatarios --
        Notificacion notificacion = new Notificacion(cliente, empresario, "Mensaje de prueba", Notificacion.TipoNotificacion.INFORMACION);
        
        // Verificación de propiedades básicas de la notificación
        assertEquals(cliente, notificacion.getCliente(), "El cliente de la notificación no coincide");
        assertEquals(empresario, notificacion.getEmpresario(), "El empresario de la notificación no coincide");
        assertEquals("Mensaje de prueba", notificacion.getMensaje(), "El mensaje de la notificación no es correcto");
        assertEquals(Notificacion.TipoNotificacion.INFORMACION, notificacion.getTipo(), "El tipo de notificación no es correcto");
        assertNull(notificacion.getIdNotificacion(), "El ID no debería estar asignado aún");
        
        // Verificación de métodos utilitarios
        assertTrue(notificacion.esParaCliente(), "La notificación debería ser para cliente");
        assertTrue(notificacion.esParaEmpresario(), "La notificación debería ser para empresario");
        
        // Validación del destinatario
        assertDoesNotThrow(() -> notificacion.validarDestinatario(), "No debería lanzar excepción con ambos destinatarios");
        
        // -- Prueba 2: Notificación solo para cliente --
        Notificacion notificacionSoloCliente = new Notificacion(cliente, null, "Mensaje solo para cliente", Notificacion.TipoNotificacion.ADVERTENCIA);
        
        // Verificación de propiedades
        assertEquals(cliente, notificacionSoloCliente.getCliente(), "El cliente de la notificación no coincide");
        assertNull(notificacionSoloCliente.getEmpresario(), "El empresario debería ser null");
        assertEquals("Mensaje solo para cliente", notificacionSoloCliente.getMensaje(), "El mensaje no es correcto");
        assertEquals(Notificacion.TipoNotificacion.ADVERTENCIA, notificacionSoloCliente.getTipo(), "El tipo no es correcto");
        
        // Verificación de métodos utilitarios
        assertTrue(notificacionSoloCliente.esParaCliente(), "La notificación debería ser para cliente");
        assertFalse(notificacionSoloCliente.esParaEmpresario(), "La notificación no debería ser para empresario");
        
        // -- Prueba 3: Notificación solo para empresario --
        Notificacion notificacionSoloEmpresario = new Notificacion(null, empresario, "Mensaje solo para empresario", Notificacion.TipoNotificacion.ERROR);
        
        // Verificación de propiedades
        assertNull(notificacionSoloEmpresario.getCliente(), "El cliente debería ser null");
        assertEquals(empresario, notificacionSoloEmpresario.getEmpresario(), "El empresario no coincide");
        assertEquals("Mensaje solo para empresario", notificacionSoloEmpresario.getMensaje(), "El mensaje no es correcto");
        assertEquals(Notificacion.TipoNotificacion.ERROR, notificacionSoloEmpresario.getTipo(), "El tipo no es correcto");
        
        // Verificación de métodos utilitarios
        assertFalse(notificacionSoloEmpresario.esParaCliente(), "La notificación no debería ser para cliente");
        assertTrue(notificacionSoloEmpresario.esParaEmpresario(), "La notificación debería ser para empresario");
        
        // -- Prueba 4: Modificación de una notificación existente --
        notificacion.setMensaje("Mensaje actualizado");
        notificacion.setTipo(Notificacion.TipoNotificacion.ERROR);
        
        // Verificación de cambios
        assertEquals("Mensaje actualizado", notificacion.getMensaje(), "El mensaje no se actualizó correctamente");
        assertEquals(Notificacion.TipoNotificacion.ERROR, notificacion.getTipo(), "El tipo no se actualizó correctamente");
        
        // -- Prueba 5: Asignación de notificaciones a cliente y empresario --
        List<Notificacion> notificacionesCliente = new ArrayList<>();
        notificacionesCliente.add(notificacion);
        notificacionesCliente.add(notificacionSoloCliente);
        cliente.setNotificaciones(notificacionesCliente);
        
        List<Notificacion> notificacionesEmpresario = new ArrayList<>();
        notificacionesEmpresario.add(notificacion);
        notificacionesEmpresario.add(notificacionSoloEmpresario);
        empresario.setNotificaciones(notificacionesEmpresario);
        
        // Verificación de las listas de notificaciones
        assertEquals(2, cliente.getNotificaciones().size(), "El cliente debería tener dos notificaciones");
        assertEquals(2, empresario.getNotificaciones().size(), "El empresario debería tener dos notificaciones");
        
        // Verificar contenido específico de las notificaciones
        assertEquals("Mensaje actualizado", cliente.getNotificaciones().get(0).getMensaje(), 
                    "El mensaje de la primera notificación del cliente no es correcto");
        assertEquals(Notificacion.TipoNotificacion.ERROR, cliente.getNotificaciones().get(0).getTipo(), 
                    "El tipo de la primera notificación del cliente no es correcto");
        
        assertEquals("Mensaje solo para cliente", cliente.getNotificaciones().get(1).getMensaje(), 
                    "El mensaje de la segunda notificación del cliente no es correcto");
        assertEquals(Notificacion.TipoNotificacion.ADVERTENCIA, cliente.getNotificaciones().get(1).getTipo(), 
                    "El tipo de la segunda notificación del cliente no es correcto");
        
        // -- Prueba 6: Actualización de destinatarios --
        notificacionSoloCliente.setEmpresario(empresario);
        
        // Verificación de cambio de destinatario
        assertEquals(empresario, notificacionSoloCliente.getEmpresario(), "El empresario no se actualizó correctamente");
        assertTrue(notificacionSoloCliente.esParaCliente(), "Debería seguir siendo para cliente");
        assertTrue(notificacionSoloCliente.esParaEmpresario(), "Ahora también debería ser para empresario");
        
        // -- Prueba 7: Verificación de excepciones --
        // Crear notificación con constructor vacío
        Notificacion notificacionVacia = new Notificacion();
        
        // Verificar que lanza excepción al validar sin destinatarios
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            notificacionVacia.validarDestinatario();
        }, "Debería lanzar IllegalStateException al validar sin destinatarios");
        
        assertEquals("Notificación sin destinatario válido", exception.getMessage(), 
                    "El mensaje de la excepción no es el esperado");
        
        // Configurar solo un mensaje y tipo sin destinatarios
        notificacionVacia.setMensaje("Mensaje sin destinatario");
        notificacionVacia.setTipo(Notificacion.TipoNotificacion.INFORMACION);
        
        // Verificar que la notificación se configuró pero sigue sin destinatarios
        assertEquals("Mensaje sin destinatario", notificacionVacia.getMensaje(), "El mensaje no se configuró correctamente");
        assertEquals(Notificacion.TipoNotificacion.INFORMACION, notificacionVacia.getTipo(), "El tipo no se configuró correctamente");
        assertFalse(notificacionVacia.esParaCliente(), "No debería ser para cliente");
        assertFalse(notificacionVacia.esParaEmpresario(), "No debería ser para empresario");
        
        // Ahora sí añadir un destinatario
        notificacionVacia.setCliente(cliente);
        assertTrue(notificacionVacia.esParaCliente(), "Ahora debería ser para cliente");
        assertDoesNotThrow(() -> notificacionVacia.validarDestinatario(), "No debería lanzar excepción con cliente asignado");
        
        // Verificar excepción al crear notificación sin destinatarios
        Exception constructorException = assertThrows(IllegalArgumentException.class, () -> {
            new Notificacion(null, null, "Mensaje inválido", Notificacion.TipoNotificacion.INFORMACION);
        }, "Debería lanzar IllegalArgumentException al crear con ambos destinatarios null");
        
        assertEquals("Debe especificar cliente o empresario", constructorException.getMessage(), 
                    "El mensaje de la excepción del constructor no es el esperado");
                    
        // -- FIN DE AMPLIACIÓN DE PRUEBAS PARA NOTIFICACIONES --

        // Creación del servicio
        Servicio servicio = new Servicio("Servicio A", "Descripción del servicio A", 60, 150, 10, empresa);

        // Creación de la reserva
        Reserva reserva = new Reserva(empresa, cliente, servicio, "2025-03-15", "10:30", "Confirmada");

        // Verificación de relación entre empresa y empresario
        Empresario result = empresa.getEmpresario();
        assertEquals(empresario, result, "El empresario no coincide con el valor esperado");
        assertEquals(nombreEmpresario, result.getNombre(), "El nombre del empresario no es correcto");
        assertEquals(apellidoEmpresario, result.getApellidos(), "El apellido del empresario no es correcto");
        assertEquals(email, result.getEmail(), "El email del empresario no es correcto");
        assertEquals(telefono, result.getTelefono(), "El teléfono del empresario no es correcto");

        // Verificación de los datos de la empresa
        assertEquals(identificadorFiscal, empresa.getIdentificadorFiscal(), "El identificador fiscal de la empresa no coincide");
        assertEquals(nombre, empresa.getNombre(), "El nombre de la empresa no coincide");
        assertEquals(direccion, empresa.getDireccion(), "La dirección de la empresa no coincide");
        assertEquals(telefonoEmpresa, empresa.getTelefono(), "El teléfono de la empresa no coincide");
        assertEquals(emailEmp, empresa.getEmail(), "El email de la empresa no coincide");

        // Verificación de modificación de datos de empresa
        empresa.setTelefono("987654321");
        empresa.setEmail("empresa@xyz.com");
        assertEquals("987654321", empresa.getTelefono(), "El teléfono de la empresa no se ha actualizado correctamente");
        assertEquals("empresa@xyz.com", empresa.getEmail(), "El email de la empresa no se ha actualizado correctamente");

        // Verificación de relaciones en la reserva
        assertEquals("Servicio A", reserva.getServicio().getNombre(), "El nombre del servicio no es correcto");
        assertEquals("Juan", reserva.getCliente().getNombre(), "El nombre del cliente asociado con la reserva no es correcto");
        assertEquals("Empresa XYZ", reserva.getEmpresa().getNombre(), "El nombre de la empresa asociada con la reserva no es correcto");

        // Modificación y verificación del tipo de empresa
        empresa.setTipo(2);
        assertEquals(2, empresa.getTipo(), "El tipo de empresa no se ha actualizado correctamente");

        // Asignación y verificación de reservas a la empresa
        List<Reserva> nuevasReservas = List.of(reserva);
        empresa.setReservas(nuevasReservas);
        assertEquals(1, empresa.getReservas().size(), "La empresa debería tener una reserva asociada");
        assertEquals(reserva, empresa.getReservas().get(0), "La reserva asociada a la empresa no es la correcta");

        // Asignación y verificación de servicios a la empresa
        List<Servicio> nuevosServicios = List.of(servicio);
        empresa.setServicios(nuevosServicios);
        assertEquals(1, empresa.getServicios().size(), "La empresa debería tener un servicio asociado");
        assertEquals(servicio, empresa.getServicios().get(0), "El servicio asociado a la empresa no es el correcto");

        // Asignación y verificación de horarios a la empresa
        List<Horario> horarios = new ArrayList<>();
        horarios.add(new Horario("Lunes-Viernes", LocalTime.of(8, 0), LocalTime.of(12, 0), empresa));
        horarios.add(new Horario("Lunes-Viernes", LocalTime.of(14, 0), LocalTime.of(18, 0), empresa));
        empresa.setHorarios(horarios);

        assertEquals(2, empresa.getHorarios().size(), "La empresa debería tener dos horarios configurados");
        assertEquals("08:00", empresa.getHorarios().get(0).getHorarioInicio().toString(), "El horario de inicio del primer horario no es correcto");
        assertEquals("12:00", empresa.getHorarios().get(0).getHorarioFin().toString(), "El horario de fin del primer horario no es correcto");
        assertEquals("14:00", empresa.getHorarios().get(1).getHorarioInicio().toString(), "El horario de inicio del segundo horario no es correcto");
        assertEquals("18:00", empresa.getHorarios().get(1).getHorarioFin().toString(), "El horario de fin del segundo horario no es correcto");
    }
}