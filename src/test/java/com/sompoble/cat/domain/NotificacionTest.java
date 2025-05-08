package com.sompoble.cat.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificacionTest {

    private Cliente clienteMock;
    private Empresario empresarioMock;

    @BeforeEach
    void setUp() {
        clienteMock = new Cliente(); 
        empresarioMock = new Empresario();
    }

    @Test
    void testCrearNotificacionConCliente() {
        Notificacion notificacion = new Notificacion(clienteMock, null, "Mensaje prueba", Notificacion.TipoNotificacion.INFORMACION);
        assertEquals("Mensaje prueba", notificacion.getMensaje());
        assertTrue(notificacion.esParaCliente());
        assertFalse(notificacion.esParaEmpresario());
    }

    @Test
    void testCrearNotificacionConEmpresario() {
        Notificacion notificacion = new Notificacion(null, empresarioMock, "Mensaje empresario", Notificacion.TipoNotificacion.ADVERTENCIA);
        assertEquals(Notificacion.TipoNotificacion.ADVERTENCIA, notificacion.getTipo());
        assertTrue(notificacion.esParaEmpresario());
        assertFalse(notificacion.esParaCliente());
    }

    @Test
    void testCrearNotificacionSinDestinatarioLanzaExcepcion() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Notificacion(null, null, "Mensaje inválido", Notificacion.TipoNotificacion.ERROR)
        );
        assertEquals("Debe especificar cliente o empresario", exception.getMessage());
    }

    @Test
    void testValidarDestinatarioValido() {
        Notificacion notificacion = new Notificacion(clienteMock, null, "Mensaje válido", Notificacion.TipoNotificacion.INFORMACION);
        assertDoesNotThrow(notificacion::validarDestinatario);
    }

    @Test
    void testValidarDestinatarioInvalidoLanzaExcepcion() {
        Notificacion notificacion = new Notificacion();
        Exception exception = assertThrows(IllegalStateException.class, notificacion::validarDestinatario);
        assertEquals("Notificación sin destinatario válido", exception.getMessage());
    }

    @Test
    void testSettersYGetters() {
        Notificacion notificacion = new Notificacion(clienteMock, null, "Mensaje", Notificacion.TipoNotificacion.ERROR);
        notificacion.setMensaje("Nuevo mensaje");
        notificacion.setTipo(Notificacion.TipoNotificacion.ADVERTENCIA);
        notificacion.setEmpresario(empresarioMock);
        notificacion.setCliente(null);

        assertEquals("Nuevo mensaje", notificacion.getMensaje());
        assertEquals(Notificacion.TipoNotificacion.ADVERTENCIA, notificacion.getTipo());
        assertFalse(notificacion.esParaCliente());
        assertTrue(notificacion.esParaEmpresario());
    }
}
