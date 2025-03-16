package com.sompoble.cat.repository.impl;

import com.sompoble.cat.Application;
import com.sompoble.cat.domain.Empresa;
import com.sompoble.cat.domain.Empresario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest(classes = Application.class)
@Transactional
class EmpresaHibernateTest {

    @Autowired
    private EmpresaHibernate empresaHibernate; // El repositorio que estás probando

    @Autowired
    private EntityManager entityManager; // Para interactuar directamente con la base de datos
    
    private Empresario empresario;
    
    @BeforeEach
    void setUp() {
        // Crear un nuevo empresario antes de cada prueba
        empresario = new Empresario();
        empresario.setDni("12345678A");
        empresario.setNombre("Carlos");
        empresario.setApellidos("Lopez Martinez");
        empresario.setEmail("carlos@empresa.com");
        empresario.setTelefono("650180800");
        empresario.setContraseña("pass");
        
        entityManager.persist(empresario);
        entityManager.flush();
    }

    
    @Test
    void addEmpresaTest() {
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        Empresa empresaPersistida = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNotNull(empresaPersistida);
        assertEquals(empresa.getCif(), empresaPersistida.getCif());
    }

    @Test
    void updateEmpresaTest() {
        // Crear una empresa inicial
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        // Actualizar la empresa
        empresa.setNombre("Nueva Empresa S.A.");
        empresaHibernate.updateEmpresa(empresa);

        // Verificar que la empresa ha sido actualizada
        Empresa empresaActualizada = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNotNull(empresaActualizada);
        assertEquals("Nueva Empresa S.A.", empresaActualizada.getNombre());
    }

    @Test
    void findByCifTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        // Buscar empresa por CIF
        Empresa result = empresaHibernate.findByCif("A12345678");
        assertNotNull(result);
        assertEquals(empresa.getCif(), result.getCif());
    }

    @Test
    void existsByCifTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        // Verificar si la empresa existe por CIF
        boolean result = empresaHibernate.existsByCif("A12345678");
        assertTrue(result);
    }

    @Test
    void deleteByIdTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        // Eliminar la empresa por ID
        empresaHibernate.deleteById(empresa.getIdEmpresa());

        // Verificar que la empresa ha sido eliminada
        Empresa empresaEliminada = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNull(empresaEliminada);
    }

    @Test
    void findAllTest() {
        // Crear empresas
        Empresa empresa1 = new Empresa();
        empresa1.setCif("A12345678");
        empresa1.setNombre("Empresa S.A.");
        empresa1.setDireccion("Calle Falsa, 123");
        empresa1.setTelefono("123456789");
        empresa1.setEmail("contacto@empresa.com");
        empresa1.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa1);

        Empresa empresa2 = new Empresa();
        empresa2.setCif("B98765432");
        empresa2.setNombre("Otra Empresa S.A.");
        empresa2.setDireccion("Calle Verdadera, 456");
        empresa2.setTelefono("987654321");
        empresa2.setEmail("contacto2@empresa.com");
        empresa2.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa2);

        // Verificar que ambas empresas están presentes
        List<Empresa> empresas = empresaHibernate.findAll();
        assertNotNull(empresas);
        assertEquals(2, empresas.size());
    }

    @Test
    void existsByIdTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        // Verificar si la empresa existe por ID
        boolean result = empresaHibernate.existsById(empresa.getIdEmpresa());
        assertTrue(result);
    }

    @Test
    void deleteByCifTest() {
        // Crear una empresa
        Empresa empresa = new Empresa();
        empresa.setCif("A12345678");
        empresa.setNombre("Empresa S.A.");
        empresa.setDireccion("Calle Falsa, 123");
        empresa.setTelefono("123456789");
        empresa.setEmail("contacto@empresa.com");
        empresa.setEmpresario(empresario);
        empresaHibernate.addEmpresario(empresa);

        // Eliminar la empresa por CIF
        empresaHibernate.deleteByCif("A12345678");

        // Verificar que la empresa ha sido eliminada
        Empresa empresaEliminada = entityManager.find(Empresa.class, empresa.getIdEmpresa());
        assertNull(empresaEliminada);
    }
}