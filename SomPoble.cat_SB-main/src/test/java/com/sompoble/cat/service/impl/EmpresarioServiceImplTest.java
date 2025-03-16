package com.sompoble.cat.service.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Empresario;
import com.sompoble.cat.repository.EmpresarioRepository;
import com.sompoble.cat.service.EmpresarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.dao.EmptyResultDataAccessException;

@SpringBootTest(classes = Application.class)
@Transactional
class EmpresarioServiceImplTest {

    @Autowired
    private EmpresarioService empresarioService; // El servicio que estás probando

    @Autowired
    private EmpresarioRepository empresarioRepository; // Para interactuar con el repositorio directamente

    @Test
    void addEmpresarioTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Sanchez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650123456");
        empresario.setContraseña("pass");
        empresarioService.addEmpresario(empresario);

        Empresario empresarioPersistido = empresarioRepository.findByDNI(empresario.getDni());
        assertNotNull(empresarioPersistido);
        assertEquals(empresario.getDni(), empresarioPersistido.getDni());
    }

    @Test
    void updateEmpresarioTest() {
        // Crear un empresario inicial
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Sanchez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650123456");
        empresario.setContraseña("pass");
        empresarioService.addEmpresario(empresario);

        // Actualizar el empresario
        empresario.setNombre("Fernando");
        empresarioService.updateEmpresario(empresario);

        // Verificar que el empresario ha sido actualizado
        Empresario empresarioActualizado = empresarioRepository.findByDNI(empresario.getDni());
        assertNotNull(empresarioActualizado);
        assertEquals("Fernando", empresarioActualizado.getNombre());
    }

    @Test
    void findByDniTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Sanchez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650123456");
        empresario.setContraseña("pass");
        empresarioService.addEmpresario(empresario);

        // Buscar empresario por DNI
        Empresario result = empresarioService.findByDNI("12345678A");
        assertNotNull(result);
        assertEquals(empresario.getDni(), result.getDni());
    }

    @Test
    void existsByDniTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Sanchez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650123456");
        empresario.setContraseña("pass");
        empresarioService.addEmpresario(empresario);

        // Verificar si el empresario existe por DNI
        boolean result = empresarioService.existsByDni("12345678A");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Sanchez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650123456");
        empresario.setContraseña("pass");
        empresarioService.addEmpresario(empresario);

        // Eliminar el empresario por ID
        empresarioService.deleteById(empresario.getIdPersona());

        // Verificar que el empresario ha sido eliminado
        try {
            Empresario empresarioEliminado = empresarioRepository.findByDNI(empresario.getDni());
            assertNull(empresarioEliminado);
        } catch (EmptyResultDataAccessException e) {
            // Exception is expected because the empresario has been deleted
            assertTrue(true);
        }
    }

    @Test
    void findAllTest() {
        // Crear empresarios
        Empresario empresario1 = new Empresario();
        empresario1.setDni("12345678A");
        empresario1.setNombre("Carlos");
        empresario1.setApellidos("Sanchez Martinez");
        empresario1.setEmail("carlos@empresa.com");
        empresario1.setTelefono("650123456");
        empresario1.setContraseña("pass");
        empresarioService.addEmpresario(empresario1);

        Empresario empresario2 = new Empresario();
        empresario2.setDni("87654321B");
        empresario2.setNombre("Maria");
        empresario2.setApellidos("Lopez Garcia");
        empresario2.setEmail("maria@empresa.com");
        empresario2.setTelefono("650123457");
        empresario2.setContraseña("pass");
        empresarioService.addEmpresario(empresario2);

        // Verificar que ambos empresarios están presentes
        List<Empresario> empresarios = empresarioService.findAll();
        assertNotNull(empresarios);
        assertEquals(2, empresarios.size());
    }

    @Test
    void existsByIdTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Sanchez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650123456");
        empresario.setContraseña("pass");
        empresarioService.addEmpresario(empresario);

        // Verificar si el empresario existe por ID
        boolean result = empresarioService.existsById(empresario.getIdPersona());
        assertTrue(result);
    }

    @Test
    void deleteByDniTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Sanchez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650123456");
        empresario.setContraseña("pass");
        empresarioService.addEmpresario(empresario);

        // Eliminar el empresario por DNI
        empresarioService.deleteByDni(empresario.getDni());

        // Verificar que el empresario ha sido eliminado
        try {
            Empresario empresarioEliminado = empresarioRepository.findByDNI(empresario.getDni());
            assertNull(empresarioEliminado);
        } catch (EmptyResultDataAccessException e) {
            // Exception is expected because the empresario has been deleted
            assertTrue(true);
        }
    }

    @Test
    void existsByEmailTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Sanchez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650123456");
        empresario.setContraseña("pass");
        empresarioService.addEmpresario(empresario);

        // Verificar si el empresario existe por email
        boolean result = empresarioService.existsByEmail("carlos@empresa.com");
        assertTrue(result);
    }
}
