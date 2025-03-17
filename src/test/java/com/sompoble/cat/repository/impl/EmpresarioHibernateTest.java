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
    private EmpresarioHibernate empresarioHibernate; 

    @Autowired
    private EntityManager entityManager;

    @Test
    void addEmpresarioTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        Empresario empresarioPersistido = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNotNull(empresarioPersistido);
        assertEquals(empresario.getDni(), empresarioPersistido.getDni());
    }

    @Test
    void updateEmpresarioTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        empresario.setNombre("Jose");
        empresarioHibernate.updateEmpresario(empresario);

        Empresario empresarioActualizado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNotNull(empresarioActualizado);
        assertEquals("Jose", empresarioActualizado.getNombre());
    }

    @Test
    void findByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        Empresario result = empresarioHibernate.findByDNI("12345678A");
        assertNotNull(result);
        assertEquals(empresario.getDni(), result.getDni());
    }

    @Test
    void existsByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        boolean result = empresarioHibernate.existsByDni("12345678A");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        empresarioHibernate.deleteById(empresario.getIdPersona());

        Empresario empresarioEliminado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNull(empresarioEliminado);
    }

    @Test
    void findAllTest() {
        Empresario empresario1 = new Empresario();
        empresario1.setDni("12345678A");
        empresario1.setNombre("Carlos");
        empresario1.setApellidos("Lopez Martinez");
        empresario1.setEmail("carlos@empresa.com");
        empresario1.setTelefono("650180800");
        empresario1.setPass("pass");
        empresarioHibernate.addEmpresario(empresario1);

        Empresario empresario2 = new Empresario();
        empresario2.setDni("87654321B");
        empresario2.setNombre("Jose");
        empresario2.setApellidos("Garcia Ruiz");
        empresario2.setEmail("jose@empresa.com");
        empresario2.setTelefono("650180801");
        empresario2.setPass("pass");
        empresarioHibernate.addEmpresario(empresario2);

        List<Empresario> empresarios = empresarioHibernate.findAll();
        assertNotNull(empresarios);
        assertEquals(2, empresarios.size());
    }

    @Test
    void existsByIdTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        boolean result = empresarioHibernate.existsById(empresario.getIdPersona());
        assertTrue(result);
    }

    @Test
    void deleteByDniTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        empresarioHibernate.deleteByDni(empresario.getDni());

        Empresario empresarioEliminado = entityManager.find(Empresario.class, empresario.getIdPersona());
        assertNull(empresarioEliminado);
    }

    @Test
    void existsByEmailTest() {
        Empresario empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setPass("pass");
        empresarioHibernate.addEmpresario(empresario);

        boolean result = empresarioHibernate.existsByEmail("carlos@empresa.com");
        assertTrue(result);

        boolean resultNoExist = empresarioHibernate.existsByEmail("noexistente@sergio.es");
        assertFalse(resultNoExist);
    }
}