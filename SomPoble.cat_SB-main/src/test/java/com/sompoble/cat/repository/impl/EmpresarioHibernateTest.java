package com.sompoble.cat.repository.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Empresario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@Transactional
class EmpresarioHibernateTest {

    @Autowired
    private EmpresarioHibernate empresarioHibernate; // El repositorio que estás probando

    @Autowired
    private EntityManager entityManager; // Para interactuar directamente con la base de datos

    @Test
    void addEmpresarioTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario);

        Empresario empresarioPersistido = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNotNull(empresarioPersistido);
        assertEquals(empresario.getDni(), empresarioPersistido.getDni());
    }

    @Test
    void updateEmpresarioTest() {
        // Crear un empresario inicial
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario);

        // Actualizar el empresario
        empresario.setNombre("Jose");
        empresarioHibernate.updateEmpresario(empresario);

        // Verificar que el empresario ha sido actualizado
        Empresario empresarioActualizado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNotNull(empresarioActualizado);
        assertEquals("Jose", empresarioActualizado.getNombre());
    }

    @Test
    void findByDniTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario);

        // Buscar empresario por DNI
        Empresario result = empresarioHibernate.findByDNI("12345678A");
        assertNotNull(result);
        assertEquals(empresario.getDni(), result.getDni());
    }

    @Test
    void existsByDniTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario);

        // Verificar si el empresario existe por DNI
        boolean result = empresarioHibernate.existsByDni("12345678A");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario);

        // Eliminar el empresario por ID
        empresarioHibernate.deleteById(empresario.getIdPersona());

        // Verificar que el empresario ha sido eliminado
        Empresario empresarioEliminado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNull(empresarioEliminado);
    }

    @Test
    void findAllTest() {
        // Crear empresarios
        Empresario empresario1 = new Empresario();
        empresario1.setDni("12345678A");
        empresario1.setNombre("Carlos");
        empresario1.setApellidos("Lopez Martinez");
        empresario1.setEmail("carlos@empresa.com");
        empresario1.setTelefono("650180800");
        empresario1.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario1);

        Empresario empresario2 = new Empresario();
        empresario2.setDni("87654321B");
        empresario2.setNombre("Jose");
        empresario2.setApellidos("Garcia Ruiz");
        empresario2.setEmail("jose@empresa.com");
        empresario2.setTelefono("650180801");
        empresario2.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario2);

        // Verificar que ambos empresarios están presentes
        List<Empresario> empresarios = empresarioHibernate.findAll();
        assertNotNull(empresarios);
        assertEquals(2, empresarios.size());
    }

    @Test
    void existsByIdTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario);

        // Verificar si el empresario existe por ID
        boolean result = empresarioHibernate.existsById(empresario.getIdPersona());
        assertTrue(result);
    }

    @Test
    void deleteByDniTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario);

        // Eliminar el empresario por DNI
        empresarioHibernate.deleteByDni(empresario.getDni());

        // Verificar que el empresario ha sido eliminado
        Empresario empresarioEliminado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNull(empresarioEliminado);
    }

    @Test
    void existsByEmailTest() {
        // Crear un empresario
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        empresarioHibernate.addEmpresario(empresario);

        // Verificar si el cliente existe por email
        boolean result = empresarioHibernate.existsByEmail("carlos@empresa.com");
        assertTrue(result);

        // Verificar que un email no registrado no existe
        boolean resultNoExist = empresarioHibernate.existsByEmail("noexistente@sergio.es");
        assertFalse(resultNoExist);
    }
}